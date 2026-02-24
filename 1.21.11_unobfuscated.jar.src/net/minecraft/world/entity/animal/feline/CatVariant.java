/*    */ package net.minecraft.world.entity.animal.feline;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.ClientAsset;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnContext;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ 
/*    */ public final class CatVariant extends Record implements net.minecraft.world.entity.variant.PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ClientAsset.ResourceTexture assetInfo;
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   public static final Codec<CatVariant> DIRECT_CODEC;
/*    */   public static final Codec<CatVariant> NETWORK_CODEC;
/*    */   
/* 19 */   public CatVariant(ClientAsset.ResourceTexture assetInfo, SpawnPrioritySelectors spawnConditions) { this.assetInfo = assetInfo; this.spawnConditions = spawnConditions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/feline/CatVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant; } public ClientAsset.ResourceTexture assetInfo() { return this.assetInfo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/feline/CatVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/feline/CatVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/feline/CatVariant;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public SpawnPrioritySelectors spawnConditions() { return this.spawnConditions; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 24 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(CatVariant::assetInfo), (App)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(CatVariant::spawnConditions)).apply((Applicative)i, CatVariant::new));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClientAsset.ResourceTexture.DEFAULT_FIELD_CODEC.forGetter(CatVariant::assetInfo)).apply((Applicative)i, CatVariant::new));
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static final Codec<Holder<CatVariant>> CODEC = (Codec<Holder<CatVariant>>)net.minecraft.resources.RegistryFixedCodec.create(net.minecraft.core.registries.Registries.CAT_VARIANT);
/* 34 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Holder<CatVariant>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.CAT_VARIANT);
/*    */   
/*    */   private CatVariant(ClientAsset.ResourceTexture assetInfo) {
/* 37 */     this(assetInfo, SpawnPrioritySelectors.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.List<net.minecraft.world.entity.variant.PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
/* 42 */     return this.spawnConditions.selectors();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/feline/CatVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */