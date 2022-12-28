package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import com.mojang.authlib.GameProfile;
import me.juancarloscp52.bedrockify.client.features.useAnimations.AnimationsHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    /**
     * Drop animation for Hotbar item.
     */
    @Inject(method = "dropSelectedItem", at = @At("HEAD"))
    private void bedrockify$animateHotbarItemDrop(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        if (entireStack) {
            return;
        }

        AnimationsHelper.doBobbingAnimation(this.getMainHandStack());
    }

    /**
     * Handles the status message from packet.
     *
     * @see net.minecraft.entity.LivingEntity#damage
     * @see net.minecraft.world.World#sendEntityStatus
     * @see net.minecraft.entity.LivingEntity#handleStatus
     */
    @Override
    public void handleStatus(byte status) {
        final ItemStack itemStack = this.getStackInHand(this.getActiveHand());
        if (status == 29 && itemStack.isOf(Items.SHIELD)) {
            // Blocked by shield
            AnimationsHelper.doBobbingAnimation(itemStack);
        }
        super.handleStatus(status);
    }
}
