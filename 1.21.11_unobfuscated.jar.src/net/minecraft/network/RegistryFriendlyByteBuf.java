/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ 
/*    */ public class RegistryFriendlyByteBuf
/*    */   extends FriendlyByteBuf {
/*    */   private final RegistryAccess registryAccess;
/*    */   
/*    */   public RegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess) {
/* 12 */     super(source);
/* 13 */     this.registryAccess = registryAccess;
/*    */   }
/*    */   
/*    */   public RegistryAccess registryAccess() {
/* 17 */     return this.registryAccess;
/*    */   }
/*    */   
/*    */   public static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess registryAccess) {
/* 21 */     return buf -> new RegistryFriendlyByteBuf(buf, registryAccess);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/RegistryFriendlyByteBuf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */