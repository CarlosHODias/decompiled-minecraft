/*    */ package net.minecraft.world.entity.animal.wolf;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ 
/*    */ public class WolfVariants {
/* 17 */   public static final ResourceKey<WolfVariant> PALE = createKey("pale");
/* 18 */   public static final ResourceKey<WolfVariant> SPOTTED = createKey("spotted");
/* 19 */   public static final ResourceKey<WolfVariant> SNOWY = createKey("snowy");
/* 20 */   public static final ResourceKey<WolfVariant> BLACK = createKey("black");
/* 21 */   public static final ResourceKey<WolfVariant> ASHEN = createKey("ashen");
/* 22 */   public static final ResourceKey<WolfVariant> RUSTY = createKey("rusty");
/* 23 */   public static final ResourceKey<WolfVariant> WOODS = createKey("woods");
/* 24 */   public static final ResourceKey<WolfVariant> CHESTNUT = createKey("chestnut");
/* 25 */   public static final ResourceKey<WolfVariant> STRIPED = createKey("striped");
/*    */   
/* 27 */   public static final ResourceKey<WolfVariant> DEFAULT = PALE;
/*    */   
/*    */   private static ResourceKey<WolfVariant> createKey(String name) {
/* 30 */     return ResourceKey.create(Registries.WOLF_VARIANT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> context, ResourceKey<WolfVariant> name, String fileName, ResourceKey<Biome> spawnBiome) {
/* 34 */     register(context, name, fileName, highPrioBiome((HolderSet<Biome>)HolderSet.direct(new Holder[] { (Holder)context.lookup(Registries.BIOME).getOrThrow(spawnBiome) })));
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> context, ResourceKey<WolfVariant> name, String fileName, TagKey<Biome> spawnBiome) {
/* 38 */     register(context, name, fileName, highPrioBiome((HolderSet<Biome>)context.lookup(Registries.BIOME).getOrThrow(spawnBiome)));
/*    */   }
/*    */   
/*    */   private static SpawnPrioritySelectors highPrioBiome(HolderSet<Biome> biomes) {
/* 42 */     return SpawnPrioritySelectors.single((SpawnCondition)new net.minecraft.world.entity.variant.BiomeCheck(biomes), 1);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<WolfVariant> context, ResourceKey<WolfVariant> name, String fileName, SpawnPrioritySelectors selectors) {
/* 46 */     Identifier wildTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName);
/* 47 */     Identifier tameTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName + "_tame");
/* 48 */     Identifier angryTexture = Identifier.withDefaultNamespace("entity/wolf/" + fileName + "_angry");
/* 49 */     context.register(name, new WolfVariant(new WolfVariant.AssetInfo(new ClientAsset.ResourceTexture(wildTexture), new ClientAsset.ResourceTexture(tameTexture), new ClientAsset.ResourceTexture(angryTexture)), selectors));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void bootstrap(BootstrapContext<WolfVariant> context) {
/* 60 */     register(context, PALE, "wolf", SpawnPrioritySelectors.fallback(0));
/* 61 */     register(context, SPOTTED, "wolf_spotted", BiomeTags.IS_SAVANNA);
/* 62 */     register(context, SNOWY, "wolf_snowy", Biomes.GROVE);
/* 63 */     register(context, BLACK, "wolf_black", Biomes.OLD_GROWTH_PINE_TAIGA);
/* 64 */     register(context, ASHEN, "wolf_ashen", Biomes.SNOWY_TAIGA);
/* 65 */     register(context, RUSTY, "wolf_rusty", BiomeTags.IS_JUNGLE);
/* 66 */     register(context, WOODS, "wolf_woods", Biomes.FOREST);
/* 67 */     register(context, CHESTNUT, "wolf_chestnut", Biomes.OLD_GROWTH_SPRUCE_TAIGA);
/* 68 */     register(context, STRIPED, "wolf_striped", BiomeTags.IS_BADLANDS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/wolf/WolfVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */