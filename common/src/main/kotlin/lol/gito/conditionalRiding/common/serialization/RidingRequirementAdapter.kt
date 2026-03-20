/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.serialization

import com.cobblemon.mod.common.api.interaction.InteractionEffect
import com.cobblemon.mod.common.api.molang.ExpressionLike
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.adapters.RequirementAdapter
import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.requirements.AdvancementRequirement
import com.cobblemon.mod.common.pokemon.requirements.AreaRequirement
import com.cobblemon.mod.common.pokemon.requirements.BiomeRequirement
import com.cobblemon.mod.common.pokemon.requirements.BlocksTraveledRequirement
import com.cobblemon.mod.common.pokemon.requirements.ChanceRequirement
import com.cobblemon.mod.common.pokemon.requirements.FriendshipRequirement
import com.cobblemon.mod.common.pokemon.requirements.HeldItemRequirement
import com.cobblemon.mod.common.pokemon.requirements.LevelRequirement
import com.cobblemon.mod.common.pokemon.requirements.MoonPhaseRequirement
import com.cobblemon.mod.common.pokemon.requirements.MoveSetRequirement
import com.cobblemon.mod.common.pokemon.requirements.MoveTypeRequirement
import com.cobblemon.mod.common.pokemon.requirements.OwnerHoldsItemRequirement
import com.cobblemon.mod.common.pokemon.requirements.PartyMemberRequirement
import com.cobblemon.mod.common.pokemon.requirements.PokemonPropertiesRequirement
import com.cobblemon.mod.common.pokemon.requirements.PropertyRangeRequirement
import com.cobblemon.mod.common.pokemon.requirements.StatEqualRequirement
import com.cobblemon.mod.common.pokemon.requirements.StructureRequirement
import com.cobblemon.mod.common.pokemon.requirements.TimeRangeRequirement
import com.cobblemon.mod.common.pokemon.requirements.UseMoveRequirement
import com.cobblemon.mod.common.pokemon.requirements.WeatherRequirement
import com.cobblemon.mod.common.pokemon.requirements.WorldRequirement
import com.cobblemon.mod.common.util.adapters.ExpressionLikeAdapter
import com.cobblemon.mod.common.util.adapters.IdentifierAdapter
import com.cobblemon.mod.common.util.adapters.IntRangeAdapter
import com.cobblemon.mod.common.util.adapters.InteractionEffectAdapter
import com.cobblemon.mod.common.util.adapters.pokemonPropertiesShortAdapter
import com.cobblemon.mod.common.util.readString
import com.cobblemon.mod.common.util.writeString
import com.google.common.collect.HashBiMap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import lol.gito.conditionalRiding.common.ConditionalRiding
import lol.gito.conditionalRiding.common.requirement.CosmeticItemRequirement
import lol.gito.conditionalRiding.common.requirement.PermissionNodeRequirement
import lol.gito.conditionalRiding.common.requirement.MinimalHpPercentRequirement
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.LowerCaseEnumTypeAdapterFactory
import java.lang.reflect.Type
import kotlin.reflect.KClass

object RidingRequirementAdapter : RequirementAdapter {

    private const val VARIANT = "variant"

    private val types = HashBiMap.create<String, KClass<out Requirement>>()

