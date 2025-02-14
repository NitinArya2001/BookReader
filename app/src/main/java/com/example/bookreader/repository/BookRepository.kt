package com.example.bookreader.repository

import android.util.Log
import com.example.bookreader.data.DataOrException
import com.example.bookreader.data.Resources
import com.example.bookreader.model.Items
import com.example.bookreader.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api:BooksApi) {

    suspend fun getBooks(searchQuery: String):Resources<List<Items>>{
        return try {
            Resources.Loading(data = "Loading....")
            val itemList = api.getAllBooks(searchQuery).items
            if(itemList.isNotEmpty()) Resources.Loading(data = false)
            Resources.Success(data = itemList)

        }catch (e:Exception){
            Resources.Error(message = e.message.toString(),)
            }
    }

    suspend fun getBookInfo(bookId: String):Resources<Items>{
        val response = try {
            Resources.Loading(data = true)
            api.getBooksInfo(bookId)
        }catch (e:Exception){
            return Resources.Error(message = "An error occured ${e.message} ")
        }
        Resources.Loading(data = false)

        return Resources.Success(data = response)
    }
}