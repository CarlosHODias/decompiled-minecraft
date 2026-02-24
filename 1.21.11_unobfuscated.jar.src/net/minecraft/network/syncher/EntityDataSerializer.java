/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public interface EntityDataSerializer<T> {
/*    */   StreamCodec<? super RegistryFriendlyByteBuf, T> codec();
/*    */   
/*    */   default EntityDataAccessor<T> createAccessor(int id) {
/* 10 */     return new EntityDataAccessor<>(id, this);
/*    */   }
/*    */   
/*    */   T copy(T paramT);
/*    */   
/*    */   public static interface ForValueType<T>
/*    */     extends EntityDataSerializer<T> {
/*    */     default T copy(T value) {
/* 18 */       return value;
/*    */     }
/*    */   }
/*    */   
/*    */   static <T> EntityDataSerializer<T> forValueType(StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
/* 23 */     return () -> codec;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/syncher/EntityDataSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */