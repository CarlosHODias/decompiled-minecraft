/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SculkSensorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.BlockPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SculkSensorBlockEntity
/*     */   extends BlockEntity implements GameEventListener.Provider<VibrationSystem.Listener>, VibrationSystem {
/*     */   private static final int DEFAULT_LAST_VIBRATION_FREQUENCY = 0;
/*     */   private VibrationSystem.Data vibrationData;
/*  24 */   private int lastVibrationFrequency = 0; private final VibrationSystem.Listener vibrationListener; private final VibrationSystem.User vibrationUser;
/*     */   
/*     */   protected SculkSensorBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
/*  27 */     super(type, worldPosition, blockState);
/*  28 */     this.vibrationUser = createVibrationUser();
/*  29 */     this.vibrationData = new VibrationSystem.Data();
/*  30 */     this.vibrationListener = new VibrationSystem.Listener(this);
/*     */   }
/*     */   
/*     */   public SculkSensorBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  34 */     this(BlockEntityType.SCULK_SENSOR, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public VibrationSystem.User createVibrationUser() {
/*  38 */     return new VibrationUser(getBlockPos());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  43 */     super.loadAdditional(input);
/*  44 */     this.lastVibrationFrequency = input.getIntOr("last_vibration_frequency", 0);
/*     */     
/*  46 */     this.vibrationData = input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  51 */     super.saveAdditional(output);
/*  52 */     output.putInt("last_vibration_frequency", this.lastVibrationFrequency);
/*     */     
/*  54 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/*     */   }
/*     */ 
/*     */   
/*     */   public VibrationSystem.Data getVibrationData() {
/*  59 */     return this.vibrationData;
/*     */   }
/*     */ 
/*     */   
/*     */   public VibrationSystem.User getVibrationUser() {
/*  64 */     return this.vibrationUser;
/*     */   }
/*     */   
/*     */   public int getLastVibrationFrequency() {
/*  68 */     return this.lastVibrationFrequency;
/*     */   }
/*     */   
/*     */   public void setLastVibrationFrequency(int lastVibrationFrequency) {
/*  72 */     this.lastVibrationFrequency = lastVibrationFrequency;
/*     */   }
/*     */ 
/*     */   
/*     */   public VibrationSystem.Listener getListener() {
/*  77 */     return this.vibrationListener;
/*     */   }
/*     */   
/*     */   protected class VibrationUser
/*     */     implements VibrationSystem.User {
/*     */     public static final int LISTENER_RANGE = 8;
/*     */     protected final BlockPos blockPos;
/*     */     private final PositionSource positionSource;
/*     */     
/*     */     public VibrationUser(BlockPos blockPos) {
/*  87 */       this.blockPos = blockPos;
/*  88 */       this.positionSource = (PositionSource)new BlockPositionSource(blockPos);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getListenerRadius() {
/*  93 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public PositionSource getPositionSource() {
/*  98 */       return this.positionSource;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canTriggerAvoidVibration() {
/* 103 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 112 */       if (pos.equals(this.blockPos) && (event.is((Holder)GameEvent.BLOCK_DESTROY) || event.is((Holder)GameEvent.BLOCK_PLACE))) {
/* 113 */         return false;
/*     */       }
/*     */       
/* 116 */       if (VibrationSystem.getGameEventFrequency(event) == 0) {
/* 117 */         return false;
/*     */       }
/*     */       
/* 120 */       return SculkSensorBlock.canActivate(SculkSensorBlockEntity.this.getBlockState());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 125 */       BlockState state = SculkSensorBlockEntity.this.getBlockState();
/* 126 */       if (SculkSensorBlock.canActivate(state)) {
/* 127 */         int eventFrequency = VibrationSystem.getGameEventFrequency(event);
/* 128 */         SculkSensorBlockEntity.this.setLastVibrationFrequency(eventFrequency);
/* 129 */         int calculatedPower = VibrationSystem.getRedstoneStrengthForDistance(receivingDistance, getListenerRadius());
/* 130 */         Block block = state.getBlock(); if (block instanceof SculkSensorBlock) { SculkSensorBlock sculkSensorBlock = (SculkSensorBlock)block;
/* 131 */           sculkSensorBlock.activate(sourceEntity, (Level)level, this.blockPos, state, calculatedPower, eventFrequency); }
/*     */       
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onDataChanged() {
/* 138 */       SculkSensorBlockEntity.this.setChanged();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresAdjacentChunksToBeTicking() {
/* 143 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/SculkSensorBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */