/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public interface SpecialModelRenderer<T>
/*    */ {
/*    */   void submit(T paramT, ItemDisplayContext paramItemDisplayContext, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3);
/*    */   
/*    */   void getExtents(Consumer<Vector3fc> paramConsumer);
/*    */   
/*    */   T extractArgument(ItemStack paramItemStack);
/*    */   
/*    */   public static interface BakingContext
/*    */   {
/*    */     EntityModelSet entityModelSet();
/*    */     
/*    */     MaterialSet materials();
/*    */     
/*    */     PlayerSkinRenderCache playerSkinRenderCache();
/*    */     
/*    */     public static final class Simple
/*    */       extends Record
/*    */       implements BakingContext
/*    */     {
/*    */       private final EntityModelSet entityModelSet;
/*    */       private final MaterialSet materials;
/*    */       private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */       
/* 38 */       public Simple(EntityModelSet entityModelSet, MaterialSet materials, PlayerSkinRenderCache playerSkinRenderCache) { this.entityModelSet = entityModelSet; this.materials = materials; this.playerSkinRenderCache = playerSkinRenderCache; } public final String toString() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;)Ljava/lang/String;
/*    */         //   6: areturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #38	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	7	0	this	Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple; } public final int hashCode() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;)I
/*    */         //   6: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #38	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	7	0	this	Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple; } public final boolean equals(Object o) { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: aload_1
/*    */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;Ljava/lang/Object;)Z
/*    */         //   7: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #38	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	8	0	this	Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;
/* 38 */         //   0	8	1	o	Ljava/lang/Object; } public EntityModelSet entityModelSet() { return this.entityModelSet; } public MaterialSet materials() { return this.materials; } public PlayerSkinRenderCache playerSkinRenderCache() { return this.playerSkinRenderCache; } } } public static interface Unbaked { SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext param1BakingContext); MapCodec<? extends Unbaked> type(); } public static final class Simple extends Record implements BakingContext { private final EntityModelSet entityModelSet; public Simple(EntityModelSet entityModelSet, MaterialSet materials, PlayerSkinRenderCache playerSkinRenderCache) { this.entityModelSet = entityModelSet; this.materials = materials; this.playerSkinRenderCache = playerSkinRenderCache; } private final MaterialSet materials; private final PlayerSkinRenderCache playerSkinRenderCache; public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #38	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/SpecialModelRenderer$BakingContext$Simple;
/* 38 */       //   0	8	1	o	Ljava/lang/Object; } public EntityModelSet entityModelSet() { return this.entityModelSet; } public MaterialSet materials() { return this.materials; } public PlayerSkinRenderCache playerSkinRenderCache() { return this.playerSkinRenderCache; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/special/SpecialModelRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */