package com.hippoddung.ribbit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class RibbitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

