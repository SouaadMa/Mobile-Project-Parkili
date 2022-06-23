package com.example.projettdm_parkili

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.projettdm_parkili.databinding.ActivityNavHomeBinding

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
    }


    override fun onSupportNavigateUp() = super.onSupportNavigateUp() || NavigationUI.navigateUp(navController,binding.drawerLayout)


}