/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ 
/*    */ public class WeatheringCopperDoorBlock extends DoorBlock implements WeatheringCopper {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(DoorBlock::type), (App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperDoorBlock::getAge), (App)propertiesCodec()).apply((Applicative)i, WeatheringCopperDoorBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<WeatheringCopperDoorBlock> CODEC;
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<WeatheringCopperDoorBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperDoorBlock(BlockSetType type, WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/* 27 */     super(type, properties);
/* 28 */     this.weatherState = weatherState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, net.minecraft.server.level.ServerLevel level, net.minecraft.core.BlockPos pos, RandomSource random) {
/* 33 */     if (state.getValue((net.minecraft.world.level.block.state.properties.Property)DoorBlock.HALF) == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER) {
/* 34 */       changeOverTime(state, level, pos, random);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 40 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 45 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WeatheringCopperDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */