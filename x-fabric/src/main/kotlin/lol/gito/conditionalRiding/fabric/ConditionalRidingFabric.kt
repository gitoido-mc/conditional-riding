/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.fabric

import lol.gito.conditionalRiding.common.ConditionalRiding
import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback

object ConditionalRidingFabric : ModInitializer, ConditionalRidingImplementation {
    override fun onInitialize() {
        ConditionalRiding.initialize(this)
        CommandRegistrationCallback.EVENT.register(ConditionalRiding.Commands::register)
    }
}
