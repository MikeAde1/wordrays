package com.mike.wordrays.ui.form

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.irozon.sneaker.Sneaker
import com.mike.wordrays.R
import com.mike.wordrays.data.Status
import com.mike.wordrays.databinding.ActivityFillForm2Binding
import com.mike.wordrays.databinding.ActivityRegisterBinding
import com.mike.wordrays.ui.home.Home
import com.mike.wordrays.utils.LoadingDialogFragment
import kotlinx.android.synthetic.main.activity_fill_form2.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FillForm : AppCompatActivity() {

    private val viewModel: FillFormViewModel by viewModel()
    private lateinit var loading: LoadingDialogFragment
    private lateinit var binding: ActivityFillForm2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fill_form2)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        loading = LoadingDialogFragment.newInstance()

        btn_submit.setOnClickListener {
            viewModel.sendProfile()
        }

        dob.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.errorEntry.observe(this, Observer {
            Sneaker.with(this).setMessage(it).setDuration(5000).sneakError()
        })

        viewModel.status.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    if (loading.isAdded) { loading.dismiss()}
                    Sneaker.with(this).setMessage(it.message.toString()).setDuration(7000).sneakError()
                }
                Status.LOADING -> {
                    loading.show(supportFragmentManager, "")
                }
                Status.SUCCESS -> {
                    if (loading.isAdded) { loading.dismiss()}
                    startActivity(Intent(this, Home::class.java))
                        //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                    finish()
                }
            }
        })

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR) // current year
        val mMonth = calendar.get(Calendar.MONTH) // current month
        val mDay = calendar.get(Calendar.DAY_OF_MONTH) // current day
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // set day of month , month and year value in the edit text
            val date =  year.toString()+ "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                val formattedDate  = dateFormat.parse(date)
                val newDate = dateFormat.format(formattedDate)
                dob.setText(newDate)
                viewModel.dateOfBirth.value = newDate
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }
}
