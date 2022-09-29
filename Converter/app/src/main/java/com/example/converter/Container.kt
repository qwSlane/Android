package com.example.converter

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.converter.databinding.FragmentContainerBinding
import java.math.BigDecimal
import java.math.MathContext


class Container : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    private lateinit var binding: FragmentContainerBinding

    private val coeffs : Array<DoubleArray> = arrayOf(
        doubleArrayOf(0.0022046226218488, 0.03527396195, 1.0),
        doubleArrayOf(0.010936132983, 0.03280839895, 1.0),
        doubleArrayOf(33.8, 1.0, 274.1)
    )

    private lateinit var currentCoeff: DoubleArray

    private var c1: Double = 1.0
    private var c2: Double = 1.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContainerBinding.inflate(inflater)
        currentCoeff = coeffs[0]

        c1 = currentCoeff[0]
        c2 = currentCoeff[0]
        binding.spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                c1 = currentCoeff[p2]
                setEmpty()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                c2 = currentCoeff[p2]
                setEmpty()

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.textFrom.setOnClickListener {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("App", binding.textFrom.text))
            Toast.makeText(requireContext(), "Copied!", Toast.LENGTH_SHORT).show()
        }

        binding.textTo.setOnClickListener {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("App", binding.textTo.text))
            Toast.makeText(requireContext(), "Copied!", Toast.LENGTH_SHORT).show()
        }

        binding.buttonPaste.setOnClickListener{
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val value = clipboard?.primaryClip
            val item = value?.getItemAt(0)
            val pasteData = item?.text
//            if(pasteData.toString().length >16){
//                Toast.makeText(requireContext(), "Buffer overflow!", Toast.LENGTH_SHORT).show()
//            }
//            else{
//                dataModel.data.value = pasteData.toString()
//            }
            dataModel.data.value = pasteData.toString()

        }

        binding.exchange.setOnClickListener{
            var spiner: Spinner = binding.spinnerFrom
            var spiner2: Spinner = binding.spinnerTo
            var coeff: Int = spiner.selectedItemPosition
            var coeff2: Int = spiner2.selectedItemPosition

            c1 = currentCoeff[coeff2]
            c2 = currentCoeff[coeff]
            spiner.setSelection(coeff2)
            spiner2.setSelection(coeff)

        }

        return binding.root
    }

    private fun setEmpty() {
        dataModel.data.value = ""
    }

    private fun setChanges(){
        if(!dataModel.data.value.isNullOrBlank()) {
            var num: BigDecimal = BigDecimal(dataModel.data.value).times(BigDecimal(c2).divide(BigDecimal(c1),
                MathContext(16)
            ))
            (binding.textTo).setText(num.toPlainString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.data.observe(activity as LifecycleOwner) {
            if(it.isNotBlank()){
                (binding.textFrom as TextView).text = it
                var num: BigDecimal = BigDecimal(dataModel.data.value).times(BigDecimal(c2).divide(BigDecimal(c1),
                    MathContext(1000)
                ))
                binding.textTo.setText(num.toPlainString().dropLastWhile { it == '0' })
            }else{
                binding.textFrom.setText("")
                binding.textTo.setText("")
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = Container
    }
}