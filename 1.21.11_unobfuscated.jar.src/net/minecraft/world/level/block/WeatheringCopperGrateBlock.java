/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperGrateBlock extends WaterloggedTransparentBlock implements WeatheringCopper {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperGrateBlock::getAge), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, WeatheringCopperGrateBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<WeatheringCopperGrateBlock> CODEC;
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   protected MapCodec<WeatheringCopperGrateBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperGrateBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 24 */     super(properties);
/* 25 */     this.weatherState = weatherState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
/* 30 */     changeOverTime(state, level, pos, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 35 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 40 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WeatheringCopperGrateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */