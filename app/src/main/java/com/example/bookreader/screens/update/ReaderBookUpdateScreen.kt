package com.example.bookreader.screens.update

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookreader.R
import com.example.bookreader.components.InputField
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.components.RoundedButton
import com.example.bookreader.data.DataOrException
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderNavigation
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.screens.home.HomeScreenViewModel
import com.example.bookreader.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(
    navController: NavController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Log.e("Kittu", "BookUpdateScreen: $bookItemId")

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Update Book",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {

        val bookInfo = viewModel.data.value
        Log.e("chintu", "BookUpdateScreen: bookInfo $bookInfo viewmodel ${viewModel.data.value}", )


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 3.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Log.d("chintu", "bookInfo: ${bookInfo.loading}")

                if (bookInfo.loading == true) {
                    CircularProgressIndicator()
                    bookInfo.loading = false
                } else {
                    Surface(
                        modifier = Modifier
                            .padding(top = 150.dp)
                            .fillMaxWidth(),
                        shape = CircleShape,
                        shadowElevation = 4.dp
                    ) {

                        ShowBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId)

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    bookInfo.data?.find { it.googleBookId == bookItemId }
                        ?.let { ShowSimpleForm(book = it, navController) }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }


    }

}

@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<MBook>, Boolean, Exception>, bookItemId: String) {

    Row {
        Spacer(modifier = Modifier.width(10.dp))
        if (!bookInfo.data.isNullOrEmpty()) {
            val book = bookInfo.data?.find { it.googleBookId == bookItemId }
            if (book != null) {
                Column(verticalArrangement = Arrangement.Center) {
                    CardListItem(book = book, onPressDetails = {})
                }
            } else {
                Text("Book not found", color = Color.Red, modifier = Modifier.padding(8.dp))
            }
        } else {
            Text("No books available", color = Color.Gray, modifier = Modifier.padding(8.dp))
        }

    }

}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {
    Card(

        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()  // ✅ Ensures full width
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .clickable { onPressDetails() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Inner padding for spacing
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Image
            Image(
                painter = rememberImagePainter(data = book.photoUrl.toString()),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)  // ✅ Fixed width
                    .height(100.dp) // ✅ Fixed height
                    .clip(RoundedCornerShape(12.dp))
            )

            // Book Details Column
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f), // ✅ Ensures text takes remaining space
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Authors: ${book.authors ?: "Unknown"}",
                    fontSize = 20.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Published: ${book.publishedDate ?: "Unknown"}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun ShowSimpleForm(book: MBook, navController: NavController) {

    val context = LocalContext.current

    val noteText = remember { mutableStateOf("") }
    val isStartReading = remember { mutableStateOf(false) }
    val isFinishReading = remember { mutableStateOf(false) }
    val ratingVal = remember { mutableIntStateOf(0) }
    val openDialog = remember { mutableStateOf(false) }
        Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        LaunchedEffect(book.notes) {
            val newValue = book.notes?.takeIf { it.isNotEmpty() } ?: "No thoughts available..."
            Log.e("nitin", "Updating noteText: $newValue")
            noteText.value = newValue
        }
            Spacer(modifier = Modifier.height(10.dp))

        SimpleForm(defaultValue = noteText.value) { note ->
            Log.e("nitin", "note: $note")
            noteText.value = note
        }

        Log.e("nitin", "ShowSimpleForm: ${noteText.value}")

        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(
                onClick = { isStartReading.value = true },
                enabled = book.startReading == null
            ) {
                if (book.startReading == null) {
                    if (!isStartReading.value) {
                        Text(text = "Start Reading")
                    } else {
                        Text(
                            text = "Started Reading...",
                            modifier = Modifier.alpha(0.6f),
                            color = Color.Red.copy(alpha = 0.5f)

                        )
                    }
                } else {
                    Text(text = "Started On: ${formatDate(book.startReading!!)}")
                }

            }
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                onClick = { isFinishReading.value = true },
                enabled = book.finishedReading == null
            ) {
                if (book.finishedReading == null) {
                    if (!isFinishReading.value) {
                        Text(text = "Mark as Read")
                    } else {
                        Text(
                            text = "Finished Reading"

                        )
                    }
                } else {
                    Text(text = "Finished On: ${formatDate(book.finishedReading!!)}")
                }
            }

        }
        Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
        book.rating?.let {
            RatingBar(rating = it) { rating ->
                ratingVal.intValue = rating
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()  // ✅ Ensures Row takes full width
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly // ✅ Evenly distributes buttons
        ) {
            val newNotes = noteText.value.trim()
            val changeRating = book.rating != ratingVal.intValue
            val changeNotes = book.notes?.trim() != newNotes && newNotes.isNotEmpty()
            val isFinishedTimeStamp =
                if (isFinishReading.value) Timestamp.now() else book.finishedReading
            val isStartedTimeStamp =
                if (isStartReading.value) Timestamp.now() else book.startReading


            val bookUpdate =
                changeNotes || changeRating || isStartReading.value || isFinishReading.value

            val bookToUpdate = mutableMapOf<String, Any?>(
                "finished_reading_at" to isFinishedTimeStamp,
                "started_reading_at" to isStartedTimeStamp,
                "rating" to ratingVal.intValue
            )

            if (changeNotes) {
                bookToUpdate["notes"] = newNotes
            }



            RoundedButton(
                label = "Update",
                radius = 48,
            ) {
                Log.e("nitin", "Updating Firebase with: $newNotes")  // Debug log

                if (bookUpdate) {
                    FirebaseFirestore.getInstance()
                        .collection("books")
                        .document(book.id!!)
                        .update(bookToUpdate)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Book Updated SuccessFully!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(ReaderScreens.HomeScreen.name)
                        }
                        .addOnFailureListener {
                            Log.e("nitin", "Firebase Update Failed", it)
                        }
                }
            }

            if(openDialog.value){
                showAlertDialog(
                    message = stringResource(R.string.sure) + "\n" + stringResource(R.string.actions),
                    openDialog){
                    FirebaseFirestore.getInstance()
                        .collection("books")
                        .document(book.id!!)
                        .delete()
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                openDialog.value = false
                                navController.navigate((ReaderScreens.HomeScreen.name))
                            }
                        }

                }
            }

            RoundedButton(
                label = "Delete",
                radius = 48,
            ){
                openDialog.value = true
            }
        }


    }
}

