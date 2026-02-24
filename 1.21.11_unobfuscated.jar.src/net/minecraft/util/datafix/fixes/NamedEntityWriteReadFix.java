/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ 
/*    */ public abstract class NamedEntityWriteReadFix
/*    */   extends DataFix
/*    */ {
/*    */   private final String name;
/*    */   private final String entityName;
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public NamedEntityWriteReadFix(Schema outputSchema, boolean changesType, String name, DSL.TypeReference type, String entityName) {
/* 23 */     super(outputSchema, changesType);
/* 24 */     this.name = name;
/* 25 */     this.type = type;
/* 26 */     this.entityName = entityName;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 31 */     Type<?> inputEntityType = getInputSchema().getType(this.type);
/* 32 */     Type<?> inputEntityChoiceType = getInputSchema().getChoiceType(this.type, this.entityName);
/*    */     
/* 34 */     Type<?> outputEntityType = getOutputSchema().getType(this.type);
/*    */     
/* 36 */     OpticFinder<?> entityF = DSL.namedChoice(this.entityName, inputEntityChoiceType);
/*    */ 
/*    */ 
/*    */     
/* 40 */     Type<?> patchedEntityType = ExtraDataFixUtils.patchSubType(inputEntityType, inputEntityType, outputEntityType);
/*    */     
/* 42 */     return fix(inputEntityType, outputEntityType, patchedEntityType, entityF);
/*    */   }
/*    */   
/*    */   private <S, T, A> TypeRewriteRule fix(Type<S> inputEntityType, Type<T> outputEntityType, Type<?> patchedEntityType, OpticFinder<A> choiceFinder) {
/* 46 */     return fixTypeEverywhereTyped(this.name, inputEntityType, outputEntityType, typed -> {
/*    */           if (choiceFinder.getOptional(choiceFinder).isEmpty())
/*    */             return ExtraDataFixUtils.cast(choiceFinder, choiceFinder); 
/*    */           Typed<?> fakeTyped = ExtraDataFixUtils.cast(outputEntityType, choiceFinder);
/*    */           return Util.writeAndReadTypedOrThrow(fakeTyped, choiceFinder, this::fix);
/*    */         });
/*    */   }
/*    */   
/*    */   protected abstract <T> Dynamic<T> fix(Dynamic<T> paramDynamic);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/NamedEntityWriteReadFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */