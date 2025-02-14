package com.example.bookreader.screens.details

import androidx.lifecycle.ViewModel
import com.example.bookreader.data.Resources
import com.example.bookreader.model.Items
import com.example.bookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {

    suspend fun getBookInfo(bookId: String): Resources<Items>{
        return repository.getBookInfo(bookId)
    }

}