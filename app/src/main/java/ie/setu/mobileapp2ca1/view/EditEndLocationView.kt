package ie.setu.mobileapp2ca1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import ie.setu.mobileapp2ca1.R
import ie.setu.mobileapp2ca1.models.Location
import ie.setu.mobileapp2ca1.presenter.EditEndLocationPresenter

class EditEndLocationView : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var presenter: EditEndLocationPresenter
    private var endLocation = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_end)
        presenter = EditEndLocationPresenter(this)
        endLocation = intent.extras?.getParcelable("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapEnd) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.initMap(map)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude,marker.position.longitude, map.cameraPosition.zoom)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()
        super.onBackPressed()
    }
}