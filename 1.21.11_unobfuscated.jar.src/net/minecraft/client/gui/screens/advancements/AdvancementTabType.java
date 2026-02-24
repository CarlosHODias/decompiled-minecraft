/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ enum AdvancementTabType
/*     */ {
/*  19 */   ABOVE(new Sprites(
/*  20 */       Identifier.withDefaultNamespace("advancements/tab_above_left_selected"), 
/*  21 */       Identifier.withDefaultNamespace("advancements/tab_above_middle_selected"), 
/*  22 */       Identifier.withDefaultNamespace("advancements/tab_above_right_selected")), new Sprites(
/*     */       
/*  24 */       Identifier.withDefaultNamespace("advancements/tab_above_left"), 
/*  25 */       Identifier.withDefaultNamespace("advancements/tab_above_middle"), 
/*  26 */       Identifier.withDefaultNamespace("advancements/tab_above_right")), 28, 32, 8),
/*     */   
/*  28 */   BELOW(new Sprites(
/*  29 */       Identifier.withDefaultNamespace("advancements/tab_below_left_selected"), 
/*  30 */       Identifier.withDefaultNamespace("advancements/tab_below_middle_selected"), 
/*  31 */       Identifier.withDefaultNamespace("advancements/tab_below_right_selected")), new Sprites(
/*     */       
/*  33 */       Identifier.withDefaultNamespace("advancements/tab_below_left"), 
/*  34 */       Identifier.withDefaultNamespace("advancements/tab_below_middle"), 
/*  35 */       Identifier.withDefaultNamespace("advancements/tab_below_right")), 28, 32, 8),
/*     */   
/*  37 */   LEFT(new Sprites(
/*  38 */       Identifier.withDefaultNamespace("advancements/tab_left_top_selected"), 
/*  39 */       Identifier.withDefaultNamespace("advancements/tab_left_middle_selected"), 
/*  40 */       Identifier.withDefaultNamespace("advancements/tab_left_bottom_selected")), new Sprites(
/*     */       
/*  42 */       Identifier.withDefaultNamespace("advancements/tab_left_top"), 
/*  43 */       Identifier.withDefaultNamespace("advancements/tab_left_middle"), 
/*  44 */       Identifier.withDefaultNamespace("advancements/tab_left_bottom")), 32, 28, 5),
/*     */   
/*  46 */   RIGHT(new Sprites(
/*  47 */       Identifier.withDefaultNamespace("advancements/tab_right_top_selected"), 
/*  48 */       Identifier.withDefaultNamespace("advancements/tab_right_middle_selected"), 
/*  49 */       Identifier.withDefaultNamespace("advancements/tab_right_bottom_selected")), new Sprites(
/*     */       
/*  51 */       Identifier.withDefaultNamespace("advancements/tab_right_top"), 
/*  52 */       Identifier.withDefaultNamespace("advancements/tab_right_middle"), 
/*  53 */       Identifier.withDefaultNamespace("advancements/tab_right_bottom")), 32, 28, 5);
/*     */   
/*     */   private final Sprites selectedSprites;
/*     */   
/*     */   private final Sprites unselectedSprites;
/*     */   
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final int max;
/*     */   
/*     */   AdvancementTabType(Sprites selectedSprites, Sprites unselectedSprites, int width, int height, int max) {
/*  64 */     this.selectedSprites = selectedSprites;
/*  65 */     this.unselectedSprites = unselectedSprites;
/*  66 */     this.width = width;
/*  67 */     this.height = height;
/*  68 */     this.max = max;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  72 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  76 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getMax() {
/*  80 */     return this.max;
/*     */   }
/*     */   public void draw(GuiGraphics graphics, int tabX, int tabY, boolean selected, int index) {
/*     */     Identifier sprite;
/*  84 */     Sprites sprites = selected ? this.selectedSprites : this.unselectedSprites;
/*     */     
/*  86 */     if (index == 0) {
/*  87 */       sprite = sprites.first();
/*  88 */     } else if (index == this.max - 1) {
/*  89 */       sprite = sprites.last();
/*     */     } else {
/*  91 */       sprite = sprites.middle();
/*     */     } 
/*  93 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, tabX, tabY, this.width, this.height);
/*     */   }
/*     */   
/*     */   public void drawIcon(GuiGraphics graphics, int xo, int yo, int index, ItemStack icon) {
/*  97 */     int x = xo + getX(index);
/*  98 */     int y = yo + getY(index);
/*  99 */     switch (ordinal()) {
/*     */       case 0:
/* 101 */         x += 6;
/* 102 */         y += 9;
/*     */         break;
/*     */       case 1:
/* 105 */         x += 6;
/* 106 */         y += 6;
/*     */         break;
/*     */       case 2:
/* 109 */         x += 10;
/* 110 */         y += 5;
/*     */         break;
/*     */       case 3:
/* 113 */         x += 6;
/* 114 */         y += 5;
/*     */         break;
/*     */     } 
/* 117 */     graphics.renderFakeItem(icon, x, y);
/*     */   }
/*     */   
/*     */   public int getX(int index) {
/* 121 */     switch (ordinal()) {
/*     */       case 0:
/* 123 */         return (this.width + 4) * index;
/*     */       case 1:
/* 125 */         return (this.width + 4) * index;
/*     */       case 2:
/* 127 */         return -this.width + 4;
/*     */       case 3:
/* 129 */         return 248;
/*     */     } 
/* 131 */     throw new UnsupportedOperationException("Don't know what this tab type is!" + String.valueOf(this));
/*     */   }
/*     */   
/*     */   public int getY(int index) {
/* 135 */     switch (ordinal()) {
/*     */       case 0:
/* 137 */         return -this.height + 4;
/*     */       case 1:
/* 139 */         return 136;
/*     */       case 2:
/* 141 */         return this.height * index;
/*     */       case 3:
/* 143 */         return this.height * index;
/*     */     } 
/* 145 */     throw new UnsupportedOperationException("Don't know what this tab type is!" + String.valueOf(this));
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(int xo, int yo, int index, double mx, double my) {
/* 149 */     int x = xo + getX(index);
/* 150 */     int y = yo + getY(index);
/* 151 */     return (mx > x && mx < (x + this.width) && my > y && my < (y + this.height));
/*     */   }
/*     */   private static final class Sprites extends Record { private final Identifier first; private final Identifier middle; private final Identifier last;
/* 154 */     private Sprites(Identifier first, Identifier middle, Identifier last) { this.first = first; this.middle = middle; this.last = last; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 154 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites; } public Identifier first() { return this.first; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;
/* 154 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier middle() { return this.middle; } public Identifier last() { return this.last; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/advancements/AdvancementTabType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */