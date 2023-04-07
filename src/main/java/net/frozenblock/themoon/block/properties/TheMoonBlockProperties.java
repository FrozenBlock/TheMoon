package net.frozenblock.themoon.block.properties;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class TheMoonBlockProperties {
	//TODO: Moon Rock Sound Type
	public static final BlockBehaviour.Properties MOON_ROCK_PROPERTIES = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.BASALT).requiresCorrectToolForDrops().strength(0.75F);
	//TODO: Polished Moon Rock Sound Type
	public static final BlockBehaviour.Properties POLISHED_MOON_ROCK_PROPERTIES = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.BASALT).requiresCorrectToolForDrops().strength(0.75F);

}
