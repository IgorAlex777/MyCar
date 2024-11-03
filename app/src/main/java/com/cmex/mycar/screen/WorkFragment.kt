package com.cmex.mycar.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmex.mycar.R
import com.cmex.mycar.data.AdapterWorkCar
import com.cmex.mycar.data.CarItem
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.data.MainViewModel
import com.cmex.mycar.data.WorkCar
import com.cmex.mycar.databinding.ActivityInfoCarBinding
import com.cmex.mycar.databinding.FragmentWorkBinding
import com.cmex.mycar.domain.MyDialog
import com.cmex.mycar.domain.myLog


class WorkFragment : Fragment() {
private lateinit var binding: FragmentWorkBinding
    private  var carItem: CarItem?=null
    private  var listWorksCar= arrayListOf<WorkCar>()
    private lateinit var adapterWorkCar: AdapterWorkCar
    private lateinit var bindingInfo: ActivityInfoCarBinding

    private val model: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainAppMyCar).db)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentWorkBinding.inflate(inflater,container,false)
        return (binding.root)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateObserver()
        bindingInfo= ActivityInfoCarBinding.inflate(layoutInflater)

      /*  ab=infoCar.supportActionBar!!
        ab.setIcon(R.drawable.to_car)
        ab.setDisplayShowTitleEnabled(true)
      //  ab.title=getString(R.string.TO_Ab)+" "+carItem?.model
        bindingInfo.ivIconApp.visibility=View.GONE*/
        bindingInfo.bottomInfo.selectedItemId=R.id.workFragment



        activity?.onBackPressedDispatcher?.addCallback(this ) {
            activity?.finish()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickAddWorkCar()
        initRecyclerView()
    }
    private fun updateObserver() {
        model.carItem.observe(activity as AppCompatActivity) {item->
            carItem = item

            if (carItem != null) {
                model.readItemByIdCarWork(carItem!!.id!!).observe(activity as AppCompatActivity) {
                    adapterWorkCar.updateAdapter(it)
                    listWorksCar.addAll(it)
                    if (it.isNotEmpty()) binding.ivEmptyWork.visibility = View.GONE
                    else binding.ivEmptyWork.visibility = View.VISIBLE

                }
            }
        }
    }


    private fun initRecyclerView(){
        adapterWorkCar= AdapterWorkCar()
        binding.rvWorkFragment.layoutManager=LinearLayoutManager(activity)
        binding.rvWorkFragment.adapter=adapterWorkCar
        getItemTouch().attachToRecyclerView(binding.rvWorkFragment)
    }
    private fun onClickAddWorkCar(){
        binding.ibAddWorkCar.setOnClickListener {
            startActivity(Intent(activity,SettingWork::class.java).apply {
              putExtra(ConstantsMyCar.CAR_ITEM,carItem)
            })
        }
    }
    private fun getItemTouch(): ItemTouchHelper {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

              val  position = viewHolder.adapterPosition
                listWorksCar.clear()
                listWorksCar.addAll(adapterWorkCar.worksCarList)
                MyDialog.dialogDel(
                    activity as AppCompatActivity,
                    object : MyDialog.SelectYesNO {
                        override fun onClickType(type: MyDialog.ClickType) {
                            if(type== MyDialog.ClickType.NO) {
                                adapterWorkCar.updateAdapter(listWorksCar)
                            }  else if(type== MyDialog.ClickType.YES){
                                myLog("id=${listWorksCar[position].id}")
                                model.deleteWorkCarItem(listWorksCar[position].id!!)
                                adapterWorkCar.updateAdapter(listWorksCar)
                            }
                        }
                    })
            }
        })
    }

    companion object {
        fun newInstance() = WorkFragment()

    }
}