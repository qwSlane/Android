package com.example.converter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel : ViewModel(){
    val data: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}