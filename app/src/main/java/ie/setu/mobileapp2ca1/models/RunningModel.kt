package ie.setu.mobileapp2ca1.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RunningModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY,
    var startLat : Double = 0.0,
    var startLng : Double = 0.0,
    var startZoom : Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f) : Parcelable
