package net.oddbirds.rockon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.oddbirds.rockon.RockOn;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity {
	
	@Shadow
	public abstract void awardStat(Stat<?> stat, int value);
	
	@Inject(at = @At("RETURN"), method = "triggerDimensionChangeTriggers")
	private void triggerDimensionChangeTriggers(CallbackInfo info) {
		awardStat(Stats.CUSTOM.get(RockOn.CHANGE_DIMENSION), 1);
	}

}