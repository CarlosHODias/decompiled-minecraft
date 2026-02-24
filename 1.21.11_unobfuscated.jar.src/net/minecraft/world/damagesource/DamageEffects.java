/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DamageEffects implements StringRepresentable {
/*  9 */   HURT("hurt", SoundEvents.PLAYER_HURT),
/* 10 */   THORNS("thorns", SoundEvents.PLAYER_HURT),
/* 11 */   DROWNING("drowning", SoundEvents.PLAYER_HURT_DROWN),
/* 12 */   BURNING("burning", SoundEvents.PLAYER_HURT_ON_FIRE),
/* 13 */   POKING("poking", SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH),
/* 14 */   FREEZING("freezing", SoundEvents.PLAYER_HURT_FREEZE);
/*    */ 
/*    */   
/* 17 */   public static final Codec<DamageEffects> CODEC = (Codec<DamageEffects>)StringRepresentable.fromEnum(DamageEffects::values);
/*    */   
/*    */   private final String id;
/*    */   private final SoundEvent sound;
/*    */   
/*    */   DamageEffects(String id, SoundEvent sound) {
/* 23 */     this.id = id;
/* 24 */     this.sound = sound;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 29 */     return this.id;
/*    */   }
/*    */   
/*    */   public SoundEvent sound() {
/* 33 */     return this.sound;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/damagesource/DamageEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */