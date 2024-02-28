package ie.setu.mobileapp2ca1.presenter

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.mobileapp2ca1.view.EditStartLocationView
import ie.setu.mobileapp2ca1.view.RunningView
import ie.setu.mobileapp2ca1.databinding.ActivityRunningBinding
import ie.setu.mobileapp2ca1.helpers.showImagePicker
import ie.setu.mobileapp2ca1.main.MainApp
import ie.setu.mobileapp2ca1.models.Location
import ie.setu.mobileapp2ca1.models.RunningModel
import timber.log.Timber


class RunningPresenter(private val view: RunningView) {

    var runningTrack = RunningModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityRunningBinding = ActivityRunningBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("track_edit")) {
            edit = true
            runningTrack = view.intent.extras?.getParcelable("track_edit")!!
            view.showTrack(runningTrack)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSave(title: String, description: String, difficulty: Int) {
        runningTrack.title = title
        runningTrack.description = description
        runningTrack.difficulty = difficulty
        if (edit) {
            app.runningTracks.update(runningTrack)
        } else {
            app.runningTracks.create(runningTrack)
        }
        view.setResult(RESULT_OK)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        view.setResult(99)
        app.runningTracks.delete(runningTrack)
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher,view)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (runningTrack.startZoom != 0f) {
            location.lat =  runningTrack.startLat
            location.lng = runningTrack.startLng
            location.zoom = runningTrack.startZoom
        }
        val launcherIntent = Intent(view, EditStartLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheTrack (title: String, description: String, difficulty: Int) {
        runningTrack.title = title;
        runningTrack.description = description
        runningTrack.difficulty = difficulty
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            runningTrack.image = result.data!!.data!!
                            view.contentResolver.takePersistableUriPermission(runningTrack.image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            view.updateImage(runningTrack.image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }            }    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            runningTrack.startLat = location.lat
                            runningTrack.startLng = location.lng
                            runningTrack.startZoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }            }    }
}