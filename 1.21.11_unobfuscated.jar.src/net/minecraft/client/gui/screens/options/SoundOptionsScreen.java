/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public class SoundOptionsScreen extends OptionsSubScreen {
/* 12 */   private static final Component TITLE = (Component)Component.translatable("options.sounds.title");
/*    */   
/*    */   public SoundOptionsScreen(Screen lastScreen, Options options) {
/* 15 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 20 */     this.list.addBig(this.options.getSoundSourceOptionInstance(SoundSource.MASTER));
/* 21 */     this.list.addSmall((OptionInstance[])getAllSoundOptionsExceptMaster());
/* 22 */     this.list.addBig(this.options.soundDevice());
/* 23 */     this.list.addSmall(new OptionInstance[] { this.options.showSubtitles(), this.options.directionalAudio() });
/* 24 */     this.list.addSmall(new OptionInstance[] { this.options.musicFrequency(), this.options.musicToast() });
/*    */   }
/*    */   
/*    */   private OptionInstance<?>[] getAllSoundOptionsExceptMaster() {
/* 28 */     Objects.requireNonNull(this.options); return (OptionInstance<?>[])Arrays.<SoundSource>stream(SoundSource.values()).filter(s -> (s != SoundSource.MASTER)).map(this.options::getSoundSourceOptionInstance).toArray(x$0 -> new OptionInstance[x$0]);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/SoundOptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */