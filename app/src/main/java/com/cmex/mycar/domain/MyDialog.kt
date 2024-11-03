package com.cmex.mycar.domain

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.cmex.mycar.R
import com.cmex.mycar.databinding.DialogDeleteBinding
import com.cmex.mycar.databinding.DialogImageBinding
import com.cmex.mycar.databinding.DialogPdfBinding
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView


object MyDialog {
    private lateinit var dialog: Dialog

    fun dialogDel( context: Context, onClick: MyDialog.SelectYesNO) {
        dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val binding = DialogDeleteBinding.inflate(inflater)

        dialog.window!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.fon_white
            )
        )

        dialog.setContentView(binding.root)
        val windowLP = dialog.window?.attributes;
        windowLP?.width = 850
        windowLP?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = windowLP
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        dialog.setCancelable(false)

        binding.tvTitleDialog.setShadowLayer(5f,-5f,5f,context.getColor(R.color.gray0))
        binding.ibNoDialog.setOnClickListener {
            onClick.onClickType(ClickType.NO)
            dialog.dismiss()
        }
        binding.ibYesDialog.setOnClickListener {
            onClick.onClickType(ClickType.YES)
            dialog.dismiss()
        }

    }
    fun dialogViewerImage( context:Context, uriImage: Uri) {
        dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val binding = DialogImageBinding.inflate(inflater)

        dialog.window!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.fon_dialog_image
            )
        )

        dialog.setContentView(binding.root)
        val windowLP = dialog.window?.attributes;
        windowLP?.width =ViewGroup.LayoutParams.MATCH_PARENT
        windowLP?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = windowLP
        dialog.setCanceledOnTouchOutside(false)
        binding.ivDialogImage.setImage(ImageSource.uri(uriImage))
        binding.ivDialogImage.orientation = SubsamplingScaleImageView.ORIENTATION_90
        dialog.show()
        binding.ibRotation.setOnClickListener {
            when (binding.ivDialogImage.appliedOrientation) {
                90 -> binding.ivDialogImage.orientation = SubsamplingScaleImageView.ORIENTATION_180
                180 -> binding.ivDialogImage.orientation = SubsamplingScaleImageView.ORIENTATION_270
                270 -> binding.ivDialogImage.orientation = SubsamplingScaleImageView.ORIENTATION_0
                0 -> binding.ivDialogImage.orientation = SubsamplingScaleImageView.ORIENTATION_90
            }
        }
    }
    fun dialogViewerPdf( context:Context, uriImage: Uri) {
        dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val binding = DialogPdfBinding.inflate(inflater)
         myLog(" Dialog PDF ---$uriImage")
        dialog.window!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.fon_dialog_image
            )
        )

        dialog.setContentView(binding.root)
        val windowLP = dialog.window?.attributes;
        windowLP?.width =ViewGroup.LayoutParams.MATCH_PARENT
        windowLP?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = windowLP
        dialog.setCanceledOnTouchOutside(false)

        binding.ivPdfDialog.fromUri(uriImage)
       //     .enableAnnotationRendering(true)
            .defaultPage(0)
           // .spacing(10)
            .load()

        dialog.show()

    }


    interface SelectYesNO{
        fun onClickType(type:ClickType)

    }

    enum class ClickType() {
        YES,
        NO,
        DEL_ALL
    }

}


