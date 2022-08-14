package com.project.schoollibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.project.schoollibrary.databinding.ActivityAnggotaBinding

class AnggotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnggotaBinding

    private lateinit var dataAnggotaRecyclerView: RecyclerView
    private lateinit var dataAnggotaList: ArrayList<AddAnggotaModel>

    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnggotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataAnggotaRecyclerView = findViewById(R.id.rv_anggota)
        dataAnggotaRecyclerView.layoutManager = LinearLayoutManager(this.baseContext)
        dataAnggotaRecyclerView.setHasFixedSize(true)

        dataAnggotaList = ArrayList()

        getListAnggota()

        binding.fabAnggotaAddA.setOnClickListener {
            startActivity(Intent(this, AddAnggotaActivity::class.java))
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getListAnggota() {
        mDbRef = FirebaseDatabase.getInstance().getReference("anggota")

        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    dataAnggotaList.clear()

                    for (listDataAnggota in snapshot.children) {
                        val listAnggota = listDataAnggota.getValue(AddAnggotaModel::class.java)

                        dataAnggotaList.add(listAnggota!!)
                    }
                    dataAnggotaRecyclerView.adapter = AnggotaAdapter(dataAnggotaList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}