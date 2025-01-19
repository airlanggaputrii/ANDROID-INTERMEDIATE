package com.bangkit.intermediateandroid.submission1intermediate2.a.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bangkit.intermediateandroid.submission1intermediate2.R
import com.bangkit.intermediateandroid.submission1intermediate2.a.data.local.UserPreference
import com.bangkit.intermediateandroid.submission1intermediate2.databinding.ActivityMainBinding
import com.bangkit.intermediateandroid.submission1intermediate2.a.adapter.LoadingStateAdapter
import com.bangkit.intermediateandroid.submission1intermediate2.a.adapter.StoryAdapter
import com.bangkit.intermediateandroid.submission1intermediate2.a.utils.ViewModelFactory
import com.bangkit.intermediateandroid.submission1intermediate2.a.mainviewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel : MainViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserPreference.init(applicationContext)
        if (UserPreference.getToken() == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        getStory()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDialogLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Apakah anda yakin untuk logout?")
            .setPositiveButton("Ya") { dialogInterface, i ->
                UserPreference.clear()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Tidak") { dialogInterface, i ->
                dialogInterface.cancel()
            }
            .show()

    }
    private fun getToken() : String?  = UserPreference.getToken()
    private fun getStory() {
        val token = getToken()
        val adapter =
            StoryAdapter()
        if (token != null) {
            binding.rvListStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            mainViewModel.story("Bearer $token").observe(this) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    // Option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionmenu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ic_map -> {
                // Handle map menu click
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_language -> {
                // Handle language menu click
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.action_logout -> {
                // Handle logout menu click
                showDialogLogout()
                 // Clear user preferences
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                finish() // Close current activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

