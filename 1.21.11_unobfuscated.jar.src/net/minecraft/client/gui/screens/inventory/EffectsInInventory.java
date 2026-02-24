/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ import com.google.common.collect.Ordering;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffectUtil;
/*     */ 
/*     */ public class EffectsInInventory {
/*  24 */   private static final Identifier EFFECT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/inventory/effect_background");
/*  25 */   private static final Identifier EFFECT_BACKGROUND_AMBIENT_SPRITE = Identifier.withDefaultNamespace("container/inventory/effect_background_ambient");
/*     */   private static final int ICON_SIZE = 18;
/*     */   public static final int SPACING = 7;
/*     */   private static final int TEXT_X_OFFSET = 32;
/*     */   public static final int SPRITE_SQUARE_SIZE = 32;
/*     */   private final AbstractContainerScreen<?> screen;
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   public EffectsInInventory(AbstractContainerScreen<?> screen) {
/*  34 */     this.screen = screen;
/*  35 */     this.minecraft = Minecraft.getInstance();
/*     */   }
/*     */   
/*     */   public boolean canSeeEffects() {
/*  39 */     int xo = this.screen.leftPos + this.screen.imageWidth + 2;
/*  40 */     int availableWidth = this.screen.width - xo;
/*     */     
/*  42 */     return (availableWidth >= 32);
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY) {
/*  46 */     int xo = this.screen.leftPos + this.screen.imageWidth + 2;
/*  47 */     int availableWidth = this.screen.width - xo;
/*     */     
/*  49 */     Collection<MobEffectInstance> activeEffects = this.minecraft.player.getActiveEffects();
/*     */     
/*  51 */     if (activeEffects.isEmpty() || availableWidth < 32) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     int maxWidth = (availableWidth >= 120) ? (availableWidth - 7) : 32;
/*  56 */     int yStep = 33;
/*  57 */     if (activeEffects.size() > 5) {
/*  58 */       yStep = 132 / (activeEffects.size() - 1);
/*     */     }
/*     */     
/*  61 */     renderEffects(graphics, activeEffects, xo, yStep, mouseX, mouseY, maxWidth);
/*     */   }
/*     */   
/*     */   private void renderEffects(GuiGraphics graphics, Collection<MobEffectInstance> activeEffects, int x0, int yStep, int mouseX, int mouseY, int maxWidth) {
/*  65 */     Iterable<MobEffectInstance> sortedEffects = Ordering.natural().sortedCopy(activeEffects);
/*  66 */     int y0 = this.screen.topPos;
/*  67 */     Font font = this.screen.getFont();
/*  68 */     for (MobEffectInstance effect : sortedEffects) {
/*  69 */       boolean isAmbient = effect.isAmbient();
/*  70 */       Component effectText = getEffectName(effect);
/*  71 */       Component duration = MobEffectUtil.formatDuration(effect, 1.0F, this.minecraft.level.tickRateManager().tickrate());
/*  72 */       int textureWidth = renderBackground(graphics, font, effectText, duration, x0, y0, isAmbient, maxWidth);
/*  73 */       renderText(graphics, effectText, duration, font, x0, y0, textureWidth, yStep, mouseX, mouseY);
/*  74 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Gui.getMobEffectSprite(effect.getEffect()), x0 + 7, y0 + 7, 18, 18);
/*  75 */       y0 += yStep;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int renderBackground(GuiGraphics graphics, Font font, Component effectName, Component duration, int x0, int y0, boolean isAmbient, int maxTextureWidth) {
/*  80 */     int nameWidth = 32 + font.width((FormattedText)effectName) + 7;
/*  81 */     int durationWidth = 32 + font.width((FormattedText)duration) + 7;
/*  82 */     int textureWidth = Math.min(maxTextureWidth, Math.max(nameWidth, durationWidth));
/*  83 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, isAmbient ? EFFECT_BACKGROUND_AMBIENT_SPRITE : EFFECT_BACKGROUND_SPRITE, x0, y0, textureWidth, 32);
/*  84 */     return textureWidth;
/*     */   }
/*     */   private void renderText(GuiGraphics graphics, Component effectText, Component duration, Font font, int x0, int y0, int textureWidth, int yStep, int mouseX, int mouseY) {
/*     */     boolean isCompact;
/*  88 */     int textX = x0 + 32;
/*  89 */     int textY = y0 + 7;
/*  90 */     int maxTextWidth = textureWidth - 32 - 7;
/*     */     
/*  92 */     if (maxTextWidth > 0) {
/*  93 */       boolean shouldClip = (font.width((FormattedText)effectText) > maxTextWidth);
/*  94 */       FormattedCharSequence clippedText = shouldClip ? StringWidget.clipText(effectText, font, maxTextWidth) : effectText.getVisualOrderText();
/*  95 */       graphics.drawString(font, clippedText, textX, textY, -1);
/*  96 */       Objects.requireNonNull(font); graphics.drawString(font, duration, textX, textY + 9, -8355712);
/*  97 */       isCompact = shouldClip;
/*     */     } else {
/*  99 */       isCompact = true;
/*     */     } 
/* 101 */     if (isCompact && mouseX >= x0 && mouseX <= x0 + textureWidth && mouseY >= y0 && mouseY <= y0 + yStep) {
/* 102 */       graphics.setTooltipForNextFrame(this.screen.getFont(), List.of(effectText, duration), Optional.empty(), mouseX, mouseY);
/*     */     }
/*     */   }
/*     */   
/*     */   private Component getEffectName(MobEffectInstance effect) {
/* 107 */     MutableComponent name = ((MobEffect)effect.getEffect().value()).getDisplayName().copy();
/* 108 */     if (effect.getAmplifier() >= 1 && effect.getAmplifier() <= 9) {
/* 109 */       name.append(CommonComponents.SPACE).append((Component)Component.translatable("enchantment.level." + effect.getAmplifier() + 1));
/*     */     }
/* 111 */     return (Component)name;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/EffectsInInventory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */