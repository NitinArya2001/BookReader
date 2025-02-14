package com.example.bookreader.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.DataOrException
import com.example.bookreader.model.MBook
import com.example.bookreader.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel@Inject constructor(
    private val repository: HomeRepository
): ViewModel() {

    val data: MutableState<DataOrException<List<MBook>,Boolean,Exception>> = mutableStateOf(DataOrException(
        listOf(),true,Exception("")
    ))


    init {
        getAllBooksFromDataBase()
    }

    private fun getAllBooksFromDataBase() {
        viewModelScope.launch {
            Log.e("kittu", "Fetching books...")

            data.value = DataOrException(emptyList(), true, null)
            val result = repository.getAllBooksFromDatabase()
            Log.e("kittu", "Fetched Books in ViewModel: ${result.data}")

            data.value = DataOrException(result.data, false, result.e)
        }
        Log.e("kittu", "getAllBooksFromDataBase: ${data.value.data}", )
    }


}