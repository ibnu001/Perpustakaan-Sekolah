package com.project.schoollibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.schoollibrary.databinding.ActivityAddAnggotaBinding
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AddAnggotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAnggotaBinding

    private lateinit var executor: Executor
    private lateinit var handler: Handler

    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAnggotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menghubungkan database firebase ke apl
        mDbRef = FirebaseDatabase.getInstance().reference

        binding.apply {
            etAddAUidaNggota.doOnTextChanged { text, start, before, count -> etLAUidA(null) }
            etAddAFullName.doOnTextChanged { text, start, before, count -> etLAFName(null) }
            etAddAClass.doOnTextChanged { text, start, before, count -> etLAClass(null) }

            btnAddAnggota.setOnClickListener {
                var uidAnggota = etAddAUidaNggota.text.toString()
                var fullName = etAddAFullName.text.toString()
                var kelas = etAddAClass.text.toString()

                when {
                    uidAnggota.isEmpty() -> {
                        etLAUidA("please enter the UID Member")
                        etAddAUidaNggota.requestFocus()
                    }
                    fullName.isEmpty() -> {
                        etLAFName("Please enter the Full Name the borrower")
                        etAddAFullName.requestFocus()
                    }
                    kelas.isEmpty() -> {
                        etLAClass("Please enter the Class the borrower")
                        etAddAClass.requestFocus()
                    }

                    uidAnggota.length != 6 -> {
                        etLAUidA("Please use the format ex: 110199")
                    }

                    uidAnggota.isNotEmpty() && fullName.isNotEmpty() && kelas.isNotEmpty()
                    -> {
                        addAnggota()
                        Snackbar.make(it, "Submit successfully", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.japaneseIndigo))
                            .setTextColor(resources.getColor(R.color.antiFlashWhite))
                            .show()
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

    private fun addAnggota() {
        binding.apply {
            var uidAnggota = etAddAUidaNggota.text.toString()
            var fullName = etAddAFullName.text.toString()
            var kelas = etAddAClass.text.toString()

            var uidA = "$fullName - $uidAnggota"

            mDbRef.child("anggota").child(uidA)
                .setValue(AddAnggotaModel(uidAnggota, fullName, kelas))

            etAddAUidaNggota.text?.clear()
            etAddAFullName.text?.clear()
            etAddAClass.text?.clear()
        }
    }

    private fun etLAUidA(note: String?) {
        binding.etLayoutAddAUidAnggota.error = note
    }

    private fun etLAFName(note: String?) {
        binding.etLayoutAddAFullName.error = note
    }

    private fun etLAClass(note: String?) {
        binding.etLayoutAddAClass.error = note
    }
}