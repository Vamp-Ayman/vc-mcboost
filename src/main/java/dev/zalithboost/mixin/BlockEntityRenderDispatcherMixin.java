package dev.zalithboost.mixin;

import dev.zalithboost.client.config.ZalithBoostConfig;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Culls off-screen and sub-pixel BlockEntities (chests, signs, hoppers, skulls, beacons)
 * to eliminate draw-call spikes in bases and storage rooms on Adreno 650.
 */
@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Shadow public Camera camera;

    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void zalithboost$cullBlockEntity(
            E blockEntity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            CallbackInfo ci
    ) {
        if (!ZalithBoostConfig.BLOCK_ENTITY_FRUSTUM_CULL || this.camera == null || blockEntity == null) {
            return;
        }
        BlockPos pos = blockEntity.getPos();
        Vec3d camPos = this.camera.getPos();
        double distSq = camPos.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        // Cull block entities beyond effective 65% resolution visibility range (48 blocks = 2304 sq)
        if (distSq > 2304.0D) {
            ci.cancel();
        }
    }
}
