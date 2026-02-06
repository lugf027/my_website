package io.lugf027.github.mywebsite.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.dto.SiteConfigItem
import io.lugf027.github.mywebsite.dto.SiteConfigKey
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.ui.components.Loading
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.AdminViewModel

/**
 * 站点设置页面
 */
@Composable
fun SiteConfigPage(
    navController: NavController,
    viewModel: AdminViewModel = remember { AdminViewModel() }
) {
    var configMap by remember { mutableStateOf(mutableMapOf<String, String>()) }
    
    LaunchedEffect(Unit) {
        viewModel.loadSiteConfigs()
    }
    
    // Initialize config map when loaded
    LaunchedEffect(viewModel.siteConfigs) {
        if (viewModel.siteConfigs.isNotEmpty()) {
            configMap = viewModel.siteConfigs.associate { it.key to it.value }.toMutableMap()
        }
    }
    
    AdminLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "站点设置",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    
                    Text(
                        text = "配置网站基本信息",
                        fontSize = 14.sp,
                        color = AppColors.TextTertiary
                    )
                }
                
                Button(
                    onClick = {
                        val configs = configMap.map { (key, value) ->
                            SiteConfigItem(key, value, "")
                        }
                        viewModel.updateSiteConfigs(configs) {
                            viewModel.loadSiteConfigs()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = AppColors.White
                        )
                    } else {
                        Text("保存设置")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Success/Error Messages
            viewModel.successMessage?.let { message ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = AppColors.Success.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = message,
                        color = AppColors.Success,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            viewModel.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = AppColors.Error.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = error,
                        color = AppColors.Error,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (viewModel.isLoading && viewModel.siteConfigs.isEmpty()) {
                Loading()
            } else {
                // Site Info Section
                ConfigSection(title = "网站信息") {
                    ConfigField(
                        label = "网站名称",
                        value = configMap[SiteConfigKey.SITE_NAME] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.SITE_NAME to it)).toMutableMap() }
                    )
                    
                    ConfigField(
                        label = "网站描述",
                        value = configMap[SiteConfigKey.SITE_DESCRIPTION] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.SITE_DESCRIPTION to it)).toMutableMap() },
                        maxLines = 3
                    )
                    
                    ConfigField(
                        label = "ICP 备案号",
                        value = configMap[SiteConfigKey.ICP_RECORD] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.ICP_RECORD to it)).toMutableMap() }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Owner Info Section
                ConfigSection(title = "个人信息") {
                    ConfigField(
                        label = "姓名",
                        value = configMap[SiteConfigKey.OWNER_NAME] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.OWNER_NAME to it)).toMutableMap() }
                    )
                    
                    ConfigField(
                        label = "职位/头衔",
                        value = configMap[SiteConfigKey.OWNER_TITLE] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.OWNER_TITLE to it)).toMutableMap() }
                    )
                    
                    ConfigField(
                        label = "个人简介",
                        value = configMap[SiteConfigKey.OWNER_BIO] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.OWNER_BIO to it)).toMutableMap() },
                        maxLines = 4
                    )
                    
                    ConfigField(
                        label = "头像 URL",
                        value = configMap[SiteConfigKey.OWNER_AVATAR] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.OWNER_AVATAR to it)).toMutableMap() }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Social Links Section
                ConfigSection(title = "社交链接") {
                    ConfigField(
                        label = "GitHub",
                        value = configMap[SiteConfigKey.GITHUB_URL] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.GITHUB_URL to it)).toMutableMap() }
                    )
                    
                    ConfigField(
                        label = "LinkedIn",
                        value = configMap[SiteConfigKey.LINKEDIN_URL] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.LINKEDIN_URL to it)).toMutableMap() }
                    )
                    
                    ConfigField(
                        label = "Email",
                        value = configMap[SiteConfigKey.EMAIL] ?: "",
                        onValueChange = { configMap = (configMap + (SiteConfigKey.EMAIL to it)).toMutableMap() }
                    )
                }
            }
        }
    }
}

/**
 * 配置分组
 */
@Composable
private fun ConfigSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

/**
 * 配置字段
 */
@Composable
private fun ConfigField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.TextSecondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            shape = RoundedCornerShape(8.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
