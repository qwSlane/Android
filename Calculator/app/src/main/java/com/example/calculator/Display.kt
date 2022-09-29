package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.calculator.databinding.FragmentDisplayBinding
import org.mariuszgromada.math.mxparser.Expression


class Display : Fragment() {

    private lateinit var binding: FragmentDisplayBinding
    private val dataModel: DataModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisplayBinding.inflate(inflater)

        binding.expression.showSoftInputOnFocus = false

        dataModel.data.observe(activity as LifecycleOwner) {
            binding.expression.setText(it)
        }

        dataModel.position.observe(activity as LifecycleOwner) {
            binding.expression.setSelection(it)
        }

        dataModel.calculate.observe(activity as LifecycleOwner) {
            calculate()
        }

        binding.expression.setOnClickListener(){
            dataModel.position.value = binding.expression.selectionStart
        }

        return binding.root
    }


    private fun calculate() {
        var expr: Expression = Expression(dataModel.data.value.toString())
        var result: String = expr.calculate().toString()
        binding.answer.text = result
        if (result == "NaN") {
            dataModel.data.value = ""
            dataModel.position.value = 0
        }else{
            dataModel.data.value = result
            dataModel.position.value = result.length
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = Display
    }
}