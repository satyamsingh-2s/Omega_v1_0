package com.satyamsingh2s.productivity.omega.ui.model

enum class PersonalizationType {
    WHERE_YOU_ARE_NOW,
    SKILL_LEVEL,
    TIME_COMMITMENT,
    DEADLINE,
    APPROACH,
    DEPTH,
    DESIRED_OUTCOME
}

data class PersonalizationConfig(
    val type: PersonalizationType,
    val displayName: String,
    val question: String,
    val predefinedChoices: List<String>
)

object PersonalizationDefinitions {
    val definitions = listOf(
        PersonalizationConfig(
            type = PersonalizationType.WHERE_YOU_ARE_NOW,
            displayName = "Where You Are Now",
            question = "Where are you starting from?",
            predefinedChoices = listOf("Starting Fresh", "Have Some Background", "Already Working")
        ),
        PersonalizationConfig(
            type = PersonalizationType.SKILL_LEVEL,
            displayName = "Skill Level",
            question = "What is your current skill level?",
            predefinedChoices = listOf("Beginner", "Intermediate", "Advanced")
        ),
        PersonalizationConfig(
            type = PersonalizationType.TIME_COMMITMENT,
            displayName = "Time Commitment",
            question = "How much time can you commit daily?",
            predefinedChoices = listOf("Less Than 1 Hour", "1–2 Hours", "2+ Hours")
        ),
        PersonalizationConfig(
            type = PersonalizationType.DEADLINE,
            displayName = "Deadline",
            question = "When do you need to reach your goal?",
            predefinedChoices = listOf("No Deadline", "Within 3 Months", "Within 6 Months")
        ),
        PersonalizationConfig(
            type = PersonalizationType.APPROACH,
            displayName = "Approach",
            question = "How do you prefer to learn?",
            predefinedChoices = listOf("Learn by Doing", "Step by Step", "Goal Focused")
        ),
        PersonalizationConfig(
            type = PersonalizationType.DEPTH,
            displayName = "Depth",
            question = "How deep do you want to go?",
            predefinedChoices = listOf("Essentials", "Practical", "Comprehensive")
        ),
        PersonalizationConfig(
            type = PersonalizationType.DESIRED_OUTCOME,
            displayName = "Desired Outcome",
            question = "What is your primary desired outcome?",
            predefinedChoices = listOf("Knowledge & Understanding", "Practical Skills", "Completed Result")
        )
    )
    
    fun getConfig(type: PersonalizationType): PersonalizationConfig {
        return definitions.first { it.type == type }
    }
}
