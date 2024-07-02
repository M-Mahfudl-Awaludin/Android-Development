package com.dicoding.asclepius.view


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.Datehelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.repository.HistoryRepository
import com.dicoding.asclepius.view.article.ArticleActivity
import com.dicoding.asclepius.view.history.HistoryActivity
import com.google.android.material.snackbar.Snackbar
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.util.Date

class  MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var hasil: String? = null
    private var prediction: String? = null
    private var score: String? = null
    private var imageURI: Uri? = null
    private val history = History()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, ResultActivity::class.java)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            analyzeImage(intent)
            moveToResult(intent)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_history -> {
                    val themeSetting = Intent(this@MainActivity, HistoryActivity::class.java)
                    startActivity(themeSetting)
                    true
                }

                R.id.menu_article -> {
                    val favIntent = Intent(this@MainActivity, ArticleActivity::class.java)
                    startActivity(favIntent)
                    true
                }

                else -> false
            }
        }


    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageURI = uri
                imageURI?.let {
                    val intent = UCrop.of(
                        it, Uri.fromFile(File(cacheDir, "sample-image-${Date().time}.png"))
                    ).getIntent(this)
                    launcherUCrop.launch(intent)
                }
            } else {
                Snackbar.make(
                    binding.root, getString(R.string.app_no_media_selected), Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    private var launcherUCrop = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = result.data?.let { UCrop.getOutput(it) }
            resultUri?.let { uri ->
                imageURI = uri
                imageURI?.let {
                    binding.analyzeButton.isEnabled = true
                    showImage(it)
                }
            }
        } else if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.app_canceled), Toast.LENGTH_SHORT).show()
        }
    }


    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(imageURI: Uri) {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        binding.previewImageView.setImageURI(imageURI)
    }

    private fun analyzeImage(intent: Intent) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            val sortedCategories =
                                it[0].categories.sortedByDescending { it?.score }
                            hasil =
                                sortedCategories.joinToString("\n") {
                                    "${it.label} " + NumberFormat.getPercentInstance()
                                        .format(it.score).trim()
                                }
                            prediction = sortedCategories[0].label
                            score =
                                NumberFormat.getPercentInstance().format(sortedCategories[0].score)
                        } else {
                            showToast()
                        }
                    }
                }
            }
        )
        imageURI?.let { this.imageClassifierHelper.classifyStaticImage(it) }
        intent.putExtra(ResultActivity.EXTRA_RESULT, hasil)
        intent.putExtra(ResultActivity.EXTRA_PREDICT, prediction)
        intent.putExtra(ResultActivity.EXTRA_SCORE, score)
        history.image = imageURI.toString()
        history.result = hasil.toString()
        history.date = Datehelper.getCurrentDate()
        val mNoteRepository = HistoryRepository(application)
        mNoteRepository.insert(history)
    }

    private fun moveToResult(intent: Intent) {
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageURI.toString())
        startActivity(intent)

    }

    private fun showToast(message: String = "No result found") {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
