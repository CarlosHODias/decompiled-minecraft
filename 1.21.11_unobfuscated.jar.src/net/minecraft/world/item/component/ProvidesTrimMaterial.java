/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.EitherHolder;
/*    */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*    */ 
/*    */ public final class ProvidesTrimMaterial extends Record {
/*    */   private final EitherHolder<TrimMaterial> material;
/*    */   
/* 15 */   public ProvidesTrimMaterial(EitherHolder<TrimMaterial> material) { this.material = material; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ProvidesTrimMaterial;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/item/component/ProvidesTrimMaterial; } public EitherHolder<TrimMaterial> material() { return this.material; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ProvidesTrimMaterial;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/ProvidesTrimMaterial; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ProvidesTrimMaterial;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/ProvidesTrimMaterial;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 18 */   } public static final com.mojang.serialization.Codec<ProvidesTrimMaterial> CODEC = EitherHolder.codec(Registries.TRIM_MATERIAL, TrimMaterial.CODEC).xmap(ProvidesTrimMaterial::new, ProvidesTrimMaterial::material);
/* 19 */   public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, ProvidesTrimMaterial> STREAM_CODEC = EitherHolder.streamCodec(Registries.TRIM_MATERIAL, TrimMaterial.STREAM_CODEC).map(ProvidesTrimMaterial::new, ProvidesTrimMaterial::material);
/*    */   
/*    */   public ProvidesTrimMaterial(Holder<TrimMaterial> material) {
/* 22 */     this(new EitherHolder(material));
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public ProvidesTrimMaterial(ResourceKey<TrimMaterial> material) {
/* 27 */     this(new EitherHolder(material));
/*    */   }
/*    */   
/*    */   public java.util.Optional<Holder<TrimMaterial>> unwrap(HolderLookup.Provider registries) {
/* 31 */     return this.material.unwrap(registries);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/ProvidesTrimMaterial.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */