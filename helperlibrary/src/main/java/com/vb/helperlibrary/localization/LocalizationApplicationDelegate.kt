/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.localization

import android.content.Context
import java.util.*


class LocalizationApplicationDelegate {

    fun onConfigurationChanged(context: Context) =
        LocalizationUtility.applyLocalizationContext(context)

    fun attachBaseContext(context: Context): Context =
        LocalizationUtility.applyLocalizationContext(context)

    fun getApplicationContext(applicationContext: Context): Context =
        LocalizationUtility.applyLocalizationContext(applicationContext)

    fun setDefaultLanguage(context: Context, language: String) {
        val locale = Locale(language)
        setDefaultLanguage(context, locale)
    }

    fun setDefaultLanguage(context: Context, language: String, country: String) {
        val locale = Locale(language, country)
        setDefaultLanguage(context, locale)
    }

    fun setDefaultLanguage(context: Context, locale: Locale) {
        LanguageSetting.setDefaultLanguage(context, locale)
    }
}
