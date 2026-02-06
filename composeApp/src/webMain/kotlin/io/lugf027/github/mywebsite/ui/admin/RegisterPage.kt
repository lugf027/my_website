package io.lugf027.github.mywebsite.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lugf027.github.mywebsite.navigation.NavController
import io.lugf027.github.mywebsite.navigation.Screen
import io.lugf027.github.mywebsite.ui.theme.AppColors
import io.lugf027.github.mywebsite.viewmodel.AuthViewModel

/**
 * 注册页面
 */
@Composable
fun RegisterPage(
    navController: NavController,
    viewModel: AuthViewModel = remember { AuthViewModel() }
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "注册",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "创建您的账号",
                    fontSize = 14.sp,
                    color = AppColors.TextTertiary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; validationError = null },
                    label = { Text("用户名") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; validationError = null },
                    label = { Text("邮箱") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; validationError = null },
                    label = { Text("密码") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; validationError = null },
                    label = { Text("确认密码") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(8.dp)
                )
                
                // Error Message
                val errorMsg = validationError ?: viewModel.error
                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMsg,
                        color = AppColors.Error,
                        fontSize = 14.sp
                    )
                }
                
                // Success Message
                if (viewModel.successMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = viewModel.successMessage!!,
                        color = AppColors.Success,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Register Button
                Button(
                    onClick = {
                        // Validation
                        when {
                            username.length < 3 -> validationError = "用户名至少3个字符"
                            !email.contains("@") -> validationError = "请输入有效的邮箱"
                            password.length < 6 -> validationError = "密码至少6个字符"
                            password != confirmPassword -> validationError = "两次输入的密码不一致"
                            else -> {
                                viewModel.register(username, password, email) {
                                    navController.navigate(Screen.Login)
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = AppColors.White
                        )
                    } else {
                        Text("注册", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Login Link
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "已有账号？",
                        fontSize = 14.sp,
                        color = AppColors.TextTertiary
                    )
                    TextButton(onClick = { navController.navigate(Screen.Login) }) {
                        Text("立即登录", color = AppColors.Primary)
                    }
                }
                
                // Back to Home
                TextButton(onClick = { navController.navigate(Screen.Home) }) {
                    Text("← 返回首页", color = AppColors.TextSecondary)
                }
            }
        }
    }
}
