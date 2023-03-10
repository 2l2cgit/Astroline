/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.storage.utils.other;

import net.minecraft.util.MathHelper;

public final class Timer {
    private long lastMS = 0L;
    private long previousTime = -1L;

    public boolean sleep(long time) {
        if (this.time() >= time) {
            this.reset();
            return true;
        }
        return false;
    }

    public boolean check(float milliseconds) {
        return (float)(System.currentTimeMillis() - this.previousTime) >= milliseconds;
    }

    public boolean delay(double milliseconds) {
        return (double)MathHelper.clamp_float(this.getCurrentMS() - this.lastMS, 0.0f, (float)milliseconds) >= milliseconds;
    }

    public void reset() {
        this.previousTime = System.currentTimeMillis();
        this.lastMS = this.getCurrentMS();
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.lastMS;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public double getLastDelay() {
        return this.getCurrentMS() - this.getLastMS();
    }

    public long getLastMS() {
        return this.lastMS;
    }
}

