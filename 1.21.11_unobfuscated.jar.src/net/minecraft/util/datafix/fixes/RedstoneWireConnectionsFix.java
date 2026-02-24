/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RedstoneWireConnectionsFix extends DataFix {
/*    */   public RedstoneWireConnectionsFix(Schema outputSchema) {
/* 11 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     Schema inputSchema = getInputSchema();
/* 17 */     return fixTypeEverywhereTyped("RedstoneConnectionsFix", inputSchema.getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), this::updateRedstoneConnections));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> updateRedstoneConnections(Dynamic<T> state) {
/* 21 */     boolean isRedstone = state.get("Name").asString().result().filter("minecraft:redstone_wire"::equals).isPresent();
/* 22 */     if (!isRedstone) {
/* 23 */       return state;
/*    */     }
/*    */     
/* 26 */     return state.update("Properties", props -> {
/*    */           String east = props.get("east").asString("none"), west = props.get("west").asString("none"), north = props.get("north").asString("none"), south = props.get("south").asString("none");
/*    */ 
/*    */ 
/*    */           
/* 31 */           boolean eastwest = (isConnected(east) || isConnected(west)), northsouth = 
/* 32 */             (isConnected(north) || isConnected(south));
/*    */           
/* 34 */           String newEast = (!isConnected(east) && !northsouth) ? "side" : east, newWest = 
/* 35 */             (!isConnected(west) && !northsouth) ? "side" : west, newNorth = 
/* 36 */             (!isConnected(north) && !eastwest) ? "side" : north, newSouth = 
/* 37 */             (!isConnected(south) && !eastwest) ? "side" : south;
/*    */           return props.update("east", ()).update("west", ()).update("north", ()).update("south", ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isConnected(String connectionType) {
/* 48 */     return !"none".equals(connectionType);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/RedstoneWireConnectionsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */