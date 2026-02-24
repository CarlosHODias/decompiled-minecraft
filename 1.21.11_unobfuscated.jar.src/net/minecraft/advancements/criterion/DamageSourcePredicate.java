/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ 
/*    */ public final class DamageSourcePredicate extends Record {
/*    */   private final List<TagPredicate<DamageType>> tags;
/*    */   private final Optional<EntityPredicate> directEntity;
/*    */   private final Optional<EntityPredicate> sourceEntity;
/*    */   private final Optional<Boolean> isDirect;
/*    */   public static final com.mojang.serialization.Codec<DamageSourcePredicate> CODEC;
/*    */   
/* 16 */   public DamageSourcePredicate(List<TagPredicate<DamageType>> tags, Optional<EntityPredicate> directEntity, Optional<EntityPredicate> sourceEntity, Optional<Boolean> isDirect) { this.tags = tags; this.directEntity = directEntity; this.sourceEntity = sourceEntity; this.isDirect = isDirect; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DamageSourcePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DamageSourcePredicate; } public List<TagPredicate<DamageType>> tags() { return this.tags; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DamageSourcePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DamageSourcePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DamageSourcePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/DamageSourcePredicate;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<EntityPredicate> directEntity() { return this.directEntity; } public Optional<EntityPredicate> sourceEntity() { return this.sourceEntity; } public Optional<Boolean> isDirect() { return this.isDirect; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 22 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)TagPredicate.<T>codec(net.minecraft.core.registries.Registries.DAMAGE_TYPE).listOf().optionalFieldOf("tags", List.of()).forGetter(DamageSourcePredicate::tags), (App)EntityPredicate.CODEC.optionalFieldOf("direct_entity").forGetter(DamageSourcePredicate::directEntity), (App)EntityPredicate.CODEC.optionalFieldOf("source_entity").forGetter(DamageSourcePredicate::sourceEntity), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("is_direct").forGetter(DamageSourcePredicate::isDirect)).apply((com.mojang.datafixers.kinds.Applicative)i, DamageSourcePredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.server.level.ServerPlayer player, DamageSource source) {
/* 30 */     return matches(player.level(), player.position(), source);
/*    */   }
/*    */   
/*    */   public boolean matches(net.minecraft.server.level.ServerLevel level, net.minecraft.world.phys.Vec3 position, DamageSource source) {
/* 34 */     for (TagPredicate<DamageType> tag : this.tags) {
/* 35 */       if (!tag.matches(source.typeHolder())) {
/* 36 */         return false;
/*    */       }
/*    */     } 
/* 39 */     if (this.directEntity.isPresent() && !((EntityPredicate)this.directEntity.get()).matches(level, position, source.getDirectEntity())) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (this.sourceEntity.isPresent() && !((EntityPredicate)this.sourceEntity.get()).matches(level, position, source.getEntity())) {
/* 43 */       return false;
/*    */     }
/* 45 */     if (this.isDirect.isPresent() && (Boolean)this.isDirect.get() != source.isDirect()) {
/* 46 */       return false;
/*    */     }
/*    */     
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 53 */     private final com.google.common.collect.ImmutableList.Builder<TagPredicate<DamageType>> tags = com.google.common.collect.ImmutableList.builder();
/* 54 */     private Optional<EntityPredicate> directEntity = Optional.empty();
/* 55 */     private Optional<EntityPredicate> sourceEntity = Optional.empty();
/* 56 */     private Optional<Boolean> isDirect = Optional.empty();
/*    */     
/*    */     public static Builder damageType() {
/* 59 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder tag(TagPredicate<DamageType> tag) {
/* 63 */       this.tags.add(tag);
/* 64 */       return this;
/*    */     }
/*    */     
/*    */     public Builder direct(EntityPredicate.Builder directEntity) {
/* 68 */       this.directEntity = Optional.of(directEntity.build());
/* 69 */       return this;
/*    */     }
/*    */     
/*    */     public Builder source(EntityPredicate.Builder sourceEntity) {
/* 73 */       this.sourceEntity = Optional.of(sourceEntity.build());
/* 74 */       return this;
/*    */     }
/*    */     
/*    */     public Builder isDirect(boolean direct) {
/* 78 */       this.isDirect = Optional.of(direct);
/* 79 */       return this;
/*    */     }
/*    */     
/*    */     public DamageSourcePredicate build() {
/* 83 */       return new DamageSourcePredicate((List<TagPredicate<DamageType>>)this.tags.build(), this.directEntity, this.sourceEntity, this.isDirect);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/DamageSourcePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */