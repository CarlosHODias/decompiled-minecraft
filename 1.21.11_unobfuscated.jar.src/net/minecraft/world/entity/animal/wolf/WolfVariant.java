/*    */ package net.minecraft.world.entity.animal.wolf;
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
/*    */ public final class WolfVariant extends Record implements net.minecraft.world.entity.variant.PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final AssetInfo assetInfo;
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   public static final Codec<WolfVariant> DIRECT_CODEC;
/*    */   public static final Codec<WolfVariant> NETWORK_CODEC;
/*    */   
/* 19 */   public WolfVariant(AssetInfo assetInfo, SpawnPrioritySelectors spawnConditions) { this.assetInfo = assetInfo; this.spawnConditions = spawnConditions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant; } public AssetInfo assetInfo() { return this.assetInfo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public SpawnPrioritySelectors spawnConditions() { return this.spawnConditions; }
/*    */ 
/*    */   
/*    */   static {
/* 23 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)AssetInfo.CODEC.fieldOf("assets").forGetter(WolfVariant::assetInfo), (App)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(WolfVariant::spawnConditions)).apply((Applicative)i, WolfVariant::new));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)AssetInfo.CODEC.fieldOf("assets").forGetter(WolfVariant::assetInfo)).apply((Applicative)i, WolfVariant::new));
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static final Codec<Holder<WolfVariant>> CODEC = (Codec<Holder<WolfVariant>>)net.minecraft.resources.RegistryFixedCodec.create(net.minecraft.core.registries.Registries.WOLF_VARIANT);
/* 34 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Holder<WolfVariant>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.WOLF_VARIANT);
/*    */   
/*    */   private WolfVariant(AssetInfo assetInfo) {
/* 37 */     this(assetInfo, SpawnPrioritySelectors.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.List<net.minecraft.world.entity.variant.PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
/* 42 */     return this.spawnConditions.selectors();
/*    */   }
/*    */   public static final class AssetInfo extends Record { private final ClientAsset.ResourceTexture wild; private final ClientAsset.ResourceTexture tame; private final ClientAsset.ResourceTexture angry; public static final Codec<AssetInfo> CODEC;
/* 45 */     public AssetInfo(ClientAsset.ResourceTexture wild, ClientAsset.ResourceTexture tame, ClientAsset.ResourceTexture angry) { this.wild = wild; this.tame = tame; this.angry = angry; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/animal/wolf/WolfVariant$AssetInfo;
/* 45 */       //   0	8	1	o	Ljava/lang/Object; } public ClientAsset.ResourceTexture wild() { return this.wild; } public ClientAsset.ResourceTexture tame() { return this.tame; } public ClientAsset.ResourceTexture angry() { return this.angry; }
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 50 */       CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ClientAsset.ResourceTexture.CODEC.fieldOf("wild").forGetter(AssetInfo::wild), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("tame").forGetter(AssetInfo::tame), (App)ClientAsset.ResourceTexture.CODEC.fieldOf("angry").forGetter(AssetInfo::angry)).apply((Applicative)instance, AssetInfo::new));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/wolf/WolfVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */