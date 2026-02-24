/*     */ package net.minecraft.client.particle;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.state.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ 
/*     */ public abstract class SingleQuadParticle extends Particle {
/*     */   protected float quadSize;
/*     */   
/*     */   public static interface FacingCameraMode {
/*     */     public static final FacingCameraMode LOOKAT_XYZ;
/*     */     public static final FacingCameraMode LOOKAT_Y;
/*     */     
/*     */     static {
/*  21 */       LOOKAT_XYZ = ((target, camera, partialTickTime) -> target.set((org.joml.Quaternionfc)camera.rotation()));
/*     */ 
/*     */ 
/*     */       
/*  25 */       LOOKAT_Y = ((target, camera, partialTickTime) -> target.set(0.0F, (camera.rotation()).y, 0.0F, (camera.rotation()).w));
/*     */     }
/*     */ 
/*     */     
/*     */     void setRotation(Quaternionf param1Quaternionf, Camera param1Camera, float param1Float);
/*     */   }
/*  31 */   protected float rCol = 1.0F;
/*  32 */   protected float gCol = 1.0F;
/*  33 */   protected float bCol = 1.0F;
/*  34 */   protected float alpha = 1.0F;
/*     */   protected float roll;
/*     */   protected float oRoll;
/*     */   protected TextureAtlasSprite sprite;
/*     */   
/*     */   protected SingleQuadParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/*  40 */     super(level, x, y, z);
/*  41 */     this.sprite = sprite;
/*  42 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
/*     */   }
/*     */   
/*     */   protected SingleQuadParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/*  46 */     super(level, x, y, z, xa, ya, za);
/*  47 */     this.sprite = sprite;
/*  48 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
/*     */   }
/*     */   
/*     */   public FacingCameraMode getFacingCameraMode() {
/*  52 */     return FacingCameraMode.LOOKAT_XYZ;
/*     */   }
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/*  56 */     Quaternionf rotation = new Quaternionf();
/*  57 */     getFacingCameraMode().setRotation(rotation, camera, partialTickTime);
/*  58 */     if (this.roll != 0.0F) {
/*  59 */       rotation.rotateZ(Mth.lerp(partialTickTime, this.oRoll, this.roll));
/*     */     }
/*     */     
/*  62 */     extractRotatedQuad(particleTypeRenderState, camera, rotation, partialTickTime);
/*     */   }
/*     */   
/*     */   protected void extractRotatedQuad(QuadParticleRenderState particleTypeRenderState, Camera camera, Quaternionf rotation, float partialTickTime) {
/*  66 */     Vec3 pos = camera.position();
/*     */     
/*  68 */     float x = (float)(Mth.lerp(partialTickTime, this.xo, this.x) - pos.x());
/*  69 */     float y = (float)(Mth.lerp(partialTickTime, this.yo, this.y) - pos.y());
/*  70 */     float z = (float)(Mth.lerp(partialTickTime, this.zo, this.z) - pos.z());
/*     */     
/*  72 */     extractRotatedQuad(particleTypeRenderState, rotation, x, y, z, partialTickTime);
/*     */   }
/*     */   
/*     */   protected void extractRotatedQuad(QuadParticleRenderState particleTypeRenderState, Quaternionf rotation, float x, float y, float z, float partialTickTime) {
/*  76 */     particleTypeRenderState.add(getLayer(), x, y, z, rotation.x, rotation.y, rotation.z, rotation.w, getQuadSize(partialTickTime), getU0(), getU1(), getV0(), getV1(), net.minecraft.util.ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol), getLightColor(partialTickTime));
/*     */   }
/*     */   
/*     */   public float getQuadSize(float a) {
/*  80 */     return this.quadSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public Particle scale(float scale) {
/*  85 */     this.quadSize *= scale;
/*  86 */     return super.scale(scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleRenderType getGroup() {
/*  91 */     return ParticleRenderType.SINGLE_QUADS;
/*     */   }
/*     */   
/*     */   public void setSpriteFromAge(SpriteSet sprites) {
/*  95 */     if (!this.removed) {
/*  96 */       setSprite(sprites.get(this.age, this.lifetime));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setSprite(TextureAtlasSprite icon) {
/* 101 */     this.sprite = icon;
/*     */   }
/*     */   
/*     */   protected float getU0() {
/* 105 */     return this.sprite.getU0();
/*     */   }
/*     */   
/*     */   protected float getU1() {
/* 109 */     return this.sprite.getU1();
/*     */   }
/*     */   
/*     */   protected float getV0() {
/* 113 */     return this.sprite.getV0();
/*     */   }
/*     */   
/*     */   protected float getV1() {
/* 117 */     return this.sprite.getV1();
/*     */   }
/*     */   
/*     */   protected abstract Layer getLayer();
/*     */   
/*     */   public void setColor(float r, float g, float b) {
/* 123 */     this.rCol = r;
/* 124 */     this.gCol = g;
/* 125 */     this.bCol = b;
/*     */   }
/*     */   
/*     */   protected void setAlpha(float alpha) {
/* 129 */     this.alpha = alpha;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return getClass().getSimpleName() + ", Pos (" + getClass().getSimpleName() + "," + this.x + "," + this.y + "), RGBA (" + this.z + "," + this.rCol + "," + this.gCol + "," + this.bCol + "), Age " + this.alpha;
/*     */   }
/*     */   public static final class Layer extends Record { private final boolean translucent; private final net.minecraft.resources.Identifier textureAtlasLocation; private final RenderPipeline pipeline;
/* 137 */     public Layer(boolean translucent, net.minecraft.resources.Identifier textureAtlasLocation, RenderPipeline pipeline) { this.translucent = translucent; this.textureAtlasLocation = textureAtlasLocation; this.pipeline = pipeline; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 137 */       //   0	7	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer; } public boolean translucent() { return this.translucent; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/SingleQuadParticle$Layer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/particle/SingleQuadParticle$Layer;
/* 137 */       //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.resources.Identifier textureAtlasLocation() { return this.textureAtlasLocation; } public RenderPipeline pipeline() { return this.pipeline; }
/* 138 */      public static final Layer TERRAIN = new Layer(true, TextureAtlas.LOCATION_BLOCKS, RenderPipelines.TRANSLUCENT_PARTICLE);
/* 139 */     public static final Layer ITEMS = new Layer(true, TextureAtlas.LOCATION_ITEMS, RenderPipelines.TRANSLUCENT_PARTICLE);
/* 140 */     public static final Layer OPAQUE = new Layer(false, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.OPAQUE_PARTICLE);
/* 141 */     public static final Layer TRANSLUCENT = new Layer(true, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.TRANSLUCENT_PARTICLE); }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SingleQuadParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */