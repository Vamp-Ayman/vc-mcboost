package dev.zalithboost.mixin;

import dev.zalithboost.client.engine.FreecamCullController;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ensures MinecraftClient chunkCullingEnabled flag stays synchronized with FreecamCullController
 * every client tick without triggering GC allocations.
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public boolean chunkCullingEnabled;

    @Inject(method = "tick", at = @At("HEAD"))
    private void zalithboost$syncFreecamCaveCulling(CallbackInfo ci) {
        if (FreecamCullController.shouldBypassCaveGraphOcclusion()) {
            this.chunkCullingEnabled = false;
        }
    }
}
