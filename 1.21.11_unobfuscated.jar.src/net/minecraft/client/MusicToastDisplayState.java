/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MusicToastDisplayState implements StringRepresentable {
/*  8 */   NEVER("never", "options.musicToast.never"),
/*  9 */   PAUSE("pause", "options.musicToast.pauseMenu"),
/* 10 */   PAUSE_AND_TOAST("pause_and_toast", "options.musicToast.pauseMenuAndToast");
/*    */ 
/*    */   
/* 13 */   public static final Codec<MusicToastDisplayState> CODEC = (Codec<MusicToastDisplayState>)StringRepresentable.fromEnum(MusicToastDisplayState::values);
/*    */   
/*    */   private final String name;
/*    */   private final Component text;
/*    */   private final Component tooltip;
/*    */   
/*    */   MusicToastDisplayState(String name, String translationKey) {
/* 20 */     this.name = name;
/* 21 */     this.text = (Component)Component.translatable(translationKey);
/* 22 */     this.tooltip = (Component)Component.translatable(translationKey + ".tooltip");
/*    */   }
/*    */   
/*    */   public Component text() {
/* 26 */     return this.text;
/*    */   }
/*    */   
/*    */   public Component tooltip() {
/* 30 */     return this.tooltip;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 35 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean renderInPauseScreen() {
/* 39 */     return (this != NEVER);
/*    */   }
/*    */   
/*    */   public boolean renderToast() {
/* 43 */     return (this == PAUSE_AND_TOAST);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/MusicToastDisplayState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */