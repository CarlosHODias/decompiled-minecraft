/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperChestBlock extends CopperChestBlock implements WeatheringCopper {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getState), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, WeatheringCopperChestBlock::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<WeatheringCopperChestBlock> CODEC;
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<WeatheringCopperChestBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WeatheringCopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
/* 28 */     super(weatherState, openSound, closeSound, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 33 */     return WeatheringCopper.getNext(state.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
/* 38 */     if (!((net.minecraft.world.level.block.state.properties.ChestType)state.getValue((net.minecraft.world.level.block.state.properties.Property)ChestBlock.TYPE)).equals(net.minecraft.world.level.block.state.properties.ChestType.RIGHT)) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof ChestBlockEntity) { ChestBlockEntity chestBlockEntity = (ChestBlockEntity)blockEntity; if (chestBlockEntity.getEntitiesWithContainerOpen().isEmpty())
/* 39 */           changeOverTime(state, level, pos, random);  }
/*    */        }
/*    */   
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 45 */     return getState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWaxed() {
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WeatheringCopperChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */