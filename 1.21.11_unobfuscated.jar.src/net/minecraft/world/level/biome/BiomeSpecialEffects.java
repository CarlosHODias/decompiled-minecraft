/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ public final class BiomeSpecialEffects extends Record {
/*     */   private final int waterColor;
/*     */   private final java.util.Optional<Integer> foliageColorOverride;
/*     */   private final java.util.Optional<Integer> dryFoliageColorOverride;
/*     */   private final java.util.Optional<Integer> grassColorOverride;
/*     */   private final GrassColorModifier grassColorModifier;
/*     */   public static final com.mojang.serialization.Codec<BiomeSpecialEffects> CODEC;
/*     */   
/*  11 */   public BiomeSpecialEffects(int waterColor, java.util.Optional<Integer> foliageColorOverride, java.util.Optional<Integer> dryFoliageColorOverride, java.util.Optional<Integer> grassColorOverride, GrassColorModifier grassColorModifier) { this.waterColor = waterColor; this.foliageColorOverride = foliageColorOverride; this.dryFoliageColorOverride = dryFoliageColorOverride; this.grassColorOverride = grassColorOverride; this.grassColorModifier = grassColorModifier; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  11 */     //   0	7	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects; } public int waterColor() { return this.waterColor; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/BiomeSpecialEffects;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/biome/BiomeSpecialEffects;
/*  11 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<Integer> foliageColorOverride() { return this.foliageColorOverride; } public java.util.Optional<Integer> dryFoliageColorOverride() { return this.dryFoliageColorOverride; } public java.util.Optional<Integer> grassColorOverride() { return this.grassColorOverride; } public GrassColorModifier grassColorModifier() { return this.grassColorModifier; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  18 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.STRING_RGB_COLOR.fieldOf("water_color").forGetter(BiomeSpecialEffects::waterColor), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("foliage_color").forGetter(BiomeSpecialEffects::foliageColorOverride), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("dry_foliage_color").forGetter(BiomeSpecialEffects::dryFoliageColorOverride), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.STRING_RGB_COLOR.optionalFieldOf("grass_color").forGetter(BiomeSpecialEffects::grassColorOverride), (com.mojang.datafixers.kinds.App)GrassColorModifier.CODEC.optionalFieldOf("grass_color_modifier", GrassColorModifier.NONE).forGetter(BiomeSpecialEffects::grassColorModifier)).apply((com.mojang.datafixers.kinds.Applicative)i, BiomeSpecialEffects::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*  27 */     private java.util.OptionalInt waterColor = java.util.OptionalInt.empty();
/*  28 */     private java.util.Optional<Integer> foliageColorOverride = java.util.Optional.empty();
/*  29 */     private java.util.Optional<Integer> dryFoliageColorOverride = java.util.Optional.empty();
/*  30 */     private java.util.Optional<Integer> grassColorOverride = java.util.Optional.empty();
/*  31 */     private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;
/*     */     
/*     */     public Builder waterColor(int waterColor) {
/*  34 */       this.waterColor = java.util.OptionalInt.of(waterColor);
/*  35 */       return this;
/*     */     }
/*     */     
/*     */     public Builder foliageColorOverride(int foliageColor) {
/*  39 */       this.foliageColorOverride = java.util.Optional.of(foliageColor);
/*  40 */       return this;
/*     */     }
/*     */     
/*     */     public Builder dryFoliageColorOverride(int dryFoliageColor) {
/*  44 */       this.dryFoliageColorOverride = java.util.Optional.of(dryFoliageColor);
/*  45 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorOverride(int grassColor) {
/*  49 */       this.grassColorOverride = java.util.Optional.of(grassColor);
/*  50 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier grassModifier) {
/*  54 */       this.grassColorModifier = grassModifier;
/*  55 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeSpecialEffects build() {
/*  59 */       return new BiomeSpecialEffects(
/*  60 */           this.waterColor.orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.foliageColorOverride, this.dryFoliageColorOverride, this.grassColorOverride, this.grassColorModifier);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum GrassColorModifier
/*     */     implements net.minecraft.util.StringRepresentable
/*     */   {
/*  70 */     NONE("none")
/*     */     {
/*     */       public int modifyColor(double x, double z, int baseColor) {
/*  73 */         return baseColor;
/*     */       }
/*     */     },
/*  76 */     DARK_FOREST("dark_forest")
/*     */     {
/*     */       public int modifyColor(double x, double z, int baseColor) {
/*  79 */         return (baseColor & 0xFEFEFE) + 2634762 >> 1;
/*     */       }
/*     */     },
/*  82 */     SWAMP("swamp")
/*     */     {
/*     */       public int modifyColor(double x, double z, int baseColor) {
/*  85 */         double groundValue = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
/*  86 */         if (groundValue < -0.1D) {
/*  87 */           return 5011004;
/*     */         }
/*  89 */         return 6975545;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     GrassColorModifier(String name) {
/*  98 */       this.name = name;
/*     */     }
/*     */     
/* 101 */     public static final com.mojang.serialization.Codec<GrassColorModifier> CODEC = (com.mojang.serialization.Codec<GrassColorModifier>)net.minecraft.util.StringRepresentable.fromEnum(GrassColorModifier::values); private final String name;
/*     */     
/*     */     public String getName() {
/* 104 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 109 */       return this.name;
/*     */     }
/*     */     
/*     */     public abstract int modifyColor(double param1Double1, double param1Double2, int param1Int);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*     */       return baseColor;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*     */       return (baseColor & 0xFEFEFE) + 2634762 >> 1;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int modifyColor(double x, double z, int baseColor) {
/*     */       double groundValue = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
/*     */       if (groundValue < -0.1D)
/*     */         return 5011004; 
/*     */       return 6975545;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/biome/BiomeSpecialEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */