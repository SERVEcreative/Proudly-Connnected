package com.servecreative.myapplication.sendbird.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sendbird.calls.SendBirdCall;
import com.sendbird.calls.SendBirdException;
import com.servecreative.myapplication.sendbird.BaseApplication;

public class PushUtils {

    public interface GetPushTokenHandler {
        void onResult(String token, SendBirdException e);
    }

//    public static void getPushToken(Context context, final GetPushTokenHandler handler) {
//        Log.i(BaseApplication.TAG, "[PushUtils] getPushToken()");
//
//        String savedToken = PrefUtils.getPushToken(context);
//        if (TextUtils.isEmpty(savedToken)) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//                if (!task.isSuccessful()) {
//                    Log.i(BaseApplication.TAG, "[PushUtils] getPushToken() => getInstanceId failed", task.getException());
//                    if (handler != null) {
//                        handler.onResult(null, new SendBirdException((task.getException() != null ? task.getException().getMessage() : "")));
//                    }
//                    return;
//                }
//
//                String pushToken = (task.getResult() != null ? task.getResult().getToken() : "");
//                Log.i(BaseApplication.TAG, "[PushUtils] getPushToken() => pushToken: " + pushToken);
//                if (handler != null) {
//                    handler.onResult(pushToken, null);
//                }
//            });
//        } else {
//            Log.i(BaseApplication.TAG, "[PushUtils] savedToken: " + savedToken);
//            if (handler != null) {
//                handler.onResult(savedToken, null);
//            }
//        }
//    }

    public static void getPushToken(Context context, final GetPushTokenHandler handler) {

        try {
            Log.i(BaseApplication.TAG, "[PushUtils] getPushToken()");
            String savedToken = PrefUtils.getPushToken(context);
            if (TextUtils.isEmpty(savedToken)) {

                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
                    String pushToken = (s != null ? s : "");
                    Log.i(BaseApplication.TAG, "[PushUtils] getPushToken() => pushToken: " + pushToken);
                    if (handler != null) {
                        handler.onResult(pushToken, null);
                    }

                }).addOnFailureListener(e -> {
                    Log.i(BaseApplication.TAG, "[PushUtils] getPushToken() => getInstanceId failed", e);
                    if (handler != null) {
                        handler.onResult(null, new SendBirdException(e.getMessage()));
                    }
                });
            } else {
                Log.i(BaseApplication.TAG, "[PushUtils] savedToken: " + savedToken);
                if (handler != null) {
                    handler.onResult(savedToken, null);
                }
            }

        }
        catch (Exception e)
        {
            Log.e("PushUtils_getPushToken",e.toString());
        }

    }


    public interface PushTokenHandler {
        void onResult(SendBirdException e);
    }

    public static void registerPushToken(Context context, String pushToken, PushTokenHandler handler) {
        Log.i(BaseApplication.TAG, "[PushUtils] registerPushToken(pushToken: " + pushToken + ")");

        SendBirdCall.registerPushToken(pushToken, false, e -> {
            if (e != null) {
                Log.i(BaseApplication.TAG, "[PushUtils] registerPushToken() => e: " + e.getMessage());
                PrefUtils.setPushToken(context, pushToken);

                if (handler != null) {
                    handler.onResult(e);
                }
                return;
            }

            Log.i(BaseApplication.TAG, "[PushUtils] registerPushToken() => OK");
            PrefUtils.setPushToken(context, pushToken);

            if (handler != null) {
                handler.onResult(null);
            }
        });
    }
}
