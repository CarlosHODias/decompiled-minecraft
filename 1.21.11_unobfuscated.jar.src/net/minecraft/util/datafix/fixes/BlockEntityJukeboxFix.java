/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityJukeboxFix extends NamedEntityFix {
/*    */   public BlockEntityJukeboxFix(Schema outputSchema, boolean changesType) {
/* 12 */     super(outputSchema, changesType, "BlockEntityJukeboxFix", References.BLOCK_ENTITY, "minecraft:jukebox");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 17 */     Type<?> jukeboxType = getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:jukebox");
/* 18 */     Type<?> itemStackType = jukeboxType.findFieldType("RecordItem");
/* 19 */     OpticFinder<?> recordItemF = DSL.fieldFinder("RecordItem", itemStackType);
/* 20 */     Dynamic<?> tag = (Dynamic)entity.get(DSL.remainderFinder());
/* 21 */     int recordId = tag.get("Record").asInt(0);
/* 22 */     if (recordId > 0) {
/* 23 */       tag.remove("Record");
/*    */       
/* 25 */       String id = ItemStackTheFlatteningFix.updateItem(ItemIdFix.getItem(recordId), 0);
/* 26 */       if (id != null) {
/* 27 */         Dynamic<?> itemTag = tag.emptyMap();
/* 28 */         itemTag = itemTag.set("id", itemTag.createString(id));
/* 29 */         itemTag = itemTag.set("Count", itemTag.createByte((byte)1));
/* 30 */         return entity.set(recordItemF, (Typed)((Pair)itemStackType.readTyped(itemTag).result().orElseThrow(() -> new IllegalStateException("Could not create record item stack."))).getFirst()).set(DSL.remainderFinder(), tag);
/*    */       } 
/*    */     } 
/* 33 */     return entity;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BlockEntityJukeboxFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */