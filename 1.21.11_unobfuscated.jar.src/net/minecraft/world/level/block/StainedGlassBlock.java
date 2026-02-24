/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class StainedGlassBlock extends TransparentBlock implements BeaconBeamBlock {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(StainedGlassBlock::getColor), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, StainedGlassBlock::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<StainedGlassBlock> CODEC;
/*    */   private final DyeColor color;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<StainedGlassBlock> codec() {
/* 15 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public StainedGlassBlock(DyeColor color, net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
/* 20 */     super(properties);
/* 21 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/*    */   public DyeColor getColor() {
/* 26 */     return this.color;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/StainedGlassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */