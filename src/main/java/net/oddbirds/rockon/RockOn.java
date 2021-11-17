package net.oddbirds.rockon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.resources.IResource;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(RockOn.MODID)
public class RockOn {
	
	public static final String MODID = "rockon";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	
	public static final ResourceLocation CHANGE_DIMENSION = registerStat("change_dimension",IStatFormatter.DEFAULT);;
	
	public RockOn() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		
		ITEMS.register("illusioner_spawn_egg", () -> new SpawnEggItem(EntityType.ILLUSIONER, 23188, 7896445, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	private static ResourceLocation registerStat(String name, IStatFormatter formatter) {
		LOGGER.info("Registered Stat: "+name);
		ResourceLocation resource = new ResourceLocation(MODID, name);
		Registry.register(Registry.CUSTOM_STAT,resource,resource);
		Stats.CUSTOM.get(resource, formatter);
		return resource;
	}
	
	private void commonSetup(FMLCommonSetupEvent event) {
		Raid.WaveMember.create("illusioner", EntityType.ILLUSIONER, new int[]{0,0,2,0,1,4,2,5});
		OverworldBiomeProvider.POSSIBLE_BIOMES = OverworldBiomeProvider.POSSIBLE_BIOMES.stream().collect(Collectors.toList());
		OverworldBiomeProvider.POSSIBLE_BIOMES.add(Biomes.DEEP_WARM_OCEAN);
		OverworldBiomeProvider.POSSIBLE_BIOMES.add(Biomes.MOUNTAIN_EDGE);
	}
	
	private void clientSetup(FMLClientSetupEvent event) {
		try {
			IResource splashes = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(MODID,"texts/splashes.txt"));
			BufferedReader reader = new BufferedReader(new InputStreamReader(splashes.getInputStream()));
			reader.lines().forEach(l->Minecraft.getInstance().getSplashManager().splashes.add(l));
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Unable to load custom splashes. This feature will be disabled.");
		}
	}

}
