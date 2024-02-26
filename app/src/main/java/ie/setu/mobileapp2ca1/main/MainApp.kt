package ie.setu.mobileapp2ca1.main

import android.app.Application
import ie.setu.mobileapp2ca1.models.RunningMemStore
import ie.setu.mobileapp2ca1.models.RunningModel
import ie.setu.mobileapp2ca1.models.RunningStore
import ie.setu.mobileapp2ca1.models.RunningJSONStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var runningTracks: RunningStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        runningTracks = RunningJSONStore(applicationContext)
        i("Running App Started")
        //runningTracks.runningTracks.add((RunningModel(10,"2","3")))
    }
}