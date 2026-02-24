/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.client.renderer.SpriteCoordinateExpander;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class TextureAtlasSprite
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final Identifier atlasLocation;
/*     */   private final SpriteContents contents;
/*     */   private final int x;
/*     */   private final int y;
/*     */   private final float u0;
/*     */   private final float u1;
/*     */   private final float v0;
/*     */   private final float v1;
/*     */   private final int padding;
/*     */   
/*     */   protected TextureAtlasSprite(Identifier atlasLocation, SpriteContents contents, int atlasWidth, int atlasHeight, int x, int y, int padding) {
/*  28 */     this.atlasLocation = atlasLocation;
/*  29 */     this.contents = contents;
/*  30 */     this.padding = padding;
/*  31 */     this.x = x;
/*  32 */     this.y = y;
/*     */     
/*  34 */     this.u0 = (x + padding) / atlasWidth;
/*  35 */     this.u1 = (x + padding + contents.width()) / atlasWidth;
/*  36 */     this.v0 = (y + padding) / atlasHeight;
/*  37 */     this.v1 = (y + padding + contents.height()) / atlasHeight;
/*     */   }
/*     */   
/*     */   public int getX() {
/*  41 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  45 */     return this.y;
/*     */   }
/*     */   
/*     */   public float getU0() {
/*  49 */     return this.u0;
/*     */   }
/*     */   
/*     */   public float getU1() {
/*  53 */     return this.u1;
/*     */   }
/*     */   
/*     */   public SpriteContents contents() {
/*  57 */     return this.contents;
/*     */   }
/*     */   
/*     */   public SpriteContents.AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
/*  61 */     return this.contents.createAnimationState(uboSlice, spriteUboSize);
/*     */   }
/*     */   
/*     */   public float getU(float offset) {
/*  65 */     float diff = this.u1 - this.u0;
/*  66 */     return this.u0 + diff * offset;
/*     */   }
/*     */   
/*     */   public float getV0() {
/*  70 */     return this.v0;
/*     */   }
/*     */   
/*     */   public float getV1() {
/*  74 */     return this.v1;
/*     */   }
/*     */   
/*     */   public float getV(float offset) {
/*  78 */     float diff = this.v1 - this.v0;
/*  79 */     return this.v0 + diff * offset;
/*     */   }
/*     */   
/*     */   public Identifier atlasLocation() {
/*  83 */     return this.atlasLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  88 */     return "TextureAtlasSprite{contents='" + String.valueOf(this.contents) + "', u0=" + this.u0 + ", u1=" + this.u1 + ", v0=" + this.v0 + ", v1=" + this.v1 + "}";
/*     */   }
/*     */   
/*     */   public void uploadFirstFrame(GpuTexture destination, int level) {
/*  92 */     this.contents.uploadFirstFrame(destination, level);
/*     */   }
/*     */   
/*     */   public VertexConsumer wrap(VertexConsumer buffer) {
/*  96 */     return (VertexConsumer)new SpriteCoordinateExpander(buffer, this);
/*     */   }
/*     */   
/*     */   boolean isAnimated() {
/* 100 */     return this.contents.isAnimated();
/*     */   }
/*     */   
/*     */   public void uploadSpriteUbo(ByteBuffer uboBuffer, int startOffset, int maxMipLevel, int atlasWidth, int atlasHeight, int spriteUboSize) {
/* 104 */     for (int level = 0; level <= maxMipLevel; level++) {
/* 105 */       Std140Builder.intoBuffer(MemoryUtil.memSlice(uboBuffer, startOffset + level * spriteUboSize, spriteUboSize))
/* 106 */         .putMat4f((Matrix4fc)new Matrix4f().ortho2D(0.0F, (atlasWidth >> level), 0.0F, (atlasHeight >> level)))
/* 107 */         .putMat4f((Matrix4fc)new Matrix4f().translate((this.x >> level), (this.y >> level), 0.0F).scale((this.contents.width() + this.padding * 2 >> level), (this.contents.height() + this.padding * 2 >> level), 1.0F))
/* 108 */         .putFloat(this.padding / this.contents.width())
/* 109 */         .putFloat(this.padding / this.contents.height())
/* 110 */         .putInt(level);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 116 */     this.contents.close();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/TextureAtlasSprite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */