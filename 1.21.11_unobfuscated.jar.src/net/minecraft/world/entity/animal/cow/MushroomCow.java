/*     */ package net.minecraft.world.entity.animal.cow;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SpellParticleOption;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class MushroomCow extends AbstractCow implements Shearable {
/*  56 */   private static final net.minecraft.network.syncher.EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(MushroomCow.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int MUTATE_CHANCE = 1024;
/*     */   
/*     */   private static final String TAG_STEW_EFFECTS = "stew_effects";
/*     */   private SuspiciousStewEffects stewEffects;
/*     */   private UUID lastLightningBoltUUID;
/*     */   
/*     */   public MushroomCow(EntityType<? extends MushroomCow> type, Level level) {
/*  65 */     super((EntityType)type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/*  70 */     if (level.getBlockState(pos.below()).is(Blocks.MYCELIUM)) {
/*  71 */       return 10.0F;
/*     */     }
/*  73 */     return level.getPathfindingCostFromLightLevels(pos);
/*     */   }
/*     */   
/*     */   public static boolean checkMushroomSpawnRules(EntityType<MushroomCow> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  77 */     return (level.getBlockState(pos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) && 
/*  78 */       isBrightEnoughToSpawn((BlockAndTintGetter)level, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/*  83 */     UUID lightningBoltUUID = lightningBolt.getUUID();
/*  84 */     if (!lightningBoltUUID.equals(this.lastLightningBoltUUID)) {
/*  85 */       setVariant((getVariant() == Variant.RED) ? Variant.BROWN : Variant.RED);
/*  86 */       this.lastLightningBoltUUID = lightningBoltUUID;
/*  87 */       playSound(SoundEvents.MOOSHROOM_CONVERT, 2.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  93 */     super.defineSynchedData(entityData);
/*     */     
/*  95 */     entityData.define(DATA_TYPE, Variant.DEFAULT.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 100 */     ItemStack itemStack = player.getItemInHand(hand);
/* 101 */     if (itemStack.is(Items.BOWL) && !isBaby()) {
/*     */       ItemStack stew;
/*     */       SoundEvent milkSound;
/*     */       boolean isSuspicious = false;
/* 105 */       if (this.stewEffects != null) {
/* 106 */         isSuspicious = true;
/* 107 */         stew = new ItemStack((ItemLike)Items.SUSPICIOUS_STEW);
/* 108 */         stew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, this.stewEffects);
/* 109 */         this.stewEffects = null;
/*     */       } else {
/* 111 */         stew = new ItemStack((ItemLike)Items.MUSHROOM_STEW);
/*     */       } 
/*     */       
/* 114 */       ItemStack bowlOrStew = ItemUtils.createFilledResult(itemStack, player, stew, false);
/* 115 */       player.setItemInHand(hand, bowlOrStew);
/*     */ 
/*     */       
/* 118 */       if (isSuspicious) {
/* 119 */         milkSound = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
/*     */       } else {
/* 121 */         milkSound = SoundEvents.MOOSHROOM_MILK;
/*     */       } 
/*     */       
/* 124 */       playSound(milkSound, 1.0F, 1.0F);
/*     */       
/* 126 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 129 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/* 130 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 131 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/* 132 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)player);
/* 133 */         itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot()); }
/*     */       
/* 135 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     if (getVariant() == Variant.BROWN) {
/* 140 */       Optional<SuspiciousStewEffects> effectsFromItemStack = getEffectsFromItemStack(itemStack);
/* 141 */       if (effectsFromItemStack.isEmpty()) {
/* 142 */         return super.mobInteract(player, hand);
/*     */       }
/* 144 */       if (this.stewEffects != null) {
/* 145 */         for (int i = 0; i < 2; i++) {
/* 146 */           level().addParticle((ParticleOptions)ParticleTypes.SMOKE, getX() + this.random.nextDouble() / 2.0D, getY(0.5D), getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
/*     */         }
/*     */       } else {
/* 149 */         itemStack.consume(1, (LivingEntity)player);
/* 150 */         SpellParticleOption particle = SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F);
/* 151 */         for (int i = 0; i < 4; i++) {
/* 152 */           level().addParticle((ParticleOptions)particle, getX() + this.random.nextDouble() / 2.0D, getY(0.5D), getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
/*     */         }
/* 154 */         this.stewEffects = effectsFromItemStack.get();
/* 155 */         playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
/*     */       } 
/* 157 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 160 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 165 */     level.playSound(null, (Entity)this, SoundEvents.MOOSHROOM_SHEAR, soundSource, 1.0F, 1.0F);
/*     */     
/* 167 */     convertTo(EntityType.COW, ConversionParams.single((Mob)this, false, false), cow -> {
/*     */           level.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, getX(), getY(0.5D), getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */           dropFromShearingLootTable(level, BuiltInLootTables.SHEAR_MOOSHROOM, level, ());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 180 */     return (isAlive() && !isBaby());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 185 */     super.addAdditionalSaveData(output);
/* 186 */     output.store("Type", Variant.CODEC, getVariant());
/*     */     
/* 188 */     output.storeNullable("stew_effects", SuspiciousStewEffects.CODEC, this.stewEffects);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 193 */     super.readAdditionalSaveData(input);
/* 194 */     setVariant(input.read("Type", Variant.CODEC).orElse(Variant.DEFAULT));
/*     */     
/* 196 */     this.stewEffects = input.read("stew_effects", SuspiciousStewEffects.CODEC).orElse(null);
/*     */   }
/*     */   
/*     */   private Optional<SuspiciousStewEffects> getEffectsFromItemStack(ItemStack itemStack) {
/* 200 */     SuspiciousEffectHolder effectHolder = SuspiciousEffectHolder.tryGet((ItemLike)itemStack.getItem());
/* 201 */     if (effectHolder != null) {
/* 202 */       return Optional.of(effectHolder.getSuspiciousEffects());
/*     */     }
/* 204 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 208 */     this.entityData.set(DATA_TYPE, variant.id);
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/* 212 */     return Variant.byId((Integer)this.entityData.get(DATA_TYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 217 */     if (type == DataComponents.MOOSHROOM_VARIANT) {
/* 218 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 221 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 226 */     applyImplicitComponentIfPresent(components, DataComponents.MOOSHROOM_VARIANT);
/* 227 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 232 */     if (type == DataComponents.MOOSHROOM_VARIANT) {
/* 233 */       setVariant((Variant)castComponentValue(DataComponents.MOOSHROOM_VARIANT, value));
/* 234 */       return true;
/*     */     } 
/*     */     
/* 237 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public MushroomCow getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 242 */     MushroomCow baby = (MushroomCow)EntityType.MOOSHROOM.create((Level)level, EntitySpawnReason.BREEDING);
/* 243 */     if (baby != null) {
/* 244 */       baby.setVariant(getOffspringVariant((MushroomCow)partner));
/*     */     }
/* 246 */     return baby;
/*     */   }
/*     */   private Variant getOffspringVariant(MushroomCow mate) {
/*     */     Variant babyVariant;
/* 250 */     Variant variant = getVariant();
/* 251 */     Variant mateVariant = mate.getVariant();
/*     */ 
/*     */     
/* 254 */     if (variant == mateVariant && this.random.nextInt(1024) == 0) {
/* 255 */       babyVariant = (variant == Variant.BROWN) ? Variant.RED : Variant.BROWN;
/*     */     } else {
/* 257 */       babyVariant = this.random.nextBoolean() ? variant : mateVariant;
/*     */     } 
/* 259 */     return babyVariant;
/*     */   }
/*     */   
/*     */   public enum Variant implements StringRepresentable {
/* 263 */     RED("red", 0, Blocks.RED_MUSHROOM.defaultBlockState()),
/* 264 */     BROWN("brown", 1, Blocks.BROWN_MUSHROOM.defaultBlockState());
/*     */     
/* 266 */     public static final Variant DEFAULT = RED;
/*     */     
/* 268 */     public static final Codec<Variant> CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 270 */     private static final java.util.function.IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*     */     
/* 272 */     public static final net.minecraft.network.codec.StreamCodec<ByteBuf, Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */     
/*     */     private final String type;
/*     */     private final int id;
/*     */     private final BlockState blockState;
/*     */     
/*     */     Variant(String type, int id, BlockState blockState) {
/* 279 */       this.type = type;
/* 280 */       this.id = id;
/* 281 */       this.blockState = blockState;
/*     */     }
/*     */     
/*     */     public BlockState getBlockState() {
/* 285 */       return this.blockState;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 290 */       return this.type;
/*     */     }
/*     */     
/*     */     private int id() {
/* 294 */       return this.id;
/*     */     }
/*     */     
/*     */     private static Variant byId(int id) {
/* 298 */       return BY_ID.apply(id);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/cow/MushroomCow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */