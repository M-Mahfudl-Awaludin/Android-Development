package com.dicoding.asclepius.view

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.asclepius.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {
    private var binding: ActivityResultBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val hasil = intent.getStringExtra(EXTRA_RESULT)
        val data = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = Uri.parse(data)


        runOnUiThread {
            imageUri?.let {
                Log.d("Image URI", "showImage: $it")
                binding?.resultImage?.setImageURI(it)
                binding?.resultText?.text = hasil
            }
        }

        Log.d(ContentValues.TAG, "Image URI: $hasil")

    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_hasil"
        const val EXTRA_PREDICT = "extra_predict"
        const val EXTRA_SCORE = "extra_score"
    }

}