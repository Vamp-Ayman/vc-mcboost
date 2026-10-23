package dev.zalithboost.mixin;

import dev.zalithboost.client.config.ZalithBoostConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Enforces hard particle budget and skips redundant particle allocations during heavy combat/explosions.
 */
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    private int zalithboost$activeParticleCounter = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void zalithboost$resetCounter(CallbackInfo ci) {
        this.zalithboost$activeParticleCounter = 0;
    }

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void zalithboost$capParticles(Particle particle, CallbackInfo ci) {
        if (!ZalithBoostConfig.PARTICLE_FRUSTUM_AND_CAP) return;
        this.zalithboost$activeParticleCounter++;
        if (this.zalithboost$activeParticleCounter > ZalithBoostConfig.MAX_PARTICLES) {
            ci.cancel();
        }
    }
}
