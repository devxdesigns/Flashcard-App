package com.example.flashcardapp

import android.app.Application
import com.example.flashcardapp.data.AppContainer

class FlashcardApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}