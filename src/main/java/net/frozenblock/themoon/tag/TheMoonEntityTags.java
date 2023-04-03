package net.frozenblock.themoon.tag;

import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class TheMoonEntityTags {
	public static final TagKey<EntityType<?>> NOT_AFFECTED_BY_GRAVITY = bind("not_affected_by_gravity");

	private static TagKey<EntityType<?>> bind(String path) {
		return TagKey.create(Registries.ENTITY_TYPE, TheMoonSharedConstants.id(path));
	}
}
