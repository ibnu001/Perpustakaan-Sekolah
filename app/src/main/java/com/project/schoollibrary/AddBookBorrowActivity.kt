package com.project.schoollibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.schoollibrary.databinding.ActivityAddBookBorrowBinding
import com.project.schoollibrary.databinding.ActivityBookBorrowBinding
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AddBookBorrowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBorrowBinding

    private lateinit var executor: Executor
    private lateinit var handler: Handler

    private lateinit var mDbRef: DatabaseReference

    var uid: UUID = UUID.randomUUID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBorrowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menghubungkan database firebase ke apl
        mDbRef = FirebaseDatabase.getInstance().reference

        binding.apply {
            etBorrowName.doOnTextChanged { text, start, before, count -> etLBName(null) }
            etBorrowTitle.doOnTextChanged { text, start, before, count -> etLBTitle(null) }
            etBorrowBookCode.doOnTextChanged { text, start, before, count -> etLBBookCode(null) }
            etBorrowDate.doOnTextChanged { text, start, before, count -> etLBDate(null) }

            btnAddBorrowBook.setOnClickListener {
                var name = etBorrowName.text.toString()
                var title = etBorrowTitle.text.toString()
                var bookCode = etBorrowBookCode.text.toString()
                var date = etBorrowDate.text.toString()

                when {
                    name.isEmpty() -> {
                        etLBName("please fill in the name of the borrower")
                        etBorrowName.requestFocus()
                    }
                    title.isEmpty() -> {
                        etLBTitle("Please enter the title of the book")
                        etBorrowTitle.requestFocus()
                    }
                    bookCode.isEmpty() -> {
                        etLBBookCode("Please enter the Code of the book")
                        etBorrowBookCode.requestFocus()
                    }

                    bookCode.length != 3 -> {
                        etLBBookCode("Please use the format ex: 001")
                    }

                    date.isEmpty() -> {
                        etLBDate("Please enter the date")
                        etBorrowDate.requestFocus()
                    }
                    name.isNotEmpty() && title.isNotEmpty() && bookCode.isNotEmpty() && date.isNotEmpty()
                    -> {
                        addBorrowerBook()
                        Snackbar.make(it, "Submit successfully", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.japaneseIndigo))
                            .setTextColor(resources.getColor(R.color.antiFlashWhite))
                            .show()
                    }
                }
            }
            executor = Executors.newSingleThreadExecutor()
            handler = Handler(Looper.getMainLooper())
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addBorrowerBook() {
        binding.apply {
            var name = etBorrowName.text.toString()
            var title = etBorrowTitle.text.toString()
            var bookCode = etBorrowBookCode.text.toString()
            var date = etBorrowDate.text.toString()

            val uid = "$name - $uid"

            mDbRef.child("borrower_books").child(uid)
                .setValue(AddBookBorrowModel(name, title, bookCode, date))

            etBorrowName.text?.clear()
            etBorrowTitle.text?.clear()
            etBorrowBookCode.text?.clear()
            etBorrowDate.text?.clear()
        }
    }

    private fun etLBName(note: String?) {
        binding.etLayoutBorrowName.error = note
    }

    private fun etLBTitle(note: String?) {
        binding.etLayoutBorrowTitle.error = note
    }

    private fun etLBBookCode(note: String?) {
        binding.etLayoutBorrowBookCode.error = note
    }

    private fun etLBDate(note: String?) {
        binding.etLayoutBorrowDate.error = note
    }
}