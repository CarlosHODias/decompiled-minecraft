/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public interface LootContextArg<R>
/*    */ {
/*    */   static {
/* 16 */     ENTITY_OR_BLOCK = createArgCodec(builder -> builder.anyOf((Object[])LootContext.EntityTarget.values()).anyOf((Object[])LootContext.BlockEntityTarget.values()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<LootContextArg<Object>> ENTITY_OR_BLOCK;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <U> LootContextArg<U> cast(LootContextArg<? extends U> original) {
/* 28 */     return (LootContextArg)original;
/*    */   }
/*    */   
/*    */   static <R> Codec<LootContextArg<R>> createArgCodec(UnaryOperator<ArgCodecBuilder<R>> consumer) {
/* 32 */     return ((ArgCodecBuilder<R>)consumer.apply(new ArgCodecBuilder<>())).build();
/*    */   }
/*    */   
/*    */   R get(LootContext paramLootContext);
/*    */   
/*    */   ContextKey<?> contextParam();
/*    */   
/*    */   public static interface Getter<T, R> extends LootContextArg<R> {
/*    */     R get(T param1T);
/*    */     
/*    */     default R get(LootContext context) {
/* 43 */       T value = context.getOptionalParameter((ContextKey)contextParam());
/* 44 */       return (value != null) ? get(value) : null;
/*    */     }
/*    */     
/*    */     ContextKey<? extends T> contextParam();
/*    */   }
/*    */   
/*    */   public static interface SimpleGetter<T> extends LootContextArg<T> {
/*    */     ContextKey<? extends T> contextParam();
/*    */     
/*    */     default T get(LootContext context) {
/* 54 */       return context.getOptionalParameter((ContextKey)contextParam());
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class ArgCodecBuilder<R> {
/* 59 */     private final ExtraCodecs.LateBoundIdMapper<String, LootContextArg<R>> sources = new ExtraCodecs.LateBoundIdMapper();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <T> ArgCodecBuilder<R> anyOf(T[] targets, Function<T, String> nameGetter, Function<T, ? extends LootContextArg<R>> argFactory) {
/* 65 */       for (T target : targets) {
/* 66 */         this.sources.put(nameGetter.apply(target), argFactory.apply(target));
/*    */       }
/* 68 */       return this;
/*    */     }
/*    */     
/*    */     public <T extends StringRepresentable> ArgCodecBuilder<R> anyOf(T[] targets, Function<T, ? extends LootContextArg<R>> argFactory) {
/* 72 */       return anyOf(targets, StringRepresentable::getSerializedName, argFactory);
/*    */     }
/*    */     
/*    */     public <T extends StringRepresentable & LootContextArg<? extends R>> ArgCodecBuilder<R> anyOf(T[] targets) {
/* 76 */       return anyOf((StringRepresentable[])targets, x$0 -> LootContextArg.cast((LootContextArg)x$0));
/*    */     }
/*    */     
/*    */     public ArgCodecBuilder<R> anyEntity(Function<? super ContextKey<? extends Entity>, ? extends LootContextArg<R>> function) {
/* 80 */       return anyOf(LootContext.EntityTarget.values(), target -> (LootContextArg)function.apply(target.contextParam()));
/*    */     }
/*    */     
/*    */     public ArgCodecBuilder<R> anyBlockEntity(Function<? super ContextKey<? extends BlockEntity>, ? extends LootContextArg<R>> function) {
/* 84 */       return anyOf(LootContext.BlockEntityTarget.values(), target -> (LootContextArg)function.apply(target.contextParam()));
/*    */     }
/*    */     
/*    */     public ArgCodecBuilder<R> anyItemStack(Function<? super ContextKey<? extends ItemStack>, ? extends LootContextArg<R>> function) {
/* 88 */       return anyOf(LootContext.ItemStackTarget.values(), target -> (LootContextArg)function.apply(target.contextParam()));
/*    */     }
/*    */     
/*    */     private Codec<LootContextArg<R>> build() {
/* 92 */       return this.sources.codec((Codec)Codec.STRING);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/LootContextArg.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */