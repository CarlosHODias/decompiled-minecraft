/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public class EntityIdFix extends com.mojang.datafixers.DataFix {
/*     */   public EntityIdFix(Schema outputSchema, boolean changesType) {
/*  15 */     super(outputSchema, changesType);
/*     */   } private static final Map<String, String> ID_MAP;
/*     */   static {
/*  18 */     ID_MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           map.put("AreaEffectCloud", "minecraft:area_effect_cloud");
/*     */           map.put("ArmorStand", "minecraft:armor_stand");
/*     */           map.put("Arrow", "minecraft:arrow");
/*     */           map.put("Bat", "minecraft:bat");
/*     */           map.put("Blaze", "minecraft:blaze");
/*     */           map.put("Boat", "minecraft:boat");
/*     */           map.put("CaveSpider", "minecraft:cave_spider");
/*     */           map.put("Chicken", "minecraft:chicken");
/*     */           map.put("Cow", "minecraft:cow");
/*     */           map.put("Creeper", "minecraft:creeper");
/*     */           map.put("Donkey", "minecraft:donkey");
/*     */           map.put("DragonFireball", "minecraft:dragon_fireball");
/*     */           map.put("ElderGuardian", "minecraft:elder_guardian");
/*     */           map.put("EnderCrystal", "minecraft:ender_crystal");
/*     */           map.put("EnderDragon", "minecraft:ender_dragon");
/*     */           map.put("Enderman", "minecraft:enderman");
/*     */           map.put("Endermite", "minecraft:endermite");
/*     */           map.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
/*     */           map.put("FallingSand", "minecraft:falling_block");
/*     */           map.put("Fireball", "minecraft:fireball");
/*     */           map.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
/*     */           map.put("Ghast", "minecraft:ghast");
/*     */           map.put("Giant", "minecraft:giant");
/*     */           map.put("Guardian", "minecraft:guardian");
/*     */           map.put("Horse", "minecraft:horse");
/*     */           map.put("Husk", "minecraft:husk");
/*     */           map.put("Item", "minecraft:item");
/*     */           map.put("ItemFrame", "minecraft:item_frame");
/*     */           map.put("LavaSlime", "minecraft:magma_cube");
/*     */           map.put("LeashKnot", "minecraft:leash_knot");
/*     */           map.put("MinecartChest", "minecraft:chest_minecart");
/*     */           map.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
/*     */           map.put("MinecartFurnace", "minecraft:furnace_minecart");
/*     */           map.put("MinecartHopper", "minecraft:hopper_minecart");
/*     */           map.put("MinecartRideable", "minecraft:minecart");
/*     */           map.put("MinecartSpawner", "minecraft:spawner_minecart");
/*     */           map.put("MinecartTNT", "minecraft:tnt_minecart");
/*     */           map.put("Mule", "minecraft:mule");
/*     */           map.put("MushroomCow", "minecraft:mooshroom");
/*     */           map.put("Ozelot", "minecraft:ocelot");
/*     */           map.put("Painting", "minecraft:painting");
/*     */           map.put("Pig", "minecraft:pig");
/*     */           map.put("PigZombie", "minecraft:zombie_pigman");
/*     */           map.put("PolarBear", "minecraft:polar_bear");
/*     */           map.put("PrimedTnt", "minecraft:tnt");
/*     */           map.put("Rabbit", "minecraft:rabbit");
/*     */           map.put("Sheep", "minecraft:sheep");
/*     */           map.put("Shulker", "minecraft:shulker");
/*     */           map.put("ShulkerBullet", "minecraft:shulker_bullet");
/*     */           map.put("Silverfish", "minecraft:silverfish");
/*     */           map.put("Skeleton", "minecraft:skeleton");
/*     */           map.put("SkeletonHorse", "minecraft:skeleton_horse");
/*     */           map.put("Slime", "minecraft:slime");
/*     */           map.put("SmallFireball", "minecraft:small_fireball");
/*     */           map.put("SnowMan", "minecraft:snowman");
/*     */           map.put("Snowball", "minecraft:snowball");
/*     */           map.put("SpectralArrow", "minecraft:spectral_arrow");
/*     */           map.put("Spider", "minecraft:spider");
/*     */           map.put("Squid", "minecraft:squid");
/*     */           map.put("Stray", "minecraft:stray");
/*     */           map.put("ThrownEgg", "minecraft:egg");
/*     */           map.put("ThrownEnderpearl", "minecraft:ender_pearl");
/*     */           map.put("ThrownExpBottle", "minecraft:xp_bottle");
/*     */           map.put("ThrownPotion", "minecraft:potion");
/*     */           map.put("Villager", "minecraft:villager");
/*     */           map.put("VillagerGolem", "minecraft:villager_golem");
/*     */           map.put("Witch", "minecraft:witch");
/*     */           map.put("WitherBoss", "minecraft:wither");
/*     */           map.put("WitherSkeleton", "minecraft:wither_skeleton");
/*     */           map.put("WitherSkull", "minecraft:wither_skull");
/*     */           map.put("Wolf", "minecraft:wolf");
/*     */           map.put("XPOrb", "minecraft:xp_orb");
/*     */           map.put("Zombie", "minecraft:zombie");
/*     */           map.put("ZombieHorse", "minecraft:zombie_horse");
/*     */           map.put("ZombieVillager", "minecraft:zombie_villager");
/*     */         });
/*     */   }
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  98 */     TaggedChoice.TaggedChoiceType<String> oldType = getInputSchema().findChoiceType(References.ENTITY);
/*  99 */     TaggedChoice.TaggedChoiceType<String> newType = getOutputSchema().findChoiceType(References.ENTITY);
/*     */     
/* 101 */     Type<?> oldItemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 102 */     Type<?> newItemStackType = getOutputSchema().getType(References.ITEM_STACK);
/*     */     
/* 104 */     return TypeRewriteRule.seq(
/* 105 */         convertUnchecked("item stack entity name hook converter", oldItemStackType, newItemStackType), 
/* 106 */         fixTypeEverywhere("EntityIdFix", (Type)oldType, (Type)newType, ops -> ()));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */