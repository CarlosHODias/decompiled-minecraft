/*     */ package net.minecraft.client.renderer;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.PowerParticleOption;
/*     */ import net.minecraft.core.particles.SculkChargeParticleOptions;
/*     */ import net.minecraft.core.particles.ShriekParticleOption;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.core.particles.SpellParticleOption;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BrushableBlock;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.PointedDripstoneBlock;
/*     */ import net.minecraft.world.level.block.SculkShriekerBlock;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
/*     */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LevelEventHandler {
/*     */   private final Minecraft minecraft;
/*     */   private final ClientLevel level;
/*  60 */   private final Map<BlockPos, SoundInstance> playingJukeboxSongs = new HashMap<>();
/*     */   
/*     */   public LevelEventHandler(Minecraft minecraft, ClientLevel level) {
/*  63 */     this.minecraft = minecraft;
/*  64 */     this.level = level;
/*     */   }
/*     */   
/*     */   public void globalLevelEvent(int type, BlockPos pos, int data) { Camera camera;
/*  68 */     switch (type) {
/*     */       case 1023:
/*     */       case 1028:
/*     */       case 1038:
/*  72 */         camera = this.minecraft.gameRenderer.getMainCamera();
/*  73 */         if (camera.isInitialized()) {
/*     */           
/*  75 */           Vec3 directionToEvent = Vec3.atCenterOf((Vec3i)pos).subtract(camera.position()).normalize();
/*     */           
/*  77 */           Vec3 soundPos = camera.position().add(directionToEvent.scale(2.0D));
/*  78 */           if (type == 1023) {
/*  79 */             this.level.playLocalSound(soundPos.x, soundPos.y, soundPos.z, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0F, 1.0F, false); break;
/*  80 */           }  if (type == 1038) {
/*  81 */             this.level.playLocalSound(soundPos.x, soundPos.y, soundPos.z, SoundEvents.END_PORTAL_SPAWN, SoundSource.HOSTILE, 1.0F, 1.0F, false); break;
/*     */           } 
/*  83 */           this.level.playLocalSound(soundPos.x, soundPos.y, soundPos.z, SoundEvents.ENDER_DRAGON_DEATH, SoundSource.HOSTILE, 5.0F, 1.0F, false);
/*     */         }  break;
/*     */     }  } public void levelEvent(int eventType, BlockPos pos, int data) { double x; Vec3 particlePos; BlockState blockState; int i; float red; BlockState blockStateForBrushing; double y; float green; int j, count; float blue; Block block; BlockEntity blockEntity; boolean isSolid; int k; BlockState state; double z; ParticleType<SpellParticleOption> particleType; int particleCount; boolean isWaterlogged; int n;
/*     */     float spread;
/*     */     int m, i1;
/*     */     double angle;
/*     */     float speed;
/*     */     int i2;
/*  91 */     RandomSource random = this.level.random;
/*  92 */     switch (eventType) {
/*     */       case 1035:
/*  94 */         this.level.playLocalSound(pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1033:
/*  97 */         this.level.playLocalSound(pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1034:
/* 100 */         this.level.playLocalSound(pos, SoundEvents.CHORUS_FLOWER_DEATH, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1032:
/* 103 */         this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRAVEL, random.nextFloat() * 0.4F + 0.8F, 0.25F));
/*     */         break;
/*     */       case 1001:
/* 106 */         this.level.playLocalSound(pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.0F, 1.2F, false);
/*     */         break;
/*     */       case 1000:
/* 109 */         this.level.playLocalSound(pos, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1049:
/* 112 */         this.level.playLocalSound(pos, SoundEvents.CRAFTER_CRAFT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1050:
/* 115 */         this.level.playLocalSound(pos, SoundEvents.CRAFTER_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1004:
/* 118 */         this.level.playLocalSound(pos, SoundEvents.FIREWORK_ROCKET_SHOOT, SoundSource.NEUTRAL, 1.0F, 1.2F, false);
/*     */         break;
/*     */       case 1002:
/* 121 */         this.level.playLocalSound(pos, SoundEvents.DISPENSER_LAUNCH, SoundSource.BLOCKS, 1.0F, 1.2F, false);
/*     */         break;
/*     */       case 1051:
/* 124 */         this.level.playLocalSound(pos, SoundEvents.WIND_CHARGE_THROW, SoundSource.BLOCKS, 0.5F, 0.4F / (this.level.getRandom().nextFloat() * 0.4F + 0.8F), false);
/*     */         break;
/*     */       case 2010:
/* 127 */         shootParticles(data, pos, random, ParticleTypes.WHITE_SMOKE);
/*     */         break;
/*     */       case 2000:
/* 130 */         shootParticles(data, pos, random, ParticleTypes.SMOKE);
/*     */         break;
/*     */       case 2003:
/* 133 */         x = pos.getX() + 0.5D;
/* 134 */         y = pos.getY();
/* 135 */         z = pos.getZ() + 0.5D;
/*     */         
/* 137 */         for (i1 = 0; i1 < 8; i1++) {
/* 138 */           this.level.addParticle((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, new ItemStack((ItemLike)Items.ENDER_EYE)), x, y, z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
/*     */         }
/* 140 */         for (angle = 0.0D; angle < 6.283185307179586D; angle += 0.15707963267948966D) {
/* 141 */           this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x + Math.cos(angle) * 5.0D, y - 0.4D, z + Math.sin(angle) * 5.0D, Math.cos(angle) * -5.0D, 0.0D, Math.sin(angle) * -5.0D);
/* 142 */           this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x + Math.cos(angle) * 5.0D, y - 0.4D, z + Math.sin(angle) * 5.0D, Math.cos(angle) * -7.0D, 0.0D, Math.sin(angle) * -7.0D);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 2002:
/*     */       case 2007:
/* 148 */         particlePos = Vec3.atBottomCenterOf((Vec3i)pos);
/*     */         
/* 150 */         for (i = 0; i < 8; i++) {
/* 151 */           this.level.addParticle((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, new ItemStack((ItemLike)Items.SPLASH_POTION)), particlePos.x, particlePos.y, particlePos.z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
/*     */         }
/*     */         
/* 154 */         red = (data >> 16 & 0xFF) / 255.0F;
/* 155 */         green = (data >> 8 & 0xFF) / 255.0F;
/* 156 */         blue = (data >> 0 & 0xFF) / 255.0F;
/*     */         
/* 158 */         particleType = (eventType == 2007) ? ParticleTypes.INSTANT_EFFECT : ParticleTypes.EFFECT;
/* 159 */         for (n = 0; n < 100; n++) {
/* 160 */           double dist = random.nextDouble() * 4.0D;
/* 161 */           double d1 = random.nextDouble() * Math.PI * 2.0D;
/* 162 */           double velocityX = Math.cos(d1) * dist;
/* 163 */           double velocityY = 0.01D + random.nextDouble() * 0.5D;
/* 164 */           double velocityZ = Math.sin(d1) * dist;
/*     */           
/* 166 */           float randomBrightness = 0.75F + random.nextFloat() * 0.25F;
/* 167 */           SpellParticleOption particle = SpellParticleOption.create(particleType, red * randomBrightness, green * randomBrightness, blue * randomBrightness, (float)dist);
/* 168 */           this.level.addParticle((ParticleOptions)particle, particlePos.x + velocityX * 0.1D, particlePos.y + 0.3D, particlePos.z + velocityZ * 0.1D, velocityX, velocityY, velocityZ);
/*     */         } 
/* 170 */         this.level.playLocalSound(pos, SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       
/*     */       case 2001:
/* 174 */         blockState = Block.stateById(data);
/* 175 */         if (!blockState.isAir()) {
/* 176 */           SoundType soundType = blockState.getSoundType();
/* 177 */           this.level.playLocalSound(pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F, false);
/*     */         } 
/* 179 */         this.level.addDestroyBlockEffect(pos, blockState);
/*     */         break;
/*     */       case 3008:
/* 182 */         blockStateForBrushing = Block.stateById(data);
/* 183 */         block = blockStateForBrushing.getBlock(); if (block instanceof BrushableBlock) { BrushableBlock brushableBlock = (BrushableBlock)block;
/* 184 */           this.level.playLocalSound(pos, brushableBlock.getBrushCompletedSound(), SoundSource.PLAYERS, 1.0F, 1.0F, false); }
/*     */         
/* 186 */         this.level.addDestroyBlockEffect(pos, blockStateForBrushing);
/*     */         break;
/*     */       case 2004:
/* 189 */         for (j = 0; j < 20; j++) {
/* 190 */           double d1 = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 191 */           double d2 = pos.getY() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 192 */           double d3 = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/*     */           
/* 194 */           this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/* 195 */           this.level.addParticle((ParticleOptions)ParticleTypes.FLAME, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */         break;
/*     */       case 3011:
/* 199 */         TrialSpawner.addSpawnParticles((Level)this.level, pos, random, (TrialSpawner.FlameParticle.decode(data)).particleType);
/*     */         break;
/*     */       case 3012:
/* 202 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 203 */         TrialSpawner.addSpawnParticles((Level)this.level, pos, random, (TrialSpawner.FlameParticle.decode(data)).particleType);
/*     */         break;
/*     */       case 3021:
/* 206 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 207 */         TrialSpawner.addSpawnParticles((Level)this.level, pos, random, (TrialSpawner.FlameParticle.decode(data)).particleType);
/*     */         break;
/*     */       case 3013:
/* 210 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 211 */         TrialSpawner.addDetectPlayerParticles((Level)this.level, pos, random, data, (ParticleOptions)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER);
/*     */         break;
/*     */       case 3019:
/* 214 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 215 */         TrialSpawner.addDetectPlayerParticles((Level)this.level, pos, random, data, (ParticleOptions)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS);
/*     */         break;
/*     */       case 3020:
/* 218 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundSource.BLOCKS, (data == 0) ? 0.3F : 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 219 */         TrialSpawner.addDetectPlayerParticles((Level)this.level, pos, random, 0, (ParticleOptions)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS);
/* 220 */         TrialSpawner.addBecomeOminousParticles((Level)this.level, pos, random);
/*     */         break;
/*     */       case 3014:
/* 223 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_EJECT_ITEM, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 224 */         TrialSpawner.addEjectItemParticles((Level)this.level, pos, random);
/*     */         break;
/*     */       case 3017:
/* 227 */         TrialSpawner.addEjectItemParticles((Level)this.level, pos, random);
/*     */         break;
/*     */       case 3015:
/* 230 */         blockEntity = this.level.getBlockEntity(pos); if (blockEntity instanceof VaultBlockEntity) { VaultBlockEntity entity = (VaultBlockEntity)blockEntity;
/* 231 */           VaultBlockEntity.Client.emitActivationParticles((Level)this.level, entity.getBlockPos(), entity.getBlockState(), entity.getSharedData(), (data == 0) ? (ParticleOptions)ParticleTypes.SMALL_FLAME : (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME);
/* 232 */           this.level.playLocalSound(pos, SoundEvents.VAULT_ACTIVATE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true); }
/*     */         
/*     */         break;
/*     */       case 3016:
/* 236 */         VaultBlockEntity.Client.emitDeactivationParticles((Level)this.level, pos, (data == 0) ? (ParticleOptions)ParticleTypes.SMALL_FLAME : (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME);
/* 237 */         this.level.playLocalSound(pos, SoundEvents.VAULT_DEACTIVATE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/*     */         break;
/*     */       case 3018:
/* 240 */         for (j = 0; j < 10; j++) {
/* 241 */           double velocityX = random.nextGaussian() * 0.02D;
/* 242 */           double velocityY = random.nextGaussian() * 0.02D;
/* 243 */           double velocityZ = random.nextGaussian() * 0.02D;
/* 244 */           this.level.addParticle((ParticleOptions)ParticleTypes.POOF, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), velocityX, velocityY, velocityZ);
/*     */         } 
/* 246 */         this.level.playLocalSound(pos, SoundEvents.COBWEB_PLACE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/*     */         break;
/*     */       case 1505:
/* 249 */         BoneMealItem.addGrowthParticles((LevelAccessor)this.level, pos, data);
/* 250 */         this.level.playLocalSound(pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 2011:
/* 253 */         ParticleUtils.spawnParticleInBlock((LevelAccessor)this.level, pos, data, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER);
/*     */         break;
/*     */       case 2012:
/* 256 */         ParticleUtils.spawnParticleInBlock((LevelAccessor)this.level, pos, data, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER);
/*     */         break;
/*     */       case 3009:
/* 259 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.EGG_CRACK, (IntProvider)UniformInt.of(3, 6));
/*     */         break;
/*     */       case 3002:
/* 262 */         if (data >= 0 && data < Direction.Axis.VALUES.length) {
/* 263 */           ParticleUtils.spawnParticlesAlongAxis(Direction.Axis.VALUES[data], (Level)this.level, pos, 0.125D, (ParticleOptions)ParticleTypes.ELECTRIC_SPARK, UniformInt.of(10, 19)); break;
/*     */         } 
/* 265 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.ELECTRIC_SPARK, (IntProvider)UniformInt.of(3, 5));
/*     */         break;
/*     */       
/*     */       case 2013:
/* 269 */         ParticleUtils.spawnSmashAttackParticles((LevelAccessor)this.level, pos, data);
/*     */         break;
/*     */       case 3006:
/* 272 */         count = data >> 6;
/* 273 */         if (count > 0) {
/*     */           
/* 275 */           if (random.nextFloat() < 0.3F + count * 0.1F) {
/* 276 */             float volume = 0.15F + 0.02F * count * count * random.nextFloat();
/* 277 */             float pitch = 0.4F + 0.3F * count * random.nextFloat();
/* 278 */             this.level.playLocalSound(pos, SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, volume, pitch, false);
/*     */           } 
/* 280 */           byte particleData = (byte)(data & 0x3F);
/* 281 */           UniformInt uniformInt = UniformInt.of(0, count);
/* 282 */           float speedVar = 0.005F;
/*     */ 
/*     */           
/*     */           Supplier<Vec3> speedSupplier = () -> new Vec3(Mth.nextDouble(random, -0.004999999888241291D, 0.004999999888241291D), Mth.nextDouble(random, -0.004999999888241291D, 0.004999999888241291D), Mth.nextDouble(random, -0.004999999888241291D, 0.004999999888241291D));
/*     */ 
/*     */           
/* 288 */           if (particleData == 0) {
/* 289 */             for (Direction direction : Direction.values()) {
/* 290 */               float fullBlockRotation = (direction == Direction.DOWN) ? 3.1415927F : 0.0F;
/* 291 */               double fullBlockFactor = (direction.getAxis() == Direction.Axis.Y) ? 0.65D : 0.57D;
/* 292 */               ParticleUtils.spawnParticlesOnBlockFace((Level)this.level, pos, (ParticleOptions)new SculkChargeParticleOptions(fullBlockRotation), (IntProvider)uniformInt, direction, speedSupplier, fullBlockFactor);
/*     */             }  break;
/*     */           } 
/* 295 */           for (Direction direction : (Iterable<Direction>)MultifaceBlock.unpack(particleData)) {
/* 296 */             float facesBlockRotation = (direction == Direction.UP) ? 3.1415927F : 0.0F;
/* 297 */             double facesBlockFactor = 0.35D;
/* 298 */             ParticleUtils.spawnParticlesOnBlockFace((Level)this.level, pos, (ParticleOptions)new SculkChargeParticleOptions(facesBlockRotation), (IntProvider)uniformInt, direction, speedSupplier, 0.35D);
/*     */           } 
/*     */           break;
/*     */         } 
/* 302 */         this.level.playLocalSound(pos, SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/* 303 */         isSolid = this.level.getBlockState(pos).isCollisionShapeFullBlock((BlockGetter)this.level, pos);
/* 304 */         particleCount = isSolid ? 40 : 20;
/* 305 */         spread = isSolid ? 0.45F : 0.25F;
/* 306 */         speed = 0.07F;
/* 307 */         for (i2 = 0; i2 < particleCount; i2++) {
/* 308 */           float velocityX = 2.0F * random.nextFloat() - 1.0F;
/* 309 */           float velocityY = 2.0F * random.nextFloat() - 1.0F;
/* 310 */           float velocityZ = 2.0F * random.nextFloat() - 1.0F;
/* 311 */           this.level.addParticle((ParticleOptions)ParticleTypes.SCULK_CHARGE_POP, 
/*     */               
/* 313 */               pos.getX() + 0.5D + (velocityX * spread), pos.getY() + 0.5D + (velocityY * spread), pos.getZ() + 0.5D + (velocityZ * spread), (velocityX * 0.07F), (velocityY * 0.07F), (velocityZ * 0.07F));
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 3007:
/* 320 */         for (k = 0; k < 10; k++) {
/* 321 */           this.level.addParticle((ParticleOptions)new ShriekParticleOption(k * 5), pos.getX() + 0.5D, pos.getY() + SculkShriekerBlock.TOP_Y, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
/*     */         }
/* 323 */         state = this.level.getBlockState(pos);
/* 324 */         isWaterlogged = (state.hasProperty((Property)BlockStateProperties.WATERLOGGED) && (Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED));
/*     */         
/* 326 */         if (!isWaterlogged) {
/* 327 */           this.level.playLocalSound(pos.getX() + 0.5D, pos.getY() + SculkShriekerBlock.TOP_Y, pos.getZ() + 0.5D, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 2.0F, 0.6F + this.level.random.nextFloat() * 0.4F, false);
/*     */         }
/*     */         break;
/*     */       case 3003:
/* 331 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.WAX_ON, (IntProvider)UniformInt.of(3, 5));
/* 332 */         this.level.playLocalSound(pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 3004:
/* 335 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.WAX_OFF, (IntProvider)UniformInt.of(3, 5));
/*     */         break;
/*     */       case 3005:
/* 338 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.SCRAPE, (IntProvider)UniformInt.of(3, 5));
/*     */         break;
/*     */       case 2008:
/* 341 */         this.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
/*     */         break;
/*     */       case 1500:
/* 344 */         ComposterBlock.handleFill((Level)this.level, pos, (data > 0));
/*     */         break;
/*     */       case 1504:
/* 347 */         PointedDripstoneBlock.spawnDripParticle((Level)this.level, pos, this.level.getBlockState(pos));
/*     */         break;
/*     */       case 1501:
/* 350 */         this.level.playLocalSound(pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
/* 351 */         for (m = 0; m < 8; m++) {
/* 352 */           this.level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, pos.getX() + random.nextDouble(), pos.getY() + 1.2D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */         break;
/*     */       case 1502:
/* 356 */         this.level.playLocalSound(pos, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
/* 357 */         for (m = 0; m < 5; m++) {
/* 358 */           double d1 = pos.getX() + random.nextDouble() * 0.6D + 0.2D;
/* 359 */           double d2 = pos.getY() + random.nextDouble() * 0.6D + 0.2D;
/* 360 */           double d3 = pos.getZ() + random.nextDouble() * 0.6D + 0.2D;
/*     */           
/* 362 */           this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */         break;
/*     */       case 1503:
/* 366 */         this.level.playLocalSound(pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/* 367 */         for (m = 0; m < 16; m++) {
/* 368 */           double d1 = pos.getX() + (5.0D + random.nextDouble() * 6.0D) / 16.0D;
/* 369 */           double d2 = pos.getY() + 0.8125D;
/* 370 */           double d3 = pos.getZ() + (5.0D + random.nextDouble() * 6.0D) / 16.0D;
/*     */           
/* 372 */           this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 2006:
/* 377 */         for (m = 0; m < 200; m++) {
/* 378 */           float dist = random.nextFloat() * 4.0F;
/* 379 */           float f1 = random.nextFloat() * 6.2831855F;
/* 380 */           double velocityX = (Mth.cos(f1) * dist);
/* 381 */           double velocityY = 0.01D + random.nextDouble() * 0.5D;
/* 382 */           double velocityZ = (Mth.sin(f1) * dist);
/*     */           
/* 384 */           this.level.addParticle((ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, dist), pos.getX() + velocityX * 0.1D, pos.getY() + 0.3D, pos.getZ() + velocityZ * 0.1D, velocityX, velocityY, velocityZ);
/*     */         } 
/*     */         
/* 387 */         if (data == 1) {
/* 388 */           this.level.playLocalSound(pos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         }
/*     */         break;
/*     */       case 2009:
/* 392 */         for (m = 0; m < 8; m++) {
/* 393 */           this.level.addParticle((ParticleOptions)ParticleTypes.CLOUD, pos.getX() + random.nextDouble(), pos.getY() + 1.2D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */         break;
/*     */       case 1009:
/* 397 */         if (data == 0) {
/* 398 */           this.level.playLocalSound(pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false); break;
/* 399 */         }  if (data == 1) {
/* 400 */           this.level.playLocalSound(pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.7F, 1.6F + (random.nextFloat() - random.nextFloat()) * 0.4F, false);
/*     */         }
/*     */         break;
/*     */       case 1029:
/* 404 */         this.level.playLocalSound(pos, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1030:
/* 407 */         this.level.playLocalSound(pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1044:
/* 410 */         this.level.playLocalSound(pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1.0F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1031:
/* 413 */         this.level.playLocalSound(pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1039:
/* 416 */         this.level.playLocalSound(pos, SoundEvents.PHANTOM_BITE, SoundSource.HOSTILE, 0.3F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1010:
/* 419 */         this.level.registryAccess().lookupOrThrow(Registries.JUKEBOX_SONG).get(data).ifPresent(song -> playJukeboxSong((Holder<JukeboxSong>)pos, pos));
/*     */         break;
/*     */       case 1011:
/* 422 */         stopJukeboxSongAndNotifyNearby(pos);
/*     */         break;
/*     */       case 1015:
/* 425 */         this.level.playLocalSound(pos, SoundEvents.GHAST_WARN, SoundSource.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1017:
/* 428 */         this.level.playLocalSound(pos, SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1016:
/* 431 */         this.level.playLocalSound(pos, SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1019:
/* 434 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1022:
/* 437 */         this.level.playLocalSound(pos, SoundEvents.WITHER_BREAK_BLOCK, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1021:
/* 440 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1020:
/* 443 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1018:
/* 446 */         this.level.playLocalSound(pos, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1024:
/* 449 */         this.level.playLocalSound(pos, SoundEvents.WITHER_SHOOT, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1026:
/* 452 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1027:
/* 455 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1040:
/* 458 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1041:
/* 461 */         this.level.playLocalSound(pos, SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1025:
/* 464 */         this.level.playLocalSound(pos, SoundEvents.BAT_TAKEOFF, SoundSource.NEUTRAL, 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1042:
/* 467 */         this.level.playLocalSound(pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1043:
/* 470 */         this.level.playLocalSound(pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 3000:
/* 473 */         this.level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, true, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
/* 474 */         this.level.playLocalSound(pos, SoundEvents.END_GATEWAY_SPAWN, SoundSource.BLOCKS, 10.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
/*     */         break;
/*     */       case 3001:
/* 477 */         this.level.playLocalSound(pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 64.0F, 0.8F + this.level.random.nextFloat() * 0.3F, false);
/*     */         break;
/*     */       case 1045:
/* 480 */         this.level.playLocalSound(pos, SoundEvents.POINTED_DRIPSTONE_LAND, SoundSource.BLOCKS, 2.0F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1046:
/* 483 */         this.level.playLocalSound(pos, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON, SoundSource.BLOCKS, 2.0F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1047:
/* 486 */         this.level.playLocalSound(pos, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, SoundSource.BLOCKS, 2.0F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1048:
/* 489 */         this.level.playLocalSound(pos, SoundEvents.SKELETON_CONVERTED_TO_STRAY, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */     }  }
/*     */ 
/*     */   
/*     */   private void shootParticles(int data, BlockPos pos, RandomSource random, SimpleParticleType particle) {
/* 495 */     Direction direction = Direction.from3DDataValue(data);
/* 496 */     int normalX = direction.getStepX();
/* 497 */     int normalY = direction.getStepY();
/* 498 */     int normalZ = direction.getStepZ();
/*     */     
/* 500 */     for (int i = 0; i < 10; i++) {
/* 501 */       double pow = random.nextDouble() * 0.2D + 0.01D;
/* 502 */       double x = pos.getX() + normalX * 0.6D + 0.5D + normalX * 0.01D + (random.nextDouble() - 0.5D) * normalZ * 0.5D;
/* 503 */       double y = pos.getY() + normalY * 0.6D + 0.5D + normalY * 0.01D + (random.nextDouble() - 0.5D) * normalY * 0.5D;
/* 504 */       double z = pos.getZ() + normalZ * 0.6D + 0.5D + normalZ * 0.01D + (random.nextDouble() - 0.5D) * normalX * 0.5D;
/* 505 */       double velocityX = normalX * pow + random.nextGaussian() * 0.01D;
/* 506 */       double velocityY = normalY * pow + random.nextGaussian() * 0.01D;
/* 507 */       double velocityZ = normalZ * pow + random.nextGaussian() * 0.01D;
/* 508 */       this.level.addParticle((ParticleOptions)particle, x, y, z, velocityX, velocityY, velocityZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playJukeboxSong(Holder<JukeboxSong> songHolder, BlockPos pos) {
/* 513 */     stopJukeboxSong(pos);
/*     */     
/* 515 */     JukeboxSong song = (JukeboxSong)songHolder.value();
/* 516 */     SoundEvent sound = (SoundEvent)song.soundEvent().value();
/* 517 */     SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forJukeboxSong(sound, Vec3.atCenterOf((Vec3i)pos));
/* 518 */     this.playingJukeboxSongs.put(pos, simpleSoundInstance);
/* 519 */     this.minecraft.getSoundManager().play((SoundInstance)simpleSoundInstance);
/* 520 */     this.minecraft.gui.setNowPlaying(song.description());
/* 521 */     notifyNearbyEntities((Level)this.level, pos, true);
/*     */   }
/*     */   
/*     */   private void stopJukeboxSong(BlockPos pos) {
/* 525 */     SoundInstance removedInstance = this.playingJukeboxSongs.remove(pos);
/* 526 */     if (removedInstance != null) {
/* 527 */       this.minecraft.getSoundManager().stop(removedInstance);
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopJukeboxSongAndNotifyNearby(BlockPos pos) {
/* 532 */     stopJukeboxSong(pos);
/* 533 */     notifyNearbyEntities((Level)this.level, pos, false);
/*     */   }
/*     */   
/*     */   private void notifyNearbyEntities(Level level, BlockPos pos, boolean isPlaying) {
/* 537 */     List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new net.minecraft.world.phys.AABB(pos).inflate(3.0D));
/* 538 */     for (LivingEntity entity : entities)
/* 539 */       entity.setRecordPlayingNearby(pos, isPlaying); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/LevelEventHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */