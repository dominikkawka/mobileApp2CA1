package ie.setu.mobileapp2ca1.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.mobileapp2ca1.R
import ie.setu.mobileapp2ca1.main.MainApp
import ie.setu.mobileapp2ca1.databinding.ActivityRunningBinding
import ie.setu.mobileapp2ca1.helpers.showImagePicker
import ie.setu.mobileapp2ca1.models.Location
import ie.setu.mobileapp2ca1.models.RunningModel
import timber.log.Timber
import timber.log.Timber.i
class RunningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunningBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    var runningTrack = RunningModel()
    lateinit var app: MainApp
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp


        if (intent.hasExtra("track_edit")) {
            edit = true
            runningTrack = intent.extras?.getParcelable("track_edit")!!
            binding.btnAdd.setText(R.string.button_saveTrack)
            binding.runningTitle.setText(runningTrack.title)
            binding.runningDescription.setText(runningTrack.description)
            Picasso.get()
                .load(runningTrack.image)
                .into(binding.trackImage)
            if (runningTrack.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.select_changeTrackImage)
            }
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, this)
        }

        binding.runningStartLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (runningTrack.startZoom != 0f) {
                location.lat =  runningTrack.startLat
                location.lng = runningTrack.startLng
                location.zoom = runningTrack.startZoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.btnAdd.setOnClickListener() {
            runningTrack.title = binding.runningTitle.text.toString()
            runningTrack.description = binding.runningDescription.text.toString()

            if (runningTrack.title.isEmpty()) {
                Snackbar.make(it,R.string.snackbar_invalidTrack, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.runningTracks.update(runningTrack.copy())
                } else {
                    app.runningTracks.create(runningTrack.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_track, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                setResult(99)
                app.runningTracks.delete(runningTrack)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            runningTrack.image = image

                            Picasso.get()
                                .load(runningTrack.image)
                                .into(binding.trackImage)
                            binding.chooseImage.setText(R.string.select_changeTrackImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            runningTrack.startLat = location.lat
                            runningTrack.startLng = location.lng
                            runningTrack.startZoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
