/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SpellParticleOption implements ParticleOptions {
/*    */   public static MapCodec<SpellParticleOption> codec(ParticleType<SpellParticleOption> type) {
/* 15 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", -1).forGetter(()), (App)Codec.FLOAT.optionalFieldOf("power", 1.0F).forGetter(())).apply((Applicative)i, ()));
/*    */   }
/*    */   private final ParticleType<SpellParticleOption> type;
/*    */   private final int color;
/*    */   private final float power;
/*    */   
/*    */   public static StreamCodec<? super ByteBuf, SpellParticleOption> streamCodec(ParticleType<SpellParticleOption> type) {
/* 22 */     return StreamCodec.composite(ByteBufCodecs.INT, o -> o.color, ByteBufCodecs.FLOAT, o -> o.power, (color, power) -> new SpellParticleOption(type, color, power));
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
/*    */   private SpellParticleOption(ParticleType<SpellParticleOption> type, int color, float power) {
/* 34 */     this.type = type;
/* 35 */     this.color = color;
/* 36 */     this.power = power;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<SpellParticleOption> getType() {
/* 41 */     return this.type;
/*    */   }
/*    */   
/*    */   public float getRed() {
/* 45 */     return ARGB.red(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getGreen() {
/* 49 */     return ARGB.green(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getBlue() {
/* 53 */     return ARGB.blue(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getPower() {
/* 57 */     return this.power;
/*    */   }
/*    */   
/*    */   public static SpellParticleOption create(ParticleType<SpellParticleOption> type, int color, float power) {
/* 61 */     return new SpellParticleOption(type, color, power);
/*    */   }
/*    */   
/*    */   public static SpellParticleOption create(ParticleType<SpellParticleOption> type, float red, float green, float blue, float power) {
/* 65 */     return create(type, ARGB.colorFromFloat(1.0F, red, green, blue), power);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/SpellParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */