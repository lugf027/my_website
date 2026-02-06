package io.lugf027.github.mywebsite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.lugf027.github.mywebsite.api.ApiClient
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.dto.BlogListItem
import io.lugf027.github.mywebsite.dto.SiteOverview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel
 */
class HomeViewModel {
    var siteOverview by mutableStateOf<SiteOverview?>(null)
        private set
    
    var latestBlogs by mutableStateOf<List<BlogListItem>>(emptyList())
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var error by mutableStateOf<String?>(null)
        private set
    
    private val scope = CoroutineScope(Dispatchers.Default)
    
    /**
     * 加载首页数据
     */
    fun loadData() {
        scope.launch {
            isLoading = true
            error = null
            
            // 加载站点概览
            val siteResponse = ApiClient.get<SiteOverview>(ApiRoutes.Site.OVERVIEW)
            if (siteResponse.code == 0 && siteResponse.data != null) {
                siteOverview = siteResponse.data
            }
            
            // 加载最新博客
            val blogsResponse = ApiClient.get<List<BlogListItem>>(
                "${ApiRoutes.Blog.BASE}/latest",
                mapOf("limit" to "5")
            )
            if (blogsResponse.code == 0 && blogsResponse.data != null) {
                latestBlogs = blogsResponse.data!!
            }
            
            isLoading = false
        }
    }
}
