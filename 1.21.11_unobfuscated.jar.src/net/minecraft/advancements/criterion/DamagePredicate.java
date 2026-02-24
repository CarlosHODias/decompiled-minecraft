/*    */ package net.minecraft.advancements.criterion;
/*    */ public final class DamagePredicate extends Record {
/*    */   private final MinMaxBounds.Doubles dealtDamage;
/*    */   private final MinMaxBounds.Doubles takenDamage;
/*    */   private final java.util.Optional<EntityPredicate> sourceEntity;
/*    */   private final java.util.Optional<Boolean> blocked;
/*    */   private final java.util.Optional<DamageSourcePredicate> type;
/*    */   public static final com.mojang.serialization.Codec<DamagePredicate> CODEC;
/*    */   
/* 10 */   public DamagePredicate(MinMaxBounds.Doubles dealtDamage, MinMaxBounds.Doubles takenDamage, java.util.Optional<EntityPredicate> sourceEntity, java.util.Optional<Boolean> blocked, java.util.Optional<DamageSourcePredicate> type) { this.dealtDamage = dealtDamage; this.takenDamage = takenDamage; this.sourceEntity = sourceEntity; this.blocked = blocked; this.type = type; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DamagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DamagePredicate; } public MinMaxBounds.Doubles dealtDamage() { return this.dealtDamage; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DamagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DamagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DamagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/DamagePredicate;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Doubles takenDamage() { return this.takenDamage; } public java.util.Optional<EntityPredicate> sourceEntity() { return this.sourceEntity; } public java.util.Optional<Boolean> blocked() { return this.blocked; } public java.util.Optional<DamageSourcePredicate> type() { return this.type; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("dealt", MinMaxBounds.Doubles.ANY).forGetter(DamagePredicate::dealtDamage), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("taken", MinMaxBounds.Doubles.ANY).forGetter(DamagePredicate::takenDamage), (com.mojang.datafixers.kinds.App)EntityPredicate.CODEC.optionalFieldOf("source_entity").forGetter(DamagePredicate::sourceEntity), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("blocked").forGetter(DamagePredicate::blocked), (com.mojang.datafixers.kinds.App)DamageSourcePredicate.CODEC.optionalFieldOf("type").forGetter(DamagePredicate::type)).apply((com.mojang.datafixers.kinds.Applicative)i, DamagePredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.server.level.ServerPlayer player, net.minecraft.world.damagesource.DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
/* 26 */     if (!this.dealtDamage.matches(originalDamage)) {
/* 27 */       return false;
/*    */     }
/* 29 */     if (!this.takenDamage.matches(actualDamage)) {
/* 30 */       return false;
/*    */     }
/* 32 */     if (this.sourceEntity.isPresent() && !((EntityPredicate)this.sourceEntity.get()).matches(player, source.getEntity())) {
/* 33 */       return false;
/*    */     }
/* 35 */     if (this.blocked.isPresent() && (Boolean)this.blocked.get() != blocked) {
/* 36 */       return false;
/*    */     }
/* 38 */     if (this.type.isPresent() && !((DamageSourcePredicate)this.type.get()).matches(player, source)) {
/* 39 */       return false;
/*    */     }
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 45 */     private MinMaxBounds.Doubles dealtDamage = MinMaxBounds.Doubles.ANY;
/* 46 */     private MinMaxBounds.Doubles takenDamage = MinMaxBounds.Doubles.ANY;
/* 47 */     private java.util.Optional<EntityPredicate> sourceEntity = java.util.Optional.empty();
/* 48 */     private java.util.Optional<Boolean> blocked = java.util.Optional.empty();
/* 49 */     private java.util.Optional<DamageSourcePredicate> type = java.util.Optional.empty();
/*    */     
/*    */     public static Builder damageInstance() {
/* 52 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder dealtDamage(MinMaxBounds.Doubles dealtDamage) {
/* 56 */       this.dealtDamage = dealtDamage;
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public Builder takenDamage(MinMaxBounds.Doubles takenDamage) {
/* 61 */       this.takenDamage = takenDamage;
/* 62 */       return this;
/*    */     }
/*    */     
/*    */     public Builder sourceEntity(EntityPredicate sourceEntity) {
/* 66 */       this.sourceEntity = java.util.Optional.of(sourceEntity);
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public Builder blocked(Boolean blocked) {
/* 71 */       this.blocked = java.util.Optional.of(blocked);
/* 72 */       return this;
/*    */     }
/*    */     
/*    */     public Builder type(DamageSourcePredicate type) {
/* 76 */       this.type = java.util.Optional.of(type);
/* 77 */       return this;
/*    */     }
/*    */     
/*    */     public Builder type(DamageSourcePredicate.Builder type) {
/* 81 */       this.type = java.util.Optional.of(type.build());
/* 82 */       return this;
/*    */     }
/*    */     
/*    */     public DamagePredicate build() {
/* 86 */       return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/DamagePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */