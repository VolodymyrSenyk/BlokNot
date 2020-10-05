package com.senyk.volodymyr.bloknot.presentation.view.activity.base

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import com.senyk.volodymyr.bloknot.presentation.viewmodel.factory.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            android.R.id.home -> {
                onBackPressed(); true
            }

            else -> super.onOptionsItemSelected(menuItem)
        }
}
