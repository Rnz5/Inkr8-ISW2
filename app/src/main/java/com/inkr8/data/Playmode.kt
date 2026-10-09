package com.inkr8.data

sealed class PlayMode {
    object Practice: PlayMode()
    object Ranked: PlayMode()
}