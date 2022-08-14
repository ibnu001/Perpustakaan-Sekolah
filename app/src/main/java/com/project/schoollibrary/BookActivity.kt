package com.project.schoollibrary

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.RoundedCorner
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.project.schoollibrary.databinding.ActivityBookBinding
import kotlinx.android.synthetic.main.activity_book.*

class BookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val email = auth.currentUser?.email

        if (email.isNullOrBlank()) {
            binding.btnBookEditBook.visibility = View.GONE
        }

        getBooks()

        // tombol back manual
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getBooks() {
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val edition = intent.getStringExtra("edition")
        val publishing = intent.getStringExtra("publishing")
        val language = intent.getStringExtra("language")
        val bookCode = intent.getStringExtra("bookCode")
        val location = intent.getStringExtra("location")
        val description = intent.getStringExtra("description")
        val available = intent.getStringExtra("available")
        val coverBook = intent.getStringExtra("coverBook")

        binding.apply {
            // Title dan author dibawah cover book
            tvBookByTitle.text = title
            tvBookByAuthor.text = "by $author"

            // detail book
            tvBookTitle.text = title
            tvBookAuthor.text = author
            tvBookEdition.text = edition
            tvBookPublishing.text = publishing
            tvBookLanguage.text = language
            tvBookBookCode.text = bookCode
            tvBookLocation.text = "Rack $location"
            tvBookDescription.text = description
            tvBookAvailable.text = available

            Glide.with(this@BookActivity)
                .load(coverBook)
                .placeholder(R.drawable.ic_baseline_book_24)
                .error(R.drawable.ic_baseline_book_24)
                .fitCenter()
                .transform(RoundedCorners(20))
                .into(iv_book_coverBook)
        }
    }
}












