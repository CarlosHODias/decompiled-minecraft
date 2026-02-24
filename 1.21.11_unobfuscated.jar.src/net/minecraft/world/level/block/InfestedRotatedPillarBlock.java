/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class InfestedRotatedPillarBlock extends InfestedBlock {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.core.registries.BuiltInRegistries.BLOCK.byNameCodec().fieldOf("host").forGetter(InfestedBlock::getHostBlock), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, InfestedRotatedPillarBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<InfestedRotatedPillarBlock> CODEC;
/*    */   
/*    */   public MapCodec<InfestedRotatedPillarBlock> codec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public InfestedRotatedPillarBlock(Block hostBlock, BlockBehaviour.Properties properties) {
/* 23 */     super(hostBlock, properties);
/* 24 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, (Comparable)net.minecraft.core.Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 29 */     return RotatedPillarBlock.rotatePillar(state, rotation);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 34 */     builder.add(new Property[] { (Property)RotatedPillarBlock.AXIS });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
/* 39 */     return (BlockState)defaultBlockState().setValue((Property)RotatedPillarBlock.AXIS, (Comparable)context.getClickedFace().getAxis());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/InfestedRotatedPillarBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */