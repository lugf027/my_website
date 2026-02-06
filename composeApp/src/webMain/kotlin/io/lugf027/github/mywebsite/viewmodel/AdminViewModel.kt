package io.lugf027.github.mywebsite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.lugf027.github.mywebsite.api.ApiClient
import io.lugf027.github.mywebsite.api.ApiRoutes
import io.lugf027.github.mywebsite.dto.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 后台管理 ViewModel
 */
class AdminViewModel {
    // 统计概览
    var statisticsOverview by mutableStateOf<StatisticsOverview?>(null)
        private set
    
    // 统计报表
    var statisticsReport by mutableStateOf<StatisticsReport?>(null)
        private set
    
    // 博客列表（后台）
    var blogList by mutableStateOf<List<BlogListItem>>(emptyList())
        private set
    
    var currentPage by mutableStateOf(1)
        private set
    
    var totalPages by mutableStateOf(1)
        private set
    
    var total by mutableStateOf(0L)
        private set
    
    // 博客详情（编辑用）
    var editingBlog by mutableStateOf<BlogDetail?>(null)
        private set
    
    // 站点配置
    var siteConfigs by mutableStateOf<List<SiteConfigItem>>(emptyList())
        private set
    
    // 通用状态
    var isLoading by mutableStateOf(false)
        private set
    
    var error by mutableStateOf<String?>(null)
        private set
    
    var successMessage by mutableStateOf<String?>(null)
        private set
    
    private val scope = CoroutineScope(Dispatchers.Default)
    
    /**
     * 加载统计概览
     */
    fun loadStatisticsOverview() {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.get<StatisticsOverview>(ApiRoutes.Admin.Statistics.OVERVIEW)
            
            if (response.code == 0 && response.data != null) {
                statisticsOverview = response.data
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 加载统计报表
     */
    fun loadStatisticsReport(startDate: String? = null, endDate: String? = null) {
        scope.launch {
            isLoading = true
            error = null
            
            val params = mutableMapOf<String, String>()
            startDate?.let { params["startDate"] = it }
            endDate?.let { params["endDate"] = it }
            
            val response = ApiClient.get<StatisticsReport>(
                ApiRoutes.Admin.Statistics.REPORT,
                params
            )
            
            if (response.code == 0 && response.data != null) {
                statisticsReport = response.data
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 加载博客列表（后台）
     */
    fun loadBlogs(page: Int = 1, status: String? = null, keyword: String? = null) {
        scope.launch {
            isLoading = true
            error = null
            
            val params = mutableMapOf(
                "page" to page.toString(),
                "pageSize" to "10"
            )
            status?.let { params["status"] = it }
            keyword?.let { params["keyword"] = it }
            
            val response = ApiClient.get<PageData<BlogListItem>>(
                ApiRoutes.Admin.Blogs.LIST,
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
     * 加载博客详情（编辑）
     */
    fun loadBlogForEdit(id: Long) {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.get<BlogDetail>("${ApiRoutes.Admin.Blogs.BASE}/$id")
            
            if (response.code == 0 && response.data != null) {
                editingBlog = response.data
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 创建博客
     */
    fun createBlog(request: BlogRequest, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.post<BlogRequest, BlogDetail>(
                ApiRoutes.Admin.Blogs.CREATE,
                request
            )
            
            if (response.code == 0) {
                successMessage = "博客创建成功"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 更新博客
     */
    fun updateBlog(id: Long, request: BlogRequest, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.put<BlogRequest, BlogDetail>(
                ApiRoutes.Admin.Blogs.update(id),
                request
            )
            
            if (response.code == 0) {
                successMessage = "博客更新成功"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 删除博客
     */
    fun deleteBlog(id: Long, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.delete<Unit>(ApiRoutes.Admin.Blogs.delete(id))
            
            if (response.code == 0) {
                successMessage = "博客删除成功"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 发布博客
     */
    fun publishBlog(id: Long, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.post<Unit, BlogDetail>(
                ApiRoutes.Admin.Blogs.publish(id),
                Unit
            )
            
            if (response.code == 0) {
                successMessage = "博客发布成功"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 取消发布博客
     */
    fun unpublishBlog(id: Long, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.post<Unit, BlogDetail>(
                ApiRoutes.Admin.Blogs.unpublish(id),
                Unit
            )
            
            if (response.code == 0) {
                successMessage = "博客已取消发布"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 加载站点配置
     */
    fun loadSiteConfigs() {
        scope.launch {
            isLoading = true
            error = null
            
            val response = ApiClient.get<List<SiteConfigItem>>(ApiRoutes.Admin.SiteConfig.GET)
            
            if (response.code == 0 && response.data != null) {
                siteConfigs = response.data!!
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 更新站点配置
     */
    fun updateSiteConfigs(configs: List<SiteConfigItem>, onSuccess: () -> Unit) {
        scope.launch {
            isLoading = true
            error = null
            
            val request = SiteConfigUpdateRequest(configs)
            val response = ApiClient.put<SiteConfigUpdateRequest, Unit>(
                ApiRoutes.Admin.SiteConfig.UPDATE,
                request
            )
            
            if (response.code == 0) {
                successMessage = "配置更新成功"
                onSuccess()
            } else {
                error = response.message
            }
            
            isLoading = false
        }
    }
    
    /**
     * 清除编辑中的博客
     */
    fun clearEditingBlog() {
        editingBlog = null
    }
    
    /**
     * 清除错误
     */
    fun clearError() {
        error = null
    }
    
    /**
     * 清除成功消息
     */
    fun clearSuccessMessage() {
        successMessage = null
    }
}
