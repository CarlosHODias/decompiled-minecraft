/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PlayerModelPart implements StringRepresentable {
/*  8 */   CAPE(0, "cape"),
/*  9 */   JACKET(1, "jacket"),
/* 10 */   LEFT_SLEEVE(2, "left_sleeve"),
/* 11 */   RIGHT_SLEEVE(3, "right_sleeve"),
/* 12 */   LEFT_PANTS_LEG(4, "left_pants_leg"),
/* 13 */   RIGHT_PANTS_LEG(5, "right_pants_leg"),
/* 14 */   HAT(6, "hat");
/*    */ 
/*    */   
/* 17 */   public static final Codec<PlayerModelPart> CODEC = (Codec<PlayerModelPart>)StringRepresentable.fromEnum(PlayerModelPart::values);
/*    */   
/*    */   private final int bit;
/*    */   private final int mask;
/*    */   private final String id;
/*    */   private final Component name;
/*    */   
/*    */   PlayerModelPart(int bit, String name) {
/* 25 */     this.bit = bit;
/* 26 */     this.mask = 1 << bit;
/* 27 */     this.id = name;
/* 28 */     this.name = (Component)Component.translatable("options.modelPart." + name);
/*    */   }
/*    */   
/*    */   public int getMask() {
/* 32 */     return this.mask;
/*    */   }
/*    */   
/*    */   public int getBit() {
/* 36 */     return this.bit;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 40 */     return this.id;
/*    */   }
/*    */   
/*    */   public Component getName() {
/* 44 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 49 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/player/PlayerModelPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */