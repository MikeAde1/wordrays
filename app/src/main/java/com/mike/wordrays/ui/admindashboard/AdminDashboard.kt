package com.mike.wordrays.ui.admindashboard

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mike.wordrays.R
import com.mike.wordrays.data.PrefStore
import com.mike.wordrays.data.Status
import com.mike.wordrays.data.model.Member
import com.mike.wordrays.databinding.ActivityAdminDashboardBinding
import com.mike.wordrays.databinding.BottomSheetLayoutBinding
import com.mike.wordrays.utils.BottomSheetDialogFragment
import com.mike.wordrays.utils.LoadingDialogFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_admin_dashboard.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminDashboard : AppCompatActivity() {
    private val viewModel: AdminDashboardViewModel by viewModel()
    private val prefStore: PrefStore by inject()
    private lateinit var adapter: DataAdapter
    private lateinit var binding: ActivityAdminDashboardBinding
    private var membersList = mutableListOf<Member>()

    private lateinit var loading: LoadingDialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        adapter = DataAdapter(this, membersList)

        loading = LoadingDialogFragment.newInstance()

        viewModel.getDetails()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.onClick = {
            viewModel.setMember(it)
            DialogFragment.newInstance()
                .show(supportFragmentManager, "Detail")
        }

        textView2.setOnClickListener {
            AlertDialog.Builder(this@AdminDashboard)
                .setMessage("Are you sure you want to logout")
                .setNegativeButton(
                    "No"
                ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                .setPositiveButton(
                    "Yes"
                ) { dialogInterface: DialogInterface, i: Int ->
                    prefStore.logout()
                    dialogInterface.dismiss()
                }.show()
        }

        viewModel.list.observe(this, Observer {
            when(it.status) {
                Status.ERROR -> {
                    if (loading.isAdded) loading.dismiss()
                    Toast.makeText(this, it.message, LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    loading.show(supportFragmentManager, "")
                }
                Status.SUCCESS -> {
                    if (loading.isAdded) loading.dismiss()
                    makeList(it.data!!)
                    Log.d("datalist", it.data.toString())
                }
            }
        })
    }

    private fun makeList(data: List<Member>) {
        adapter.setList(data)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

class DialogFragment : BottomSheetDialogFragment() {
    private val viewModel: AdminDashboardViewModel by sharedViewModel()

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        val binding: BottomSheetLayoutBinding =  DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_layout, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    companion object {
        fun newInstance(): DialogFragment {
            return DialogFragment()
        }
    }
}
