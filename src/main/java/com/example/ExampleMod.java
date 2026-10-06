package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class ExampleMod implements ClientModInitializer {

    private static KeyBinding webKey;
    private static KeyBinding waterBucketKey;
    private static KeyBinding lavaBucketKey;
    private static KeyBinding pearlKey;
    private static KeyBinding shieldBreakerKey;

    @Override
    public void onInitializeClient() {
        String category = "category.multimacro.title";

        // GLFW Key codes: 86=V, 67=C, 88=X, 71=G, 66=B
        webKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.web", 86, category));
        waterBucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.water", 67, category));
        lavaBucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.lava", 88, category));
        pearlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.pearl", 71, category));
        shieldBreakerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.macro.shieldbreaker", 66, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (webKey.wasPressed()) executeItemMacro(client, Items.COBWEB);
            while (waterBucketKey.wasPressed()) executeItemMacro(client, Items.WATER_BUCKET);
            while (lavaBucketKey.wasPressed()) executeItemMacro(client, Items.LAVA_BUCKET);
            while (pearlKey.wasPressed()) executeItemMacro(client, Items.ENDER_PEARL);
            while (shieldBreakerKey.wasPressed()) executeShieldBreaker(client);
        });
    }

    private void executeItemMacro(MinecraftClient client, Item targetItem) {
        if (client.player == null || client.interactionManager == null) return;

        PlayerInventory inventory = client.player.getInventory();
        int originalSlot = inventory.selectedSlot;
        int targetSlot = -1;

        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).isOf(targetItem)) {
                targetSlot = i;
                break;
            }
        }

        if (targetSlot != -1) {
            inventory.selectedSlot = targetSlot;
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            inventory.selectedSlot = originalSlot;
        }
    }

    private void executeShieldBreaker(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;

        PlayerInventory inventory = client.player.getInventory();
        int originalSlot = inventory.selectedSlot;
        int axeSlot = -1;

        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem() instanceof AxeItem) {
                axeSlot = i;
                break;
            }
        }

        if (axeSlot != -1) {
            inventory.selectedSlot = axeSlot;
            if (client.targetedEntity != null) {
                client.interactionManager.attackEntity(client.player, client.targetedEntity);
                client.player.swingHand(Hand.MAIN_HAND);
            }
            inventory.selectedSlot = originalSlot;
        }
    }
}
