/*    */ package net.minecraft.client.renderer.block.model.multipart;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Condition
/*    */ {
/*    */   static {
/* 16 */     CODEC = Codec.recursive("condition", self -> {
/*    */           Codec<CombinedCondition> combinerCodec = Codec.simpleMap(CombinedCondition.Operation.CODEC, self.listOf(), StringRepresentable.keys((StringRepresentable[])CombinedCondition.Operation.values())).codec().comapFlatMap((), ());
/*    */           return Codec.either(combinerCodec, KeyValueCondition.CODEC).flatComapMap((), ());
/*    */         });
/*    */   }
/*    */   
/*    */   public static final Codec<Condition> CODEC;
/*    */   
/*    */   <O, S extends net.minecraft.world.level.block.state.StateHolder<O, S>> Predicate<S> instantiate(StateDefinition<O, S> paramStateDefinition);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/multipart/Condition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */