package com.example.bookreader.screens.search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreader.components.InputField
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.data.Resources
import com.example.bookreader.model.Items
import com.example.bookreader.navigation.ReaderScreens

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: BookSearchViewModel = hiltViewModel()
) {


    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false

            ) {
                navController.navigate(ReaderScreens.HomeScreen.name)
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues) // Apply scaffold padding to avoid overlap
        ) {
            Column {
                SearchForm(
                    navController,
                    searchViewModel,
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { query ->
                    searchViewModel.searchBooks(query)
                }
            }

        }

    }


}

@Composable
fun SearchForm(
    navController: NavController,
    searchViewModel: BookSearchViewModel,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            },
            modifier = modifier,
            isSingleLine = true,
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(8.dp))

        val booksList by searchViewModel.listOfBooks

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when (booksList) {
                is Resources.Loading<*> -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                is Resources.Success -> {
                    val books = booksList.data ?: emptyList()
                    if (books.isNotEmpty()) {
                        LazyColumn {
                            items(books) { book ->
                                BookItem(book = book, navController = navController)
                            }
                        }
                    } else {
                        Text(
                            text = "No books found.",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    }
                }

                is Resources.Error -> {
                    Text(
                        text = booksList.message ?: "An error occurred.",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
            }
        }
    }

}

@Composable
fun BookItem(book: Items,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                navController.navigate(ReaderScreens.DetailsScreen.name + "/${book.id }")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            AsyncImage(
                model = book.volumeInfo?.imageLinks?.thumbnail ?: "",
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(120.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(20.dp))

            // 📚 Book Details
            Column(

            ) {
                Text(
                    text = book.volumeInfo?.title ?: "Unknown Title",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                Text(
                    text = book.volumeInfo?.authors?.joinToString(", ") ?: "Unknown Author",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = 6.dp)
                )
            }
        }
    }
}
