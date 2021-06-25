/*
 * Copyright (c) 2021. Vattanac Bank.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.utils;

import android.content.Intent;


import com.vb.helperlibrary.interfaces.CallbackManager;

import java.util.HashMap;
import java.util.Map;


public final class CallbackManagerImpl implements CallbackManager {
    private static Map<Integer, Callback> staticCallbacks = new HashMap<>();

    /**
     * If there is no explicit callback, but we still need to call the Facebook component, because
     * it's going to update some state, e.g., login, like. Then we should register a static callback
     * that can still handle the response.
     *
     * @param requestCode The request code.
     * @param callback    The callback for the feature.
     */
    public static synchronized void registerStaticCallback(int requestCode, Callback callback) {
        Validate.notNull(callback, "callback");
        if (staticCallbacks.containsKey(requestCode)) {
            return;
        }
        staticCallbacks.put(requestCode, callback);
    }

    private static synchronized Callback getStaticCallback(Integer requestCode) {
        return staticCallbacks.get(requestCode);
    }

    private static boolean runStaticCallback(int requestCode, int resultCode, Intent data) {
        Callback callback = getStaticCallback(requestCode);
        if (callback != null) {
            return callback.onActivityResult(resultCode, data);
        }
        return false;
    }

    private Map<Integer, Callback> callbacks = new HashMap<>();

    public void registerCallback(int requestCode, Callback callback) {
        Validate.notNull(callback, "callback");
        callbacks.put(requestCode, callback);
    }

    public void unregisterCallback(int requestCode) {
        callbacks.remove(requestCode);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Callback callback = callbacks.get(requestCode);
        if (callback != null) {
            return callback.onActivityResult(resultCode, data);
        }
        return runStaticCallback(requestCode, resultCode, data);
    }

    public interface Callback {
        public boolean onActivityResult(int resultCode, Intent data);
    }

}
