package dev.zalithboost.mixin;

import dev.zalithboost.client.engine.FreecamCullController;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents opaque blocks from marking chunk faces as closed in ChunkOcclusionDataBuilder
 * when Freecam mode is active, ensuring zero cave culling artifacts while relying strictly
 * on the 6-plane ScreenFrustumEngine to cull everything outside the user's screen.
 */
@Mixin(ChunkOcclusionDataBuilder.class)
public abstract class ChunkOcclusionDataBuilderMixin {
    @Inject(method = "markClosed", at = @At("HEAD"), cancellable = true)
    private void zalithboost$bypassCaveOcclusionInFreecam(BlockPos pos, CallbackInfo ci) {
        if (FreecamCullController.shouldBypassCaveGraphOcclusion()) {
            ci.cancel();
        }
    }
}
