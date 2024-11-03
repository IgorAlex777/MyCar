package com.cmex.mycar.screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmex.mycar.R
import com.cmex.mycar.data.AdapterRepairCar
import com.cmex.mycar.data.AdapterWorkCar
import com.cmex.mycar.data.CarItem
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.data.MainViewModel
import com.cmex.mycar.data.RepairCar
import com.cmex.mycar.data.WorkCar
import com.cmex.mycar.databinding.FragmentRepairBinding
import com.cmex.mycar.databinding.FragmentWorkBinding
import com.cmex.mycar.domain.MyDialog
import com.cmex.mycar.domain.myLog
import com.cmex.mycar.domain.utilOpenFragment


class RepairFragment : Fragment() {
    private lateinit var binding: FragmentRepairBinding
    private  var carItem: CarItem?=null
    private  var listRepairCar= arrayListOf<RepairCar>()
    private lateinit var adapterRepairCar:  AdapterRepairCar

    private val model: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainAppMyCar).db)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateObserver()
        activity?.onBackPressedDispatcher?.addCallback(this) {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentRepairBinding.inflate(inflater,container,false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        onClickAddRepairCar()
    }
    private fun updateObserver() {
        model.carItem.observe(activity as AppCompatActivity) { item->
            carItem = item
            myLog("fragment-${carItem!!.nameCar}, ${carItem!!.model}, ${carItem!!.vinCar}")

            if (carItem != null) {
                model.readItemByIdCarRepair(carItem!!.id!!).observe(activity as AppCompatActivity) {
                    adapterRepairCar.updateAdapter(it)
                    listRepairCar.addAll(it)
                    if (it.isNotEmpty()) binding.ivEmptyRepair.visibility = View.GONE
                    else binding.ivEmptyRepair.visibility = View.VISIBLE

                }
            }
        }
    }


    private fun initRecyclerView(){
        adapterRepairCar= AdapterRepairCar()
        binding.rvRepairFragment.layoutManager= LinearLayoutManager(activity)
        binding.rvRepairFragment.adapter=adapterRepairCar
        getItemTouch()?.attachToRecyclerView(binding.rvRepairFragment)
    }
    private fun onClickAddRepairCar(){
        binding.ibAddRepairCar.setOnClickListener {
            startActivity(Intent(activity,SettingRepair::class.java).apply {
                putExtra(ConstantsMyCar.CAR_ITEM,carItem)
            })
        }
    }
    private fun getItemTouch(): ItemTouchHelper? {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

               val position = viewHolder.adapterPosition
                listRepairCar.clear()
                listRepairCar.addAll(adapterRepairCar.repairCarList)
                MyDialog.dialogDel(
                    activity as AppCompatActivity,
                    object : MyDialog.SelectYesNO {
                        override fun onClickType(type: MyDialog.ClickType) {
                            if(type== MyDialog.ClickType.NO) {
                                adapterRepairCar.updateAdapter(listRepairCar)
                            }  else if(type== MyDialog.ClickType.YES){
                                myLog("id=${listRepairCar[position].id}")
                                model.deleteRepairCarItem(listRepairCar[position].id!!)
                                adapterRepairCar.updateAdapter(listRepairCar)
                            }
                        }
                    })
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance() = RepairFragment()
    }
}