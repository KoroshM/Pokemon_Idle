package pc.korosh.pokemonidle.pokemon

import android.content.Context
import org.apache.commons.lang3.StringUtils.upperCase
import java.io.*
import java.util.*

class Backend {
    fun initialize(model: Player, context: Context): Boolean {
        // Load game data
        loadAttDex(context)
        loadDex(context)

        // TODO: Load user data

        // Give a starter to new accounts
        return model.party.isEmpty()
    }

    fun pokreate(poke: String, lvl: Int): Pokemon {
        val r = Random()
        val ivs = listOf(r.nextInt(31),
            r.nextInt(31),
            r.nextInt(31),
            r.nextInt(31),
            r.nextInt(31),
            r.nextInt(31))
        return pokreate(poke, lvl, ivs)
    }
    fun pokreate(poke: String, lvl: Int, ivs: List<Int>): Pokemon {
        val r = Random()
        val mon = Const.DEX[poke]!!
        val moves = mon.moveTable
        var fast = moves[0]!!
        var charged = moves[1]!!

        moves.forEach { (key, value) ->
            if(key <= lvl) {
                if(r.nextBoolean())
                    if(value.fast)
                        fast = value
                    else
                        charged = value
            }
        }

        return Pokemon(mon,
            null,
            BattleCalc().calcStat(Const.DEX[poke]!!.stats[0], ivs[0], lvl, 0),
            lvl,
            0,
            fast,
            charged,
            false,
            0,
            ivs
            )
    }

    fun heal(poke: Pokemon): Int {
        return BattleCalc().calcStat(poke, 0)
    }

    fun healParty(party: List<Pokemon>): List<Pokemon> {
        val calc = BattleCalc()

        for(poke in party) {
            poke.curHP = calc.calcStat(poke, 0)
        }
        return party
    }

    private fun loadAttDex(context: Context) {
        // Load Move Data
        var dex = mutableMapOf<String, Move>()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(
                InputStreamReader(context.assets.open("attacks.txt"), "UTF-8")
            )

            // Read file
            var line = reader.readLine()
            val debug = line.split(",").toTypedArray()
            line = reader.readLine()
            while (line  != null) {
                val vals = line.split(",").toTypedArray()

                val name = vals[1]
                dex[name] = Move(
                    vals[0].toInt(),        // Move ID
                    name,                   // Move name
                    upperCase(vals[2]),     // Elemental type
                    vals[3] == "1",   // Physical or not
                    vals[4].toInt(),        // Base damage
                    vals[5].toFloat(),      // Energy gain/cost
                    vals[6].toFloat(),      // Cooldown
                    vals[7] == "1"     // Fast move or not
                )

                line = reader.readLine()
            }
            Const.ATKDEX = dex
        } catch (e: IOException) {
            println("================== FAILED TO LOAD CSV ==================")
            println(e.printStackTrace())
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (e: IOException) {
                    println("================== FAILED TO CLOSE CSV ==================")
                    println(e.printStackTrace())
                }
            }
        }
    }

    private fun loadDex(context: Context) {
        // Load Dex Data
        var dex = mutableMapOf<String, Poke>()
        var reader: BufferedReader? = null
        var learnset: BufferedReader? = null
        try {
            reader = BufferedReader(
                InputStreamReader(context.assets.open("pokemon.txt"), "UTF-8")
            )
            learnset = BufferedReader(
                InputStreamReader(context.assets.open("learnset.txt"), "UTF-8")
            )

            // do reading, usually loop until end of file reading
            var line = reader.readLine()
            var lLine = learnset.readLine()
            val debug1 = line.split(",").toTypedArray()
            val debug2 = lLine.split(",").toTypedArray()

            line = reader.readLine()
            lLine = learnset.readLine()
            while (line  != null) {
                val vals = line.split(",").toTypedArray()
                val moves = LinkedList<String>(lLine.split(","))

                val moveTable = mutableMapOf<Int, Move>()  // Int = level, <0 = special (TM, event, etc.)
                var mLvl = moves.pop()  // Dex number
                var mName = moves.pop() // Pokemon name
                mLvl = moves.pop()  // Learn level
                mName = moves.pop() // Move name
                while(mLvl != "") {
                    try {
                        val aMove = Const.ATKDEX[mName]!!
                        moveTable[mLvl.toInt()] = aMove

                        mLvl = moves.pop()  // Learn level
                        mName = moves.pop() // Move name
                    }
                    catch(e: NoSuchElementException) {
                        break
                    }
                }

                val id = vals[0].toDouble()
                val name = vals[2]
                val desc = vals[3]
                val type1 = upperCase(vals[4])
                val type2 = if(vals[5] == "") null
                            else upperCase(vals[5])
                val stats = listOf<Int>(
                    vals[13].toInt(),
                    vals[14].toInt(),
                    vals[15].toInt(),
                    vals[16].toInt(),
                    vals[17].toInt(),
                    vals[18].toInt()
                )
                val evolve = mutableListOf<Pair<Int, String>>()
                if(vals[27] != "")
                    evolve.add(Pair(vals[26].toInt(),vals[27]))
                if(vals[29] != "")
                    evolve.add(Pair(vals[28].toInt(),vals[29]))
                if(vals[31] != "")
                    evolve.add(Pair(vals[30].toInt(),vals[31]))

                dex[vals[2]] = Poke(id, name, desc, moveTable, type1, type2, stats, evolve.toList())

                line = reader.readLine()
                lLine = learnset.readLine()
            }
            Const.DEX = dex
        } catch (e: IOException) {
            println("================== FAILED TO LOAD CSV ==================")
            println(e.printStackTrace())
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (e: IOException) {
                    println("================== FAILED TO CLOSE CSV ==================")
                    println(e.printStackTrace())
                }
            }
        }
    }
}

