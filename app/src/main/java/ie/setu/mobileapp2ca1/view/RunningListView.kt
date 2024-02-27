package ie.setu.mobileapp2ca1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding = ActivityRunningListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = RunningListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
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

    fun onRefresh() {
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,presenter.getTracks().size)
    }

    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }


}