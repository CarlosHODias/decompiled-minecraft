/*    */ package net.minecraft.client.gui.screens.options.controls;
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.options.MouseSettingsScreen;
/*    */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ControlsScreen extends OptionsSubScreen {
/* 12 */   private static final Component TITLE = (Component)Component.translatable("controls.title");
/*    */   
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 15 */     return (OptionInstance<?>[])new OptionInstance[] {
/* 16 */         options.toggleCrouch(), 
/* 17 */         options.toggleSprint(), 
/* 18 */         options.toggleAttack(), 
/* 19 */         options.toggleUse(), 
/* 20 */         options.autoJump(), 
/* 21 */         options.sprintWindow(), 
/* 22 */         options.operatorItemsTab()
/*    */       };
/*    */   }
/*    */   
/*    */   public ControlsScreen(Screen lastScreen, Options options) {
/* 27 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 32 */     this.list.addSmall(
/* 33 */         (AbstractWidget)Button.builder((Component)Component.translatable("options.mouse_settings"), button -> this.minecraft.setScreen((Screen)new MouseSettingsScreen((Screen)this, this.options))).build(), 
/* 34 */         (AbstractWidget)Button.builder((Component)Component.translatable("controls.keybinds"), button -> this.minecraft.setScreen((Screen)new KeyBindsScreen((Screen)this, this.options))).build());
/*    */     
/* 36 */     this.list.addSmall((OptionInstance[])options(this.options));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/controls/ControlsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */