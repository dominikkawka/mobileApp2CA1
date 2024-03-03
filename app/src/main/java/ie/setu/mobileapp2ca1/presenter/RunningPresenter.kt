package ie.setu.mobileapp2ca1.presenter

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Location.distanceBetween
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.mobileapp2ca1.view.EditStartLocationView
import ie.setu.mobileapp2ca1.view.RunningView
import ie.setu.mobileapp2ca1.helpers.showCamera
import ie.setu.mobileapp2ca1.helpers.showImagePicker
import ie.setu.mobileapp2ca1.main.MainApp
import ie.setu.mobileapp2ca1.models.Location
import ie.setu.mobileapp2ca1.models.RunningModel
import ie.setu.mobileapp2ca1.view.EditEndLocationView
import timber.log.Timber.i


class RunningPresenter(private val view: RunningView) {

    private var runningTrack = RunningModel()
    var app: MainApp = view.application as MainApp
    //var binding: ActivityRunningBinding = ActivityRunningBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    //private lateinit var cameraIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    init {
        if (view.intent.hasExtra("track_edit")) {
            edit = true
            runningTrack = view.intent.extras?.getParcelable("track_edit")!!
            view.showTrack(runningTrack)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSave(title: String, description: String, difficulty: Int, weather: String) {
        runningTrack.apply {
            this.title = title
            this.description = description
            this.difficulty = difficulty
            this.weatherCondition = weather

            //TODO: BUG: distance value returns as 0.0
            val distanceResults = FloatArray(1)
            distanceBetween(startLat, startLng, endLat, endLng, distanceResults)
            i("start: $startLat $startLng")
            i("end: $endLat $endLng")
            i("distance results: ${distanceResults[0]}")
            this.distance = distanceResults[0]
        }
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

    fun doOpenCamera() {
        showCamera(imageIntentLauncher,view)
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

    fun doEndLocation() {
        val endLocation = Location(52.245691, -7.139102, 15f)
        if (runningTrack.endZoom != 0f) {
            endLocation.lat =  runningTrack.endLat
            endLocation.lng = runningTrack.endLng
            endLocation.zoom = runningTrack.endZoom
        }
        val launcherIntent = Intent(view, EditEndLocationView::class.java)
            .putExtra("location", endLocation)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheTrack (title: String, description: String, difficulty: Int, weather: String) {
        runningTrack.title = title
        runningTrack.description = description
        runningTrack.difficulty = difficulty
        runningTrack.weatherCondition = weather
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = view.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when(result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val data = result.data
                    if (data != null) {
                        i("Got data result $data")
                        //when choosing image from file; imageData is there, but data isn't present
                        //when using camera...
                        val imageData = data.data
                        if (imageData != null) {
                            i("Got Image Data Result $imageData")
                            runningTrack.image = imageData
                            view.contentResolver.takePersistableUriPermission(
                                runningTrack.image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            view.updateImage(runningTrack.image)
                        }
                    }
                }
                AppCompatActivity.RESULT_CANCELED -> { }
                else -> { }
            }
        }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")

                            if (result.data!!.extras?.containsKey("location") == true) {
                                val location = result.data!!.extras?.getParcelable<Location>("location")!!
                                i("Location == $location")
                                runningTrack.startLat = location.lat
                                runningTrack.startLng = location.lng
                                runningTrack.startZoom = location.zoom
                            }

                            if (result.data!!.extras?.containsKey("location") == true) {
                                val location = result.data!!.extras?.getParcelable<Location>("location")!!
                                i("Location == $location")
                                runningTrack.endLat = location.lat
                                runningTrack.endLng = location.lng
                                runningTrack.endZoom = location.zoom
                            }

                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }            }    }
}