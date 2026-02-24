/*     */ package net.minecraft.client.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.decoration.Mannequin;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ClientMannequin
/*     */   extends Mannequin
/*     */   implements ClientAvatarEntity {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  22 */   public static final PlayerSkin DEFAULT_SKIN = DefaultPlayerSkin.get(Mannequin.DEFAULT_PROFILE.partialProfile());
/*  23 */   private final ClientAvatarState avatarState = new ClientAvatarState();
/*     */   
/*     */   private CompletableFuture<Optional<PlayerSkin>> skinLookup;
/*  26 */   private PlayerSkin skin = DEFAULT_SKIN;
/*     */   
/*     */   private final PlayerSkinRenderCache skinRenderCache;
/*     */ 
/*     */   
/*     */   public static void registerOverrides(PlayerSkinRenderCache cache) {
/*  32 */     Mannequin.constructor = ((type, level) -> (level instanceof net.minecraft.client.multiplayer.ClientLevel) ? new ClientMannequin(level, cache) : new Mannequin(type, level));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientMannequin(Level level, PlayerSkinRenderCache skinRenderCache) {
/*  38 */     super(level);
/*  39 */     this.skinRenderCache = skinRenderCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  44 */     super.tick();
/*  45 */     this.avatarState.tick(position(), getDeltaMovement());
/*  46 */     if (this.skinLookup != null && this.skinLookup.isDone()) {
/*     */       try {
/*  48 */         ((Optional)this.skinLookup.get()).ifPresent(this::setSkin);
/*  49 */         this.skinLookup = null;
/*  50 */       } catch (Exception e) {
/*  51 */         LOGGER.error("Error when trying to look up skin", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  58 */     super.onSyncedDataUpdated(accessor);
/*     */     
/*  60 */     if (accessor.equals(DATA_PROFILE)) {
/*  61 */       updateSkin();
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateSkin() {
/*  66 */     if (this.skinLookup != null) {
/*     */       
/*  68 */       CompletableFuture<Optional<PlayerSkin>> future = this.skinLookup;
/*  69 */       this.skinLookup = null;
/*  70 */       future.cancel(false);
/*     */     } 
/*     */     
/*  73 */     this.skinLookup = this.skinRenderCache.lookup(getProfile()).thenApply(info -> info.map(PlayerSkinRenderCache.RenderInfo::playerSkin));
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientAvatarState avatarState() {
/*  78 */     return this.avatarState;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerSkin getSkin() {
/*  83 */     return this.skin;
/*     */   }
/*     */   
/*     */   private void setSkin(PlayerSkin skin) {
/*  87 */     this.skin = skin;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component belowNameDisplay() {
/*  92 */     return getDescription();
/*     */   }
/*     */ 
/*     */   
/*     */   public Parrot.Variant getParrotVariantOnShoulder(boolean left) {
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean showExtraEars() {
/* 102 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/entity/ClientMannequin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */