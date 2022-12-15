package pc.korosh.pokemonidle.pokemon

import pc.korosh.pokemonidle.pokemon.Move
import pc.korosh.pokemonidle.pokemon.Poke

class Pokemon(
    id: Double, name: String, desc: String, moveTable: Map<Int, Move>, type1: String, type2: String?,
    stats: List<Int>, evolve: List<Pair<Int, String>>?,
    var nickname: String?,
    var curHP: Int,
    var curLvl: Int,
    var curXP: Int,
    var fast: Move,
    var charged: Move,
    val shiny: Boolean,
    var status: Int,
    //val boosts: List<Int>,   // TODO: Temporary stat boosts
    //val item: Item?,         // TODO: Held items
    //val nature: idk yet,     // TODO: Natures
    val ivs: List<Int>

): Poke(id, name, desc, moveTable, type1, type2, stats, evolve) {

    constructor(base: Poke,
                nickname: String?,
                curHP: Int,
                curLvl: Int,
                curXP: Int,
                fast: Move,
                charged: Move,
                shiny: Boolean,
                status: Int,
                //boosts: List<Int>,
                //item: Item,
                ivs: List<Int>
                ) : this(
                    base.id,
                    base.name,
                    base.desc,
                    base.moveTable,
                    base.type1,
                    base.type2,
                    base.stats,
                    base.evolve,
                    nickname,
                    curHP,
                    curLvl,
                    curXP,
                    fast,
                    charged,
                    shiny,
                    status,
                    //boosts,
                    //item,
                    ivs
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
 * 0 SLP Sleepy
 * 1 PLZ Paralyzed
 * 2 FRZ Frozen
 * 3 BRN Burned
 * 4 PSN Poisoned
 * 5 CNF Confused
 */