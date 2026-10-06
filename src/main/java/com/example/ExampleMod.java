package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

public class ExampleMod implements ClientModInitializer {

    private static KeyBinding webKey;
    private static KeyBinding waterBucketKey;
    private static KeyBinding lavaBucketKey;
    private static KeyBinding pearlKey;
    private static KeyBinding shieldBreakerKey;

    @Override
    public void onInitializeClient() {
        String category = "category.multimacro.title";

        // V=86, C=67, X=88, G=71, B=66
        webKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.web", 86, category));
        waterBucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.water", 67, category));
        lavaBucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.lava", 88, category));
        pearlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.pearl", 71, category));
        shieldBreakerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.shieldbreaker", 66, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (webKey.wasPressed()) {
                switchToHotbarItem(client, "cobweb");
            }
            while (waterBucketKey.wasPressed()) {
                switchToHotbarItem(client, "water_bucket");
            }
            while (lavaBucketKey.wasPressed()) {
                switchToHotbarItem(client, "lava_bucket");
            }
            while (pearlKey.wasPressed()) {
                switchToHotbarItem(client, "ender_pearl");
            }
            while (shieldBreakerKey.wasPressed()) {
                switchToAxe(client);
            }
        });
    }

    private void switchToHotbarItem(net.minecraft.client.MinecraftClient client, String itemId) {
        if (client.player == null) return;
        for (int i = 0; i < 9; i++) {
            String currentItem = client.player.getInventory().getStack(i).getItem().toString();
            if (currentItem.contains(itemId)) {
                client.player.getInventory().selectedSlot = i;
                break;
            }
        }
    }

    private void switchToAxe(net.minecraft.client.MinecraftClient client) {
        if (client.player == null) return;
        for (int i = 0; i < 9; i++) {
            String currentItem = client.player.getInventory().getStack(i).getItem().toString();
            if (currentItem.contains("axe")) {
                client.player.getInventory().selectedSlot = i;
                break;
            }
        }
    }
}
