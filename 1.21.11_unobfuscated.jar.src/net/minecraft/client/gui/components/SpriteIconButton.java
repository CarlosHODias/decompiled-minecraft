/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public abstract class SpriteIconButton
/*     */   extends Button {
/*     */   protected final WidgetSprites sprite;
/*     */   protected final int spriteWidth;
/*     */   protected final int spriteHeight;
/*     */   
/*     */   private SpriteIconButton(int width, int height, Component message, int spriteWidth, int spriteHeight, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration) {
/*  16 */     super(0, 0, width, height, message, onPress, (narration == null) ? DEFAULT_NARRATION : narration);
/*  17 */     if (tooltip != null) {
/*  18 */       setTooltip(Tooltip.create(tooltip));
/*     */     }
/*  20 */     this.spriteWidth = spriteWidth;
/*  21 */     this.spriteHeight = spriteHeight;
/*  22 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   protected void renderSprite(GuiGraphics graphics, int x, int y) {
/*  26 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite.get(isActive(), isHoveredOrFocused()), x, y, this.spriteWidth, this.spriteHeight, this.alpha);
/*     */   }
/*     */   
/*     */   public static Builder builder(Component message, Button.OnPress onPress, boolean iconOnly) {
/*  30 */     return new Builder(message, onPress, iconOnly);
/*     */   }
/*     */   
/*     */   public static class CenteredIcon extends SpriteIconButton {
/*     */     protected CenteredIcon(int width, int height, Component message, int spriteWidth, int spriteHeight, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration) {
/*  35 */       super(width, height, message, spriteWidth, spriteHeight, sprite, onPress, tooltip, narration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  40 */       renderDefaultSprite(graphics);
/*     */       
/*  42 */       int x = getX() + getWidth() / 2 - this.spriteWidth / 2;
/*  43 */       int y = getY() + getHeight() / 2 - this.spriteHeight / 2;
/*  44 */       renderSprite(graphics, x, y);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TextAndIcon extends SpriteIconButton {
/*     */     protected TextAndIcon(int width, int height, Component message, int spriteWidth, int spriteHeight, WidgetSprites sprite, Button.OnPress onPress, Component tooltip, Button.CreateNarration narration) {
/*  50 */       super(width, height, message, spriteWidth, spriteHeight, sprite, onPress, tooltip, narration);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  55 */       renderDefaultSprite(graphics);
/*     */       
/*  57 */       int left = getX() + 2;
/*  58 */       int right = getX() + getWidth() - this.spriteWidth - 4;
/*     */       
/*  60 */       int centerX = getX() + getWidth() / 2;
/*  61 */       ActiveTextCollector output = graphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE);
/*  62 */       output.acceptScrolling(getMessage(), centerX, left, right, getY(), getY() + getHeight());
/*     */       
/*  64 */       int x = getX() + getWidth() - this.spriteWidth - 2;
/*  65 */       int y = getY() + getHeight() / 2 - this.spriteHeight / 2;
/*  66 */       renderSprite(graphics, x, y);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final Component message;
/*     */     private final Button.OnPress onPress;
/*     */     private final boolean iconOnly;
/*  75 */     private int width = 150;
/*  76 */     private int height = 20;
/*     */     
/*     */     private WidgetSprites sprite;
/*     */     private int spriteWidth;
/*     */     private int spriteHeight;
/*     */     private Component tooltip;
/*     */     private Button.CreateNarration narration;
/*     */     
/*     */     public Builder(Component message, Button.OnPress onPress, boolean iconOnly) {
/*  85 */       this.message = message;
/*  86 */       this.onPress = onPress;
/*  87 */       this.iconOnly = iconOnly;
/*     */     }
/*     */     
/*     */     public Builder width(int width) {
/*  91 */       this.width = width;
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public Builder size(int width, int height) {
/*  96 */       this.width = width;
/*  97 */       this.height = height;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sprite(Identifier sprite, int spriteWidth, int spriteHeight) {
/* 102 */       this.sprite = new WidgetSprites(sprite);
/* 103 */       this.spriteWidth = spriteWidth;
/* 104 */       this.spriteHeight = spriteHeight;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sprite(WidgetSprites sprite, int spriteWidth, int spriteHeight) {
/* 109 */       this.sprite = sprite;
/* 110 */       this.spriteWidth = spriteWidth;
/* 111 */       this.spriteHeight = spriteHeight;
/* 112 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withTootip() {
/* 116 */       this.tooltip = this.message;
/* 117 */       return this;
/*     */     }
/*     */     
/*     */     public Builder narration(Button.CreateNarration narration) {
/* 121 */       this.narration = narration;
/* 122 */       return this;
/*     */     }
/*     */     
/*     */     public SpriteIconButton build() {
/* 126 */       if (this.sprite == null) {
/* 127 */         throw new IllegalStateException("Sprite not set");
/*     */       }
/* 129 */       if (this.iconOnly) {
/* 130 */         return new SpriteIconButton.CenteredIcon(this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.sprite, this.onPress, this.tooltip, this.narration);
/*     */       }
/* 132 */       return new SpriteIconButton.TextAndIcon(this.width, this.height, this.message, this.spriteWidth, this.spriteHeight, this.sprite, this.onPress, this.tooltip, this.narration);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/SpriteIconButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */