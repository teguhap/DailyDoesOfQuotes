package com.project.dailydoesofquotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class ActivityUtama : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utama)

        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction().replace(R.id.frameLayoutUtama, fragment, fragment.javaClass.simpleName)
                .commit()
        }


        val navigation = findViewById<BottomNavigationView>(R.id.navigationBarUtama)
        navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home -> {
                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutUtama, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.add -> {
                    val fragment = AddFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutUtama, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    val fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutUtama, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@setOnItemSelectedListener true
                }

             }
        }



    }
}