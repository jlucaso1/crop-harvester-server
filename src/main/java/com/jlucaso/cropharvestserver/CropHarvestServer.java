package com.jlucaso.cropharvestserver;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CropHarvestServer implements ModInitializer {
	public static final String MOD_ID = "cropharvestserver";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world.isClient)
				return ActionResult.PASS;

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof CropBlock crop && crop.isMature(state)) {
				if (world instanceof ServerWorld serverWorld) {
					// Harvest the crop
					java.util.List<ItemStack> drops = Block.getDroppedStacks(state, serverWorld, pos, null, player,
							player.getMainHandStack());
					drops.forEach(drop -> Block.dropStack(world, pos, drop));

					// Replant the crop
					world.setBlockState(pos, crop.getDefaultState());

					return ActionResult.SUCCESS;
				}
			}

			return ActionResult.PASS;
		});
	}
}