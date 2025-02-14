package com.example.bookreader.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.DataOrException
import com.example.bookreader.data.Resources
import com.example.bookreader.model.Items
import com.example.bookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {

    val listOfBooks: MutableState<Resources<List<Items>>>
     = mutableStateOf(Resources.Loading(null))

    init {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) return@launch
            listOfBooks.value = Resources.Loading(null)
            val result = repository.getBooks(query)
            listOfBooks.value = result


            Log.e("nitin", "searchBooks: ${listOfBooks.value.data}")
            if (listOfBooks.value is Resources.Loading) {
                Log.e("nitin", "searchBooks: Loading...")
            } else if (listOfBooks.value is Resources.Success) {
                Log.e("nitin", "searchBooks: Success, ${listOfBooks.value.data?.size} books found")
            } else if (listOfBooks.value is Resources.Error) {
                Log.e("nitin", "searchBooks: Error - ${listOfBooks.value.message}")
            }
        }
    }

}