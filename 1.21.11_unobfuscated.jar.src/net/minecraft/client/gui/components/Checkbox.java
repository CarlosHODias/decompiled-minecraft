/*     */ package net.minecraft.client.gui.components;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class Checkbox extends AbstractButton {
/*  17 */   private static final Identifier CHECKBOX_SELECTED_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_selected_highlighted");
/*  18 */   private static final Identifier CHECKBOX_SELECTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_selected");
/*  19 */   private static final Identifier CHECKBOX_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/checkbox_highlighted");
/*  20 */   private static final Identifier CHECKBOX_SPRITE = Identifier.withDefaultNamespace("widget/checkbox");
/*     */   
/*     */   private static final int SPACING = 4;
/*     */   
/*     */   private static final int BOX_PADDING = 8;
/*     */   private boolean selected;
/*     */   private final OnValueChange onValueChange;
/*     */   private final MultiLineTextWidget textWidget;
/*     */   
/*     */   public static interface OnValueChange
/*     */   {
/*     */     public static final OnValueChange NOP = (checkbox, value) -> {
/*     */       
/*     */       };
/*     */     
/*     */     void onValueChange(Checkbox param1Checkbox, boolean param1Boolean);
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final Component message;
/*     */     private final Font font;
/*     */     private int maxWidth;
/*  43 */     private int x = 0;
/*  44 */     private int y = 0;
/*  45 */     private Checkbox.OnValueChange onValueChange = Checkbox.OnValueChange.NOP;
/*     */     private boolean selected = false;
/*  47 */     private OptionInstance<Boolean> option = null;
/*  48 */     private Tooltip tooltip = null;
/*     */     
/*     */     private Builder(Component message, Font font) {
/*  51 */       this.message = message;
/*  52 */       this.font = font;
/*  53 */       this.maxWidth = Checkbox.getDefaultWidth(message, font);
/*     */     }
/*     */     
/*     */     public Builder pos(int x, int y) {
/*  57 */       this.x = x;
/*  58 */       this.y = y;
/*  59 */       return this;
/*     */     }
/*     */     
/*     */     public Builder onValueChange(Checkbox.OnValueChange onValueChange) {
/*  63 */       this.onValueChange = onValueChange;
/*  64 */       return this;
/*     */     }
/*     */     
/*     */     public Builder selected(boolean selected) {
/*  68 */       this.selected = selected;
/*  69 */       this.option = null;
/*  70 */       return this;
/*     */     }
/*     */     
/*     */     public Builder selected(OptionInstance<Boolean> option) {
/*  74 */       this.option = option;
/*  75 */       this.selected = (Boolean)option.get();
/*  76 */       return this;
/*     */     }
/*     */     
/*     */     public Builder tooltip(Tooltip tooltip) {
/*  80 */       this.tooltip = tooltip;
/*  81 */       return this;
/*     */     }
/*     */     
/*     */     public Builder maxWidth(int maxWidth) {
/*  85 */       this.maxWidth = maxWidth;
/*  86 */       return this;
/*     */     }
/*     */     
/*     */     public Checkbox build() {
/*  90 */       Checkbox.OnValueChange onChange = (this.option == null) ? this.onValueChange : ((checkbox, value) -> {
/*     */           this.option.set(value);
/*     */           
/*     */           this.onValueChange.onValueChange(checkbox, value);
/*     */         });
/*  95 */       Checkbox box = new Checkbox(this.x, this.y, this.maxWidth, this.message, this.font, this.selected, onChange);
/*  96 */       box.setTooltip(this.tooltip);
/*  97 */       return box;
/*     */     }
/*     */   }
/*     */   
/*     */   private Checkbox(int x, int y, int maxWidth, Component message, Font font, boolean selected, OnValueChange onValueChange) {
/* 102 */     super(x, y, 0, 0, message);
/*     */     
/* 104 */     this.textWidget = new MultiLineTextWidget(message, font);
/* 105 */     this.textWidget.setMaxRows(2);
/* 106 */     this.width = adjustWidth(maxWidth, font);
/* 107 */     this.height = getAdjustedHeight(font);
/* 108 */     this.selected = selected;
/* 109 */     this.onValueChange = onValueChange;
/*     */   }
/*     */   
/*     */   public int adjustWidth(int maxWidth, Font font) {
/* 113 */     this.width = getAdjustedWidth(maxWidth, getMessage(), font);
/* 114 */     this.textWidget.setMaxWidth(this.width);
/* 115 */     return this.width;
/*     */   }
/*     */   
/*     */   private int getAdjustedWidth(int maxWidth, Component message, Font font) {
/* 119 */     return Math.min(getDefaultWidth(message, font), maxWidth);
/*     */   }
/*     */   
/*     */   private int getAdjustedHeight(Font font) {
/* 123 */     return Math.max(getBoxSize(font), this.textWidget.getHeight());
/*     */   }
/*     */   
/*     */   private static int getDefaultWidth(Component message, Font font) {
/* 127 */     return getBoxSize(font) + 4 + font.width((FormattedText)message);
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Font font) {
/* 131 */     return new Builder(message, font);
/*     */   }
/*     */   
/*     */   public static int getBoxSize(Font font) {
/* 135 */     Objects.requireNonNull(font); return 9 + 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/* 140 */     this.selected = !this.selected;
/* 141 */     this.onValueChange.onValueChange(this, this.selected);
/*     */   }
/*     */   
/*     */   public boolean selected() {
/* 145 */     return this.selected;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 150 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/* 151 */     if (this.active) {
/* 152 */       if (isFocused()) {
/* 153 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable(this.selected ? "narration.checkbox.usage.focused.uncheck" : "narration.checkbox.usage.focused.check"));
/*     */       } else {
/* 155 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable(this.selected ? "narration.checkbox.usage.hovered.uncheck" : "narration.checkbox.usage.hovered.check"));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*     */     Identifier sprite;
/* 162 */     Minecraft minecraft = Minecraft.getInstance();
/* 163 */     Font font = minecraft.font;
/*     */ 
/*     */     
/* 166 */     if (this.selected) {
/* 167 */       sprite = isFocused() ? CHECKBOX_SELECTED_HIGHLIGHTED_SPRITE : CHECKBOX_SELECTED_SPRITE;
/*     */     } else {
/* 169 */       sprite = isFocused() ? CHECKBOX_HIGHLIGHTED_SPRITE : CHECKBOX_SPRITE;
/*     */     } 
/* 171 */     int boxSize = getBoxSize(font);
/* 172 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), boxSize, boxSize, ARGB.white(this.alpha));
/*     */     
/* 174 */     int textX = getX() + boxSize + 4;
/* 175 */     int textY = getY() + boxSize / 2 - this.textWidget.getHeight() / 2;
/* 176 */     this.textWidget.setPosition(textX, textY);
/*     */     
/* 178 */     this.textWidget.visitLines(graphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.notClickable(isHovered())));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/Checkbox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */