package com.samuraidragons;

import com.samuraidragons.entity.DragonMountEntity;
import com.samuraidragons.entity.OniShogunEntity;
import com.samuraidragons.entity.ShadowRoninEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SamuraiDragons.MOD_ID);

    public static final RegistryObject<EntityType<OniShogunEntity>> ONI_SHOGUN = ENTITIES.register("oni_shogun",
            () -> EntityType.Builder.of(OniShogunEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 3.4f).fireImmune().clientTrackingRange(10)
                    .build("oni_shogun"));

    public static final RegistryObject<EntityType<ShadowRoninEntity>> SHADOW_RONIN = ENTITIES.register("shadow_ronin",
            () -> EntityType.Builder.of(ShadowRoninEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f).clientTrackingRange(10)
                    .build("shadow_ronin"));

    public static final RegistryObject<EntityType<DragonMountEntity>> DRAGON = ENTITIES.register("dragon",
            () -> EntityType.Builder.of(DragonMountEntity::new, MobCategory.CREATURE)
                    .sized(2.0f, 1.6f).fireImmune().clientTrackingRange(10)
                    .build("dragon"));

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ONI_SHOGUN.get(), OniShogunEntity.createAttributes().build());
        event.put(SHADOW_RONIN.get(), ShadowRoninEntity.createAttributes().build());
        event.put(DRAGON.get(), DragonMountEntity.createAttributes().build());
    }
}
