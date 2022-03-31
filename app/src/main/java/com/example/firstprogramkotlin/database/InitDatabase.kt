package com.example.firstprogramkotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Apartment:: class], version = 1, exportSchema = false)
abstract class InitDatabase : RoomDatabase(){

    companion object{
        @Volatile
        private var INSTANCE: InitDatabase? = null

        fun getInstance(context: Context) : InitDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                                InitDatabase :: class.java, "room_db" ).
                                build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
    abstract fun getRoomDao(): RoomDao
}