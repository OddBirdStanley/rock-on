package net.oddbirds.rockon.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.EntityType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.raid.Raid;

@Mixin(Raid.class)
public class MixinRaid {
	
	@Overwrite
	private int getPotentialBonusSpawns(Raid.WaveMember member, Random random, int groups, DifficultyInstance difficulty, boolean hasBonus) {
		int count;
		switch(member) {
			case WITCH:
				if(difficulty.getDifficulty()==Difficulty.EASY || groups<3 || groups==4) return 0;
				count = 1;
				break;
			case VINDICATOR:
				if(difficulty.getDifficulty()==Difficulty.EASY) count = random.nextInt(2);
				else if(difficulty.getDifficulty()==Difficulty.NORMAL) count = 1;
				else count = 2;
				break;
			case RAVAGER:
				count = (difficulty.getDifficulty()!=Difficulty.EASY && hasBonus) ? 1 : 0;
				break;
			default:
				if(member.entityType==EntityType.ILLUSIONER) {
					if(difficulty.getDifficulty()==Difficulty.EASY) count = random.nextInt(2);
					else if(difficulty.getDifficulty()==Difficulty.NORMAL) count = 1;
					else count = 2;
				}else return 0;
		}
		return count;
	}

}
