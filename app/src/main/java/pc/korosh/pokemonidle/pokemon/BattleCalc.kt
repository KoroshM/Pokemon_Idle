package pc.korosh.pokemonidle.pokemon

import kotlin.random.Random

class BattleCalc() {
    private val typeChart = arrayOf(
        floatArrayOf(  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F, .5F,  0F,  1F,  1F, .5F,  1F),// Normal
        floatArrayOf(  1F, .5F, .5F,  1F,  2F,  2F,  1F,  1F,  1F,  1F,  1F,  2F, .5F,  1F, .5F,  1F,  2F,  1F),// Fire
        floatArrayOf(  1F,  2F, .5F,  1F, .5F,  1F,  1F,  1F,  2F,  1F,  1F,  1F,  2F,  1F, .5F,  1F,  1F,  1F),// Water
        floatArrayOf(  1F,  1F,  2F, .5F, .5F,  1F,  1F,  1F,  0F,  2F,  1F,  1F,  1F,  1F, .5F,  1F,  1F,  1F),// Electric
        floatArrayOf(  1F, .5F,  2F,  1F, .5F,  1F,  1F, .5F,  2F, .5F,  1F, .5F,  2F,  1F, .5F,  1F, .5F,  1F),// Grass
        floatArrayOf(  1F, .5F, .5F,  1F,  2F, .5F,  1F,  1F,  2F,  2F,  1F,  1F,  1F,  1F,  2F,  1F, .5F,  1F),// Ice
        floatArrayOf(  2F,  1F,  1F,  1F,  1F,  2F,  1F, .5F,  1F, .5F, .5F, .5F,  2F,  0F,  1F,  2F,  2F, .5F),// Fighting
        floatArrayOf(  1F,  1F,  1F,  1F,  2F,  1F,  1F, .5F, .5F,  1F,  1F,  1F, .5F, .5F,  1F,  1F,  0F,  2F),// Poison
        floatArrayOf(  1F,  2F,  1F,  2F, .5F,  1F,  1F,  2F,  1F,  0F,  1F, .5F,  2F,  1F,  1F,  1F,  2F,  1F),// Ground
        floatArrayOf(  1F,  1F,  1F, .5F,  2F,  1F,  2F,  1F,  1F,  1F,  1F,  2F, .5F,  1F,  1F,  1F, .5F,  1F),// Flying
        floatArrayOf(  1F,  1F,  1F,  1F,  1F,  1F,  2F,  2F,  1F,  1F, .5F,  1F,  1F,  1F,  1F,  0F, .5F,  1F),// Psychic
        floatArrayOf(  1F, .5F,  1F,  1F,  2F,  1F, .5F, .5F,  1F, .5F,  2F,  1F,  1F, .5F,  1F,  2F, .5F, .5F),// Bug
        floatArrayOf(  1F,  2F,  1F,  1F,  1F,  2F, .5F,  1F, .5F,  2F,  1F,  2F,  1F,  1F,  1F,  1F, .5F,  1F),// Rock
        floatArrayOf(  0F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  2F,  1F,  1F,  2F,  1F, .5F,  1F,  1F),// Ghost
        floatArrayOf(  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  1F,  2F,  1F, .5F,  0F),// Dragon
        floatArrayOf(  1F,  1F,  1F,  1F,  1F,  1F, .5F,  1F,  1F,  1F,  2F,  1F,  1F,  2F,  1F, .5F,  1F, .5F),// Dark
        floatArrayOf(  1F, .5F, .5F, .5F,  1F,  2F,  1F,  1F,  1F,  1F,  1F,  1F,  2F,  1F,  1F,  1F, .5F,  2F),// Steel
        floatArrayOf(  1F, .5F,  1F,  1F,  1F,  1F,  2F, .5F,  1F,  1F,  1F,  1F,  1F,  1F,  2F,  2F, .5F,  1F) // Fairy
    )

    fun calcMove(attack: Pokemon, move: Move, defend: Pokemon): Move {
        val moveDamage = calcDamage(attack, move, defend)
        val moveTime = calcTime(attack, move)
        val moveEnergy = calcEnergy(attack, move)

        return Move(0, move.name, move.type, move.phys, moveDamage, moveTime, moveEnergy, move.fast)
    }

