/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ 
/*    */ public enum TerrainAdjustment
/*    */   implements StringRepresentable
/*    */ {
/* 10 */   NONE("none"),
/* 11 */   BURY("bury"),
/*    */   
/* 13 */   BEARD_THIN("beard_thin"),
/* 14 */   BEARD_BOX("beard_box"),
/* 15 */   ENCAPSULATE("encapsulate");
/*    */ 
/*    */   
/* 18 */   public static final Codec<TerrainAdjustment> CODEC = (Codec<TerrainAdjustment>)StringRepresentable.fromEnum(TerrainAdjustment::values);
/*    */   
/*    */   private final String id;
/*    */   
/*    */   TerrainAdjustment(String id) {
/* 23 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/TerrainAdjustment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */