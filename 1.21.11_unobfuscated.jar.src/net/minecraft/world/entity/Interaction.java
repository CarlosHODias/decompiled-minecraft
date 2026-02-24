/*     */ package net.minecraft.world.entity;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Interaction extends Entity implements Attackable, Targeting {
/*  27 */   private static final EntityDataAccessor<Float> DATA_WIDTH_ID = SynchedEntityData.defineId(Interaction.class, EntityDataSerializers.FLOAT);
/*  28 */   private static final EntityDataAccessor<Float> DATA_HEIGHT_ID = SynchedEntityData.defineId(Interaction.class, EntityDataSerializers.FLOAT);
/*  29 */   private static final EntityDataAccessor<Boolean> DATA_RESPONSE_ID = SynchedEntityData.defineId(Interaction.class, EntityDataSerializers.BOOLEAN); private static final String TAG_WIDTH = "width"; private static final String TAG_HEIGHT = "height"; private static final String TAG_ATTACK = "attack"; private static final String TAG_INTERACTION = "interaction";
/*     */   private static final String TAG_RESPONSE = "response";
/*     */   private static final float DEFAULT_WIDTH = 1.0F;
/*     */   private static final float DEFAULT_HEIGHT = 1.0F;
/*     */   private static final boolean DEFAULT_RESPONSE = false;
/*     */   private PlayerAction attack;
/*     */   private PlayerAction interaction;
/*     */   
/*     */   private static final class PlayerAction extends Record { private final UUID player;
/*     */     private final long timestamp;
/*     */     public static final Codec<PlayerAction> CODEC;
/*     */     
/*  41 */     private PlayerAction(UUID player, long timestamp) { this.player = player; this.timestamp = timestamp; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Interaction$PlayerAction;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  41 */       //   0	7	0	this	Lnet/minecraft/world/entity/Interaction$PlayerAction; } public UUID player() { return this.player; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Interaction$PlayerAction;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Interaction$PlayerAction; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Interaction$PlayerAction;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Interaction$PlayerAction;
/*  41 */       //   0	8	1	o	Ljava/lang/Object; } public long timestamp() { return this.timestamp; } static {
/*  42 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.CODEC.fieldOf("player").forGetter(PlayerAction::player), (App)Codec.LONG.fieldOf("timestamp").forGetter(PlayerAction::timestamp)).apply((Applicative)i, PlayerAction::new));
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interaction(EntityType<?> type, Level level) {
/*  52 */     super(type, level);
/*  53 */     this.noPhysics = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  58 */     entityData.define(DATA_WIDTH_ID, 1.0F);
/*  59 */     entityData.define(DATA_HEIGHT_ID, 1.0F);
/*  60 */     entityData.define(DATA_RESPONSE_ID, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  65 */     setWidth(input.getFloatOr("width", 1.0F));
/*  66 */     setHeight(input.getFloatOr("height", 1.0F));
/*  67 */     this.attack = input.read("attack", PlayerAction.CODEC).orElse(null);
/*  68 */     this.interaction = input.read("interaction", PlayerAction.CODEC).orElse(null);
/*  69 */     setResponse(input.getBooleanOr("response", false));
/*  70 */     setBoundingBox(makeBoundingBox());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  75 */     output.putFloat("width", getWidth());
/*  76 */     output.putFloat("height", getHeight());
/*  77 */     output.storeNullable("attack", PlayerAction.CODEC, this.attack);
/*  78 */     output.storeNullable("interaction", PlayerAction.CODEC, this.interaction);
/*  79 */     output.putBoolean("response", getResponse());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  84 */     super.onSyncedDataUpdated(accessor);
/*     */     
/*  86 */     if (DATA_HEIGHT_ID.equals(accessor) || DATA_WIDTH_ID.equals(accessor)) {
/*  87 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeHitByProjectile() {
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 103 */     return PushReaction.IGNORE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoringBlockTriggers() {
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean skipAttackInteraction(Entity source) {
/* 113 */     if (source instanceof Player) { Player player = (Player)source;
/* 114 */       this.attack = new PlayerAction(player.getUUID(), level().getGameTime());
/* 115 */       if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 116 */         CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(serverPlayer, this, player.damageSources().generic(), 1.0F, 1.0F, false); }
/*     */       
/* 118 */       return !getResponse(); }
/*     */     
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 130 */     if (level().isClientSide()) {
/* 131 */       return getResponse() ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.CONSUME;
/*     */     }
/* 133 */     this.interaction = new PlayerAction(player.getUUID(), level().getGameTime());
/* 134 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {}
/*     */ 
/*     */   
/*     */   public LivingEntity getLastAttacker() {
/* 143 */     if (this.attack != null) {
/* 144 */       return (LivingEntity)level().getPlayerByUUID(this.attack.player());
/*     */     }
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getTarget() {
/* 151 */     if (this.interaction != null) {
/* 152 */       return (LivingEntity)level().getPlayerByUUID(this.interaction.player());
/*     */     }
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   private void setWidth(float width) {
/* 158 */     this.entityData.set(DATA_WIDTH_ID, width);
/*     */   }
/*     */   
/*     */   private float getWidth() {
/* 162 */     return (Float)this.entityData.get(DATA_WIDTH_ID);
/*     */   }
/*     */   
/*     */   private void setHeight(float width) {
/* 166 */     this.entityData.set(DATA_HEIGHT_ID, width);
/*     */   }
/*     */   
/*     */   private float getHeight() {
/* 170 */     return (Float)this.entityData.get(DATA_HEIGHT_ID);
/*     */   }
/*     */   
/*     */   private void setResponse(boolean response) {
/* 174 */     this.entityData.set(DATA_RESPONSE_ID, response);
/*     */   }
/*     */   
/*     */   private boolean getResponse() {
/* 178 */     return (Boolean)this.entityData.get(DATA_RESPONSE_ID);
/*     */   }
/*     */   
/*     */   private EntityDimensions getDimensions() {
/* 182 */     return EntityDimensions.scalable(getWidth(), getHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose pose) {
/* 187 */     return getDimensions();
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB makeBoundingBox(Vec3 position) {
/* 192 */     return getDimensions().makeBoundingBox(position);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/Interaction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */