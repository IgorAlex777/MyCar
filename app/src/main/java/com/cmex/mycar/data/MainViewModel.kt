package com.cmex.mycar.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch


class MainViewModel( db: MainDb):ViewModel() {
val listCarItem=MutableLiveData<ArrayList<CarItem>>()
    val carItem=MutableLiveData<CarItem>()
    val workCarItem=MutableLiveData<WorkCar>()
    val repairCarItem=MutableLiveData<RepairCar>()
    val documentsCar=MutableLiveData<DocumentsCar>()
//=====================READ=======================================
    private val daoDd=db.getDaoDb()

    var carItemList:LiveData<List<CarItem>> =daoDd.readAllCarList().asLiveData()


    var workCarList:LiveData<List<WorkCar>> =daoDd.readAllWorksList().asLiveData()
    fun readItemByIdCarWork(idCar:Int):LiveData<List<WorkCar>> {
        return daoDd.readItemWorksList(idCar).asLiveData()

    }

    fun readItemByIdCarRepair(idCar:Int):LiveData<List<RepairCar>> {
        return daoDd.readItemRepairList(idCar).asLiveData()
    }

    fun readDocumentsCar(idCar:Int):LiveData<List<DocumentsCar>> {
        return daoDd.readDocumentsList(idCar).asLiveData()
    }

//=====================Insert==================================
 fun insertCarItem(carItem: CarItem) =viewModelScope.launch{
        daoDd.insertTabCarItem(carItem)
    }

    fun insertWorkCar(workCar: WorkCar) =viewModelScope.launch{
        daoDd.insertTabWorkCar(workCar)
    }

    fun insertRepairCar(repairCar: RepairCar) =viewModelScope.launch{
        daoDd.insertTabRepairCar(repairCar)
    }

    fun insertDocumentsCar(documentsCar: DocumentsCar) =viewModelScope.launch{
        daoDd.insertDocumentsCar(documentsCar)
    }
//=====================Update===================================
    fun updateCarItem(carItem: CarItem) =viewModelScope.launch{
        daoDd.updateCarItem(carItem)
    }

    fun updateWorkCarItem(workCar: WorkCar) =viewModelScope.launch{
        daoDd.updateWorkCarItem(workCar)
    }

    fun updateRepairCarItem(repairCar: RepairCar) =viewModelScope.launch{
        daoDd.updateRepairCarItem(repairCar)
    }

    fun updateDocumentsCar(documentsCar: DocumentsCar) =viewModelScope.launch{
        daoDd.updateDocumentsCar(documentsCar)
    }
//=======================Delete===================================
   fun deleteCarItem(id: Int) =viewModelScope.launch{
    daoDd.deleteCarItem(id)
    }

    fun deleteWorkCarItem(id: Int) =viewModelScope.launch{
        daoDd.deleteWorkCarItem(id)
    }
    fun deleteWorkCarItems(id: Int) =viewModelScope.launch{
        daoDd.deleteWorkCarItems(id)
    }

    fun deleteRepairCarItem(id: Int) =viewModelScope.launch{
        daoDd.deleteRepairCarItem(id)
    }
    fun deleteRepairCarItems(id: Int) =viewModelScope.launch{
        daoDd.deleteRepairCarItems(id)
    }

    fun deleteDocumentsCar(id: Int) =viewModelScope.launch{
        daoDd.deleteDocumentsCar(id)
    }
    fun deleteDocumentsCarItems(id: Int) =viewModelScope.launch{
        daoDd.deleteDocumentsCarItems(id)
    }
//===================================================================
  fun  updateCarItemsList(){
      carItemList=daoDd.readAllCarList().asLiveData()
  }


//========================================================================================
    class MainViewModelFactory(private val database: MainDb): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Error")
        }
    }
}