    init {
        this.registerType(AreaRequirement.ADAPTER_VARIANT, AreaRequirement::class)
        this.registerType(BiomeRequirement.ADAPTER_VARIANT, BiomeRequirement::class)
        this.registerType(FriendshipRequirement.ADAPTER_VARIANT, FriendshipRequirement::class)
        this.registerType(HeldItemRequirement.ADAPTER_VARIANT, HeldItemRequirement::class)
        this.registerType(WorldRequirement.ADAPTER_VARIANT, WorldRequirement::class)
        this.registerType(MoveSetRequirement.ADAPTER_VARIANT, MoveSetRequirement::class)
        this.registerType(MoveTypeRequirement.ADAPTER_VARIANT, MoveTypeRequirement::class)
        this.registerType(PartyMemberRequirement.ADAPTER_VARIANT, PartyMemberRequirement::class)
        this.registerType(PokemonPropertiesRequirement.ADAPTER_VARIANT, PokemonPropertiesRequirement::class)
        this.registerType(TimeRangeRequirement.ADAPTER_VARIANT, TimeRangeRequirement::class)
        this.registerType(LevelRequirement.ADAPTER_VARIANT, LevelRequirement::class)
        this.registerType(WeatherRequirement.ADAPTER_VARIANT, WeatherRequirement::class)
        this.registerType(StatEqualRequirement.ADAPTER_VARIANT, StatEqualRequirement::class)
        this.registerType(UseMoveRequirement.ADAPTER_VARIANT, UseMoveRequirement::class)
        this.registerType(MoonPhaseRequirement.ADAPTER_VARIANT, MoonPhaseRequirement::class)
        this.registerType(BlocksTraveledRequirement.ADAPTER_VARIANT, BlocksTraveledRequirement::class)
        this.registerType(StructureRequirement.ADAPTER_VARIANT, StructureRequirement::class)
        this.registerType(PropertyRangeRequirement.ADAPTER_VARIANT, PropertyRangeRequirement::class)
        this.registerType(AdvancementRequirement.ADAPTER_VARIANT, AdvancementRequirement::class)
        this.registerType(OwnerHoldsItemRequirement.ADAPTER_VARIANT, OwnerHoldsItemRequirement::class)
        this.registerType(ChanceRequirement.ADAPTER_VARIANT, ChanceRequirement::class)
        this.registerType(PermissionNodeRequirement.ADAPTER_VARIANT, PermissionNodeRequirement::class)
        this.registerType(CosmeticItemRequirement.ADAPTER_VARIANT, CosmeticItemRequirement::class)
        this.registerType(MinimalHpPercentRequirement.ADAPTER_VARIANT, MinimalHpPercentRequirement::class)
    }

    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(ResourceLocation::class.java, IdentifierAdapter)
            .registerTypeAdapter(PokemonProperties::class.java, pokemonPropertiesShortAdapter)
            .registerTypeAdapter(
                ItemPredicate::class.java,
                ItemConditionWrapperAdapter(ConditionalRiding.server.registryAccess())
            )
            .registerTypeAdapter(Requirement::class.java, RidingRequirementAdapter)
            .registerTypeAdapter(InteractionEffect::class.java, InteractionEffectAdapter)
            .registerTypeAdapter(ExpressionLike::class.java, ExpressionLikeAdapter)
            .registerTypeAdapter(IntRange::class.java, IntRangeAdapter)
            .registerTypeAdapterFactory(OptionalTypeAdapterFactory())
            .registerTypeAdapterFactory(LowerCaseEnumTypeAdapterFactory())
            .setPrettyPrinting()
            .create()
    }

    override fun <T : Requirement> registerType(id: String, type: KClass<T>) {
        this.types[id.lowercase()] = type
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Requirement {
        val variant = json.asJsonObject.get(VARIANT).asString.lowercase()
        val type = getRequirementType(variant)
        return context.deserialize(json, type.java)
    }

    override fun serialize(requirement: Requirement, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = context.serialize(requirement, requirement::class.java).asJsonObject
        val variant = getVariant(requirement)
        json.addProperty(VARIANT, variant)
        return json
    }

    fun encode(requirement: Requirement, buffer: FriendlyByteBuf) {
        buffer.writeString(gson.toJson(requirement, Requirement::class.java))
    }

    fun decode(buffer: FriendlyByteBuf): Requirement {
        return gson.fromJson(buffer.readString(), Requirement::class.java)
    }

    private fun getVariant(requirement: Requirement): String {
        val variant = this.types.inverse()[requirement::class] ?: throw IllegalArgumentException(
            "Cannot resolve evolution requirement for type ${requirement::class.qualifiedName}"
        )

        return variant
    }

    private fun getRequirementType(variant: String): KClass<out Requirement> {
        val requirementType = this.types[variant] ?: throw IllegalArgumentException(
            "Cannot resolve evolution requirement type for variant $variant"
        )

        return requirementType
    }
}
