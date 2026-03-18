package lol.gito.conditionalRiding.common.mixin.pokemon.interaction;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import lol.gito.conditionalRiding.common.ConditionalRiding;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonEntity.class)
public abstract class OnShowInteractionWheel {
    @Unique
    private ServerPlayer conditionalRiding$chosenPlayer;

    @Inject(method = "showInteractionWheel", at = @At(value = "HEAD"))
    private void conditionalRiding$captureShowInteractionWheelArgs(ServerPlayer player, ItemStack itemStack, CallbackInfo ci) {
        this.conditionalRiding$chosenPlayer = player;
    }


    @ModifyVariable(method = "showInteractionWheel", at = @At(value = "STORE"), name = "canRide")
    private boolean conditionalRiding$OverrideCanRide(boolean captured) {
        if (captured) {
            return ConditionalRiding.canRide(this.conditionalRiding$chosenPlayer, (PokemonEntity) (Object) this);
        }
        return false;
    }
}
