/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BlockAttachedEntity
/*     */   extends Entity {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private int checkInterval;
/*     */   protected BlockPos pos;
/*     */   
/*     */   protected BlockAttachedEntity(EntityType<? extends BlockAttachedEntity> type, Level level) {
/*  29 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected BlockAttachedEntity(EntityType<? extends BlockAttachedEntity> type, Level level, BlockPos pos) {
/*  33 */     this(type, level);
/*  34 */     this.pos = pos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void recalculateBoundingBox();
/*     */   
/*     */   public void tick() {
/*  41 */     Level level1 = level(); ServerLevel level = (ServerLevel)level1;
/*  42 */     checkBelowWorld();
/*  43 */     if (level1 instanceof ServerLevel && this.checkInterval++ == 100) {
/*  44 */       this.checkInterval = 0;
/*  45 */       if (!isRemoved() && !survives()) {
/*  46 */         discard();
/*  47 */         dropItem(level, null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean survives();
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/*  57 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean skipAttackInteraction(Entity source) {
/*  62 */     if (source instanceof Player) { Player player = (Player)source;
/*  63 */       if (!level().mayInteract((Entity)player, this.pos)) {
/*  64 */         return true;
/*     */       }
/*  66 */       return hurtOrSimulate(damageSources().playerAttack(player), 0.0F); }
/*     */     
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtClient(DamageSource source) {
/*  73 */     return !isInvulnerableToBase(source);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  78 */     if (isInvulnerableToBase(source)) {
/*  79 */       return false;
/*     */     }
/*  81 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)) && source.getEntity() instanceof net.minecraft.world.entity.Mob) {
/*  82 */       return false;
/*     */     }
/*  84 */     if (!isRemoved()) {
/*  85 */       kill(level);
/*  86 */       markHurt();
/*  87 */       dropItem(level, source.getEntity());
/*     */     } 
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion(Explosion explosion) {
/*  94 */     Entity directEntity = explosion.getDirectSourceEntity();
/*  95 */     if (directEntity != null && directEntity.isInWater()) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (explosion.shouldAffectBlocklikeEntities()) {
/*  99 */       return super.ignoreExplosion(explosion);
/*     */     }
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 106 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!isRemoved() && delta.lengthSqr() > 0.0D) {
/* 107 */         kill(serverLevel);
/* 108 */         dropItem(serverLevel, null);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   public void push(double xa, double ya, double za) {
/* 114 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!isRemoved() && xa * xa + ya * ya + za * za > 0.0D) {
/* 115 */         kill(serverLevel);
/* 116 */         dropItem(serverLevel, null);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 122 */     output.store("block_pos", BlockPos.CODEC, getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 127 */     BlockPos storedPos = input.read("block_pos", BlockPos.CODEC).orElse(null);
/* 128 */     if (storedPos == null || !storedPos.closerThan((Vec3i)blockPosition(), 16.0D)) {
/* 129 */       LOGGER.error("Block-attached entity at invalid position: {}", storedPos);
/*     */       return;
/*     */     } 
/* 132 */     this.pos = storedPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void dropItem(ServerLevel paramServerLevel, Entity paramEntity);
/*     */   
/*     */   protected boolean repositionEntityAfterLoad() {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 144 */     this.pos = BlockPos.containing(x, y, z);
/* 145 */     recalculateBoundingBox();
/* 146 */     this.needsSync = true;
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 150 */     return this.pos;
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {}
/*     */   
/*     */   public void refreshDimensions() {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/decoration/BlockAttachedEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */