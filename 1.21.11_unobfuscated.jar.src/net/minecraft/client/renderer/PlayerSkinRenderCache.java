/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.time.Duration;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.font.GlyphRenderTypes;
/*     */ import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.client.resources.SkinManager;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ 
/*     */ public class PlayerSkinRenderCache
/*     */ {
/*  25 */   public static final RenderType DEFAULT_PLAYER_SKIN_RENDER_TYPE = playerSkinRenderType(DefaultPlayerSkin.getDefaultSkin());
/*  26 */   public static final Duration CACHE_DURATION = Duration.ofMinutes(5L);
/*     */ 
/*     */   
/*  29 */   private final LoadingCache<ResolvableProfile, CompletableFuture<Optional<RenderInfo>>> renderInfoCache = CacheBuilder.newBuilder()
/*  30 */     .expireAfterAccess(CACHE_DURATION)
/*  31 */     .build(new CacheLoader<ResolvableProfile, CompletableFuture<Optional<RenderInfo>>>()
/*     */       {
/*     */         public CompletableFuture<Optional<PlayerSkinRenderCache.RenderInfo>> load(ResolvableProfile profile) {
/*  34 */           return 
/*  35 */             profile.resolveProfile(PlayerSkinRenderCache.this.profileResolver)
/*  36 */             .thenCompose(resolvedProfile -> PlayerSkinRenderCache.this.skinManager.get(profile).thenApply(()));
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private final LoadingCache<ResolvableProfile, RenderInfo> defaultSkinCache = CacheBuilder.newBuilder()
/*  46 */     .expireAfterAccess(CACHE_DURATION)
/*  47 */     .build(new CacheLoader<ResolvableProfile, RenderInfo>()
/*     */       {
/*     */         public PlayerSkinRenderCache.RenderInfo load(ResolvableProfile profile) {
/*  50 */           GameProfile temporaryProfile = profile.partialProfile();
/*  51 */           return new PlayerSkinRenderCache.RenderInfo(temporaryProfile, DefaultPlayerSkin.get(temporaryProfile), profile.skinPatch());
/*     */         }
/*     */       });
/*     */   
/*     */   private final TextureManager textureManager;
/*     */   private final SkinManager skinManager;
/*     */   private final ProfileResolver profileResolver;
/*     */   
/*     */   public PlayerSkinRenderCache(TextureManager textureManager, SkinManager skinManager, ProfileResolver profileResolver) {
/*  60 */     this.textureManager = textureManager;
/*  61 */     this.skinManager = skinManager;
/*  62 */     this.profileResolver = profileResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RenderInfo getOrDefault(ResolvableProfile profile) {
/*  72 */     RenderInfo result = ((Optional<RenderInfo>)lookup(profile).getNow(Optional.empty())).orElse(null);
/*  73 */     if (result != null) {
/*  74 */       return result;
/*     */     }
/*     */     
/*  77 */     return (RenderInfo)this.defaultSkinCache.getUnchecked(profile);
/*     */   }
/*     */   
/*     */   public Supplier<RenderInfo> createLookup(ResolvableProfile profile) {
/*  81 */     RenderInfo defaultForProfile = (RenderInfo)this.defaultSkinCache.getUnchecked(profile);
/*  82 */     CompletableFuture<Optional<RenderInfo>> future = (CompletableFuture<Optional<RenderInfo>>)this.renderInfoCache.getUnchecked(profile);
/*  83 */     Optional<RenderInfo> currentValue = future.getNow(null);
/*  84 */     if (currentValue != null) {
/*  85 */       RenderInfo finalValue = currentValue.orElse(defaultForProfile);
/*  86 */       return () -> finalValue;
/*     */     } 
/*     */     
/*  89 */     return () -> (RenderInfo)((Optional<RenderInfo>)future.getNow(Optional.empty())).orElse(defaultForProfile);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Optional<RenderInfo>> lookup(ResolvableProfile profile) {
/*  94 */     return (CompletableFuture<Optional<RenderInfo>>)this.renderInfoCache.getUnchecked(profile);
/*     */   }
/*     */   
/*     */   private static RenderType playerSkinRenderType(PlayerSkin playerSkin) {
/*  98 */     return SkullBlockRenderer.getPlayerSkinRenderType(playerSkin.body().texturePath());
/*     */   }
/*     */   
/*     */   public final class RenderInfo
/*     */   {
/*     */     private final GameProfile gameProfile;
/*     */     private final PlayerSkin playerSkin;
/*     */     private RenderType itemRenderType;
/*     */     private GpuTextureView textureView;
/*     */     private GlyphRenderTypes glyphRenderTypes;
/*     */     
/*     */     public RenderInfo(GameProfile gameProfile, PlayerSkin playerSkin, PlayerSkin.Patch patch) {
/* 110 */       this.gameProfile = gameProfile;
/* 111 */       this.playerSkin = playerSkin.with(patch);
/*     */     }
/*     */     
/*     */     public GameProfile gameProfile() {
/* 115 */       return this.gameProfile;
/*     */     }
/*     */     
/*     */     public PlayerSkin playerSkin() {
/* 119 */       return this.playerSkin;
/*     */     }
/*     */     
/*     */     public RenderType renderType() {
/* 123 */       if (this.itemRenderType == null) {
/* 124 */         this.itemRenderType = PlayerSkinRenderCache.playerSkinRenderType(this.playerSkin);
/*     */       }
/* 126 */       return this.itemRenderType;
/*     */     }
/*     */     
/*     */     public GpuTextureView textureView() {
/* 130 */       if (this.textureView == null) {
/* 131 */         this.textureView = PlayerSkinRenderCache.this.textureManager.getTexture(this.playerSkin.body().texturePath()).getTextureView();
/*     */       }
/* 133 */       return this.textureView;
/*     */     }
/*     */     
/*     */     public GlyphRenderTypes glyphRenderTypes() {
/* 137 */       if (this.glyphRenderTypes == null) {
/* 138 */         this.glyphRenderTypes = GlyphRenderTypes.createForColorTexture(this.playerSkin.body().texturePath());
/*     */       }
/* 140 */       return this.glyphRenderTypes;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 145 */       if (this != o) { if (o instanceof RenderInfo) { RenderInfo that = (RenderInfo)o; if (this.gameProfile.equals(that.gameProfile) && this.playerSkin.equals(that.playerSkin)); }  return false; }
/*     */     
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 150 */       int result = 1;
/* 151 */       result = 31 * result + this.gameProfile.hashCode();
/* 152 */       result = 31 * result + this.playerSkin.hashCode();
/* 153 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/PlayerSkinRenderCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */