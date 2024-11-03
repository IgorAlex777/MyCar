package com.cmex.mycar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "documents_list")
class DocumentsCar (
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
    @ColumnInfo(name="title_documents_car")
    val titleDocuments:String,
    @ColumnInfo(name="uri_documents")
    val uriDocuments:String
):Serializable