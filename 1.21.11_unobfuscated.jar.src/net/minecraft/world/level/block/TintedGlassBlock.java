/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TintedGlassBlock extends TransparentBlock {
/*  8 */   public static final MapCodec<TintedGlassBlock> CODEC = simpleCodec(TintedGlassBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TintedGlassBlock> codec() {
/* 12 */     return CODEC;
/*    */   }
/*    */   public TintedGlassBlock(BlockBehaviour.Properties properties) {
/* 15 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState state) {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLightBlock(BlockState state) {
/* 25 */     return 15;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TintedGlassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */