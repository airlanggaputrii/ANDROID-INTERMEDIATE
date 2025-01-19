package com.bangkit.intermediateandroid.submission1intermediate2.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.bangkit.intermediateandroid.submission1intermediate2.a.activity.MapsActivity
import com.bangkit.intermediateandroid.submission1intermediate2.a.adapter.StoryAdapter
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.remote.response.ListStoryItem
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.repository.StoryAppRepository
import com.bangkit.intermediateandroid.submission1intermediate2.utils.DataDummy
import com.bangkit.intermediateandroid.submission1intermediate2.utils.MainDispatcherRule
import com.bangkit.intermediateandroid.submission1intermediate2.utils.getOrAwaitValue
import com.bangkit.intermediateandroid.submission1intermediate2.a.mainviewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyStory = DataDummy.generateDummyStories()


    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyAppRepository)
    }

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()


    @Test
    fun `when getStories Should Not Null and Return Success` () = runTest {
        val data : PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory.listStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        val  mockContext = mock(MapsActivity::class.java)
        val token = mainViewModel.getPreference(mockContext).value.toString()
        `when`(storyAppRepository.getStory("Bearer $token")).thenReturn(expectedStory)
        val listStoryViewModel = MainViewModel(storyAppRepository)
        val actualStory : PagingData<ListStoryItem> = listStoryViewModel.story("Bearer $token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.listStory, differ.snapshot())
        Assert.assertEquals(dummyStory.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory.listStory[0], differ.snapshot()[0])

    }
    @Test
    fun `when getStories is Null and Return Error` () = runTest {
        val data : PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        val  mockContext = mock(MapsActivity::class.java)
        val token = mainViewModel.getPreference(mockContext).value.toString()
        `when`(storyAppRepository.getStory("Bearer $token")).thenReturn(expectedStory)
        val listStoryViewModel = MainViewModel(storyAppRepository)
        val actualStory : PagingData<ListStoryItem> = listStoryViewModel.story("Bearer $token").getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}
class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

