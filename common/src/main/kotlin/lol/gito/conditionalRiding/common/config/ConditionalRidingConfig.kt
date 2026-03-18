package lol.gito.conditionalRiding.common.config

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement

data class ConditionalRidingConfig(
    val debug: Boolean? = false,
    val rulesets: Map<String, List<Requirement>>? = emptyMap()
)
