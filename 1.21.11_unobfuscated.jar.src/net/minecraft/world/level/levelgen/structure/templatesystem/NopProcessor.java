/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ 
/*    */ public class NopProcessor extends StructureProcessor {
/*  6 */   public static final MapCodec<NopProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*  8 */   public static final NopProcessor INSTANCE = new NopProcessor();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 15 */     return StructureProcessorType.NOP;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/NopProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */