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
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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
import com.cmex.mycar.data.WorkCar
import com.cmex.mycar.databinding.ActivitySettingWorkBinding
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

class SettingWork : AppCompatActivity() {
    private lateinit var binding:ActivitySettingWorkBinding
    private  var uriImage:Uri=ConstantsMyCar.IMAGE_EMPTY.toUri()
    private lateinit var ab:ActionBar
    private lateinit var workCar: WorkCar
    private lateinit var cropImage: ActivityResultLauncher<CropImageContractOptions>
    private lateinit var carItem:CarItem
     private var flagState=0

    private  val model: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((this.applicationContext as MainAppMyCar).db)}

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        super.onCreate(savedInstanceState)
        binding= ActivitySettingWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){

        flagState=intent.getIntExtra(ConstantsMyCar.FLAG_STATE,0)
        if (flagState!=0){
            getIntentWorkItem()
        } else  getIntentCarItem()
        //установка цвета звездочки
        binding.tvMileAgeWork.text = utilSetColorChar(binding.tvMileAgeWork.text.toString(),this)
        binding.tvNameWorkCar.text = utilSetColorChar(binding.tvNameWorkCar.text.toString(),this)
        binding.tvMainWorkCar.text = utilSetColorChar(binding.tvMainWorkCar.text.toString(),this)
        //__________________________________________
        setScreenActivity()
        checkingImageView()
        launcherPruningImage()
        initToolbar()
        updateObserverWorkCarItem()

    }

    private fun setStatusBar(){
        val window= this.window
        window.statusBarColor= ContextCompat.getColor(this,R.color.white)
        WindowInsetsControllerCompat(window,window.decorView).isAppearanceLightStatusBars = true
    }
    private fun setScreenActivity(){
        when(flagState){
            0->setImageView()
            1-> {setElementsWorkItem()}
            2->{setViewing()}
        }
    }
    private fun setViewing(){
        setElementsWorkItem()
        binding.toolbarWorkCar.visibility=View.GONE

    }
    private fun setImageView(){
        if(uriImage.toString()!=ConstantsMyCar.IMAGE_EMPTY) {
            binding.ivWorkCar.setImageURI(uriImage)
            binding.ivWorkCar.visibility=View.VISIBLE
        }  else binding.ivWorkCar.visibility=View.GONE
    }
    private fun setElementsWorkItem(){
        if(workCar.imageWork!=ConstantsMyCar.IMAGE_EMPTY) {
            binding.ivWorkCar.setImageURI(workCar.imageWork.toUri())
            binding.ivWorkCar.visibility=View.VISIBLE
        }  else binding.ivWorkCar.visibility=View.GONE
        binding.etNameWorkCar.setText(workCar.workCar)
        binding.etMainWorkCar.setText(workCar.mainWorks)
        binding.etAdditionWorkCar.setText(workCar.additionWork)
        binding.etMileAgeWork.setText(workCar.mileAge)
        binding.etMileAgeWork.setText(workCar.mileAge)
        binding.etPriceWorkCar.setText(workCar.priceWorks)

    }

    private fun getIntentWorkItem(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            workCar = intent.getSerializableExtra(
                ConstantsMyCar.WORK_ITEM,
                WorkCar::class.java
            ) as WorkCar

        } else {
            @Suppress("DEPRECATION")
            workCar =
                intent.getSerializableExtra(ConstantsMyCar.WORK_ITEM) as WorkCar
        }
       uriImage=workCar.imageWork.toUri()
        model.workCarItem.value=workCar
    }

    private fun getIntentCarItem(){

            @Suppress("DEPRECATION")
            carItem =
                intent.getSerializableExtra(ConstantsMyCar.CAR_ITEM) as CarItem

          uriImage=carItem.imageCar.toUri()
        model.carItem.value=carItem
    }
    private fun updateWorkItem():WorkCar{
        return WorkCar(
            workCar.id,
            workCar.nameCar,
            workCar.modelCar,
            workCar.vinCar,
            workCar.idCar,
            binding.etNameWorkCar.text.toString(),
            binding.etMainWorkCar.text.toString(),
            binding.etAdditionWorkCar.text.toString(),
            binding.etMileAgeWork.text.toString(),
            binding.etPriceWorkCar.text.toString(),
            utilGetDate(System.currentTimeMillis()),
            workCar.imageWork
        )
    }
    private fun settingWorkCar(): WorkCar {
        return WorkCar(
            null,
            carItem.nameCar,
            carItem.model,
            carItem.vinCar,
            carItem.id!!,
            binding.etNameWorkCar.text.toString(),
            binding.etMainWorkCar.text.toString(),
            binding.etAdditionWorkCar.text.toString(),
            binding.etMileAgeWork.text.toString(),
            binding.etPriceWorkCar.text.toString(),
            utilGetDate(System.currentTimeMillis()),
            uriImage.toString()
        )
    }
    private fun checkingImageView(){
        if(uriImage.toString()== ConstantsMyCar.IMAGE_EMPTY){
            binding.ivWorkCar.visibility= View.GONE
        } else  binding.ivWorkCar.visibility= View.VISIBLE
    }
    private fun updateObserverWorkCarItem(){
        model.workCarItem.observe(this){
            workCar=it
        }
    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbarWorkCar)
        ab=supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        Glide.with(this)
            .load(R.drawable.uaz_gifka)
            .override(160,160)
            .fitCenter()
            .error(R.drawable.box_empty)
            .into(binding.iconTbWork)
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
               if(flagState==0) model.workCarItem.value=settingWorkCar()
                else model.workCarItem.value=updateWorkItem()

                if(binding.etNameWorkCar.text.toString()=="" || binding.etMainWorkCar.text.toString()==""
                    || binding.etMileAgeWork.text.toString()==""){
                    onSnack(binding.toolbarWorkCar.findViewById(R.id.saveCar))
                } else {
                    utilSaveImageToGallery(utilSetUriToBitmap(uriImage,this),this)
                    if(flagState==0) model.insertWorkCar( settingWorkCar())
                    else model.updateWorkCarItem(updateWorkItem())
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
                    .into(binding.ivWorkCar)
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