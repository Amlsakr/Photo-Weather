package com.example.weatheramlsakrtask.view;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.weatheramlsakrtask.R;
import com.example.weatheramlsakrtask.data.PermissionHandler;
import com.example.weatheramlsakrtask.data.storage.WriteImage;
import com.example.weatheramlsakrtask.data.weatherResponceModel.Response;
import com.example.weatheramlsakrtask.databinding.ActivityMainBinding;
import com.example.weatheramlsakrtask.view.adapter.MainAdapter;
import com.example.weatheramlsakrtask.view.adapter.RecyclerViewItemClickListener;
import com.example.weatheramlsakrtask.viewModel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.weatheramlsakrtask.data.PermissionHandler.REQUEST_PERMISSIONS_LOCATION_CODE;
import static com.example.weatheramlsakrtask.data.PermissionHandler.REQUEST_PERMISSIONS_READ_STORAGE_CODE;
import static com.example.weatheramlsakrtask.data.PermissionHandler.REQUEST_PERMISSIONS_WRITE_STORAGE_CODE;
import static com.example.weatheramlsakrtask.data.storage.ReadImage.getImages;


public class MainActivity extends AppCompatActivity implements RecyclerViewItemClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    protected Location mLastLocation;
    ActivityMainBinding binding;
    List<Uri> imageUries = new ArrayList<Uri>();
    LocationRequest locationRequest;
    private MainViewModel mainViewModel;
    private String placeName;
    private double temperature;
    private String condition;
    private MainAdapter mainAdapter;
    private PermissionHandler permissionHandler;
    private WriteImage writeImage;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initObjects();
        if (!permissionHandler.checkLocationPermission()) {
            permissionHandler.requestLocationPermission();
        } else {
            getLastLocation();
        }
    }

    private void initObjects() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        permissionHandler = new PermissionHandler(this);
        writeImage = new WriteImage(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    mLastLocation = location;
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                }
            }
        };

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
        if (!permissionHandler.checkWriteToStoragePermission()) {
            permissionHandler.requestWriteToStoragePermission();
        } else {
            imageUries = getImages(this);
            mainAdapter = new MainAdapter(imageUries, this);
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            binding.recyclerView.setAdapter(mainAdapter);
            mainAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_LOCATION_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
            } else {
                // Permission denied.
                showSnackbar(getString(R.string.no_location_detected));
            }
        } else if (requestCode == REQUEST_PERMISSIONS_WRITE_STORAGE_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "Capture Image was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission denied.
                showSnackbar(getString(R.string.no_write_storage_permission_detected));
            }
        } else if (requestCode == REQUEST_PERMISSIONS_READ_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageUries = getImages(this);
                mainAdapter = new MainAdapter(imageUries, this);
                binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                binding.recyclerView.setAdapter(mainAdapter);
                mainAdapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public void takePhoto(View view) {
        if (permissionHandler.checkLocationPermission()) {
            startLocationUpdates();
            getLastLocation();
            Call<Response> call = mainViewModel.getData(latitude, longitude);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    placeName = response.body().getName();
                    temperature = response.body().getMain().getTemp();
                    condition = response.body().getWeather().get(0).getDescription();
                    Log.e(TAG, placeName + temperature + condition);
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {

                }
            });


            if (!permissionHandler.checkWriteToStoragePermission()) {
                permissionHandler.requestWriteToStoragePermission();
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap bitmap = writeImage.writeTextOnDrawable(imageBitmap, placeName, temperature, condition);
            writeImage.saveImage(bitmap);

        }
    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("uri", imageUries.get(position).toString());
        startActivity(intent);
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();
                            Log.e(TAG, "lat" + longitude + "lon" + latitude);
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.activity_main);
        if (container != null) {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    text,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.settings, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    "com.example.weatheramlsakrtask", null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }
}



