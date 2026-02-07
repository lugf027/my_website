package io.lugf027.github.mywebsite.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mywebsite.composeapp.generated.resources.NotoSansSC_Regular
import mywebsite.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

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
 * 创建中文字体族
 */
@Composable
fun chineseFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.NotoSansSC_Regular, FontWeight.Normal),
        Font(Res.font.NotoSansSC_Regular, FontWeight.Medium),
        Font(Res.font.NotoSansSC_Regular, FontWeight.SemiBold),
        Font(Res.font.NotoSansSC_Regular, FontWeight.Bold)
    )
}

/**
 * 创建自定义字体排版
 */
@Composable
fun appTypography(): Typography {
    val fontFamily = chineseFontFamily()
    
    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp,
            lineHeight = 64.sp
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp,
            lineHeight = 52.sp
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    )
}

/**
 * 应用主题
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val typography = appTypography()
    
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
        content = content
    )
}
