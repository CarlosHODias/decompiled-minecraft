/*     */ package net.minecraft.world.food;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class FoodData
/*     */ {
/*     */   private static final int DEFAULT_TICK_TIMER = 0;
/*     */   private static final float DEFAULT_EXHAUSTION_LEVEL = 0.0F;
/*  15 */   private int foodLevel = 20;
/*  16 */   private float saturationLevel = 5.0F;
/*     */   
/*     */   private float exhaustionLevel;
/*     */   private int tickTimer;
/*     */   
/*     */   private void add(int food, float saturation) {
/*  22 */     this.foodLevel = Mth.clamp(food + this.foodLevel, 0, 20);
/*  23 */     this.saturationLevel = Mth.clamp(saturation + this.saturationLevel, 0.0F, this.foodLevel);
/*     */   }
/*     */   
/*     */   public void eat(int food, float saturationModifier) {
/*  27 */     add(food, FoodConstants.saturationByModifier(food, saturationModifier));
/*     */   }
/*     */   
/*     */   public void eat(FoodProperties foodProperties) {
/*  31 */     add(foodProperties.nutrition(), foodProperties.saturation());
/*     */   }
/*     */   
/*     */   public void tick(ServerPlayer player) {
/*  35 */     ServerLevel level = player.level();
/*  36 */     Difficulty difficulty = level.getDifficulty();
/*     */     
/*  38 */     if (this.exhaustionLevel > 4.0F) {
/*  39 */       this.exhaustionLevel -= 4.0F;
/*     */       
/*  41 */       if (this.saturationLevel > 0.0F) {
/*  42 */         this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
/*  43 */       } else if (difficulty != Difficulty.PEACEFUL) {
/*  44 */         this.foodLevel = Math.max(this.foodLevel - 1, 0);
/*     */       } 
/*     */     } 
/*     */     
/*  48 */     boolean naturalRegen = (Boolean)level.getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION);
/*  49 */     if (naturalRegen && this.saturationLevel > 0.0F && player.isHurt() && this.foodLevel >= 20) {
/*  50 */       this.tickTimer++;
/*  51 */       if (this.tickTimer >= 10) {
/*  52 */         float saturationSpent = Math.min(this.saturationLevel, 6.0F);
/*  53 */         player.heal(saturationSpent / 6.0F);
/*  54 */         addExhaustion(saturationSpent);
/*  55 */         this.tickTimer = 0;
/*     */       } 
/*  57 */     } else if (naturalRegen && this.foodLevel >= 18 && player.isHurt()) {
/*  58 */       this.tickTimer++;
/*  59 */       if (this.tickTimer >= 80) {
/*  60 */         player.heal(1.0F);
/*  61 */         addExhaustion(6.0F);
/*  62 */         this.tickTimer = 0;
/*     */       } 
/*  64 */     } else if (this.foodLevel <= 0) {
/*  65 */       this.tickTimer++;
/*  66 */       if (this.tickTimer >= 80) {
/*  67 */         if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || (player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)) {
/*  68 */           player.hurtServer(level, player.damageSources().starve(), 1.0F);
/*     */         }
/*  70 */         this.tickTimer = 0;
/*     */       } 
/*     */     } else {
/*  73 */       this.tickTimer = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readAdditionalSaveData(ValueInput input) {
/*  78 */     this.foodLevel = input.getIntOr("foodLevel", 20);
/*  79 */     this.tickTimer = input.getIntOr("foodTickTimer", 0);
/*  80 */     this.saturationLevel = input.getFloatOr("foodSaturationLevel", 5.0F);
/*  81 */     this.exhaustionLevel = input.getFloatOr("foodExhaustionLevel", 0.0F);
/*     */   }
/*     */   
/*     */   public void addAdditionalSaveData(ValueOutput output) {
/*  85 */     output.putInt("foodLevel", this.foodLevel);
/*  86 */     output.putInt("foodTickTimer", this.tickTimer);
/*  87 */     output.putFloat("foodSaturationLevel", this.saturationLevel);
/*  88 */     output.putFloat("foodExhaustionLevel", this.exhaustionLevel);
/*     */   }
/*     */   
/*     */   public int getFoodLevel() {
/*  92 */     return this.foodLevel;
/*     */   }
/*     */   
/*     */   public boolean hasEnoughFood() {
/*  96 */     return (getFoodLevel() > 6.0F);
/*     */   }
/*     */   
/*     */   public boolean needsFood() {
/* 100 */     return (this.foodLevel < 20);
/*     */   }
/*     */   
/*     */   public void addExhaustion(float amount) {
/* 104 */     this.exhaustionLevel = Math.min(this.exhaustionLevel + amount, 40.0F);
/*     */   }
/*     */   
/*     */   public float getSaturationLevel() {
/* 108 */     return this.saturationLevel;
/*     */   }
/*     */   
/*     */   public void setFoodLevel(int food) {
/* 112 */     this.foodLevel = food;
/*     */   }
/*     */   
/*     */   public void setSaturation(float saturation) {
/* 116 */     this.saturationLevel = saturation;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/food/FoodData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */