package dev.zalithboost.mixin;

import dev.zalithboost.client.config.ZalithBoostConfig;
import dev.zalithboost.client.engine.ScreenFrustumEngine;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Enforces strict screen-space frustum culling and 65% resolution sub-pixel LOD culling
 * for all Entities before matrix transformations or shadow passes run.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void zalithboost$strictScreenSpaceEntityCull(
            E entity,
            Frustum frustum,
            double x,
            double y,
            double z,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (entity == null) {
            cir.setReturnValue(false);
            return;
        }
        if (ZalithBoostConfig.ENTITY_SCREEN_SPACE_LOD) {
            boolean visible = ScreenFrustumEngine.isVisibleOnScreen(
                    frustum,
                    entity.getVisibilityBoundingBox(),
                    ZalithBoostConfig.SUB_PIXEL_CULL_THRESHOLD_PX * 1.25
            );
            if (!visible) {
                cir.setReturnValue(false);
            }
        }
    }
}
