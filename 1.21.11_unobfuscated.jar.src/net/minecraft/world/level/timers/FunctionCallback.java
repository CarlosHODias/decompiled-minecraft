/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public final class FunctionCallback extends Record implements TimerCallback<net.minecraft.server.MinecraftServer> {
/*    */   private final net.minecraft.resources.Identifier functionId;
/*    */   public static final com.mojang.serialization.MapCodec<FunctionCallback> CODEC;
/*    */   
/*  9 */   public FunctionCallback(net.minecraft.resources.Identifier functionId) { this.functionId = functionId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/FunctionCallback;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionCallback; } public net.minecraft.resources.Identifier functionId() { return this.functionId; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/FunctionCallback;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionCallback; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/FunctionCallback;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/timers/FunctionCallback;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.resources.Identifier.CODEC.fieldOf("Name").forGetter(FunctionCallback::functionId)).apply((com.mojang.datafixers.kinds.Applicative)i, FunctionCallback::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(net.minecraft.server.MinecraftServer server, TimerQueue<net.minecraft.server.MinecraftServer> queue, long time) {
/* 16 */     ServerFunctionManager functionManager = server.getFunctions();
/* 17 */     functionManager.get(this.functionId).ifPresent(function -> functionManager.execute(function, functionManager.getGameLoopSender()));
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<FunctionCallback> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/timers/FunctionCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */