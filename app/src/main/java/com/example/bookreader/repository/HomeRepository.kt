package com.example.bookreader.repository

import android.util.Log
import com.example.bookreader.data.DataOrException
import com.example.bookreader.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val queryBook: Query
) {
    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>,Boolean,Exception>{
        val dataOrException = DataOrException<List<MBook>,Boolean,Exception>()

        try {
            dataOrException.loading = true
            val snapshot = queryBook.get().await()
            val books = snapshot.documents.mapNotNull { it.toObject(MBook::class.java) }

            Log.e("kittu", "Fetched Books: $books")
            dataOrException.data = books

            if(!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false

        }catch (exception: FirebaseFirestoreException){
            dataOrException.e = exception
        }
        Log.e("kittu", "getAllBooksFromDatabase: repo: ${dataOrException.data}", )
        return dataOrException
    }
}
