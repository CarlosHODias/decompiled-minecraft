/*    */ package net.minecraft.world.entity.animal.nautilus;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.entity.variant.ModelAndTexture;
/*    */ import net.minecraft.world.entity.variant.SpawnCondition;
/*    */ import net.minecraft.world.entity.variant.SpawnContext;
/*    */ import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
/*    */ 
/*    */ public final class ZombieNautilusVariant extends Record implements net.minecraft.world.entity.variant.PriorityProvider<SpawnContext, SpawnCondition> {
/*    */   private final ModelAndTexture<ModelType> modelAndTexture;
/*    */   private final SpawnPrioritySelectors spawnConditions;
/*    */   public static final Codec<ZombieNautilusVariant> DIRECT_CODEC;
/*    */   public static final Codec<ZombieNautilusVariant> NETWORK_CODEC;
/*    */   
/* 20 */   public ZombieNautilusVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) { this.modelAndTexture = modelAndTexture; this.spawnConditions = spawnConditions; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant; } public ModelAndTexture<ModelType> modelAndTexture() { return this.modelAndTexture; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public SpawnPrioritySelectors spawnConditions() { return this.spawnConditions; }
/*    */ 
/*    */   
/*    */   static {
/* 24 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ZombieNautilusVariant::modelAndTexture), (App)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(ZombieNautilusVariant::spawnConditions)).apply((Applicative)i, ZombieNautilusVariant::new));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ZombieNautilusVariant::modelAndTexture)).apply((Applicative)i, ZombieNautilusVariant::new));
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static final Codec<Holder<ZombieNautilusVariant>> CODEC = (Codec<Holder<ZombieNautilusVariant>>)net.minecraft.resources.RegistryFixedCodec.create(net.minecraft.core.registries.Registries.ZOMBIE_NAUTILUS_VARIANT);
/* 34 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Holder<ZombieNautilusVariant>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.ZOMBIE_NAUTILUS_VARIANT);
/*    */   
/*    */   private ZombieNautilusVariant(ModelAndTexture<ModelType> assetInfo) {
/* 37 */     this(assetInfo, SpawnPrioritySelectors.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.List<net.minecraft.world.entity.variant.PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
/* 42 */     return this.spawnConditions.selectors();
/*    */   }
/*    */   
/*    */   public enum ModelType implements net.minecraft.util.StringRepresentable {
/* 46 */     NORMAL("normal"),
/* 47 */     WARM("warm");
/*    */     
/* 49 */     public static final Codec<ModelType> CODEC = (Codec<ModelType>)net.minecraft.util.StringRepresentable.fromEnum(ModelType::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     ModelType(String name) {
/* 54 */       this.name = name;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 59 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/nautilus/ZombieNautilusVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */