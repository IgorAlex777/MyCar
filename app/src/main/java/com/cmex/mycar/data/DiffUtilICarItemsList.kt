package com.cmex.mycar.data

import androidx.recyclerview.widget.DiffUtil



class DiffUtilICarItemsList(private val oldACarItemList:ArrayList<CarItem>,
                            private val newACarItemList: List<CarItem>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int {
       return oldACarItemList.size

    }

    override fun getNewListSize(): Int {
       return newACarItemList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldACarItemList[oldItemPosition].id==newACarItemList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldACarItemList[oldItemPosition]==newACarItemList[newItemPosition]
    }

}