package com.example.weatheramlsakrtask;

import android.app.Application;

public class App extends Application {

//    // A reference to the service used to get location updates.
//    private LocationUpdatesService mService = null;
//    // Tracks the bound state of the service.
//    private boolean mBound = false;
//    // Monitors the state of the connection to the service.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
//            mService = binder.getService();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mService = null;
//            mBound = false;
//        }
//    };

    @Override
    public void onCreate() {
        super.onCreate();

//        if (!permissionHandler.checkLocationPermission()) {
//            permissionHandler.requestLocationPermission();
//        } else {
//            if (mService != null)
//                mService.requestLocationUpdates();
//        }
//
//        // Bind to the service. If the service is in foreground mode, this signals to the service
//        // that since this activity is in the foreground, the service can exit foreground mode.
//        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                Context.BIND_AUTO_CREATE);
    }


}
