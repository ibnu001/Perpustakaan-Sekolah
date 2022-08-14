package com.project.schoollibrary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_anggota.view.*

class AnggotaAdapter(
    val dataAnggotaList: ArrayList<AddAnggotaModel>
) : RecyclerView.Adapter<AnggotaAdapter.AnggotaViewHolder>() {

    class AnggotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnggotaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_anggota, parent, false)

        return  AnggotaViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AnggotaViewHolder, position: Int) {
        val currentItem = dataAnggotaList[position]

        holder.itemView.apply {
            tv_itemAnggota_uidAnggota.text = "UID Anggota : ${currentItem.uidAnggota}"
            tv_itemAnggota_fName.text = "Full Name : ${currentItem.fullName}"
            tv_itemAnggota_Class.text = "Class : ${currentItem.kelas}"
        }
    }

    override fun getItemCount(): Int {
        return dataAnggotaList.size
    }
}