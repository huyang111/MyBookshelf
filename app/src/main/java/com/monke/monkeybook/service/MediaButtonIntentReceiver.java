package com.monke.monkeybook.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.hwangjr.rxbus.RxBus;
import com.monke.monkeybook.BuildConfig;
import com.monke.monkeybook.help.RxBusTag;

/**
 * Created by GKF on 2018/1/6.
 * 监听耳机键
 */

public class MediaButtonIntentReceiver extends BroadcastReceiver {
    public static final String TAG = MediaButtonIntentReceiver.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static boolean handleIntent(final Context context, final Intent intent) {
        if (DEBUG) Log.d(TAG, "Received intent: " + intent);
        final String intentAction = intent.getAction();
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return false;
            }

            final int keycode = event.getKeyCode();
            final int action = event.getAction();

            String command = null;
            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    command = ReadAloudService.ActionMediaButton;
                    break;
                default:
                    break;
            }
            if (command != null) {
                if (action == KeyEvent.ACTION_DOWN) {
                    RxBus.get().post(RxBusTag.MEDIA_BUTTON, command);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (handleIntent(context, intent) && isOrderedBroadcast()) {
            abortBroadcast();
        }
    }
}
