/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperFullBlock extends Block implements WeatheringCopper {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, WeatheringCopperFullBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<WeatheringCopperFullBlock> CODEC;
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   public MapCodec<WeatheringCopperFullBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public WeatheringCopperFullBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
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


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WeatheringCopperFullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */