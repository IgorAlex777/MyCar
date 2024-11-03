package com.cmex.mycar.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmex.mycar.databinding.ItemRepairBinding
import com.cmex.mycar.screen.SettingRepair

class AdapterRepairCar :RecyclerView.Adapter<AdapterRepairCar.HolderRepairCar>(){
    private lateinit var binding: ItemRepairBinding
    val repairCarList= arrayListOf<RepairCar>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRepairCar {
        val inflanter= LayoutInflater.from(parent.context)
        binding= ItemRepairBinding.inflate(inflanter,parent,false)
        return HolderRepairCar(binding)
    }

    override fun getItemCount(): Int {
        return repairCarList.size
    }

    override fun onBindViewHolder(holder: HolderRepairCar, position: Int) {
        return holder.setData(repairCarList[position])
    }
    class HolderRepairCar(val binding: ItemRepairBinding) : RecyclerView.ViewHolder(binding.root){
        val context= binding.root.context!!
        fun setData(repairCar: RepairCar){

            binding.tvDate.text=repairCar.timeRepair
            binding.tvRepairCar.text=repairCar.titleRepair
            binding.tvMileAgeRepair.text=repairCar.mileAge
            binding.tvPriceRepair.text=repairCar.priceRepair
            clickEditWork(repairCar)
            clickViewingWork(repairCar)
        }

        private fun clickEditWork(repairCar: RepairCar){
            binding.ibEditWork.setOnClickListener {
                val intent= Intent(context, SettingRepair::class.java).apply {
                    putExtra(ConstantsMyCar.REPAIR_ITEM,repairCar)
                    putExtra(ConstantsMyCar.FLAG_STATE,1)
                }
                context.startActivity(intent)
            }
        }
        private fun clickViewingWork(repairCar: RepairCar){
            binding.ibViewingWork.setOnClickListener {
                val intent= Intent(context, SettingRepair::class.java).apply {
                    putExtra(ConstantsMyCar.REPAIR_ITEM, repairCar)
                    putExtra(ConstantsMyCar.FLAG_STATE, 2)
                }
                context.startActivity(intent)
            }
        }
    }

    fun updateAdapter(newList: List<RepairCar>){
        val diffUtil= DiffUtil.calculateDiff(DiffUtilIRepairCarList(repairCarList,newList))
        diffUtil.dispatchUpdatesTo(this)
        repairCarList.clear()
        repairCarList.addAll(newList)
        notifyDataSetChanged()
    }
}