    fun calcStat(base: Int, iv: Int, lvl: Int, stat: Int): Int {
        return if(stat == 0) {
            (kotlin.math.floor(0.01 * (2 * base + iv) * lvl) + lvl + 10).toInt()
        } else {
            (kotlin.math.floor(0.01 * (2 * base + iv) * lvl) + 5).toInt()
        }
    }

    fun calcStat(poke: Pokemon, stat: Int): Int {
        return calcStat(poke.stats[stat], poke.ivs[stat], poke.curLvl, stat)
    }

    fun calcMaxXp(curLvl: Int): Int {
        return Math.ceil(Math.pow(curLvl.toDouble(),3.0) * 5/4).toInt() + 1
    }

    private fun calcTime(attack: Pokemon, move: Move): Float {
        val status = attack.status
        var time = move.time
        // Scale attack speed with speed stat
        val debug = (calcStat(attack, 5) / 100)
        time /= (calcStat(attack, 5) / 100)

        return when(status) {
            // Burn
            2 -> 2 * time
            // Confused
            5 -> Random.nextDouble(1.25, 1.75).toFloat() * time
            // Other
            else -> time
        }
    }

    private fun calcEnergy(attack: Pokemon, move: Move): Float {
        var energy = move.energy
        if(!move.fast)
            energy *= -1
        val status = attack.status
        return when(status) {
            // Sleep
            0 -> {
                if(Random.nextFloat() > 0.5F)
                    0F
                else
                    energy
            }
            // Paralyzed
            1 -> 0F
            // Confused
            5 -> 0F
            else -> energy
        }
    }

    private fun calcDamage(attack: Pokemon, move: Move, defend: Pokemon): Int {
        val status = attack.status
        when(status) {
            // Sleep
            0 -> {
                if (Random.nextFloat() > 0.5F)
                    return 0
            }
            // Paralyzed
            1 -> return 0
            // Confused
            5 -> {
                if (move == attack.charged)
                    return 0
            }
        }
        val effAtk = statScaler(attack, move, defend)
        val baseScaler = ((2 * attack.curLvl)/5 + 2) * move.damage
        val scaledDmg = (baseScaler * effAtk)/50 + 2
        val multiplier = calcMult(attack, move, defend)

        return (scaledDmg * multiplier).toInt()
    }

    private fun statScaler(attack: Pokemon, move: Move, defend: Pokemon): Float {
        // TODO: Stat boosts
//        // Calculate effective stats with boosts
//        if(attack.boosts.count { it == 0 } != 6 || defend.boosts.count() { it == 0} != 6) {
//
//        }

        val offensiveStat: Int
        val defensiveStat: Int
        if(move.phys) {    // Physical
            offensiveStat = calcStat(attack, 1)
            defensiveStat = calcStat(defend, 2)
        }
        else {
            offensiveStat = calcStat(attack, 3)
            defensiveStat = calcStat(defend, 4)
        }

        return offensiveStat.toFloat()/defensiveStat.toFloat()
    }

    private fun calcMult(attack: Pokemon, move: Move, defend: Pokemon): Float {
        // STAB, typing, status, range
        var multiplier = 1F

        // STAB
        if(move.type == attack.type1 || move.type == attack.type2)
            multiplier *= 1.5F
        // Defensive typing
        multiplier *= getTypeMult(move.type, defend)
        // Status
        multiplier *= getStatusMult(attack, defend)
        // Range
        multiplier *= Random.nextDouble(0.85, 1.0).toFloat()

        return multiplier
    }

    private fun getTypeMult(type: String, poke: Pokemon): Float {
        var mult = 1F

        mult *= typeChart[Const.TYPES[type]!!][Const.TYPES[poke.type1]!!]
        if(poke.type2 != null) {
            mult *= typeChart[Const.TYPES[type]!!][Const.TYPES[poke.type2]!!]
        }
        return mult
    }

    private fun getStatusMult(attack: Pokemon, defend: Pokemon): Float {
        if(attack.status == 3 && defend.status == 3) return 1F
        if(attack.status == 3) return 0.5F
        return if(defend.status == 3) 1.5F
        else 1F
    }

}

/* Status
 * 0 SLP Sleepy
 * 1 PLZ Paralyzed
 * 2 FRZ Frozen
 * 3 BRN Burned
 * 4 PSN Poisoned
 * 5 CNF Confused
 */