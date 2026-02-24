/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DeathMessageType implements StringRepresentable {
/*  7 */   DEFAULT("default"),
/*  8 */   FALL_VARIANTS("fall_variants"),
/*  9 */   INTENTIONAL_GAME_DESIGN("intentional_game_design");
/*    */ 
/*    */   
/* 12 */   public static final Codec<DeathMessageType> CODEC = (Codec<DeathMessageType>)StringRepresentable.fromEnum(DeathMessageType::values);
/*    */   
/*    */   private final String id;
/*    */   
/*    */   DeathMessageType(String id) {
/* 17 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 22 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/damagesource/DeathMessageType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */