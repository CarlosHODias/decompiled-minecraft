/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class JukeboxTicksSinceSongStartedFix extends NamedEntityFix {
/*    */   public JukeboxTicksSinceSongStartedFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false, "JukeboxTicksSinceSongStartedFix", References.BLOCK_ENTITY, "minecraft:jukebox");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     long ticksSinceSongStarted = input.get("TickCount").asLong(0L) - input.get("RecordStartTick").asLong(0L);
/* 15 */     Dynamic<?> result = input.remove("IsPlaying").remove("TickCount").remove("RecordStartTick");
/* 16 */     if (ticksSinceSongStarted > 0L) {
/* 17 */       return result.set("ticks_since_song_started", input.createLong(ticksSinceSongStarted));
/*    */     }
/* 19 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 24 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/JukeboxTicksSinceSongStartedFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */