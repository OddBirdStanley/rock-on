package net.oddbirds.rockon.advancements.criterion;

import java.util.function.Predicate;

import com.google.gson.JsonObject;

import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate.AndPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;
import net.oddbirds.rockon.RockOn;

public class ChangeDimensionAccumulationTrigger extends AbstractCriterionTrigger<ChangeDimensionAccumulationTrigger.Instance> {
	
	public static final ResourceLocation ID = new ResourceLocation(RockOn.MODID, "change_dimension_accumulation");
	
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	protected Instance createInstance(JsonObject data, AndPredicate predicate, ConditionArrayParser parser) {
		return new Instance(predicate, data.get("minimum").getAsInt());
	}
	
	public void trigger(ServerPlayerEntity entity, int value) {
		trigger(entity, (e) -> e.matches(value));
	}
	
	public static class Instance extends CriterionInstance {
		
		public int minimum;

		public Instance(AndPredicate predicate, int minimum) {
			super(ChangeDimensionAccumulationTrigger.ID, predicate);
			this.minimum = minimum;
		}
		
		public static Instance minimum(int value) {
			return new Instance(AndPredicate.ANY, value);
		}
		
		public boolean matches(int value) {
			return minimum == value;
		}
		
		@Override
		public JsonObject serializeToJson(ConditionArraySerializer serializer) {
			JsonObject json = super.serializeToJson(serializer);
			json.addProperty("minimum", minimum);
			return json;
		}
		
	}

}
