/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ public class MobEffect
/*     */   implements FeatureElement
/*     */ {
/*  40 */   public static final Codec<Holder<MobEffect>> CODEC = BuiltInRegistries.MOB_EFFECT.holderByNameCodec();
/*  41 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MobEffect>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT);
/*     */   
/*  43 */   private static final int AMBIENT_ALPHA = Mth.floor(38.25F);
/*  44 */   private final Map<Holder<Attribute>, AttributeTemplate> attributeModifiers = (Map<Holder<Attribute>, AttributeTemplate>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final MobEffectCategory category;
/*     */   private final int color;
/*     */   private final Function<MobEffectInstance, ParticleOptions> particleFactory;
/*     */   private String descriptionId;
/*     */   private int blendInDurationTicks;
/*     */   private int blendOutDurationTicks;
/*     */   private int blendOutAdvanceTicks;
/*  53 */   private Optional<SoundEvent> soundOnAdded = Optional.empty();
/*  54 */   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */   
/*     */   protected MobEffect(MobEffectCategory category, int color) {
/*  57 */     this.category = category;
/*  58 */     this.color = color;
/*     */     
/*  60 */     this.particleFactory = (effectInstance -> {
/*     */         int alpha = effectInstance.isAmbient() ? AMBIENT_ALPHA : 255;
/*     */         return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(alpha, color));
/*     */       });
/*     */   }
/*     */   
/*     */   protected MobEffect(MobEffectCategory category, int color, ParticleOptions particleOptions) {
/*  67 */     this.category = category;
/*  68 */     this.color = color;
/*  69 */     this.particleFactory = (ignored -> particleOptions);
/*     */   }
/*     */   
/*     */   public int getBlendInDurationTicks() {
/*  73 */     return this.blendInDurationTicks;
/*     */   }
/*     */   
/*     */   public int getBlendOutDurationTicks() {
/*  77 */     return this.blendOutDurationTicks;
/*     */   }
/*     */   
/*     */   public int getBlendOutAdvanceTicks() {
/*  81 */     return this.blendOutAdvanceTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
/*  92 */     return true;
/*     */   }
/*     */   
/*     */   public void applyInstantenousEffect(ServerLevel level, Entity source, Entity owner, LivingEntity mob, int amplification, double scale) {
/*  96 */     applyEffectTick(level, mob, amplification);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEffectStarted(LivingEntity mob, int amplifier) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEffectAdded(LivingEntity mob, int amplifier) {
/* 122 */     this.soundOnAdded.ifPresent(soundEvent -> mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), soundEvent, mob.getSoundSource(), 1.0F, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {}
/*     */ 
/*     */   
/*     */   public void onMobHurt(ServerLevel level, LivingEntity mob, int amplifier, DamageSource source, float damage) {}
/*     */   
/*     */   public boolean isInstantenous() {
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   protected String getOrCreateDescriptionId() {
/* 136 */     if (this.descriptionId == null) {
/* 137 */       this.descriptionId = Util.makeDescriptionId("effect", BuiltInRegistries.MOB_EFFECT.getKey(this));
/*     */     }
/* 139 */     return this.descriptionId;
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/* 143 */     return getOrCreateDescriptionId();
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 147 */     return (Component)Component.translatable(getDescriptionId());
/*     */   }
/*     */   
/*     */   public MobEffectCategory getCategory() {
/* 151 */     return this.category;
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 155 */     return this.color;
/*     */   }
/*     */   
/*     */   public MobEffect addAttributeModifier(Holder<Attribute> attribute, Identifier id, double amount, AttributeModifier.Operation operation) {
/* 159 */     this.attributeModifiers.put(attribute, new AttributeTemplate(id, amount, operation));
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public MobEffect setBlendDuration(int ticks) {
/* 164 */     return setBlendDuration(ticks, ticks, ticks);
/*     */   }
/*     */   
/*     */   public MobEffect setBlendDuration(int inTicks, int outTicks, int outAdvanceTicks) {
/* 168 */     this.blendInDurationTicks = inTicks;
/* 169 */     this.blendOutDurationTicks = outTicks;
/* 170 */     this.blendOutAdvanceTicks = outAdvanceTicks;
/* 171 */     return this;
/*     */   }
/*     */   
/*     */   public void createModifiers(int amplifier, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 175 */     this.attributeModifiers.forEach((attribute, template) -> consumer.accept(attribute, template.create(amplifier)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAttributeModifiers(AttributeMap attributes) {
/* 181 */     for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
/* 182 */       AttributeInstance attribute = attributes.getInstance(entry.getKey());
/*     */       
/* 184 */       if (attribute != null) {
/* 185 */         attribute.removeModifier(((AttributeTemplate)entry.getValue()).id());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAttributeModifiers(AttributeMap attributes, int amplifier) {
/* 191 */     for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifiers.entrySet()) {
/* 192 */       AttributeInstance attribute = attributes.getInstance(entry.getKey());
/*     */       
/* 194 */       if (attribute != null) {
/* 195 */         attribute.removeModifier(((AttributeTemplate)entry.getValue()).id());
/* 196 */         attribute.addPermanentModifier(((AttributeTemplate)entry.getValue()).create(amplifier));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBeneficial() {
/* 202 */     return (this.category == MobEffectCategory.BENEFICIAL);
/*     */   }
/*     */   
/*     */   public ParticleOptions createParticleOptions(MobEffectInstance mobEffectInstance) {
/* 206 */     return this.particleFactory.apply(mobEffectInstance);
/*     */   }
/*     */   
/*     */   public MobEffect withSoundOnAdded(SoundEvent soundEvent) {
/* 210 */     this.soundOnAdded = Optional.of(soundEvent);
/* 211 */     return this;
/*     */   }
/*     */   
/*     */   public MobEffect requiredFeatures(FeatureFlag... flags) {
/* 215 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 221 */     return this.requiredFeatures;
/*     */   }
/*     */   private static final class AttributeTemplate extends Record { private final Identifier id; private final double amount; private final AttributeModifier.Operation operation;
/* 224 */     private AttributeTemplate(Identifier id, double amount, AttributeModifier.Operation operation) { this.id = id; this.amount = amount; this.operation = operation; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 224 */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #224	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/effect/MobEffect$AttributeTemplate;
/* 224 */       //   0	8	1	o	Ljava/lang/Object; } public double amount() { return this.amount; } public AttributeModifier.Operation operation() { return this.operation; }
/*     */      public AttributeModifier create(int amplifier) {
/* 226 */       return new AttributeModifier(this.id, this.amount * (amplifier + 1), this.operation);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/MobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */