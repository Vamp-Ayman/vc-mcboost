package dev.zalithboost.client.config;

/**
 * Auto-generated hardware-tuned configuration for Zalith Launcher 1.21.11
 * Device Target: Lenovo Legion Y700 (J706X · Snapdragon 870 / Adreno 650)
 * Render Scale: 65% (1664x1040)
 */
public final class ZalithBoostConfig {
    // Hardware & Resolution Profile
    public static final String TARGET_MC_VERSION = "1.21.11";
    public static final String TARGET_FABRIC_VERSION = "1.19.0";
    public static final String TARGET_DEVICE = "J706X";
    public static final float RESOLUTION_SCALE = 0.65f;
    public static final int EFFECTIVE_WIDTH = 1664;
    public static final int EFFECTIVE_HEIGHT = 1040;

    // Freecam & Screen-Space Culling Rules
    public static boolean DISABLE_CAVE_CULLING_IN_FREECAM = true;
    public static boolean STRICT_SCREEN_SPACE_FRUSTUM = true;
    public static float SUB_PIXEL_CULL_THRESHOLD_PX = 1.5f;
    public static int RENDER_DISTANCE_CHUNKS = 12;

    // Rendering Engine Overhaul Flags
    public static boolean GREEDY_CHUNK_MESHER = true;
    public static boolean ADRENO_JNI_GL_DEDUP = true;
    public static boolean ZERO_GC_FRAME_ALLOCATOR = true;
    public static boolean ENTITY_SCREEN_SPACE_LOD = true;
    public static boolean BLOCK_ENTITY_FRUSTUM_CULL = true;
    public static boolean PARTICLE_FRUSTUM_AND_CAP = true;
    public static int MAX_PARTICLES = 1200;
    public static float MIPMAP_LOD_BIAS_65_RES = -0.45f;
    public static boolean DISABLE_SKY_FOG_CHURN = true;
    public static boolean THROTTLE_OFFSCREEN_ENTITY_TICKS = true;

    private ZalithBoostConfig() {}
}