object Const {
    var ATKDEX = mapOf<String, Move>()
    var DEX = mapOf<String, Poke>()
    val RATE = 4    // Multiplier for move cooldowns

    val TYPES = mapOf<String, Int>(
        "NORMAL" to 0,
        "FIRE" to 1,
        "WATER" to 2,
        "ELECTRIC" to 3,
        "GRASS" to 4,
        "ICE" to 5,
        "FIGHTING" to 6,
        "POISON" to 7,
        "GROUND" to 8,
        "FLYING" to 9,
        "PSYCHIC" to 10,
        "BUG" to 11,
        "ROCK" to 12,
        "GHOST" to 13,
        "DRAGON" to 14,
        "DARK" to 15,
        "STEEL" to 16,
        "FAIRY" to 17
    )

    val EVO = mapOf<Int, String>(
        -1 to "Fire Stone",
        -2 to "Water Stone",
        -3 to "Leaf Stone",
        -4 to "Thunder Stone",
        -5 to "Moon Stone",
        -6 to "Sun Stone",
        -7 to "Friendship",
        -8 to "Trade",
        -9 to "Metal Coat",
        -10 to "Electirizer",
        -11 to "Magmatizer",
        -12 to "Oval stone",
        -13 to "King's Rock",
        -14 to "Protector",
        -15 to "Dragon Scale",
        -16 to "Upgrade",
        -17 to "Dubious Disc"
    )
}

/* Stats
 * 0 HP
 * 1 Atk
 * 2 Def
 * 3 SpAtk
 * 4 SpDef
 * 5 Spd
 */

/* Status
 * 1 SLP Sleepy
 * 2 PLZ Paralyzed
 * 3 FRZ Frozen
 * 4 BRN Burned
 * 5 PSN Poisoned
 * 6 CNF Confused
 */

/* Evo Enum (negative):
1 - Fire stone
2 - Water stone
3 - Leaf stone
4 - Thunder stone
5 - Moon stone
6 - Sun stone
7 - Friendship
8 - Trade
9 - Metal Coat
10 - Electirizer
11 - Magmatizer
12 - Oval stone
13 - King's Rock
14 - Protector
15 - Dragon Scale
16 - Upgrade
17 - Dubious Disc
 */