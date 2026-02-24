/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class StainedGlassPaneBlock extends IronBarsBlock implements BeaconBeamBlock {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)DyeColor.CODEC.fieldOf("color").forGetter(StainedGlassPaneBlock::getColor), (com.mojang.datafixers.kinds.App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, StainedGlassPaneBlock::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<StainedGlassPaneBlock> CODEC;
/*    */   private final DyeColor color;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<StainedGlassPaneBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public StainedGlassPaneBlock(DyeColor color, net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
/* 21 */     super(properties);
/* 22 */     this.color = color;
/* 23 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((net.minecraft.world.level.block.state.properties.Property)NORTH, false)).setValue((net.minecraft.world.level.block.state.properties.Property)EAST, false)).setValue((net.minecraft.world.level.block.state.properties.Property)SOUTH, false)).setValue((net.minecraft.world.level.block.state.properties.Property)WEST, false)).setValue((net.minecraft.world.level.block.state.properties.Property)WATERLOGGED, false));
/*    */   }
/*    */ 
/*    */   
/*    */   public DyeColor getColor() {
/* 28 */     return this.color;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/StainedGlassPaneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */