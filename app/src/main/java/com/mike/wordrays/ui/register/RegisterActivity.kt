package com.mike.wordrays.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.irozon.sneaker.Sneaker
import com.mike.wordrays.R
import com.mike.wordrays.data.PrefStore
import com.mike.wordrays.data.Status
import com.mike.wordrays.databinding.ActivityRegisterBinding
import com.mike.wordrays.ui.form.FillForm
import com.mike.wordrays.utils.LoadingDialogFragment
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModel()
    private lateinit var loading: LoadingDialogFragment
    private lateinit var binding: ActivityRegisterBinding
    private val prefStore: PrefStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.viewModel = registerViewModel
        binding.lifecycleOwner = this
        loading = LoadingDialogFragment.newInstance()

        btn_register.setOnClickListener {
            registerViewModel.createUser()
        }
        observeData()
    }

    private fun observeData() {
        registerViewModel.status.observe(this, Observer {
            when(it.status) {
                Status.ERROR -> {
                    if (loading.isAdded) {
                        loading.dismiss()
                    }
                    Sneaker.with(this).setMessage(it.message.toString()).sneakError()
                }
                Status.LOADING -> {
                    if (!loading.isAdded) {
                        loading.show(supportFragmentManager, "Loader")
                    }
                }
                Status.SUCCESS -> {
                    if (loading.isAdded) {
                        loading.dismiss()
                    }
                    prefStore.saveLogIn(true)
                    startActivity(Intent(this, FillForm::class.java))
                }
            }
        })

        registerViewModel.errorEntry.observe(this, Observer {
            Sneaker.with(this).setMessage(it.toString()).setDuration(7000).sneakWarning()
        })

    }
}
