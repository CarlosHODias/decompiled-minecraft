/*     */ package net.minecraft.world.item.enchantment.effects;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class SpawnParticlesEffect extends Record implements EnchantmentEntityEffect {
/*     */   private final net.minecraft.core.particles.ParticleOptions particle;
/*     */   private final PositionSource horizontalPosition;
/*     */   private final PositionSource verticalPosition;
/*     */   private final VelocitySource horizontalVelocity;
/*     */   private final VelocitySource verticalVelocity;
/*     */   private final FloatProvider speed;
/*     */   public static final com.mojang.serialization.MapCodec<SpawnParticlesEffect> CODEC;
/*     */   
/*  19 */   public SpawnParticlesEffect(net.minecraft.core.particles.ParticleOptions particle, PositionSource horizontalPosition, PositionSource verticalPosition, VelocitySource horizontalVelocity, VelocitySource verticalVelocity, FloatProvider speed) { this.particle = particle; this.horizontalPosition = horizontalPosition; this.verticalPosition = verticalPosition; this.horizontalVelocity = horizontalVelocity; this.verticalVelocity = verticalVelocity; this.speed = speed; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  19 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect; } public net.minecraft.core.particles.ParticleOptions particle() { return this.particle; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect;
/*  19 */     //   0	8	1	o	Ljava/lang/Object; } public PositionSource horizontalPosition() { return this.horizontalPosition; } public PositionSource verticalPosition() { return this.verticalPosition; } public VelocitySource horizontalVelocity() { return this.horizontalVelocity; } public VelocitySource verticalVelocity() { return this.verticalVelocity; } public FloatProvider speed() { return this.speed; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  27 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.core.particles.ParticleTypes.CODEC.fieldOf("particle").forGetter(SpawnParticlesEffect::particle), (App)PositionSource.CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesEffect::horizontalPosition), (App)PositionSource.CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesEffect::verticalPosition), (App)VelocitySource.CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesEffect::horizontalVelocity), (App)VelocitySource.CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesEffect::verticalVelocity), (App)FloatProvider.CODEC.optionalFieldOf("speed", net.minecraft.util.valueproviders.ConstantFloat.ZERO).forGetter(SpawnParticlesEffect::speed)).apply((com.mojang.datafixers.kinds.Applicative)i, SpawnParticlesEffect::new));
/*     */   }
/*     */   
/*     */   public enum PositionSourceType
/*     */     implements net.minecraft.util.StringRepresentable {
/*     */     BOUNDING_BOX, ENTITY_POSITION;
/*     */     private final CoordinateSource source;
/*     */     private final String id;
/*     */     
/*     */     static {
/*  37 */       ENTITY_POSITION = new PositionSourceType("ENTITY_POSITION", 0, "entity_position", (pos, center, bbSpan, random) -> pos);
/*  38 */       BOUNDING_BOX = new PositionSourceType("BOUNDING_BOX", 1, "in_bounding_box", (pos, center, bbSpan, random) -> center + (random.nextDouble() - 0.5D) * bbSpan);
/*     */     }
/*     */     
/*  41 */     public static final Codec<PositionSourceType> CODEC = (Codec<PositionSourceType>)net.minecraft.util.StringRepresentable.fromEnum(PositionSourceType::values);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PositionSourceType(String id, CoordinateSource source) {
/*  52 */       this.id = id;
/*  53 */       this.source = source;
/*     */     }
/*     */     
/*     */     public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
/*  57 */       return this.source.getCoordinate(position, center, boundingBoxSpan, random);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  62 */       return this.id;
/*     */     } @FunctionalInterface
/*     */     private static interface CoordinateSource { double getCoordinate(double param2Double1, double param2Double2, float param2Float, RandomSource param2RandomSource); }
/*     */   } public static final class PositionSource extends Record { private final SpawnParticlesEffect.PositionSourceType type; private final float offset; private final float scale; public static final com.mojang.serialization.MapCodec<PositionSource> CODEC;
/*  66 */     public PositionSource(SpawnParticlesEffect.PositionSourceType type, float offset, float scale) { this.type = type; this.offset = offset; this.scale = scale; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$PositionSource;
/*  66 */       //   0	8	1	o	Ljava/lang/Object; } public SpawnParticlesEffect.PositionSourceType type() { return this.type; } public float offset() { return this.offset; } public float scale() { return this.scale; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  75 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SpawnParticlesEffect.PositionSourceType.CODEC.fieldOf("type").forGetter(PositionSource::type), (App)Codec.FLOAT.optionalFieldOf("offset", 0.0F).forGetter(PositionSource::offset), (App)net.minecraft.util.ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("scale", 1.0F).forGetter(PositionSource::scale)).apply((com.mojang.datafixers.kinds.Applicative)i, PositionSource::new)).validate(positioning -> 
/*  76 */           (positioning.type() == SpawnParticlesEffect.PositionSourceType.ENTITY_POSITION && positioning.scale() != 1.0F) ? com.mojang.serialization.DataResult.error(()) : com.mojang.serialization.DataResult.success(positioning));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
/*  83 */       return this.type.getCoordinate(position, center, boundingBoxSpan * this.scale, random) + this.offset;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static PositionSource offsetFromEntityPosition(float offset) {
/*  88 */     return new PositionSource(PositionSourceType.ENTITY_POSITION, offset, 1.0F);
/*     */   }
/*     */   
/*     */   public static PositionSource inBoundingBox() {
/*  92 */     return new PositionSource(PositionSourceType.BOUNDING_BOX, 0.0F, 1.0F);
/*     */   }
/*     */   public static final class VelocitySource extends Record { private final float movementScale; private final FloatProvider base; public static final com.mojang.serialization.MapCodec<VelocitySource> CODEC;
/*  95 */     public VelocitySource(float movementScale, FloatProvider base) { this.movementScale = movementScale; this.base = base; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/SpawnParticlesEffect$VelocitySource;
/*  95 */       //   0	8	1	o	Ljava/lang/Object; } public float movementScale() { return this.movementScale; } public FloatProvider base() { return this.base; }
/*     */ 
/*     */     
/*     */     static {
/*  99 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.optionalFieldOf("movement_scale", 0.0F).forGetter(VelocitySource::movementScale), (App)FloatProvider.CODEC.optionalFieldOf("base", net.minecraft.util.valueproviders.ConstantFloat.ZERO).forGetter(VelocitySource::base)).apply((com.mojang.datafixers.kinds.Applicative)i, VelocitySource::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public double getVelocity(double movement, RandomSource random) {
/* 105 */       return movement * this.movementScale + this.base.sample(random);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static VelocitySource movementScaled(float scale) {
/* 110 */     return new VelocitySource(scale, (FloatProvider)net.minecraft.util.valueproviders.ConstantFloat.ZERO);
/*     */   }
/*     */   
/*     */   public static VelocitySource fixedVelocity(FloatProvider provider) {
/* 114 */     return new VelocitySource(0.0F, provider);
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply(net.minecraft.server.level.ServerLevel serverLevel, int enchantmentLevel, net.minecraft.world.item.enchantment.EnchantedItemInUse item, net.minecraft.world.entity.Entity entity, Vec3 position) {
/* 119 */     RandomSource random = entity.getRandom();
/* 120 */     Vec3 movement = entity.getKnownMovement();
/* 121 */     float bbWidth = entity.getBbWidth();
/* 122 */     float bbHeight = entity.getBbHeight();
/* 123 */     serverLevel.sendParticles(this.particle, 
/*     */         
/* 125 */         this.horizontalPosition.getCoordinate(position.x(), position.x(), bbWidth, random), 
/* 126 */         this.verticalPosition.getCoordinate(position.y(), position.y() + (bbHeight / 2.0F), bbHeight, random), 
/* 127 */         this.horizontalPosition.getCoordinate(position.z(), position.z(), bbWidth, random), 0, 
/*     */         
/* 129 */         this.horizontalVelocity.getVelocity(movement.x(), random), 
/* 130 */         this.verticalVelocity.getVelocity(movement.y(), random), 
/* 131 */         this.horizontalVelocity.getVelocity(movement.z(), random), 
/* 132 */         this.speed.sample(random));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public com.mojang.serialization.MapCodec<SpawnParticlesEffect> codec() {
/* 138 */     return CODEC;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface CoordinateSource {
/*     */     double getCoordinate(double param1Double1, double param1Double2, float param1Float, RandomSource param1RandomSource);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/effects/SpawnParticlesEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */