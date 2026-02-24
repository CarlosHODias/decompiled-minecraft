/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.GlyphSource;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.client.gui.font.glyphs.EffectGlyph;
/*     */ import net.minecraft.client.gui.font.glyphs.SpecialGlyphs;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ public class FontSet
/*     */   implements AutoCloseable
/*     */ {
/*     */   private static final float LARGE_FORWARD_ADVANCE = 32.0F;
/*     */   
/*  34 */   private static final BakedGlyph INVISIBLE_MISSING_GLYPH = new BakedGlyph()
/*     */     {
/*     */       public GlyphInfo info() {
/*  37 */         return (GlyphInfo)SpecialGlyphs.MISSING;
/*     */       }
/*     */ 
/*     */       
/*     */       public TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
/*  42 */         return null;
/*     */       }
/*     */     };
/*     */   
/*     */   private final GlyphStitcher stitcher;
/*     */   
/*  48 */   private final UnbakedGlyph.Stitcher wrappedStitcher = new UnbakedGlyph.Stitcher()
/*     */     {
/*     */       public BakedGlyph stitch(GlyphInfo glyphInfo, GlyphBitmap glyphBitmap) {
/*  51 */         return (BakedGlyph)Objects.requireNonNullElse(FontSet.this.stitcher.stitch(glyphInfo, glyphBitmap), FontSet.this.missingGlyph);
/*     */       }
/*     */ 
/*     */       
/*     */       public BakedGlyph getMissing() {
/*  56 */         return FontSet.this.missingGlyph;
/*     */       }
/*     */     };
/*     */   
/*  60 */   private List<GlyphProvider.Conditional> allProviders = List.of();
/*  61 */   private List<GlyphProvider> activeProviders = List.of();
/*  62 */   private final Int2ObjectMap<IntList> glyphsByWidth = (Int2ObjectMap<IntList>)new Int2ObjectOpenHashMap(); private final CodepointMap<SelectedGlyphs> glyphCache; private final IntFunction<SelectedGlyphs> glyphGetter; private BakedGlyph missingGlyph; private final Supplier<BakedGlyph> missingGlyphGetter; private final SelectedGlyphs missingSelectedGlyphs; private EffectGlyph whiteGlyph; private final GlyphSource anyGlyphs; private final GlyphSource nonFishyGlyphs;
/*     */   public FontSet(GlyphStitcher stitcher) {
/*  64 */     this.glyphCache = new CodepointMap<>(x$0 -> new SelectedGlyphs[x$0], x$0 -> new SelectedGlyphs[x$0][]);
/*     */     
/*  66 */     this.glyphGetter = this::computeGlyphInfo;
/*     */     
/*  68 */     this.missingGlyph = INVISIBLE_MISSING_GLYPH;
/*  69 */     this.missingGlyphGetter = (() -> this.missingGlyph);
/*  70 */     this.missingSelectedGlyphs = new SelectedGlyphs(this.missingGlyphGetter, this.missingGlyphGetter);
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.anyGlyphs = new Source(false);
/*  75 */     this.nonFishyGlyphs = new Source(true);
/*     */ 
/*     */     
/*  78 */     this.stitcher = stitcher;
/*     */   }
/*     */   
/*     */   public void reload(List<GlyphProvider.Conditional> providers, Set<FontOption> options) {
/*  82 */     this.allProviders = providers;
/*  83 */     reload(options);
/*     */   }
/*     */   
/*     */   public void reload(Set<FontOption> options) {
/*  87 */     this.activeProviders = List.of();
/*  88 */     resetTextures();
/*  89 */     this.activeProviders = selectProviders(this.allProviders, options);
/*     */   }
/*     */   
/*     */   private void resetTextures() {
/*  93 */     this.stitcher.reset();
/*  94 */     this.glyphCache.clear();
/*  95 */     this.glyphsByWidth.clear();
/*     */     
/*  97 */     this.missingGlyph = (BakedGlyph)Objects.requireNonNull(SpecialGlyphs.MISSING.bake(this.stitcher));
/*  98 */     this.whiteGlyph = (EffectGlyph)SpecialGlyphs.WHITE.bake(this.stitcher);
/*     */   }
/*     */   
/*     */   private List<GlyphProvider> selectProviders(List<GlyphProvider.Conditional> providers, Set<FontOption> options) {
/* 102 */     IntOpenHashSet intOpenHashSet = new IntOpenHashSet();
/* 103 */     List<GlyphProvider> selectedProviders = new ArrayList<>();
/* 104 */     for (GlyphProvider.Conditional conditionalProvider : providers) {
/* 105 */       if (conditionalProvider.filter().apply(options)) {
/* 106 */         selectedProviders.add(conditionalProvider.provider());
/* 107 */         intOpenHashSet.addAll((IntCollection)conditionalProvider.provider().getSupportedGlyphs());
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     Set<GlyphProvider> usedProviders = Sets.newHashSet();
/* 112 */     intOpenHashSet.forEach(codepoint -> {
/*     */           for (GlyphProvider provider : (Iterable<GlyphProvider>)selectedProviders) {
/*     */             UnbakedGlyph glyph = provider.getGlyph(selectedProviders);
/*     */             
/*     */             if (glyph != null) {
/*     */               selectedProviders.add(provider);
/*     */               if (glyph.info() != SpecialGlyphs.MISSING) {
/*     */                 ((IntList)this.glyphsByWidth.computeIfAbsent(Mth.ceil(glyph.info().getAdvance(false)), ())).add(selectedProviders);
/*     */               }
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         });
/* 125 */     Objects.requireNonNull(usedProviders); return selectedProviders.stream().filter(usedProviders::contains).toList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 130 */     this.stitcher.close();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean hasFishyAdvance(GlyphInfo glyph) {
/* 135 */     float advance = glyph.getAdvance(false);
/* 136 */     if (advance < 0.0F || advance > 32.0F) {
/* 137 */       return true;
/*     */     }
/*     */     
/* 140 */     float boldAdvance = glyph.getAdvance(true);
/* 141 */     if (boldAdvance < 0.0F || boldAdvance > 32.0F) {
/* 142 */       return true;
/*     */     }
/*     */     
/* 145 */     return false;
/*     */   }
/*     */   
/*     */   private SelectedGlyphs computeGlyphInfo(int codepoint) {
/* 149 */     DelayedBake firstGlyph = null;
/*     */     
/* 151 */     for (GlyphProvider provider : this.activeProviders) {
/* 152 */       UnbakedGlyph glyph = provider.getGlyph(codepoint);
/* 153 */       if (glyph == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 157 */       if (firstGlyph == null) {
/* 158 */         firstGlyph = new DelayedBake(glyph);
/*     */       }
/* 160 */       if (!hasFishyAdvance(glyph.info())) {
/* 161 */         if (firstGlyph.unbaked == glyph) {
/* 162 */           return new SelectedGlyphs(firstGlyph, firstGlyph);
/*     */         }
/* 164 */         return new SelectedGlyphs(firstGlyph, new DelayedBake(glyph));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 169 */     if (firstGlyph != null) {
/* 170 */       return new SelectedGlyphs(firstGlyph, this.missingGlyphGetter);
/*     */     }
/*     */     
/* 173 */     return this.missingSelectedGlyphs;
/*     */   }
/*     */   
/*     */   private SelectedGlyphs getGlyph(int codepoint) {
/* 177 */     return this.glyphCache.computeIfAbsent(codepoint, this.glyphGetter);
/*     */   }
/*     */   
/*     */   public BakedGlyph getRandomGlyph(RandomSource random, int width) {
/* 181 */     IntList chars = (IntList)this.glyphsByWidth.get(width);
/* 182 */     if (chars != null && !chars.isEmpty())
/*     */     {
/*     */       
/* 185 */       return getGlyph(chars.getInt(random.nextInt(chars.size()))).nonFishy().get();
/*     */     }
/* 187 */     return this.missingGlyph;
/*     */   }
/*     */   
/*     */   public EffectGlyph whiteGlyph() {
/* 191 */     return Objects.<EffectGlyph>requireNonNull(this.whiteGlyph);
/*     */   }
/*     */   
/*     */   private class DelayedBake implements Supplier<BakedGlyph> {
/*     */     private final UnbakedGlyph unbaked;
/*     */     private BakedGlyph baked;
/*     */     
/*     */     private DelayedBake(UnbakedGlyph unbaked) {
/* 199 */       this.unbaked = unbaked;
/*     */     }
/*     */ 
/*     */     
/*     */     public BakedGlyph get() {
/* 204 */       if (this.baked == null) {
/* 205 */         this.baked = this.unbaked.bake(FontSet.this.wrappedStitcher);
/*     */       }
/* 207 */       return this.baked;
/*     */     }
/*     */   }
/*     */   
/*     */   public GlyphSource source(boolean nonFishyOnly) {
/* 212 */     return nonFishyOnly ? this.nonFishyGlyphs : this.anyGlyphs;
/*     */   }
/*     */   private static final class SelectedGlyphs extends Record { private final Supplier<BakedGlyph> any; private final Supplier<BakedGlyph> nonFishy;
/* 215 */     private SelectedGlyphs(Supplier<BakedGlyph> any, Supplier<BakedGlyph> nonFishy) { this.any = any; this.nonFishy = nonFishy; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 215 */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs; } public Supplier<BakedGlyph> any() { return this.any; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;
/* 215 */       //   0	8	1	o	Ljava/lang/Object; } public Supplier<BakedGlyph> nonFishy() { return this.nonFishy; }
/*     */      private Supplier<BakedGlyph> select(boolean filterFishy) {
/* 217 */       return filterFishy ? this.nonFishy : this.any;
/*     */     } }
/*     */ 
/*     */   
/*     */   public class Source implements GlyphSource {
/*     */     private final boolean filterFishyGlyphs;
/*     */     
/*     */     public Source(boolean filterFishyGlyphs) {
/* 225 */       this.filterFishyGlyphs = filterFishyGlyphs;
/*     */     }
/*     */ 
/*     */     
/*     */     public BakedGlyph getGlyph(int codepoint) {
/* 230 */       return FontSet.this.getGlyph(codepoint).select(this.filterFishyGlyphs).get();
/*     */     }
/*     */ 
/*     */     
/*     */     public BakedGlyph getRandomGlyph(RandomSource random, int width) {
/* 235 */       return FontSet.this.getRandomGlyph(random, width);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/FontSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */