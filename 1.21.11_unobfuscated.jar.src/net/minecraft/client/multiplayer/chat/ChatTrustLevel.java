/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.GuiMessageTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FontDescription;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ChatTrustLevel
/*    */   implements StringRepresentable
/*    */ {
/* 16 */   SECURE("secure"),
/* 17 */   MODIFIED("modified"),
/* 18 */   NOT_SECURE("not_secure");
/*    */ 
/*    */   
/* 21 */   public static final Codec<ChatTrustLevel> CODEC = (Codec<ChatTrustLevel>)StringRepresentable.fromEnum(ChatTrustLevel::values);
/*    */   
/*    */   private final String serializedName;
/*    */   
/*    */   ChatTrustLevel(String serializedName) {
/* 26 */     this.serializedName = serializedName;
/*    */   }
/*    */   
/*    */   public static ChatTrustLevel evaluate(PlayerChatMessage message, Component decoratedMessage, Instant received) {
/* 30 */     if (!message.hasSignature() || message.hasExpiredClient(received)) {
/* 31 */       return NOT_SECURE;
/*    */     }
/*    */     
/* 34 */     if (isModified(message, decoratedMessage)) {
/* 35 */       return MODIFIED;
/*    */     }
/*    */     
/* 38 */     return SECURE;
/*    */   }
/*    */   
/*    */   private static boolean isModified(PlayerChatMessage message, Component decoratedMessage) {
/* 42 */     if (!decoratedMessage.getString().contains(message.signedContent())) {
/* 43 */       return true;
/*    */     }
/*    */     
/* 46 */     Component decoratedContent = message.unsignedContent();
/* 47 */     if (decoratedContent == null) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     return containsModifiedStyle(decoratedContent);
/*    */   }
/*    */   
/*    */   private static boolean containsModifiedStyle(Component decoratedContent) {
/* 55 */     return (Boolean)decoratedContent.visit((style, contents) -> isModifiedStyle(style) ? Optional.of(true) : Optional.empty(), Style.EMPTY)
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 60 */       .orElse(false);
/*    */   }
/*    */   
/*    */   private static boolean isModifiedStyle(Style style) {
/* 64 */     return !style.getFont().equals(FontDescription.DEFAULT);
/*    */   }
/*    */   
/*    */   public boolean isNotSecure() {
/* 68 */     return (this == NOT_SECURE);
/*    */   }
/*    */   
/*    */   public GuiMessageTag createTag(PlayerChatMessage message) {
/* 72 */     switch (ordinal()) { case 1: case 2: default: break; }  return 
/*    */ 
/*    */       
/* 75 */       null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 81 */     return this.serializedName;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/ChatTrustLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */