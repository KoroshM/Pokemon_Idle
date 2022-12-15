package pc.korosh.pokemonidle.tabs.battle

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pc.korosh.pokemonidle.R
import pc.korosh.pokemonidle.databinding.BattleLayoutBinding
import pc.korosh.pokemonidle.pokemon.*
import pc.korosh.pokemonidle.tabs.pokecenter.PokecenterFragment
import java.lang.Thread.sleep
import java.util.*
import kotlin.properties.Delegates

class BattleFragment(private val player: Player) : Fragment() {
    private val ROUTES = Encounters()
    private val CALC = BattleCalc()

    private var isTrainer = false
    private var cont = true
    private var whiteout = false
    private var playerMove= 0
    private var time = 0L
    private val timerInterval = 100L       // Timer update in ms
    private var mHandler: Handler? = null   // Manages the timer

    private lateinit var ppoke: Pokemon
    private lateinit var opoke: Pokemon
    private var pcd by Delegates.notNull<Float>()
    private var pcdMax = 0F
    private var penergy = 0F
    private var ocd by Delegates.notNull<Float>()
    private var ocdMax = 0F
    private var oenergy = 0F

    private lateinit var pHpText: TextView
    private lateinit var pXpText: TextView
    private lateinit var pCdProg: ProgressBar
    private lateinit var pChProg: ProgressBar

    private lateinit var oHpText: TextView
    private lateinit var oXpText: TextView
    private lateinit var oCdProg: ProgressBar
    private lateinit var oChProg: ProgressBar

    private var _binding: BattleLayoutBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BattleLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnStart.visibility = View.GONE
        initialize()

        // TODO: On Route Selection
        val route = "Route 1"
        binding.btnStart.visibility = View.VISIBLE

        binding.btnStart.setOnClickListener {
            binding.btnStart.visibility = View.GONE
            while (!whiteout && cont) {
                explore(route)
                checkWhiteout()
            }

            if (whiteout) {
                player.removeBalls(player.getBalls() / 2)
                player.spendMoney(player.getMoney() / 3)
            }
            val navLogin = activity as FragmentNavigation
            navLogin.replaceFragment(PokecenterFragment(player))
        }

