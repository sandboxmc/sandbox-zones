package io.sandbox.zones.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.sandbox.zones.zone.Zone;
import io.sandbox.zones.zone.ZoneManagerStore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {

  public PlayerEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
  public void safeInv(CallbackInfo ci) {
    UUID playerId = this.getUuid();
    Zone zone = ZoneManagerStore.getZoneByPlayerId(playerId);
    if (zone != null && zone.shouldKeepInventory(playerId)) {
      // cancel the dropInventory method
      ci.cancel();
      return;
    }
  }
}
