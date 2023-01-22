/*
 * Decompiled with CFR 0.150.
 */
package vip.astroline.client.storage.utils.other;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import vip.astroline.client.Astroline;
import vip.astroline.client.service.module.Module;

public class DiscordThread
extends Thread {
    private boolean running = true;
    private static long created = 0L;
    private String app_id = "1055788885000585216";

    public static int getEnabledModules() {
        return (int)Astroline.INSTANCE.moduleManager.getModules().stream().filter(Module::isToggled).count();
    }

    public static int getTotalModules() {
        return Astroline.INSTANCE.moduleManager.getModules().size();
    }

    @Override
    public void start() {
        created = System.currentTimeMillis();
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> DiscordThread.update("Launching...")).build();
        DiscordRPC.discordInitialize(this.app_id, handlers, true);
        new Thread("Discord RPC Callback"){

            @Override
            public void run() {
                while (DiscordThread.this.running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();
    }

    public void shutdown() {
        this.running = false;
        DiscordRPC.discordShutdown();
    }

    public static void update(String status) {
        DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(status);
        b.setBigImage("logo", "Astroline " + Astroline.INSTANCE.VERSION + " [BETA]");
        b.setDetails("Enabled Modules: " + DiscordThread.getEnabledModules() + " / " + DiscordThread.getTotalModules());
        b.setStartTimestamps(created);
        DiscordRPC.discordUpdatePresence(b.build());
    }
}

