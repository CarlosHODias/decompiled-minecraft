/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SecondaryPoiSensor
/*    */   extends Sensor<Villager> {
/*    */   public SecondaryPoiSensor() {
/* 21 */     super(40);
/*    */   }
/*    */   private static final int SCAN_RATE = 40;
/*    */   
/*    */   protected void doTick(ServerLevel level, Villager body) {
/* 26 */     ResourceKey<Level> dimensionType = level.dimension();
/* 27 */     BlockPos center = body.blockPosition();
/* 28 */     List<GlobalPos> jobSites = Lists.newArrayList();
/*    */     
/* 30 */     int horizontalSearch = 4;
/* 31 */     for (int x = -4; x <= 4; x++) {
/* 32 */       for (int y = -2; y <= 2; y++) {
/* 33 */         for (int z = -4; z <= 4; z++) {
/* 34 */           BlockPos testPos = center.offset(x, y, z);
/* 35 */           if (((VillagerProfession)body.getVillagerData().profession().value()).secondaryPoi().contains(level.getBlockState(testPos).getBlock())) {
/* 36 */             jobSites.add(GlobalPos.of(dimensionType, testPos));
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 42 */     Brain<?> brain = body.getBrain();
/* 43 */     if (!jobSites.isEmpty()) {
/* 44 */       brain.setMemory(MemoryModuleType.SECONDARY_JOB_SITE, jobSites);
/*    */     } else {
/* 46 */       brain.eraseMemory(MemoryModuleType.SECONDARY_JOB_SITE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 52 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.SECONDARY_JOB_SITE);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/SecondaryPoiSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */