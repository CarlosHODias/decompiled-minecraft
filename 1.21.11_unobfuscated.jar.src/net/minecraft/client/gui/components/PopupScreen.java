/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class PopupScreen extends Screen {
/*  19 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("popup/background");
/*     */   
/*     */   private static final int SPACING = 12;
/*     */   
/*     */   private static final int BG_BORDER_WITH_SPACING = 18;
/*     */   private static final int BUTTON_SPACING = 6;
/*     */   private static final int IMAGE_SIZE_X = 130;
/*     */   private static final int IMAGE_SIZE_Y = 64;
/*     */   private static final int POPUP_DEFAULT_WIDTH = 250;
/*     */   private final Screen backgroundScreen;
/*     */   private final Identifier image;
/*     */   private final Component message;
/*     */   private final List<ButtonOption> buttons;
/*     */   private final Runnable onClose;
/*     */   private final int contentWidth;
/*  34 */   private final LinearLayout layout = LinearLayout.vertical();
/*     */   
/*     */   private PopupScreen(Screen backgroundScreen, int backgroundWidth, Identifier image, Component title, Component message, List<ButtonOption> buttons, Runnable onClose) {
/*  37 */     super(title);
/*  38 */     this.backgroundScreen = backgroundScreen;
/*  39 */     this.image = image;
/*  40 */     this.message = message;
/*  41 */     this.buttons = buttons;
/*  42 */     this.onClose = onClose;
/*  43 */     this.contentWidth = backgroundWidth - 36;
/*     */   }
/*     */ 
/*     */   
/*     */   public void added() {
/*  48 */     super.added();
/*  49 */     this.backgroundScreen.clearFocus();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  54 */     this.backgroundScreen.init(this.width, this.height);
/*  55 */     this.layout.spacing(12).defaultCellSetting().alignHorizontallyCenter();
/*  56 */     this.layout.addChild(new MultiLineTextWidget((Component)this.title.copy().withStyle(ChatFormatting.BOLD), this.font).setMaxWidth(this.contentWidth).setCentered(true));
/*  57 */     if (this.image != null) {
/*  58 */       this.layout.addChild(ImageWidget.texture(130, 64, this.image, 130, 64));
/*     */     }
/*  60 */     this.layout.addChild(new MultiLineTextWidget(this.message, this.font).setMaxWidth(this.contentWidth).setCentered(true));
/*  61 */     this.layout.addChild((LayoutElement)buildButtonRow());
/*     */     
/*  63 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  64 */     repositionElements();
/*     */   }
/*     */   
/*     */   private LinearLayout buildButtonRow() {
/*  68 */     int totalSpacing = 6 * (this.buttons.size() - 1);
/*  69 */     int buttonWidth = Math.min((this.contentWidth - totalSpacing) / this.buttons.size(), 150);
/*  70 */     LinearLayout row = LinearLayout.horizontal();
/*  71 */     row.spacing(6);
/*  72 */     for (ButtonOption button : this.buttons) {
/*  73 */       row.addChild(Button.builder(button.message(), b -> button.action().accept(this)).width(buttonWidth).build());
/*     */     }
/*  75 */     return row;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  80 */     this.backgroundScreen.resize(this.width, this.height);
/*  81 */     this.layout.arrangeElements();
/*  82 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  87 */     this.backgroundScreen.renderBackground(graphics, mouseX, mouseY, a);
/*  88 */     graphics.nextStratum();
/*  89 */     this.backgroundScreen.render(graphics, -1, -1, a);
/*  90 */     graphics.nextStratum();
/*  91 */     renderTransparentBackground(graphics);
/*  92 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, this.layout.getX() - 18, this.layout.getY() - 18, this.layout.getWidth() + 36, this.layout.getHeight() + 36);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/*  97 */     return (Component)CommonComponents.joinForNarration(new Component[] { this.title, this.message });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 102 */     if (this.onClose != null) {
/* 103 */       this.onClose.run();
/*     */     }
/* 105 */     this.minecraft.setScreen(this.backgroundScreen);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Screen backgroundScreen;
/*     */     private final Component title;
/* 111 */     private Component message = CommonComponents.EMPTY;
/* 112 */     private int width = 250;
/*     */     private Identifier image;
/* 114 */     private final List<PopupScreen.ButtonOption> buttons = new ArrayList<>();
/* 115 */     private Runnable onClose = null;
/*     */     
/*     */     public Builder(Screen backgroundScreen, Component title) {
/* 118 */       this.backgroundScreen = backgroundScreen;
/* 119 */       this.title = title;
/*     */     }
/*     */     
/*     */     public Builder setWidth(int width) {
/* 123 */       this.width = width;
/* 124 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setImage(Identifier image) {
/* 128 */       this.image = image;
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMessage(Component message) {
/* 133 */       this.message = message;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addButton(Component message, Consumer<PopupScreen> action) {
/* 138 */       this.buttons.add(new PopupScreen.ButtonOption(message, action));
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     public Builder onClose(Runnable onClose) {
/* 143 */       this.onClose = onClose;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public PopupScreen build() {
/* 148 */       if (this.buttons.isEmpty()) {
/* 149 */         throw new IllegalStateException("Popup must have at least one button");
/*     */       }
/* 151 */       return new PopupScreen(this.backgroundScreen, this.width, this.image, this.title, this.message, List.copyOf(this.buttons), this.onClose);
/*     */     } }
/*     */   private static final class ButtonOption extends Record { private final Component message; private final Consumer<PopupScreen> action;
/*     */     
/* 155 */     private ButtonOption(Component message, Consumer<PopupScreen> action) { this.message = message; this.action = action; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #155	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 155 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption; } public Component message() { return this.message; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #155	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #155	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;
/* 155 */       //   0	8	1	o	Ljava/lang/Object; } public Consumer<PopupScreen> action() { return this.action; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/PopupScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */