/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ 
/*    */ public final class StaticAction extends Record implements Action {
/*    */   private final ClickEvent value;
/*    */   public static final Map<ClickEvent.Action, MapCodec<StaticAction>> WRAPPED_CODECS;
/*    */   
/* 12 */   public StaticAction(ClickEvent value) { this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/StaticAction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/StaticAction; } public ClickEvent value() { return this.value; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/StaticAction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/StaticAction; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/StaticAction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/StaticAction;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 15 */     WRAPPED_CODECS = (Map<ClickEvent.Action, MapCodec<StaticAction>>)net.minecraft.util.Util.make(() -> {
/*    */           Map<ClickEvent.Action, MapCodec<StaticAction>> result = new java.util.EnumMap<>(ClickEvent.Action.class);
/*    */           for (ClickEvent.Action action : (ClickEvent.Action[])ClickEvent.Action.class.getEnumConstants()) {
/*    */             if (action.isAllowedFromServer()) {
/*    */               MapCodec<ClickEvent> mapCodec = action.valueCodec();
/*    */               result.put(action, mapCodec.xmap(StaticAction::new, StaticAction::value));
/*    */             } 
/*    */           } 
/*    */           return java.util.Collections.unmodifiableMap(result);
/*    */         });
/*    */   }
/*    */   
/*    */   public MapCodec<StaticAction> codec() {
/* 28 */     return WRAPPED_CODECS.get(this.value.action());
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Optional<ClickEvent> createAction(Map<String, Action.ValueGetter> parameters) {
/* 33 */     return java.util.Optional.of(this.value);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/action/StaticAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */