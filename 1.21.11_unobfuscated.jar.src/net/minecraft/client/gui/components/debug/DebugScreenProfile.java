/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DebugScreenProfile implements StringRepresentable {
/*  6 */   DEFAULT("default", "debug.options.profile.default"),
/*  7 */   PERFORMANCE("performance", "debug.options.profile.performance");
/*    */ 
/*    */   
/* 10 */   public static final StringRepresentable.EnumCodec<DebugScreenProfile> CODEC = StringRepresentable.fromEnum(DebugScreenProfile::values);
/*    */   private final String name;
/*    */   private final String translationKey;
/*    */   
/*    */   DebugScreenProfile(String name, String translationKey) {
/* 15 */     this.name = name;
/* 16 */     this.translationKey = translationKey;
/*    */   }
/*    */   
/*    */   public String translationKey() {
/* 20 */     return this.translationKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 25 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugScreenProfile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */