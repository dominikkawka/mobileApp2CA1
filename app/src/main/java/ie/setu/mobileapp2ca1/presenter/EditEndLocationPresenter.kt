package ie.setu.mobileapp2ca1.presenter

import android.app.Activity
import android.content.Intent
import ie.setu.mobileapp2ca1.models.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.mobileapp2ca1.view.EditEndLocationView

class EditEndLocationPresenter(val view: EditEndLocationView) {

    var location = Location()

    init {
        location = view.intent.extras?.getParcelable<Location>("endLocation")?: Location()
    }

    fun initMap(map: GoogleMap) {
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Ending Point")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        map.setOnMarkerDragListener(view)
        map.setOnMarkerClickListener(view)
    }

    fun doUpdateLocation(lat: Double, lng: Double, zoom: Float) {
        location.lat = lat
        location.lng = lng
        location.zoom = zoom
    }

    fun doOnBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("endLocation", location)
        view.setResult(Activity.RESULT_OK, resultIntent)
        view.finish()
    }

    fun doUpdateMarker(marker: Marker) {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
    }
}