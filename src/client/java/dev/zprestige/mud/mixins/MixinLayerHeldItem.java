package dev.zprestige.mud.mixins;

import dev.zprestige.mud.Mud;
import dev.zprestige.mud.events.impl.render.HeldItemEvent;
import dev.zprestige.mud.util.MC;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem implements MC {

    @Inject(method = "renderHeldItem", at = @At("HEAD"), cancellable = true)
    private void renderHeldItem(EntityLivingBase entity, ItemStack item, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, CallbackInfo ci) {
        if (entity instanceof EntityPlayer && !entity.equals(mc.player)) {
            final HeldItemEvent event = new HeldItemEvent();
            Mud.eventBus.invoke(event);
            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }
}