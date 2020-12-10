package com.dturnip.qswap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public class QuickSwap implements ModInitializer {

	@Override
	public void onInitialize() {
		onRightClick();
	}

	private static void onRightClick() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			MinecraftClient mc = MinecraftClient.getInstance();
			ClientPlayerInteractionManager interactionManager = mc.interactionManager;

			if (mc.mouse.wasRightButtonClicked()) {
				ItemStack stack = player.inventory.getMainHandStack();
				int holdIndex = player.inventory.main.indexOf(stack);

				EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack);
				int clothingIndex = determineIndex(equipmentSlot);

				if (hand == Hand.MAIN_HAND && clothingIndex != -1) {
					player.playSound(stack.getItem() == Items.ELYTRA ? SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
							: SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
					interactionManager.clickSlot(player.playerScreenHandler.syncId, clothingIndex, holdIndex,
							SlotActionType.SWAP, player);
					return TypedActionResult.success(stack);
				}
			}
			return TypedActionResult.pass(ItemStack.EMPTY);
		});
	}

	private static int determineIndex(EquipmentSlot type) {
		switch (type) {
		case HEAD:
			return 5;
		case CHEST:
			return 6;
		case LEGS:
			return 7;
		case FEET:
			return 8;
		default:
			return -1;
		}
	}

}
