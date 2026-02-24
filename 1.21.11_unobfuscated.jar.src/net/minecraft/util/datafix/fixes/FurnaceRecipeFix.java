/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FurnaceRecipeFix
/*    */   extends DataFix
/*    */ {
/*    */   public FurnaceRecipeFix(Schema schema, boolean changesType) {
/* 29 */     super(schema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 34 */     return cap(getOutputSchema().getTypeRaw(References.RECIPE));
/*    */   }
/*    */   
/*    */   private <R> TypeRewriteRule cap(Type<R> recipeType) {
/* 38 */     Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> replacedType = DSL.and(
/* 39 */         DSL.optional((Type)DSL.field("RecipesUsed", DSL.and((Type)DSL.compoundList(recipeType, DSL.intType()), DSL.remainderType()))), 
/* 40 */         DSL.remainderType());
/*    */ 
/*    */     
/* 43 */     OpticFinder<?> oldFurnaceFinder = DSL.namedChoice("minecraft:furnace", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace"));
/* 44 */     OpticFinder<?> oldBlastFurnaceFinder = DSL.namedChoice("minecraft:blast_furnace", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace"));
/* 45 */     OpticFinder<?> oldSmokerFinder = DSL.namedChoice("minecraft:smoker", getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker"));
/*    */     
/* 47 */     Type<?> newFurnaceType = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace");
/* 48 */     Type<?> newBlastFurnaceFinder = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace");
/* 49 */     Type<?> newSmokerFinder = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker");
/*    */     
/* 51 */     Type<?> oldEntityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 52 */     Type<?> newEntityType = getOutputSchema().getType(References.BLOCK_ENTITY);
/* 53 */     return fixTypeEverywhereTyped("FurnaceRecipesFix", oldEntityType, newEntityType, input -> newSmokerFinder.updateTyped(oldFurnaceFinder, oldFurnaceFinder, ()).updateTyped(replacedType, oldBlastFurnaceFinder, ()).updateTyped(newBlastFurnaceFinder, oldSmokerFinder, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <R> Typed<?> updateFurnaceContents(Type<R> recipeType, Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> replacedType, Typed<?> input) {
/* 62 */     Dynamic<?> tag = (Dynamic)input.getOrCreate(DSL.remainderFinder());
/*    */     
/* 64 */     int recipesUsedSize = tag.get("RecipesUsedSize").asInt(0);
/* 65 */     tag = tag.remove("RecipesUsedSize");
/*    */     
/* 67 */     List<Pair<R, Integer>> results = Lists.newArrayList();
/* 68 */     for (int i = 0; i < recipesUsedSize; i++) {
/* 69 */       String locationKey = "RecipeLocation" + i;
/* 70 */       String amountKey = "RecipeAmount" + i;
/*    */       
/* 72 */       Optional<? extends Dynamic<?>> maybeLocation = tag.get(locationKey).result();
/* 73 */       int amount = tag.get(amountKey).asInt(0);
/* 74 */       if (amount > 0) {
/* 75 */         maybeLocation.ifPresent(location -> {
/*    */               Optional<? extends Pair<R, ? extends Dynamic<?>>> parseResult = recipeType.read(location).result();
/*    */               
/*    */               parseResult.ifPresent(());
/*    */             });
/*    */       }
/* 81 */       tag = tag.remove(locationKey).remove(amountKey);
/*    */     } 
/*    */     
/* 84 */     return input.set(DSL.remainderFinder(), replacedType, Pair.of(Either.left(Pair.of(results, tag.emptyMap())), tag));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/FurnaceRecipeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */