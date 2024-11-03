package com.cmex.mycar.screen

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import android.view.View
import android.widget.TextView
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
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.cmex.mycar.R
import com.cmex.mycar.data.CarItem
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.data.DocumentsCar
import com.cmex.mycar.data.MainViewModel
import com.cmex.mycar.data.RepairCar
import com.cmex.mycar.databinding.ActivitySettingDocumentsBinding
import com.cmex.mycar.domain.myLog
import com.cmex.mycar.domain.myToastShort
import com.cmex.mycar.domain.utilCheckTypeUri
import com.cmex.mycar.domain.utilImageInsertEndText
import com.cmex.mycar.domain.utilSaveImageToGallery
import com.cmex.mycar.domain.utilSetColorChar
import com.cmex.mycar.domain.utilSetUriToBitmap
import com.cmex.mycar.domain.utilShareImage
import com.cmex.mycar.domain.utilSharePdf
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class SettingDocuments : AppCompatActivity() {
    private lateinit var binding:ActivitySettingDocumentsBinding
    private  var uriImage: Uri = ConstantsMyCar.IMAGE_EMPTY.toUri()
    private lateinit var ab: ActionBar
    private  lateinit var docCar: DocumentsCar
    private lateinit var cropImage: ActivityResultLauncher<CropImageContractOptions>

    private lateinit var selectPdf: ActivityResultLauncher<Intent>
    private lateinit var carItem: CarItem
    private var flagState=0

    private  val model: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((this.applicationContext as MainAppMyCar).db)}

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        super.onCreate(savedInstanceState)
        binding= ActivitySettingDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)


     init()
    }
    private fun init(){
        flagState=intent.getIntExtra(ConstantsMyCar.FLAG_STATE,0)
        if (flagState!=0){
            getIntentDocuments()
        } else  getIntentCarItem()
        //установка цвета звездочки
        binding.tvTitleDoc.text = utilSetColorChar(binding.tvTitleDoc.text.toString(),this)
        //__________________________________________

        setScreenActivity()
        onSelectPdf()
        launcherPruningImage()
        initToolbar()
        updateObserverDocuments()
    }
    private fun setScreenActivity(){
        when(flagState){
            1-> {setElementsDocuments()}
            2->{setViewing()}
        }
    }
    private fun setStatusBar(){
        val window= this.window
        window.statusBarColor= ContextCompat.getColor(this,R.color.white)
        WindowInsetsControllerCompat(window,window.decorView).isAppearanceLightStatusBars = true
    }
    private fun setViewing(){
        setElementsDocuments()
        binding.toolbarDoc.visibility= View.GONE

    }
    private fun setElementsDocuments() {
        if (docCar.uriDocuments != ConstantsMyCar.IMAGE_EMPTY) {
            setImageOrPdf()

        } else binding.ivDocCarImageView.visibility = View.GONE
        binding.etTitleDoc.setText(docCar.titleDocuments)
    }
    private fun setImageOrPdf(){

        if (utilCheckTypeUri(docCar.uriDocuments)){
            binding.ivDocCarImageView.visibility=View.GONE
            binding.pdfImageDocBarteksc.visibility=View.VISIBLE
            binding.pdfImageDocBarteksc.fromUri(docCar.uriDocuments.toUri())
                .enableDoubletap(true)
                .defaultPage(0)
            //    .spacing(10)
                .load()
        }else{
            binding.ivDocCarImageView.visibility=View.VISIBLE
            binding.pdfImageDocBarteksc.visibility=View.GONE
            Glide.with(this)
                .load(docCar.uriDocuments.toUri())
                .override(1400,1800)
                .fitCenter()
                .error(R.drawable.doc_car)
                .into(binding.ivDocCarImageView)
        }
    }
    private fun updateObserverDocuments(){
        model.documentsCar.observe(this){
            docCar=it
        }
    }
    private fun getIntentDocuments(){

        @Suppress("DEPRECATION")
            docCar =
                intent.getSerializableExtra(ConstantsMyCar.DOC_ITEM) as DocumentsCar

        if(flagState!=0) uriImage=docCar.uriDocuments.toUri()
        model.documentsCar.value=docCar
    }

    private fun getIntentCarItem(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            carItem = intent.getSerializableExtra(
                ConstantsMyCar.CAR_ITEM,
                CarItem::class.java
            ) as CarItem

        } else {
            @Suppress("DEPRECATION")
            carItem =
                intent.getSerializableExtra(ConstantsMyCar.CAR_ITEM) as CarItem
        }

        model.carItem.value=carItem
    }
    private fun initToolbar(){
        setSupportActionBar(binding.toolbarDoc)
        ab=supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        Glide.with(this)
            .load(R.drawable.uaz_gifka)
            .override(160,160)
            .fitCenter()
            .error(R.drawable.box_empty)
            .into(binding.iconTbDocument)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tb_documents_car,menu)
        if(flagState==0){
            menu?.findItem(R.id.shareDocument)?.isVisible = false

        }

        return super.onCreateOptionsMenu(menu)

    }
    private fun checkingImageView(){
        if(uriImage.toString()== ConstantsMyCar.IMAGE_EMPTY){
            binding.ivDocCarImageView.visibility= View.GONE
        } else  binding.ivDocCarImageView.visibility= View.VISIBLE
    }
    private fun settingDocuments(): DocumentsCar {
        return DocumentsCar(
            null,
            carItem.nameCar,
            carItem.model,
            carItem.vinCar,
            carItem.id!!,
            binding.etTitleDoc.text.toString(),
            uriImage.toString()
        )
    }
    private fun updateDocuments():DocumentsCar {
        return DocumentsCar(
            docCar.id,
            docCar.nameCar,
            docCar.modelCar,
            docCar.vinCar,
            docCar.idCar,
            binding.etTitleDoc.text.toString(),
           uriImage.toString()
        )
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.selectImageDoc->{
                startCropStorageAndCamera()
            }
            R.id.selectPdf->{
                onClickSelectPdf()
            }
            R.id.shareDocument->{

                if(docCar.uriDocuments!=ConstantsMyCar.IMAGE_EMPTY ){
                if(utilCheckTypeUri(docCar.uriDocuments))
               startActivity(Intent.createChooser(utilSharePdf(docCar.uriDocuments.toUri()),docCar.titleDocuments))
                else startActivity(Intent.createChooser(utilShareImage(docCar.uriDocuments.toUri()),docCar.titleDocuments))
                    }
            }

            R.id.saveCarDoc->{
                if(flagState==0) model.documentsCar.value=settingDocuments()
                else model.documentsCar.value=updateDocuments()

                if(binding.etTitleDoc.text.toString()==""|| uriImage.toString()==ConstantsMyCar.IMAGE_EMPTY){

                    onSnack(binding.toolbarDoc.findViewById(R.id.saveCar))
                } else {
                    if(!utilCheckTypeUri(uriImage.toString())) {
                        utilSaveImageToGallery(utilSetUriToBitmap(uriImage, this), this)
                    }
                    if(flagState==0) model.insertDocumentsCar( settingDocuments())
                    else model.updateDocumentsCar(updateDocuments())
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
                    .into(binding.ivDocCarImageView)
                checkingImageView()
            } else {
                val exception = result.error
                myLog("ERROR: $exception")
            }
        }
    }

    private fun startCropStorageAndCamera() {
        // запуск Launcher выбор и обрезка изображений, камера
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )
    }

    private fun onSelectPdf() {
        selectPdf = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data?.data != null) {
                    val uri = it.data!!.data
                    if (uri != null) {
                        uriImage=uri
                        myLog("$uri")
                        contentResolver?.takePersistableUriPermission(
                            uriImage,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        binding.pdfImageDocBarteksc.visibility=View.VISIBLE
                        binding.ivDocCarImageView.visibility=View.GONE
                        binding.pdfImageDocBarteksc.fromUri(uriImage)
                            .defaultPage(0)
                          //  .spacing(10)
                            .load()
                       // checkingImageView()
                    }
                }
            }
        }
    }

    private fun onClickSelectPdf() {
        val intent= Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type="application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        selectPdf.launch(intent)
    }

    private fun onSnack(view: View){
        val behavior = BaseTransientBottomBar.Behavior().apply {
            setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY)
        }
        //  val sbStyle= ContextThemeWrapper(this,R.style.MySnackBar)
        val snackBar = Snackbar.make(view, getString(R.string.snackBar_text_doc), Snackbar.LENGTH_INDEFINITE)
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
        snackBarTV.setLines(3)

        val snackBarActionTv=snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        snackBarActionTv.setTextColor(getColor(R.color.red1))
        snackBarActionTv.textSize=20f

        snackBar.show()
    }
}