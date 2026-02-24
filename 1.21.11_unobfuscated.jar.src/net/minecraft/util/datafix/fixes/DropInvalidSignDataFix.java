/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class DropInvalidSignDataFix extends DataFix {
/*    */   private final String entityName;
/*    */   
/*    */   public DropInvalidSignDataFix(Schema outputSchema, String entityName) {
/* 22 */     super(outputSchema, false);
/* 23 */     this.entityName = entityName;
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fix(Dynamic<T> tag) {
/* 27 */     tag = tag.update("front_text", DropInvalidSignDataFix::fixText);
/* 28 */     tag = tag.update("back_text", DropInvalidSignDataFix::fixText);
/*    */     
/* 30 */     for (String field : BlockEntitySignDoubleSidedEditableTextFix.FIELDS_TO_DROP) {
/* 31 */       tag = tag.remove(field);
/*    */     }
/* 33 */     return tag;
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> fixText(Dynamic<T> tag) {
/* 38 */     Optional<Stream<Dynamic<T>>> filteredLines = tag.get("filtered_messages").asStreamOpt().result();
/* 39 */     if (filteredLines.isEmpty()) {
/* 40 */       return tag;
/*    */     }
/*    */     
/* 43 */     Dynamic<T> emptyComponent = LegacyComponentDataFixUtils.createEmptyComponent(tag.getOps());
/* 44 */     List<Dynamic<T>> lines = ((Stream<Dynamic<T>>)tag.get("messages").asStreamOpt().result().orElse(Stream.of((Dynamic<T>[])new Dynamic[0]))).toList();
/* 45 */     List<Dynamic<T>> newFilteredLines = Streams.mapWithIndex(filteredLines.get(), (line, index) -> {
/*    */           Dynamic<T> fallbackLine = (index < lines.size()) ? lines.get((int)index) : emptyComponent;
/*    */           return line.equals(emptyComponent) ? fallbackLine : line;
/* 48 */         }).toList();
/* 49 */     if (newFilteredLines.equals(lines)) {
/* 50 */       return tag.remove("filtered_messages");
/*    */     }
/* 52 */     return tag.set("filtered_messages", tag.createList(newFilteredLines.stream()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 58 */     Type<?> entityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 59 */     Type<?> entityChoiceType = getInputSchema().getChoiceType(References.BLOCK_ENTITY, this.entityName);
/* 60 */     OpticFinder<?> entityF = DSL.namedChoice(this.entityName, entityChoiceType);
/*    */     
/* 62 */     return fixTypeEverywhereTyped("DropInvalidSignDataFix for " + this.entityName, entityType, input -> entityChoiceType.updateTyped(entityF, entityF, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/DropInvalidSignDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */