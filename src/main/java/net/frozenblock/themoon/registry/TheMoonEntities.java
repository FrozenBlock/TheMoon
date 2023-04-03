package net.frozenblock.themoon.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.frozenblock.lib.mobcategory.api.FrozenMobCategories;
import net.frozenblock.themoon.entity.Asteroid;
import net.frozenblock.themoon.util.TheMoonSharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public final class TheMoonEntities {

	public static final EntityType<Asteroid> ASTEROID = register(
			"asteroid",
			FabricEntityTypeBuilder.createMob()
					.spawnGroup(FrozenMobCategories.getCategory(TheMoonSharedConstants.MOD_ID, "asteroids"))
					.entityFactory(Asteroid::new)
					.defaultAttributes(Asteroid::addAttributes)
					.spawnRestriction(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Asteroid::canSpawn)
					.dimensions(EntityDimensions.scalable(0.98F, 0.98F))
					.build()
	);

    public static void init() {
        TheMoonSharedConstants.logMod("Registering Entities for", true);
    }

    private static <E extends Entity, T extends EntityType<E>> T register(String path, T entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, TheMoonSharedConstants.id(path), entityType);
    }
}
