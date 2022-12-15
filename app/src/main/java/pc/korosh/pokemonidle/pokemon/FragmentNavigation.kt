package pc.korosh.pokemonidle.pokemon

import androidx.fragment.app.Fragment

// Interface to allow replaceFragment to only be implemented in MainActivity
interface FragmentNavigation {
    fun replaceFragment(frag: Fragment)
}