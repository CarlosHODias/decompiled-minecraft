/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class ColoredFallingBlock extends FallingBlock {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ColoredFallingBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<ColoredFallingBlock> CODEC;
/*    */   protected final ColorRGBA dustColor;
/*    */   
/*    */   public MapCodec<? extends ColoredFallingBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ColoredFallingBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) {
/* 24 */     super(properties);
/* 25 */     this.dustColor = dustColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDustColor(net.minecraft.world.level.block.state.BlockState blockState, net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos) {
/* 30 */     return this.dustColor.rgba();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ColoredFallingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */