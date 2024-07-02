package com.dicoding.asclepius.view.history


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.helper.HistoryModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding:   ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = HistoryModelFactory.getInstance(application)
        val viewModel: HistoryViewModel = ViewModelProvider(this,factory)[HistoryViewModel::class.java]

        val adapter = HistoryAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)

        adapter.onClick = {
            viewModel.delete(it)
        }

        viewModel.getAllNotes().observe(this){list ->
            if (list != null){
                adapter.submitList(list)
                binding.rvHistory.adapter = adapter
            }

            if (list.isEmpty()){
                Toast.makeText(
                    this,
                    "Tidak Ada Riwayat Hasil Analisa",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}
