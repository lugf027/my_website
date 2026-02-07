package io.lugf027.github.mywebsite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.ui.theme.AppColors

/**
 * 页脚组件
 */
@Composable
fun Footer(
    icpRecord: String = "",
    copyright: String = "© 2026 MyWebsite. All rights reserved."
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.SurfaceVariant)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = copyright,
            fontSize = 14.sp,
            color = AppColors.TextTertiary,
            textAlign = TextAlign.Center
        )
        
        if (icpRecord.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = icpRecord,
                fontSize = 12.sp,
                color = AppColors.TextTertiary,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Powered by Kotlin Multiplatform & Compose",
            fontSize = 12.sp,
            color = AppColors.TextTertiary,
            textAlign = TextAlign.Center
        )
    }
}
