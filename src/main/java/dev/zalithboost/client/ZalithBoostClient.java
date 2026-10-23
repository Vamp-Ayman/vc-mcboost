package dev.zalithboost.client;

import dev.zalithboost.client.config.ZalithBoostConfig;
import dev.zalithboost.client.engine.FreecamCullController;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZalithBoostClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ZalithBoost-1.21.11");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[ZalithBoost] Initializing Rendering Engine Overhaul for Minecraft {}", ZalithBoostConfig.TARGET_MC_VERSION);
        LOGGER.info("[ZalithBoost] Hardware Profile: {} @ {}% Resolution ({}x{})",
                ZalithBoostConfig.TARGET_DEVICE,
                Math.round(ZalithBoostConfig.RESOLUTION_SCALE * 100),
                ZalithBoostConfig.EFFECTIVE_WIDTH,
                ZalithBoostConfig.EFFECTIVE_HEIGHT);
        LOGGER.info("[ZalithBoost] Freecam Cave Culling Override: {} | Strict Screen-Space Frustum: {}",
                ZalithBoostConfig.DISABLE_CAVE_CULLING_IN_FREECAM,
                ZalithBoostConfig.STRICT_SCREEN_SPACE_FRUSTUM);
    }
}
