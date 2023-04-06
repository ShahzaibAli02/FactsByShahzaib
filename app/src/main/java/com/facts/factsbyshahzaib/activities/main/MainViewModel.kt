package com.facts.factsbyshahzaib.activities.main

import androidx.lifecycle.*
import com.facts.factsbyshahzaib.model.FactsResponse
import com.facts.factsbyshahzaib.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository):ViewModel(){


    private val _response = MutableLiveData<Resource<FactsResponse>>(Resource.None())
    val response: LiveData<Resource<FactsResponse>> = _response

    fun loadFacts() {
        viewModelScope.launch {
            _response.value=Resource.Loading()
            _response.value=mainRepository.loadFacts()
        }
    }
}