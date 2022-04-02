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

    // base - Material Basic
    @Insert
    fun insertMaterialBasic(materialBasic: MaterialBasic)

    @Update
    fun updateMaterialBasic(materialBasic: MaterialBasic)

    @Query("SELECT * FROM material_basic_room WHERE id_material = :key")
    fun getMaterialBasic(key: Long): MaterialBasic?

    @Query("SELECT * FROM material_basic_room ORDER BY id_material DESC")
    fun getAllMaterialBasic(): LiveData<List<MaterialBasic>>

    @Query("SELECT * FROM material_basic_room ORDER BY id_material DESC LIMIT 1")
    fun MaterialBasicNow(): MaterialBasic?

    @Query("DELETE FROM material_basic_room")
    fun clearMaterialBasic()

    @Delete
    fun deleteItemMaterialBasic(materialBasic: MaterialBasic)

    //TEST
    @Query("INSERT INTO material_basic_room " +
            "SELECT * FROM material_room")
    fun addMaterialIntoMaterialBasic()

}