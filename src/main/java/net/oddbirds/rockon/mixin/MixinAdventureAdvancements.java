package net.oddbirds.rockon.mixin;

import java.util.List;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.ImmutableList;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.ChanneledLightningTrigger;
import net.minecraft.advancements.criterion.DamagePredicate;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.DistancePredicate;
import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.KilledByCrossbowTrigger;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.advancements.criterion.ShotCrossbowTrigger;
import net.minecraft.advancements.criterion.SlideDownBlockTrigger;
import net.minecraft.advancements.criterion.SummonedEntityTrigger;
import net.minecraft.advancements.criterion.TargetHitTrigger;
import net.minecraft.advancements.criterion.UsedTotemTrigger;
import net.minecraft.advancements.criterion.VillagerTradeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.advancements.AdventureAdvancements;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.raid.Raid;
import net.oddbirds.rockon.advancements.criterion.ChangeDimensionAccumulationTrigger;

@Mixin(AdventureAdvancements.class)
public abstract class MixinAdventureAdvancements {
	
	public static final List<RegistryKey<Biome>> EXPLORABLE_BIOMES = ImmutableList.of();
	
	@Shadow
	public abstract Advancement.Builder addMobsToKill(Advancement.Builder builder);
	
	@Overwrite
	public void accept(Consumer<Advancement> consumer) {
		Advancement a0 = Advancement.Builder.advancement().display(Items.MAP, new TranslationTextComponent("advancements.adventure.root.title"), new TranslationTextComponent("advancements.adventure.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"), FrameType.TASK, false, false, false).requirements(IRequirementsStrategy.OR).addCriterion("killed_something", KilledTrigger.Instance.playerKilledEntity()).addCriterion("killed_by_something", KilledTrigger.Instance.entityKilledPlayer()).save(consumer, "adventure/root");
		Advancement a1 = Advancement.Builder.advancement().parent(a0).display(Blocks.RED_BED, new TranslationTextComponent("advancements.adventure.sleep_in_bed.title"), new TranslationTextComponent("advancements.adventure.sleep_in_bed.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("slept_in_bed", PositionTrigger.Instance.sleptInBed()).save(consumer, "adventure/sleep_in_bed");
		AdventureAdvancements.addBiomes(Advancement.Builder.advancement(), EXPLORABLE_BIOMES).parent(a1).display(Items.DIAMOND_BOOTS, new TranslationTextComponent("advancements.adventure.adventuring_time.title"), new TranslationTextComponent("advancements.adventure.adventuring_time.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(500)).save(consumer, "adventure/adventuring_time");
		Advancement a2 = Advancement.Builder.advancement().parent(a0).display(Items.EMERALD, new TranslationTextComponent("advancements.adventure.trade.title"), new TranslationTextComponent("advancements.adventure.trade.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("traded", VillagerTradeTrigger.Instance.tradedWithVillager()).save(consumer, "adventure/trade");
		Advancement a3 = addMobsToKill(Advancement.Builder.advancement()).parent(a0).display(Items.IRON_SWORD, new TranslationTextComponent("advancements.adventure.kill_a_mob.title"), new TranslationTextComponent("advancements.adventure.kill_a_mob.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).requirements(IRequirementsStrategy.OR).save(consumer, "adventure/kill_a_mob");
		addMobsToKill(Advancement.Builder.advancement()).parent(a3).display(Items.DIAMOND_SWORD, new TranslationTextComponent("advancements.adventure.kill_all_mobs.title"), new TranslationTextComponent("advancements.adventure.kill_all_mobs.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).save(consumer, "adventure/kill_all_mobs");
		Advancement a4 = Advancement.Builder.advancement().parent(a3).display(Items.BOW, new TranslationTextComponent("advancements.adventure.shoot_arrow.title"), new TranslationTextComponent("advancements.adventure.shoot_arrow.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("shot_arrow", PlayerHurtEntityTrigger.Instance.playerHurtEntity(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().isProjectile(true).direct(EntityPredicate.Builder.entity().of(EntityTypeTags.ARROWS))))).save(consumer, "adventure/shoot_arrow");
		Advancement a5 = Advancement.Builder.advancement().parent(a3).display(Items.TRIDENT, new TranslationTextComponent("advancements.adventure.throw_trident.title"), new TranslationTextComponent("advancements.adventure.throw_trident.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("shot_trident", PlayerHurtEntityTrigger.Instance.playerHurtEntity(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().isProjectile(true).direct(EntityPredicate.Builder.entity().of(EntityType.TRIDENT))))).save(consumer, "adventure/throw_trident");
		Advancement.Builder.advancement().parent(a5).display(Items.TRIDENT, new TranslationTextComponent("advancements.adventure.very_very_frightening.title"), new TranslationTextComponent("advancements.adventure.very_very_frightening.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("struck_villager", ChanneledLightningTrigger.Instance.channeledLightning(EntityPredicate.Builder.entity().of(EntityType.VILLAGER).build())).save(consumer, "adventure/very_very_frightening");
		Advancement.Builder.advancement().parent(a2).display(Blocks.CARVED_PUMPKIN, new TranslationTextComponent("advancements.adventure.summon_iron_golem.title"), new TranslationTextComponent("advancements.adventure.summon_iron_golem.description"), (ResourceLocation)null, FrameType.GOAL, true, true, false).addCriterion("summoned_golem", SummonedEntityTrigger.Instance.summonedEntity(EntityPredicate.Builder.entity().of(EntityType.IRON_GOLEM))).save(consumer, "adventure/summon_iron_golem");
		Advancement.Builder.advancement().parent(a4).display(Items.ARROW, new TranslationTextComponent("advancements.adventure.sniper_duel.title"), new TranslationTextComponent("advancements.adventure.sniper_duel.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).addCriterion("killed_skeleton", KilledTrigger.Instance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.SKELETON).distance(DistancePredicate.horizontal(MinMaxBounds.FloatBound.atLeast(50.0F))), DamageSourcePredicate.Builder.damageType().isProjectile(true))).save(consumer, "adventure/sniper_duel");
		Advancement.Builder.advancement().parent(a3).display(Items.TOTEM_OF_UNDYING, new TranslationTextComponent("advancements.adventure.totem_of_undying.title"), new TranslationTextComponent("advancements.adventure.totem_of_undying.description"), (ResourceLocation)null, FrameType.GOAL, true, true, false).addCriterion("used_totem", UsedTotemTrigger.Instance.usedTotem(Items.TOTEM_OF_UNDYING)).save(consumer, "adventure/totem_of_undying");
		Advancement a6 = Advancement.Builder.advancement().parent(a0).display(Items.CROSSBOW, new TranslationTextComponent("advancements.adventure.ol_betsy.title"), new TranslationTextComponent("advancements.adventure.ol_betsy.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("shot_crossbow", ShotCrossbowTrigger.Instance.shotCrossbow(Items.CROSSBOW)).save(consumer, "adventure/ol_betsy");
		Advancement.Builder.advancement().parent(a6).display(Items.CROSSBOW, new TranslationTextComponent("advancements.adventure.whos_the_pillager_now.title"), new TranslationTextComponent("advancements.adventure.whos_the_pillager_now.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("kill_pillager", KilledByCrossbowTrigger.Instance.crossbowKilled(EntityPredicate.Builder.entity().of(EntityType.PILLAGER))).save(consumer, "adventure/whos_the_pillager_now");
		Advancement.Builder.advancement().parent(a6).display(Items.CROSSBOW, new TranslationTextComponent("advancements.adventure.two_birds_one_arrow.title"), new TranslationTextComponent("advancements.adventure.two_birds_one_arrow.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(65)).addCriterion("two_birds", KilledByCrossbowTrigger.Instance.crossbowKilled(EntityPredicate.Builder.entity().of(EntityType.PHANTOM), EntityPredicate.Builder.entity().of(EntityType.PHANTOM))).save(consumer, "adventure/two_birds_one_arrow");
		Advancement.Builder.advancement().parent(a6).display(Items.CROSSBOW, new TranslationTextComponent("advancements.adventure.arbalistic.title"), new TranslationTextComponent("advancements.adventure.arbalistic.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, true).rewards(AdvancementRewards.Builder.experience(85)).addCriterion("arbalistic", KilledByCrossbowTrigger.Instance.crossbowKilled(MinMaxBounds.IntBound.exactly(5))).save(consumer, "adventure/arbalistic");
		Advancement a7 = Advancement.Builder.advancement().parent(a0).display(Raid.getLeaderBannerInstance(), new TranslationTextComponent("advancements.adventure.voluntary_exile.title"), new TranslationTextComponent("advancements.adventure.voluntary_exile.description"), (ResourceLocation)null, FrameType.TASK, true, true, true).addCriterion("voluntary_exile", KilledTrigger.Instance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityTypeTags.RAIDERS).equipment(EntityEquipmentPredicate.CAPTAIN))).save(consumer, "adventure/voluntary_exile");
		Advancement.Builder.advancement().parent(a7).display(Raid.getLeaderBannerInstance(), new TranslationTextComponent("advancements.adventure.hero_of_the_village.title"), new TranslationTextComponent("advancements.adventure.hero_of_the_village.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, true).rewards(AdvancementRewards.Builder.experience(100)).addCriterion("hero_of_the_village", PositionTrigger.Instance.raidWon()).save(consumer, "adventure/hero_of_the_village");
		Advancement.Builder.advancement().parent(a0).display(Blocks.HONEY_BLOCK.asItem(), new TranslationTextComponent("advancements.adventure.honey_block_slide.title"), new TranslationTextComponent("advancements.adventure.honey_block_slide.description"), (ResourceLocation)null, FrameType.TASK, true, true, false).addCriterion("honey_block_slide", SlideDownBlockTrigger.Instance.slidesDownBlock(Blocks.HONEY_BLOCK)).save(consumer, "adventure/honey_block_slide");
		Advancement.Builder.advancement().parent(a4).display(Blocks.TARGET.asItem(), new TranslationTextComponent("advancements.adventure.bullseye.title"), new TranslationTextComponent("advancements.adventure.bullseye.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).addCriterion("bullseye", TargetHitTrigger.Instance.targetHit(MinMaxBounds.IntBound.exactly(15), EntityPredicate.AndPredicate.wrap(EntityPredicate.Builder.entity().distance(DistancePredicate.horizontal(MinMaxBounds.FloatBound.atLeast(30.0F))).build()))).save(consumer, "adventure/bullseye");
	
		Advancement.Builder.advancement().parent(a0).display(Items.FLINT_AND_STEEL, new TranslationTextComponent("advancements.almost_there.title"), new TranslationTextComponent("advancements.almost_there.description"), (ResourceLocation)null, FrameType.CHALLENGE, true, true, true).rewards(AdvancementRewards.Builder.experience(100)).addCriterion("almost_there", ChangeDimensionAccumulationTrigger.Instance.minimum(1)).save(consumer, "adventure/almost_there");
	}

}
