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
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BoatSplitFix
/*    */   extends DataFix {
/*    */   public BoatSplitFix(Schema outputSchema) {
/* 18 */     super(outputSchema, true);
/*    */   }
/*    */   
/*    */   private static boolean isNormalBoat(String id) {
/* 22 */     return id.equals("minecraft:boat");
/*    */   }
/*    */   
/*    */   private static boolean isChestBoat(String id) {
/* 26 */     return id.equals("minecraft:chest_boat");
/*    */   }
/*    */   
/*    */   private static boolean isAnyBoat(String id) {
/* 30 */     return (isNormalBoat(id) || isChestBoat(id));
/*    */   }
/*    */   
/*    */   private static String mapVariantToNormalBoat(String id) {
/* 34 */     switch (id) { default: case "spruce": case "birch": case "jungle": case "acacia": case "cherry": case "dark_oak": case "mangrove": case "bamboo": break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 43 */       "minecraft:bamboo_raft";
/*    */   }
/*    */ 
/*    */   
/*    */   private static String mapVariantToChestBoat(String id) {
/* 48 */     switch (id) { default: case "spruce": case "birch": case "jungle": case "acacia": case "cherry": case "dark_oak": case "mangrove": case "bamboo": break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 57 */       "minecraft:bamboo_chest_raft";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 63 */     OpticFinder<String> idF = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*    */     
/* 65 */     Type<?> oldType = getInputSchema().getType(References.ENTITY);
/* 66 */     Type<?> newType = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 68 */     return fixTypeEverywhereTyped("BoatSplitFix", oldType, newType, input -> {
/*    */           Optional<String> id = input.getOptional(idF);
/*    */           if (id.isPresent() && isAnyBoat(id.get())) {
/*    */             String newId;
/*    */             Dynamic<?> tag = (Dynamic)input.getOrCreate(DSL.remainderFinder());
/*    */             Optional<String> maybeBoatId = tag.get("Type").asString().result();
/*    */             if (isChestBoat(id.get())) {
/*    */               newId = maybeBoatId.<String>map(BoatSplitFix::mapVariantToChestBoat).orElse("minecraft:oak_chest_boat");
/*    */             } else {
/*    */               newId = maybeBoatId.<String>map(BoatSplitFix::mapVariantToNormalBoat).orElse("minecraft:oak_boat");
/*    */             } 
/*    */             return ExtraDataFixUtils.cast(newType, input).update(DSL.remainderFinder(), ()).set(idF, newId);
/*    */           } 
/*    */           return ExtraDataFixUtils.cast(newType, input);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BoatSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */