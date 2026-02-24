/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityPaintingMotiveFix extends NamedEntityFix {
/*    */   public EntityPaintingMotiveFix(Schema outputSchema, boolean changesType) {
/* 17 */     super(outputSchema, changesType, "EntityPaintingMotiveFix", References.ENTITY, "minecraft:painting");
/*    */   } private static final Map<String, String> MAP;
/*    */   static {
/* 20 */     MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*    */           map.put("donkeykong", "donkey_kong");
/*    */           map.put("burningskull", "burning_skull");
/*    */           map.put("skullandroses", "skull_and_roses");
/*    */         });
/*    */   }
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 27 */     Optional<String> motive = input.get("Motive").asString().result();
/* 28 */     if (motive.isPresent()) {
/* 29 */       String lowerCaseMotive = ((String)motive.get()).toLowerCase(Locale.ROOT);
/* 30 */       return input.set("Motive", input.createString(NamespacedSchema.ensureNamespaced(MAP.getOrDefault(lowerCaseMotive, lowerCaseMotive))));
/*    */     } 
/* 32 */     return input;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 37 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityPaintingMotiveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */