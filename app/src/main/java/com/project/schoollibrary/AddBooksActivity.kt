package com.project.schoollibrary

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.project.schoollibrary.databinding.ActivityAddBooksBinding
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AddBooksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBooksBinding

    private lateinit var imageUri: Uri

    private lateinit var executor: Executor
    private lateinit var handler: Handler

    // Untuk penggunaan Firebase Storage
    private lateinit var uploadTask: UploadTask
    private lateinit var fStore: FirebaseStorage
    private lateinit var sRef: StorageReference

    private lateinit var mDbRef: DatabaseReference

    var uid: UUID = UUID.randomUUID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menghubungkan database firebase ke apl
        mDbRef = FirebaseDatabase.getInstance().reference

        // url dari firebase storage, ganti bila firebase project berubah
        fStore = FirebaseStorage.getInstance("gs://school-library-project.appspot.com")
        sRef = fStore.reference

        /**
         * yang tidak boleh kosong saat input buku baru
         * - title
         * - author
         * - book code
         * - location
         * - cover book
         * **/

        binding.apply {
            etAddTitle.doOnTextChanged { text, start, before, count -> etLTitle(null) }
            etAddAuthor.doOnTextChanged { text, start, before, count -> etLAuthor(null) }
            etAddBookCode.doOnTextChanged { text, start, before, count -> etLBookCode(null) }
            etAddLocation.doOnTextChanged { text, start, before, count -> etLLocation(null) }

            btnAddSelecImage.setOnClickListener { selectImage() }

            btnAddAddBook.setOnClickListener {
                var title = etAddTitle.text.toString()
                var author = etAddAuthor.text.toString()
                var edition = etAddEdition.text.toString()
                var publishing = etAddPublishing.text.toString()
                var language = etAddLanguage.text.toString()
                var bookCode = etAddBookCode.text.toString()
                var location = etAddLocation.text.toString()
                var description = etAddDescription.text.toString()
                var available = etAddAvailable.text.toString()

                when {
                    title.isEmpty() -> {
                        etLTitle("Please enter the title of the book")
                        etAddTitle.requestFocus()
                    }

                    author.isEmpty() -> {
                        etLAuthor("Please enter the author of the book")
                        etAddAuthor.requestFocus()
                    }

                    edition.isEmpty() -> etAddEdition.setText("-")
                    publishing.isEmpty() -> etAddPublishing.setText("-")
                    language.isEmpty() -> etAddLanguage.setText("-")

                    bookCode.isEmpty() -> {
                        etLBookCode("Please enter the book code of the book")
                        etAddBookCode.requestFocus()
                    }

                    location.isEmpty() -> {
                        etLLocation("Please enter the location of the book")
                        etAddLocation.requestFocus()
                    }

                    description.isEmpty() -> etAddDescription.setText("-")
                    available.isEmpty() -> etAddAvailable.setText("-")

                    bookCode.length != 3 -> {
                        etLBookCode("Please use the format ex: 001")
                    }

                    title.isNotEmpty() && author.isNotEmpty() && bookCode.isNotEmpty() && location.isNotEmpty()
                    -> {
                        addBook(imageUri)
                        Snackbar.make(it, "Uploaded successfully", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.japaneseIndigo))
                            .setTextColor(resources.getColor(R.color.antiFlashWhite))
                            .show()

                        etAddTitle.text?.clear()
                        etAddAuthor.text?.clear()
                        etAddEdition.text?.clear()
                        etAddPublishing.text?.clear()
                        etAddLanguage.text?.clear()
                        etAddBookCode.text?.clear()
                        etAddLocation.text?.clear()
                        etAddDescription.text?.clear()
                        etAddAvailable.text?.clear()
                    }
                }
            }
            executor = Executors.newSingleThreadExecutor()
            handler  = Handler(Looper.getMainLooper())
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addBook(imageUri: Uri) {
        binding.apply {
            var title = etAddTitle.text.toString()
            var author = etAddAuthor.text.toString()
            var edition = etAddEdition.text.toString()
            var publishing = etAddPublishing.text.toString()
            var language = etAddLanguage.text.toString()
            var bookCode = etAddBookCode.text.toString()
            var location = etAddLocation.text.toString()
            var description = etAddDescription.text.toString()
            var available = etAddAvailable.text.toString()

            val uid = "$title - $uid"
            val imageRef = sRef.child("uploads/images/${uid}.jpg")

            uploadTask = imageRef.putFile(imageUri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val coverBook = downloadUri.toString()

                    ivAddCoverBook.setImageResource(R.drawable.ic_baseline_book_24)

                    btnAddSelecImage.setOnClickListener { selectImage() }

                    addBookToDatabase(
                        title,
                        author,
                        edition,
                        publishing,
                        language,
                        bookCode,
                        location,
                        description,
                        available,
                        coverBook
                    )

                    showToast("Image uploaded successfully")
                } else {
                    showToast("Image failed to upload")
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent()

        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    private fun addBookToDatabase(
        title: String,
        author: String,
        edition: String,
        publishing: String,
        language: String,
        bookCode: String,
        location: String,
        description: String,
        available: String,
        coverBook: String
    ) {
        val nameUid = "$title - $uid"

        mDbRef.child("books").child(nameUid)
            .setValue(
                AddBookModel(
                    title,
                    author,
                    edition,
                    publishing,
                    language,
                    bookCode,
                    location,
                    description,
                    available,
                    coverBook
                )
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            binding.ivAddCoverBook.setImageURI(imageUri)
        }
    }

    private fun etLTitle(note: String?) {
        binding.etLayoutAddTitle.error = note
    }

    private fun etLAuthor(note: String?) {
        binding.etLayoutAddAuthor.error = note
    }

    private fun etLBookCode(note: String?) {
        binding.etLayoutAddBookCode.error = note
    }

    private fun etLLocation(note: String?) {
        binding.etLayoutAddLocation.error = note
    }

    private fun showToast(note: String) {
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show()
    }
}