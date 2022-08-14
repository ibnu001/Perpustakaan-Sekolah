package com.project.schoollibrary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.project.schoollibrary.databinding.ActivityDashMemberBinding
import kotlinx.android.synthetic.main.activity_dash_member.*


class DashMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashMemberBinding

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anime
        )
    }

    private var clicked = false

    private lateinit var dataBookRecyclerView: RecyclerView
    private lateinit var dataBookList: ArrayList<AddBookModel>

    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBookRecyclerView = findViewById(R.id.rv_dashMember)
        dataBookRecyclerView.layoutManager = LinearLayoutManager(this.baseContext)
        dataBookRecyclerView.setHasFixedSize(true)

        dataBookList = ArrayList()

        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")
        binding.tvDmUser.text = email

        val emailAuth = auth.currentUser?.email
        binding.tvDmUser.text = emailAuth

        if (emailAuth.isNullOrBlank()) {
            binding.apply {
                tvDmUser.visibility = View.GONE
                fabDmAdds.visibility = View.GONE
                fabDmAddBook.visibility = View.GONE
                fabDmAddAnggota.visibility = View.GONE
                topAppBar.menu.clear()
            }
        }

        // get data List Book from firebase
        getListBook()

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_tab_logOut -> {
                    logout()
                    true
                }
                R.id.btn_tab_borrow -> {
                    moveToBorrowBooks()
                    true
                }
                else -> false
            }
        }

        binding.fabDmAdds.setOnClickListener {
            onFabDmAddsBtnClicked()
        }

        binding.fabDmAddBook.setOnClickListener {
            startActivity(Intent(this, AddBooksActivity::class.java))
        }

        binding.fabDmAddAnggota.setOnClickListener {
            startActivity(Intent(this, AnggotaActivity::class.java))
        }

        binding.fabDmReturn.setOnClickListener {
            startActivity(Intent(this, ReturnActivity::class.java))
        }
    }

    private fun onFabDmAddsBtnClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fabDmAddBook.visibility = View.VISIBLE
            binding.fabDmAddAnggota.visibility = View.VISIBLE
            binding.fabDmReturn.visibility = View.VISIBLE
        } else {
            binding.fabDmAddBook.visibility = View.INVISIBLE
            binding.fabDmAddAnggota.visibility = View.INVISIBLE
            binding.fabDmReturn.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.fabDmAdds.startAnimation(rotateOpen)
        } else {
            binding.fabDmAdds.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.fabDmAddBook.isClickable = true
            binding.fabDmAddAnggota.isClickable = true
            binding.fabDmReturn.isClickable = true
        } else {
            binding.fabDmAddBook.isClickable = false
            binding.fabDmAddAnggota.isClickable = false
            binding.fabDmReturn.isClickable = false
        }
    }

    private fun getListBook() {
        // read data from realtime database
        mDbRef = FirebaseDatabase.getInstance().getReference("books")

        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    dataBookList.clear()

                    for (listDataBook in snapshot.children) {
                        val listBook = listDataBook.getValue(AddBookModel::class.java)

                        dataBookList.add(listBook!!)
                    }
                    dataBookRecyclerView.adapter = DashMemberAdapter(dataBookList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun logout() {
        if (true) {
            Firebase.auth.signOut()

            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }

    private fun moveToBorrowBooks() {
        startActivity(Intent(this, BookBorrowActivity::class.java))
    }
}