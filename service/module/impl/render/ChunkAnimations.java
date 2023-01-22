/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.module.impl.render;

import vip.astroline.client.service.module.Category;
import vip.astroline.client.service.module.Module;
import vip.astroline.client.storage.utils.render.chunk.BetterChunkUtils;

public class ChunkAnimations
extends Module {
    public static BetterChunkUtils betterChunkUtils;

    public ChunkAnimations() {
        super("ChunkAnimations", Category.Render, 0, false);
    }

    @Override
    public void onEnable() {
        betterChunkUtils = new BetterChunkUtils();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

