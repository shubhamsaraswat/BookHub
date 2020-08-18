package com.shubham.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.shubham.bookhub.R
import com.shubham.bookhub.fragment.Dashboard
import com.shubham.bookhub.fragment.aboutFragment
import com.shubham.bookhub.fragment.favoriteFragment
import com.shubham.bookhub.fragment.profileFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerlayout:DrawerLayout
    lateinit var coordinatorlayout: CoordinatorLayout
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var  frame : FrameLayout
    lateinit var navigationview : NavigationView
    var previousmenuitem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openDashboard()

        drawerlayout = findViewById(R.id.drawerlayout)
        coordinatorlayout =findViewById(R.id.coordinatorlayout)
        toolbar=findViewById(R.id.toolbar)
        frame=findViewById(R.id.frame)
        navigationview=findViewById(R.id.navigationview)
        setUpToolbar()

        val actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity,drawerlayout,
            R.string.open_Drawer,
            R.string.close_drawer
        )
        drawerlayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationview.setNavigationItemSelectedListener{
            if(previousmenuitem!=null){
                previousmenuitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it
            when(it.itemId) {
                R.id.Dashboard ->{
                    Toast.makeText(this@MainActivity,"Clicked on Dashboard",Toast.LENGTH_SHORT).show()
                    openDashboard()
                    drawerlayout.closeDrawers()
                }
                R.id.Favourite ->{
                    Toast.makeText(this@MainActivity,"Clicked on Favorite",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            favoriteFragment()
                        )
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerlayout.closeDrawers()
                }
                R.id.Profile ->{
                    Toast.makeText(this@MainActivity,"Clicked on Profile",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            profileFragment()
                        )
                        .commit()
                    supportActionBar?.title="Profile"

                    drawerlayout.closeDrawers()
                }
                R.id.aboutapp ->{
                    Toast.makeText(this@MainActivity,"Clicked on About app",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            aboutFragment()
                        )
                        .commit()
                    supportActionBar?.title="About App"

                    drawerlayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Hub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerlayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, favoriteFragment())
            .commit()
        supportActionBar?.title="Favourites"
        navigationview.setCheckedItem(R.id.Dashboard)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is Dashboard -> openDashboard()
            else -> super.onBackPressed()
        }

    }
}