/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class PoiTypeRemoveFix
/*    */   extends AbstractPoiSectionFix {
/*    */   private final Predicate<String> typesToKeep;
/*    */   
/*    */   public PoiTypeRemoveFix(Schema outputSchema, String name, Predicate<String> typesToRemove) {
/* 13 */     super(outputSchema, name);
/* 14 */     this.typesToKeep = typesToRemove.negate();
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> records) {
/* 19 */     return records.filter(this::shouldKeepRecord);
/*    */   }
/*    */   
/*    */   private <T> boolean shouldKeepRecord(Dynamic<T> record) {
/* 23 */     return record.get("type").asString().result().filter(this.typesToKeep).isPresent();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/PoiTypeRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */