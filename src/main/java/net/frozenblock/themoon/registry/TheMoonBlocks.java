package net.frozenblock.themoon.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.frozenblock.lib.item.api.FrozenCreativeTabs;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

public class TheMoonBlocks {

	public static final Block MOON_ROCK = new Block(BlockBehaviour.Properties.copy(Blocks.CALCITE).color(MaterialColor.COLOR_LIGHT_GRAY));

	public static void register() {
		registerBlockAfter(Items.STONE,"moon_rock", MOON_ROCK, CreativeModeTabs.NATURAL_BLOCKS);
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
