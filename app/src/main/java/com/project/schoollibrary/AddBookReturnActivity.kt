package com.project.schoollibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.schoollibrary.databinding.ActivityAddBookReturnBinding
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AddBookReturnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookReturnBinding

    private lateinit var executor: Executor
    private lateinit var handler: Handler

    private lateinit var mDbRef: DatabaseReference

    var uid: UUID = UUID.randomUUID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBookReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menghubungkan database firebase ke apl
        mDbRef = FirebaseDatabase.getInstance().reference

        binding.apply {
            etReturnName.doOnTextChanged { text, start, before, count -> etLRName(null) }
            etReturnTitle.doOnTextChanged { text, start, before, count -> etLRTitle(null) }
            etReturnBookCode.doOnTextChanged { text, start, before, count -> etLRBookCode(null) }
            etReturnDate.doOnTextChanged { text, start, before, count -> etLRDate(null) }
            etReturnFine.doOnTextChanged { text, start, before, count -> etLRDate(null) }

            btnAddReturnBook.setOnClickListener {
                var name = etReturnName.text.toString()
                var title = etReturnTitle.text.toString()
                var bookCode = etReturnBookCode.text.toString()
                var date = etReturnDate.text.toString()
                var fine = etReturnFine.text.toString()

                when {
                    name.isEmpty() -> {
                        etLRName("please fill in the name of the Returner")
                        etReturnName.requestFocus()
                    }
                    title.isEmpty() -> {
                        etLRTitle("Please enter the title of the book")
                        etReturnTitle.requestFocus()
                    }
                    bookCode.isEmpty() -> {
                        etLRBookCode("Please enter the Code of the book")
                        etReturnBookCode.requestFocus()
                    }

                    bookCode.length != 3 -> {
                        etLRBookCode("Please use the format ex: 001")
                    }

                    date.isEmpty() -> {
                        etLRDate("Please enter the date")
                        etReturnDate.requestFocus()
                    }

                    fine.isEmpty() -> {
                        etLRFine("Please enter the fine or ( - )")
                        etReturnFine.requestFocus()
                    }

                    name.isNotEmpty() && title.isNotEmpty() && bookCode.isNotEmpty() && date.isNotEmpty() && fine.isNotEmpty()
                    -> {
                        addReturnerBook()
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

    private fun addReturnerBook() {
        binding.apply {
            var name = etReturnName.text.toString()
            var title = etReturnTitle.text.toString()
            var bookCode = etReturnBookCode.text.toString()
            var date = etReturnDate.text.toString()
            var fine = etReturnFine.text.toString()

            val uid = "$name - $uid"

            mDbRef.child("returner_books").child(uid)
                .setValue(AddBookReturnModel(name, title, bookCode, date, fine))

            etReturnName.text?.clear()
            etReturnTitle.text?.clear()
            etReturnBookCode.text?.clear()
            etReturnDate.text?.clear()
            etReturnFine.text?.clear()
        }
    }

    private fun etLRName(note: String?) {
        binding.etLayoutReturnName.error = note
    }

    private fun etLRTitle(note: String?) {
        binding.etLayoutReturnTitle.error = note
    }

    private fun etLRBookCode(note: String?) {
        binding.etLayoutReturnBookCode.error = note
    }

    private fun etLRDate(note: String?) {
        binding.etLayoutReturnDate.error = note
    }

    private fun etLRFine(note: String?) {
        binding.etLayoutReturnFine.error = note
    }
}
