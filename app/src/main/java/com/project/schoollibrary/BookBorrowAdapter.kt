package com.project.schoollibrary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_books.view.*
import kotlinx.android.synthetic.main.item_list_borrow.view.*

class BookBorrowAdapter(
    val dataBorrowerList: ArrayList<AddBookBorrowModel>
) : RecyclerView.Adapter<BookBorrowAdapter.BorrowViewHolder>() {

    class BorrowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BorrowViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_borrow, parent, false)

        return BorrowViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BorrowViewHolder, position: Int) {
        val currentItem = dataBorrowerList[position]

        holder.itemView.apply {
            tv_itemBorrow_name.text = "Name : ${currentItem.nameBorrow}"
            tv_itemBorrow_title.text = "Title : ${currentItem.titleBorrow}"
            tv_bookBorrow_bookCode.text = "Book Code : ${currentItem.bookCodeBorrow}"
            tv_bookBorrow_date.text = "Date : ${currentItem.dateBorrow}"
        }
    }

    override fun getItemCount(): Int {
        return dataBorrowerList.size
    }


}
