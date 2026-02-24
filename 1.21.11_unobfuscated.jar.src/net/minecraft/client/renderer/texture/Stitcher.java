/*     */ package net.minecraft.client.renderer.texture;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class Stitcher<T extends Stitcher.Entry> {
/*     */   private static final Comparator<Holder<?>> HOLDER_COMPARATOR;
/*     */   private final int mipLevel;
/*     */   
/*     */   static {
/*  13 */     HOLDER_COMPARATOR = Comparator.comparing(h -> -h.height).thenComparing(h -> -h.width).thenComparing(h -> h.entry.name());
/*     */   }
/*     */   
/*  16 */   private final List<Holder<T>> texturesToBeStitched = new ArrayList<>();
/*  17 */   private final List<Region<T>> storage = new ArrayList<>();
/*     */   
/*     */   private int storageX;
/*     */   private int storageY;
/*     */   private final int maxWidth;
/*     */   private final int maxHeight;
/*     */   private final int padding;
/*     */   
/*     */   public Stitcher(int maxWidth, int maxHeight, int mipLevel, int anisotropyBit) {
/*  26 */     this.mipLevel = mipLevel;
/*  27 */     this.maxWidth = maxWidth;
/*  28 */     this.maxHeight = maxHeight;
/*  29 */     this.padding = 1 << mipLevel << Mth.clamp(anisotropyBit - 1, 0, 4);
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  33 */     return this.storageX;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  37 */     return this.storageY;
/*     */   }
/*     */   
/*     */   public void registerSprite(T entry) {
/*  41 */     Holder<T> holder = new Holder<>(entry, 
/*     */         
/*  43 */         smallestFittingMinTexel(entry.width() + this.padding * 2, this.mipLevel), 
/*  44 */         smallestFittingMinTexel(entry.height() + this.padding * 2, this.mipLevel));
/*     */     
/*  46 */     this.texturesToBeStitched.add(holder);
/*     */   }
/*     */   
/*     */   public void stitch() {
/*  50 */     List<Holder<T>> holders = new ArrayList<>(this.texturesToBeStitched);
/*  51 */     holders.sort(HOLDER_COMPARATOR);
/*     */     
/*  53 */     for (Holder<T> holder : holders) {
/*  54 */       if (!addToStorage(holder)) {
/*  55 */         throw new StitcherException(holder.entry, (java.util.Collection)holders.stream().map(h -> h.entry).collect(com.google.common.collect.ImmutableList.toImmutableList()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void gatherSprites(SpriteLoader<T> loader) {
/*  65 */     for (Region<T> topRegion : this.storage) {
/*  66 */       topRegion.walk(loader, this.padding);
/*     */     }
/*     */   }
/*     */   
/*     */   private static int smallestFittingMinTexel(int input, int maxMipLevel) {
/*  71 */     return (input >> maxMipLevel) + (((input & (1 << maxMipLevel) - 1) == 0) ? 0 : 1) << maxMipLevel;
/*     */   }
/*     */   
/*     */   private boolean addToStorage(Holder<T> holder) {
/*  75 */     for (Region<T> region : this.storage) {
/*  76 */       if (region.add(holder)) {
/*  77 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  81 */     return expand(holder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean expand(Holder<T> holder) {
/*     */     boolean growOnX;
/*     */     Region<T> slot;
/*  93 */     int xCurrentSize = Mth.smallestEncompassingPowerOfTwo(this.storageX);
/*  94 */     int yCurrentSize = Mth.smallestEncompassingPowerOfTwo(this.storageY);
/*  95 */     int xNewSize = Mth.smallestEncompassingPowerOfTwo(this.storageX + holder.width);
/*  96 */     int yNewSize = Mth.smallestEncompassingPowerOfTwo(this.storageY + holder.height);
/*     */     
/*  98 */     boolean xCanGrow = (xNewSize <= this.maxWidth);
/*  99 */     boolean yCanGrow = (yNewSize <= this.maxHeight);
/*     */     
/* 101 */     if (!xCanGrow && !yCanGrow) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     boolean xWillGrow = (xCanGrow && xCurrentSize != xNewSize);
/* 106 */     boolean yWillGrow = (yCanGrow && yCurrentSize != yNewSize);
/*     */     
/* 108 */     if (xWillGrow ^ yWillGrow) {
/* 109 */       growOnX = xWillGrow;
/*     */     } else {
/*     */       
/* 112 */       growOnX = (xCanGrow && xCurrentSize <= yCurrentSize);
/*     */     } 
/*     */ 
/*     */     
/* 116 */     if (growOnX) {
/*     */       
/* 118 */       if (this.storageY == 0) {
/* 119 */         this.storageY = yNewSize;
/*     */       }
/*     */       
/* 122 */       slot = new Region<>(this.storageX, 0, xNewSize - this.storageX, this.storageY);
/* 123 */       this.storageX = xNewSize;
/*     */     } else {
/*     */       
/* 126 */       slot = new Region<>(0, this.storageY, this.storageX, yNewSize - this.storageY);
/* 127 */       this.storageY = yNewSize;
/*     */     } 
/*     */     
/* 130 */     slot.add(holder);
/* 131 */     this.storage.add(slot);
/*     */     
/* 133 */     return true;
/*     */   }
/*     */   private static final class Holder<T extends Entry> extends Record { private final T entry; private final int width; private final int height;
/* 136 */     private Holder(T entry, int width, int height) { this.entry = entry; this.width = width; this.height = height; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/Stitcher$Holder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 136 */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder<TT;>; } public T entry() { return this.entry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/Stitcher$Holder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/Stitcher$Holder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 136 */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder<TT;>; } public int width() { return this.width; } public int height() { return this.height; }
/*     */      }
/*     */   public static interface Entry { int width();
/*     */     int height();
/*     */     
/*     */     Identifier name(); }
/*     */   
/*     */   public static class Region<T extends Entry> { private final int originX;
/*     */     private final int originY;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private List<Region<T>> subSlots;
/*     */     private Stitcher.Holder<T> holder;
/*     */     
/*     */     public Region(int originX, int originY, int width, int height) {
/* 151 */       this.originX = originX;
/* 152 */       this.originY = originY;
/* 153 */       this.width = width;
/* 154 */       this.height = height;
/*     */     }
/*     */     
/*     */     public int getX() {
/* 158 */       return this.originX;
/*     */     }
/*     */     
/*     */     public int getY() {
/* 162 */       return this.originY;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(Stitcher.Holder<T> holder) {
/* 167 */       if (this.holder != null) {
/* 168 */         return false;
/*     */       }
/*     */       
/* 171 */       int textureWidth = holder.width;
/* 172 */       int textureHeight = holder.height;
/*     */ 
/*     */       
/* 175 */       if (textureWidth > this.width || textureHeight > this.height) {
/* 176 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 180 */       if (textureWidth == this.width && textureHeight == this.height) {
/*     */         
/* 182 */         this.holder = holder;
/* 183 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 187 */       if (this.subSlots == null) {
/* 188 */         this.subSlots = new ArrayList<>(1);
/*     */ 
/*     */         
/* 191 */         this.subSlots.add(new Region(this.originX, this.originY, textureWidth, textureHeight));
/*     */         
/* 193 */         int spareWidth = this.width - textureWidth;
/* 194 */         int spareHeight = this.height - textureHeight;
/*     */         
/* 196 */         if (spareHeight > 0 && spareWidth > 0) {
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
/* 211 */           int right = Math.max(this.height, spareWidth);
/* 212 */           int bottom = Math.max(this.width, spareHeight);
/* 213 */           if (right >= bottom) {
/* 214 */             this.subSlots.add(new Region(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
/* 215 */             this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, this.height));
/*     */           } else {
/* 217 */             this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
/* 218 */             this.subSlots.add(new Region(this.originX, this.originY + textureHeight, this.width, spareHeight));
/*     */           } 
/* 220 */         } else if (spareWidth == 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 230 */           this.subSlots.add(new Region(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
/* 231 */         } else if (spareHeight == 0) {
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
/* 242 */           this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
/*     */         } 
/*     */       } 
/*     */       
/* 246 */       for (Region<T> subSlot : this.subSlots) {
/* 247 */         if (subSlot.add(holder)) {
/* 248 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 252 */       return false;
/*     */     }
/*     */     
/*     */     public void walk(Stitcher.SpriteLoader<T> output, int padding) {
/* 256 */       if (this.holder != null) {
/* 257 */         output.load(this.holder.entry, getX(), getY(), padding);
/* 258 */       } else if (this.subSlots != null) {
/* 259 */         for (Region<T> subSlot : this.subSlots) {
/* 260 */           subSlot.walk(output, padding);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 267 */       return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + String.valueOf(this.holder) + ", subSlots=" + String.valueOf(this.subSlots) + "}";
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface SpriteLoader<T extends Entry> {
/*     */     void load(T param1T, int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/Stitcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */