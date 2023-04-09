package net.frozenblock.themoon.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.frozenblock.lib.item.api.FrozenCreativeTabs;
import net.frozenblock.themoon.block.properties.TheMoonBlockProperties;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MaterialColor;

public class TheMoonBlocks {

	public static final Block MOON_ROCK = new Block(TheMoonBlockProperties.MOON_ROCK_PROPERTIES);
	public static final Block MOON_ROCK_SLAB = new SlabBlock(TheMoonBlockProperties.MOON_ROCK_PROPERTIES);
	public static final Block MOON_ROCK_STAIRS = new StairBlock(MOON_ROCK.defaultBlockState(), TheMoonBlockProperties.MOON_ROCK_PROPERTIES);
	public static final Block MOON_ROCK_WALL = new WallBlock(TheMoonBlockProperties.MOON_ROCK_PROPERTIES);

	public static final Block POLISHED_MOON_ROCK = new Block(TheMoonBlockProperties.POLISHED_MOON_ROCK_PROPERTIES);
	public static final Block POLISHED_MOON_ROCK_SLAB = new SlabBlock(TheMoonBlockProperties.POLISHED_MOON_ROCK_PROPERTIES);
	public static final Block POLISHED_MOON_ROCK_STAIRS = new StairBlock(POLISHED_MOON_ROCK.defaultBlockState(), TheMoonBlockProperties.POLISHED_MOON_ROCK_PROPERTIES);

	//ORES
	public static final Block MOON_ROCK_COAL_ORE = new DropExperienceBlock(TheMoonBlockProperties.MOON_ROCK_ORE_PROPERTIES, UniformInt.of(0, 2));
	public static final Block MOON_ROCK_GOLD_ORE = new DropExperienceBlock(TheMoonBlockProperties.MOON_ROCK_ORE_PROPERTIES);
	public static final Block MOON_ROCK_IRON_ORE = new DropExperienceBlock(TheMoonBlockProperties.MOON_ROCK_ORE_PROPERTIES);
	public static final Block MOON_ROCK_LAPIS_ORE = new DropExperienceBlock(TheMoonBlockProperties.MOON_ROCK_ORE_PROPERTIES, UniformInt.of(2, 5));
	public static final Block MOON_ROCK_DIAMOND_ORE = new DropExperienceBlock(TheMoonBlockProperties.MOON_ROCK_ORE_PROPERTIES, UniformInt.of(3, 7));
	public static final Block MOON_ROCK_REDSTONE_ORE = new RedStoneOreBlock(TheMoonBlockProperties.MOON_ROCK_REDSTONE_ORE_PROPERTIES);
	public static final Block MOON_ROCK_EMERALD_ORE = new DropExperienceBlock(TheMoonBlockProperties.MOON_ROCK_ORE_PROPERTIES, UniformInt.of(3, 7));
	public static final Block MOON_ROCK_COPPER_ORE = new DropExperienceBlock(TheMoonBlockProperties.MOON_ROCK_ORE_PROPERTIES);

