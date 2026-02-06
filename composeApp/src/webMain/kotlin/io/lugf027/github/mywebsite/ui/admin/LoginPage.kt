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
 * 登录页面
 */
@Composable
fun LoginPage(
    navController: NavController,
    viewModel: AuthViewModel = remember { AuthViewModel() }
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
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
                    text = "登录",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "登录后台管理系统",
                    fontSize = 14.sp,
                    color = AppColors.TextTertiary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("用户名") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("密码") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(8.dp)
                )
                
                // Error Message
                if (viewModel.error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = viewModel.error!!,
                        color = AppColors.Error,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Login Button
                Button(
                    onClick = {
                        viewModel.login(username, password) {
                            navController.navigate(Screen.Dashboard)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !viewModel.isLoading && username.isNotBlank() && password.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = AppColors.White
                        )
                    } else {
                        Text("登录", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Register Link
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "还没有账号？",
                        fontSize = 14.sp,
                        color = AppColors.TextTertiary
                    )
                    TextButton(onClick = { navController.navigate(Screen.Register) }) {
                        Text("立即注册", color = AppColors.Primary)
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
