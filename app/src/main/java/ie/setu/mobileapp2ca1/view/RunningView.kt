package ie.setu.mobileapp2ca1.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.mobileapp2ca1.R
import ie.setu.mobileapp2ca1.databinding.ActivityRunningBinding
import ie.setu.mobileapp2ca1.models.RunningModel
import ie.setu.mobileapp2ca1.presenter.RunningPresenter
import timber.log.Timber.i

class RunningView : AppCompatActivity() {

    private lateinit var binding: ActivityRunningBinding
    private lateinit var presenter: RunningPresenter
    var runningTrack = RunningModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = RunningPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheTrack(
                binding.runningTitle.text.toString(),
                binding.runningDescription.text.toString(),
                binding.runningDifficulty.progress
            )
            presenter.doSelectImage()
        }

        binding.runningStartLocation.setOnClickListener {
            presenter.cacheTrack(binding.runningTitle.text.toString(), binding.runningTitle.text.toString(),binding.runningDifficulty.progress)
            presenter.doSetLocation()
        }

        binding.btnAdd.setOnClickListener {
            if (binding.runningTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.hint_trackTitle, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(binding.runningTitle.text.toString(), binding.runningDescription.text.toString(), binding.runningDifficulty.progress+1) //+1 since ratings start at 0 in seekbar
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_track, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showTrack(runningTrack: RunningModel) {
        binding.runningTitle.setText(runningTrack.title)
        binding.runningDescription.setText(runningTrack.description)
        binding.runningDifficulty.setProgress(runningTrack.difficulty-1) //-1 since ratings start at 0 in seekbar
        binding.btnAdd.setText(R.string.button_saveTrack)
        Picasso.get()
            .load(runningTrack.image)
            .into(binding.trackImage)
        if (runningTrack.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.select_changeTrackImage)
        }
    }

    fun updateImage(image: Uri) {
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.trackImage)
        binding.chooseImage.setText(R.string.select_changeTrackImage)
    }

}
