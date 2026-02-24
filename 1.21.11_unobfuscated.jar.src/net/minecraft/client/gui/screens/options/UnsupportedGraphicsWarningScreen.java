/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.TextAlignment;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineLabel;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ 
/*    */ public class UnsupportedGraphicsWarningScreen extends Screen {
/*    */   private static final int BUTTON_PADDING = 20;
/*    */   private static final int BUTTON_MARGIN = 5;
/*    */   private static final int BUTTON_HEIGHT = 20;
/*    */   private final Component narrationMessage;
/*    */   private final List<Component> message;
/*    */   private final ImmutableList<ButtonOption> buttonOptions;
/* 26 */   private MultiLineLabel messageLines = MultiLineLabel.EMPTY;
/*    */   private int contentTop;
/*    */   private int buttonWidth;
/*    */   
/*    */   protected UnsupportedGraphicsWarningScreen(Component title, List<Component> message, ImmutableList<ButtonOption> buttonOptions) {
/* 31 */     super(title);
/* 32 */     this.message = message;
/* 33 */     this.narrationMessage = (Component)CommonComponents.joinForNarration(new Component[] { title, ComponentUtils.formatList(message, CommonComponents.EMPTY) });
/* 34 */     this.buttonOptions = buttonOptions;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getNarrationMessage() {
/* 39 */     return this.narrationMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 44 */     for (UnmodifiableIterator<ButtonOption> unmodifiableIterator1 = this.buttonOptions.iterator(); unmodifiableIterator1.hasNext(); ) { ButtonOption buttonOption = unmodifiableIterator1.next();
/* 45 */       this.buttonWidth = Math.max(this.buttonWidth, 20 + this.font.width((FormattedText)buttonOption.message) + 20); }
/*    */     
/* 47 */     int buttonAdvance = 5 + this.buttonWidth + 5;
/*    */     
/* 49 */     int contentWidth = buttonAdvance * this.buttonOptions.size();
/* 50 */     this.messageLines = MultiLineLabel.create(this.font, contentWidth, this.message.<Component>toArray(new Component[0]));
/*    */     
/* 52 */     Objects.requireNonNull(this.font); int messageHeight = this.messageLines.getLineCount() * 9;
/* 53 */     this.contentTop = (int)(this.height / 2.0D - messageHeight / 2.0D);
/*    */     
/* 55 */     Objects.requireNonNull(this.font); int buttonTop = this.contentTop + messageHeight + 9 * 2;
/*    */     
/* 57 */     int x = (int)(this.width / 2.0D - contentWidth / 2.0D);
/* 58 */     for (UnmodifiableIterator<ButtonOption> unmodifiableIterator2 = this.buttonOptions.iterator(); unmodifiableIterator2.hasNext(); ) { ButtonOption buttonOption = unmodifiableIterator2.next();
/* 59 */       addRenderableWidget((GuiEventListener)Button.builder(buttonOption.message, buttonOption.onPress).bounds(x, buttonTop, this.buttonWidth, 20).build());
/* 60 */       x += buttonAdvance; }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 66 */     super.render(graphics, mouseX, mouseY, a);
/* 67 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/*    */     
/* 69 */     Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, this.title, this.width / 2, this.contentTop - 9 * 2, -1);
/* 70 */     Objects.requireNonNull(this.font); this.messageLines.visitLines(TextAlignment.CENTER, this.width / 2, this.contentTop, 9, textRenderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   public static final class ButtonOption {
/*    */     private final Component message;
/*    */     private final Button.OnPress onPress;
/*    */     
/*    */     public ButtonOption(Component message, Button.OnPress onPress) {
/* 83 */       this.message = message;
/* 84 */       this.onPress = onPress;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/UnsupportedGraphicsWarningScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */