package ie.setu.mobileapp2ca1.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class RunningMemStore: RunningStore {
    val runningTracks = ArrayList<RunningModel>()

    private fun logAll() {
        runningTracks.forEach{ i("$it") }
    }

    override fun findAll(): List<RunningModel> {
        return runningTracks
    }

    override fun create(track: RunningModel) {
        track.id = getId()
        runningTracks.add(track)
        logAll()
    }

    override fun update(track: RunningModel) {
        var foundTrack: RunningModel? = runningTracks.find { p -> p.id == track.id }
        if (foundTrack != null) {
            foundTrack.title = track.title
            foundTrack.description = track.description
            foundTrack.image = track.image
            foundTrack.startLat = track.startLat
            foundTrack.startLng = track.startLng
            foundTrack.startZoom = track.startZoom
            logAll()
        }
    }

}