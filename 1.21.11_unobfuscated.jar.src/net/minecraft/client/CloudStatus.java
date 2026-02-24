/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum CloudStatus
/*    */   implements StringRepresentable {
/*  9 */   OFF("false", "options.off"),
/* 10 */   FAST("fast", "options.clouds.fast"),
/* 11 */   FANCY("true", "options.clouds.fancy");
/*    */ 
/*    */   
/* 14 */   public static final Codec<CloudStatus> CODEC = (Codec<CloudStatus>)StringRepresentable.fromEnum(CloudStatus::values);
/*    */   
/*    */   private final String legacyName;
/*    */   private final Component caption;
/*    */   
/*    */   CloudStatus(String legacyName, String key) {
/* 20 */     this.legacyName = legacyName;
/* 21 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 25 */     return this.caption;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 30 */     return this.legacyName;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/CloudStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */