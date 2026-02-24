/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public class GenerationStep
/*    */ {
/*    */   public enum Decoration
/*    */     implements StringRepresentable {
/* 10 */     RAW_GENERATION("raw_generation"),
/*    */     
/* 12 */     LAKES("lakes"),
/*    */     
/* 14 */     LOCAL_MODIFICATIONS("local_modifications"),
/*    */     
/* 16 */     UNDERGROUND_STRUCTURES("underground_structures"),
/*    */     
/* 18 */     SURFACE_STRUCTURES("surface_structures"),
/*    */     
/* 20 */     STRONGHOLDS("strongholds"),
/*    */     
/* 22 */     UNDERGROUND_ORES("underground_ores"),
/*    */     
/* 24 */     UNDERGROUND_DECORATION("underground_decoration"),
/*    */     
/* 26 */     FLUID_SPRINGS("fluid_springs"),
/*    */     
/* 28 */     VEGETAL_DECORATION("vegetal_decoration"),
/*    */     
/* 30 */     TOP_LAYER_MODIFICATION("top_layer_modification");
/*    */ 
/*    */     
/* 33 */     public static final Codec<Decoration> CODEC = (Codec<Decoration>)StringRepresentable.fromEnum(Decoration::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     Decoration(String name) {
/* 38 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getName() {
/* 42 */       return this.name;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 47 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/GenerationStep.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */