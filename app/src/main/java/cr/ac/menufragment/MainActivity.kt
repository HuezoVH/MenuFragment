package cr.ac.menufragment

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

lateinit var drawerlayout : DrawerLayout

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerlayout = findViewById(R.id.drawer_layout)

        var toggle = ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        var navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener (this)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment : Fragment
        var title : Int
        when(item.itemId){
            R.id.nav_camera -> {
                fragment = CamaraFragment.newInstance(getString(R.string.menu_camera))
                title = R.string.menu_camera
            }
            R.id.nav_gallery -> {
                fragment = GaleriaFragment.newInstance(getString(R.string.menu_galeria))
                title = R.string.menu_galeria
            }
            else -> {
                fragment = HomeFragment.newInstance(getString(R.string.homeFragment))
                title = R.string.homeFragment
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit()
        setTitle(title)
        drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }
}