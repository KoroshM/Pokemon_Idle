package pc.korosh.pokemonidle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import pc.korosh.pokemonidle.databinding.ActivityMainBinding
import pc.korosh.pokemonidle.pokemon.Backend
import pc.korosh.pokemonidle.pokemon.FragmentNavigation
import pc.korosh.pokemonidle.pokemon.Player
import pc.korosh.pokemonidle.tabs.pokecenter.PokecenterFragment
import pc.korosh.pokemonidle.tabs.starter.StarterFragment

// TODO: Eevee, Tyrogue, Trade, Friendship Evos

class MainActivity : AppCompatActivity(), FragmentNavigation {
    private lateinit var binding: ActivityMainBinding           // Easily access UI elements
    private var fragment: Fragment? = null                      // Currently loaded fragment
    private val player = Player(mutableListOf())
    private val bk = Backend()

    // Main method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)                      // Required setup
        binding = ActivityMainBinding.inflate(layoutInflater)   // Required for binding
        setContentView(binding.root)

        if(bk.initialize(player, this.applicationContext)) {
            replaceFragment(StarterFragment(player))
        }

        binding.fab.setOnClickListener {
            binding.fab.visibility = View.GONE
            replaceFragment(PokecenterFragment(player))
        }
    }

    // Used by every fragment for fragment navigation
    override fun replaceFragment(frag: Fragment) {
        val fragManager = supportFragmentManager
        fragManager.beginTransaction()
            .replace(R.id.fragcontainer_main, frag)
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }
}