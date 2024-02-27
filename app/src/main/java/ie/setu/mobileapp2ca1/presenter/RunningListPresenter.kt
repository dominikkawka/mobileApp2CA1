package ie.setu.mobileapp2ca1.presenter

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.mobileapp2ca1.view.RunningListView
import ie.setu.mobileapp2ca1.view.RunningView
import ie.setu.mobileapp2ca1.main.MainApp
import ie.setu.mobileapp2ca1.models.RunningModel

class RunningListPresenter(val view: RunningListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getTracks() = app.runningTracks.findAll()

    fun doAddTrack() {
        val launcherIntent = Intent(view, RunningView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditTrack(track: RunningModel, pos: Int) {
        val launcherIntent = Intent(view, RunningView::class.java)
        launcherIntent.putExtra("track_edit", track)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) view.onRefresh()
                else // Deleting
                    if (it.resultCode == 99) view.onDelete(position)
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher = view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {  }
    }
}