package ie.setu.mobileapp2ca1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ie.setu.mobileapp2ca1.R
import ie.setu.mobileapp2ca1.main.MainApp
import ie.setu.mobileapp2ca1.databinding.ActivityRunningBinding
import ie.setu.mobileapp2ca1.models.RunningModel
import timber.log.Timber
import timber.log.Timber.i

class RunningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningBinding
    var RunningTrack = RunningModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        Timber.plant(Timber.DebugTree())
        i("Running Activity started..")

        binding.btnAdd.setOnClickListener() {

            RunningTrack.title = binding.runningTitle.text.toString()
            RunningTrack.description = binding.runningDescription.text.toString()
            if (RunningTrack.title.isNotEmpty()) {
                app.runningTracks.add(RunningTrack.copy())
                i("add Button Pressed: ${RunningTrack}")
                for (i in app.runningTracks.indices) {
                    i("Running Tracks[$i]:${this.app.runningTracks[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_track, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}