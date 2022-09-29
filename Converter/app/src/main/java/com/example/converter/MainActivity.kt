package com.example.converter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import com.example.converter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val dataModel: DataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners(R.array.forWeight, binding.weight)
        setListeners(R.array.forLength, binding.length)
        setListeners(R.array.forTemperature, binding.temperature)

        supportFragmentManager
            .beginTransaction().replace(R.id.converter_holder, Container())
            .commit()

        supportFragmentManager
            .beginTransaction().replace(R.id.numpad_holder, NumpadFragment())
            .commit()

        supportActionBar?.hide()

    }

    @SuppressLint("ResourceType")
    private fun setListeners(arrayList: Int, button: Button) {
        button.setOnClickListener {
            ArrayAdapter.createFromResource(
                this,
                arrayList,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                var spinner = findViewById<Spinner>(R.id.spinnerFrom)
                spinner.adapter = adapter
                spinner = findViewById<Spinner>(R.id.spinnerTo)
                spinner.adapter = adapter
                dataModel.data.value = ""

            }
        }
    }


}