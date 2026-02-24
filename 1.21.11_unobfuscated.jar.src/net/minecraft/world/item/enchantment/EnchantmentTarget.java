/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum EnchantmentTarget implements StringRepresentable {
/*  7 */   ATTACKER("attacker"),
/*  8 */   DAMAGING_ENTITY("damaging_entity"),
/*  9 */   VICTIM("victim");
/*    */   
/* 11 */   public static final Codec<EnchantmentTarget> CODEC = (Codec<EnchantmentTarget>)StringRepresentable.fromEnum(EnchantmentTarget::values);
/*    */   
/*    */   private final String id;
/*    */   
/*    */   EnchantmentTarget(String id) {
/* 16 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 21 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/EnchantmentTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */