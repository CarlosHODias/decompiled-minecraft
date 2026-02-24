/*    */ package net.minecraft.client.gui.screens.options.controls;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.InputConstants;
/*    */ import net.minecraft.client.KeyMapping;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class KeyBindsScreen extends OptionsSubScreen {
/* 19 */   private static final Component TITLE = (Component)Component.translatable("controls.keybinds.title");
/*    */   
/*    */   public KeyMapping selectedKey;
/*    */   public long lastKeySelection;
/*    */   private KeyBindsList keyBindsList;
/*    */   private Button resetButton;
/*    */   
/*    */   public KeyBindsScreen(Screen lastScreen, Options options) {
/* 27 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addContents() {
/* 32 */     this.keyBindsList = (KeyBindsList)this.layout.addToContents((LayoutElement)new KeyBindsList(this, this.minecraft));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addOptions() {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addFooter() {
/* 42 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 47 */       .resetButton = Button.builder((Component)Component.translatable("controls.resetAll"), button -> { for (KeyMapping key : this.options.keyMappings) key.setKey(key.getDefaultKey());  this.keyBindsList.resetMappingAndUpdateButtons(); }).build();
/* 48 */     LinearLayout bottomButtons = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 49 */     bottomButtons.addChild((LayoutElement)this.resetButton);
/* 50 */     bottomButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).build());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 55 */     this.layout.arrangeElements();
/* 56 */     this.keyBindsList.updateSize(this.width, this.layout);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 61 */     if (this.selectedKey != null) {
/* 62 */       this.selectedKey.setKey(InputConstants.Type.MOUSE.getOrCreate(event.button()));
/* 63 */       this.selectedKey = null;
/* 64 */       this.keyBindsList.resetMappingAndUpdateButtons();
/* 65 */       return true;
/*    */     } 
/* 67 */     return super.mouseClicked(event, doubleClick);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 72 */     if (this.selectedKey != null) {
/* 73 */       if (event.isEscape()) {
/* 74 */         this.selectedKey.setKey(InputConstants.UNKNOWN);
/*    */       } else {
/* 76 */         this.selectedKey.setKey(InputConstants.getKey(event));
/*    */       } 
/*    */       
/* 79 */       this.selectedKey = null;
/* 80 */       this.lastKeySelection = Util.getMillis();
/* 81 */       this.keyBindsList.resetMappingAndUpdateButtons();
/* 82 */       return true;
/*    */     } 
/* 84 */     return super.keyPressed(event);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 89 */     super.render(graphics, mouseX, mouseY, a);
/*    */     
/*    */     boolean canReset = false;
/* 92 */     for (KeyMapping key : this.options.keyMappings) {
/* 93 */       if (!key.isDefault()) {
/* 94 */         canReset = true;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 99 */     this.resetButton.active = canReset;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/controls/KeyBindsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */