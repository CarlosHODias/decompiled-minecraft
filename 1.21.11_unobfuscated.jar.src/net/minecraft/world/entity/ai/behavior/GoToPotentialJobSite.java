/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GoToPotentialJobSite
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   private static final int TICKS_UNTIL_TIMEOUT = 1200;
/*    */   final float speedModifier;
/*    */   
/*    */   public GoToPotentialJobSite(float speedModifier) {
/* 28 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT), 1200);
/*    */ 
/*    */     
/* 31 */     this.speedModifier = speedModifier;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/* 36 */     return (Boolean)body.getBrain().getActiveNonCoreActivity().map(activity -> (activity == Activity.IDLE || activity == Activity.WORK || activity == Activity.PLAY)).orElse(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/* 42 */     return body.getBrain().hasMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 47 */     BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)body, ((GlobalPos)body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos(), this.speedModifier, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 52 */     Optional<GlobalPos> potentialJobSitePos = body.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/* 53 */     potentialJobSitePos.ifPresent(globalPos -> {
/*    */           BlockPos pos = globalPos.pos();
/*    */           ServerLevel serverLevel = level.getServer().getLevel(globalPos.dimension());
/*    */           if (serverLevel == null) {
/*    */             return;
/*    */           }
/*    */           PoiManager manager = serverLevel.getPoiManager();
/*    */           if (manager.exists(pos, ())) {
/*    */             manager.release(pos);
/*    */           }
/*    */           level.debugSynchronizers().updatePoi(pos);
/*    */         });
/* 65 */     body.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/GoToPotentialJobSite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */