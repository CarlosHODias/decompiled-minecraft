/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public final class FunctionTagCallback extends Record implements TimerCallback<MinecraftServer> {
/*    */   private final Identifier tagId;
/*    */   public static final com.mojang.serialization.MapCodec<FunctionTagCallback> CODEC;
/*    */   
/* 13 */   public FunctionTagCallback(Identifier tagId) { this.tagId = tagId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/FunctionTagCallback;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionTagCallback; } public Identifier tagId() { return this.tagId; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/FunctionTagCallback;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionTagCallback; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/FunctionTagCallback;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/timers/FunctionTagCallback;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)Identifier.CODEC.fieldOf("Name").forGetter(FunctionTagCallback::tagId)).apply((com.mojang.datafixers.kinds.Applicative)i, FunctionTagCallback::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(MinecraftServer server, TimerQueue<MinecraftServer> queue, long time) {
/* 20 */     ServerFunctionManager functionManager = server.getFunctions();
/* 21 */     java.util.List<net.minecraft.commands.functions.CommandFunction<CommandSourceStack>> tag = functionManager.getTag(this.tagId);
/* 22 */     for (net.minecraft.commands.functions.CommandFunction<CommandSourceStack> function : tag) {
/* 23 */       functionManager.execute(function, functionManager.getGameLoopSender());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<FunctionTagCallback> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/timers/FunctionTagCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */