/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public final class VillagerProfession extends Record {
/*    */   private final net.minecraft.network.chat.Component name;
/*    */   private final Predicate<Holder<PoiType>> heldJobSite;
/*    */   private final Predicate<Holder<PoiType>> acquirableJobSite;
/*    */   private final ImmutableSet<net.minecraft.world.item.Item> requestedItems;
/*    */   private final ImmutableSet<Block> secondaryPoi;
/*    */   private final SoundEvent workSound;
/*    */   public static final Predicate<Holder<PoiType>> ALL_ACQUIRABLE_JOBS;
/*    */   
/* 23 */   public VillagerProfession(net.minecraft.network.chat.Component name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<net.minecraft.world.item.Item> requestedItems, ImmutableSet<Block> secondaryPoi, SoundEvent workSound) { this.name = name; this.heldJobSite = heldJobSite; this.acquirableJobSite = acquirableJobSite; this.requestedItems = requestedItems; this.secondaryPoi = secondaryPoi; this.workSound = workSound; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/npc/villager/VillagerProfession;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerProfession; } public net.minecraft.network.chat.Component name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/npc/villager/VillagerProfession;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerProfession; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/npc/villager/VillagerProfession;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerProfession;
/* 23 */     //   0	8	1	o	Ljava/lang/Object; } public Predicate<Holder<PoiType>> heldJobSite() { return this.heldJobSite; } public Predicate<Holder<PoiType>> acquirableJobSite() { return this.acquirableJobSite; } public ImmutableSet<net.minecraft.world.item.Item> requestedItems() { return this.requestedItems; } public ImmutableSet<Block> secondaryPoi() { return this.secondaryPoi; } public SoundEvent workSound() { return this.workSound; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 31 */     ALL_ACQUIRABLE_JOBS = (holder -> holder.is(net.minecraft.tags.PoiTypeTags.ACQUIRABLE_JOB_SITE));
/*    */   }
/* 33 */   public static final ResourceKey<VillagerProfession> NONE = createKey("none");
/* 34 */   public static final ResourceKey<VillagerProfession> ARMORER = createKey("armorer");
/* 35 */   public static final ResourceKey<VillagerProfession> BUTCHER = createKey("butcher");
/* 36 */   public static final ResourceKey<VillagerProfession> CARTOGRAPHER = createKey("cartographer");
/* 37 */   public static final ResourceKey<VillagerProfession> CLERIC = createKey("cleric");
/* 38 */   public static final ResourceKey<VillagerProfession> FARMER = createKey("farmer");
/* 39 */   public static final ResourceKey<VillagerProfession> FISHERMAN = createKey("fisherman");
/* 40 */   public static final ResourceKey<VillagerProfession> FLETCHER = createKey("fletcher");
/* 41 */   public static final ResourceKey<VillagerProfession> LEATHERWORKER = createKey("leatherworker");
/* 42 */   public static final ResourceKey<VillagerProfession> LIBRARIAN = createKey("librarian");
/* 43 */   public static final ResourceKey<VillagerProfession> MASON = createKey("mason");
/* 44 */   public static final ResourceKey<VillagerProfession> NITWIT = createKey("nitwit");
/* 45 */   public static final ResourceKey<VillagerProfession> SHEPHERD = createKey("shepherd");
/* 46 */   public static final ResourceKey<VillagerProfession> TOOLSMITH = createKey("toolsmith");
/* 47 */   public static final ResourceKey<VillagerProfession> WEAPONSMITH = createKey("weaponsmith");
/*    */   
/*    */   private static ResourceKey<VillagerProfession> createKey(String name) {
/* 50 */     return ResourceKey.create(net.minecraft.core.registries.Registries.VILLAGER_PROFESSION, net.minecraft.resources.Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, ResourceKey<PoiType> jobSite, SoundEvent workSound) {
/* 54 */     return register(registry, name, poiType -> poiType.is(jobSite), poiType -> poiType.is(jobSite), workSound);
/*    */   }
/*    */   
/*    */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, SoundEvent workSound) {
/* 58 */     return register(registry, name, heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), workSound);
/*    */   }
/*    */   
/*    */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, ResourceKey<PoiType> jobSite, ImmutableSet<net.minecraft.world.item.Item> requestedItems, ImmutableSet<Block> secondaryPoi, SoundEvent workSound) {
/* 62 */     return register(registry, name, poiType -> poiType.is(jobSite), poiType -> poiType.is(jobSite), requestedItems, secondaryPoi, workSound);
/*    */   }
/*    */   
/*    */   private static VillagerProfession register(Registry<VillagerProfession> registry, ResourceKey<VillagerProfession> name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<net.minecraft.world.item.Item> requestedItems, ImmutableSet<Block> secondaryPoi, SoundEvent workSound) {
/* 66 */     return (VillagerProfession)Registry.register(registry, name, new VillagerProfession(
/* 67 */           (net.minecraft.network.chat.Component)net.minecraft.network.chat.Component.translatable("entity." + name.identifier().getNamespace() + ".villager." + name.identifier().getPath()), heldJobSite, acquirableJobSite, requestedItems, secondaryPoi, workSound));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static VillagerProfession bootstrap(Registry<VillagerProfession> registry) {
/* 77 */     register(registry, NONE, PoiType.NONE, ALL_ACQUIRABLE_JOBS, null);
/* 78 */     register(registry, ARMORER, PoiTypes.ARMORER, SoundEvents.VILLAGER_WORK_ARMORER);
/* 79 */     register(registry, BUTCHER, PoiTypes.BUTCHER, SoundEvents.VILLAGER_WORK_BUTCHER);
/* 80 */     register(registry, CARTOGRAPHER, PoiTypes.CARTOGRAPHER, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
/* 81 */     register(registry, CLERIC, PoiTypes.CLERIC, SoundEvents.VILLAGER_WORK_CLERIC);
/* 82 */     register(registry, FARMER, PoiTypes.FARMER, ImmutableSet.of(net.minecraft.world.item.Items.WHEAT, net.minecraft.world.item.Items.WHEAT_SEEDS, net.minecraft.world.item.Items.BEETROOT_SEEDS, net.minecraft.world.item.Items.BONE_MEAL), ImmutableSet.of(net.minecraft.world.level.block.Blocks.FARMLAND), SoundEvents.VILLAGER_WORK_FARMER);
/* 83 */     register(registry, FISHERMAN, PoiTypes.FISHERMAN, SoundEvents.VILLAGER_WORK_FISHERMAN);
/* 84 */     register(registry, FLETCHER, PoiTypes.FLETCHER, SoundEvents.VILLAGER_WORK_FLETCHER);
/* 85 */     register(registry, LEATHERWORKER, PoiTypes.LEATHERWORKER, SoundEvents.VILLAGER_WORK_LEATHERWORKER);
/* 86 */     register(registry, LIBRARIAN, PoiTypes.LIBRARIAN, SoundEvents.VILLAGER_WORK_LIBRARIAN);
/* 87 */     register(registry, MASON, PoiTypes.MASON, SoundEvents.VILLAGER_WORK_MASON);
/* 88 */     register(registry, NITWIT, PoiType.NONE, PoiType.NONE, null);
/* 89 */     register(registry, SHEPHERD, PoiTypes.SHEPHERD, SoundEvents.VILLAGER_WORK_SHEPHERD);
/* 90 */     register(registry, TOOLSMITH, PoiTypes.TOOLSMITH, SoundEvents.VILLAGER_WORK_TOOLSMITH);
/* 91 */     return register(registry, WEAPONSMITH, PoiTypes.WEAPONSMITH, SoundEvents.VILLAGER_WORK_WEAPONSMITH);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/npc/villager/VillagerProfession.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */