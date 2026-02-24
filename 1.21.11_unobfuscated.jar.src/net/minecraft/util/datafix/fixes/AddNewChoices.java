/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class AddNewChoices extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AddNewChoices(Schema outputSchema, String name, DSL.TypeReference type) {
/* 16 */     super(outputSchema, true);
/* 17 */     this.name = name;
/* 18 */     this.type = type;
/*    */   }
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     TaggedChoice.TaggedChoiceType<?> inputType = getInputSchema().findChoiceType(this.type);
/* 24 */     TaggedChoice.TaggedChoiceType<?> outputType = getOutputSchema().findChoiceType(this.type);
/* 25 */     return cap(inputType, outputType);
/*    */   }
/*    */ 
/*    */   
/*    */   private <K> TypeRewriteRule cap(TaggedChoice.TaggedChoiceType<K> inputType, TaggedChoice.TaggedChoiceType<?> outputType) {
/* 30 */     if (inputType.getKeyType() != outputType.getKeyType()) {
/* 31 */       throw new IllegalStateException("Could not inject: key type is not the same");
/*    */     }
/* 33 */     TaggedChoice.TaggedChoiceType<K> outputChoiceType = (TaggedChoice.TaggedChoiceType)outputType;
/* 34 */     return fixTypeEverywhere(this.name, (Type)inputType, (Type)outputChoiceType, ops -> ());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/AddNewChoices.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */