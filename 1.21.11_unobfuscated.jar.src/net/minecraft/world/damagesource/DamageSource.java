/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class DamageSource
/*     */ {
/*     */   private final Holder<DamageType> type;
/*     */   private final Entity causingEntity;
/*     */   private final Entity directEntity;
/*     */   private final Vec3 damageSourcePosition;
/*     */   
/*     */   public String toString() {
/*  23 */     return "DamageSource (" + type().msgId() + ")";
/*     */   }
/*     */   
/*     */   public float getFoodExhaustion() {
/*  27 */     return type().exhaustion();
/*     */   }
/*     */   
/*     */   public boolean isDirect() {
/*  31 */     return (this.causingEntity == this.directEntity);
/*     */   }
/*     */   
/*     */   private DamageSource(Holder<DamageType> type, Entity directEntity, Entity causingEntity, Vec3 damageSourcePosition) {
/*  35 */     this.type = type;
/*  36 */     this.causingEntity = causingEntity;
/*  37 */     this.directEntity = directEntity;
/*  38 */     this.damageSourcePosition = damageSourcePosition;
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type, Entity directEntity, Entity causingEntity) {
/*  42 */     this(type, directEntity, causingEntity, null);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type, Vec3 damageSourcePosition) {
/*  46 */     this(type, null, null, damageSourcePosition);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type, Entity causingEntity) {
/*  50 */     this(type, causingEntity, causingEntity);
/*     */   }
/*     */   
/*     */   public DamageSource(Holder<DamageType> type) {
/*  54 */     this(type, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getDirectEntity() {
/*  62 */     return this.directEntity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity() {
/*  70 */     return this.causingEntity;
/*     */   }
/*     */   
/*     */   public ItemStack getWeaponItem() {
/*  74 */     return (this.directEntity != null) ? this.directEntity.getWeaponItem() : null;
/*     */   }
/*     */   
/*     */   public Component getLocalizedDeathMessage(LivingEntity victim) {
/*  78 */     String deathMsg = "death.attack." + type().msgId();
/*  79 */     if (this.causingEntity != null || this.directEntity != null) {
/*  80 */       Component name = (this.causingEntity == null) ? this.directEntity.getDisplayName() : this.causingEntity.getDisplayName();
/*  81 */       Entity entity = this.causingEntity; LivingEntity livingEntity = (LivingEntity)entity; ItemStack held = (entity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY;
/*     */       
/*  83 */       if (!held.isEmpty() && held.has(DataComponents.CUSTOM_NAME)) {
/*  84 */         return (Component)Component.translatable(deathMsg + ".item", new Object[] { victim.getDisplayName(), name, held.getDisplayName() });
/*     */       }
/*  86 */       return (Component)Component.translatable(deathMsg, new Object[] { victim.getDisplayName(), name });
/*     */     } 
/*     */ 
/*     */     
/*  90 */     LivingEntity source = victim.getKillCredit();
/*  91 */     String playerMsg = deathMsg + ".player";
/*  92 */     if (source != null) {
/*  93 */       return (Component)Component.translatable(playerMsg, new Object[] { victim.getDisplayName(), source.getDisplayName() });
/*     */     }
/*  95 */     return (Component)Component.translatable(deathMsg, new Object[] { victim.getDisplayName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMsgId() {
/* 100 */     return type().msgId();
/*     */   }
/*     */   
/*     */   public boolean scalesWithDifficulty() {
/* 104 */     switch (type().scaling()) { default: throw new MatchException(null, null);
/*     */       case NEVER: 
/* 106 */       case WHEN_CAUSED_BY_LIVING_NON_PLAYER: if (this.causingEntity instanceof LivingEntity && !(this.causingEntity instanceof Player));
/*     */       case ALWAYS:
/*     */         break; }
/*     */     
/*     */     return true;
/*     */   } public boolean isCreativePlayer() {
/* 112 */     Entity entity = getEntity(); if (entity instanceof Player) { Player player = (Player)entity; if ((player.getAbilities()).instabuild); }  return false;
/*     */   }
/*     */   
/*     */   public Vec3 getSourcePosition() {
/* 116 */     if (this.damageSourcePosition != null)
/* 117 */       return this.damageSourcePosition; 
/* 118 */     if (this.directEntity != null) {
/* 119 */       return this.directEntity.position();
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   public Vec3 sourcePositionRaw() {
/* 125 */     return this.damageSourcePosition;
/*     */   }
/*     */   
/*     */   public boolean is(TagKey<DamageType> tag) {
/* 129 */     return this.type.is(tag);
/*     */   }
/*     */   
/*     */   public boolean is(ResourceKey<DamageType> typeKey) {
/* 133 */     return this.type.is(typeKey);
/*     */   }
/*     */   
/*     */   public DamageType type() {
/* 137 */     return (DamageType)this.type.value();
/*     */   }
/*     */   
/*     */   public Holder<DamageType> typeHolder() {
/* 141 */     return this.type;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/damagesource/DamageSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */