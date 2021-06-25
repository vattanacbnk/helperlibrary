/*
 * Copyright (c) 2021. Vattanac Bank.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.interfaces;
import android.content.Intent;

import com.vb.helperlibrary.utils.CallbackManagerImpl;


public interface CallbackManager {

    /**
     * The method that should be called from the Activity's or Fragment's onActivityResult method.
     *
     * @param requestCode The request code that's received by the Activity or Fragment.
     * @param resultCode The result code that's received by the Activity or Fragment.
     * @param data The result data that's received by the Activity or Fragment.
     * @return true If the result could be handled.
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data);


    public static class Factory {
        public static CallbackManager create() {
            return new CallbackManagerImpl();
        }
    }
}