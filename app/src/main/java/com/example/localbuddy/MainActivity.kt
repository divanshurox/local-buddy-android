package com.example.localbuddy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.api.models.entity.User
import com.example.localbuddy.data.Resource
import com.example.localbuddy.ui.auth.LoginFragment
import com.example.localbuddy.ui.auth.SignupFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    companion object {
        private val SHARED_PREF = "shared_pref"
        private val PREF_TOKEN = "token"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_login,
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.action_logout -> {
                    authViewModel.logout()
                    run {
                        sharedPreferences.edit {
                            remove(PREF_TOKEN)
                        }
                    }
                    drawerLayout.closeDrawers()
                }
                else -> selectDrawerItem(it)
            }
            return@setNavigationItemSelectedListener true
        }


        sharedPreferences.getString(PREF_TOKEN, null)?.let { t ->
            authViewModel.signinUserToken(t)
        }

        authViewModel.user.observe({ lifecycle }, {
            val inflater = navController.navInflater
            if (it is Resource.Success) {
                updateMenu(it.value)
                it.value.token.let { t ->
                    sharedPreferences.edit {
                        putString(PREF_TOKEN, t)
                    }
                }
                navController.popBackStack()
                navController.graph = inflater.inflate(R.navigation.nav_graph_auth)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
            } else if (it == null) {
                updateMenu(it)
                navController.popBackStack()
                navController.graph = inflater.inflate(R.navigation.mobile_navigation)
            }
        })
    }

    private fun selectDrawerItem(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_login -> replaceFragment(item, LoginFragment(), item.title.toString())
            R.id.nav_signup -> replaceFragment(item, SignupFragment(), item.title.toString())
        }
    }

    private fun replaceFragment(item: MenuItem, fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment).setTransition(
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN
            )
            .addToBackStack(null).commit()
        supportActionBar?.title = title
        drawerLayout.closeDrawers()
    }

    private fun updateMenu(user: User?) {
        navView.menu.clear()
        if (user != null) {
            navView.inflateMenu(R.menu.activity_main_drawer_auth)
        } else {
            navView.inflateMenu(R.menu.activity_main_drawer)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}