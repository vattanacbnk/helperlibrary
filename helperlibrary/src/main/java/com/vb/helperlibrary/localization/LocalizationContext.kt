/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */


package com.vb.helperlibrary.localization

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import com.vb.helperlibrary.localization.LanguageSetting


class LocalizationContext(base: Context) : ContextWrapper(base) {
    override fun getResources(): Resources {
        val locale = LanguageSetting.getLanguageWithDefault(this, LanguageSetting.getDefaultLanguage(this))
        val configuration = super.getResources().configuration

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> configuration.setLocales(LocaleList(locale))
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> configuration.setLocale(locale)
            else -> {
                @Suppress("DEPRECATION")
                configuration.locale = locale
            }
        }
        val metrics: DisplayMetrics = super.getResources().displayMetrics
        @Suppress("DEPRECATION")
        return Resources(assets, metrics, configuration)
    }
}
