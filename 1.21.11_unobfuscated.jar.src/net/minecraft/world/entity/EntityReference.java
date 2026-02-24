/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UUIDLookup;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public final class EntityReference<StoredEntityType extends UniquelyIdentifyable> {
/*  21 */   private static final Codec<? extends EntityReference<?>> CODEC = UUIDUtil.CODEC.xmap(EntityReference::new, EntityReference::getUUID);
/*  22 */   private static final StreamCodec<ByteBuf, ? extends EntityReference<?>> STREAM_CODEC = UUIDUtil.STREAM_CODEC.map(EntityReference::new, EntityReference::getUUID);
/*     */   private Either<UUID, StoredEntityType> entity;
/*     */   
/*     */   public static <Type extends UniquelyIdentifyable> Codec<EntityReference<Type>> codec() {
/*  26 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <Type extends UniquelyIdentifyable> StreamCodec<ByteBuf, EntityReference<Type>> streamCodec() {
/*  31 */     return (StreamCodec)STREAM_CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EntityReference(StoredEntityType entity) {
/*  37 */     this.entity = Either.right(entity);
/*     */   }
/*     */   
/*     */   private EntityReference(UUID uuid) {
/*  41 */     this.entity = Either.left(uuid);
/*     */   }
/*     */   
/*     */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(T entity) {
/*  45 */     return (entity != null) ? new EntityReference<>(entity) : null;
/*     */   }
/*     */   
/*     */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(UUID uuid) {
/*  49 */     return new EntityReference<>(uuid);
/*     */   }
/*     */   
/*     */   public UUID getUUID() {
/*  53 */     return (UUID)this.entity.map(uuid -> uuid, UniquelyIdentifyable::getUUID);
/*     */   }
/*     */   
/*     */   public StoredEntityType getEntity(UUIDLookup<? extends UniquelyIdentifyable> lookup, Class<StoredEntityType> clazz) {
/*  57 */     Optional<StoredEntityType> stored = this.entity.right();
/*  58 */     if (stored.isPresent()) {
/*  59 */       UniquelyIdentifyable uniquelyIdentifyable = (UniquelyIdentifyable)stored.get();
/*  60 */       if (uniquelyIdentifyable.isRemoved()) {
/*     */         
/*  62 */         this.entity = Either.left(uniquelyIdentifyable.getUUID());
/*     */       } else {
/*  64 */         return (StoredEntityType)uniquelyIdentifyable;
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     Optional<UUID> uuid = this.entity.left();
/*  69 */     if (uuid.isPresent()) {
/*  70 */       StoredEntityType resolved = resolve(lookup.lookup(uuid.get()), clazz);
/*  71 */       if (resolved != null && !resolved.isRemoved()) {
/*  72 */         this.entity = Either.right(resolved);
/*  73 */         return resolved;
/*     */       } 
/*     */     } 
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public StoredEntityType getEntity(Level level, Class<StoredEntityType> clazz) {
/*  80 */     if (Player.class.isAssignableFrom(clazz)) {
/*  81 */       Objects.requireNonNull(level); return getEntity(level::getPlayerInAnyDimension, clazz);
/*     */     } 
/*  83 */     Objects.requireNonNull(level); return getEntity(level::getEntityInAnyDimension, clazz);
/*     */   }
/*     */   
/*     */   private StoredEntityType resolve(UniquelyIdentifyable entity, Class<StoredEntityType> clazz) {
/*  87 */     if (entity != null && clazz.isAssignableFrom(entity.getClass())) {
/*  88 */       return clazz.cast(entity);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   public boolean matches(StoredEntityType entity) {
/*  94 */     return getUUID().equals(entity.getUUID());
/*     */   }
/*     */   
/*     */   public void store(ValueOutput output, String key) {
/*  98 */     output.store(key, UUIDUtil.CODEC, getUUID());
/*     */   }
/*     */   
/*     */   public static void store(EntityReference<?> reference, ValueOutput output, String key) {
/* 102 */     if (reference != null) {
/* 103 */       reference.store(output, key);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> StoredEntityType get(EntityReference<StoredEntityType> reference, Level level, Class<StoredEntityType> clazz) {
/* 108 */     return (reference != null) ? reference.getEntity(level, clazz) : null;
/*     */   }
/*     */   
/*     */   public static Entity getEntity(EntityReference<Entity> reference, Level level) {
/* 112 */     return get(reference, level, Entity.class);
/*     */   }
/*     */   
/*     */   public static LivingEntity getLivingEntity(EntityReference<LivingEntity> reference, Level level) {
/* 116 */     return get(reference, level, LivingEntity.class);
/*     */   }
/*     */   
/*     */   public static Player getPlayer(EntityReference<Player> reference, Level level) {
/* 120 */     return get(reference, level, Player.class);
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> read(ValueInput input, String key) {
/* 124 */     return input.read(key, codec()).orElse(null);
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> readWithOldOwnerConversion(ValueInput input, String key, Level level) {
/* 128 */     Optional<UUID> uuid = input.read(key, UUIDUtil.CODEC);
/* 129 */     if (uuid.isPresent()) {
/* 130 */       return of(uuid.get());
/*     */     }
/* 132 */     return input.getString(key)
/* 133 */       .map(oldName -> OldUsersConverter.convertMobOwnerIfNecessary(level.getServer(), oldName))
/* 134 */       .map(EntityReference::new)
/* 135 */       .orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 141 */     if (obj == this) {
/* 142 */       return true;
/*     */     }
/* 144 */     if (obj instanceof EntityReference) { EntityReference<?> reference = (EntityReference)obj; if (getUUID().equals(reference.getUUID())); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     return getUUID().hashCode();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EntityReference.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */