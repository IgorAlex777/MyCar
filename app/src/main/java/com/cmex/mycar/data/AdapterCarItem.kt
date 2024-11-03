package com.cmex.mycar.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmex.mycar.databinding.ItemCarBinding
import com.cmex.mycar.screen.InfoCar
import com.cmex.mycar.screen.SettingCar

class AdapterCarItem :RecyclerView.Adapter<AdapterCarItem.HolderCarItem>() {
    private lateinit var binding: ItemCarBinding
    var carItemList= arrayListOf<CarItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCarItem {
        val inflater=LayoutInflater.from(parent.context)
        binding= ItemCarBinding.inflate(inflater,parent,false)
        return HolderCarItem(binding,parent.context)
    }

    override fun getItemCount(): Int {
        return  carItemList.size
    }

    override fun onBindViewHolder(holder: HolderCarItem, position: Int) {
        holder.setData( carItemList[position])
    }

     fun updateAdapter(newList: List<CarItem>){
         val diffUtil= DiffUtil.calculateDiff(DiffUtilICarItemsList(carItemList,newList))
         diffUtil.dispatchUpdatesTo(this)
         carItemList.clear()
         carItemList.addAll(newList)
         notifyDataSetChanged()
     }

    class HolderCarItem(val binding: ItemCarBinding,val context: Context):RecyclerView.ViewHolder(binding.root){
        fun setData(carItem: CarItem){
            binding.tvItemNameCar.text= carItem.nameCar
            binding.tvItemModelCar.text=carItem.model
            binding.ivItemCarImage.setImageURI(carItem.imageCar.toUri())
            clickItem(carItem)
            clickViewingCar(carItem)
            clickEditCar(carItem)
        }
        private fun clickEditCar(carItem: CarItem){
            binding.ibEditCarItem.setOnClickListener {
                val intent= Intent(context,SettingCar::class.java).apply {
                    putExtra(ConstantsMyCar.CAR_ITEM,carItem)
                    putExtra(ConstantsMyCar.FLAG_STATE,1)
                }
                context.startActivity(intent)
            }
            }
        private fun clickViewingCar(carItem: CarItem){
            binding.ibViewingCarItem.setOnClickListener {
                val intent= Intent(context,SettingCar::class.java).apply {
                    putExtra(ConstantsMyCar.CAR_ITEM, carItem)
                    putExtra(ConstantsMyCar.FLAG_STATE, 2)
                }
                context.startActivity(intent)
            }
        }
        private fun clickItem(carItem: CarItem){
            itemView.setOnClickListener {
                val intent= Intent(context,InfoCar::class.java).apply {
                   putExtra(ConstantsMyCar.CAR_ITEM,carItem)

                }
                context.startActivity(intent)
            }
        }
    }
}