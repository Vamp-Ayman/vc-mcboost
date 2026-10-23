package dev.zalithboost.mixin;

import dev.zalithboost.client.config.ZalithBoostConfig;
import dev.zalithboost.client.engine.AdrenoGlStateCache;
import dev.zalithboost.client.engine.FreecamCullController;
import dev.zalithboost.client.engine.ScreenFrustumEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Rewrites WorldRenderer terrain setup to:
 * 1. Detect Freecam state and force chunkCullingEnabled = false so underground caves are never occluded.
 * 2. Initialize the 6-plane ScreenFrustumEngine calibrated for 65% resolution on J706X.
 * 3. Reset the Adreno 650 JNI GL state cache per frame.
 */
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "setupTerrain", at = @At("HEAD"))
    private void zalithboost$onSetupTerrainHead(
            Camera camera,
            Frustum frustum,
            boolean hasForcedFrustum,
            boolean spectator,
            CallbackInfo ci
    ) {
        AdrenoGlStateCache.resetFrameStats();
        FreecamCullController.updateFrameState(this.client, camera);
        float fov = this.client.options != null ? this.client.options.getFov().getValue().floatValue() : 90.0f;
        ScreenFrustumEngine.beginFrame(camera.getPos(), fov);
    }
}
