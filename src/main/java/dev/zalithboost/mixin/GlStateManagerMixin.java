package dev.zalithboost.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.zalithboost.client.engine.AdrenoGlStateCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts redundant OpenGL state calls in Blaze3D GlStateManager before they cross
 * the JNI boundary into Zalith Launcher's mobile OpenGL/Vulkan translator on J706X.
 */
@Mixin(value = GlStateManager.class, remap = false)
public abstract class GlStateManagerMixin {
    @Inject(method = "_bindTexture", at = @At("HEAD"), cancellable = true, remap = false)
    private static void zalithboost$dedupBindTexture(int texture, CallbackInfo ci) {
        if (!AdrenoGlStateCache.shouldBindTexture(texture)) {
            ci.cancel();
        }
    }

    @Inject(method = "_enableBlend", at = @At("HEAD"), cancellable = true, remap = false)
    private static void zalithboost$dedupEnableBlend(CallbackInfo ci) {
        if (!AdrenoGlStateCache.shouldToggleBlend(true)) {
            ci.cancel();
        }
    }

    @Inject(method = "_disableBlend", at = @At("HEAD"), cancellable = true, remap = false)
    private static void zalithboost$dedupDisableBlend(CallbackInfo ci) {
        if (!AdrenoGlStateCache.shouldToggleBlend(false)) {
            ci.cancel();
        }
    }

    @Inject(method = "_depthMask", at = @At("HEAD"), cancellable = true, remap = false)
    private static void zalithboost$dedupDepthMask(boolean mask, CallbackInfo ci) {
        if (!AdrenoGlStateCache.shouldSetDepthMask(mask)) {
            ci.cancel();
        }
    }
}
