/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.Optionull;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public class OnlineOptionsScreen
/*    */   extends OptionsSubScreen {
/* 19 */   private static final Component TITLE = (Component)Component.translatable("options.online.title");
/*    */   private OptionInstance<Unit> difficultyDisplay;
/*    */   
/*    */   public OnlineOptionsScreen(Screen lastScreen, Options options) {
/* 23 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 28 */     super.init();
/*    */     
/* 30 */     if (this.difficultyDisplay != null) {
/* 31 */       AbstractWidget difficultyButton = this.list.findOption(this.difficultyDisplay);
/* 32 */       if (difficultyButton != null) {
/* 33 */         difficultyButton.active = false;
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private OptionInstance<?>[] options(Options options, Minecraft minecraft) {
/* 39 */     List<OptionInstance<?>> optionList = new ArrayList<>();
/*    */     
/* 41 */     optionList.add(options.realmsNotifications());
/* 42 */     optionList.add(options.allowServerListing());
/*    */     
/* 44 */     OptionInstance<Unit> difficultyDisplay = (OptionInstance<Unit>)Optionull.map(minecraft.level, level -> {
/*    */           Difficulty difficulty = level.getDifficulty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           return new OptionInstance("options.difficulty.online", OptionInstance.noTooltip(), (), (OptionInstance.ValueSet)new OptionInstance.Enum(List.of(Unit.INSTANCE), Codec.EMPTY.codec()), Unit.INSTANCE, ());
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     if (difficultyDisplay != null) {
/* 58 */       this.difficultyDisplay = difficultyDisplay;
/* 59 */       optionList.add(difficultyDisplay);
/*    */     } 
/*    */     
/* 62 */     return (OptionInstance<?>[])optionList.<OptionInstance>toArray(new OptionInstance[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 67 */     this.list.addSmall((OptionInstance[])options(this.options, this.minecraft));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/OnlineOptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */