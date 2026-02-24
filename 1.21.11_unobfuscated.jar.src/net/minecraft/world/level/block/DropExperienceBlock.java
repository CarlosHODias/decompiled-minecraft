/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class DropExperienceBlock extends Block {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProvider.codec(0, 10).fieldOf("experience").forGetter(()), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, DropExperienceBlock::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<DropExperienceBlock> CODEC;
/*    */   private final IntProvider xpRange;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<? extends DropExperienceBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DropExperienceBlock(IntProvider xpRange, BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/* 26 */     this.xpRange = xpRange;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(net.minecraft.world.level.block.state.BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 31 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 32 */     if (dropExperience)
/* 33 */       tryDropExperience(level, pos, tool, this.xpRange); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DropExperienceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */