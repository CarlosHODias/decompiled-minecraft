/*    */ package net.minecraft.client.gui.screens;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.TextAlignment;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineLabel;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class DemoIntroScreen extends Screen {
/* 17 */   private static final Identifier DEMO_BACKGROUND_LOCATION = Identifier.withDefaultNamespace("textures/gui/demo_background.png");
/*    */   
/*    */   private static final int BACKGROUND_TEXTURE_WIDTH = 256;
/*    */   private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
/*    */   
/*    */   public DemoIntroScreen() {
/* 23 */     super((Component)Component.translatable("demo.help.title"));
/*    */ 
/*    */     
/* 26 */     this.movementMessage = MultiLineLabel.EMPTY;
/* 27 */     this.durationMessage = MultiLineLabel.EMPTY;
/*    */   }
/*    */   private static final int TEXT_COLOR = -14737633; private MultiLineLabel movementMessage; private MultiLineLabel durationMessage;
/*    */   protected void init() {
/* 31 */     int yo = -16;
/*    */     
/* 33 */     addRenderableWidget(Button.builder((Component)Component.translatable("demo.help.buy"), button -> {
/*    */             button.active = false;
/*    */             Util.getPlatform().openUri(CommonLinks.BUY_MINECRAFT_JAVA);
/* 36 */           }).bounds(this.width / 2 - 116, this.height / 2 + 62 + -16, 114, 20).build());
/* 37 */     addRenderableWidget(Button.builder((Component)Component.translatable("demo.help.later"), button -> {
/*    */             this.minecraft.setScreen(null);
/*    */             this.minecraft.mouseHandler.grabMouse();
/* 40 */           }).bounds(this.width / 2 + 2, this.height / 2 + 62 + -16, 114, 20).build());
/*    */     
/* 42 */     Options options = this.minecraft.options;
/* 43 */     this.movementMessage = MultiLineLabel.create(this.font, new Component[] {
/* 44 */           movementMessage(Component.translatable("demo.help.movementShort", new Object[] { options.keyUp.getTranslatedKeyMessage(), options.keyLeft.getTranslatedKeyMessage(), options.keyDown.getTranslatedKeyMessage(), options.keyRight.getTranslatedKeyMessage()
/* 45 */               })), movementMessage(Component.translatable("demo.help.movementMouse")), 
/* 46 */           movementMessage(Component.translatable("demo.help.jump", new Object[] { options.keyJump.getTranslatedKeyMessage()
/* 47 */               })), movementMessage(Component.translatable("demo.help.inventory", new Object[] { options.keyInventory.getTranslatedKeyMessage() }))
/*    */         });
/*    */     
/* 50 */     this.durationMessage = MultiLineLabel.create(this.font, (Component)Component.translatable("demo.help.fullWrapped").withoutShadow().withColor(-14737633), 218);
/*    */   }
/*    */   
/*    */   private Component movementMessage(MutableComponent line) {
/* 54 */     return (Component)line.withoutShadow().withColor(-11579569);
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 59 */     super.renderBackground(graphics, mouseX, mouseY, a);
/*    */     
/* 61 */     int xo = (this.width - 248) / 2;
/* 62 */     int yo = (this.height - 166) / 2;
/* 63 */     graphics.blit(RenderPipelines.GUI_TEXTURED, DEMO_BACKGROUND_LOCATION, xo, yo, 0.0F, 0.0F, 248, 166, 256, 256);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 68 */     super.render(graphics, mouseX, mouseY, a);
/*    */     
/* 70 */     int x = (this.width - 248) / 2 + 10;
/*    */     
/* 72 */     int y = (this.height - 166) / 2 + 8;
/*    */     
/* 74 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/* 75 */     graphics.drawString(this.font, this.title, x, y, -14737633, false);
/*    */     
/* 77 */     y = this.movementMessage.visitLines(TextAlignment.LEFT, x, y + 12, 12, textRenderer);
/* 78 */     Objects.requireNonNull(this.font); this.durationMessage.visitLines(TextAlignment.LEFT, x, y + 20, 9, textRenderer);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/DemoIntroScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */