package com.project.schoollibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.project.schoollibrary.databinding.ActivityReturnBinding

class ReturnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReturnBinding

    private lateinit var dataReturnRecyclerView: RecyclerView
    private lateinit var dataReturnerList: ArrayList<AddBookReturnModel>

    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataReturnRecyclerView = findViewById(R.id.rv_return_book)
        dataReturnRecyclerView.layoutManager = LinearLayoutManager(this.baseContext)
        dataReturnRecyclerView.setHasFixedSize(true)

        dataReturnerList = ArrayList()

        getListReturnBook()

        binding.fabReturnAddReturn.setOnClickListener {
            startActivity(Intent(this, AddBookReturnActivity::class.java))
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun getListReturnBook() {
        // read data from realtime database
        mDbRef = FirebaseDatabase.getInstance().getReference("returner_books")

        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    dataReturnerList.clear()

                    for (listDataReturn in snapshot.children) {
                        val listReturn = listDataReturn.getValue(AddBookReturnModel::class.java)

                        dataReturnerList.add(listReturn!!)
                    }
                    dataReturnRecyclerView.adapter = ReturnAdapter(dataReturnerList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}