/*    */ package net.minecraft.world.level.gameevent;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityPositionSource implements PositionSource {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.core.UUIDUtil.CODEC.fieldOf("source_entity").forGetter(EntityPositionSource::getUuid), (App)Codec.FLOAT.fieldOf("y_offset").orElse(0.0F).forGetter(())).apply((Applicative)i, ()));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, EntityPositionSource::getId, ByteBufCodecs.FLOAT, o -> o.yOffset, (id, offset) -> new EntityPositionSource(Either.right(Either.right(id)), offset));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<EntityPositionSource> CODEC;
/*    */   public static final StreamCodec<ByteBuf, EntityPositionSource> STREAM_CODEC;
/*    */   private Either<Entity, Either<UUID, Integer>> entityOrUuidOrId;
/*    */   private final float yOffset;
/*    */   
/*    */   public EntityPositionSource(Entity entity, float yOffset) {
/* 36 */     this(Either.left(entity), yOffset);
/*    */   }
/*    */   
/*    */   private EntityPositionSource(Either<Entity, Either<UUID, Integer>> entityOrUuidOrId, float yOffset) {
/* 40 */     this.entityOrUuidOrId = entityOrUuidOrId;
/* 41 */     this.yOffset = yOffset;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Vec3> getPosition(Level level) {
/* 46 */     if (this.entityOrUuidOrId.left().isEmpty()) {
/* 47 */       resolveEntity(level);
/*    */     }
/* 49 */     return this.entityOrUuidOrId.left().map(entity -> entity.position().add(0.0D, this.yOffset, 0.0D));
/*    */   }
/*    */   
/*    */   private void resolveEntity(Level level) {
/* 53 */     ((Optional)this.entityOrUuidOrId.map(Optional::of, uuidOrId -> {
/*    */           java.util.Objects.requireNonNull(level);
/*    */ 
/*    */ 
/*    */           
/*    */           return Optional.ofNullable((Entity)uuidOrId.map((), level::getEntity));
/* 59 */         })).ifPresent(entity -> this.entityOrUuidOrId = Either.left(entity));
/*    */   }
/*    */   
/*    */   public UUID getUuid() {
/* 63 */     return (UUID)this.entityOrUuidOrId.map(Entity::getUUID, uuidOrId -> (UUID)uuidOrId.map(Function.identity(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int getId() {
/* 75 */     return (Integer)this.entityOrUuidOrId.map(Entity::getId, uuidOrId -> (Integer)uuidOrId.map((), Function.identity()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PositionSourceType<EntityPositionSource> getType() {
/* 88 */     return PositionSourceType.ENTITY;
/*    */   }
/*    */   
/*    */   public static class Type
/*    */     implements PositionSourceType<EntityPositionSource> {
/*    */     public MapCodec<EntityPositionSource> codec() {
/* 94 */       return EntityPositionSource.CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public StreamCodec<ByteBuf, EntityPositionSource> streamCodec() {
/* 99 */       return EntityPositionSource.STREAM_CODEC;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gameevent/EntityPositionSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */