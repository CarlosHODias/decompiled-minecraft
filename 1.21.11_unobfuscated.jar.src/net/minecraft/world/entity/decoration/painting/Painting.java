/*     */ package net.minecraft.world.entity.decoration.painting;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.PaintingVariantTags;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.HangingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Painting extends HangingEntity {
/*  41 */   private static final EntityDataAccessor<Holder<PaintingVariant>> DATA_PAINTING_VARIANT_ID = SynchedEntityData.defineId(Painting.class, EntityDataSerializers.PAINTING_VARIANT);
/*     */   
/*     */   public static final float DEPTH = 0.0625F;
/*     */   
/*     */   public Painting(EntityType<? extends Painting> type, Level level) {
/*  46 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  51 */     super.defineSynchedData(entityData);
/*  52 */     entityData.define(DATA_PAINTING_VARIANT_ID, VariantUtils.getAny(registryAccess(), Registries.PAINTING_VARIANT));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  57 */     super.onSyncedDataUpdated(accessor);
/*  58 */     if (DATA_PAINTING_VARIANT_ID.equals(accessor)) {
/*  59 */       recalculateBoundingBox();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setVariant(Holder<PaintingVariant> variant) {
/*  64 */     this.entityData.set(DATA_PAINTING_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   public Holder<PaintingVariant> getVariant() {
/*  68 */     return (Holder<PaintingVariant>)this.entityData.get(DATA_PAINTING_VARIANT_ID);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/*  73 */     if (type == DataComponents.PAINTING_VARIANT) {
/*  74 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/*  77 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/*  82 */     applyImplicitComponentIfPresent(components, DataComponents.PAINTING_VARIANT);
/*  83 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/*  88 */     if (type == DataComponents.PAINTING_VARIANT) {
/*  89 */       setVariant((Holder<PaintingVariant>)castComponentValue(DataComponents.PAINTING_VARIANT, value));
/*  90 */       return true;
/*     */     } 
/*     */     
/*  93 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public static Optional<Painting> create(Level level, BlockPos pos, Direction direction) {
/*  97 */     Painting candidate = new Painting(level, pos);
/*     */     
/*  99 */     List<Holder<PaintingVariant>> potentialVariants = new ArrayList<>();
/* 100 */     Objects.requireNonNull(potentialVariants); level.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE).forEach(potentialVariants::add);
/* 101 */     if (potentialVariants.isEmpty()) {
/* 102 */       return Optional.empty();
/*     */     }
/*     */     
/* 105 */     candidate.setDirection(direction);
/* 106 */     potentialVariants.removeIf(variant -> {
/*     */           candidate.setVariant(variant);
/*     */           
/*     */           return !candidate.survives();
/*     */         });
/* 111 */     if (potentialVariants.isEmpty()) {
/* 112 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 116 */     int largestPaintingAreaSize = potentialVariants.stream()
/* 117 */       .mapToInt(Painting::variantArea)
/* 118 */       .max().orElse(0);
/*     */     
/* 120 */     potentialVariants.removeIf(variant -> (variantArea(variant) < largestPaintingAreaSize));
/* 121 */     Optional<Holder<PaintingVariant>> selectedVariant = Util.getRandomSafe(potentialVariants, candidate.random);
/* 122 */     if (selectedVariant.isEmpty()) {
/* 123 */       return Optional.empty();
/*     */     }
/* 125 */     candidate.setVariant(selectedVariant.get());
/* 126 */     candidate.setDirection(direction);
/* 127 */     return Optional.of(candidate);
/*     */   }
/*     */   
/*     */   private static int variantArea(Holder<PaintingVariant> variant) {
/* 131 */     return ((PaintingVariant)variant.value()).area();
/*     */   }
/*     */   
/*     */   private Painting(Level level, BlockPos blockPos) {
/* 135 */     super(EntityType.PAINTING, level, blockPos);
/*     */   }
/*     */   
/*     */   public Painting(Level level, BlockPos blockPos, Direction direction, Holder<PaintingVariant> variant) {
/* 139 */     this(level, blockPos);
/* 140 */     setVariant(variant);
/* 141 */     setDirection(direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 146 */     output.store("facing", Direction.LEGACY_ID_CODEC_2D, getDirection());
/* 147 */     super.addAdditionalSaveData(output);
/*     */     
/* 149 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 154 */     Direction direction = input.read("facing", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
/* 155 */     super.readAdditionalSaveData(input);
/* 156 */     setDirection(direction);
/*     */     
/* 158 */     VariantUtils.readVariant(input, Registries.PAINTING_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB calculateBoundingBox(BlockPos pos, Direction direction) {
/* 163 */     float shiftToBlockWall = 0.46875F;
/* 164 */     Vec3 attachedToWall = Vec3.atCenterOf((Vec3i)pos).relative(direction, -0.46875D);
/*     */     
/* 166 */     PaintingVariant variant = (PaintingVariant)getVariant().value();
/* 167 */     double horizontalOffset = offsetForPaintingSize(variant.width());
/* 168 */     double verticalOffset = offsetForPaintingSize(variant.height());
/*     */     
/* 170 */     Direction left = direction.getCounterClockWise();
/* 171 */     Vec3 position = attachedToWall.relative(left, horizontalOffset).relative(Direction.UP, verticalOffset);
/*     */     
/* 173 */     Direction.Axis axis = direction.getAxis();
/* 174 */     double xSize = (axis == Direction.Axis.X) ? 0.0625D : variant.width();
/* 175 */     double ySize = variant.height();
/* 176 */     double zSize = (axis == Direction.Axis.Z) ? 0.0625D : variant.width();
/*     */     
/* 178 */     return AABB.ofSize(position, xSize, ySize, zSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double offsetForPaintingSize(int size) {
/* 185 */     return (size % 2 == 0) ? 0.5D : 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(ServerLevel level, Entity causedBy) {
/* 190 */     if (!((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS))) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
/*     */     
/* 196 */     if (causedBy instanceof Player) { Player player = (Player)causedBy;
/* 197 */       if (player.hasInfiniteMaterials()) {
/*     */         return;
/*     */       } }
/*     */ 
/*     */     
/* 202 */     spawnAtLocation(level, (ItemLike)Items.PAINTING);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playPlacementSound() {
/* 207 */     playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void snapTo(double x, double y, double z, float yRot, float xRot) {
/* 212 */     setPos(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 trackingPosition() {
/* 217 */     return Vec3.atLowerCornerOf((Vec3i)this.pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 222 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket((Entity)this, getDirection().get3DDataValue(), getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 227 */     super.recreateFromPacket(packet);
/* 228 */     setDirection(Direction.from3DDataValue(packet.getData()));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 233 */     return new ItemStack((ItemLike)Items.PAINTING);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/decoration/painting/Painting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */