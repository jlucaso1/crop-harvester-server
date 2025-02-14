package com.jlucaso.cropharvestserver.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface CropHarvestedCallback {
  Event<CropHarvestedCallback> EVENT = EventFactory.createArrayBacked(CropHarvestedCallback.class,
      (listeners) -> (player, world, pos, state) -> {
        for (CropHarvestedCallback listener : listeners) {
          listener.onCropHarvested(player, world, pos, state);
        }
      });

  void onCropHarvested(PlayerEntity player, ServerWorld world, BlockPos pos, BlockState state);
}
