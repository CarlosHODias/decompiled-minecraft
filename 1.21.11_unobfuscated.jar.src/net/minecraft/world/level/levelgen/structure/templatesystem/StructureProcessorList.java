/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class StructureProcessorList {
/*    */   private final List<StructureProcessor> list;
/*    */   
/*    */   public StructureProcessorList(List<StructureProcessor> list) {
/*  9 */     this.list = list;
/*    */   }
/*    */   
/*    */   public List<StructureProcessor> list() {
/* 13 */     return this.list;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 18 */     return "ProcessorList[" + String.valueOf(this.list) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */