/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ 
/*     */ public class InlineBlockPosFormatFix extends DataFix {
/*     */   public InlineBlockPosFormatFix(Schema outputSchema) {
/*  18 */     super(outputSchema, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  23 */     OpticFinder<?> vexFinder = entityFinder("minecraft:vex");
/*  24 */     OpticFinder<?> phantomFinder = entityFinder("minecraft:phantom");
/*  25 */     OpticFinder<?> turtleFinder = entityFinder("minecraft:turtle");
/*  26 */     List<OpticFinder<?>> blockAttachedFinders = List.of(
/*  27 */         entityFinder("minecraft:item_frame"), 
/*  28 */         entityFinder("minecraft:glow_item_frame"), 
/*  29 */         entityFinder("minecraft:painting"), 
/*  30 */         entityFinder("minecraft:leash_knot"));
/*     */     
/*  32 */     return TypeRewriteRule.seq(
/*  33 */         fixTypeEverywhereTyped("InlineBlockPosFormatFix - player", getInputSchema().getType(References.PLAYER), player -> player.update(DSL.remainderFinder(), this::fixPlayer)), 
/*     */ 
/*     */         
/*  36 */         fixTypeEverywhereTyped("InlineBlockPosFormatFix - entity", getInputSchema().getType(References.ENTITY), entity -> {
/*     */             vexFinder = vexFinder.update(DSL.remainderFinder(), this::fixLivingEntity).updateTyped(vexFinder, ()).updateTyped(vexFinder, ()).updateTyped(phantomFinder, ());
/*     */             for (OpticFinder<?> blockAttachedFinder : (Iterable<OpticFinder<?>>)turtleFinder) {
/*     */               vexFinder = vexFinder.updateTyped(blockAttachedFinder, ());
/*     */             }
/*     */             return vexFinder;
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OpticFinder<?> entityFinder(String choiceName) {
/*  50 */     return DSL.namedChoice(choiceName, getInputSchema().getChoiceType(References.ENTITY, choiceName));
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixPlayer(Dynamic<?> tag) {
/*  54 */     tag = fixLivingEntity(tag);
/*  55 */     Optional<Number> spawnX = tag.get("SpawnX").asNumber().result();
/*  56 */     Optional<Number> spawnY = tag.get("SpawnY").asNumber().result();
/*  57 */     Optional<Number> spawnZ = tag.get("SpawnZ").asNumber().result();
/*  58 */     if (spawnX.isPresent() && spawnY.isPresent() && spawnZ.isPresent()) {
/*  59 */       Dynamic<?> respawn = tag.createMap(Map.of(
/*  60 */             tag.createString("pos"), ExtraDataFixUtils.createBlockPos(tag, ((Number)spawnX.get()).intValue(), ((Number)spawnY.get()).intValue(), ((Number)spawnZ.get()).intValue())));
/*     */       
/*  62 */       respawn = Dynamic.copyField(tag, "SpawnAngle", respawn, "angle");
/*  63 */       respawn = Dynamic.copyField(tag, "SpawnDimension", respawn, "dimension");
/*  64 */       respawn = Dynamic.copyField(tag, "SpawnForced", respawn, "forced");
/*  65 */       tag = tag.remove("SpawnX").remove("SpawnY").remove("SpawnZ").remove("SpawnAngle").remove("SpawnDimension").remove("SpawnForced");
/*  66 */       tag = tag.set("respawn", respawn);
/*     */     } 
/*  68 */     Optional<? extends Dynamic<?>> enteredNetherPos = tag.get("enteredNetherPosition").result();
/*  69 */     if (enteredNetherPos.isPresent()) {
/*  70 */       tag = tag.remove("enteredNetherPosition").set("entered_nether_pos", tag.createList(Stream.of(new Dynamic[] {
/*  71 */                 tag.createDouble(((Dynamic)enteredNetherPos.get()).get("x").asDouble(0.0D)), 
/*  72 */                 tag.createDouble(((Dynamic)enteredNetherPos.get()).get("y").asDouble(0.0D)), 
/*  73 */                 tag.createDouble(((Dynamic)enteredNetherPos.get()).get("z").asDouble(0.0D))
/*     */               })));
/*     */     }
/*  76 */     return tag;
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixLivingEntity(Dynamic<?> tag) {
/*  80 */     return ExtraDataFixUtils.fixInlineBlockPos(tag, "SleepingX", "SleepingY", "SleepingZ", "sleeping_pos");
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixVex(Dynamic<?> tag) {
/*  84 */     return ExtraDataFixUtils.fixInlineBlockPos(
/*  85 */         tag.renameField("LifeTicks", "life_ticks"), "BoundX", "BoundY", "BoundZ", "bound_pos");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dynamic<?> fixPhantom(Dynamic<?> tag) {
/*  92 */     return ExtraDataFixUtils.fixInlineBlockPos(
/*  93 */         tag.renameField("Size", "size"), "AX", "AY", "AZ", "anchor_pos");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dynamic<?> fixTurtle(Dynamic<?> tag) {
/* 100 */     tag = tag.remove("TravelPosX").remove("TravelPosY").remove("TravelPosZ");
/* 101 */     tag = ExtraDataFixUtils.fixInlineBlockPos(tag, "HomePosX", "HomePosY", "HomePosZ", "home_pos");
/* 102 */     return tag.renameField("HasEgg", "has_egg");
/*     */   }
/*     */   
/*     */   private Dynamic<?> fixBlockAttached(Dynamic<?> tag) {
/* 106 */     return ExtraDataFixUtils.fixInlineBlockPos(tag, "TileX", "TileY", "TileZ", "block_pos");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/InlineBlockPosFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */