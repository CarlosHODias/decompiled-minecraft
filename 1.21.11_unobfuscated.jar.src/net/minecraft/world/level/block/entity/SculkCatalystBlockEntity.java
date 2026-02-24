/*     */ package net.minecraft.world.level.block.entity;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.SculkCatalystBlock;
/*     */ import net.minecraft.world.level.block.SculkSpreader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.BlockPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SculkCatalystBlockEntity extends BlockEntity implements GameEventListener.Provider<SculkCatalystBlockEntity.CatalystListener> {
/*     */   public SculkCatalystBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  34 */     super(BlockEntityType.SCULK_CATALYST, worldPosition, blockState);
/*  35 */     this.catalystListener = new CatalystListener(blockState, (PositionSource)new BlockPositionSource(worldPosition));
/*     */   }
/*     */   private final CatalystListener catalystListener;
/*     */   public static void serverTick(Level level, BlockPos pos, BlockState state, SculkCatalystBlockEntity entity) {
/*  39 */     entity.catalystListener.getSculkSpreader().updateCursors((LevelAccessor)level, pos, level.getRandom(), true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  44 */     super.loadAdditional(input);
/*     */     
/*  46 */     this.catalystListener.sculkSpreader.load(input);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  51 */     this.catalystListener.sculkSpreader.save(output);
/*  52 */     super.saveAdditional(output);
/*     */   }
/*     */ 
/*     */   
/*     */   public CatalystListener getListener() {
/*  57 */     return this.catalystListener;
/*     */   }
/*     */   
/*     */   public static class CatalystListener implements GameEventListener {
/*     */     public static final int PULSE_TICKS = 8;
/*     */     private final SculkSpreader sculkSpreader;
/*     */     private final BlockState blockState;
/*     */     private final PositionSource positionSource;
/*     */     
/*     */     public CatalystListener(BlockState blockState, PositionSource positionSource) {
/*  67 */       this.blockState = blockState;
/*  68 */       this.positionSource = positionSource;
/*  69 */       this.sculkSpreader = SculkSpreader.createLevelSpreader();
/*     */     }
/*     */ 
/*     */     
/*     */     public PositionSource getListenerSource() {
/*  74 */       return this.positionSource;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getListenerRadius() {
/*  79 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public GameEventListener.DeliveryMode getDeliveryMode() {
/*  84 */       return GameEventListener.DeliveryMode.BY_DISTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/*  89 */       if (event.is((Holder)GameEvent.ENTITY_DIE)) { Entity entity = context.sourceEntity(); if (entity instanceof LivingEntity) { LivingEntity mob = (LivingEntity)entity;
/*  90 */           if (!mob.wasExperienceConsumed()) {
/*  91 */             DamageSource lastDamageSource = mob.getLastDamageSource();
/*  92 */             int experienceWouldDrop = mob.getExperienceReward(level, (Entity)Optionull.map(lastDamageSource, DamageSource::getEntity));
/*  93 */             if (mob.shouldDropExperience() && experienceWouldDrop > 0) {
/*  94 */               this.sculkSpreader.addCursors(BlockPos.containing((Position)sourcePosition.relative(net.minecraft.core.Direction.UP, 0.5D)), experienceWouldDrop);
/*  95 */               tryAwardItSpreadsAdvancement((Level)level, mob);
/*     */             } 
/*  97 */             mob.skipDropExperience();
/*  98 */             this.positionSource.getPosition((Level)level).ifPresent(vec3 -> bloom(level, BlockPos.containing((Position)level), this.blockState, level.getRandom()));
/*     */           } 
/* 100 */           return true; }
/*     */          }
/*     */       
/* 103 */       return false;
/*     */     }
/*     */     
/*     */     @com.google.common.annotations.VisibleForTesting
/*     */     public SculkSpreader getSculkSpreader() {
/* 108 */       return this.sculkSpreader;
/*     */     }
/*     */     
/*     */     private void bloom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
/* 112 */       level.setBlock(pos, (BlockState)state.setValue((Property)SculkCatalystBlock.PULSE, true), 3);
/* 113 */       level.scheduleTick(pos, state.getBlock(), 8);
/*     */       
/* 115 */       level.sendParticles((ParticleOptions)ParticleTypes.SCULK_SOUL, pos.getX() + 0.5D, pos.getY() + 1.15D, pos.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
/*     */       
/* 117 */       level.playSound(null, pos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + random.nextFloat() * 0.4F);
/*     */     }
/*     */     
/*     */     private void tryAwardItSpreadsAdvancement(Level level, LivingEntity mob) {
/* 121 */       LivingEntity lastHurtByMob = mob.getLastHurtByMob();
/* 122 */       if (lastHurtByMob instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)lastHurtByMob;
/* 123 */         DamageSource damageSource = (mob.getLastDamageSource() == null) ? level.damageSources().playerAttack((Player)player) : mob.getLastDamageSource();
/* 124 */         CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.trigger(player, (Entity)mob, damageSource); }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/SculkCatalystBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */