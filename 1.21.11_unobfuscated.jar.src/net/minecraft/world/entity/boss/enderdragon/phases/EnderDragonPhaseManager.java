/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class EnderDragonPhaseManager
/*    */ {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final EnderDragon dragon;
/* 14 */   private final DragonPhaseInstance[] phases = new DragonPhaseInstance[EnderDragonPhase.getCount()];
/*    */   private DragonPhaseInstance currentPhase;
/*    */   
/*    */   public EnderDragonPhaseManager(EnderDragon dragon) {
/* 18 */     this.dragon = dragon;
/*    */     
/* 20 */     setPhase(EnderDragonPhase.HOVERING);
/*    */   }
/*    */   
/*    */   public void setPhase(EnderDragonPhase<?> target) {
/* 24 */     if (this.currentPhase != null && target == this.currentPhase.getPhase()) {
/*    */       return;
/*    */     }
/*    */     
/* 28 */     if (this.currentPhase != null) {
/* 29 */       this.currentPhase.end();
/*    */     }
/*    */     
/* 32 */     this.currentPhase = getPhase(target);
/* 33 */     if (!this.dragon.level().isClientSide()) {
/* 34 */       this.dragon.getEntityData().set(EnderDragon.DATA_PHASE, target.getId());
/*    */     }
/* 36 */     LOGGER.debug("Dragon is now in phase {} on the {}", target, this.dragon.level().isClientSide() ? "client" : "server");
/*    */     
/* 38 */     this.currentPhase.begin();
/*    */   }
/*    */   
/*    */   public DragonPhaseInstance getCurrentPhase() {
/* 42 */     return Objects.<DragonPhaseInstance>requireNonNull(this.currentPhase);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends DragonPhaseInstance> T getPhase(EnderDragonPhase<T> phase) {
/* 47 */     int id = phase.getId();
/* 48 */     DragonPhaseInstance phaseInstance = this.phases[id];
/* 49 */     if (phaseInstance == null) {
/* 50 */       phaseInstance = phase.createInstance(this.dragon);
/* 51 */       this.phases[id] = phaseInstance;
/*    */     } 
/* 53 */     return (T)phaseInstance;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhaseManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */