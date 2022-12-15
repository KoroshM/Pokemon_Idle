package pc.korosh.pokemonidle.pokemon

import pc.korosh.pokemonidle.pokemon.Move

open class Poke(
    val id: Double,
    val name: String,
    val desc: String,
    val moveTable: Map<Int, Move>,  // Int = level, <0 = special (TM, event, etc.)
    val type1: String,
    val type2: String?,
    val stats: List<Int>,
    val evolve: List<Pair<Int, String>>?
    ) {

}

/* Stats
 * 0 HP
 * 1 Atk
 * 2 Def
 * 3 SpAtk
 * 4 SpDef
 * 5 Spd
 */