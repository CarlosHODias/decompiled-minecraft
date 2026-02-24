/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FlowerBlock extends VegetationBlock implements SuspiciousEffectHolder {
/* 21 */   protected static final MapCodec<SuspiciousStewEffects> EFFECTS_FIELD = SuspiciousStewEffects.CODEC.fieldOf("suspicious_stew_effects");
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects), (App)propertiesCodec()).apply((Applicative)i, FlowerBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<FlowerBlock> CODEC;
/*    */   
/*    */   public MapCodec<? extends FlowerBlock> codec() {
/* 30 */     return CODEC;
/*    */   }
/*    */   
/* 33 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 10.0D);
/*    */   
/*    */   private final SuspiciousStewEffects suspiciousStewEffects;
/*    */   
/*    */   public FlowerBlock(Holder<MobEffect> suspiciousStewEffect, float effectSeconds, BlockBehaviour.Properties properties) {
/* 38 */     this(makeEffectList(suspiciousStewEffect, effectSeconds), properties);
/*    */   }
/*    */   
/*    */   public FlowerBlock(SuspiciousStewEffects suspiciousStewEffects, BlockBehaviour.Properties properties) {
/* 42 */     super(properties);
/* 43 */     this.suspiciousStewEffects = suspiciousStewEffects;
/*    */   }
/*    */   
/*    */   protected static SuspiciousStewEffects makeEffectList(Holder<MobEffect> suspiciousStewEffect, float effectSeconds) {
/* 47 */     return new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(suspiciousStewEffect, 
/* 48 */             Mth.floor(effectSeconds * 20.0F))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 54 */     return SHAPE.move(state.getOffset(pos));
/*    */   }
/*    */ 
/*    */   
/*    */   public SuspiciousStewEffects getSuspiciousEffects() {
/* 59 */     return this.suspiciousStewEffects;
/*    */   }
/*    */   
/*    */   public MobEffectInstance getBeeInteractionEffect() {
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/FlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */