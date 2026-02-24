/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class PlayerHeadBlock extends SkullBlock {
/*  6 */   public static final MapCodec<PlayerHeadBlock> CODEC = simpleCodec(PlayerHeadBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<PlayerHeadBlock> codec() {
/* 10 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected PlayerHeadBlock(BlockBehaviour.Properties properties) {
/* 14 */     super(SkullBlock.Types.PLAYER, properties);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/PlayerHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */