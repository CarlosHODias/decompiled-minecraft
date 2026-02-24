/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.fog.FogData;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.material.FogType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LavaFogEnvironment
/*    */   extends FogEnvironment
/*    */ {
/*    */   private static final int COLOR = -6743808;
/*    */   
/*    */   public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
/* 18 */     return -6743808;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
/*    */     // Byte code:
/*    */     //   0: aload_2
/*    */     //   1: invokevirtual entity : ()Lnet/minecraft/world/entity/Entity;
/*    */     //   4: invokevirtual isSpectator : ()Z
/*    */     //   7: ifeq -> 28
/*    */     //   10: aload_1
/*    */     //   11: ldc -8.0
/*    */     //   13: putfield environmentalStart : F
/*    */     //   16: aload_1
/*    */     //   17: fload #4
/*    */     //   19: ldc 0.5
/*    */     //   21: fmul
/*    */     //   22: putfield environmentalEnd : F
/*    */     //   25: goto -> 85
/*    */     //   28: aload_2
/*    */     //   29: invokevirtual entity : ()Lnet/minecraft/world/entity/Entity;
/*    */     //   32: astore #7
/*    */     //   34: aload #7
/*    */     //   36: instanceof net/minecraft/world/entity/LivingEntity
/*    */     //   39: ifeq -> 74
/*    */     //   42: aload #7
/*    */     //   44: checkcast net/minecraft/world/entity/LivingEntity
/*    */     //   47: astore #6
/*    */     //   49: aload #6
/*    */     //   51: getstatic net/minecraft/world/effect/MobEffects.FIRE_RESISTANCE : Lnet/minecraft/core/Holder;
/*    */     //   54: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*    */     //   57: ifeq -> 74
/*    */     //   60: aload_1
/*    */     //   61: fconst_0
/*    */     //   62: putfield environmentalStart : F
/*    */     //   65: aload_1
/*    */     //   66: ldc 5.0
/*    */     //   68: putfield environmentalEnd : F
/*    */     //   71: goto -> 85
/*    */     //   74: aload_1
/*    */     //   75: ldc 0.25
/*    */     //   77: putfield environmentalStart : F
/*    */     //   80: aload_1
/*    */     //   81: fconst_1
/*    */     //   82: putfield environmentalEnd : F
/*    */     //   85: aload_1
/*    */     //   86: aload_1
/*    */     //   87: getfield environmentalEnd : F
/*    */     //   90: putfield skyEnd : F
/*    */     //   93: aload_1
/*    */     //   94: aload_1
/*    */     //   95: getfield environmentalEnd : F
/*    */     //   98: putfield cloudEnd : F
/*    */     //   101: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     //   #24	-> 10
/*    */     //   #25	-> 16
/*    */     //   #26	-> 28
/*    */     //   #27	-> 60
/*    */     //   #28	-> 65
/*    */     //   #30	-> 74
/*    */     //   #31	-> 80
/*    */     //   #33	-> 85
/*    */     //   #34	-> 93
/*    */     //   #35	-> 101
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   49	25	6	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*    */     //   0	102	0	this	Lnet/minecraft/client/renderer/fog/environment/LavaFogEnvironment;
/*    */     //   0	102	1	fog	Lnet/minecraft/client/renderer/fog/FogData;
/*    */     //   0	102	2	camera	Lnet/minecraft/client/Camera;
/*    */     //   0	102	3	level	Lnet/minecraft/client/multiplayer/ClientLevel;
/*    */     //   0	102	4	renderDistance	F
/*    */     //   0	102	5	deltaTracker	Lnet/minecraft/client/DeltaTracker;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isApplicable(FogType fogType, Entity entity) {
/* 39 */     return (fogType == FogType.LAVA);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/fog/environment/LavaFogEnvironment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */