package com.islamozroko.autoez;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class AutoEzMod implements ClientModInitializer {
    private static AutoEzConfig config;
    private static final Set<UUID> alivePlayers = new HashSet<>();
    private static final Random random = new Random();

    @Override
    public void onInitializeClient() {
        config = AutoEzConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null || !config.enabled) return;

            Set<UUID> currentAlive = new HashSet<>();
            boolean someoneDied = false;

            for (Player player : client.level.players()) {
                if (player != client.player && player.distanceTo(client.player) <= config.radius) {
                    if (player.isAlive()) {
                        currentAlive.add(player.getUUID());
                    } else if (alivePlayers.contains(player.getUUID())) {
                        someoneDied = true;
                    }
                }
            }

            if (someoneDied && !config.phrases.isEmpty()) {
                String phrase = config.phrases.get(random.nextInt(config.phrases.size()));
                client.player.connection.sendChat(phrase);
            }

            alivePlayers.clear();
            alivePlayers.addAll(currentAlive);
        });
    }

    public static AutoEzConfig getConfig() {
        return config;
    }
}
