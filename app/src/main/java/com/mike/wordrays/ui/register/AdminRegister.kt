package com.mike.wordrays.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.irozon.sneaker.Sneaker
import com.mike.wordrays.R
import com.mike.wordrays.data.Status
import com.mike.wordrays.databinding.ActivityAdminRegisterBinding
import com.mike.wordrays.ui.admindashboard.AdminDashboard
import com.mike.wordrays.utils.LoadingDialogFragment
import kotlinx.android.synthetic.main.activity_admin_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminRegister : AppCompatActivity() {

    private val viewModel: AdminRegisterViewModel by viewModel()
    private lateinit var binding: ActivityAdminRegisterBinding
    private lateinit var loading: LoadingDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = DataBindingUtil.setContentView(this, R.layout.activity_admin_register)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        loading = LoadingDialogFragment.newInstance()

        btn_register.setOnClickListener {
            viewModel.createAdmin()
        }

        viewModel.status.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (loading.isAdded) loading.dismiss()
                    startActivity(Intent(this, AdminDashboard::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                    finish()
                }
                Status.LOADING -> {
                    loading.show(supportFragmentManager, "")
                }
                Status.ERROR -> {
                    if (loading.isAdded) loading.dismiss()
                    Sneaker.with(this).setMessage(it.message.toString()).setDuration(5000).sneakError()
                }
            }
        })
    }
}