@Composable
fun showAlertDialog(
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit) {

    if(openDialog.value){
        AlertDialog(onDismissRequest = {openDialog.value = false},
            title = { Text(text = "Delete Book") },
            text = {Text(text = message)},
            confirmButton = {
                Row(modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.Center) {
                    TextButton(onClick = {onYesPressed.invoke()}) {
                        Text(text = "Yes")
                    }

                    TextButton(onClick = {openDialog.value = false}) {
                        Text(text = "No")
                    }
                }
            })

    }

}

@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String,
    onSearch: (String) -> Unit
) {
    val textFieldValue = remember { mutableStateOf(defaultValue) }

    // ✅ Ensure it updates when `defaultValue` changes
    LaunchedEffect(defaultValue) {
        Log.e("nitin", "SimpleForm defaultValue updated: $defaultValue")  // Debug log
        textFieldValue.value = defaultValue
    }

    InputField(
        valueState = textFieldValue,
        labelId = "Enter Your Thoughts...",
        onAction = KeyboardActions {
            Log.e("nitin", "User Input: ${textFieldValue.value}")  // Debug log
            onSearch(textFieldValue.value.trim())
        },
        modifier = modifier,
        enabled = true,
        isSingleLine = true,
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit,
) {
    var ratingState by remember { mutableIntStateOf(rating) }
    var selected by remember { mutableStateOf(false) }

    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "star",
                modifier = Modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }

                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }

    }


}