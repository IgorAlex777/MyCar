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
import com.bumptech.glide.Glide
import com.cmex.mycar.R
import com.cmex.mycar.data.AdapterDocCar
import com.cmex.mycar.data.AdapterRepairCar
import com.cmex.mycar.data.CarItem
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.data.DocumentsCar
import com.cmex.mycar.data.MainViewModel
import com.cmex.mycar.data.RepairCar
import com.cmex.mycar.databinding.ActivityInfoCarBinding
import com.cmex.mycar.databinding.FragmentDocumentsBinding
import com.cmex.mycar.domain.MyDialog
import com.cmex.mycar.domain.myLog
import com.cmex.mycar.domain.utilOpenFragment


class DocumentsFragment : Fragment(),AdapterDocCar.Listener {
   private lateinit var binding:FragmentDocumentsBinding

   private lateinit var bindingInfo:ActivityInfoCarBinding
    private  var carItem: CarItem?=null
    private  var listDocumentsCar= arrayListOf<DocumentsCar>()
    private lateinit var adapterDocCar: AdapterDocCar

    private val model: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainAppMyCar).db)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingInfo= ActivityInfoCarBinding.inflate(layoutInflater)


        activity?.onBackPressedDispatcher?.addCallback(this ) {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDocumentsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        updateObserver()
        onClickAddDoc()

    }
    private fun updateObserver() {
        model.carItem.observe(activity as AppCompatActivity) { item->
            carItem = item


           if (carItem != null) {
                model.readDocumentsCar(carItem!!.id!!).observe(activity as AppCompatActivity) {

                    listDocumentsCar.addAll(it)
                    if (it.isNotEmpty()) binding.ivEmptyDoc.visibility = View.GONE
                    else binding.ivEmptyDoc.visibility = View.VISIBLE
                    adapterDocCar.updateAdapter(it)
                }
            }
        }
    }


    private fun initRecyclerView(){
        adapterDocCar= AdapterDocCar(this)
        binding.rvDoc.layoutManager= LinearLayoutManager(activity,
            LinearLayoutManager.HORIZONTAL,false)
        binding.rvDoc.adapter=adapterDocCar
       // getItemTouch()?.attachToRecyclerView(binding.rvDoc)
    }
    private fun onClickAddDoc(){
        binding.ibAddDocumentsCar.setOnClickListener {
            startActivity(Intent(activity,SettingDocuments::class.java).apply {
                putExtra(ConstantsMyCar.CAR_ITEM,carItem)
            })
        }
    }
    private fun getItemTouch(): ItemTouchHelper? {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP or ItemTouchHelper.DOWN) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

             val position = viewHolder.adapterPosition
                listDocumentsCar.clear()
                listDocumentsCar.addAll(adapterDocCar.docList)
                MyDialog.dialogDel(
                    activity as AppCompatActivity,
                    object : MyDialog.SelectYesNO {
                        override fun onClickType(type: MyDialog.ClickType) {
                            if(type== MyDialog.ClickType.NO) {
                                adapterDocCar.updateAdapter(listDocumentsCar)
                            }  else if(type== MyDialog.ClickType.YES){
                                myLog("id=${listDocumentsCar[position].id}")
                                model.deleteDocumentsCar(listDocumentsCar[position].id!!)
                                adapterDocCar.updateAdapter(listDocumentsCar)
                            }
                        }
                    })
            }
        })
    }
    companion object {

        @JvmStatic
        fun newInstance() = DocumentsFragment()
    }

    override fun onClickDelete(documentsCar: DocumentsCar) {
        model.deleteDocumentsCar(documentsCar.id!!)
        //adapterDocCar.updateAdapter(listDocumentsCar)
    }
}