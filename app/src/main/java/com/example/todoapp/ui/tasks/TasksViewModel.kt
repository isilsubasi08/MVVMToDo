package com.example.todoapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.todoapp.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/*
* Hilt, ViewModel'i destekler ve bu nedenle ViewModels ile çalışmak hilt ile çok kolaydır.
* */

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao : TaskDao
) : ViewModel(){

    val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)


    private val tasksFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    ){query,sortOrder,hideCompleted ->
        Triple(query,sortOrder,hideCompleted)
    } .flatMapLatest { (query,sortOrder,hideCompleted) ->
        taskDao.getTasks(query,sortOrder,hideCompleted)
    }


    val tasks = tasksFlow.asLiveData()

}


enum class SortOrder{BY_NAME,BY_DATE}