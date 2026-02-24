/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public class EnderDragonPhase<T extends DragonPhaseInstance>
/*    */ {
/*  9 */   private static EnderDragonPhase<?>[] phases = (EnderDragonPhase<?>[])new EnderDragonPhase[0];
/* 10 */   public static final EnderDragonPhase<DragonHoldingPatternPhase> HOLDING_PATTERN = create(DragonHoldingPatternPhase.class, "HoldingPattern");
/* 11 */   public static final EnderDragonPhase<DragonStrafePlayerPhase> STRAFE_PLAYER = create(DragonStrafePlayerPhase.class, "StrafePlayer");
/* 12 */   public static final EnderDragonPhase<DragonLandingApproachPhase> LANDING_APPROACH = create(DragonLandingApproachPhase.class, "LandingApproach");
/* 13 */   public static final EnderDragonPhase<DragonLandingPhase> LANDING = create(DragonLandingPhase.class, "Landing");
/* 14 */   public static final EnderDragonPhase<DragonTakeoffPhase> TAKEOFF = create(DragonTakeoffPhase.class, "Takeoff");
/* 15 */   public static final EnderDragonPhase<DragonSittingFlamingPhase> SITTING_FLAMING = create(DragonSittingFlamingPhase.class, "SittingFlaming");
/* 16 */   public static final EnderDragonPhase<DragonSittingScanningPhase> SITTING_SCANNING = create(DragonSittingScanningPhase.class, "SittingScanning");
/* 17 */   public static final EnderDragonPhase<DragonSittingAttackingPhase> SITTING_ATTACKING = create(DragonSittingAttackingPhase.class, "SittingAttacking");
/* 18 */   public static final EnderDragonPhase<DragonChargePlayerPhase> CHARGING_PLAYER = create(DragonChargePlayerPhase.class, "ChargingPlayer");
/* 19 */   public static final EnderDragonPhase<DragonDeathPhase> DYING = create(DragonDeathPhase.class, "Dying");
/* 20 */   public static final EnderDragonPhase<DragonHoverPhase> HOVERING = create(DragonHoverPhase.class, "Hover");
/*    */   
/*    */   private final Class<? extends DragonPhaseInstance> instanceClass;
/*    */   private final int id;
/*    */   private final String name;
/*    */   
/*    */   private EnderDragonPhase(int id, Class<? extends DragonPhaseInstance> instanceClass, String name) {
/* 27 */     this.id = id;
/* 28 */     this.instanceClass = instanceClass;
/* 29 */     this.name = name;
/*    */   }
/*    */   
/*    */   public DragonPhaseInstance createInstance(EnderDragon dragon) {
/*    */     try {
/* 34 */       Constructor<? extends DragonPhaseInstance> constructor = getConstructor();
/* 35 */       return constructor.newInstance(new Object[] { dragon });
/* 36 */     } catch (Exception e) {
/* 37 */       throw new Error(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected Constructor<? extends DragonPhaseInstance> getConstructor() throws NoSuchMethodException {
/* 42 */     return this.instanceClass.getConstructor(new Class<?>[] { EnderDragon.class });
/*    */   }
/*    */   
/*    */   public int getId() {
/* 46 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return this.name + " (#" + this.name + ")";
/*    */   }
/*    */   
/*    */   public static EnderDragonPhase<?> getById(int id) {
/* 55 */     if (id < 0 || id >= phases.length) {
/* 56 */       return HOLDING_PATTERN;
/*    */     }
/* 58 */     return phases[id];
/*    */   }
/*    */   
/*    */   public static int getCount() {
/* 62 */     return phases.length;
/*    */   }
/*    */   
/*    */   private static <T extends DragonPhaseInstance> EnderDragonPhase<T> create(Class<T> instanceClass, String name) {
/* 66 */     EnderDragonPhase<T> phase = new EnderDragonPhase<>(phases.length, instanceClass, name);
/* 67 */     phases = (EnderDragonPhase<?>[])Arrays.<EnderDragonPhase>copyOf((EnderDragonPhase[])phases, phases.length + 1);
/* 68 */     phases[phase.getId()] = phase;
/* 69 */     return phase;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */