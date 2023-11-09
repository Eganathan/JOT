package net.eknath.jot

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class JotApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}