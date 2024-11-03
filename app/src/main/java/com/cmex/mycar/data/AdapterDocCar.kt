package com.cmex.mycar.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmex.mycar.R
import com.cmex.mycar.databinding.ItemDocumentBinding
import com.cmex.mycar.domain.MyDialog
import com.cmex.mycar.domain.myLog
import com.cmex.mycar.domain.utilCheckTypeUri
import com.cmex.mycar.screen.SettingDocuments

class AdapterDocCar( val listener:Listener) :RecyclerView.Adapter<AdapterDocCar.HolderDocCar>(){
    private lateinit var binding: ItemDocumentBinding
    val docList= arrayListOf<DocumentsCar>()
    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDocCar {
        val inflanter= LayoutInflater.from(parent.context)
        context=parent.context
        binding= ItemDocumentBinding.inflate(inflanter,parent,false)
        return HolderDocCar(binding, listener)
    }

    override fun getItemCount(): Int {
        return docList.size
    }

    override fun onViewAttachedToWindow(holder: HolderDocCar) {
        super.onViewAttachedToWindow(holder)
       holder.setDocument(docList[holder.adapterPosition])
    }
    override fun onBindViewHolder(holder: HolderDocCar, position: Int) {
        return holder.setData(docList[position])
    }

    class HolderDocCar(val binding: ItemDocumentBinding,val listener: Listener) : RecyclerView.ViewHolder(binding.root) {
        val context = binding.root.context!!

        fun setData(documentsCar: DocumentsCar) {
            binding.tvTitleDocItem.text = documentsCar.titleDocuments

            clickEditDocument(documentsCar)
               clickDeleteDocument(documentsCar)
               clickViewingDocument(documentsCar)
        }

 fun setDocument(documentsCar: DocumentsCar){
    val textUri=documentsCar.uriDocuments
    val typeDocUri=textUri.substring(textUri.length-3)
    myLog("typeUri=$typeDocUri")
    if(typeDocUri==ConstantsMyCar.TYPE_PDF){
        binding.ivDocItem.visibility=View.GONE
        binding.pdfViewerItem.visibility=View.VISIBLE
        binding.pdfViewerItem.fromUri(textUri.toUri())
            .enableDoubletap(true)
            .defaultPage(0)
           // .spacing(10)
            .load()
    } else {
        binding.ivDocItem.visibility=View.VISIBLE
        binding.pdfViewerItem.visibility=View.GONE
        Glide.with(context)
            .load(documentsCar.uriDocuments.toUri())
            .override(1400,1800)
            .fitCenter()
            .error(R.drawable.doc_car)
            .into(binding.ivDocItem)
    }
}
        private fun clickViewingDocument(documentsCar: DocumentsCar){
            binding.ibtnViewingItemDoc.setOnClickListener {
                    if (utilCheckTypeUri(documentsCar.uriDocuments)) {
                        MyDialog.dialogViewerPdf(context, documentsCar.uriDocuments.toUri())
                    } else {
                        MyDialog.dialogViewerImage(context, documentsCar.uriDocuments.toUri())
                    }
                }
        }

        private fun clickEditDocument(documentsCar: DocumentsCar){
            binding.ibEditDoc.setOnClickListener {
                val intent= Intent(context, SettingDocuments::class.java).apply {
                    putExtra(ConstantsMyCar.DOC_ITEM,documentsCar)
                    putExtra(ConstantsMyCar.FLAG_STATE,1)
                }
                context.startActivity(intent)
            }
        }
        private fun clickDeleteDocument(documentsCar: DocumentsCar){
            binding.ibDeleteDoc.setOnClickListener {
                MyDialog.dialogDel(context,object :MyDialog.SelectYesNO{
                    override fun onClickType(type: MyDialog.ClickType) {
                      if(type==MyDialog.ClickType.YES){
                          listener.onClickDelete(documentsCar)
                      }
                    }

                })

            }
        }
    }

    fun updateAdapter(newList: List<DocumentsCar>){
        val diffUtil= DiffUtil.calculateDiff(DiffUtilDocumentsCarList(docList,newList))
        diffUtil.dispatchUpdatesTo(this)
        docList.clear()
        docList.addAll(newList)
        notifyDataSetChanged()
    }
    interface Listener{
        fun onClickDelete(documentsCar: DocumentsCar)
    }
}