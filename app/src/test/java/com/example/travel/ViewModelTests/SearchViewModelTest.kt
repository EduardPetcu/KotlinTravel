import com.example.travel.data.User
import com.example.travel.data.user.SearchViewModel
import com.example.travel.repositories.FakeDatabaseRepository
import com.example.travel.repository.DatabaseRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SearchViewModelTest {
    private lateinit var searchViewModel: SearchViewModel
    private val mockDatabaseRepositoryImpl = FakeDatabaseRepository()

    @Before
    fun setup() {
        mockDatabaseRepositoryImpl.addUserData(User("testUser1", "testEmail1", "testPassword"))
        mockDatabaseRepositoryImpl.addUserData(User("testUser2", "testEmail2", "testPassword2"))
        mockDatabaseRepositoryImpl.addUserData(User("testUser3", "testEmail3", "testPassword3"))
        searchViewModel = SearchViewModel(mockDatabaseRepositoryImpl)
    }

    @Test
    fun onSearchTextChange_updatesSearchText() = runTest {
        val searchText = "testSearch"
        searchViewModel.onSearchTextChange(searchText)
        assert(searchViewModel.searchText.first() == searchText)
    }

    @Test
    fun onSearchTextChange_updatesIsSearching() = runTest {
        val searchText = "testUser2"
        searchViewModel.onSearchTextChange(searchText)
        assert(searchViewModel.searchText.first() == searchText)
    }

    @Test
    fun onSearchTextChange_emptySearchText_doesNotUpdateIsSearching() = runTest {
        val searchText = ""
        searchViewModel.onSearchTextChange(searchText)
        assert(!searchViewModel.isSearching.first())
    }

    @Test
    fun onSearchTextChange_updatesUsers() = runTest {
        val searchText = "testUser"
        searchViewModel.onSearchTextChange(searchText)
        val users = searchViewModel.users.value
        val usersFromFakeDB = mockDatabaseRepositoryImpl.getAllUsers()
        assert(users.size == usersFromFakeDB.size)
        assert(users[0].username == usersFromFakeDB[0].username)
    }
}