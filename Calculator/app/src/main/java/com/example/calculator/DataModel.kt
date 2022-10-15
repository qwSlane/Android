package com.example.calculator
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


open class DataModel :  ViewModel(){
    val data: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val position: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val calculate: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}