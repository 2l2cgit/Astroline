/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.service.event.impl.render;

import vip.astroline.client.service.event.Event;

public class EventShader
extends Event {
    private ShaderType shaderType;

    public EventShader(ShaderType shaderType) {
        this.shaderType = shaderType;
    }

    public boolean isBlur() {
        return this.shaderType == ShaderType.BLUR;
    }

    public boolean isShadow() {
        return this.shaderType == ShaderType.SHADOW;
    }

    public boolean isGlow() {
        return this.shaderType == ShaderType.GLOW;
    }

    public ShaderType getShaderType() {
        return this.shaderType;
    }

    public void setShaderType(ShaderType shaderType) {
        this.shaderType = shaderType;
    }

    public static enum ShaderType {
        BLUR,
        SHADOW,
        GLOW;

    }
}

