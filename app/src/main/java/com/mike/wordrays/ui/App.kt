package com.mike.wordrays.ui

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.mike.wordrays.di.repositoryModule
import com.mike.wordrays.di.sharedPrefModule
import com.mike.wordrays.di.viewModelModule
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App: Application() {


    override fun onCreate() {
        super.onCreate()
        koinToss()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        ViewPump.init(ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/" + "ProximaNovaRegular.ttf")
                                //.setFontAttrId(R.attr.fontPath)
                                .build())
                )
                .build())
    }

    private fun koinToss(){
        startKoin {
            androidContext(this@App)
            modules(listOf(viewModelModule, sharedPrefModule, repositoryModule))
        }
    }

}