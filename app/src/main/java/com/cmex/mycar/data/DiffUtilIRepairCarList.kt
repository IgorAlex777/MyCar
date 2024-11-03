package com.cmex.mycar.data

import androidx.recyclerview.widget.DiffUtil


class DiffUtilIRepairCarList(private val oldARepairCarList: ArrayList<RepairCar>,
                             private val newRepairCarList: List<RepairCar>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int {

       return oldARepairCarList.size

    }

    override fun getNewListSize(): Int {
       return newRepairCarList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldARepairCarList[oldItemPosition].id==newRepairCarList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldARepairCarList[oldItemPosition]==newRepairCarList[newItemPosition]
    }

}