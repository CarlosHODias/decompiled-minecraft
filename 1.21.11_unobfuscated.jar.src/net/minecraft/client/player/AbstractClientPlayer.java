/*     */ package net.minecraft.client.player;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.ClientAvatarEntity;
/*     */ import net.minecraft.client.entity.ClientAvatarState;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.StyledFormat;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.scores.DisplaySlot;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ 
/*     */ public abstract class AbstractClientPlayer extends Player implements ClientAvatarEntity {
/*     */   private PlayerInfo playerInfo;
/*     */   private final boolean showExtraEars;
/*  32 */   private final ClientAvatarState clientAvatarState = new ClientAvatarState();
/*     */   
/*     */   public AbstractClientPlayer(ClientLevel level, GameProfile gameProfile) {
/*  35 */     super((Level)level, gameProfile);
/*     */     
/*  37 */     this.showExtraEars = "deadmau5".equals(getGameProfile().name());
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType gameMode() {
/*  42 */     PlayerInfo info = getPlayerInfo();
/*  43 */     return (info != null) ? info.getGameMode() : null;
/*     */   }
/*     */   
/*     */   protected PlayerInfo getPlayerInfo() {
/*  47 */     if (this.playerInfo == null) {
/*  48 */       this.playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(getUUID());
/*     */     }
/*  50 */     return this.playerInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  55 */     this.clientAvatarState.tick(position(), getDeltaMovement());
/*  56 */     super.tick();
/*     */   }
/*     */   
/*     */   protected void addWalkedDistance(float distance) {
/*  60 */     this.clientAvatarState.addWalkDistance(distance);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientAvatarState avatarState() {
/*  65 */     return this.clientAvatarState;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component belowNameDisplay() {
/*  70 */     Scoreboard scoreboard = level().getScoreboard();
/*  71 */     Objective objective = scoreboard.getDisplayObjective(DisplaySlot.BELOW_NAME);
/*  72 */     if (objective != null) {
/*  73 */       ReadOnlyScoreInfo score = scoreboard.getPlayerScoreInfo((ScoreHolder)this, objective);
/*  74 */       MutableComponent mutableComponent = ReadOnlyScoreInfo.safeFormatValue(score, objective.numberFormatOrDefault((NumberFormat)StyledFormat.NO_STYLE));
/*  75 */       return (Component)Component.empty().append((Component)mutableComponent).append(CommonComponents.SPACE).append(objective.getDisplayName());
/*     */     } 
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerSkin getSkin() {
/*  82 */     PlayerInfo info = getPlayerInfo();
/*  83 */     return (info == null) ? DefaultPlayerSkin.get(getUUID()) : info.getSkin();
/*     */   }
/*     */ 
/*     */   
/*     */   public Parrot.Variant getParrotVariantOnShoulder(boolean left) {
/*  88 */     return (left ? getShoulderParrotLeft() : getShoulderParrotRight()).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rideTick() {
/*  93 */     super.rideTick();
/*  94 */     avatarState().resetBob();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  99 */     updateBob();
/* 100 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected void updateBob() {
/*     */     float tBob;
/* 105 */     if (!onGround() || isDeadOrDying() || isSwimming()) {
/* 106 */       tBob = 0.0F;
/*     */     } else {
/* 108 */       tBob = Math.min(0.1F, (float)getDeltaMovement().horizontalDistance());
/*     */     } 
/*     */     
/* 111 */     avatarState().updateBob(tBob);
/*     */   }
/*     */   
/*     */   public float getFieldOfViewModifier(boolean firstPerson, float effectScale) {
/* 115 */     float modifier = 1.0F;
/*     */     
/* 117 */     if ((getAbilities()).flying) {
/* 118 */       modifier *= 1.1F;
/*     */     }
/*     */     
/* 121 */     float walkingSpeed = getAbilities().getWalkingSpeed();
/* 122 */     if (walkingSpeed != 0.0F) {
/* 123 */       float speedFactor = (float)getAttributeValue(Attributes.MOVEMENT_SPEED) / walkingSpeed;
/* 124 */       modifier *= (speedFactor + 1.0F) / 2.0F;
/*     */     } 
/*     */     
/* 127 */     if (isUsingItem()) {
/* 128 */       if (getUseItem().is(Items.BOW)) {
/* 129 */         float scale = Math.min(getTicksUsingItem() / 20.0F, 1.0F);
/* 130 */         modifier *= 1.0F - Mth.square(scale) * 0.15F;
/* 131 */       } else if (firstPerson && isScoping()) {
/* 132 */         return 0.1F;
/*     */       } 
/*     */     }
/*     */     
/* 136 */     return Mth.lerp(effectScale, 1.0F, modifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean showExtraEars() {
/* 141 */     return this.showExtraEars;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/player/AbstractClientPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */