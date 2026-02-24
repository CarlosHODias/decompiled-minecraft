/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ 
/*     */ public enum ConversionType
/*     */ {
/*  17 */   SINGLE(true)
/*     */   {
/*     */     void convert(Mob from, Mob to, ConversionParams params) {
/*  20 */       Entity rootPassenger = from.getFirstPassenger();
/*     */       
/*  22 */       to.copyPosition(from);
/*  23 */       to.setDeltaMovement(from.getDeltaMovement());
/*     */       
/*  25 */       if (rootPassenger != null) {
/*  26 */         rootPassenger.stopRiding();
/*  27 */         rootPassenger.boardingCooldown = 0;
/*     */ 
/*     */ 
/*     */         
/*  31 */         for (Entity passenger : to.getPassengers()) {
/*  32 */           passenger.stopRiding();
/*  33 */           passenger.remove(Entity.RemovalReason.DISCARDED);
/*     */         } 
/*     */         
/*  36 */         rootPassenger.startRiding(to);
/*     */       } 
/*     */       
/*  39 */       Entity vehicle = from.getVehicle();
/*     */       
/*  41 */       if (vehicle != null) {
/*  42 */         from.stopRiding();
/*  43 */         to.startRiding(vehicle, false, false);
/*     */       } 
/*     */       
/*  46 */       if (params.keepEquipment()) {
/*  47 */         for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  48 */           ItemStack itemStack = from.getItemBySlot(slot);
/*  49 */           if (!itemStack.isEmpty()) {
/*  50 */             to.setItemSlot(slot, itemStack.copyAndClear());
/*  51 */             to.setDropChance(slot, from.getDropChances().byEquipment(slot));
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*  56 */       to.fallDistance = from.fallDistance;
/*  57 */       to.setSharedFlag(7, from.isFallFlying());
/*     */       
/*  59 */       to.lastHurtByPlayerMemoryTime = from.lastHurtByPlayerMemoryTime;
/*  60 */       to.hurtTime = from.hurtTime;
/*     */       
/*  62 */       to.yBodyRot = from.yBodyRot;
/*     */       
/*  64 */       to.setOnGround(from.onGround());
/*  65 */       Objects.requireNonNull(to); from.getSleepingPos().ifPresent(to::setSleepingPos);
/*     */       
/*  67 */       Entity leashHolder = from.getLeashHolder();
/*  68 */       if (leashHolder != null) {
/*  69 */         to.setLeashedTo(leashHolder, true);
/*     */       }
/*     */       
/*  72 */       convertCommon(from, to, params);
/*     */     }
/*     */   },
/*     */   
/*  76 */   SPLIT_ON_DEATH(false)
/*     */   {
/*     */     void convert(Mob from, Mob to, ConversionParams params) {
/*  79 */       Entity rootPassenger = from.getFirstPassenger();
/*     */       
/*  81 */       if (rootPassenger != null) {
/*  82 */         rootPassenger.stopRiding();
/*     */       }
/*     */ 
/*     */       
/*  86 */       Entity leashHolder = from.getLeashHolder();
/*  87 */       if (leashHolder != null) {
/*  88 */         from.dropLeash();
/*     */       }
/*     */       
/*  91 */       convertCommon(from, to, params);
/*     */     }
/*     */   };
/*     */   
/*  95 */   private static final Set<DataComponentType<?>> COMPONENTS_TO_COPY = Set.of(DataComponents.CUSTOM_NAME, DataComponents.CUSTOM_DATA);
/*     */ 
/*     */   
/*     */   private final boolean discardAfterConversion;
/*     */ 
/*     */ 
/*     */   
/*     */   ConversionType(boolean discardAfterConversion) {
/* 103 */     this.discardAfterConversion = discardAfterConversion;
/*     */   }
/*     */   
/*     */   public boolean shouldDiscardAfterConversion() {
/* 107 */     return this.discardAfterConversion;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void convertCommon(Mob from, Mob to, ConversionParams params) {
/* 113 */     to.setAbsorptionAmount(from.getAbsorptionAmount());
/*     */     
/* 115 */     for (MobEffectInstance effect : from.getActiveEffects()) {
/* 116 */       to.addEffect(new MobEffectInstance(effect));
/*     */     }
/*     */     
/* 119 */     if (from.isBaby()) {
/* 120 */       to.setBaby(true);
/*     */     }
/*     */     
/* 123 */     if (from instanceof AgeableMob) { AgeableMob oldAgeable = (AgeableMob)from; if (to instanceof AgeableMob) { AgeableMob convertedAgeable = (AgeableMob)to;
/* 124 */         convertedAgeable.setAge(oldAgeable.getAge());
/* 125 */         convertedAgeable.forcedAge = oldAgeable.forcedAge;
/* 126 */         convertedAgeable.forcedAgeTimer = oldAgeable.forcedAgeTimer; }
/*     */        }
/*     */     
/* 129 */     Brain<?> oldBrain = from.getBrain();
/* 130 */     Brain<?> convertedBrain = to.getBrain();
/*     */     
/* 132 */     if (oldBrain.checkMemory(MemoryModuleType.ANGRY_AT, MemoryStatus.REGISTERED) && oldBrain.hasMemoryValue(MemoryModuleType.ANGRY_AT)) {
/* 133 */       convertedBrain.setMemory(MemoryModuleType.ANGRY_AT, oldBrain.getMemory(MemoryModuleType.ANGRY_AT));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (params.preserveCanPickUpLoot()) {
/* 141 */       to.setCanPickUpLoot(from.canPickUpLoot());
/*     */     }
/*     */     
/* 144 */     to.setLeftHanded(from.isLeftHanded());
/* 145 */     to.setNoAi(from.isNoAi());
/*     */     
/* 147 */     if (from.isPersistenceRequired()) {
/* 148 */       to.setPersistenceRequired();
/*     */     }
/*     */     
/* 151 */     to.setCustomNameVisible(from.isCustomNameVisible());
/*     */     
/* 153 */     to.setSharedFlagOnFire(from.isOnFire());
/*     */     
/* 155 */     to.setInvulnerable(from.isInvulnerable());
/*     */     
/* 157 */     to.setNoGravity(from.isNoGravity());
/*     */     
/* 159 */     to.setPortalCooldown(from.getPortalCooldown());
/*     */     
/* 161 */     to.setSilent(from.isSilent());
/*     */     
/* 163 */     Objects.requireNonNull(to); from.getTags().forEach(to::addTag);
/*     */     
/* 165 */     for (DataComponentType<?> component : COMPONENTS_TO_COPY) {
/* 166 */       copyComponent(from, to, component);
/*     */     }
/*     */     
/* 169 */     if (params.team() != null) {
/* 170 */       Scoreboard scoreboard = to.level().getScoreboard();
/* 171 */       scoreboard.addPlayerToTeam(to.getStringUUID(), params.team());
/*     */ 
/*     */ 
/*     */       
/* 175 */       if (from.getTeam() != null && from.getTeam() == params.team()) {
/* 176 */         scoreboard.removePlayerFromTeam(from.getStringUUID(), from.getTeam());
/*     */       }
/*     */     } 
/*     */     
/* 180 */     if (from instanceof Zombie) { Zombie fromZombie = (Zombie)from; if (fromZombie.canBreakDoors() && to instanceof Zombie) { Zombie toZombie = (Zombie)to;
/* 181 */         toZombie.setCanBreakDoors(true); }
/*     */        }
/*     */   
/*     */   }
/*     */   private static <T> void copyComponent(Mob from, Mob to, DataComponentType<T> componentType) {
/* 186 */     T value = from.get(componentType);
/* 187 */     if (value != null)
/* 188 */       to.setComponent(componentType, value); 
/*     */   }
/*     */   
/*     */   abstract void convert(Mob paramMob1, Mob paramMob2, ConversionParams paramConversionParams);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ConversionType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */