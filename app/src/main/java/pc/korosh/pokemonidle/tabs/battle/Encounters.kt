package pc.korosh.pokemonidle.tabs.battle

import pc.korosh.pokemonidle.pokemon.Backend
import pc.korosh.pokemonidle.pokemon.Const
import pc.korosh.pokemonidle.pokemon.Trainer
import java.util.*

class Encounters {
    private val ROUTES = mapOf(
        "Route 1" to loadRoute1()
    )

    private fun loadRoute1(): Pair<RandomCollection<Entry>, List<Trainer>> {

        val wild = RandomCollection<Entry>()
        wild.add(64, Entry("Pidgey", 3, 4))
        wild.add(44, Entry("Rattata",3, 4))
        wild.add(1, Entry("Oddish",3, 4))
        wild.add(1, Entry("Bellsprout", 3, 4))

        var poke = Backend().pokreate("Rattata", 3, listOf(10, 10, 10, 10, 10, 10))
        poke.fast = Const.ATKDEX["Tackle"]!!
        val npc = listOf(Trainer("Youngster Ronny",
                "We locked eyes! That means we have to battle!",
                "I like shorts. They're comfy and easy to wear.",
                listOf(poke)))

        return Pair(wild, npc)
    }

    fun getRouteWild(route: String): Trainer {
        val enc = ROUTES[route]!!.first.next()

        return Trainer(Backend().pokreate(enc.poke, Random().nextInt(enc.high-enc.low) + enc.low))
    }

    fun getRouteNpc(route: String): Trainer {
        val peeps = ROUTES[route]!!.second

        return peeps.random()
    }

    private data class Entry(val poke: String, val low: Int, val high: Int)

    private class RandomCollection<E> {
        private val map: NavigableMap<Double, E> = TreeMap<Double, E>()
        private val r = Random()
        private var total = 0.0

        fun add(weight: Int, result: E): RandomCollection<E> {
            if (weight <= 0) return this
            total += weight
            map[total] = result
            return this
        }

        fun next(): E {
            val value: Double = r.nextDouble() * total
            return map.higherEntry(value)!!.value
        }
    }
}