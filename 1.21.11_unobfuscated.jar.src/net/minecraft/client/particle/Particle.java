/*     */ package net.minecraft.client.particle;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleLimit;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class Particle {
/*  17 */   private static final AABB INITIAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
/*  18 */   private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0D);
/*     */   
/*     */   protected final ClientLevel level;
/*     */   protected double xo;
/*     */   protected double yo;
/*     */   protected double zo;
/*     */   protected double x;
/*     */   protected double y;
/*     */   protected double z;
/*     */   protected double xd;
/*     */   protected double yd;
/*     */   protected double zd;
/*  30 */   private AABB bb = INITIAL_AABB;
/*     */   
/*     */   protected boolean onGround;
/*     */   
/*     */   protected boolean hasPhysics = true;
/*     */   private boolean stoppedByCollision;
/*     */   protected boolean removed;
/*  37 */   protected float bbWidth = 0.6F;
/*  38 */   protected float bbHeight = 1.8F;
/*     */   
/*  40 */   protected final RandomSource random = RandomSource.create();
/*     */   
/*     */   protected int age;
/*     */   protected int lifetime;
/*     */   protected float gravity;
/*  45 */   protected float friction = 0.98F;
/*     */   protected boolean speedUpWhenYMotionIsBlocked = false;
/*     */   
/*     */   protected Particle(ClientLevel level, double x, double y, double z) {
/*  49 */     this.level = level;
/*     */     
/*  51 */     setSize(0.2F, 0.2F);
/*  52 */     setPos(x, y, z);
/*  53 */     this.xo = x;
/*  54 */     this.yo = y;
/*  55 */     this.zo = z;
/*     */     
/*  57 */     this.lifetime = (int)(4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
/*     */   }
/*     */   
/*     */   public Particle(ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
/*  61 */     this(level, x, y, z);
/*     */     
/*  63 */     this.xd = xa + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
/*  64 */     this.yd = ya + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
/*  65 */     this.zd = za + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F);
/*  66 */     double speed = ((this.random.nextFloat() + this.random.nextFloat() + 1.0F) * 0.15F);
/*     */     
/*  68 */     double dd = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
/*  69 */     this.xd = this.xd / dd * speed * 0.4000000059604645D;
/*  70 */     this.yd = this.yd / dd * speed * 0.4000000059604645D + 0.10000000149011612D;
/*  71 */     this.zd = this.zd / dd * speed * 0.4000000059604645D;
/*     */   }
/*     */   
/*     */   public Particle setPower(float power) {
/*  75 */     this.xd *= power;
/*  76 */     this.yd = (this.yd - 0.10000000149011612D) * power + 0.10000000149011612D;
/*  77 */     this.zd *= power;
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public void setParticleSpeed(double xd, double yd, double zd) {
/*  82 */     this.xd = xd;
/*  83 */     this.yd = yd;
/*  84 */     this.zd = zd;
/*     */   }
/*     */   
/*     */   public Particle scale(float scale) {
/*  88 */     setSize(0.2F * scale, 0.2F * scale);
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public void setLifetime(int lifetime) {
/*  93 */     this.lifetime = lifetime;
/*     */   }
/*     */   
/*     */   public int getLifetime() {
/*  97 */     return this.lifetime;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 101 */     this.xo = this.x;
/* 102 */     this.yo = this.y;
/* 103 */     this.zo = this.z;
/*     */     
/* 105 */     if (this.age++ >= this.lifetime) {
/* 106 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/* 110 */     this.yd -= 0.04D * this.gravity;
/* 111 */     move(this.xd, this.yd, this.zd);
/* 112 */     if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
/* 113 */       this.xd *= 1.1D;
/* 114 */       this.zd *= 1.1D;
/*     */     } 
/* 116 */     this.xd *= this.friction;
/* 117 */     this.yd *= this.friction;
/* 118 */     this.zd *= this.friction;
/*     */     
/* 120 */     if (this.onGround) {
/* 121 */       this.xd *= 0.699999988079071D;
/* 122 */       this.zd *= 0.699999988079071D;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract ParticleRenderType getGroup();
/*     */   
/*     */   public String toString() {
/* 130 */     return getClass().getSimpleName() + ", Pos (" + getClass().getSimpleName() + "," + this.x + "," + this.y + "), Age " + this.z;
/*     */   }
/*     */   
/*     */   public void remove() {
/* 134 */     this.removed = true;
/*     */   }
/*     */   
/*     */   protected void setSize(float w, float h) {
/* 138 */     if (w != this.bbWidth || h != this.bbHeight) {
/* 139 */       this.bbWidth = w;
/* 140 */       this.bbHeight = h;
/* 141 */       AABB aabb = getBoundingBox();
/* 142 */       double newMinX = (aabb.minX + aabb.maxX - w) / 2.0D;
/* 143 */       double newMinZ = (aabb.minZ + aabb.maxZ - w) / 2.0D;
/* 144 */       setBoundingBox(new AABB(newMinX, aabb.minY, newMinZ, newMinX + this.bbWidth, aabb.minY + this.bbHeight, newMinZ + this.bbWidth));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 149 */     this.x = x;
/* 150 */     this.y = y;
/* 151 */     this.z = z;
/* 152 */     float w = this.bbWidth / 2.0F;
/* 153 */     float h = this.bbHeight;
/* 154 */     setBoundingBox(new AABB(x - w, y, z - w, x + w, y + h, z + w));
/*     */   }
/*     */   
/*     */   public void move(double xa, double ya, double za) {
/* 158 */     if (this.stoppedByCollision) {
/*     */       return;
/*     */     }
/*     */     
/* 162 */     double originalXa = xa;
/* 163 */     double originalYa = ya;
/* 164 */     double originalZa = za;
/*     */     
/* 166 */     if (this.hasPhysics && (xa != 0.0D || ya != 0.0D || za != 0.0D) && xa * xa + ya * ya + za * za < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
/* 167 */       Vec3 movement = Entity.collideBoundingBox(null, new Vec3(xa, ya, za), getBoundingBox(), (Level)this.level, List.of());
/* 168 */       xa = movement.x;
/* 169 */       ya = movement.y;
/* 170 */       za = movement.z;
/*     */     } 
/*     */     
/* 173 */     if (xa != 0.0D || ya != 0.0D || za != 0.0D) {
/* 174 */       setBoundingBox(getBoundingBox().move(xa, ya, za));
/* 175 */       setLocationFromBoundingbox();
/*     */     } 
/*     */     
/* 178 */     if (Math.abs(originalYa) >= 9.999999747378752E-6D && Math.abs(ya) < 9.999999747378752E-6D) {
/* 179 */       this.stoppedByCollision = true;
/*     */     }
/*     */     
/* 182 */     this.onGround = (originalYa != ya && originalYa < 0.0D);
/*     */     
/* 184 */     if (originalXa != xa) {
/* 185 */       this.xd = 0.0D;
/*     */     }
/* 187 */     if (originalZa != za) {
/* 188 */       this.zd = 0.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setLocationFromBoundingbox() {
/* 193 */     AABB aabb = getBoundingBox();
/* 194 */     this.x = (aabb.minX + aabb.maxX) / 2.0D;
/* 195 */     this.y = aabb.minY;
/* 196 */     this.z = (aabb.minZ + aabb.maxZ) / 2.0D;
/*     */   }
/*     */   
/*     */   protected int getLightColor(float a) {
/* 200 */     BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
/* 201 */     if (this.level.hasChunkAt(pos)) {
/* 202 */       return LevelRenderer.getLightColor((BlockAndTintGetter)this.level, pos);
/*     */     }
/* 204 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isAlive() {
/* 208 */     return !this.removed;
/*     */   }
/*     */   
/*     */   public AABB getBoundingBox() {
/* 212 */     return this.bb;
/*     */   }
/*     */   
/*     */   public void setBoundingBox(AABB bb) {
/* 216 */     this.bb = bb;
/*     */   }
/*     */   
/*     */   public Optional<ParticleLimit> getParticleLimit() {
/* 220 */     return Optional.empty();
/*     */   }
/*     */   public static final class LifetimeAlpha extends Record { private final float startAlpha; private final float endAlpha; private final float startAtNormalizedAge; private final float endAtNormalizedAge;
/* 223 */     public LifetimeAlpha(float startAlpha, float endAlpha, float startAtNormalizedAge, float endAtNormalizedAge) { this.startAlpha = startAlpha; this.endAlpha = endAlpha; this.startAtNormalizedAge = startAtNormalizedAge; this.endAtNormalizedAge = endAtNormalizedAge; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #223	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 223 */       //   0	7	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha; } public float startAlpha() { return this.startAlpha; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #223	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/Particle$LifetimeAlpha;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #223	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/particle/Particle$LifetimeAlpha;
/* 223 */       //   0	8	1	o	Ljava/lang/Object; } public float endAlpha() { return this.endAlpha; } public float startAtNormalizedAge() { return this.startAtNormalizedAge; } public float endAtNormalizedAge() { return this.endAtNormalizedAge; }
/* 224 */      public static final LifetimeAlpha ALWAYS_OPAQUE = new LifetimeAlpha(1.0F, 1.0F, 0.0F, 1.0F);
/*     */     
/*     */     public boolean isOpaque() {
/* 227 */       return (this.startAlpha >= 1.0F && this.endAlpha >= 1.0F);
/*     */     }
/*     */     
/*     */     public float currentAlphaForAge(int age, int lifetime, float partialTickTime) {
/* 231 */       if (Mth.equal(this.startAlpha, this.endAlpha)) {
/* 232 */         return this.startAlpha;
/*     */       }
/*     */       
/* 235 */       float timeNormalized = Mth.inverseLerp((age + partialTickTime) / lifetime, this.startAtNormalizedAge, this.endAtNormalizedAge);
/* 236 */       return Mth.clampedLerp(timeNormalized, this.startAlpha, this.endAlpha);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/Particle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */