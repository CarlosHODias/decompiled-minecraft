/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.block.model.BlockModelPart;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedVariants
/*    */   implements BlockStateModel {
/*    */   private final WeightedList<BlockStateModel> list;
/*    */   private final TextureAtlasSprite particleIcon;
/*    */   
/*    */   public WeightedVariants(WeightedList<BlockStateModel> list) {
/* 17 */     this.list = list;
/*    */     
/* 19 */     BlockStateModel firstModel = (BlockStateModel)((Weighted)list.unwrap().getFirst()).value();
/* 20 */     this.particleIcon = firstModel.particleIcon();
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureAtlasSprite particleIcon() {
/* 25 */     return this.particleIcon;
/*    */   }
/*    */ 
/*    */   
/*    */   public void collectParts(RandomSource random, List<BlockModelPart> output) {
/* 30 */     ((BlockStateModel)this.list.getRandomOrThrow(random)).collectParts(random, output);
/*    */   }
/*    */   public static final class Unbaked extends Record implements BlockStateModel.Unbaked { private final WeightedList<BlockStateModel.Unbaked> entries;
/* 33 */     public Unbaked(WeightedList<BlockStateModel.Unbaked> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/WeightedVariants$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 33 */       //   0	7	0	this	Lnet/minecraft/client/resources/model/WeightedVariants$Unbaked; } public WeightedList<BlockStateModel.Unbaked> entries() { return this.entries; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/WeightedVariants$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/WeightedVariants$Unbaked; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/WeightedVariants$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/WeightedVariants$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object; } public BlockStateModel bake(ModelBaker modelBakery) {
/* 36 */       return new WeightedVariants(this.entries.map(m -> m.bake(modelBakery)));
/*    */     }
/*    */ 
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 41 */       this.entries.unwrap().forEach(v -> ((BlockStateModel.Unbaked)v.value()).resolveDependencies(resolver));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/WeightedVariants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */