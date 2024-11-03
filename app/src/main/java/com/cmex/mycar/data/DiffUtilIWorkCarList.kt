package com.cmex.mycar.data

import androidx.recyclerview.widget.DiffUtil



class DiffUtilIWorkCarList(private val oldAWorkCarList: ArrayList<WorkCar>,
                           private val newWorkCarList: List<WorkCar>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int {

       return oldAWorkCarList.size

    }

    override fun getNewListSize(): Int {
       return newWorkCarList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldAWorkCarList[oldItemPosition].id==newWorkCarList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldAWorkCarList[oldItemPosition]==newWorkCarList[newItemPosition]
    }

}