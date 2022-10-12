package com.example.converter

import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.converter.databinding.FragmentNumpadBinding


class NumpadFragment : Fragment() {

    private val dataModel: DataModel by activityViewModels()
    private lateinit var binding: FragmentNumpadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNumpadBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button1.setOnClickListener{
            changeValue("1")
        }
        binding.button2.setOnClickListener{
            changeValue("2")
        }
        binding.button3.setOnClickListener{
            changeValue("3")
        }
        binding.button4.setOnClickListener{
            changeValue("4")
        }
        binding.button5.setOnClickListener{
            changeValue("5")
        }
        binding.button6.setOnClickListener{
            changeValue("6")
        }
        binding.button7.setOnClickListener{
            changeValue("7")
        }
        binding.button8.setOnClickListener{
            changeValue("8")
        }
        binding.button9.setOnClickListener{
            changeValue("9")
        }
        binding.button0.setOnClickListener{
            changeValue("0")
        }
        binding.buttonDec.setOnClickListener{
            setDot()
        }
        binding.buttonDelete.setOnClickListener{
            deleteValue()
        }
    }

    private fun deleteValue(){
        var data = dataModel.data.value
        if(!data.isNullOrBlank()) {
            if(data.toString().length == 1){
                dataModel.data.value = ""
            }else{
                dataModel.data.value = data.toString().dropLast(1)
            }
        }
    }

    private fun setDot(){
        var str: String = "0."
        var data  = dataModel.data.value
        if(!data.isNullOrEmpty()) {
//            if(str.length < 16){
//                if("." !in data.toString()){
//                    str = dataModel.data.value.toString()
//                    str += "."
//                }else{
//                    str = dataModel.data.value.toString()
//                }
//            }
            if("." !in data.toString()){
                    str = dataModel.data.value.toString()
                    str += "."
                }else{
                    str = dataModel.data.value.toString()
                }
        }
        dataModel.data.value = str
    }

    private fun changeValue(value: String){
        var str: String
        if( dataModel.data.value == null){
            str = value
        }else{
            str = dataModel.data.value.toString()
//            if(str.length < 16){
//                str += value
//            }
//            else{
//                Toast.makeText(requireContext(), "Out of limit!", Toast.LENGTH_SHORT).show()
//
//            }
            str += value
        }
        dataModel.data.value = str
    }

    companion object {
        @JvmStatic
        fun newInstance() = NumpadFragment()
    }
}