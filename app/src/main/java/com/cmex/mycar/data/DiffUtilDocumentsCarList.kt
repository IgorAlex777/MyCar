package com.cmex.mycar.data

import androidx.recyclerview.widget.DiffUtil


class DiffUtilDocumentsCarList(private val oldDocList: ArrayList<DocumentsCar>,
                               private val newDocList: List<DocumentsCar>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int {

       return oldDocList.size

    }

    override fun getNewListSize(): Int {
       return newDocList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldDocList[oldItemPosition].id==newDocList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldDocList[oldItemPosition]==newDocList[newItemPosition]
    }

}