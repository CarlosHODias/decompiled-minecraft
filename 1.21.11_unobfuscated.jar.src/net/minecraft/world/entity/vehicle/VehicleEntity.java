/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class VehicleEntity
/*     */   extends Entity
/*     */ {
/*  23 */   protected static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.INT);
/*  24 */   protected static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.INT);
/*  25 */   protected static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.FLOAT);
/*     */   
/*     */   public VehicleEntity(EntityType<?> type, Level level) {
/*  28 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtClient(DamageSource source) {
/*  33 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual isRemoved : ()Z
/*     */     //   4: ifeq -> 9
/*     */     //   7: iconst_1
/*     */     //   8: ireturn
/*     */     //   9: aload_0
/*     */     //   10: aload_2
/*     */     //   11: invokevirtual isInvulnerableToBase : (Lnet/minecraft/world/damagesource/DamageSource;)Z
/*     */     //   14: ifeq -> 19
/*     */     //   17: iconst_0
/*     */     //   18: ireturn
/*     */     //   19: aload_0
/*     */     //   20: aload_0
/*     */     //   21: invokevirtual getHurtDir : ()I
/*     */     //   24: ineg
/*     */     //   25: invokevirtual setHurtDir : (I)V
/*     */     //   28: aload_0
/*     */     //   29: bipush #10
/*     */     //   31: invokevirtual setHurtTime : (I)V
/*     */     //   34: aload_0
/*     */     //   35: invokevirtual markHurt : ()V
/*     */     //   38: aload_0
/*     */     //   39: aload_0
/*     */     //   40: invokevirtual getDamage : ()F
/*     */     //   43: fload_3
/*     */     //   44: ldc 10.0
/*     */     //   46: fmul
/*     */     //   47: fadd
/*     */     //   48: invokevirtual setDamage : (F)V
/*     */     //   51: aload_0
/*     */     //   52: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_DAMAGE : Lnet/minecraft/core/Holder$Reference;
/*     */     //   55: aload_2
/*     */     //   56: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   59: invokevirtual gameEvent : (Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;)V
/*     */     //   62: aload_2
/*     */     //   63: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   66: astore #6
/*     */     //   68: aload #6
/*     */     //   70: instanceof net/minecraft/world/entity/player/Player
/*     */     //   73: ifeq -> 98
/*     */     //   76: aload #6
/*     */     //   78: checkcast net/minecraft/world/entity/player/Player
/*     */     //   81: astore #5
/*     */     //   83: aload #5
/*     */     //   85: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*     */     //   88: getfield instabuild : Z
/*     */     //   91: ifeq -> 98
/*     */     //   94: iconst_1
/*     */     //   95: goto -> 99
/*     */     //   98: iconst_0
/*     */     //   99: istore #4
/*     */     //   101: iload #4
/*     */     //   103: ifne -> 116
/*     */     //   106: aload_0
/*     */     //   107: invokevirtual getDamage : ()F
/*     */     //   110: ldc 40.0
/*     */     //   112: fcmpl
/*     */     //   113: ifgt -> 124
/*     */     //   116: aload_0
/*     */     //   117: aload_2
/*     */     //   118: invokevirtual shouldSourceDestroy : (Lnet/minecraft/world/damagesource/DamageSource;)Z
/*     */     //   121: ifeq -> 133
/*     */     //   124: aload_0
/*     */     //   125: aload_1
/*     */     //   126: aload_2
/*     */     //   127: invokevirtual destroy : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V
/*     */     //   130: goto -> 142
/*     */     //   133: iload #4
/*     */     //   135: ifeq -> 142
/*     */     //   138: aload_0
/*     */     //   139: invokevirtual discard : ()V
/*     */     //   142: iconst_1
/*     */     //   143: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     //   #39	-> 7
/*     */     //   #41	-> 9
/*     */     //   #42	-> 17
/*     */     //   #44	-> 19
/*     */     //   #45	-> 28
/*     */     //   #46	-> 34
/*     */     //   #47	-> 38
/*     */     //   #48	-> 51
/*     */     //   #49	-> 62
/*     */     //   #51	-> 101
/*     */     //   #52	-> 124
/*     */     //   #53	-> 133
/*     */     //   #54	-> 138
/*     */     //   #56	-> 142
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   83	15	5	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	144	0	this	Lnet/minecraft/world/entity/vehicle/VehicleEntity;
/*     */     //   0	144	1	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	144	2	source	Lnet/minecraft/world/damagesource/DamageSource;
/*     */     //   0	144	3	damage	F
/*     */     //   101	43	4	creativePlayer	Z
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldSourceDestroy(DamageSource source) {
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion(Explosion explosion) {
/*  65 */     return (explosion.getIndirectSourceEntity() instanceof net.minecraft.world.entity.Mob && !((Boolean)
/*  66 */       explosion.level().getGameRules().get(GameRules.MOB_GRIEFING)));
/*     */   }
/*     */   
/*     */   public void destroy(ServerLevel level, Item dropItem) {
/*  70 */     kill(level);
/*     */     
/*  72 */     if (!((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS))) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     ItemStack itemStack = new ItemStack((ItemLike)dropItem);
/*  77 */     itemStack.set(DataComponents.CUSTOM_NAME, getCustomName());
/*  78 */     spawnAtLocation(level, itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  83 */     entityData.define(DATA_ID_HURT, 0);
/*  84 */     entityData.define(DATA_ID_HURTDIR, 1);
/*  85 */     entityData.define(DATA_ID_DAMAGE, 0.0F);
/*     */   }
/*     */   
/*     */   public void setHurtTime(int hurtTime) {
/*  89 */     this.entityData.set(DATA_ID_HURT, hurtTime);
/*     */   }
/*     */   
/*     */   public void setHurtDir(int hurtDir) {
/*  93 */     this.entityData.set(DATA_ID_HURTDIR, hurtDir);
/*     */   }
/*     */   
/*     */   public void setDamage(float damage) {
/*  97 */     this.entityData.set(DATA_ID_DAMAGE, damage);
/*     */   }
/*     */   
/*     */   public float getDamage() {
/* 101 */     return (Float)this.entityData.get(DATA_ID_DAMAGE);
/*     */   }
/*     */   
/*     */   public int getHurtTime() {
/* 105 */     return (Integer)this.entityData.get(DATA_ID_HURT);
/*     */   }
/*     */   
/*     */   public int getHurtDir() {
/* 109 */     return (Integer)this.entityData.get(DATA_ID_HURTDIR);
/*     */   }
/*     */   
/*     */   protected void destroy(ServerLevel level, DamageSource source) {
/* 113 */     destroy(level, getDropItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDimensionChangingDelay() {
/* 118 */     return 10;
/*     */   }
/*     */   
/*     */   protected abstract Item getDropItem();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/vehicle/VehicleEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */