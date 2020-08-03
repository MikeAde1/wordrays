package com.mike.wordrays.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.irozon.sneaker.Sneaker
import com.mike.wordrays.R
import com.mike.wordrays.data.Status
import com.mike.wordrays.databinding.ActivityLoginBinding
import com.mike.wordrays.ui.admindashboard.AdminDashboard
import com.mike.wordrays.ui.form.FillForm
import com.mike.wordrays.ui.home.Home
import com.mike.wordrays.utils.LoadingDialogFragment
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class Login : AppCompatActivity() {
    private lateinit var loading: LoadingDialogFragment
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.viewmodel  = viewModel

        loading = LoadingDialogFragment.newInstance()

        btn_login.setOnClickListener {
            viewModel.loginUser()
        }
        viewModel.errorEntry.observe(this, Observer {
            Sneaker.with(this).setMessage(it).sneakWarning()
        })

        viewModel.status.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    //check if user has filled form
                    loading.dismiss()
                    when {
                        it.message.isNullOrBlank() -> {
                            startActivity(
                                Intent(this, FillForm::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        }
                        it.message == "filled" -> {
                            startActivity(
                                Intent(this, Home::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        }
                        else -> {
                            startActivity(
                                Intent(this, AdminDashboard::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        }
                    }
                }
                Status.LOADING -> {
                    loading.show(supportFragmentManager,  "")
                }
                Status.ERROR -> {
                    loading.dismiss()
                    Sneaker.with(this).setMessage(it.message.toString()).setDuration(5000).sneakError()
                }
            }
        })

    }
}
