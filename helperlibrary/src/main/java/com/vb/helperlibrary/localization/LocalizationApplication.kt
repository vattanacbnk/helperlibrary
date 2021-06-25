package com.vb.helperlibrary.localization

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import java.util.*

abstract class LocalizationApplication : Application() {
    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, getDefaultLanguage())
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    abstract fun getDefaultLanguage(): Locale
}
