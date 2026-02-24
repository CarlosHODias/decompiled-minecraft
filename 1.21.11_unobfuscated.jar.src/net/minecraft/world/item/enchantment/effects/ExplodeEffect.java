/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ExplodeEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final boolean attributeToUser;
/*    */   private final Optional<Holder<DamageType>> damageType;
/*    */   private final Optional<LevelBasedValue> knockbackMultiplier;
/*    */   private final Optional<HolderSet<Block>> immuneBlocks;
/*    */   private final Vec3 offset;
/*    */   private final LevelBasedValue radius;
/*    */   
/* 29 */   public ExplodeEffect(boolean attributeToUser, Optional<Holder<DamageType>> damageType, Optional<LevelBasedValue> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks, Vec3 offset, LevelBasedValue radius, boolean createFire, Level.ExplosionInteraction blockInteraction, ParticleOptions smallParticle, ParticleOptions largeParticle, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> sound) { this.attributeToUser = attributeToUser; this.damageType = damageType; this.knockbackMultiplier = knockbackMultiplier; this.immuneBlocks = immuneBlocks; this.offset = offset; this.radius = radius; this.createFire = createFire; this.blockInteraction = blockInteraction; this.smallParticle = smallParticle; this.largeParticle = largeParticle; this.blockParticles = blockParticles; this.sound = sound; } private final boolean createFire; private final Level.ExplosionInteraction blockInteraction; private final ParticleOptions smallParticle; private final ParticleOptions largeParticle; private final WeightedList<ExplosionParticleInfo> blockParticles; private final Holder<SoundEvent> sound; public static final com.mojang.serialization.MapCodec<ExplodeEffect> CODEC; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ExplodeEffect;
/* 29 */     //   0	8	1	o	Ljava/lang/Object; } public boolean attributeToUser() { return this.attributeToUser; } public Optional<Holder<DamageType>> damageType() { return this.damageType; } public Optional<LevelBasedValue> knockbackMultiplier() { return this.knockbackMultiplier; } public Optional<HolderSet<Block>> immuneBlocks() { return this.immuneBlocks; } public Vec3 offset() { return this.offset; } public LevelBasedValue radius() { return this.radius; } public boolean createFire() { return this.createFire; } public Level.ExplosionInteraction blockInteraction() { return this.blockInteraction; } public ParticleOptions smallParticle() { return this.smallParticle; } public ParticleOptions largeParticle() { return this.largeParticle; } public WeightedList<ExplosionParticleInfo> blockParticles() { return this.blockParticles; } public Holder<SoundEvent> sound() { return this.sound; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 43 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("attribute_to_user", false).forGetter(ExplodeEffect::attributeToUser), (App)DamageType.CODEC.optionalFieldOf("damage_type").forGetter(ExplodeEffect::damageType), (App)LevelBasedValue.CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeEffect::knockbackMultiplier), (App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeEffect::immuneBlocks), (App)Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(ExplodeEffect::offset), (App)LevelBasedValue.CODEC.fieldOf("radius").forGetter(ExplodeEffect::radius), (App)Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplodeEffect::createFire), (App)Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(ExplodeEffect::blockInteraction), (App)ParticleTypes.CODEC.fieldOf("small_particle").forGetter(ExplodeEffect::smallParticle), (App)ParticleTypes.CODEC.fieldOf("large_particle").forGetter(ExplodeEffect::largeParticle), (App)WeightedList.codec(ExplosionParticleInfo.CODEC).optionalFieldOf("block_particles", WeightedList.of()).forGetter(ExplodeEffect::blockParticles), (App)SoundEvent.CODEC.fieldOf("sound").forGetter(ExplodeEffect::sound)).apply((com.mojang.datafixers.kinds.Applicative)i, ExplodeEffect::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(net.minecraft.server.level.ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 60 */     Vec3 pos = position.add(this.offset);
/* 61 */     serverLevel.explode(
/* 62 */         this.attributeToUser ? entity : null, 
/* 63 */         getDamageSource(entity, pos), (net.minecraft.world.level.ExplosionDamageCalculator)new net.minecraft.world.level.SimpleExplosionDamageCalculator((this.blockInteraction != Level.ExplosionInteraction.NONE), 
/*    */ 
/*    */           
/* 66 */           this.damageType.isPresent(), 
/* 67 */           this.knockbackMultiplier.map(value -> value.calculate(enchantmentLevel)), this.immuneBlocks), 
/*    */ 
/*    */         
/* 70 */         pos.x(), 
/* 71 */         pos.y(), 
/* 72 */         pos.z(), 
/* 73 */         Math.max(this.radius.calculate(enchantmentLevel), 0.0F), this.createFire, this.blockInteraction, this.smallParticle, this.largeParticle, this.blockParticles, this.sound);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private net.minecraft.world.damagesource.DamageSource getDamageSource(Entity entity, Vec3 position) {
/* 83 */     if (this.damageType.isEmpty()) {
/* 84 */       return null;
/*    */     }
/* 86 */     if (this.attributeToUser) {
/* 87 */       return new net.minecraft.world.damagesource.DamageSource(this.damageType.get(), entity);
/*    */     }
/* 89 */     return new net.minecraft.world.damagesource.DamageSource(this.damageType.get(), position);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ExplodeEffect> codec() {
/* 94 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/ExplodeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */