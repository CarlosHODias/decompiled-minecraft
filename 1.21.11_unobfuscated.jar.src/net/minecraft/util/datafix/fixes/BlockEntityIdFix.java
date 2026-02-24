/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class BlockEntityIdFix extends com.mojang.datafixers.DataFix {
/*    */   public BlockEntityIdFix(Schema outputSchema, boolean changesType) {
/* 16 */     super(outputSchema, changesType);
/*    */   } public static final Map<String, String> ID_MAP;
/*    */   static {
/* 19 */     ID_MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*    */           map.put("Airportal", "minecraft:end_portal");
/*    */           map.put("Banner", "minecraft:banner");
/*    */           map.put("Beacon", "minecraft:beacon");
/*    */           map.put("Cauldron", "minecraft:brewing_stand");
/*    */           map.put("Chest", "minecraft:chest");
/*    */           map.put("Comparator", "minecraft:comparator");
/*    */           map.put("Control", "minecraft:command_block");
/*    */           map.put("DLDetector", "minecraft:daylight_detector");
/*    */           map.put("Dropper", "minecraft:dropper");
/*    */           map.put("EnchantTable", "minecraft:enchanting_table");
/*    */           map.put("EndGateway", "minecraft:end_gateway");
/*    */           map.put("EnderChest", "minecraft:ender_chest");
/*    */           map.put("FlowerPot", "minecraft:flower_pot");
/*    */           map.put("Furnace", "minecraft:furnace");
/*    */           map.put("Hopper", "minecraft:hopper");
/*    */           map.put("MobSpawner", "minecraft:mob_spawner");
/*    */           map.put("Music", "minecraft:noteblock");
/*    */           map.put("Piston", "minecraft:piston");
/*    */           map.put("RecordPlayer", "minecraft:jukebox");
/*    */           map.put("Sign", "minecraft:sign");
/*    */           map.put("Skull", "minecraft:skull");
/*    */           map.put("Structure", "minecraft:structure_block");
/*    */           map.put("Trap", "minecraft:dispenser");
/*    */         });
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 47 */     Type<?> oldItemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 48 */     Type<?> newItemStackType = getOutputSchema().getType(References.ITEM_STACK);
/*    */     
/* 50 */     TaggedChoice.TaggedChoiceType<String> oldType = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 51 */     TaggedChoice.TaggedChoiceType<String> newType = getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
/*    */     
/* 53 */     return TypeRewriteRule.seq(
/* 54 */         convertUnchecked("item stack block entity name hook converter", oldItemStackType, newItemStackType), 
/* 55 */         fixTypeEverywhere("BlockEntityIdFix", (Type)oldType, (Type)newType, ops -> ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BlockEntityIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */