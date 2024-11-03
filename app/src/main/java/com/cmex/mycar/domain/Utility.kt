package com.cmex.mycar.domain

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cmex.mycar.R
import com.cmex.mycar.data.ConstantsMyCar
import com.cmex.mycar.screen.InfoCar
import java.text.SimpleDateFormat
import java.util.Calendar

fun Fragment.utilOpenFragment(fragment: Fragment) {
    (activity as AppCompatActivity).supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        .replace(R.id.clContainer, fragment)
        .commit()
}

fun AppCompatActivity.utilOpenFragment(fragment: Fragment) {

    if (supportFragmentManager.fragments.isNotEmpty()) {
        val nameFragment = supportFragmentManager.fragments[0].javaClass
        if (nameFragment == fragment.javaClass) return
    }
    supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        .replace(R.id.clContainer, fragment)
        .commit()
}

fun myToastLong(context: Context, string: String) {
    Toast.makeText(context, string, Toast.LENGTH_LONG).show()
}
fun myToastShort(context: Context, string: String) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
}

fun myLog(text:String){
 Log.d("CMEX",text)
}


fun utilGetTime(timeInMileSec: Long): String {
    val timeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMileSec
    // timeFormat.timeZone= TimeZone.getTimeZone("")
    return timeFormat.format(calendar.time)
}
fun utilGetDate(timeInMileSec: Long): String {
    val timeFormat = SimpleDateFormat("dd.MM.yyyy")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMileSec
    return timeFormat.format(calendar.time)
}
fun utilGetHhAndMm(timeInMileSec: Long): String {
    val timeFormat = SimpleDateFormat("HH:mm")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMileSec
    return timeFormat.format(calendar.time)
}
fun utilCheckingPermission(context: Context,permission:String):Boolean {
    return when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(context, permission) -> true
        else -> false
    }
}

fun utilCheckingTwoPermissions(context: Context):Boolean{
    return utilCheckingPermission(context,Manifest.permission.POST_NOTIFICATIONS)&&
        Settings.canDrawOverlays(context)
}


fun utilUriPermission(uriImage: Uri, context: Context){
    context.contentResolver.takePersistableUriPermission(
        uriImage,
        Intent.FLAG_GRANT_READ_URI_PERMISSION)
}
@SuppressLint("BatteryLife")
fun utilIsIgnoringBatteryOptimizations(name:String, context: Context){
    val intent = Intent()
    val pm: PowerManager = context.getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
    if (pm.isIgnoringBatteryOptimizations(name)) {
        intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
    } else {
        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        intent.data = Uri.parse("package:${name}")
    }
    context.startActivity(intent)
}
fun utilSetColorChar(text: String,context: Context): SpannableString {
    val color1 = ContextCompat.getColor(context, R.color.red1)
    val spanText= SpannableString(text)
       spanText.setSpan(
            ForegroundColorSpan(color1),
            text.length - 2,
            text.length - 1,
            0
        )
    return spanText
}
fun utilImageInsertStartText(context: Context,text:String) :SpannableStringBuilder{
    val spanText=SpannableStringBuilder()
        spanText.append("-$text")
        spanText .setSpan(
        ImageSpan(context,R.drawable.exit),
           spanText .indexOf("-"),
            spanText.indexOf("-")+1,
            0
        )
    return spanText
}
fun utilImageInsertEndText(context: Context,text:String) :SpannableStringBuilder{
    val spanText=SpannableStringBuilder()
    spanText.append("$text-")
    spanText .setSpan(
        ImageSpan(context,R.drawable.exit),
        spanText .indexOf("-"),
        spanText.indexOf("-")+1,
        0
    )
    return spanText
}
fun utilSharePdf(uri: Uri): Intent {
    val intent=Intent(Intent.ACTION_SEND)
    intent.type="application/pdf"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    return intent
}
fun utilShareImage(uri: Uri): Intent {
    val intent=Intent(Intent.ACTION_SEND)
    intent.type="application/image"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    return intent
}
fun utilCheckTypeUri(uriText:String):Boolean{
    return uriText.substring(uriText.length-3)==ConstantsMyCar.TYPE_PDF

}

fun utilSaveImageToGallery(bitmap: Bitmap,context:Context) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val nameApp=context.getString(R.string.app_name)
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$nameApp")
    }
    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    if (uri != null) {
        contentResolver.openOutputStream(uri).use { outputStream ->
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)

            } else {
                Toast.makeText(context, "Failed to save image to gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

 fun utilSetUriToBitmap(uriImage: Uri,context: Context): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uriImage
            )
        }
        else -> {
            val source = ImageDecoder.createSource(context.contentResolver, uriImage)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}