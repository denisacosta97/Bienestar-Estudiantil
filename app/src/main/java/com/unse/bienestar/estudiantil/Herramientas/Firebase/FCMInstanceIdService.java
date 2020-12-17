package com.unse.bienestar.estudiantil.Herramientas.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.unse.bienestar.estudiantil.Herramientas.Almacenamiento.PreferenceManager;
import com.unse.bienestar.estudiantil.Herramientas.Utils;

public class FCMInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = FCMInstanceIdService.class.getSimpleName();

    public FCMInstanceIdService() {
    }

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + fcmToken);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager != null)preferenceManager.setValue(Utils.TOKEN_FIREBASE, fcmToken);

        //sendTokenToServer(fcmToken);
    }


}