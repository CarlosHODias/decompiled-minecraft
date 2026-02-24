/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class WeaponSmithChestLootTableFix extends NamedEntityFix {
/*    */   public WeaponSmithChestLootTableFix(Schema outputSchema, boolean changesType) {
/*  9 */     super(outputSchema, changesType, "WeaponSmithChestLootTableFix", References.BLOCK_ENTITY, "minecraft:chest");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 14 */     return entity.update(DSL.remainderFinder(), tag -> {
/*    */           String lootTable = tag.get("LootTable").asString("");
/*    */           return lootTable.equals("minecraft:chests/village_blacksmith") ? tag.set("LootTable", tag.createString("minecraft:chests/village/village_weaponsmith")) : tag;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/WeaponSmithChestLootTableFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */