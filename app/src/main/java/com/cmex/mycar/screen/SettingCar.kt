package com.cmex.mycar.screen

import android.graphics.Typeface.DEFAULT_BOLD
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
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
import com.cmex.mycar.databinding.ActivitySettingCarBinding
import com.cmex.mycar.domain.myLog
import com.cmex.mycar.domain.myToastShort
import com.cmex.mycar.domain.utilImageInsertEndText
import com.cmex.mycar.domain.utilSaveImageToGallery
import com.cmex.mycar.domain.utilSetColorChar
import com.cmex.mycar.domain.utilSetUriToBitmap
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class SettingCar : AppCompatActivity() {
    private lateinit var binding: ActivitySettingCarBinding
    private lateinit var cropImage: ActivityResultLauncher<CropImageContractOptions>
    private  var uriImage=ConstantsMyCar.IMAGE_EMPTY.toUri()
    private lateinit var ab:ActionBar
    private lateinit var carItem: CarItem
    private var flagState=0



    private  val model: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((this.applicationContext as MainAppMyCar).db)}


    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivitySettingCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        flagState=intent.getIntExtra(ConstantsMyCar.FLAG_STATE,0)
        if(flagState!=0) {
            getIntentCarItem()
            setScreenActivity()
        }
        //установка цвета звездочки
        binding.tvVINCar.text = utilSetColorChar(binding.tvVINCar.text.toString(),this)
        binding.tvNameCar.text = utilSetColorChar(binding.tvNameCar.text.toString(),this)
        binding.tvModelCar.text = utilSetColorChar(binding.tvModelCar.text.toString(),this)
        //__________________________________________
        checkingImageView()
        launcherPruningImage()
        initToolbar()
        }

    private fun setStatusBar(){
        val window= this.window
        window.statusBarColor= ContextCompat.getColor(this,R.color.white)
        WindowInsetsControllerCompat(window,window.decorView).isAppearanceLightStatusBars = true
    }
    private fun setScreenActivity(){
        when(flagState){
            1-> {setElementsCarItem()}
            2->{setViewing()}
        }
    }
    private fun setViewing(){
        setElementsCarItem()
        binding.toolbarSettingCar.visibility=View.GONE

    }
    private fun setElementsCarItem(){
        if(carItem.imageCar!=ConstantsMyCar.IMAGE_EMPTY) {
            uriImage=carItem.imageCar.toUri()
            Glide.with(this)
                .load(carItem.imageCar.toUri())
                .error(R.drawable.empty_car)
                .into(binding.ivCar)
            binding.ivCar.visibility=View.VISIBLE
        }  else binding.ivCar.visibility=View.GONE
        binding.etNameCar.setText(carItem.nameCar)
        binding.etModelCar.setText(carItem.model)
        binding.etVINCar.setText(carItem.vinCar)
        binding.etYearCar.setText(carItem.yearCar)
        binding.etMileAgeCar.setText(carItem.mileage)
        binding.etFuel.setText(carItem.fuel)
        binding.etSizeWheelCar.setText(carItem.sizeWheel)
        binding.etINFO.setText(carItem.addInfo)
    }

    private fun settingCarItem(): CarItem {

        return CarItem(
            null,
            binding.etNameCar.text.toString(),
            binding.etModelCar.text.toString(),
            binding.etVINCar.text.toString(),
            binding.etYearCar.text.toString(),
            binding.etMileAgeCar.text.toString(),
            binding.etFuel.text.toString(),
            binding.etSizeWheelCar.text.toString(),
            binding.etINFO.text.toString(),
            uriImage.toString()
        )
    }
    private fun updateCarItem():CarItem{
        return CarItem(
            carItem.id,
            binding.etNameCar.text.toString(),
            binding.etModelCar.text.toString(),
            binding.etVINCar.text.toString(),
            binding.etYearCar.text.toString(),
            binding.etMileAgeCar.text.toString(),
            binding.etFuel.text.toString(),
            binding.etSizeWheelCar.text.toString(),
            binding.etINFO.text.toString(),
            uriImage.toString()
        )
    }
    private fun checkingImageView(){
        if(uriImage.toString()==ConstantsMyCar.IMAGE_EMPTY){
            binding.ivCar.visibility=View.GONE
        } else  binding.ivCar.visibility=View.VISIBLE
    }

    private  fun getIntentCarItem(){

          @Suppress("DEPRECATION")
            carItem =
                intent.getSerializableExtra(ConstantsMyCar.CAR_ITEM) as CarItem

        model.carItem.value=carItem
    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbarSettingCar)
        ab=supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        Glide.with(this)
            .load(R.drawable.uaz_gifka)
            .override(160,160)
            .fitCenter()
            .error(R.drawable.box_empty)
            .into(binding.iconTbSettingCar)
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

                if(flagState==0) {
                    model.carItem.value = settingCarItem()
                    carItem=settingCarItem()
                } else {
                    model.carItem.value=updateCarItem()
                    carItem=updateCarItem()

                }
                if(binding.etModelCar.text.toString()=="" || binding.etNameCar.text.toString()==""
                    || binding.etVINCar.text.toString()==""){
                    onSnack(binding.toolbarSettingCar.findViewById(R.id.saveCar))
                } else {
                    myLog("${carItem.imageCar}")
                    if(carItem.imageCar!="empty") {
                        utilSaveImageToGallery(utilSetUriToBitmap(uriImage, this), this)
                    }
                    if (flagState==0) model.insertCarItem( settingCarItem())
                    else model.updateCarItem(carItem)
                    myToastShort(this,"save")
                   finish()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }
    //+++++++++++++++Работа с камерой обрезка изображения++++++++++++++++
    private fun launcherPruningImage(){
          cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                uriImage = result.uriContent!!
                Glide.with(this)
                    .load(uriImage)
                    .error(R.drawable.empty_car)
                    .into(binding.ivCar)
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
//+++++++++++++++++++++++++++++++ end +++++++++++++++++++++++++++++++++++++++

    private fun onSnack(view:View){
        val behavior = BaseTransientBottomBar.Behavior().apply {
            setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY)
        }
        val snackBar = Snackbar.make(view, getString(R.string.snackBar_text),Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(utilImageInsertEndText(this,"")){}

        snackBar.setBackgroundTint(getColor(R.color.white))
        snackBar.behavior=behavior
        val snackBarView = snackBar.view

        snackBarView.setBackgroundResource(R.drawable.fon_2) //не работает только углы скругляет работает style
        val snackBarTV=snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackBarTV.textSize=16F

        snackBarTV.setTextColor(getColor(R.color.red1))
        snackBarTV.setShadowLayer(2f,-2f,2f,getColor(R.color.gray))
        snackBarTV.typeface= DEFAULT_BOLD

        val snackBarActionTv=snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        snackBarActionTv.setTextColor(getColor(R.color.red1))
        snackBarActionTv.textSize=20f

        snackBar.show()
    }
}