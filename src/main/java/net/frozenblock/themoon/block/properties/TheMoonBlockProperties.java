package net.frozenblock.themoon.block.properties;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class TheMoonBlockProperties {
	public static final float MOON_ROCK_STRENGTH = 0.75F;
	public static final float MOON_ROCK_ORE_STRENGTH = 2.25F;
	public static final float MOON_ROCK_ORE_RESISTANCE = 2.25F;
	public static final SoundType MOON_ROCK_SOUND_TYPE = SoundType.BASALT;
	public static final SoundType POLISHED_MOON_ROCK_SOUND_TYPE = SoundType.BASALT;
	public static final Material MOON_ROCK_MATERIAL = Material.STONE;
	public static final MaterialColor MOON_ROCK_MATERIAL_COLOR = MaterialColor.COLOR_LIGHT_GRAY;

	//TODO: Moon Rock Sound Type
	public static final BlockBehaviour.Properties MOON_ROCK_PROPERTIES = BlockBehaviour.Properties.of(MOON_ROCK_MATERIAL, MOON_ROCK_MATERIAL_COLOR).sound(MOON_ROCK_SOUND_TYPE).requiresCorrectToolForDrops().strength(MOON_ROCK_STRENGTH);
	//TODO: Polished Moon Rock Sound Type
	public static final BlockBehaviour.Properties POLISHED_MOON_ROCK_PROPERTIES = BlockBehaviour.Properties.of(MOON_ROCK_MATERIAL, MOON_ROCK_MATERIAL_COLOR).sound(POLISHED_MOON_ROCK_SOUND_TYPE).requiresCorrectToolForDrops().strength(MOON_ROCK_STRENGTH);
	//TODO: Moon Rock Sound Type
	public static final BlockBehaviour.Properties MOON_ROCK_ORE_PROPERTIES = BlockBehaviour.Properties.of(MOON_ROCK_MATERIAL, MOON_ROCK_MATERIAL_COLOR).sound(MOON_ROCK_SOUND_TYPE).requiresCorrectToolForDrops().strength(MOON_ROCK_ORE_STRENGTH, MOON_ROCK_ORE_RESISTANCE);
	//TODO: Moon Rock Sound Type
	public static final BlockBehaviour.Properties MOON_ROCK_REDSTONE_ORE_PROPERTIES = BlockBehaviour.Properties.of(MOON_ROCK_MATERIAL, MOON_ROCK_MATERIAL_COLOR).sound(MOON_ROCK_SOUND_TYPE).lightLevel(Blocks.litBlockEmission(9)).requiresCorrectToolForDrops().strength(MOON_ROCK_ORE_STRENGTH, MOON_ROCK_ORE_RESISTANCE);
}
