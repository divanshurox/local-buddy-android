package com.example.localbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.api.models.entity.User
import com.example.localbuddy.data.Resource
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
    private lateinit var navController: NavController

    @SuppressLint("SetTextI18n")
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
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_login,
                R.id.nav_signup,
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
                }
                else -> NavigationUI.onNavDestinationSelected(it,navController)
            }
            drawerLayout.closeDrawers()
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
                val navHeader: View = navView.getHeaderView(0)
                val navHeaderName: TextView = navHeader.findViewById(R.id.nav_header_name)
                val navHeaderUsername: TextView = navHeader.findViewById(R.id.nav_header_username)
                val navHeaderImageView: ImageView = navHeader.findViewById(R.id.nav_header_image)
                navHeaderName.text = it.value.firstname + " " + it.value.lastname
                navHeaderUsername.text = it.value.username
                navHeaderImageView.avatarUrl(it.value.avatar)
            } else if (it == null) {
                updateMenu(it)
                navController.popBackStack()
                navController.graph = inflater.inflate(R.navigation.mobile_navigation)
            }
        })
    }

//    private fun selectDrawerItem(item: MenuItem) {
//        when (item.itemId) {
//            R.id.nav_login -> replaceFragment(LoginFragment(), item.title.toString())
//            R.id.nav_signup -> replaceFragment(SignupFragment(), item.title.toString())
//        }
//    }
//
//    private fun replaceFragment(fragment: Fragment, title: String) {
//        supportFragmentManager.commit{
//            add(R.id.frame_layout, fragment, title).setTransition(
//                FragmentTransaction.TRANSIT_FRAGMENT_OPEN
//            )
//            setReorderingAllowed(true)
//            addToBackStack(title)
//        }
//        supportActionBar?.title = title
//        drawerLayout.closeDrawers()
//    }

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
        return NavigationUI.navigateUp(navController,drawerLayout) || super.onSupportNavigateUp()
    }
}