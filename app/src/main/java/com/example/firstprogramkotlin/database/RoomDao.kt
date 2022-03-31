package com.example.firstprogramkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomDao {
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
//    @Query("DELETE FROM data_room WHERE id = :key")
//    fun clearItem(key: Long)
}