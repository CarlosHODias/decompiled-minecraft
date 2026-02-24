/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.lwjgl.glfw.GLFWVidMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class VideoMode
/*     */ {
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final int redBits;
/*     */   private final int greenBits;
/*     */   private final int blueBits;
/*     */   private final int refreshRate;
/*     */   
/*     */   public VideoMode(int width, int height, int redBits, int greenBits, int blueBits, int refreshRate) {
/*  22 */     this.width = width;
/*  23 */     this.height = height;
/*  24 */     this.redBits = redBits;
/*  25 */     this.greenBits = greenBits;
/*  26 */     this.blueBits = blueBits;
/*  27 */     this.refreshRate = refreshRate;
/*     */   }
/*     */   
/*     */   public VideoMode(GLFWVidMode.Buffer buffer) {
/*  31 */     this.width = buffer.width();
/*  32 */     this.height = buffer.height();
/*  33 */     this.redBits = buffer.redBits();
/*  34 */     this.greenBits = buffer.greenBits();
/*  35 */     this.blueBits = buffer.blueBits();
/*  36 */     this.refreshRate = buffer.refreshRate();
/*     */   }
/*     */   
/*     */   public VideoMode(GLFWVidMode mode) {
/*  40 */     this.width = mode.width();
/*  41 */     this.height = mode.height();
/*  42 */     this.redBits = mode.redBits();
/*  43 */     this.greenBits = mode.greenBits();
/*  44 */     this.blueBits = mode.blueBits();
/*  45 */     this.refreshRate = mode.refreshRate();
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  49 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  53 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getRedBits() {
/*  57 */     return this.redBits;
/*     */   }
/*     */   
/*     */   public int getGreenBits() {
/*  61 */     return this.greenBits;
/*     */   }
/*     */   
/*     */   public int getBlueBits() {
/*  65 */     return this.blueBits;
/*     */   }
/*     */   
/*     */   public int getRefreshRate() {
/*  69 */     return this.refreshRate;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  74 */     if (this == o) {
/*  75 */       return true;
/*     */     }
/*  77 */     if (o == null || getClass() != o.getClass()) {
/*  78 */       return false;
/*     */     }
/*  80 */     VideoMode videoMode = (VideoMode)o;
/*  81 */     return (this.width == videoMode.width && this.height == videoMode.height && this.redBits == videoMode.redBits && this.greenBits == videoMode.greenBits && this.blueBits == videoMode.blueBits && this.refreshRate == videoMode.refreshRate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  92 */     return Objects.hash(new Object[] { this.width, this.height, this.redBits, this.greenBits, this.blueBits, this.refreshRate });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  97 */     return String.format(Locale.ROOT, "%sx%s@%s (%sbit)", new Object[] { this.width, this.height, this.refreshRate, this.redBits + this.greenBits + this.blueBits });
/*     */   }
/*     */   
/* 100 */   private static final Pattern PATTERN = Pattern.compile("(\\d+)x(\\d+)(?:@(\\d+)(?::(\\d+))?)?");
/*     */   
/*     */   public static Optional<VideoMode> read(String s) {
/* 103 */     if (s == null) {
/* 104 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     try {
/* 108 */       Matcher m = PATTERN.matcher(s);
/* 109 */       if (m.matches()) {
/* 110 */         int rate, bits; int width = Integer.parseInt(m.group(1));
/* 111 */         int height = Integer.parseInt(m.group(2));
/* 112 */         String rateString = m.group(3);
/*     */         
/* 114 */         if (rateString == null) {
/* 115 */           rate = 60;
/*     */         } else {
/* 117 */           rate = Integer.parseInt(rateString);
/*     */         } 
/* 119 */         String bitString = m.group(4);
/*     */         
/* 121 */         if (bitString == null) {
/* 122 */           bits = 24;
/*     */         } else {
/* 124 */           bits = Integer.parseInt(bitString);
/*     */         } 
/* 126 */         int componentBits = bits / 3;
/* 127 */         return Optional.of(new VideoMode(width, height, componentBits, componentBits, componentBits, rate));
/*     */       } 
/* 129 */     } catch (Exception exception) {}
/*     */     
/* 131 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public String write() {
/* 135 */     return String.format(Locale.ROOT, "%sx%s@%s:%s", new Object[] { this.width, this.height, this.refreshRate, this.redBits + this.greenBits + this.blueBits });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/VideoMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */