package pc.korosh.pokemonidle.pokemon

import androidx.lifecycle.ViewModel

// Class used to store data from fragment to fragment
class Player(
    val party: MutableList<Pokemon>
    ) : ViewModel() {
    // https://img.pokemondb.net/sprites/black-white/anim/back-shiny/bulbasaur.gif
    private val baseUrl = "https://img.pokemondb.net/sprites/black-white/anim/"
    private var balls = 0
    private var money = 0

    fun buildURL(poke: Pokemon, backView: Boolean): String {
        var url = baseUrl

        if(backView)
            url += "back-"

        url += if(poke.shiny)
            "shiny/"
        else
            "normal/"

        return url + poke.name.lowercase() + ".gif"
    }

    fun getBalls() = balls

    fun addBalls(amt: Int) {
        balls += amt
    }

    fun removeBalls(amt: Int) {
        balls -= amt
        if(balls < 0)
            balls = 0
    }

    fun getMoney() = money

    fun addMoney(amt: Int) {
        money += amt
    }

    fun spendMoney(amt: Int) {
        money -= amt
        if(money < 0)
            money = 0
    }
}