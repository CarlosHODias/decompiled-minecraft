/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ 
/*    */ public final class SavedDataType<T extends SavedData> extends Record {
/*    */   private final String id;
/*    */   private final java.util.function.Supplier<T> constructor;
/*    */   
/*  8 */   public SavedDataType(String id, java.util.function.Supplier<T> constructor, com.mojang.serialization.Codec<T> codec, net.minecraft.util.datafix.DataFixTypes dataFixType) { this.id = id; this.constructor = constructor; this.codec = codec; this.dataFixType = dataFixType; } private final com.mojang.serialization.Codec<T> codec; private final net.minecraft.util.datafix.DataFixTypes dataFixType; public String id() { return this.id; } public java.util.function.Supplier<T> constructor() { return this.constructor; } public com.mojang.serialization.Codec<T> codec() { return this.codec; } public net.minecraft.util.datafix.DataFixTypes dataFixType() { return this.dataFixType; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 16 */     if (obj instanceof SavedDataType) { SavedDataType<?> type = (SavedDataType)obj; if (this.id.equals(type.id)); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 21 */     return this.id.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return "SavedDataType[" + this.id + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/saveddata/SavedDataType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */