/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ 
/*    */ public final class MoonBrightnessCheck extends Record implements SpawnCondition {
/*    */   private final MinMaxBounds.Doubles range;
/*    */   public static final com.mojang.serialization.MapCodec<MoonBrightnessCheck> MAP_CODEC;
/*    */   
/* 11 */   public MoonBrightnessCheck(MinMaxBounds.Doubles range) { this.range = range; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck; } public MinMaxBounds.Doubles range() { return this.range; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/MoonBrightnessCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)MinMaxBounds.Doubles.CODEC.fieldOf("range").forGetter(MoonBrightnessCheck::range)).apply((com.mojang.datafixers.kinds.Applicative)i, MoonBrightnessCheck::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(SpawnContext context) {
/* 21 */     net.minecraft.world.level.MoonPhase moonPhase = (net.minecraft.world.level.MoonPhase)context.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.MOON_PHASE, net.minecraft.world.phys.Vec3.atCenterOf((net.minecraft.core.Vec3i)context.pos()));
/* 22 */     float moonBrightness = net.minecraft.world.level.dimension.DimensionType.MOON_BRIGHTNESS_PER_PHASE[moonPhase.index()];
/* 23 */     return this.range.matches(moonBrightness);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<MoonBrightnessCheck> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/variant/MoonBrightnessCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */