package com.kwende.snapcook.model

import org.junit.Assert.*
import org.junit.Test

class MealContextTest {
    @Test fun contextKeepsPreferencesBeforeIngredientsAndPreservesNotes() {
        val text = buildMealContext("No mushrooms", emptyList(), listOf(Ingredient("Apples", "two")))
        assertTrue(text.indexOf("No mushrooms") < text.indexOf("Apples"))
        assertTrue(text.contains("Apples — two"))
        assertTrue(text.contains("amounts unknown unless noted"))
    }
    @Test fun emptyContextDoesNotInventFoodOrPreferences() {
        val text = buildMealContext("", emptyList(), emptyList())
        assertTrue(text.contains("Not specified."))
        assertTrue(text.contains("No ingredients added yet."))
    }
}
