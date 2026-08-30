package com.pockettrack

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
