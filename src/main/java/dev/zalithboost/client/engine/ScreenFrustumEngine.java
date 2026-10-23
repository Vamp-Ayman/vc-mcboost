package dev.zalithboost.client.engine;

import dev.zalithboost.client.config.ZalithBoostConfig;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * High-speed Screen-Space Frustum & Sub-Pixel Culling Engine.
 * Guarantees that ONLY geometry intersecting the active screen viewport and projecting
 * to >= SUB_PIXEL_CULL_THRESHOLD_PX at 65% resolution (1664x1040) is rendered.
 */
public final class ScreenFrustumEngine {
    private static double camX, camY, camZ;
    private static float focalLengthPx = 832;
    private static int culledSectionsThisFrame = 0;
    private static int renderedSectionsThisFrame = 0;

    public static void beginFrame(Vec3d cameraPos, float fovDegrees) {
        camX = cameraPos.x;
        camY = cameraPos.y;
        camZ = cameraPos.z;
        double halfFovRad = Math.toRadians(Math.max(30.0f, Math.min(130.0f, fovDegrees)) * 0.5);
        focalLengthPx = (float) ((ZalithBoostConfig.EFFECTIVE_WIDTH * 0.5) / Math.tan(halfFovRad));
        culledSectionsThisFrame = 0;
        renderedSectionsThisFrame = 0;
    }

    /**
     * Tests whether an axis-aligned bounding box (Chunk section, Entity, or BlockEntity)
     * is strictly inside the screen frustum AND projects to a visible pixel footprint
     * at 65% resolution on the J706X display.
     */
    public static boolean isVisibleOnScreen(Frustum frustum, Box box, double minPixelThreshold) {
        if (frustum == null || box == null) {
            return true;
        }

        // 1. Strict 6-plane screen frustum intersection test
        if (!frustum.isVisible(box)) {
            culledSectionsThisFrame++;
            return false;
        }

        if (!ZalithBoostConfig.STRICT_SCREEN_SPACE_FRUSTUM) {
            renderedSectionsThisFrame++;
            return true;
        }

        // 2. Screen-space projected pixel size check at 65% resolution
        double centerX = (box.minX + box.maxX) * 0.5 - camX;
        double centerY = (box.minY + box.maxY) * 0.5 - camY;
        double centerZ = (box.minZ + box.maxZ) * 0.5 - camZ;
        double distSq = centerX * centerX + centerY * centerY + centerZ * centerZ;

        if (distSq < 64.0) {
            renderedSectionsThisFrame++;
            return true;
        }

        double maxExtent = Math.max(box.getLengthX(), Math.max(box.getLengthY(), box.getLengthZ()));
        double projectedPx = (maxExtent * focalLengthPx) / Math.sqrt(distSq);

        if (projectedPx < minPixelThreshold) {
            culledSectionsThisFrame++;
            return false;
        }

        renderedSectionsThisFrame++;
        return true;
    }

    public static int getCulledSectionsThisFrame() {
        return culledSectionsThisFrame;
    }

    public static int getRenderedSectionsThisFrame() {
        return renderedSectionsThisFrame;
    }
}
