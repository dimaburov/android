package com.example.firstprogramkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomDao {
    //  base - Room
    @Insert
    fun insert(room: Apartment)

    @Update
    fun update(room: Apartment)

    @Query("SELECT * FROM data_room WHERE id = :key")
    fun get(key: Long): Apartment?

    @Query("SELECT * FROM data_room ORDER BY id DESC")
    fun getAllRoom(): LiveData<List<Apartment>>

    @Query("SELECT * FROM data_room ORDER BY id DESC LIMIT 1")
    fun getRoomNow(): Apartment?

    @Query("DELETE FROM data_room")
    fun clear()

    @Delete
    fun deleteItem(apartment: Apartment)

    //  base - Material
    @Insert
    fun insertMaterial(material: Material)

    @Update
    fun updateMaterial(material: Material)

    @Query("SELECT * FROM material_room WHERE id_material = :key")
    fun getMaterial(key: Long): Material?

    @Query("SELECT * FROM material_room ORDER BY id_material DESC")
    fun getAllMaterial(): LiveData<List<Material>>

    @Query("SELECT * FROM material_room ORDER BY id_material DESC LIMIT 1")
    fun MaterialNow(): Material?

    @Query("DELETE FROM material_room")
    fun clearMaterial()

    @Delete
    fun deleteItemMaterial(material: Material)
}