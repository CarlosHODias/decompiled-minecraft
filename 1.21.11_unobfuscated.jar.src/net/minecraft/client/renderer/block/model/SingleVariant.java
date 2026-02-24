/*    */ package net.minecraft.client.renderer.block.model;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SingleVariant implements BlockStateModel {
/*    */   private final BlockModelPart model;
/*    */   
/*    */   public SingleVariant(BlockModelPart model) {
/* 14 */     this.model = model;
/*    */   }
/*    */ 
/*    */   
/*    */   public void collectParts(RandomSource random, List<BlockModelPart> output) {
/* 19 */     output.add(this.model);
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureAtlasSprite particleIcon() {
/* 24 */     return this.model.particleIcon();
/*    */   }
/*    */   public static final class Unbaked extends Record implements BlockStateModel.Unbaked { private final Variant variant;
/* 27 */     public Unbaked(Variant variant) { this.variant = variant; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/SingleVariant$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 27 */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SingleVariant$Unbaked; } public Variant variant() { return this.variant; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/SingleVariant$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/SingleVariant$Unbaked; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/SingleVariant$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/SingleVariant$Unbaked;
/* 28 */       //   0	8	1	o	Ljava/lang/Object; } public static final Codec<Unbaked> CODEC = Variant.CODEC.xmap(Unbaked::new, Unbaked::variant);
/*    */ 
/*    */     
/*    */     public BlockStateModel bake(ModelBaker modelBakery) {
/* 32 */       return new SingleVariant(this.variant.bake(modelBakery));
/*    */     }
/*    */ 
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 37 */       this.variant.resolveDependencies(resolver);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/SingleVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */