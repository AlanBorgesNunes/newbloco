package com.app.newblocodenotas.viewModels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.newblocodenotas.repositorios.Repository
import com.app.newblocodenotas.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelAdmob @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _adLoaded = MutableLiveData<UiState<Boolean>>()
    val adLoaded: LiveData<UiState<Boolean>>
        get() = _adLoaded

    private val _adInter = MutableLiveData<UiState<String>>()
    val adInter: LiveData<UiState<String>>
        get() = _adInter

    private val _adShowInter = MutableLiveData<UiState<String>>()
    val adShowInter: LiveData<UiState<String>>
        get() = _adShowInter


    fun inter(context: Context){
        viewModelScope.launch {
            _adInter.value = UiState.Loading
            repository.loadInterstitialAd(context){
                _adInter.value = it
            }
        }
    }

    fun interShow(activity: Activity){
        viewModelScope.launch {
            _adShowInter.value = UiState.Loading
            repository.showInterstitialAd(activity){
                _adShowInter.value = it
            }
        }
    }


}