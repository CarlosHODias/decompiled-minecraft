/*    */ package net.minecraft.world.entity.player;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class Abilities {
/*    */   private static final boolean DEFAULT_INVULNERABLE = false;
/*    */   private static final boolean DEFAULY_FLYING = false;
/*    */   private static final boolean DEFAULT_MAY_FLY = false;
/*    */   private static final boolean DEFAULT_INSTABUILD = false;
/*    */   private static final boolean DEFAULT_MAY_BUILD = true;
/*    */   private static final float DEFAULT_FLYING_SPEED = 0.05F;
/*    */   private static final float DEFAULT_WALKING_SPEED = 0.1F;
/*    */   public boolean invulnerable;
/*    */   public boolean flying;
/*    */   public boolean mayfly;
/*    */   public boolean instabuild;
/*    */   public boolean mayBuild = true;
/* 20 */   private float flyingSpeed = 0.05F;
/* 21 */   private float walkingSpeed = 0.1F;
/*    */   
/*    */   public float getFlyingSpeed() {
/* 24 */     return this.flyingSpeed;
/*    */   }
/*    */   
/*    */   public void setFlyingSpeed(float value) {
/* 28 */     this.flyingSpeed = value;
/*    */   }
/*    */   
/*    */   public float getWalkingSpeed() {
/* 32 */     return this.walkingSpeed;
/*    */   }
/*    */   
/*    */   public void setWalkingSpeed(float value) {
/* 36 */     this.walkingSpeed = value;
/*    */   }
/*    */   
/*    */   public Packed pack() {
/* 40 */     return new Packed(this.invulnerable, this.flying, this.mayfly, this.instabuild, this.mayBuild, this.flyingSpeed, this.walkingSpeed);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(Packed packed) {
/* 52 */     this.invulnerable = packed.invulnerable;
/* 53 */     this.flying = packed.flying;
/* 54 */     this.mayfly = packed.mayFly;
/* 55 */     this.instabuild = packed.instabuild;
/* 56 */     this.mayBuild = packed.mayBuild;
/* 57 */     this.flyingSpeed = packed.flyingSpeed;
/* 58 */     this.walkingSpeed = packed.walkingSpeed;
/*    */   }
/*    */   public static final class Packed extends Record { private final boolean invulnerable; private final boolean flying; private final boolean mayFly; private final boolean instabuild; private final boolean mayBuild; private final float flyingSpeed; private final float walkingSpeed; public static final Codec<Packed> CODEC;
/* 61 */     public Packed(boolean invulnerable, boolean flying, boolean mayFly, boolean instabuild, boolean mayBuild, float flyingSpeed, float walkingSpeed) { this.invulnerable = invulnerable; this.flying = flying; this.mayFly = mayFly; this.instabuild = instabuild; this.mayBuild = mayBuild; this.flyingSpeed = flyingSpeed; this.walkingSpeed = walkingSpeed; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/Abilities$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 61 */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed; } public boolean invulnerable() { return this.invulnerable; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/Abilities$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/Abilities$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed;
/* 61 */       //   0	8	1	o	Ljava/lang/Object; } public boolean flying() { return this.flying; } public boolean mayFly() { return this.mayFly; } public boolean instabuild() { return this.instabuild; } public boolean mayBuild() { return this.mayBuild; } public float flyingSpeed() { return this.flyingSpeed; } public float walkingSpeed() { return this.walkingSpeed; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 70 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.fieldOf("invulnerable").orElse(false).forGetter(Packed::invulnerable), (App)Codec.BOOL.fieldOf("flying").orElse(false).forGetter(Packed::flying), (App)Codec.BOOL.fieldOf("mayfly").orElse(false).forGetter(Packed::mayFly), (App)Codec.BOOL.fieldOf("instabuild").orElse(false).forGetter(Packed::instabuild), (App)Codec.BOOL.fieldOf("mayBuild").orElse(true).forGetter(Packed::mayBuild), (App)Codec.FLOAT.fieldOf("flySpeed").orElse(0.05F).forGetter(Packed::flyingSpeed), (App)Codec.FLOAT.fieldOf("walkSpeed").orElse(0.1F).forGetter(Packed::walkingSpeed)).apply((com.mojang.datafixers.kinds.Applicative)i, Packed::new));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/player/Abilities.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */