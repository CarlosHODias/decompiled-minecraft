/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class StatsRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public StatsRenameFix(Schema outputSchema, String name, Map<String, String> renames) {
/* 20 */     super(outputSchema, false);
/* 21 */     this.name = name;
/* 22 */     this.renames = renames;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     return TypeRewriteRule.seq(createStatRule(), createCriteriaRule());
/*    */   }
/*    */   
/*    */   private TypeRewriteRule createCriteriaRule() {
/* 31 */     Type<?> outputType = getOutputSchema().getType(References.OBJECTIVE);
/* 32 */     Type<?> inputType = getInputSchema().getType(References.OBJECTIVE);
/*    */     
/* 34 */     OpticFinder<?> criteriaTypeFinder = inputType.findField("CriteriaType");
/* 35 */     TaggedChoice.TaggedChoiceType<?> choiceType = (TaggedChoice.TaggedChoiceType)criteriaTypeFinder.type().findChoiceType("type", -1).orElseThrow(() -> new IllegalStateException("Can't find choice type for criteria"));
/* 36 */     Type<?> customFieldType = (Type)choiceType.types().get("minecraft:custom");
/* 37 */     if (customFieldType == null) {
/* 38 */       throw new IllegalStateException("Failed to find custom criterion type variant");
/*    */     }
/*    */     
/* 41 */     OpticFinder<?> customTypeFinder = DSL.namedChoice("minecraft:custom", customFieldType);
/* 42 */     OpticFinder<String> idFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 44 */     return fixTypeEverywhereTyped(this.name, inputType, outputType, input -> idFinder.updateTyped(criteriaTypeFinder, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private TypeRewriteRule createStatRule() {
/* 54 */     Type<?> outputType = getOutputSchema().getType(References.STATS);
/* 55 */     Type<?> inputType = getInputSchema().getType(References.STATS);
/* 56 */     OpticFinder<?> statsFinder = inputType.findField("stats");
/* 57 */     OpticFinder<?> customFinder = statsFinder.type().findField("minecraft:custom");
/* 58 */     OpticFinder<String> nameFinder = NamespacedSchema.namespacedString().finder();
/* 59 */     return fixTypeEverywhereTyped(this.name, inputType, outputType, input -> nameFinder.updateTyped(statsFinder, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/StatsRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */