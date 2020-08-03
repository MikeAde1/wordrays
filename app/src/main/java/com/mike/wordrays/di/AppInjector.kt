package com.mike.wordrays.di

import com.mike.wordrays.data.PrefStore
import com.mike.wordrays.ui.admindashboard.AdminDashboardViewModel
import com.mike.wordrays.ui.form.FillFormViewModel
import com.mike.wordrays.ui.login.LoginViewModel
import com.mike.wordrays.ui.register.AdminRegisterViewModel
import com.mike.wordrays.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { RegisterViewModel() }
    viewModel { LoginViewModel() }
    viewModel { AdminRegisterViewModel() }
    viewModel { AdminDashboardViewModel() }
    viewModel { FillFormViewModel() }
}

val sharedPrefModule = module {
    single { PrefStore(get()) }
}

val repositoryModule = module{
}
