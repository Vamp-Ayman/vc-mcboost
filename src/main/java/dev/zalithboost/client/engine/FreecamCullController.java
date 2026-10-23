package dev.zalithboost.client.engine;

import dev.zalithboost.client.config.ZalithBoostConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Detects whenever the player enters Freecam (either via standalone Freecam mods,
 * detached Camera entity, Spectator noclip, or camera-to-player eye displacement)
 * and immediately disables Minecraft's cave occlusion flood-fill graph while keeping
 * strict 6-plane screen-space frustum culling active.
 */
public final class FreecamCullController {
    private static boolean manualFreecamOverride = false;
    private static boolean activeFreecamDetected = false;
    private static boolean lastFrameFreecamState = false;

    public static void toggleManualFreecamMode() {
        manualFreecamOverride = !manualFreecamOverride;
    }

    public static boolean isManualFreecamOverride() {
        return manualFreecamOverride;
    }

    /**
     * Evaluates once per frame at the start of WorldRenderer#setupTerrain.
     * If the active Camera position diverges from ClientPlayerEntity's eye position
     * by more than 0.5 blocks, or if the camera entity is detached, Freecam is active.
     */
    public static void updateFrameState(MinecraftClient client, Camera camera) {
        if (client == null || client.player == null || camera == null) {
            activeFreecamDetected = false;
            return;
        }

        if (manualFreecamOverride || client.player.isSpectator()) {
            activeFreecamDetected = true;
        } else {
            Entity focused = camera.getFocusedEntity();
            if (focused != null && focused != client.player) {
                activeFreecamDetected = true;
            } else {
                Vec3d camPos = camera.getPos();
                Vec3d eyePos = client.player.getCameraPosVec(1.0f);
                double distSq = camPos.squaredDistanceTo(eyePos);
                // > 0.5 blocks separation without standard 3rd-person or with noclip freecam
                activeFreecamDetected = (!camera.isThirdPerson() && distSq > 0.25D) || distSq > 20.25D;
            }
        }

        if (ZalithBoostConfig.DISABLE_CAVE_CULLING_IN_FREECAM) {
            if (activeFreecamDetected) {
                // Disable vanilla cave graph occlusion so underground caves on screen are never culled
                client.chunkCullingEnabled = false;
            } else if (lastFrameFreecamState) {
                // Restore cave graph culling immediately when exiting Freecam
                client.chunkCullingEnabled = true;
            }
        }

        lastFrameFreecamState = activeFreecamDetected;
    }

    public static boolean shouldBypassCaveGraphOcclusion() {
        return ZalithBoostConfig.DISABLE_CAVE_CULLING_IN_FREECAM && activeFreecamDetected;
    }

    public static boolean isFreecamActive() {
        return activeFreecamDetected;
    }
}
