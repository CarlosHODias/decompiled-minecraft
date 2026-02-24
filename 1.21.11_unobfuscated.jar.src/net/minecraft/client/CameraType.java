/*    */ package net.minecraft.client;
/*    */ 
/*    */ public enum CameraType {
/*  4 */   FIRST_PERSON(true, false),
/*  5 */   THIRD_PERSON_BACK(false, false),
/*  6 */   THIRD_PERSON_FRONT(false, true);
/*    */ 
/*    */   
/*  9 */   private static final CameraType[] VALUES = values();
/*    */   
/*    */   private final boolean firstPerson;
/*    */   private final boolean mirrored;
/*    */   
/*    */   CameraType(boolean firstPerson, boolean mirrored) {
/* 15 */     this.firstPerson = firstPerson;
/* 16 */     this.mirrored = mirrored;
/*    */   }
/*    */   
/*    */   public boolean isFirstPerson() {
/* 20 */     return this.firstPerson;
/*    */   }
/*    */   
/*    */   public boolean isMirrored() {
/* 24 */     return this.mirrored;
/*    */   }
/*    */   
/*    */   public CameraType cycle() {
/* 28 */     return VALUES[(ordinal() + 1) % VALUES.length];
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/CameraType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */