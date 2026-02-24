/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum LiquidSettings implements StringRepresentable {
/*  7 */   IGNORE_WATERLOGGING("ignore_waterlogging"),
/*  8 */   APPLY_WATERLOGGING("apply_waterlogging");
/*    */   
/* 10 */   public static Codec<LiquidSettings> CODEC = StringRepresentable.fromValues(LiquidSettings::values);
/*    */   private final String name;
/*    */   
/*    */   LiquidSettings(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */