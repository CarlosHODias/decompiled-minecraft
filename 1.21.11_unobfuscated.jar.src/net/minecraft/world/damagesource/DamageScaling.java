/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DamageScaling implements StringRepresentable {
/*  7 */   NEVER("never"),
/*  8 */   WHEN_CAUSED_BY_LIVING_NON_PLAYER("when_caused_by_living_non_player"),
/*  9 */   ALWAYS("always");
/*    */ 
/*    */   
/* 12 */   public static final Codec<DamageScaling> CODEC = (Codec<DamageScaling>)StringRepresentable.fromEnum(DamageScaling::values);
/*    */   
/*    */   private final String id;
/*    */   
/*    */   DamageScaling(String id) {
/* 17 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 22 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/damagesource/DamageScaling.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */