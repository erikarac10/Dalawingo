package com.dalawingo.app.model

enum class CosmeticSlot { HAT, OUTFIT, ACCESSORY, ROOM }

data class Cosmetic(
    val id: String,
    val name: String,
    val emoji: String,
    val slot: CosmeticSlot,
    val price: Int,
    val xpNeeded: Int = 0,
    val goldenNeeded: Int = 0,
    val achievementOnly: Boolean = false
)

data class PetChoice(val id: String, val name: String, val emoji: String)

val pets = listOf(
    PetChoice("tarsier", "Tarsier", "🐵"),
    PetChoice("carabao", "Carabao", "🐃"),
    PetChoice("eagle", "Eagle", "🦅"),
    PetChoice("cat", "Cat", "🐱"),
    PetChoice("dog", "Dog", "🐶"),
    PetChoice("bunny", "Bunny", "🐰"),
    PetChoice("deer", "Philippine Deer", "🦌")
)

val cosmetics = listOf(
    Cosmetic("pink_bow", "Pink Bow", "🎀", CosmeticSlot.HAT, 100),
    Cosmetic("flower_crown", "Flower Crown", "🌸", CosmeticSlot.HAT, 200, xpNeeded = 100),
    Cosmetic("gold_crown", "Golden Crown", "👑", CosmeticSlot.HAT, 0, goldenNeeded = 3),
    Cosmetic("blue_hoodie", "Blue Hoodie", "🧥", CosmeticSlot.OUTFIT, 250),
    Cosmetic("barong", "Barong", "👕", CosmeticSlot.OUTFIT, 400, xpNeeded = 300),
    Cosmetic("heart_pin", "Heart Pin", "💗", CosmeticSlot.ACCESSORY, 75),
    Cosmetic("gold_medal", "Gold Medal", "🏅", CosmeticSlot.ACCESSORY, 0, goldenNeeded = 1),
    Cosmetic("room_cozy", "Cozy Room", "🏡", CosmeticSlot.ROOM, 0),
    Cosmetic("room_sky", "Blue Sky", "☁️", CosmeticSlot.ROOM, 300),
    Cosmetic("room_forest", "Forest", "🌿", CosmeticSlot.ROOM, 450, xpNeeded = 500),
    Cosmetic("starlight_bow", "Starlight Bow", "🎀", CosmeticSlot.HAT, 0, achievementOnly = true),
    Cosmetic("room_stars", "Starry Room", "🌟", CosmeticSlot.ROOM, 0, achievementOnly = true)
)
