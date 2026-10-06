package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ClientModInitializer {

    private static KeyBinding webKey;
    private static KeyBinding waterBucketKey;
    private static KeyBinding lavaBucketKey;
    private static KeyBinding pearlKey;
    private static KeyBinding shieldBreakerKey;

    @Override
    public void onInitializeClient() {
        String category = "category.multimacro.title";

        webKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.web", GLFW.GLFW_KEY_V, category));
        waterBucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.water", GLFW.GLFW_KEY_C, category));
        lavaBucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.lava", GLFW.GLFW_KEY_X, category));
        pearlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.pearl", GLFW.GLFW_KEY_G, category));
        shieldBreakerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.shieldbreaker", GLFW.GLFW_KEY_B, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.interactionManager == null) return;

            while (webKey.wasPressed()) {
                switchToItemAndUse(client, "cobweb");
            }
            while (waterBucketKey.wasPressed()) {
                switchToItemAndUse(client, "water_bucket");
            }
            while (lavaBucketKey.wasPressed()) {
                switchToItemAndUse(client, "lava_bucket");
            }
            while (pearlKey.wasPressed()) {
                switchToItemAndUse(client, "ender_pearl");
            }
            while (shieldBreakerKey.wasPressed()) {
                switchToAxeAndAttack(client);
            }
        });
    }

    private void switchToItemAndUse(MinecraftClient client, String itemId) {
        int originalSlot = client.player.getInventory().selectedSlot;
        int targetSlot = -1;

        for (int i = 0; i < 9; i++) {
            String name = client.player.getInventory().getStack(i).getItem().toString();
            if (name.contains(itemId)) {
                targetSlot = i;
                break;
            }
        }

        if (targetSlot != -1) {
            client.player.getInventory().selectedSlot = targetSlot;
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            client.player.getInventory().selectedSlot = originalSlot;
        }
    }

    private void switchToAxeAndAttack(MinecraftClient client) {
        int originalSlot = client.player.getInventory().selectedSlot;
        int axeSlot = -1;

        for (int i = 0; i < 9; i++) {
            String name = client.player.getInventory().getStack(i).getItem().toString();
            if (name.contains("axe")) {
                axeSlot = i;
                break;
            }
        }

        if (axeSlot != -1) {
            client.player.getInventory().selectedSlot = axeSlot;
            if (client.targetedEntity != null) {
                client.interactionManager.attackEntity(client.player, client.targetedEntity);
                client.player.swingHand(Hand.MAIN_HAND);
            }
            client.player.getInventory().selectedSlot = originalSlot;
        }
    }
}
