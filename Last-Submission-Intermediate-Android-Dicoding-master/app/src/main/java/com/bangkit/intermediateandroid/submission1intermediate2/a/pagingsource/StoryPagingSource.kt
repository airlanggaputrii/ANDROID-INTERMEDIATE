package com.bangkit.intermediateandroid.submission1intermediate2.a.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.ApiService
import com.bangkit.intermediateandroid.submission1intermediate2.a.api.Story
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException

class StoryPagingSource(
    private val apiService: ApiService,
    private val token: String
) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: 1
            val size = params.loadSize

            val response = apiService.getAllStoriesWithPaging3(token, page, size).awaitResponse()
            if (response.isSuccessful) {
                val storyResponse = response.body()
                val stories = storyResponse?.listStory

                LoadResult.Page(
                    data = stories ?: emptyList(),
                    prevKey = if (page > 1) page - 1 else null,
                    nextKey = if (stories.isNullOrEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load stories"))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition
    }
}
