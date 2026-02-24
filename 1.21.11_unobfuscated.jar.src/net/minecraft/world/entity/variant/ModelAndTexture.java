/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ 
/*    */ public final class ModelAndTexture<T> extends Record {
/*    */   private final T model;
/*    */   private final ClientAsset.ResourceTexture asset;
/*    */   
/* 11 */   public ModelAndTexture(T model, ClientAsset.ResourceTexture asset) { this.model = model; this.asset = asset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/ModelAndTexture;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>; } public T model() { return this.model; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/ModelAndTexture;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/ModelAndTexture;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 11 */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/ModelAndTexture<TT;>; } public ClientAsset.ResourceTexture asset() { return this.asset; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ModelAndTexture(T model, net.minecraft.resources.Identifier assetId) {
/* 16 */     this(model, new ClientAsset.ResourceTexture(assetId));
/*    */   }
/*    */   
/*    */   public static <T> com.mojang.serialization.MapCodec<ModelAndTexture<T>> codec(com.mojang.serialization.Codec<T> modelCodec, T defaultModel) {
/* 20 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)modelCodec.optionalFieldOf("model", defaultModel).forGetter(ModelAndTexture::model), (App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(ModelAndTexture::asset)).apply((com.mojang.datafixers.kinds.Applicative)i, ModelAndTexture::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ModelAndTexture<T>> streamCodec(net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> modelCodec) {
/* 27 */     return net.minecraft.network.codec.StreamCodec.composite(modelCodec, ModelAndTexture::model, ClientAsset.ResourceTexture.STREAM_CODEC, ModelAndTexture::asset, ModelAndTexture::new);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/variant/ModelAndTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */