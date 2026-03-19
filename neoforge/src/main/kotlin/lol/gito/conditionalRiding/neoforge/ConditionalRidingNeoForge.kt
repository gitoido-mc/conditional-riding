/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.neoforge

import lol.gito.conditionalRiding.common.ConditionalRiding
import lol.gito.conditionalRiding.common.ConditionalRiding.MOD_ID
import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(MOD_ID)
object ConditionalRidingNeoForge : ConditionalRidingImplementation {
    init {
        with(MOD_BUS) {
            addListener { _: FMLCommonSetupEvent ->
                ConditionalRiding.initialize(this@ConditionalRidingNeoForge)
            }
        }
    }
}
