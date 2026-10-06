package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ClientModInitializer {

    private static KeyMapping webKey;
    private static KeyMapping waterBucketKey;
    private static KeyMapping lavaBucketKey;
    private static KeyMapping pearlKey;
    private static KeyMapping shieldBreakerKey;

    @Override
    public void onInitializeClient() {
        String category = "category.multimacro.title";

        webKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.macro.web", GLFW.GLFW_KEY_V, category));
        waterBucketKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.macro.water", GLFW.GLFW_KEY_C, category));
        lavaBucketKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.macro.lava", GLFW.GLFW_KEY_X, category));
        pearlKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.macro.pearl", GLFW.GLFW_KEY_G, category));
        shieldBreakerKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.macro.shieldbreaker", GLFW.GLFW_KEY_B, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.gameMode == null) return;

            while (webKey.consumeClick()) {
                switchToItemAndUse(client, "cobweb");
            }
            while (waterBucketKey.consumeClick()) {
                switchToItemAndUse(client, "water_bucket");
            }
            while (lavaBucketKey.consumeClick()) {
                switchToItemAndUse(client, "lava_bucket");
            }
            while (pearlKey.consumeClick()) {
                switchToItemAndUse(client, "ender_pearl");
            }
            while (shieldBreakerKey.consumeClick()) {
                switchToAxeAndAttack(client);
            }
        });
    }

    private void switchToItemAndUse(Minecraft client, String itemId) {
        int originalSlot = client.player.getInventory().selectedSlot;
        int targetSlot = -1;

        for (int i = 0; i < 9; i++) {
            String name = client.player.getInventory().getItem(i).getItem().toString();
            if (name.contains(itemId)) {
                targetSlot = i;
                break;
            }
        }

        if (targetSlot != -1) {
            client.player.getInventory().selectedSlot = targetSlot;
            client.gameMode.useItem(client.player, InteractionHand.MAIN_HAND);
            client.player.getInventory().selectedSlot = originalSlot;
        }
    }

    private void switchToAxeAndAttack(Minecraft client) {
        int originalSlot = client.player.getInventory().selectedSlot;
        int axeSlot = -1;

        for (int i = 0; i < 9; i++) {
            String name = client.player.getInventory().getItem(i).getItem().toString();
            if (name.contains("axe")) {
                axeSlot = i;
                break;
            }
        }

        if (axeSlot != -1) {
            client.player.getInventory().selectedSlot = axeSlot;
            if (client.crosshairPickEntity != null) {
                client.gameMode.attack(client.player, client.crosshairPickEntity);
                client.player.swing(InteractionHand.MAIN_HAND);
            }
            client.player.getInventory().selectedSlot = originalSlot;
        }
    }
}
