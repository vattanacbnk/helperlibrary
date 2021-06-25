/*
 * Copyright (c) 2021. Piseysen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.base
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected open fun unSubscribeViewModel() {

    }

}