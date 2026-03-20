/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.config

import com.cobblemon.mod.common.pokemon.requirements.HeldItemRequirement
import com.google.gson.annotations.Since
import lol.gito.conditionalRiding.common.ConditionalRiding.GLOBAL_RULESET
import lol.gito.conditionalRiding.common.serialization.RidingRequirementAdapter.VERSION_1_1
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.world.item.Items

data class ConditionalRidingConfig(
    val debug: Boolean = false,
    val enabled: Boolean = true,
    @Since(VERSION_1_1)
    val messagesInHotbar: Boolean = false,
    val rulesets: Map<String, Ruleset> = mapOf(
        GLOBAL_RULESET to Ruleset(
            message = "conditional_riding.failed_ruleset.global",
            rules = listOf(
                HeldItemRequirement(
                    ItemPredicate.Builder.item().of(Items.SADDLE).build()
                )
            )
        )
    )
)
