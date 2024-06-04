package com.example.travel.data.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel.repository.DatabaseRepository
import com.example.travel.repository.DatabaseRepositoryImpl
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class SearchViewModel(private val databaseRepositoryImpl: DatabaseRepository = DatabaseRepositoryImpl()) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _users = MutableStateFlow(databaseRepositoryImpl.getAllUsers())
    @OptIn(FlowPreview::class)
    val users = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true }}
        .combine(_users) { query, users ->
            if (query.isBlank()) {
                listOf()
            } else {
                users.filter { it.doesMatchSearchQuery(query) }
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
    }

}