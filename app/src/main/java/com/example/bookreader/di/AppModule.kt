package com.example.bookreader.di

import com.example.bookreader.network.BooksApi
import com.example.bookreader.repository.BookRepository
import com.example.bookreader.repository.HomeRepository
import com.example.bookreader.utils.Constants.BASE_URL
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class )
object AppModule  {

    @Singleton
    @Provides
    fun provideBookRepository(): HomeRepository =
        HomeRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))



    @Singleton
    @Provides
    fun provideBookApi():BooksApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)


    }


}