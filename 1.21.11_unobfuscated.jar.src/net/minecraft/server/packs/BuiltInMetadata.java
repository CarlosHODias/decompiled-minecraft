/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ 
/*    */ public class BuiltInMetadata
/*    */ {
/*  8 */   private static final BuiltInMetadata EMPTY = new BuiltInMetadata(Map.of());
/*    */   
/*    */   private final Map<MetadataSectionType<?>, ?> values;
/*    */   
/*    */   private BuiltInMetadata(Map<MetadataSectionType<?>, ?> values) {
/* 13 */     this.values = values;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T get(MetadataSectionType<T> section) {
/* 18 */     return (T)this.values.get(section);
/*    */   }
/*    */   
/*    */   public static BuiltInMetadata of() {
/* 22 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public static <T> BuiltInMetadata of(MetadataSectionType<T> k, T v) {
/* 26 */     return new BuiltInMetadata(Map.of(k, v));
/*    */   }
/*    */   
/*    */   public static <T1, T2> BuiltInMetadata of(MetadataSectionType<T1> k1, T1 v1, MetadataSectionType<T2> k2, T2 v2) {
/* 30 */     return new BuiltInMetadata(Map.of(k1, v1, k2, v2));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/BuiltInMetadata.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */