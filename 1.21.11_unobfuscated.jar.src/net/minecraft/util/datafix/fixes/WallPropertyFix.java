/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class WallPropertyFix extends DataFix {
/* 14 */   private static final Set<String> WALL_BLOCKS = (Set<String>)ImmutableSet.of("minecraft:andesite_wall", "minecraft:brick_wall", "minecraft:cobblestone_wall", "minecraft:diorite_wall", "minecraft:end_stone_brick_wall", "minecraft:granite_wall", (Object[])new String[] { "minecraft:mossy_cobblestone_wall", "minecraft:mossy_stone_brick_wall", "minecraft:nether_brick_wall", "minecraft:prismarine_wall", "minecraft:red_nether_brick_wall", "minecraft:red_sandstone_wall", "minecraft:sandstone_wall", "minecraft:stone_brick_wall" });
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
/*    */   public WallPropertyFix(Schema outputSchema, boolean changesType) {
/* 32 */     super(outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 37 */     return fixTypeEverywhereTyped("WallPropertyFix", getInputSchema().getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), WallPropertyFix::upgradeBlockStateTag));
/*    */   }
/*    */   
/*    */   private static String mapProperty(String value) {
/* 41 */     return "true".equals(value) ? "low" : "none";
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixWallProperty(Dynamic<T> state, String property) {
/* 45 */     return state.update(property, value -> {
/*    */           Objects.requireNonNull(value);
/*    */           return DataFixUtils.orElse(value.asString().result().map(WallPropertyFix::mapProperty).map(value::createString), value);
/*    */         }); } private static <T> Dynamic<T> upgradeBlockStateTag(Dynamic<T> state) {
/* 49 */     Objects.requireNonNull(WALL_BLOCKS); boolean isWall = state.get("Name").asString().result().filter(WALL_BLOCKS::contains).isPresent();
/* 50 */     if (!isWall) {
/* 51 */       return state;
/*    */     }
/*    */     
/* 54 */     return state.update("Properties", properties -> {
/*    */           Dynamic<?> newState = fixWallProperty(properties, "east");
/*    */           newState = fixWallProperty(newState, "west");
/*    */           newState = fixWallProperty(newState, "north");
/*    */           return fixWallProperty(newState, "south");
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/WallPropertyFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */