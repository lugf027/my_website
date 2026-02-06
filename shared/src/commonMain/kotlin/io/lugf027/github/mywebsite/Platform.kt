package io.lugf027.github.mywebsite

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform