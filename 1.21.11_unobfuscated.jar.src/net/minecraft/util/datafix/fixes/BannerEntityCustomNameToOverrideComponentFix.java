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
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class BannerEntityCustomNameToOverrideComponentFix
/*    */   extends DataFix {
/*    */   public BannerEntityCustomNameToOverrideComponentFix(Schema outputSchema) {
/* 21 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 26 */     Type<?> blockEntityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 27 */     TaggedChoice.TaggedChoiceType<?> blockEntityIdFinder = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 28 */     OpticFinder<?> customNameFinder = blockEntityType.findField("CustomName");
/*    */     
/* 30 */     OpticFinder<Pair<String, String>> textComponentFinder = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/*    */     
/* 32 */     return fixTypeEverywhereTyped("Banner entity custom_name to item_name component fix", blockEntityType, input -> {
/*    */           Object blockEntityId = ((Pair)blockEntityIdFinder.get(blockEntityIdFinder.finder())).getFirst();
/*    */           return blockEntityId.equals("minecraft:banner") ? fix(blockEntityIdFinder, blockEntityIdFinder, textComponentFinder) : blockEntityIdFinder;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> fix(Typed<?> input, OpticFinder<Pair<String, String>> textComponentFinder, OpticFinder<?> customNameFinder) {
/* 43 */     Optional<String> customName = input.getOptionalTyped(customNameFinder)
/* 44 */       .flatMap(name -> name.getOptional(textComponentFinder).map(Pair::getSecond));
/*    */ 
/*    */     
/* 47 */     boolean isOminousBanner = customName.flatMap(LegacyComponentDataFixUtils::extractTranslationString)
/* 48 */       .filter(e -> e.equals("block.minecraft.ominous_banner"))
/* 49 */       .isPresent();
/*    */     
/* 51 */     if (isOminousBanner) {
/* 52 */       return Util.writeAndReadTypedOrThrow(input, input.getType(), dynamic -> {
/*    */             Dynamic<?> components = dynamic.createMap(Map.of(dynamic.createString("minecraft:item_name"), dynamic.createString(customName.get()), dynamic.createString("minecraft:hide_additional_tooltip"), dynamic.emptyMap()));
/*    */ 
/*    */             
/*    */             return dynamic.set("components", components).remove("CustomName");
/*    */           });
/*    */     }
/*    */     
/* 60 */     return input;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BannerEntityCustomNameToOverrideComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */