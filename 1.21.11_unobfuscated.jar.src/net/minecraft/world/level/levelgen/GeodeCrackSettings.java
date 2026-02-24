/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class GeodeCrackSettings {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration.CHANCE_RANGE.fieldOf("generate_crack_chance").orElse(1.0D).forGetter(()), (App)Codec.doubleRange(0.0D, 5.0D).fieldOf("base_crack_size").orElse(2.0D).forGetter(()), (App)Codec.intRange(0, 10).fieldOf("crack_point_offset").orElse(2).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, GeodeCrackSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<GeodeCrackSettings> CODEC;
/*    */   
/*    */   public final double generateCrackChance;
/*    */   public final double baseCrackSize;
/*    */   public final int crackPointOffset;
/*    */   
/*    */   public GeodeCrackSettings(double generateCrackChance, double baseCrackSize, int crackPointOffset) {
/* 19 */     this.generateCrackChance = generateCrackChance;
/* 20 */     this.baseCrackSize = baseCrackSize;
/* 21 */     this.crackPointOffset = crackPointOffset;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/GeodeCrackSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */