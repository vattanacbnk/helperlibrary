/*
 * Copyright (c) 2021. Vattanac Bank.
 * @Created by piseysen(IT Application) on 08/02/2021 5:36 PM
 */

package com.vb.helperlibrary.session

import android.content.Context
import android.content.SharedPreferences


public abstract class MySessionManager protected constructor(context: Context) {
    private val mShare: SharedPreferences
    private val mEditor: SharedPreferences.Editor

    init {
        mShare = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        mEditor = mShare.edit()
    }

    fun saveData(key: String, value: Int) {
        mEditor.putInt(key, value)
        mEditor.apply()
    }

    fun saveData(key: String, value: Boolean) {
        mEditor.putBoolean(key, value)
        mEditor.apply()
    }

    fun saveData(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.apply()
    }

    fun getData(key: String, def: String): String? {
        return mShare.getString(key, def)
    }

    fun getData(key: String, def: Int): Int {
        return mShare.getInt(key, def)
    }

    fun getData(key: String, def: Boolean): Boolean {
        return mShare.getBoolean(key, def)
    }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        mShare.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        mShare.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun clear() {
        mEditor.clear()
        mEditor.apply()
    }

    companion object {
       const val MyPREFERENCES = "VattanacBankMobileSDKPrefs"
    }

}
