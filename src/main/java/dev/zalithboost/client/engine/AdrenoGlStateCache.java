package dev.zalithboost.client.engine;

import dev.zalithboost.client.config.ZalithBoostConfig;

/**
 * Eliminates redundant OpenGL state transitions across the Zalith Launcher JNI bridge
 * on Qualcomm Adreno 650 (Lenovo Legion Y700 J706X).
 * Mobile GL wrappers (LTW / Holy GL4ES / Turnip / Zink) suffer severe CPU stalls
 * when Minecraft repeatedly binds the same texture or toggles depth/blend states.
 */
public final class AdrenoGlStateCache {
    private static final int[] BOUND_TEXTURES = new int[32];
    private static int activeTextureUnit = 0;
    private static boolean blendEnabled = false;
    private static boolean depthMaskEnabled = true;
    private static boolean cullFaceEnabled = true;
    private static int savedJniCallsThisFrame = 0;

    public static void resetFrameStats() {
        savedJniCallsThisFrame = 0;
    }

    public static boolean shouldBindTexture(int targetTextureId) {
        if (!ZalithBoostConfig.ADRENO_JNI_GL_DEDUP) return true;
        int unit = activeTextureUnit & 31;
        if (BOUND_TEXTURES[unit] == targetTextureId) {
            savedJniCallsThisFrame++;
            return false;
        }
        BOUND_TEXTURES[unit] = targetTextureId;
        return true;
    }

    public static void setActiveTextureUnit(int unitIndex) {
        activeTextureUnit = unitIndex & 31;
    }

    public static boolean shouldToggleBlend(boolean enable) {
        if (!ZalithBoostConfig.ADRENO_JNI_GL_DEDUP) return true;
        if (blendEnabled == enable) {
            savedJniCallsThisFrame++;
            return false;
        }
        blendEnabled = enable;
        return true;
    }

    public static boolean shouldSetDepthMask(boolean flag) {
        if (!ZalithBoostConfig.ADRENO_JNI_GL_DEDUP) return true;
        if (depthMaskEnabled == flag) {
            savedJniCallsThisFrame++;
            return false;
        }
        depthMaskEnabled = flag;
        return true;
    }

    public static boolean shouldToggleCull(boolean enable) {
        if (!ZalithBoostConfig.ADRENO_JNI_GL_DEDUP) return true;
        if (cullFaceEnabled == enable) {
            savedJniCallsThisFrame++;
            return false;
        }
        cullFaceEnabled = enable;
        return true;
    }

    public static int getSavedJniCallsThisFrame() {
        return savedJniCallsThisFrame;
    }
}
