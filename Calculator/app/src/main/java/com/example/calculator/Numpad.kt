package com.example.calculator

import android.R
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.calculator.databinding.FragmentNumpadBinding
import kotlin.properties.Delegates


class Numpad : Fragment() {

    private lateinit var binding: FragmentNumpadBinding
    private val dataModel: DataModel by activityViewModels()

    private var pos by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNumpadBinding.inflate(inflater)

        pos = -1

        dataModel.position.observe(activity as LifecycleOwner) {
            pos = dataModel.position.value!!
        }

        binding.b0.setOnClickListener() {
            changeValue("0")
        }

        binding.b1.setOnClickListener() {
            changeValue("1")
        }

        binding.b2.setOnClickListener() {
            changeValue("2")
        }

        binding.b3.setOnClickListener() {
            changeValue("3")
        }

        binding.b4.setOnClickListener() {
            changeValue("4")
        }

        binding.b5.setOnClickListener() {
            changeValue("5")
        }

        binding.b6.setOnClickListener() {
            changeValue("6")
        }

        binding.b7.setOnClickListener() {
            changeValue("7")
        }

        binding.b8.setOnClickListener() {
            changeValue("8")
        }

        binding.b9.setOnClickListener() {
            changeValue("9")
        }

        binding.bac.setOnClickListener() {
            dataModel.data.value = ""

        }

        binding.bdiv.setOnClickListener() {
            changeValue("÷")
        }

        binding.bmul.setOnClickListener() {
            changeValue("×")
        }

        binding.bminus.setOnClickListener() {
            changeValue("-")
        }

        binding.bplus.setOnClickListener() {
            changeValue("+")
        }

        binding.bbrac1.setOnClickListener() {
            changeValue("(")
        }

        binding.bbrac2.setOnClickListener() {
            changeValue(")")
        }

        binding.bdot.setOnClickListener() {
            if(dataModel.data.value.isNullOrEmpty() == true)
            {
                changeValue("0.")
                dataModel.position.value = dataModel.position.value?.plus(1)
            }else{
                changeValue(".")
            }
        }

        binding.bequal.setOnClickListener() {
            if (dataModel.calculate.value == null) {
                dataModel.calculate.value = true
            } else {
                dataModel.calculate.value = !dataModel.calculate.value!!
            }

        }

        binding.bc.setOnClickListener() {
            if (pos != 0 && dataModel.data.value?.isNotEmpty() == true) {
                var str: SpannableStringBuilder = SpannableStringBuilder(dataModel.data.value)
                str.replace(pos - 1, pos, "")
                dataModel.data.value = str.toString()
                dataModel.position.value = dataModel.position.value?.minus(1)
            }
        }
        return binding.root
    }


    private fun changeValue(strToAdd: String) {
        var str: String
        var tmpstr = dataModel.data.value.toString()

        if (dataModel.data.value.isNullOrEmpty()) {
            dataModel.data.value = strToAdd
            dataModel.position.value = 1

        } else {

            var leftstr: String? = tmpstr.substring(0, pos)
            var rightStr = tmpstr.substring(pos)
            dataModel.data.value = String.format("%s%s%s", leftstr, strToAdd, rightStr)
            dataModel.position.value = dataModel.position.value?.plus(1)

        }
    }


    companion object {
        //        public lateinit var field: EditText
        @JvmStatic
        fun newInstance() = Numpad
    }
}