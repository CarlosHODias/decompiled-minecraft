/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.Tooltip;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ 
/*    */ public class AccessibilityOptionsScreen
/*    */   extends OptionsSubScreen
/*    */ {
/* 22 */   public static final Component TITLE = (Component)Component.translatable("options.accessibility.title");
/*    */   
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 25 */     return (OptionInstance<?>[])new OptionInstance[] { 
/* 26 */         options.narrator(), options.showSubtitles(), 
/* 27 */         options.highContrast(), 
/* 28 */         options.menuBackgroundBlurriness(), options.textBackgroundOpacity(), 
/* 29 */         options.backgroundForChatOnly(), options.chatOpacity(), 
/* 30 */         options.chatLineSpacing(), options.chatDelay(), 
/* 31 */         options.notificationDisplayTime(), options.bobView(), 
/* 32 */         options.screenEffectScale(), options.fovEffectScale(), 
/* 33 */         options.darknessEffectScale(), options.damageTiltStrength(), 
/* 34 */         options.glintSpeed(), options.glintStrength(), 
/* 35 */         options.hideLightningFlash(), options.darkMojangStudiosBackground(), 
/* 36 */         options.panoramaSpeed(), options.hideSplashTexts(), 
/* 37 */         options.narratorHotkey(), options.rotateWithMinecart(), 
/* 38 */         options.highContrastBlockOutline() };
/*    */   }
/*    */ 
/*    */   
/*    */   public AccessibilityOptionsScreen(Screen lastScreen, Options options) {
/* 43 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 48 */     super.init();
/* 49 */     AbstractWidget highContrast = this.list.findOption(this.options.highContrast());
/* 50 */     if (highContrast != null && 
/* 51 */       !this.minecraft.getResourcePackRepository().getAvailableIds().contains("high_contrast")) {
/* 52 */       highContrast.active = false;
/* 53 */       highContrast.setTooltip(Tooltip.create((Component)Component.translatable("options.accessibility.high_contrast.error.tooltip")));
/*    */     } 
/*    */ 
/*    */     
/* 57 */     AbstractWidget rotateWithMinecart = this.list.findOption(this.options.rotateWithMinecart());
/* 58 */     if (rotateWithMinecart != null) {
/* 59 */       rotateWithMinecart.active = isMinecartOptionEnabled();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 65 */     OptionInstance[] arrayOfOptionInstance = (OptionInstance[])options(this.options);
/* 66 */     Button controlsLink = Button.builder(OptionsScreen.CONTROLS, button -> this.minecraft.setScreen((Screen)new ControlsScreen(this, this.options))).build();
/* 67 */     OptionInstance<?> firstOptionInstance = arrayOfOptionInstance[0];
/* 68 */     this.list.addSmall(firstOptionInstance.createButton(this.options), this.options.narrator(), (AbstractWidget)controlsLink);
/* 69 */     this.list.addSmall((OptionInstance[])Arrays.<OptionInstance>stream(arrayOfOptionInstance).filter(instance -> (instance != firstOptionInstance)).toArray(x$0 -> new OptionInstance[x$0]));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addFooter() {
/* 74 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 75 */     footer.addChild(
/* 76 */         (LayoutElement)Button.builder((Component)Component.translatable("options.accessibility.link"), ConfirmLinkScreen.confirmLink(this, CommonLinks.ACCESSIBILITY_HELP))
/* 77 */         .build());
/*    */     
/* 79 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.lastScreen)).build());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean panoramaShouldSpin() {
/* 84 */     return !(this.lastScreen instanceof net.minecraft.client.gui.screens.AccessibilityOnboardingScreen);
/*    */   }
/*    */   
/*    */   private boolean isMinecartOptionEnabled() {
/* 88 */     return (this.minecraft.level != null && this.minecraft.level.enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/AccessibilityOptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */