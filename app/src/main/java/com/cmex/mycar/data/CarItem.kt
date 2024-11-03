package com.cmex.mycar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "car_list")
data class CarItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    @ColumnInfo(name="name_car")
    val nameCar:String,
    @ColumnInfo(name="model")
    val model:String,
    @ColumnInfo(name="vin_car")
    val vinCar:String,
    @ColumnInfo(name="year_car")
    val yearCar:String,
    @ColumnInfo(name="mileage")
    val mileage:String,
    @ColumnInfo(name="fuel")
    val fuel:String,
    @ColumnInfo(name="size_wheel")
    val sizeWheel:String,
    @ColumnInfo(name="add_info")
    val addInfo:String,
    @ColumnInfo(name="image_car")
    var imageCar:String="empty"
): Serializable
