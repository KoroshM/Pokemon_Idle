package pc.korosh.pokemonidle.pokemon

open class Move(
        val id: Int,            // Move ID #
        val name: String,       // Move name
        val type: String,       // Elemental type
        val phys: Boolean,      // Physical/Special
        val damage: Int,        // Base damage
        val energy: Float,      // Energy drain/charge
        val time: Float,        // Move duration
        val fast: Boolean       // Fast move or not
    ) {
}