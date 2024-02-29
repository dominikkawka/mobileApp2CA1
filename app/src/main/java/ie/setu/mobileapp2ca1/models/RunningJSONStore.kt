package ie.setu.mobileapp2ca1.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import ie.setu.mobileapp2ca1.helpers.*
import kotlin.collections.ArrayList

const val JSON_FILE = "runningTracks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<RunningModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class RunningJSONStore(private val context: Context) : RunningStore {

    var runningTracks = mutableListOf<RunningModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<RunningModel> {
        logAll()
        return runningTracks
    }

    override fun create(track: RunningModel) {
        track.id = generateRandomId()
        runningTracks.add(track)
        serialize()
    }


    override fun update(track: RunningModel) {
        val runningTrackList = findAll() as ArrayList<RunningModel>
        var editedTrack: RunningModel? = runningTrackList.find { p -> p.id === track.id}
        if (editedTrack != null) {
            editedTrack.title = track.title
            editedTrack.description = track.description
            editedTrack.image = track.image
            editedTrack.startLng = track.startLng
            editedTrack.startLat = track.startLat
            editedTrack.startZoom = track.startZoom

            editedTrack.endLat = track.endLat
            editedTrack.endLng = track.endLng
            editedTrack.endZoom = track.endZoom
            editedTrack.difficulty = track.difficulty
            editedTrack.distance = track.distance
            editedTrack.weatherCondition = track.weatherCondition
        }
        serialize()
    }
    override fun delete(track: RunningModel) {
        runningTracks.remove(track)
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(runningTracks, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        runningTracks = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        runningTracks.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

    class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Uri {
            return Uri.parse(json?.asString)
        }

        override fun serialize(
            src: Uri?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src.toString())
        }
    }
}


