package pc.korosh.pokemonidle.tabs.pokecenter

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pc.korosh.pokemonidle.R
import pc.korosh.pokemonidle.databinding.PokecenterLayoutBinding
import pc.korosh.pokemonidle.pokemon.BattleCalc
import pc.korosh.pokemonidle.pokemon.FragmentNavigation
import pc.korosh.pokemonidle.pokemon.Player
import pc.korosh.pokemonidle.tabs.battle.BattleFragment

class PokecenterFragment(private val player: Player) : Fragment() {
    private var _binding: PokecenterLayoutBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokecenterLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val healButton = binding.buttonHeal
        val fab = activity?.findViewById<FloatingActionButton>(R.id.fab)
        fab?.setImageDrawable(resources.getDrawable(R.drawable.ic_blam, context?.theme))

        drawParty(player)

        healButton.setOnClickListener {
            val calc = BattleCalc()
            for(pokemon in player.party)
                pokemon.curHP = calc.calcStat(pokemon, 0)

            healButton.isEnabled = false
            Toast.makeText(context, "Your Pokemon have been healed!", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({healButton.isEnabled = true}, 2500)
        }

        fab?.visibility = View.VISIBLE
        fab?.setOnClickListener {
            fab.setImageDrawable(resources.getDrawable(R.drawable.ic_heal, context?.theme))
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(BattleFragment(player))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun drawParty(player: Player) {
        val party = player.party
        val calc = BattleCalc()
        if(party.size > 0) {
            val poke = party[0]
            val curStats = listOf(
                calc.calcStat(poke, 0),
                calc.calcStat(poke, 1),
                calc.calcStat(poke, 2),
                calc.calcStat(poke, 3),
                calc.calcStat(poke, 4),
                calc.calcStat(poke, 5)
            )
            binding.move1TxtName1.text = poke.fast.name
            binding.move1TxtType1.text = "\t\t\t${poke.fast.type}"
            binding.move2TxtName1.text = poke.charged.name
            binding.move2TxtType1.text = "\t\t\t${poke.charged.type}"

            if(poke.nickname != null)
                binding.nameTxt1.text = "${poke.nickname}, ${poke.curLvl}"
            else
                binding.nameTxt1.text = "${poke.name}, ${poke.curLvl}"
            binding.hpTxt1.text = "${poke.curHP} / ${curStats[0]} HP"
            binding.xpTxt1.text = "${poke.curXP} / ${calc.calcMaxXp(poke.curLvl)}"

            binding.atkTxt1.text = "Attack: ${curStats[1]}"
            binding.defTxt1.text = "Defence: ${curStats[2]}"
            binding.spaTxt1.text = "Sp.Attack: ${curStats[3]}"
            binding.spdTxt1.text = "Sp.Defence: ${curStats[4]}"
            binding.speedTxt1.text = "Speed: ${curStats[5]}"

            var url = player.buildURL(poke, false)
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.pcenterImg1)
        }
        if(party.size > 1) {
            val poke = party[1]
            val curStats = listOf(
                calc.calcStat(poke, 0),
                calc.calcStat(poke, 1),
                calc.calcStat(poke, 2),
                calc.calcStat(poke, 3),
                calc.calcStat(poke, 4),
                calc.calcStat(poke, 5)
            )
            binding.move1TxtName2.text = poke.fast.name
            binding.move1TxtType2.text = "\t\t\t${poke.fast.type}"
            binding.move2TxtName2.text = poke.charged.name
            binding.move2TxtType2.text = "\t\t\t${poke.charged.type}"

            if(poke.nickname != null)
                binding.nameTxt2.text = "${poke.nickname}, ${poke.curLvl}"
            else
                binding.nameTxt2.text = "${poke.name}, ${poke.curLvl}"
            binding.hpTxt2.text = "${poke.curHP} / ${curStats[0]} HP"
            binding.xpTxt2.text = "${poke.curXP} / ${calc.calcMaxXp(poke.curLvl)}"

            binding.atkTxt2.text = "Attack: ${curStats[1]}"
            binding.defTxt2.text = "Defence: ${curStats[2]}"
            binding.spaTxt2.text = "Sp.Attack: ${curStats[3]}"
            binding.spdTxt2.text = "Sp.Defence: ${curStats[4]}"
            binding.speedTxt2.text = "Speed: ${curStats[5]}"

            var url = player.buildURL(poke, false)
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.pcenterImg2)
        }
        if(party.size > 2) {
            val poke = party[2]
            val curStats = listOf(
                calc.calcStat(poke, 0),
                calc.calcStat(poke, 1),
                calc.calcStat(poke, 2),
                calc.calcStat(poke, 3),
                calc.calcStat(poke, 4),
                calc.calcStat(poke, 5)
            )
            binding.move1TxtName3.text = poke.fast.name
            binding.move1TxtType3.text = "\t\t\t${poke.fast.type}"
            binding.move2TxtName3.text = poke.charged.name
            binding.move2TxtType3.text = "\t\t\t${poke.charged.type}"

            if(poke.nickname != null)
                binding.nameTxt3.text = "${poke.nickname}, ${poke.curLvl}"
            else
                binding.nameTxt3.text = "${poke.name}, ${poke.curLvl}"
            binding.hpTxt3.text = "${poke.curHP} / ${curStats[0]} HP"
            binding.xpTxt3.text = "${poke.curXP} / ${calc.calcMaxXp(poke.curLvl)}"

            binding.atkTxt3.text = "Attack: ${curStats[1]}"
            binding.defTxt3.text = "Defence: ${curStats[2]}"
            binding.spaTxt3.text = "Sp.Attack: ${curStats[3]}"
            binding.spdTxt3.text = "Sp.Defence: ${curStats[4]}"
            binding.speedTxt3.text = "Speed: ${curStats[5]}"

            var url = player.buildURL(poke, false)
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.pcenterImg3)
        }
        if(party.size > 3) {
            val poke = party[3]
            val curStats = listOf(
                calc.calcStat(poke, 0),
                calc.calcStat(poke, 1),
                calc.calcStat(poke, 2),
                calc.calcStat(poke, 3),
                calc.calcStat(poke, 4),
                calc.calcStat(poke, 5)
            )
            binding.move1TxtName4.text = poke.fast.name
            binding.move1TxtType4.text = "\t\t\t${poke.fast.type}"
            binding.move2TxtName4.text = poke.charged.name
            binding.move2TxtType4.text = "\t\t\t${poke.charged.type}"

            if(poke.nickname != null)
                binding.nameTxt4.text = "${poke.nickname}, ${poke.curLvl}"
            else
                binding.nameTxt4.text = "${poke.name}, ${poke.curLvl}"
            binding.hpTxt4.text = "${poke.curHP} / ${curStats[0]} HP"
            binding.xpTxt4.text = "${poke.curXP} / ${calc.calcMaxXp(poke.curLvl)}"

            binding.atkTxt4.text = "Attack: ${curStats[1]}"
            binding.defTxt4.text = "Defence: ${curStats[2]}"
            binding.spaTxt4.text = "Sp.Attack: ${curStats[3]}"
            binding.spdTxt4.text = "Sp.Defence: ${curStats[4]}"
            binding.speedTxt4.text = "Speed: ${curStats[5]}"

            var url = player.buildURL(poke, false)
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.pcenterImg4)
        }
        if(party.size > 4) {
            val poke = party[4]
            val curStats = listOf(
                calc.calcStat(poke, 0),
                calc.calcStat(poke, 1),
                calc.calcStat(poke, 2),
                calc.calcStat(poke, 3),
                calc.calcStat(poke, 4),
                calc.calcStat(poke, 5)
            )
            binding.move1TxtName4.text = poke.fast.name
            binding.move1TxtType4.text = "\t\t\t${poke.fast.type}"
            binding.move2TxtName4.text = poke.charged.name
            binding.move2TxtType4.text = "\t\t\t${poke.charged.type}"

            if(poke.nickname != null)
                binding.nameTxt4.text = "${poke.nickname}, ${poke.curLvl}"
            else
                binding.nameTxt4.text = "${poke.name}, ${poke.curLvl}"
            binding.hpTxt4.text = "${poke.curHP} / ${curStats[0]} HP"
            binding.xpTxt4.text = "${poke.curXP} / ${calc.calcMaxXp(poke.curLvl)}"

            binding.atkTxt4.text = "Attack: ${curStats[1]}"
            binding.defTxt4.text = "Defence: ${curStats[2]}"
            binding.spaTxt4.text = "Sp.Attack: ${curStats[3]}"
            binding.spdTxt4.text = "Sp.Defence: ${curStats[4]}"
            binding.speedTxt4.text = "Speed: ${curStats[5]}"

            var url = player.buildURL(poke, false)
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.pcenterImg4)
        }
        if(party.size > 5) {
            val poke = party[5]
            val curStats = listOf(
                calc.calcStat(poke, 0),
                calc.calcStat(poke, 1),
                calc.calcStat(poke, 2),
                calc.calcStat(poke, 3),
                calc.calcStat(poke, 4),
                calc.calcStat(poke, 5)
            )
            binding.move1TxtName5.text = poke.fast.name
            binding.move1TxtType5.text = "\t\t\t${poke.fast.type}"
            binding.move2TxtName5.text = poke.charged.name
            binding.move2TxtType5.text = "\t\t\t${poke.charged.type}"

            if(poke.nickname != null)
                binding.nameTxt5.text = "${poke.nickname}, ${poke.curLvl}"
            else
                binding.nameTxt5.text = "${poke.name}, ${poke.curLvl}"
            binding.hpTxt5.text = "${poke.curHP} / ${curStats[0]} HP"
            binding.xpTxt5.text = "${poke.curXP} / ${calc.calcMaxXp(poke.curLvl)}"

            binding.atkTxt5.text = "Attack: ${curStats[1]}"
            binding.defTxt5.text = "Defence: ${curStats[2]}"
            binding.spaTxt5.text = "Sp.Attack: ${curStats[3]}"
            binding.spdTxt5.text = "Sp.Defence: ${curStats[4]}"
            binding.speedTxt5.text = "Speed: ${curStats[5]}"

            var url = player.buildURL(poke, false)
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.pcenterImg5)
        }
    }
}