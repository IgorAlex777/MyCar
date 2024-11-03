package com.cmex.mycar.screen

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.cmex.mycar.R
import com.cmex.mycar.data.CarItem
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.data.MainViewModel
import com.cmex.mycar.databinding.ActivityInfoCarBinding
import com.cmex.mycar.domain.myLog
import com.cmex.mycar.domain.myToastShort
import com.cmex.mycar.domain.utilOpenFragment


class InfoCar : AppCompatActivity() {
     private lateinit var binding:ActivityInfoCarBinding
    private lateinit var ab: ActionBar
    private lateinit var carItem: CarItem


    private  val model: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((this.applicationContext as MainAppMyCar).db)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        binding= ActivityInfoCarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentCarItem()

        init()
    }

    private fun init(){
        onClickBottomMenuInfo()
        initToolbar()
     }

    private fun setStatusBar(){
        val window= this.window
        window.statusBarColor= ContextCompat.getColor(this,R.color.white)
        WindowInsetsControllerCompat(window,window.decorView).isAppearanceLightStatusBars = true
    }
    private fun getIntentCarItem(){

           @Suppress("DEPRECATION")
           carItem =
               intent.getSerializableExtra(ConstantsMyCar.CAR_ITEM) as CarItem

       model.carItem.value=carItem

   }

    private fun onClickBottomMenuInfo(){
        binding.bottomInfo.setOnItemSelectedListener {
            when(it.itemId){
                R.id.workFragment->{
                    ab.setIcon(R.drawable.to_car)
                    ab.setDisplayShowTitleEnabled(true)
                    ab.title=getString(R.string.TO_Ab)+" "+carItem.model
                  binding.ivIconApp.visibility=View.GONE
                    utilOpenFragment(WorkFragment.newInstance())

                }
                R.id.repairFragment->{
                    ab.setIcon(R.drawable.repair)
                    ab.title=getString(R.string.repairAB)+" "+carItem.model
                    utilOpenFragment(RepairFragment.newInstance())

                }
                R.id.documentsFragment->{
                    ab.setIcon(R.drawable.doc_car)
                    ab.title=getString(R.string.docCar)+" "+carItem.model
                    utilOpenFragment(DocumentsFragment.newInstance())

                }
            }
            true
        }
    }



  /*  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tb_info,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_car->{
                myToastShort(this,"edit Car")

            }

        }
        return super.onOptionsItemSelected(item)
    }*/
    private fun initToolbar(){
        setSupportActionBar(binding.toolbarInfoCar)
        ab= supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        Glide.with(this)
            .load(R.drawable.uaz_gifka)
            .override(160,160)
            .fitCenter()
            .error(R.drawable.box_empty)
            .into(binding.ivIconApp)
        binding.bottomInfo.selectedItemId=R.id.workFragment

    }
}