        view?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            cont = false
        }

        return root
    }

    private fun initialize() {
        pHpText = binding.textviewPlayerPokeHp
        pXpText = binding.textviewPlayerPokeXp
        pCdProg = binding.progbPlayerPokeCd
        pChProg = binding.progbPlayerPokeEnergy
        oHpText = binding.textviewOtherPokeHp
        oXpText = binding.textviewOtherPokeXp
        oCdProg = binding.progbOtherPokeCd
        oChProg = binding.progbOtherPokeEnergy
    }

    // Timer method as an object
    private var mTimerHandler: Runnable = object : Runnable {
        override fun run() {
            try {
                runBattle()
            } finally {
                mHandler!!.postDelayed(this, timerInterval)
            }
        }
    }

    private fun explore(route: String?) {
        val start = System.currentTimeMillis()
        if(route == null)
        // TODO: Idle/No battle screen
            return

        // TODO: Timer task explore
        while(rollExplore(route) )
            Handler(Looper.getMainLooper()).postDelayed({}, 100)// explorePbar.progress = (System.currentTimeMillis() - start) % 100}
    }

    private fun rollExplore(route:String): Boolean {
        val r = Random()
        if(r.nextInt(25) == 1) { // 1/25 chance to find something
            if (r.nextInt(10) == 1) {  // 1/10 chance for trainer battle
                isTrainer = true
                startBattle(Trainer(player.party), ROUTES.getRouteNpc(route))
            }
            else {                          // 9/10 chance for wild pokemon
                startBattle(Trainer(player.party), ROUTES.getRouteWild(route))
            }
            isTrainer = false
            return true
        }
        return false
    }

    private fun startBattle(player: Trainer, other: Trainer) {
        val oparty: Queue<Pokemon> = LinkedList(other.getParty())
        val pparty: Queue<Pokemon> = LinkedList(player.getParty())

        // Keep battling until loss
        while(!oparty.isEmpty() && !pparty.isEmpty())
        {
            time = System.currentTimeMillis()
            // Send out first Pokemon
            opoke = oparty.peek() as Pokemon
            ppoke = pparty.peek() as Pokemon

            pcd = ppoke.fast.time
            ocd = opoke.fast.time

            drawPoke(opoke, false)
            drawPoke(ppoke, true)

            battle()

            if(opoke.curHP <= 0) {
                oparty.remove()
                oenergy = 0F
            }
            if(ppoke.curHP <= 0) {
                pparty.remove()
                penergy = 0F
            }
        }

        // Post-Battle
        if(pparty.isEmpty()) {
            // TODO: Whiteout
            whiteout = true
        }
        else {
            // TODO: Player wins battle
            if(isTrainer){
                this.player.addBalls(1)
            }
            else {
                this.player.addMoney(1)
            }
        }
    }

    private fun battle() {
        // TODO: Battle mechanics
        // Timer task will handle cooldowns
        if(!whiteout) {
            mHandler = Handler(Looper.getMainLooper())
            mTimerHandler.run()
        }
    }

    private fun runBattle() {
        if(!whiteout) {
            updateGame()
        }
        else {
            if(opoke.curHP <= 0) {
                // Award XP
                var xp = (opoke.stats[0]+opoke.ivs[0])/100
                if(opoke.evolve == null)
                    xp *= 3
                ppoke.curXP += xp * opoke.curLvl
            }
        }
    }

    private fun attack(offense: Pokemon, defence: Pokemon, energy: Float): Move {
        val move = if(energy >= offense.charged.energy) offense.charged
                   else offense.fast
        val effMove = CALC.calcMove(offense, move, defence)
        defence.curHP -= effMove.damage
        if(defence.curHP < 0)
            defence.curHP = 0
        if(move.fast)
            return move
        return move
    }

    private fun checkWhiteout(): Boolean {
        var curHpPool = 0
        for(pokemon in player.party) {
            curHpPool += pokemon.curHP
        }
        whiteout = curHpPool <= 0
        return whiteout
    }

    private fun drawPoke(poke: Pokemon, isPlayer: Boolean) {
        val imgView: ImageView
        val nameText: TextView

        // Get elements + build URL
        if(isPlayer) {
            imgView = binding.imgviewPlayerPoke
            nameText = binding.textviewPlayerPokeName
            binding.textviewPlayerPokeXp.text = "${poke.curXP}/${BattleCalc().calcMaxXp(poke.curLvl)} XP"
        }
        else {
            imgView = binding.imgviewOtherPoke
            nameText = binding.textviewPokeName3
        }

        var url = player.buildURL(poke, isPlayer)
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .into(imgView)

        nameText.text = if(poke.nickname != null) poke.nickname
                        else poke.name

        updateText(poke, isPlayer)
    }

    private fun updateText(poke: Pokemon, isPlayer: Boolean) {
        val hpText: TextView
        val xpText: TextView
        val cdProg: ProgressBar
        val chProg: ProgressBar
        val energy: Float
        val egyMax: Float
        val cd: Float
        val cdMax: Float

        if(isPlayer) {
            hpText = binding.textviewPlayerPokeHp
            xpText = binding.textviewPlayerPokeXp
            cdProg = binding.progbPlayerPokeCd
            chProg = binding.progbPlayerPokeEnergy
            energy = penergy
            egyMax = ppoke.charged.energy
            cd = pcd
            cdMax = pcdMax
        }
        else {
            hpText = binding.textviewOtherPokeHp
            xpText = binding.textviewOtherPokeXp
            cdProg = binding.progbOtherPokeCd
            chProg = binding.progbOtherPokeEnergy
            energy = oenergy
            egyMax = opoke.charged.energy
            cd = ocd
            cdMax = ocdMax
        }

        hpText.text = "${poke.curHP}/${BattleCalc().calcStat(poke, 0)} HP"
        xpText.text = "${poke.curXP}/${BattleCalc().calcMaxXp(poke.curLvl)}"

        cdProg.progress = (cd.toDouble()/cdMax * 100).toInt()
        chProg.progress = ((energy % egyMax).toDouble()/poke.charged.energy * 100).toInt()
    }

    private fun updateGame() {
        ocd -= timerInterval/1000
        pcd -= timerInterval/1000
        if(ocd < 0) ocd = 0F
        if(pcd < 0) pcd = 0F

        when(playerMove) {
            0 -> {
                if (ocd == 0F) {
                    val moveUsed = attack(opoke, ppoke, oenergy)
                    ocdMax = moveUsed.time
                    if(moveUsed.fast)
                        oenergy += moveUsed.energy
                    else
                        oenergy -= moveUsed.energy
                }
                if(pcd == 0F) {
                    val moveUsed = attack(ppoke, opoke, penergy)
                    pcdMax = moveUsed.time
                    if(moveUsed.fast)
                        penergy += moveUsed.energy
                    else
                        penergy -= moveUsed.energy
                }
                updateText(ppoke, true)
                updateText(opoke, false)
                checkWhiteout()
            }
            1 -> {
                if (!isTrainer && player.getBalls() > 0) {
                    // TODO: Catch
                }
                else {
                    if(isTrainer)
                        Toast.makeText(context, "You can't catch other trainers' Pokemon", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context, "You don't have any Pokeballs", Toast.LENGTH_SHORT).show()
                }
            }
            2 -> {
                val navLogin = activity as FragmentNavigation
                navLogin.replaceFragment(PokecenterFragment(player))
            }
        }
        playerMove = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}