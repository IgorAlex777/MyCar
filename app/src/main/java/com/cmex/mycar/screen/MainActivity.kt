package com.cmex.mycar.screen

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmex.mycar.R
import com.cmex.mycar.data.AdapterCarItem
import com.cmex.mycar.data.CarItem
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.data.DocumentsCar
import com.cmex.mycar.data.MainViewModel
import com.cmex.mycar.data.RepairCar
import com.cmex.mycar.data.WorkCar
import com.cmex.mycar.databinding.ActivityMainBinding
import com.cmex.mycar.domain.MyDialog
import com.cmex.mycar.domain.myLog

import com.cmex.mycar.domain.myToastShort
import com.cmex.mycar.domain.utilCheckingPermission
import com.cmex.mycar.domain.utilIsIgnoringBatteryOptimizations

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var multiplePermissions: ActivityResultLauncher<Array<String>>
    private lateinit var ab:ActionBar
    private lateinit var adapterCarItem: AdapterCarItem
    private  var carList=ArrayList<CarItem>()


    private  val model: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((this.applicationContext as MainAppMyCar).db)}

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isPermissionGranted()) {
            val permissions = arrayOf(WRITE_EXTERNAL_STORAGE)
            for (i in permissions.indices) {
                requestPermission(permissions[i], i)
            }
        }

     init()
    }
    private fun isPermissionGranted(): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    private fun init(){

        registerPermissions()
        setPermission()
        clickAddCar()
        initToolbar()
        setRecycler()
        updateObserver()
        getItemTouch()?.attachToRecyclerView(binding.rwSelectCar)
    }
     private fun setStatusBar(){
    val window= this.window
    window.statusBarColor=ContextCompat.getColor(this,R.color.white)
    WindowInsetsControllerCompat(window,window.decorView).isAppearanceLightStatusBars = true
      }
    override fun onResume() {
        super.onResume()
      //  updateObserver()
    }
    private fun setPermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            if(!utilCheckingPermission(this, Manifest.permission.POST_NOTIFICATIONS)) {
                multiplePermissions.launch(arrayOf( Manifest.permission.POST_NOTIFICATIONS))
                myLog(Manifest.permission.POST_NOTIFICATIONS)
            }  //else myToastLong(this,getString(R.string.yesPermission))

            checkingBatteryOptimizations()
        }
    }
    private fun checkingBatteryOptimizations() {
        val pm: PowerManager = getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            utilIsIgnoringBatteryOptimizations(packageName, this)
        }
    }
    private fun registerPermissions(){
        multiplePermissions=registerForActivityResult(
            ActivityResultContracts
                .RequestMultiplePermissions()){
            if(it[Manifest.permission.POST_NOTIFICATIONS]==true){

            } else {
                myLog("NO")
            }
        }
    }
    private fun setRecycler(){
        adapterCarItem= AdapterCarItem()
        binding.rwSelectCar.layoutManager=LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        binding.rwSelectCar.adapter=adapterCarItem
    }
    private fun updateObserver(){
        model.carItemList.observe(this){
              adapterCarItem.updateAdapter(it)

            if(it.isEmpty()) binding.ivEmptyCar.visibility=View.VISIBLE
            else binding.ivEmptyCar.visibility=View.GONE
        }
    }
    private fun initToolbar(){
        setSupportActionBar(binding.toolbarMain)
        ab=supportActionBar!!
        ab.elevation=8f
        ab.setDisplayShowTitleEnabled(false)
        Glide.with(this)
            .load(R.drawable.uaz_gifka)
            .override(160,160)
            .fitCenter()
            .error(R.drawable.box_empty)
            .into(binding.ivIconApp)
    }
    private fun clickAddCar(){
        binding.ibAddCarItem.setOnClickListener {
            startActivity(Intent(this,SettingCar::class.java).apply {
                putExtra(ConstantsMyCar.FLAG_STATE,0)
            })
        }
    }

  /*  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tb_main,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_car->{
                myToastShort(this,"add Car")
                startActivity(Intent(this,SettingCar::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }*/
    //========Swipe вверх-вниз=========
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

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

               val position = viewHolder.adapterPosition
                carList.clear()
                carList.addAll(adapterCarItem.carItemList)
                MyDialog.dialogDel(
                    this@MainActivity,
                    object : MyDialog.SelectYesNO {
                        override fun onClickType(type: MyDialog.ClickType) {
                            if(type== MyDialog.ClickType.NO) {

                                adapterCarItem.updateAdapter(carList)
                            }  else if(type== MyDialog.ClickType.YES){
                                myLog("id=${carList[position].id}")
                                model.deleteCarItem(carList[position].id!!)
                                      myLog("id= ${carList[position].id}")
                                     model.deleteWorkCarItems(carList[position].id!!)
                                     model.deleteRepairCarItems(carList[position].id!!)
                                     model.deleteDocumentsCarItems(carList[position].id!!)

                                adapterCarItem.updateAdapter(carList)
                            }
                        }
                    })
            }
        })
    }

}

