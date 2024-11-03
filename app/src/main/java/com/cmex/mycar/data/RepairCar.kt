package com.cmex.mycar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "repair_list")
data class RepairCar(
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
    @ColumnInfo(name="title_repair")
    val titleRepair:String,
    @ColumnInfo(name="text_repair")
    val textRepair:String,
    @ColumnInfo(name="mileage_repair")
    val mileAge:String,
    @ColumnInfo(name="price_repair")
    val priceRepair:String,
    @ColumnInfo(name="time_repair")
    val timeRepair:String,
    @ColumnInfo(name="image_repair")
    var imageRepair:String="empty"

): Serializable
