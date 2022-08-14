package com.project.schoollibrary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_return.view.*

class ReturnAdapter(
    val dataReturnerList: ArrayList<AddBookReturnModel>
) : RecyclerView.Adapter<ReturnAdapter.ReturnViewHolder>() {

    class ReturnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_return, parent, false)

        return ReturnViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReturnViewHolder, position: Int) {
        val currentItem = dataReturnerList[position]

        holder.itemView.apply {
            tv_itemReturn_name.text = "Name : ${currentItem.nameReturn}"
            tv_itemReturn_title.text = "Title : ${currentItem.titleReturn}"
            tv_itemReturn_bookCode.text = "Book Code : ${currentItem.bookCodeReturn}"
            tv_itemReturn_date.text = "Date : ${currentItem.dateReturn}"
            tv_itemReturn_fine.text = "Fine : Rp${currentItem.fineReturn}"
        }
    }

    override fun getItemCount(): Int {
        return dataReturnerList.size
    }

}