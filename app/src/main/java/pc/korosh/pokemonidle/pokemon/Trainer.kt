package pc.korosh.pokemonidle.pokemon

import pc.korosh.pokemonidle.pokemon.Backend
import pc.korosh.pokemonidle.pokemon.Pokemon

class Trainer(val name: String?,
              val greet: String,
              val end: String,
              private var party: List<Pokemon>
    ) {

    constructor(wild: Pokemon) : this(null,  "", "", listOf(wild)) {}
    constructor(party: List<Pokemon>) : this(null, "", "", party) {}

//    fun add(poke: Pokemon): Boolean {
//        return if(party.size < 6) {
//            party.add(poke)
//            true
//        } else {
//            false
//        }
//    }
//
//    fun remove(poke: Pokemon): Boolean {
//        return if(party.contains(poke)) {
//            party.remove(poke)
//            true
//        } else {
//            false
//        }
//    }

    fun getParty(): List<Pokemon> {
        return party
    }

    fun heal() {
        party = Backend().healParty(party) as MutableList<Pokemon>
    }
}