/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.permission

import com.cobblemon.mod.common.api.permission.Permission
import com.cobblemon.mod.common.api.permission.PermissionLevel
import net.minecraft.resources.ResourceLocation

data class RidingPermission(
    override val identifier: ResourceLocation,
    override val literal: String
) : Permission {
    override val level: PermissionLevel = PermissionLevel.NONE
}
