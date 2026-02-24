/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ColorRGBA;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class SandBlock extends ColoredFallingBlock {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ColorRGBA.CODEC.fieldOf("falling_dust_color").forGetter(()), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, SandBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SandBlock> CODEC;
/*    */   
/*    */   public MapCodec<SandBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SandBlock(ColorRGBA dustColor, BlockBehaviour.Properties properties) {
/* 24 */     super(dustColor, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(net.minecraft.world.level.block.state.BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 29 */     super.animateTick(state, level, pos, random);
/* 30 */     net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, random);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */