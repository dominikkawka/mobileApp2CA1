package ie.setu.mobileapp2ca1.main

import android.app.Application
import ie.setu.mobileapp2ca1.models.RunningModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    var runningTracks = ArrayList<RunningModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Running App Started")
        //runningTracks.add((RunningModel("1","2")))
    }
}