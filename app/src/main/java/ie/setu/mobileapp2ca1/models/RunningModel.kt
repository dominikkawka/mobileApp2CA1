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
    var startZoom : Float = 0f,
    // new data model
    var endLat: Double = 0.0,
    var endLng : Double = 0.0,
    var endZoom : Float = 0f,
    var difficulty : Int = 0,
    var distance : Float = 0f, //Difference between start and end lat,lng,zoom
    var weatherCondition : String = "", //enum class CLEAR,SUNNY,CLOUDY,RAINY
) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f) : Parcelable
