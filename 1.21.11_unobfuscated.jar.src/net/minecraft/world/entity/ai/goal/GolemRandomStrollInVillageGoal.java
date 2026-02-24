/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.level.entity.EntityAccess;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GolemRandomStrollInVillageGoal extends RandomStrollGoal {
/*     */   private static final int POI_SECTION_SCAN_RADIUS = 2;
/*     */   private static final int VILLAGER_SCAN_RADIUS = 32;
/*     */   
/*     */   public GolemRandomStrollInVillageGoal(PathfinderMob mob, double speedModifier) {
/*  25 */     super(mob, speedModifier, 240, false);
/*     */   }
/*     */   private static final int RANDOM_POS_XY_DISTANCE = 10; private static final int RANDOM_POS_Y_DISTANCE = 7;
/*     */   
/*     */   protected Vec3 getPosition() {
/*     */     Vec3 target;
/*  31 */     float randomValue = (this.mob.level()).random.nextFloat();
/*  32 */     if ((this.mob.level()).random.nextFloat() < 0.3F) {
/*  33 */       return getPositionTowardsAnywhere();
/*     */     }
/*     */     
/*  36 */     if (randomValue < 0.7F) {
/*  37 */       target = getPositionTowardsVillagerWhoWantsGolem();
/*  38 */       if (target == null) {
/*  39 */         target = getPositionTowardsPoi();
/*     */       }
/*     */     } else {
/*  42 */       target = getPositionTowardsPoi();
/*  43 */       if (target == null) {
/*  44 */         target = getPositionTowardsVillagerWhoWantsGolem();
/*     */       }
/*     */     } 
/*     */     
/*  48 */     return (target == null) ? getPositionTowardsAnywhere() : target;
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsAnywhere() {
/*  52 */     return LandRandomPos.getPos(this.mob, 10, 7);
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsVillagerWhoWantsGolem() {
/*  56 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  57 */     List<Villager> villagers = level.getEntities((EntityTypeTest)EntityType.VILLAGER, this.mob.getBoundingBox().inflate(32.0D), this::doesVillagerWantGolem);
/*  58 */     if (villagers.isEmpty()) {
/*  59 */       return null;
/*     */     }
/*  61 */     Villager villager = villagers.get((this.mob.level()).random.nextInt(villagers.size()));
/*  62 */     Vec3 targetPos = villager.position();
/*  63 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, targetPos);
/*     */   }
/*     */   
/*     */   private Vec3 getPositionTowardsPoi() {
/*  67 */     SectionPos targetSection = getRandomVillageSection();
/*  68 */     if (targetSection == null) {
/*  69 */       return null;
/*     */     }
/*     */     
/*  72 */     BlockPos targetPos = getRandomPoiWithinSection(targetSection);
/*  73 */     if (targetPos == null)
/*     */     {
/*  75 */       return null;
/*     */     }
/*     */     
/*  78 */     return LandRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)targetPos));
/*     */   }
/*     */   
/*     */   private SectionPos getRandomVillageSection() {
/*  82 */     ServerLevel level = (ServerLevel)this.mob.level();
/*     */     
/*  84 */     List<SectionPos> villageSections = (List<SectionPos>)SectionPos.cube(SectionPos.of((EntityAccess)this.mob), 2)
/*  85 */       .filter(sectionPos -> (level.sectionsToVillage(sectionPos) == 0))
/*  86 */       .collect(Collectors.toList());
/*     */     
/*  88 */     if (villageSections.isEmpty()) {
/*  89 */       return null;
/*     */     }
/*  91 */     return villageSections.get(level.random.nextInt(villageSections.size()));
/*     */   }
/*     */   
/*     */   private BlockPos getRandomPoiWithinSection(SectionPos sectionPos) {
/*  95 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  96 */     PoiManager poiManager = level.getPoiManager();
/*  97 */     List<BlockPos> pois = (List<BlockPos>)poiManager.getInRange(poiType -> true, sectionPos.center(), 8, PoiManager.Occupancy.IS_OCCUPIED)
/*  98 */       .map(PoiRecord::getPos)
/*  99 */       .collect(Collectors.toList());
/*     */     
/* 101 */     if (pois.isEmpty()) {
/* 102 */       return null;
/*     */     }
/* 104 */     return pois.get(level.random.nextInt(pois.size()));
/*     */   }
/*     */   
/*     */   private boolean doesVillagerWantGolem(Villager villager) {
/* 108 */     return villager.wantsToSpawnGolem(this.mob.level().getGameTime());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/GolemRandomStrollInVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */