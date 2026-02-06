package io.lugf027.github.mywebsite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.lugf027.github.mywebsite.api.ApiClient
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.dto.BlogDetail
import io.lugf027.github.mywebsite.dto.BlogListItem
import io.lugf027.github.mywebsite.dto.PageData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 博客 ViewModel
 */
class BlogViewModel {
    // 博客列表状态
    var blogList by mutableStateOf<List<BlogListItem>>(emptyList())
        private set
    
    var currentPage by mutableStateOf(1)
        private set
    
    var totalPages by mutableStateOf(1)
        private set
    
    var total by mutableStateOf(0L)
        private set
    
    // 博客详情状态
    var blogDetail by mutableStateOf<BlogDetail?>(null)
        private set
    
    // 通用状态
    var isLoading by mutableStateOf(false)
        private set
    
    var error by mutableStateOf<String?>(null)
        private set
    
    private val scope = CoroutineScope(Dispatchers.Default)
    
    /**
     * 加载博客列表
     */
    fun loadBlogs(page: Int = 1, keyword: String? = null) {
        scope.launch {
            isLoading = true
            error = null
            
            val params = mutableMapOf(
                "page" to page.toString(),
                "pageSize" to "10"
            )
            if (!keyword.isNullOrBlank()) {
                params["keyword"] = keyword
            }
            
            val response = ApiClient.get<PageData<BlogListItem>>(
                ApiRoutes.Blog.LIST,
                params
            )
            
            if (response.code == 0 && response.data != null) {
                blogList = response.data!!.items
                currentPage = response.data!!.page
                totalPages = response.data!!.totalPages
                total = response.data!!.total
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 加载博客详情
     */
    fun loadBlogDetail(id: Long) {
        scope.launch {
            isLoading = true
            error = null
            blogDetail = null
            
            val response = ApiClient.get<BlogDetail>(ApiRoutes.Blog.detail(id))
            
            if (response.code == 0 && response.data != null) {
                blogDetail = response.data
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 翻页
     */
    fun goToPage(page: Int) {
        if (page in 1..totalPages) {
            loadBlogs(page)
        }
    }
    
    /**
     * 搜索
     */
    fun search(keyword: String) {
        loadBlogs(1, keyword)
    }
    
    /**
     * 清除详情
     */
    fun clearDetail() {
        blogDetail = null
    }
}
