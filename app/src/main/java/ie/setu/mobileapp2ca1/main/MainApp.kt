package ie.setu.mobileapp2ca1.main

import android.app.Application
import ie.setu.mobileapp2ca1.models.RunningMemStore
import ie.setu.mobileapp2ca1.models.RunningModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val runningTracks = RunningMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Running App Started")
        //runningTracks.runningTracks.add((RunningModel(10,"2","3")))
    }
}