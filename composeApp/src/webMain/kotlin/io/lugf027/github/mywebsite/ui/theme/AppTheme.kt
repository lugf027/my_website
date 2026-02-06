package io.lugf027.github.mywebsite.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 颜色定义
 */
object AppColors {
    // Primary colors
    val Primary = Color(0xFF2563EB)
    val PrimaryLight = Color(0xFF3B82F6)
    val PrimaryLighter = Color(0xFF60A5FA)
    
    // Background colors
    val Background = Color(0xFFFFFFFF)
    val Surface = Color(0xFFF8FAFC)
    val SurfaceVariant = Color(0xFFF1F5F9)
    
    // Text colors
    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF475569)
    val TextTertiary = Color(0xFF94A3B8)
    
    // Functional colors
    val Success = Color(0xFF22C55E)
    val Error = Color(0xFFEF4444)
    val Warning = Color(0xFFF59E0B)
    val Info = Color(0xFF6366F1)
    
    // Additional colors
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
    val Border = Color(0xFFE2E8F0)
    val Divider = Color(0xFFE2E8F0)
}

/**
 * 自定义 Material3 颜色方案
 */
private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.White,
    primaryContainer = AppColors.PrimaryLighter,
    onPrimaryContainer = AppColors.TextPrimary,
    secondary = AppColors.Info,
    onSecondary = AppColors.White,
    background = AppColors.Background,
    onBackground = AppColors.TextPrimary,
    surface = AppColors.Surface,
    onSurface = AppColors.TextPrimary,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.TextSecondary,
    error = AppColors.Error,
    onError = AppColors.White,
    outline = AppColors.Border
)

/**
 * 应用主题
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
