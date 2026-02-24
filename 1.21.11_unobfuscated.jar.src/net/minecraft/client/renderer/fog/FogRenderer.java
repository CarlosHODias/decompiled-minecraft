/*     */ package net.minecraft.client.renderer.fog;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.MappableRingBuffer;
/*     */ import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.DarknessFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.FogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.LavaFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.PowderedSnowFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FogRenderer
/*     */   implements AutoCloseable
/*     */ {
/*  37 */   public static final int FOG_UBO_SIZE = new Std140SizeCalculator()
/*  38 */     .putVec4()
/*  39 */     .putFloat()
/*  40 */     .putFloat()
/*  41 */     .putFloat()
/*  42 */     .putFloat()
/*  43 */     .putFloat()
/*  44 */     .putFloat()
/*  45 */     .get();
/*     */   
/*     */   public enum FogMode {
/*  48 */     NONE, WORLD;
/*     */   }
/*     */ 
/*     */   
/*  52 */   private static final List<FogEnvironment> FOG_ENVIRONMENTS = Lists.newArrayList((Object[])new FogEnvironment[] { (FogEnvironment)new LavaFogEnvironment(), (FogEnvironment)new PowderedSnowFogEnvironment(), (FogEnvironment)new BlindnessFogEnvironment(), (FogEnvironment)new DarknessFogEnvironment(), (FogEnvironment)new WaterFogEnvironment(), (FogEnvironment)new AtmosphericFogEnvironment() });
/*     */ 
/*     */   
/*     */   private static boolean fogEnabled = true;
/*     */ 
/*     */   
/*     */   private final GpuBuffer emptyBuffer;
/*     */ 
/*     */   
/*     */   private final MappableRingBuffer regularBuffer;
/*     */ 
/*     */ 
/*     */   
/*     */   public FogRenderer() {
/*  66 */     GpuDevice device = RenderSystem.getDevice();
/*  67 */     this.regularBuffer = new MappableRingBuffer(() -> "Fog UBO", 130, FOG_UBO_SIZE);
/*     */     
/*  69 */     MemoryStack stack = MemoryStack.stackPush(); 
/*  70 */     try { ByteBuffer buffer = stack.malloc(FOG_UBO_SIZE);
/*  71 */       updateBuffer(buffer, 0, new Vector4f(0.0F), Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
/*  72 */       this.emptyBuffer = device.createBuffer(() -> "Empty fog", 128, buffer.flip());
/*  73 */       if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/*     */         try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  76 */      RenderSystem.setShaderFog(getBuffer(FogMode.NONE));
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  81 */     this.emptyBuffer.close();
/*  82 */     this.regularBuffer.close();
/*     */   }
/*     */   
/*     */   public void endFrame() {
/*  86 */     this.regularBuffer.rotate();
/*     */   }
/*     */   
/*     */   public GpuBufferSlice getBuffer(FogMode mode) {
/*  90 */     if (!fogEnabled) {
/*  91 */       return this.emptyBuffer.slice(0L, FOG_UBO_SIZE);
/*     */     }
/*  93 */     switch (mode.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */       
/*  95 */       this.regularBuffer.currentBuffer().slice(0L, FOG_UBO_SIZE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Vector4f computeFogColor(Camera camera, float partialTicks, ClientLevel level, int renderDistance, float darkenWorldAmount) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual getFogType : (Lnet/minecraft/client/Camera;)Lnet/minecraft/world/level/material/FogType;
/*     */     //   5: astore #6
/*     */     //   7: aload_1
/*     */     //   8: invokevirtual entity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   11: astore #7
/*     */     //   13: aconst_null
/*     */     //   14: astore #8
/*     */     //   16: aconst_null
/*     */     //   17: astore #9
/*     */     //   19: getstatic net/minecraft/client/renderer/fog/FogRenderer.FOG_ENVIRONMENTS : Ljava/util/List;
/*     */     //   22: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   27: astore #10
/*     */     //   29: aload #10
/*     */     //   31: invokeinterface hasNext : ()Z
/*     */     //   36: ifeq -> 100
/*     */     //   39: aload #10
/*     */     //   41: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   46: checkcast net/minecraft/client/renderer/fog/environment/FogEnvironment
/*     */     //   49: astore #11
/*     */     //   51: aload #11
/*     */     //   53: aload #6
/*     */     //   55: aload #7
/*     */     //   57: invokevirtual isApplicable : (Lnet/minecraft/world/level/material/FogType;Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   60: ifeq -> 97
/*     */     //   63: aload #8
/*     */     //   65: ifnonnull -> 80
/*     */     //   68: aload #11
/*     */     //   70: invokevirtual providesColor : ()Z
/*     */     //   73: ifeq -> 80
/*     */     //   76: aload #11
/*     */     //   78: astore #8
/*     */     //   80: aload #9
/*     */     //   82: ifnonnull -> 97
/*     */     //   85: aload #11
/*     */     //   87: invokevirtual modifiesDarkness : ()Z
/*     */     //   90: ifeq -> 97
/*     */     //   93: aload #11
/*     */     //   95: astore #9
/*     */     //   97: goto -> 29
/*     */     //   100: aload #8
/*     */     //   102: ifnonnull -> 115
/*     */     //   105: new java/lang/IllegalStateException
/*     */     //   108: dup
/*     */     //   109: ldc 'No color source environment found'
/*     */     //   111: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   114: athrow
/*     */     //   115: aload #8
/*     */     //   117: aload_3
/*     */     //   118: aload_1
/*     */     //   119: iload #4
/*     */     //   121: fload_2
/*     */     //   122: invokevirtual getBaseColor : (Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Camera;IF)I
/*     */     //   125: istore #10
/*     */     //   127: aload_3
/*     */     //   128: invokevirtual getLevelData : ()Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;
/*     */     //   131: invokevirtual voidDarknessOnsetRange : ()F
/*     */     //   134: fstore #11
/*     */     //   136: fload #11
/*     */     //   138: aload_3
/*     */     //   139: invokevirtual getMinY : ()I
/*     */     //   142: i2f
/*     */     //   143: fadd
/*     */     //   144: aload_1
/*     */     //   145: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   148: getfield y : D
/*     */     //   151: d2f
/*     */     //   152: fsub
/*     */     //   153: fload #11
/*     */     //   155: fdiv
/*     */     //   156: fconst_0
/*     */     //   157: fconst_1
/*     */     //   158: invokestatic clamp : (FFF)F
/*     */     //   161: fstore #12
/*     */     //   163: aload #9
/*     */     //   165: ifnull -> 187
/*     */     //   168: aload #7
/*     */     //   170: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   173: astore #13
/*     */     //   175: aload #9
/*     */     //   177: aload #13
/*     */     //   179: fload #12
/*     */     //   181: fload_2
/*     */     //   182: invokevirtual getModifiedDarkness : (Lnet/minecraft/world/entity/LivingEntity;FF)F
/*     */     //   185: fstore #12
/*     */     //   187: iload #10
/*     */     //   189: invokestatic redFloat : (I)F
/*     */     //   192: fstore #13
/*     */     //   194: iload #10
/*     */     //   196: invokestatic greenFloat : (I)F
/*     */     //   199: fstore #14
/*     */     //   201: iload #10
/*     */     //   203: invokestatic blueFloat : (I)F
/*     */     //   206: fstore #15
/*     */     //   208: fload #12
/*     */     //   210: fconst_0
/*     */     //   211: fcmpl
/*     */     //   212: ifle -> 261
/*     */     //   215: aload #6
/*     */     //   217: getstatic net/minecraft/world/level/material/FogType.LAVA : Lnet/minecraft/world/level/material/FogType;
/*     */     //   220: if_acmpeq -> 261
/*     */     //   223: aload #6
/*     */     //   225: getstatic net/minecraft/world/level/material/FogType.POWDER_SNOW : Lnet/minecraft/world/level/material/FogType;
/*     */     //   228: if_acmpeq -> 261
/*     */     //   231: fconst_1
/*     */     //   232: fload #12
/*     */     //   234: fsub
/*     */     //   235: invokestatic square : (F)F
/*     */     //   238: fstore #16
/*     */     //   240: fload #13
/*     */     //   242: fload #16
/*     */     //   244: fmul
/*     */     //   245: fstore #13
/*     */     //   247: fload #14
/*     */     //   249: fload #16
/*     */     //   251: fmul
/*     */     //   252: fstore #14
/*     */     //   254: fload #15
/*     */     //   256: fload #16
/*     */     //   258: fmul
/*     */     //   259: fstore #15
/*     */     //   261: fload #5
/*     */     //   263: fconst_0
/*     */     //   264: fcmpl
/*     */     //   265: ifle -> 310
/*     */     //   268: fload #5
/*     */     //   270: fload #13
/*     */     //   272: fload #13
/*     */     //   274: ldc 0.7
/*     */     //   276: fmul
/*     */     //   277: invokestatic lerp : (FFF)F
/*     */     //   280: fstore #13
/*     */     //   282: fload #5
/*     */     //   284: fload #14
/*     */     //   286: fload #14
/*     */     //   288: ldc 0.6
/*     */     //   290: fmul
/*     */     //   291: invokestatic lerp : (FFF)F
/*     */     //   294: fstore #14
/*     */     //   296: fload #5
/*     */     //   298: fload #15
/*     */     //   300: fload #15
/*     */     //   302: ldc 0.6
/*     */     //   304: fmul
/*     */     //   305: invokestatic lerp : (FFF)F
/*     */     //   308: fstore #15
/*     */     //   310: aload #6
/*     */     //   312: getstatic net/minecraft/world/level/material/FogType.WATER : Lnet/minecraft/world/level/material/FogType;
/*     */     //   315: if_acmpne -> 345
/*     */     //   318: aload #7
/*     */     //   320: instanceof net/minecraft/client/player/LocalPlayer
/*     */     //   323: ifeq -> 339
/*     */     //   326: aload #7
/*     */     //   328: checkcast net/minecraft/client/player/LocalPlayer
/*     */     //   331: invokevirtual getWaterVision : ()F
/*     */     //   334: fstore #16
/*     */     //   336: goto -> 396
/*     */     //   339: fconst_1
/*     */     //   340: fstore #16
/*     */     //   342: goto -> 396
/*     */     //   345: aload #7
/*     */     //   347: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   350: ifeq -> 393
/*     */     //   353: aload #7
/*     */     //   355: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   358: astore #17
/*     */     //   360: aload #17
/*     */     //   362: getstatic net/minecraft/world/effect/MobEffects.NIGHT_VISION : Lnet/minecraft/core/Holder;
/*     */     //   365: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*     */     //   368: ifeq -> 393
/*     */     //   371: aload #17
/*     */     //   373: getstatic net/minecraft/world/effect/MobEffects.DARKNESS : Lnet/minecraft/core/Holder;
/*     */     //   376: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*     */     //   379: ifne -> 393
/*     */     //   382: aload #17
/*     */     //   384: fload_2
/*     */     //   385: invokestatic getNightVisionScale : (Lnet/minecraft/world/entity/LivingEntity;F)F
/*     */     //   388: fstore #16
/*     */     //   390: goto -> 396
/*     */     //   393: fconst_0
/*     */     //   394: fstore #16
/*     */     //   396: fload #13
/*     */     //   398: fconst_0
/*     */     //   399: fcmpl
/*     */     //   400: ifeq -> 475
/*     */     //   403: fload #14
/*     */     //   405: fconst_0
/*     */     //   406: fcmpl
/*     */     //   407: ifeq -> 475
/*     */     //   410: fload #15
/*     */     //   412: fconst_0
/*     */     //   413: fcmpl
/*     */     //   414: ifeq -> 475
/*     */     //   417: fconst_1
/*     */     //   418: fload #13
/*     */     //   420: fload #14
/*     */     //   422: fload #15
/*     */     //   424: invokestatic max : (FF)F
/*     */     //   427: invokestatic max : (FF)F
/*     */     //   430: fdiv
/*     */     //   431: fstore #17
/*     */     //   433: fload #16
/*     */     //   435: fload #13
/*     */     //   437: fload #13
/*     */     //   439: fload #17
/*     */     //   441: fmul
/*     */     //   442: invokestatic lerp : (FFF)F
/*     */     //   445: fstore #13
/*     */     //   447: fload #16
/*     */     //   449: fload #14
/*     */     //   451: fload #14
/*     */     //   453: fload #17
/*     */     //   455: fmul
/*     */     //   456: invokestatic lerp : (FFF)F
/*     */     //   459: fstore #14
/*     */     //   461: fload #16
/*     */     //   463: fload #15
/*     */     //   465: fload #15
/*     */     //   467: fload #17
/*     */     //   469: fmul
/*     */     //   470: invokestatic lerp : (FFF)F
/*     */     //   473: fstore #15
/*     */     //   475: new org/joml/Vector4f
/*     */     //   478: dup
/*     */     //   479: fload #13
/*     */     //   481: fload #14
/*     */     //   483: fload #15
/*     */     //   485: fconst_1
/*     */     //   486: invokespecial <init> : (FFFF)V
/*     */     //   489: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #100	-> 0
/*     */     //   #101	-> 7
/*     */     //   #103	-> 13
/*     */     //   #104	-> 16
/*     */     //   #105	-> 19
/*     */     //   #106	-> 51
/*     */     //   #107	-> 63
/*     */     //   #108	-> 76
/*     */     //   #110	-> 80
/*     */     //   #111	-> 93
/*     */     //   #114	-> 97
/*     */     //   #116	-> 100
/*     */     //   #117	-> 105
/*     */     //   #120	-> 115
/*     */     //   #122	-> 127
/*     */     //   #123	-> 136
/*     */     //   #124	-> 163
/*     */     //   #125	-> 168
/*     */     //   #126	-> 175
/*     */     //   #129	-> 187
/*     */     //   #130	-> 194
/*     */     //   #131	-> 201
/*     */     //   #133	-> 208
/*     */     //   #134	-> 231
/*     */     //   #135	-> 240
/*     */     //   #136	-> 247
/*     */     //   #137	-> 254
/*     */     //   #140	-> 261
/*     */     //   #141	-> 268
/*     */     //   #142	-> 282
/*     */     //   #143	-> 296
/*     */     //   #147	-> 310
/*     */     //   #148	-> 318
/*     */     //   #149	-> 326
/*     */     //   #151	-> 339
/*     */     //   #153	-> 345
/*     */     //   #154	-> 382
/*     */     //   #156	-> 393
/*     */     //   #159	-> 396
/*     */     //   #161	-> 417
/*     */     //   #162	-> 433
/*     */     //   #163	-> 447
/*     */     //   #164	-> 461
/*     */     //   #167	-> 475
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   51	46	11	fogEnvironment	Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;
/*     */     //   175	12	13	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   240	21	16	brightness	F
/*     */     //   336	3	16	brightenFactor	F
/*     */     //   342	3	16	brightenFactor	F
/*     */     //   390	3	16	brightenFactor	F
/*     */     //   360	33	17	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   433	42	17	targetScale	F
/*     */     //   0	490	0	this	Lnet/minecraft/client/renderer/fog/FogRenderer;
/*     */     //   0	490	1	camera	Lnet/minecraft/client/Camera;
/*     */     //   0	490	2	partialTicks	F
/*     */     //   0	490	3	level	Lnet/minecraft/client/multiplayer/ClientLevel;
/*     */     //   0	490	4	renderDistance	I
/*     */     //   0	490	5	darkenWorldAmount	F
/*     */     //   7	483	6	fogType	Lnet/minecraft/world/level/material/FogType;
/*     */     //   13	477	7	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   16	474	8	colorSourceEnvironment	Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;
/*     */     //   19	471	9	darknessModifyingEnvironment	Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;
/*     */     //   127	363	10	color	I
/*     */     //   136	354	11	voidDarknessOnsetRange	F
/*     */     //   163	327	12	darkness	F
/*     */     //   194	296	13	fogRed	F
/*     */     //   201	289	14	fogGreen	F
/*     */     //   208	282	15	fogBlue	F
/*     */     //   396	94	16	brightenFactor	F
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean toggleFog() {
/* 171 */     return fogEnabled = !fogEnabled;
/*     */   }
/*     */   
/*     */   public Vector4f setupFog(Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level) {
/* 175 */     float partialTickTime = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 176 */     Vector4f fogColor = computeFogColor(camera, partialTickTime, level, renderDistanceInChunks, darkenWorldAmount);
/* 177 */     float renderDistanceInBlocks = (renderDistanceInChunks * 16);
/*     */     
/* 179 */     FogType fogType = getFogType(camera);
/* 180 */     Entity entity = camera.entity();
/* 181 */     FogData fog = new FogData();
/*     */     
/* 183 */     for (FogEnvironment fogEnvironment : FOG_ENVIRONMENTS) {
/* 184 */       if (fogEnvironment.isApplicable(fogType, entity)) {
/* 185 */         fogEnvironment.setupFog(fog, camera, level, renderDistanceInBlocks, deltaTracker);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 190 */     float renderDistanceFogSpan = Mth.clamp(renderDistanceInBlocks / 10.0F, 4.0F, 64.0F);
/* 191 */     fog.renderDistanceStart = renderDistanceInBlocks - renderDistanceFogSpan;
/* 192 */     fog.renderDistanceEnd = renderDistanceInBlocks;
/*     */     
/* 194 */     GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.regularBuffer.currentBuffer(), false, true); 
/* 195 */     try { updateBuffer(view.data(), 0, fogColor, fog.environmentalStart, fog.environmentalEnd, fog.renderDistanceStart, fog.renderDistanceEnd, fog.skyEnd, fog.cloudEnd);
/* 196 */       if (view != null) view.close();  } catch (Throwable throwable) { if (view != null)
/* 197 */         try { view.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return fogColor;
/*     */   }
/*     */   
/*     */   private FogType getFogType(Camera camera) {
/* 201 */     FogType blockFogType = camera.getFluidInCamera();
/* 202 */     if (blockFogType == FogType.NONE) {
/* 203 */       return FogType.ATMOSPHERIC;
/*     */     }
/* 205 */     return blockFogType;
/*     */   }
/*     */   
/*     */   private void updateBuffer(ByteBuffer byteBuffer, int offset, Vector4f fogColor, float environmentalStart, float environmentalEnd, float renderDistanceStart, float renderDistanceEnd, float skyEnd, float endClouds) {
/* 209 */     byteBuffer.position(offset);
/* 210 */     Std140Builder.intoBuffer(byteBuffer)
/* 211 */       .putVec4((Vector4fc)fogColor)
/* 212 */       .putFloat(environmentalStart)
/* 213 */       .putFloat(environmentalEnd)
/* 214 */       .putFloat(renderDistanceStart)
/* 215 */       .putFloat(renderDistanceEnd)
/* 216 */       .putFloat(skyEnd)
/* 217 */       .putFloat(endClouds);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/fog/FogRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */