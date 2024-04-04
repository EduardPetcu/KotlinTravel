package com.example.travel.data

data class Achievement(
    val title: String,
    val description: String,
    val requirement: String,
) {
    // make a list of hardcoded achievements
    companion object {
        val achievements = listOf(
            Achievement(
                title = "Edited description",
                description = "You have edited your description!",
                requirement = "Edit your description in the profile screen"
            ),
            Achievement(
                title = "5 visited cities",
                description = "You have visited 5 cities!",
                requirement = "Visit 5 cities"
            ),
            Achievement(
                title = "Calculator usage",
                description = "You have planned a budget!",
                requirement = "Use the calculator in the calculator screen to plan a budget"
            ),
            Achievement(
                title = "10 visited cities",
                description = "You have visited 10 cities",
                requirement = "Visit 10 cities"
            )
        )
    }
}
