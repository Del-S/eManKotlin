package cz.eman.test

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowManager

/**
 * Application extension only to initiate Database Flow.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
    }
    override fun onTerminate() {
        super.onTerminate()
        FlowManager.destroy()
    }
}