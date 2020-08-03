package com.mike.wordrays.ui.home

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mike.wordrays.R
import com.mike.wordrays.data.PrefStore
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject


class Home : AppCompatActivity() {

    val prefStore: PrefStore by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setToolBar()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            AlertDialog.Builder(this@Home)
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
        return super.onOptionsItemSelected(item)
    }

    private fun setToolBar() {
        setSupportActionBar(toolBar)
        val viewActionBar = layoutInflater.inflate(R.layout.actionbar_background, null)
        val params = ActionBar.LayoutParams(//Center the textview in the ActionBar!
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT,
            Gravity.CENTER)
        val tvTitle = viewActionBar.findViewById(R.id.actionbar_tv) as TextView
        tvTitle.text = "HOME"
        supportActionBar?.setCustomView(viewActionBar, params)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}
