package ie.setu.mobileapp2ca1.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.mobileapp2ca1.R
import ie.setu.mobileapp2ca1.adapters.RunningAdapter
import ie.setu.mobileapp2ca1.adapters.RunningListener
import ie.setu.mobileapp2ca1.databinding.ActivityRunningListBinding
import ie.setu.mobileapp2ca1.main.MainApp
import ie.setu.mobileapp2ca1.models.RunningModel

class RunningListActivity : AppCompatActivity(), RunningListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityRunningListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = RunningAdapter(app.runningTracks.findAll(), this)
        RunningAdapter(app.runningTracks.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, RunningActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.runningTracks.findAll().size)

            }
        }

    override fun onTrackClick(track: RunningModel) {
        val launcherIntent = Intent(this, RunningActivity::class.java)
        launcherIntent.putExtra("track_edit", track)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.runningTracks.findAll().size)
            }
        }
}