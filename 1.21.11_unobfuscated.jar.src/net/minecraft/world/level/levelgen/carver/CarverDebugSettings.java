/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CarverDebugSettings {
/*  9 */   public static final CarverDebugSettings DEFAULT = new CarverDebugSettings(false, 
/*    */       
/* 11 */       Blocks.ACACIA_BUTTON.defaultBlockState(), 
/* 12 */       Blocks.CANDLE.defaultBlockState(), 
/* 13 */       Blocks.ORANGE_STAINED_GLASS.defaultBlockState(), 
/* 14 */       Blocks.GLASS.defaultBlockState()); public static final Codec<CarverDebugSettings> CODEC; private final boolean debugMode; private final BlockState airState;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("debug_mode", false).forGetter(CarverDebugSettings::isDebugMode), (App)BlockState.CODEC.optionalFieldOf("air_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getAirState), (App)BlockState.CODEC.optionalFieldOf("water_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getWaterState), (App)BlockState.CODEC.optionalFieldOf("lava_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getLavaState), (App)BlockState.CODEC.optionalFieldOf("barrier_state", DEFAULT.getAirState()).forGetter(CarverDebugSettings::getBarrierState)).apply((com.mojang.datafixers.kinds.Applicative)i, CarverDebugSettings::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final BlockState waterState;
/*    */ 
/*    */   
/*    */   private final BlockState lavaState;
/*    */ 
/*    */   
/*    */   private final BlockState barrierState;
/*    */ 
/*    */   
/*    */   public static CarverDebugSettings of(boolean enabled, BlockState airState, BlockState waterState, BlockState lavaState, BlockState barrierState) {
/* 32 */     return new CarverDebugSettings(enabled, airState, waterState, lavaState, barrierState);
/*    */   }
/*    */   
/*    */   public static CarverDebugSettings of(BlockState airState, BlockState waterState, BlockState lavaState, BlockState barrierState) {
/* 36 */     return new CarverDebugSettings(false, airState, waterState, lavaState, barrierState);
/*    */   }
/*    */   
/*    */   public static CarverDebugSettings of(boolean debugMode, BlockState airState) {
/* 40 */     return new CarverDebugSettings(debugMode, airState, DEFAULT.getWaterState(), DEFAULT.getLavaState(), DEFAULT.getBarrierState());
/*    */   }
/*    */   
/*    */   private CarverDebugSettings(boolean debugMode, BlockState airState, BlockState waterState, BlockState lavaState, BlockState barrierState) {
/* 44 */     this.debugMode = debugMode;
/* 45 */     this.airState = airState;
/* 46 */     this.waterState = waterState;
/* 47 */     this.lavaState = lavaState;
/* 48 */     this.barrierState = barrierState;
/*    */   }
/*    */   
/*    */   public boolean isDebugMode() {
/* 52 */     return this.debugMode;
/*    */   }
/*    */   
/*    */   public BlockState getAirState() {
/* 56 */     return this.airState;
/*    */   }
/*    */   
/*    */   public BlockState getWaterState() {
/* 60 */     return this.waterState;
/*    */   }
/*    */   
/*    */   public BlockState getLavaState() {
/* 64 */     return this.lavaState;
/*    */   }
/*    */   
/*    */   public BlockState getBarrierState() {
/* 68 */     return this.barrierState;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/carver/CarverDebugSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */