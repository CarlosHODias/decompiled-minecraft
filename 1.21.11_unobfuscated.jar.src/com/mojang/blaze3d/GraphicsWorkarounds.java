/*     */ package com.mojang.blaze3d;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.GLX;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class GraphicsWorkarounds
/*     */ {
/*  13 */   private static final List<String> INTEL_GEN11_CORE = List.of(new String[] { "i3-1000g1", "i3-1000g4", "i3-1000ng4", "i3-1005g1", "i3-l13g4", "i5-1030g4", "i5-1030g7", "i5-1030ng7", "i5-1034g1", "i5-1035g1", "i5-1035g4", "i5-1035g7", "i5-1038ng7", "i5-l16g7", "i7-1060g7", "i7-1060ng7", "i7-1065g7", "i7-1068g7", "i7-1068ng7" });
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
/*  34 */   private static final List<String> INTEL_GEN11_ATOM = List.of("x6211e", "x6212re", "x6214re", "x6413e", "x6414re", "x6416re", "x6425e", "x6425re", "x6427fe");
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
/*  45 */   private static final List<String> INTEL_GEN11_CELERON = List.of("j6412", "j6413", "n4500", "n4505", "n5095", "n5095a", "n5100", "n5105", "n6210", "n6211");
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
/*  57 */   private static final List<String> INTEL_GEN11_PENTIUM = List.of("6805", "j6426", "n6415", "n6000", "n6005");
/*     */ 
/*     */   
/*     */   private static GraphicsWorkarounds instance;
/*     */ 
/*     */   
/*     */   private final WeakReference<GpuDevice> gpuDevice;
/*     */   
/*     */   private final boolean alwaysCreateFreshImmediateBuffer;
/*     */   
/*     */   private final boolean isGlOnDx12;
/*     */   
/*     */   private final boolean isAmd;
/*     */ 
/*     */   
/*     */   private GraphicsWorkarounds(GpuDevice gpuDevice) {
/*  73 */     this.gpuDevice = new WeakReference<>(gpuDevice);
/*  74 */     this.alwaysCreateFreshImmediateBuffer = isIntelGen11(gpuDevice);
/*  75 */     this.isGlOnDx12 = isGlOnDx12(gpuDevice);
/*  76 */     this.isAmd = isAmd(gpuDevice);
/*     */   }
/*     */   
/*     */   public static GraphicsWorkarounds get(GpuDevice gpuDevice) {
/*  80 */     GraphicsWorkarounds instance = GraphicsWorkarounds.instance;
/*  81 */     if (instance == null || instance.gpuDevice.get() != gpuDevice) {
/*  82 */       GraphicsWorkarounds.instance = instance = new GraphicsWorkarounds(gpuDevice);
/*     */     }
/*  84 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean alwaysCreateFreshImmediateBuffer() {
/*  91 */     return this.alwaysCreateFreshImmediateBuffer;
/*     */   }
/*     */   
/*     */   public boolean isGlOnDx12() {
/*  95 */     return this.isGlOnDx12;
/*     */   }
/*     */   
/*     */   public boolean isAmd() {
/*  99 */     return this.isAmd;
/*     */   }
/*     */   
/*     */   private static boolean isIntelGen11(GpuDevice gpuDevice) {
/* 103 */     String cpuInfo = GLX._getCpuInfo().toLowerCase(Locale.ROOT);
/* 104 */     String renderer = gpuDevice.getRenderer().toLowerCase(Locale.ROOT);
/* 105 */     if (!cpuInfo.contains("intel") || !renderer.contains("intel") || renderer.contains("mesa")) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     if (renderer.endsWith("gen11")) {
/* 110 */       return true;
/*     */     }
/* 112 */     if (!renderer.contains("uhd graphics") && !renderer.contains("iris")) {
/* 113 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 117 */     Objects.requireNonNull(cpuInfo);
/* 118 */     Objects.requireNonNull(cpuInfo);
/* 119 */     Objects.requireNonNull(cpuInfo);
/* 120 */     Objects.requireNonNull(cpuInfo); return ((cpuInfo.contains("atom") && INTEL_GEN11_ATOM.stream().anyMatch(cpuInfo::contains)) || (cpuInfo.contains("celeron") && INTEL_GEN11_CELERON.stream().anyMatch(cpuInfo::contains)) || (cpuInfo.contains("pentium") && INTEL_GEN11_PENTIUM.stream().anyMatch(cpuInfo::contains)) || INTEL_GEN11_CORE.stream().anyMatch(cpuInfo::contains));
/*     */   }
/*     */   
/*     */   private static boolean isGlOnDx12(GpuDevice gpuDevice) {
/* 124 */     boolean isWindowsArm64 = (Util.getPlatform() == Util.OS.WINDOWS && Util.isAarch64());
/* 125 */     return (isWindowsArm64 || gpuDevice.getRenderer().startsWith("D3D12"));
/*     */   }
/*     */   
/*     */   private static boolean isAmd(GpuDevice gpuDevice) {
/* 129 */     return gpuDevice.getRenderer().contains("AMD");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/GraphicsWorkarounds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */