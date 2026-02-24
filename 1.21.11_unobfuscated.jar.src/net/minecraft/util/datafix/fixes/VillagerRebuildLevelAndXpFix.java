/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class VillagerRebuildLevelAndXpFix
/*    */   extends DataFix {
/*    */   private static final int TRADES_PER_LEVEL = 2;
/* 18 */   private static final int[] LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 50, 100, 150 };
/*    */   
/*    */   public static int getMinXpPerLevel(int level) {
/* 21 */     return LEVEL_XP_THRESHOLDS[Mth.clamp(level - 1, 0, LEVEL_XP_THRESHOLDS.length - 1)];
/*    */   }
/*    */   
/*    */   public VillagerRebuildLevelAndXpFix(Schema outputSchema, boolean changesType) {
/* 25 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 30 */     Type<?> villagerType = getInputSchema().getChoiceType(References.ENTITY, "minecraft:villager");
/* 31 */     OpticFinder<?> entityF = DSL.namedChoice("minecraft:villager", villagerType);
/*    */     
/* 33 */     OpticFinder<?> offersF = villagerType.findField("Offers");
/* 34 */     Type<?> offersType = offersF.type();
/* 35 */     OpticFinder<?> recipeListF = offersType.findField("Recipes");
/* 36 */     List.ListType<?> recipeListType = (List.ListType)recipeListF.type();
/* 37 */     OpticFinder<?> recipeF = recipeListType.getElement().finder();
/*    */     
/* 39 */     return fixTypeEverywhereTyped("Villager level and xp rebuild", getInputSchema().getType(References.ENTITY), input -> input.updateTyped(entityF, villagerType, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> addLevel(Typed<?> villager, int level) {
/* 72 */     return villager.update(DSL.remainderFinder(), remainder -> remainder.update("VillagerData", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> addXpFromLevel(Typed<?> villager, int level) {
/* 79 */     int xp = getMinXpPerLevel(level);
/* 80 */     return villager.update(DSL.remainderFinder(), remainder -> remainder.set("Xp", remainder.createInt(xp)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/VillagerRebuildLevelAndXpFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */