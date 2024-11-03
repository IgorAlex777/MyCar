package com.cmex.mycar.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [CarItem::class,WorkCar::class,RepairCar::class,DocumentsCar::class], version = 1,
    exportSchema = true
)

 abstract class MainDb:RoomDatabase() {
     abstract fun getDaoDb():DaoDb
     companion object {
         @Volatile
         private var INSTANCE: MainDb? = null

         @OptIn(InternalCoroutinesApi::class)
         fun getDataBase(context: Context): MainDb {
             return INSTANCE ?: synchronized(this) {
                 val instance = Room.databaseBuilder(
                     context.applicationContext,
                     MainDb::class.java,
                     "car.db"
                 ).build()
                 instance
             }
         }
     }
}
