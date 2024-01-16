package dev.zprestige.mud.mixins.interfaces;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface IEntityRenderer {

    @Invoker("renderHand")
    void invokeRenderHand(float partialTicks, int pass);

}