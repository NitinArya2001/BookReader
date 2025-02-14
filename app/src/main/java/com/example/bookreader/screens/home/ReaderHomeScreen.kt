package com.example.bookreader.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookreader.components.FABContent
import com.example.bookreader.components.ListCard
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.components.TitleSection
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Reader",
                navController = navController)
        },
        floatingActionButton = {
            FABContent{
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }
    ) { paddingValues ->  // Capture scaffold padding
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply scaffold padding to avoid overlap
        ) {
            HomeContent(navController,viewModel)
        }
    }

}

@Composable
fun HomeContent(navController: NavController,viewModel: HomeScreenViewModel) {

    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (viewModel.data.value.data?.isNotEmpty() == true) {
        listOfBooks = viewModel.data.value.data!!.toList()
            .onEach { mBook ->
                mBook.userId = currentUser?.uid.toString()
            }
            .filter { mBook ->
                mBook.userId == currentUser?.uid.toString()
            }

        Log.d("nitin", "HomeContent: listOfBooks: $listOfBooks")
    }


//    val listOfBooks = listOf(
//        MBook(id = "bcdce", title = "hello Again", authors = "All of us", notes = null),
//        MBook(id = "bcdce", title = "hello", authors = "All of us", notes = null),
//        MBook(id = "bcdce", title = "Again", authors = "All of us", notes = null),
//        MBook(id = "bcdce", title = "Love", authors = "All of us", notes = null),
//        MBook(id = "bcdce", title = "Namaste", authors = "All of us", notes = null),
//    )

    val currentUsername = if(!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    }else{
        "N/A"
    }

    Column (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top

    ){
        Spacer(modifier = Modifier.height(24.dp))
        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TitleSection(label = "Your Reading \n " + " activity right now")
                Spacer(modifier = Modifier.width(180.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = Color(0xFF7AC2D3),
                        modifier = Modifier
                            .clickable {
                                navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                            }
                            .size(45.dp)
                            ,
                    )
                    Text(
                        text = currentUsername!!,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            ReadingRightNowArea(books = listOfBooks, navController = navController)
            Spacer(modifier = Modifier.height(16.dp))

            TitleSection(label = "Reading List")

            BookListArea(listOfBooks = listOfBooks,navController = navController)
        }

    }

}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    val addedBooks = listOfBooks
        .filter { mBook ->
            mBook.startReading == null && mBook.finishedReading == null
        }
    HorizontalScrollableComponents(addedBooks){
        Log.e("chintu", "BookListArea: $it", )
        navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")
    }

}



@Composable
fun ReadingRightNowArea(books:List<MBook>, navController: NavController){

    val readingNowList = books.filter { mBook ->
        mBook.startReading!= null && mBook.finishedReading == null
    }
    Log.e("all", "ReadingRightNowArea: ${readingNowList.size}", )

    HorizontalScrollableComponents(readingNowList){
        Log.e("chintu", "ReadingRightNowArea: $it", )
        navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")
    }
}

@Composable
fun HorizontalScrollableComponents(
    listOfBooks: List<MBook>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPress: (String?) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(200.dp)
            .horizontalScroll(scrollState)
    ) {
        if(viewModel.data.value.loading == true){
            CircularProgressIndicator()
        }else{
            if(listOfBooks.isNullOrEmpty()){
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No Books Found. Add Books",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
        for (book in listOfBooks) {
            ListCard(book) {
                Log.e("chintu", "HorizontalScrollableComponents: ${book.googleBookId} ", )
                onCardPress(book.googleBookId.toString())
            }
        }
    }
}
