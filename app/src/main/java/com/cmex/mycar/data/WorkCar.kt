package com.cmex.mycar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "works_list")
data class WorkCar(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    @ColumnInfo(name="name_car")
    val nameCar:String,
    @ColumnInfo(name="model_car")
    val modelCar:String,
    @ColumnInfo(name="vin_car")
    val vinCar:String,
    @ColumnInfo(name="idCar")
    val idCar:Int,
    @ColumnInfo(name="works_car")
    val workCar:String,
    @ColumnInfo(name="main_works")
    val mainWorks:String,
    @ColumnInfo(name="addition_work")
    val additionWork:String,
    @ColumnInfo(name="mileage_work")
    val mileAge:String,
    @ColumnInfo(name="price_to")
    val priceWorks:String,
    @ColumnInfo(name="time_work")
    val timeWork:String,
    @ColumnInfo(name="image_work")
    var imageWork:String="empty"

): Serializable
