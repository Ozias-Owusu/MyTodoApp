package com.persol.mytodoapp

import android.app.Application
import com.persol.mytodoapp.data.AppContainer
import com.persol.mytodoapp.data.DefaultAppContainer

class PostItemApplication: Application()  {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}
