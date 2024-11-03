package com.cmex.mycar.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmex.mycar.databinding.ItemWorkBinding
import com.cmex.mycar.screen.SettingWork

class AdapterWorkCar :RecyclerView.Adapter<AdapterWorkCar.HolderWorkCar>() {
   private lateinit var binding:ItemWorkBinding
    val worksCarList= arrayListOf<WorkCar>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderWorkCar {
        val inflanter=LayoutInflater.from(parent.context)
        binding=ItemWorkBinding.inflate(inflanter,parent,false)
       return HolderWorkCar(binding)
    }

    override fun getItemCount(): Int {
       return worksCarList.size
    }

    override fun onBindViewHolder(holder: HolderWorkCar, position: Int) {
       return holder.setData(worksCarList[position])
    }
    class HolderWorkCar(val binding: ItemWorkBinding) :RecyclerView.ViewHolder(binding.root){
        val context= binding.root.context!!
        fun setData(workCar: WorkCar){

            binding.tvDateW.text=workCar.timeWork
            binding.tvWorkCar.text=workCar.workCar
            binding.tvMileageW.text=workCar.mileAge
            binding.tvPriceW.text=workCar.priceWorks
            clickEditWork(workCar)
            clickViewingWork(workCar)
        }

        private fun clickEditWork(workCar: WorkCar){
            binding.ibEditWork.setOnClickListener {
                val intent= Intent(context, SettingWork::class.java).apply {
                    putExtra(ConstantsMyCar.WORK_ITEM,workCar)
                    putExtra(ConstantsMyCar.FLAG_STATE,1)
                }
                context.startActivity(intent)
            }
        }
        private fun clickViewingWork(workCar: WorkCar){
            binding.ibViewingWork.setOnClickListener {
                val intent= Intent(context, SettingWork::class.java).apply {
                    putExtra(ConstantsMyCar.WORK_ITEM, workCar)
                    putExtra(ConstantsMyCar.FLAG_STATE, 2)
                }
                context.startActivity(intent)
            }
        }
    }

    fun updateAdapter(newList: List<WorkCar>){
        val diffUtil= DiffUtil.calculateDiff(DiffUtilIWorkCarList(worksCarList,newList))
        diffUtil.dispatchUpdatesTo(this)
        worksCarList.clear()
        worksCarList.addAll(newList)
        notifyDataSetChanged()
    }
}