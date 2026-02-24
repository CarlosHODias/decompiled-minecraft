/*   */ package net.minecraft.commands.execution;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface UnboundEntryAction<T> {
/*   */   void execute(T paramT, ExecutionContext<T> paramExecutionContext, Frame paramFrame);
/*   */   
/*   */   default EntryAction<T> bind(T sender) {
/* 8 */     return (context, frame) -> execute((T)sender, sender, frame);
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/execution/UnboundEntryAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */