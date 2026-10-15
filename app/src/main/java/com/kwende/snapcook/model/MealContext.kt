package com.kwende.snapcook.model

// Strings preserve leading zeros. No quantities or stock accounting.
data class Product(val barcode: String, val name: String)
data class Ingredient(val name: String, val note: String = "")
data class FavoriteRecipe(val title: String, val instructions: String, val feedback: String = "")

fun buildMealContext(
    householdNotes: String,
    favorites: List<FavoriteRecipe>,
    ingredients: List<Ingredient>,
): String = buildString {
    appendLine("Help me choose a simple dinner using these ingredients. Ask about gaps if needed.")
    appendLine("\nHOUSEHOLD NOTES")
    appendLine(householdNotes.trim().ifEmpty { "Not specified." })
    appendLine("\nFAVORITE RECIPES")
    if (favorites.isEmpty()) appendLine("None recorded.")
    favorites.forEach {
        appendLine("- ${it.title}: ${it.instructions}")
        if (it.feedback.isNotBlank()) appendLine("  Feedback: ${it.feedback}")
    }
    appendLine("\nAVAILABLE FOR THIS MEAL (amounts unknown unless noted)")
    if (ingredients.isEmpty()) appendLine("No ingredients added yet.")
    ingredients.forEach {
        appendLine("- ${it.name}" + if (it.note.isBlank()) "" else " — ${it.note}")
    }
}.trim()
