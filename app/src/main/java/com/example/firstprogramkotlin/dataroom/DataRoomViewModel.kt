package com.example.firstprogramkotlin.dataroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firstprogramkotlin.database.RoomDao
import com.example.firstprogramkotlin.database.Apartment
import com.example.firstprogramkotlin.database.Material
import kotlinx.coroutines.*

class DataRoomViewModel(
    val dao: RoomDao,
    application: Application) : AndroidViewModel(application){

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val materials = dao.getAllMaterial()

//    var steps = ArrayList<Apartment>()

    private var roomNow = MutableLiveData<Apartment?>()

    private var materialNow = MutableLiveData<Material?>()

    private val _navigateAfterNewRecipe= MutableLiveData<Boolean>()
    val navigateAfterNewRecipe: LiveData<Boolean>
        get() = _navigateAfterNewRecipe

    init{
        initRoomNow()
        initMaterialNow()
    }
//----------  ROOM  -----------
    private fun initRoomNow() {
        uiScope.launch {
            roomNow.value = getRoomNowFromDao()
        }
    }

    private suspend fun getRoomNowFromDao(): Apartment? {
        return withContext(Dispatchers.IO){
            var room = dao.getRoomNow()
            return@withContext room
        }
    }

    fun onClearJob(){
        onCleared()
    }

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    //Передавать сюда все парамеры комнаты
    fun onSaveRoom(length:Double,height:Double,wight:Double, checkUtilsFloorLamOrLin:Boolean, checkUtilsMOrSM:Boolean, size:String, boardCount:Int){
        uiScope.launch {
            val newRoom = Apartment()
            newRoom.length = length
            newRoom.height = height
            newRoom.wight = wight
            newRoom.floorF = checkUtilsFloorLamOrLin
            newRoom.size = size
            newRoom.utilsMSm = checkUtilsMOrSM
            newRoom.countBoard = boardCount
            insert(newRoom)
//            steps.add(newRoom)
            _navigateAfterNewRecipe.value = true
        }
    }

    private suspend fun insert(newRoom: Apartment) {
        withContext(Dispatchers.IO){
            dao.insert(newRoom)
        }
    }
    //Написать функцию изменения данных
//    fun onStopTracking(){
//        uiScope.launch {
//            val oldRoom = roomNow.value ?: return@launch
//
//        }
//    }

    private suspend fun update(oldRoom: Apartment){
        withContext(Dispatchers.IO){
            dao.update(oldRoom)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            roomNow.value=null
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            dao.clear()
        }
    }
//--------  MATERIAL  -----------


    private fun initMaterialNow() {
        uiScope.launch {
            materialNow.value = getMaterialNowFromDao()
        }
    }

    private suspend fun getMaterialNowFromDao(): Material? {
        return withContext(Dispatchers.IO){
            var material = dao.MaterialNow()
            return@withContext material
        }
    }

    fun onClearMaterial(){
        uiScope.launch {
            clearMaterial()
            materialNow.value=null
        }
    }

    private suspend fun clearMaterial(){
        withContext(Dispatchers.IO){
            dao.clearMaterial()
        }
    }


    fun doneNavigating(){
        _navigateAfterNewRecipe.value = false
    }


    fun deleteMaterial(material: Material){
        uiScope.launch {
            removeMaterial(material)
        }
    }

    private suspend fun removeMaterial(material: Material) {
        withContext(Dispatchers.IO){
            dao.deleteItemMaterial(material)
        }
    }

    //TEST MATERIAL - MATERIAL BASIC
    fun addMaterialInMaterialBasic(){
        uiScope.launch {
            addMaterialBasic()
        }
    }

    private suspend fun addMaterialBasic(){
        withContext(Dispatchers.IO){
            dao.addMaterialIntoMaterialBasic()
        }
    }
}