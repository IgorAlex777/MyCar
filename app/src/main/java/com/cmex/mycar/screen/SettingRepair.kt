package com.cmex.mycar.screen

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.options
import com.cmex.mycar.R
import com.cmex.mycar.data.CarItem
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.data.MainViewModel
import com.cmex.mycar.data.RepairCar
import com.cmex.mycar.databinding.ActivitySettingRepairBinding
import com.cmex.mycar.domain.myLog
import com.cmex.mycar.domain.myToastShort
import com.cmex.mycar.domain.utilGetDate
import com.cmex.mycar.domain.utilImageInsertEndText
import com.cmex.mycar.domain.utilSaveImageToGallery
import com.cmex.mycar.domain.utilSetColorChar
import com.cmex.mycar.domain.utilSetUriToBitmap
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class SettingRepair : AppCompatActivity() {
    private lateinit var binding: ActivitySettingRepairBinding
    private  var uriImage: Uri = ConstantsMyCar.IMAGE_EMPTY.toUri()
    private lateinit var ab: ActionBar
    private lateinit var repairCar: RepairCar
    private lateinit var cropImage: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var carItem: CarItem
    private var flagState=0

    private  val model: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((this.applicationContext as MainAppMyCar).db)}


    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        super.onCreate(savedInstanceState)
        binding=ActivitySettingRepairBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init(){
        flagState=intent.getIntExtra(ConstantsMyCar.FLAG_STATE,0)
        if (flagState!=0){
            getIntentRepairItem()
        } else  getIntentCarItem()
        //установка цвета звездочки
        binding.tvMileAgeRepair.text = utilSetColorChar(binding.tvMileAgeRepair.text.toString(),this)
        binding.tvNameRepairCar.text = utilSetColorChar(binding.tvNameRepairCar.text.toString(),this)
        binding.tvTextRepairCar.text = utilSetColorChar(binding.tvTextRepairCar.text.toString(),this)
        //__________________________________________
        setScreenActivity()
        checkingImageView()
        launcherPruningImage()
        initToolbar()

        updateObserverRepairCarItem()
    }
    private fun setScreenActivity(){
        when(flagState){
            1-> {setElementsRepairItem()}
            2->{setViewing()}
        }
    }
    private fun setViewing(){

        setElementsRepairItem()
        binding.toolbarRepairCar.visibility= View.GONE

    }
    private fun setElementsRepairItem(){
        if(repairCar.imageRepair!=ConstantsMyCar.IMAGE_EMPTY) {
            binding.ivRepairCar.setImageURI(repairCar.imageRepair.toUri())
            binding.ivRepairCar.visibility= View.VISIBLE
        }  else binding.ivRepairCar.visibility= View.GONE
        binding.etNameRepairCar.setText(repairCar.titleRepair)
        binding.etTextRepairCar.setText(repairCar.textRepair)
        binding.etMileAgeRepair.setText(repairCar.mileAge)
        binding.etPriceRepairCar.setText(repairCar.priceRepair)

    }
    private fun updateObserverRepairCarItem(){
        model.repairCarItem.observe(this){
            repairCar=it
        }
    }
    private fun getIntentRepairItem(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            repairCar = intent.getSerializableExtra(
                ConstantsMyCar.REPAIR_ITEM,
                RepairCar::class.java
            ) as RepairCar

        } else {
            @Suppress("DEPRECATION")
            repairCar =
                intent.getSerializableExtra(ConstantsMyCar.REPAIR_ITEM) as RepairCar
        }
        uriImage=repairCar.imageRepair.toUri()
        model.repairCarItem.value=repairCar
    }

    private fun getIntentCarItem(){

            @Suppress("DEPRECATION")
            carItem =
                intent.getSerializableExtra(ConstantsMyCar.CAR_ITEM) as CarItem

        uriImage=carItem.imageCar.toUri()
        model.carItem.value=carItem
    }
    private fun updateRepairItem():RepairCar{
        return RepairCar(
            repairCar.id,
            repairCar.nameCar,
            repairCar.modelCar,
            repairCar.vinCar,
            repairCar.idCar,
            binding.etNameRepairCar.text.toString(),
            binding.etTextRepairCar.text.toString(),
            binding.etMileAgeRepair.text.toString(),
            binding.etPriceRepairCar.text.toString(),
            utilGetDate(System.currentTimeMillis()),
            repairCar.imageRepair
        )
    }
    private fun settingRepairCar(): RepairCar {
        return RepairCar(
            null,
            carItem.nameCar,
            carItem.model,
            carItem.vinCar,
            carItem.id!!,
            binding.etNameRepairCar.text.toString(),
            binding.etTextRepairCar.text.toString(),
            binding.etMileAgeRepair.text.toString(),
            binding.etPriceRepairCar.text.toString(),
            utilGetDate(System.currentTimeMillis()),
            uriImage.toString()
        )
    }
    private fun checkingImageView(){
        if(uriImage.toString()== ConstantsMyCar.IMAGE_EMPTY){
            binding.ivRepairCar.visibility= View.GONE
        } else  binding.ivRepairCar.visibility= View.VISIBLE
    }
    private fun setStatusBar(){
        val window= this.window
        window.statusBarColor= ContextCompat.getColor(this,R.color.white)
        WindowInsetsControllerCompat(window,window.decorView).isAppearanceLightStatusBars = true
    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbarRepairCar)
        ab=supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        Glide.with(this)
            .load(R.drawable.uaz_gifka)
            .override(160,160)
            .fitCenter()
            .error(R.drawable.box_empty)
            .into(binding.iconTbRepair)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tb_setting_car,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.cameraCar->{
                startCropCamera()
            }

            R.id.selectImageCar->{
                startCropStorage()
            }
            R.id.saveCar->{
                if(flagState==0) model.repairCarItem.value=settingRepairCar()
                else model.repairCarItem.value=updateRepairItem()

                if(binding.etNameRepairCar.text.toString()=="" || binding.etTextRepairCar.text.toString()==""
                    || binding.etMileAgeRepair.text.toString()==""){
                    //  myToastShort(this,"поля со звездочками должны быть заполнены")
                    onSnack(binding.toolbarRepairCar.findViewById(R.id.saveCar))
                } else {
                    utilSaveImageToGallery(utilSetUriToBitmap(uriImage,this),this)
                    if(flagState==0) model.insertRepairCar( settingRepairCar())
                    else model.updateRepairCarItem(updateRepairItem())
                    myToastShort(this,"save")
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launcherPruningImage(){
        cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                uriImage = result.uriContent!!
                Glide.with(this)
                    .load(uriImage)
                    .error(R.drawable.empty_car)
                    .into(binding.ivRepairCar)
                checkingImageView()
            } else {
                val exception = result.error
                myLog("ERROR: $exception")
            }
        }
    }

    private fun startCropCamera(){
        cropImage.launch(
            options {
                setImageSource(includeCamera = true, includeGallery = false)

            }
        )
    }
    private fun startCropStorage(){
        cropImage.launch(
            options {
                setImageSource(includeCamera = false, includeGallery = true)

            }
        )
    }

    private fun onSnack(view: View){
        val behavior = BaseTransientBottomBar.Behavior().apply {
            setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY)
        }
        //  val sbStyle= ContextThemeWrapper(this,R.style.MySnackBar)
        val snackBar = Snackbar.make(view, getString(R.string.snackBar_text), Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(utilImageInsertEndText(this,"")){}

        snackBar.setBackgroundTint(getColor(R.color.white))
        snackBar.behavior=behavior
        val snackBarView = snackBar.view

        snackBarView.setBackgroundResource(R.drawable.fon_2) //не работает только углы скругляет работает style
        val snackBarTV=snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackBarTV.textSize=16F

        snackBarTV.setTextColor(getColor(R.color.red1))
        snackBarTV.setShadowLayer(4f,-4f,4f,getColor(R.color.gray0))
        snackBarTV.typeface= Typeface.DEFAULT_BOLD

        val snackBarActionTv=snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        snackBarActionTv.setTextColor(getColor(R.color.red1))
        snackBarActionTv.textSize=20f

        snackBar.show()
    }
}