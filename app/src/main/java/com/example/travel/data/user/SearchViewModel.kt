package com.example.travel.data.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel.repository.DatabaseRepositoryImpl
import com.example.travel.repository.Images.ImageRepositoryImpl
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class SearchViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val imageRepositoryImpl = ImageRepositoryImpl()
    private val databaseRepositoryImpl = DatabaseRepositoryImpl()
    private val _users = MutableStateFlow(databaseRepositoryImpl.getAllUsers())
    @OptIn(FlowPreview::class)
    val users = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true }}
        .combine(_users) { query, users ->
            if (query.isBlank()) {
                listOf()
            } else {
                //delay(1000L)
                users.filter { it.doesMatchSearchQuery(query) }.map { user ->
                    val bitmap = imageRepositoryImpl.loadImageFromFirebaseStorage("/ProfilePicture/${user.id}")
                    user.copy(imagePicture = bitmap)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _users.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        Log.d("SearchViewModel", "Search text changed: $text")
    }
}