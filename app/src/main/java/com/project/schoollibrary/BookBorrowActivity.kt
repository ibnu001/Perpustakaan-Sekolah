package com.project.schoollibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.project.schoollibrary.databinding.ActivityBookBorrowBinding

class BookBorrowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookBorrowBinding

    private lateinit var dataBookRecyclerView: RecyclerView
    private lateinit var dataBorrowerList: ArrayList<AddBookBorrowModel>

    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBorrowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBookRecyclerView = findViewById(R.id.rv_borrow_book)
        dataBookRecyclerView.layoutManager = LinearLayoutManager(this.baseContext)
        dataBookRecyclerView.setHasFixedSize(true)

        dataBorrowerList = ArrayList()

        getListBorrowBook()

        binding.fabBbAddBorrow.setOnClickListener {
            startActivity(Intent(this, AddBookBorrowActivity::class.java))
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun getListBorrowBook() {
        // read data from realtime database
        mDbRef = FirebaseDatabase.getInstance().getReference("borrower_books")

        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    dataBorrowerList.clear()

                    for (listDataBook in snapshot.children) {
                        val listBook = listDataBook.getValue(AddBookBorrowModel::class.java)

                        dataBorrowerList.add(listBook!!)
                    }
                    dataBookRecyclerView.adapter = BookBorrowAdapter(dataBorrowerList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}