/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum InactivityFpsLimit implements StringRepresentable {
/*  8 */   MINIMIZED("minimized", "options.inactivityFpsLimit.minimized"),
/*  9 */   AFK("afk", "options.inactivityFpsLimit.afk");
/*    */ 
/*    */   
/* 12 */   public static final Codec<InactivityFpsLimit> CODEC = (Codec<InactivityFpsLimit>)StringRepresentable.fromEnum(InactivityFpsLimit::values);
/*    */   
/*    */   private final String serializedName;
/*    */   private final Component caption;
/*    */   
/*    */   InactivityFpsLimit(String serializedName, String key) {
/* 18 */     this.serializedName = serializedName;
/* 19 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 23 */     return this.caption;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return this.serializedName;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/InactivityFpsLimit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */