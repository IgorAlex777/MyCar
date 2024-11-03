package com.cmex.mycar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoDb {
    //============Insert==============
    @Insert
    suspend fun insertTabCarItem(carItem: CarItem)

    @Insert
    suspend fun insertTabWorkCar(workCar: WorkCar)

    @Insert
    suspend fun insertTabRepairCar(repairCar: RepairCar)

    @Insert
    suspend fun insertDocumentsCar(documentsCar: DocumentsCar)

//=======================================

//================READ========================

    @Query("SELECT * FROM car_list")
    fun readAllCarList(): Flow<List<CarItem>>

   /* @Query("SELECT * FROM car_list ORDER BY year_car ")
    fun getAllCarList(): Flow<List<CarItem>>*/
   @Query("SELECT * FROM works_list")
   fun readAllWorksList(): Flow<List<WorkCar>>

    @Query("SELECT * FROM works_list WHERE idCar LIKE:idCar")
    fun readItemWorksList(idCar:Int): Flow<List<WorkCar>>

    @Query("SELECT * FROM repair_list WHERE idCar LIKE:idCar")
    fun readItemRepairList(idCar:Int): Flow<List<RepairCar>>

    @Query("SELECT * FROM documents_list WHERE idCar LIKE:idCar")
    fun readDocumentsList(idCar:Int): Flow<List<DocumentsCar>>

//==============================================

    //==================DELETE======================
    @Query("DELETE  FROM car_list WHERE id IS :idCar")
    suspend fun deleteCarItem(idCar:Int)

    @Query("DELETE  FROM works_list WHERE id IS :idWork")
    suspend fun deleteWorkCarItem(idWork:Int)
    @Query("DELETE  FROM works_list WHERE idCar LIKE :idCarWork")
    suspend fun deleteWorkCarItems(idCarWork:Int)

    @Query("DELETE  FROM repair_list WHERE id IS :idRepair")
    suspend fun deleteRepairCarItem(idRepair:Int)
    @Query("DELETE  FROM repair_list WHERE idCar LIKE :idCarRepair")
    suspend fun deleteRepairCarItems(idCarRepair:Int)

    @Query("DELETE  FROM documents_list WHERE id IS :idDoc")
    suspend fun deleteDocumentsCar(idDoc:Int)
    @Query("DELETE  FROM documents_list WHERE idCar LIKE :idCarDoc")
    suspend fun deleteDocumentsCarItems(idCarDoc:Int)

//==========================================

    //=================UPDATE===========================
    @Update
    suspend fun updateCarItem(carItem: CarItem)

    @Update
    suspend fun updateWorkCarItem(workCar: WorkCar)

    @Update
    suspend fun updateRepairCarItem(repairCar: RepairCar)

    @Update
    suspend fun updateDocumentsCar(documentsCar: DocumentsCar)

//==================================================
}