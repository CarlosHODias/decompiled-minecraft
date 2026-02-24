/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DebugScreenEntryStatus implements StringRepresentable {
/*  6 */   ALWAYS_ON("alwaysOn"),
/*  7 */   IN_OVERLAY("inOverlay"),
/*  8 */   NEVER("never");
/*    */ 
/*    */   
/* 11 */   public static final StringRepresentable.EnumCodec<DebugScreenEntryStatus> CODEC = StringRepresentable.fromEnum(DebugScreenEntryStatus::values);
/*    */   private final String name;
/*    */   
/*    */   DebugScreenEntryStatus(String name) {
/* 15 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 20 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */