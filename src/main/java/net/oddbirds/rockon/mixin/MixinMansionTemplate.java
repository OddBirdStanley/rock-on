package net.oddbirds.rockon.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.structure.WoodlandMansionPieces.MansionTemplate;
import net.minecraft.world.gen.feature.template.PlacementSettings;

@Mixin(MansionTemplate.class)
public abstract class MixinMansionTemplate extends MixinTemplateStructurePiece {
	
	@Overwrite
	protected void handleDataMarker(String value, BlockPos pos, IServerWorld world, Random random, MutableBoundingBox box) {
		if(value.startsWith("Chest")) {
			Rotation rotation = placeSettings.getRotation();
			BlockState state = Blocks.CHEST.defaultBlockState();
			if("ChestWest".equals(value)) {
				state = state.setValue(ChestBlock.FACING, rotation.rotate(Direction.WEST));
			}else if("ChestEast".equals(value)) {
				state = state.setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST));
			}else if("ChestSouth".equals(value)) {
				state = state.setValue(ChestBlock.FACING, rotation.rotate(Direction.SOUTH));
			}else if("ChestNorth".equals(value)) {
				state = state.setValue(ChestBlock.FACING, rotation.rotate(Direction.NORTH));
			}
			createChest(world, box, random, pos, LootTables.WOODLAND_MANSION, state);
		}else {
			List<AbstractIllagerEntity> entityList = new ArrayList<AbstractIllagerEntity>();
			switch(value) {
				case "Mage":
					entityList.add(EntityType.EVOKER.create(world.getLevel()));
					entityList.add(EntityType.ILLUSIONER.create(world.getLevel()));
					break;
				case "Warrior":
					entityList.add(EntityType.VINDICATOR.create(world.getLevel()));
					break;
				default:
					return;
			}
			
			for(AbstractIllagerEntity entity : entityList) {
				entity.setPersistenceRequired();
				entity.moveTo(pos, 0.0F, 0.0F);
				entity.finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.STRUCTURE, (ILivingEntityData) null, (CompoundNBT) null);
				world.addFreshEntityWithPassengers(entity);
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
			}
		}
	}

}

@Mixin(StructurePiece.class)
abstract class MixinStructurePiece {
	
	@Shadow
	protected abstract boolean createChest(IServerWorld world, MutableBoundingBox box, Random random, BlockPos pos, ResourceLocation path, @Nullable BlockState state);
	
}

@Mixin(TemplateStructurePiece.class)
abstract class MixinTemplateStructurePiece extends MixinStructurePiece {
	
	@Shadow
	protected PlacementSettings placeSettings;
	
}
