package com.islamozroko.autoez;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class AutoEzConfigScreen {
    public static Screen create(Screen parent) {
        AutoEzConfig config = AutoEzMod.getConfig();
        
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("AutoEZ Настройки"))
                .setSavingRunnable(config::save);
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.literal("Основное"));
        
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Включить"), config.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enabled = newValue)
                .build());
                
        general.addEntry(entryBuilder.startIntField(Component.literal("Радиус (блоки)"), config.radius)
                .setDefaultValue(15)
                .setSaveConsumer(newValue -> config.radius = newValue)
                .build());
                
        general.addEntry(entryBuilder.startStrList(Component.literal("Фразы"), config.phrases)
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Component.literal("Добавь фразы. Используй %player% для вставки ника убитого."))
                .setSaveConsumer(newValue -> config.phrases = newValue)
                .build());
        
        return builder.build();
    }
}
