package ie.setu.mobileapp2ca1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import ie.setu.mobileapp2ca1.R
import ie.setu.mobileapp2ca1.adapters.RunningAdapter
import ie.setu.mobileapp2ca1.adapters.RunningListener
import ie.setu.mobileapp2ca1.databinding.ActivityRunningListBinding
import ie.setu.mobileapp2ca1.main.MainApp
import ie.setu.mobileapp2ca1.models.RunningModel
import ie.setu.mobileapp2ca1.presenter.RunningListPresenter

class RunningListView : AppCompatActivity(), RunningListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityRunningListBinding
    lateinit var presenter: RunningListPresenter
    private var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityRunningListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = RunningListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        //https://stackoverflow.com/questions/40569436/kotlin-addtextchangelistener-lambda
        //SEARCH BAR CODE
        val searchBar = findViewById<EditText>(R.id.runningSearchBar)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val search = charSequence.toString()
                presenter.doSearchTrack(search)
            }

            override fun afterTextChanged(editable: Editable?) {
                // TODO
            }
        })
        // CHIP FILTER CODE
        // There surely is a better way to do this? with ChipGroup Maybe?
        // https://developer.android.com/reference/com/google/android/material/chip/ChipGroup
        val clearChip = findViewById<Chip>(R.id.clear_weather_chip)
        val sunnyChip = findViewById<Chip>(R.id.sunny_weather_chip)
        val windyChip = findViewById<Chip>(R.id.windy_weather_chip)
        val rainyChip = findViewById<Chip>(R.id.rainy_weather_chip)

        clearChip.setOnClickListener {
            if (clearChip.isEnabled) {
                presenter.doFilterTrackByWeather(clearChip.text.toString())
            } else {
                loadTracks()
            }
        }

        sunnyChip.setOnClickListener {
            if (sunnyChip.isEnabled) {
                presenter.doFilterTrackByWeather(sunnyChip.text.toString())
            } else {
                loadTracks()
            }
        }

        windyChip.setOnClickListener {
            if (windyChip.isEnabled) {
                presenter.doFilterTrackByWeather(windyChip.text.toString())
            } else {
                loadTracks()
            }
        }

        rainyChip.setOnClickListener {
            if (rainyChip.isEnabled) {
                presenter.doFilterTrackByWeather(rainyChip.text.toString())
            } else {
                loadTracks()
            }
        }

        //Loading search tracks; if the search bar is left alone all tracks will load
        loadTracks()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddTrack() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTrackClick(track: RunningModel, position: Int) {
        this.position = position
        presenter.doEditTrack(track, this.position)
    }

    private fun loadTracks() {
        binding.recyclerView.adapter = RunningAdapter(presenter.getTracks(), this)
        onRefresh()
    }

    fun showSearchedTracks(searchedTracks: List<RunningModel>) {
        (binding.recyclerView.adapter as RunningAdapter).updateTracks(searchedTracks)
        onRefresh()
    }

    fun showFilteredTracks(filteredTracks: List<RunningModel>) {
        (binding.recyclerView.adapter as RunningAdapter).updateTracks(filteredTracks)
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,presenter.getTracks().size)
    }

    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }

}