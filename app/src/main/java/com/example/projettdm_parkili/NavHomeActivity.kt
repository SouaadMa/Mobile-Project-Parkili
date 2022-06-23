package com.example.projettdm_parkili

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.projettdm_parkili.databinding.ActivityNavHomeBinding
import com.example.projettdm_parkili.utils.getUserName
import com.google.android.material.navigation.NavigationView

class NavHomeActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityNavHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment = supportFragmentManager. findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this,navController,binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navView,navController)


        //Logged in as : fullname in nav drawer
        val navigationView : NavigationView = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.tv_username)
        navUsername.text = getUserName(this)


    }


    override fun onSupportNavigateUp() = super.onSupportNavigateUp() || NavigationUI.navigateUp(navController,binding.drawerLayout)


}