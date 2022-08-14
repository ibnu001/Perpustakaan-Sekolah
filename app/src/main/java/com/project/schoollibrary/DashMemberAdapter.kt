package com.project.schoollibrary

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.activity_book.view.*
import kotlinx.android.synthetic.main.item_list_books.view.*

class DashMemberAdapter(
    val dataBookList: ArrayList<AddBookModel>
) : RecyclerView.Adapter<DashMemberAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_books, parent, false)

        return BookViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = dataBookList[position]

        holder.itemView.apply {
            tv_itemBook_titleBook.text = currentItem.title
            tv_itemBook_author.text = "by ${currentItem.author}"
            tv_itemBook_available.text = currentItem.available

            Glide.with(holder.itemView)
                .load(currentItem.coverBook)
                .placeholder(R.drawable.ic_baseline_book_24)
                .error(R.drawable.ic_baseline_book_24)
                .fitCenter()
                .transform(RoundedCorners(20))
                .into(iv_itemBook_coverBook)

            // data buku dimasukkan ke activity
            holder.itemView.setOnClickListener {
                val intent = Intent(context, BookActivity::class.java)

                intent.putExtra("title", currentItem.title)
                intent.putExtra("author", currentItem.author)
                intent.putExtra("edition", currentItem.edition)
                intent.putExtra("publishing", currentItem.publishing)
                intent.putExtra("language", currentItem.language)
                intent.putExtra("bookCode", currentItem.bookCode)
                intent.putExtra("location", currentItem.location)
                intent.putExtra("description", currentItem.description)
                intent.putExtra("available", currentItem.available)
                intent.putExtra("coverBook", currentItem.coverBook)

                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataBookList.size
    }
}