	public static void register() {
		registerBlock(true,"moon_rock", MOON_ROCK, CreativeModeTabs.NATURAL_BLOCKS, CreativeModeTabs.BUILDING_BLOCKS);
		registerBlock(true,"moon_rock_slab", MOON_ROCK_SLAB, CreativeModeTabs.BUILDING_BLOCKS);
		registerBlock(true,"moon_rock_stairs", MOON_ROCK_STAIRS, CreativeModeTabs.BUILDING_BLOCKS);
		registerBlock(true,"moon_rock_wall", MOON_ROCK_WALL, CreativeModeTabs.BUILDING_BLOCKS);

		registerBlock(true,"polished_moon_rock", POLISHED_MOON_ROCK, CreativeModeTabs.BUILDING_BLOCKS);
		registerBlock(true,"polished_moon_rock_slab", POLISHED_MOON_ROCK_SLAB, CreativeModeTabs.BUILDING_BLOCKS);
		registerBlock(true,"polished_moon_rock_stairs", POLISHED_MOON_ROCK_STAIRS, CreativeModeTabs.BUILDING_BLOCKS);

		registerBlock(true,"moon_rock_coal_ore", MOON_ROCK_COAL_ORE, CreativeModeTabs.NATURAL_BLOCKS);
		registerBlock(true,"moon_rock_gold_ore", MOON_ROCK_GOLD_ORE, CreativeModeTabs.NATURAL_BLOCKS);
		registerBlock(true,"moon_rock_iron_ore", MOON_ROCK_IRON_ORE, CreativeModeTabs.NATURAL_BLOCKS);
		registerBlock(true,"moon_rock_lapis_ore", MOON_ROCK_LAPIS_ORE, CreativeModeTabs.NATURAL_BLOCKS);
		registerBlock(true,"moon_rock_diamond_ore", MOON_ROCK_DIAMOND_ORE, CreativeModeTabs.NATURAL_BLOCKS);
		registerBlock(true,"moon_rock_redstone_ore", MOON_ROCK_REDSTONE_ORE, CreativeModeTabs.NATURAL_BLOCKS);
		registerBlock(true,"moon_rock_emerald_ore", MOON_ROCK_EMERALD_ORE, CreativeModeTabs.NATURAL_BLOCKS);
		registerBlock(true,"moon_rock_copper_ore", MOON_ROCK_COPPER_ORE, CreativeModeTabs.NATURAL_BLOCKS);
	}

	private static void registerBlock(boolean registerBlockItem, String path, Block block, CreativeModeTab... tabs) {
		if (registerBlockItem) {
			registerBlockItem(path, block, tabs);
		}
		actualRegisterBlock(path, block);
	}

	private static void registerBlockBefore(ItemLike comparedItem, String path, Block block, CreativeModeTab... tabs) {
		registerBlockItemBefore(comparedItem, path, block, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
		actualRegisterBlock(path, block);
	}

	private static void registerBlockAfter(ItemLike comparedItem, String path, Block block, CreativeModeTab... tabs) {
		registerBlockItemAfter(comparedItem, path, block, tabs);
		actualRegisterBlock(path, block);
	}

	private static void registerBlockItem(String path, Block block, CreativeModeTab... tabs) {
		actualRegisterBlockItem(path, block);
		FrozenCreativeTabs.add(block, tabs);
	}

	private static void registerBlockItemBefore(ItemLike comparedItem, String name, Block block, CreativeModeTab... tabs) {
		registerBlockItemBefore(comparedItem, name, block, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
	}

	private static void registerBlockItemBefore(ItemLike comparedItem, String path, Block block, CreativeModeTab.TabVisibility tabVisibility, CreativeModeTab... tabs) {
		actualRegisterBlockItem(path, block);
		FrozenCreativeTabs.addBefore(comparedItem, block, tabVisibility, tabs);
	}

	private static void registerBlockItemAfter(ItemLike comparedItem, String name, Block block, CreativeModeTab... tabs) {
		registerBlockItemAfter(comparedItem, name, block, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
	}

	private static void registerBlockItemAfter(ItemLike comparedItem, String path, Block block, CreativeModeTab.TabVisibility visibility, CreativeModeTab... tabs) {
		actualRegisterBlockItem(path, block);
		FrozenCreativeTabs.addAfter(comparedItem, block, visibility, tabs);
	}

	private static void actualRegisterBlock(String path, Block block) {
		if (BuiltInRegistries.BLOCK.getOptional(TheMoonSharedConstants.id(path)).isEmpty()) {
			Registry.register(BuiltInRegistries.BLOCK, TheMoonSharedConstants.id(path), block);
		}
	}

	private static void actualRegisterBlockItem(String path, Block block) {
		if (BuiltInRegistries.ITEM.getOptional(TheMoonSharedConstants.id(path)).isEmpty()) {
			Registry.register(BuiltInRegistries.ITEM, TheMoonSharedConstants.id(path), new BlockItem(block, new FabricItemSettings()));
		}
	}
}
