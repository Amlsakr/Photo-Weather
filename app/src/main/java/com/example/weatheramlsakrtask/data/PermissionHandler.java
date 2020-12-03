package com.example.weatheramlsakrtask.data;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.example.weatheramlsakrtask.R;
import com.google.android.material.snackbar.Snackbar;

public class PermissionHandler {
    // Used in checking for runtime permissions.
    public static final int REQUEST_PERMISSIONS_LOCATION_CODE = 34;
    public static final int REQUEST_PERMISSIONS_WRITE_STORAGE_CODE = 35;
    public static final int REQUEST_PERMISSIONS_READ_STORAGE_CODE = 36;
    private static final String TAG = PermissionHandler.class.getSimpleName();
    private Activity activity;

    public PermissionHandler(Activity activity) {
        this.activity = activity;
    }

    /**
     * Returns the current state of the permissions needed.
     */

    public boolean checkLocationPermission() {
        if (checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return true;
        } else {
            return false;
        }
    }

    public void requestLocationPermission() {
        requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSIONS_LOCATION_CODE);
    }

    public boolean checkReadFromStoragePermission() {
        if (checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return true;
        } else {
            return false;
        }
    }

    public void requestReadFromStoragePermission() {
        requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSIONS_READ_STORAGE_CODE);
    }


    public boolean checkWriteToStoragePermission() {
        if (checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return true;
        } else {
            return false;
        }
    }

    public void requestWriteToStoragePermission() {
        requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSIONS_WRITE_STORAGE_CODE);
    }

    private boolean checkPermissions(String permission) {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(activity,
                //   Manifest.permission.ACCESS_FINE_LOCATION
                permission);
    }

    private void requestPermissions(String permission, int requestCode) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        permission);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    activity.findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{permission},
                                    requestCode);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
        }
    }
}
