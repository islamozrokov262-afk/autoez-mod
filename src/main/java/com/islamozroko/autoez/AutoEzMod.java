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
            Player deadPlayer = null;

            // Проходим ТОЛЬКО по игрокам
            for (Player player : client.level.players()) {
                if (player != client.player && player.distanceTo(client.player) <= config.radius) {
                    if (player.isAlive()) {
                        currentAlive.add(player.getUUID());
                    } else if (alivePlayers.contains(player.getUUID())) {
                        // Запоминаем, кто именно умер
                        deadPlayer = player;
                    }
                }
            }

            // Если кто-то умер и у нас есть фразы
            if (deadPlayer != null && !config.phrases.isEmpty()) {
                String phrase = config.phrases.get(random.nextInt(config.phrases.size()));
                // Заменяем %player% на ник умершего
                String message = phrase.replace("%player%", deadPlayer.getName().getString());
                client.player.connection.sendChat(message);
            }

            alivePlayers.clear();
            alivePlayers.addAll(currentAlive);
        });
    }

    public static AutoEzConfig getConfig() {
        return config;
    }
}
