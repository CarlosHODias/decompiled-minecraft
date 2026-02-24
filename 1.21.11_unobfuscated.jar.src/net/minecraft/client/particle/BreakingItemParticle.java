/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class BreakingItemParticle extends SingleQuadParticle {
/*     */   private final float uo;
/*     */   
/*     */   private BreakingItemParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/*  23 */     this(level, x, y, z, sprite);
/*  24 */     this.xd *= 0.10000000149011612D;
/*  25 */     this.yd *= 0.10000000149011612D;
/*  26 */     this.zd *= 0.10000000149011612D;
/*  27 */     this.xd += xa;
/*  28 */     this.yd += ya;
/*  29 */     this.zd += za;
/*     */   }
/*     */   private final float vo; private final SingleQuadParticle.Layer layer;
/*     */   protected BreakingItemParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/*  33 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/*  34 */     this.gravity = 1.0F;
/*  35 */     this.quadSize /= 2.0F;
/*     */     
/*  37 */     this.uo = this.random.nextFloat() * 3.0F;
/*  38 */     this.vo = this.random.nextFloat() * 3.0F;
/*  39 */     this.layer = sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS) ? SingleQuadParticle.Layer.TERRAIN : SingleQuadParticle.Layer.ITEMS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getU0() {
/*  44 */     return this.sprite.getU((this.uo + 1.0F) / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getU1() {
/*  49 */     return this.sprite.getU(this.uo / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getV0() {
/*  54 */     return this.sprite.getV(this.vo / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getV1() {
/*  59 */     return this.sprite.getV((this.vo + 1.0F) / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  64 */     return this.layer;
/*     */   }
/*     */   
/*     */   public static abstract class ItemParticleProvider<T extends ParticleOptions> implements ParticleProvider<T> {
/*  68 */     private final ItemStackRenderState scratchRenderState = new ItemStackRenderState();
/*     */     
/*     */     protected TextureAtlasSprite getSprite(ItemStack itemStack, ClientLevel level, RandomSource random) {
/*  71 */       Minecraft.getInstance().getItemModelResolver().updateForTopItem(this.scratchRenderState, itemStack, ItemDisplayContext.GROUND, (Level)level, null, 0);
/*  72 */       TextureAtlasSprite icon = this.scratchRenderState.pickParticleIcon(random);
/*  73 */       return (icon != null) ? icon : Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).missingSprite();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Provider
/*     */     extends ItemParticleProvider<ItemParticleOption> {
/*     */     public Particle createParticle(ItemParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  80 */       return new BreakingItemParticle(level, x, y, z, xAux, yAux, zAux, getSprite(options.getItem(), level, random));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SlimeProvider
/*     */     extends ItemParticleProvider<SimpleParticleType> {
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  87 */       return new BreakingItemParticle(level, x, y, z, getSprite(new ItemStack((ItemLike)Items.SLIME_BALL), level, random));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CobwebProvider
/*     */     extends ItemParticleProvider<SimpleParticleType> {
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  94 */       return new BreakingItemParticle(level, x, y, z, getSprite(new ItemStack((ItemLike)Items.COBWEB), level, random));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SnowballProvider
/*     */     extends ItemParticleProvider<SimpleParticleType> {
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 101 */       return new BreakingItemParticle(level, x, y, z, getSprite(new ItemStack((ItemLike)Items.SNOWBALL), level, random));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/BreakingItemParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */