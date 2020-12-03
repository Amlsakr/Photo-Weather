package com.example.weatheramlsakrtask.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatheramlsakrtask.R;
import com.example.weatheramlsakrtask.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding activityDetailsBinding;
    Uri uri;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = activityDetailsBinding.getRoot();
        setContentView(view);
        uri = Uri.parse(getIntent().getStringExtra("uri"));
        activityDetailsBinding.detailsimageView.setImageURI(uri);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    public void shareImageView(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            activityDetailsBinding.detailsimageView.setScaleX(scaleFactor);
            activityDetailsBinding.detailsimageView.setScaleY(scaleFactor);
            return true;
        }
    }
}