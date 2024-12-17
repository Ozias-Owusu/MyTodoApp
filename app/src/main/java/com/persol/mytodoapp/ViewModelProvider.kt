package com.persol.mytodoapp

import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.persol.mytodoapp.appmodule.PostItemApplication
import com.persol.mytodoapp.database.TodoDatabase
import com.persol.mytodoapp.viewModels.PostViewModel
import com.persol.mytodoapp.viewModels.PinScreenViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            PostViewModel(myTodoApplication().container.postRepository)
        }
        initializer {
            PinScreenViewModel(myTodoApplication().applicationContext)
        }
    }
}

fun CreationExtras.myTodoApplication(): PostItemApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PostItemApplication)