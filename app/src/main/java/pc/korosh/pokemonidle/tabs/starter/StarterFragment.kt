package pc.korosh.pokemonidle.tabs.starter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import pc.korosh.pokemonidle.R
import pc.korosh.pokemonidle.databinding.PokecenterLayoutBinding
import pc.korosh.pokemonidle.databinding.StarterLayoutBinding
import pc.korosh.pokemonidle.pokemon.Backend
import pc.korosh.pokemonidle.pokemon.FragmentNavigation
import pc.korosh.pokemonidle.pokemon.Player
import pc.korosh.pokemonidle.tabs.pokecenter.PokecenterFragment

class StarterFragment(private val player: Player) : Fragment() {
    private var _binding: StarterLayoutBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StarterLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Current starters
        val starterName1 = "Bulbasaur"
        val starterName2 = "Charmander"
        val starterName3 = "Squirtle"
        val starterName4 = "Pikachu"    // Debug "else"

        // Create starters at lvl 5 with randomized IVs
        val starter1 = Backend().pokreate(starterName1, 5)
        val starter2 = Backend().pokreate(starterName2, 5)
        val starter3 = Backend().pokreate(starterName3, 5)
        val starter4 = Backend().pokreate(starterName4, 5)

        // Display starters
        binding.radiob1.text = starterName1
        Glide.with(this)
            .load(player.buildURL(starter1, false))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(binding.imgviewPoke1)

        binding.radiob2.text = starterName2
        Glide.with(this)
            .load(player.buildURL(starter2, false))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(binding.imgviewPoke2)

        binding.radiob3.text = starterName3
        Glide.with(this)
            .load(player.buildURL(starter3, false))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(binding.imgviewPoke3)

        //  Set the selected starter as the player's first party Pokemon
        binding.buttonChoose.setOnClickListener {
            // Choose a starter
            val starter = when (binding.radioOptions.checkedRadioButtonId) {
                R.id.radiob_1 -> starter1
                R.id.radiob_2 -> starter2
                R.id.radiob_3 -> starter3
                else -> starter4
            }

            player.party.add(starter)
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(PokecenterFragment(player))
        }

        return root
    }
}