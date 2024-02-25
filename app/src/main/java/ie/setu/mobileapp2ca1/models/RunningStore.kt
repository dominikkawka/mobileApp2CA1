package ie.setu.mobileapp2ca1.models

interface RunningStore {
    fun findAll(): List<RunningModel>
    fun create(track: RunningModel)
    fun update(track: RunningModel)
}