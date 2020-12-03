package com.example.weatheramlsakrtask.data.storage;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;

import com.example.weatheramlsakrtask.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadImage {

    public static List<Uri> getImages(Activity activity) {
        List<String> fileNames = null;
        List<Uri> imageUris = new ArrayList<>();
        File path = new File(Environment.getExternalStorageDirectory().toString() + '/' + activity.getString(R.string.app_name));
        if (path.exists()) {
            fileNames = Arrays.asList(path.list());
        }
        if (fileNames != null) {
            for (int i = 0; i < fileNames.size(); i++) {

                ///Now set this bitmap on imageview
                imageUris.add(Uri.parse(path + "/" + fileNames.get(i)));
            }
        }
        return imageUris;
    }
}
