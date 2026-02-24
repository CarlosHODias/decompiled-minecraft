/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.RedirectModifier;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface CustomModifierExecutor<T>
/*    */ {
/*    */   void apply(T paramT, List<T> paramList, ContextChain<T> paramContextChain, ChainModifiers paramChainModifiers, ExecutionControl<T> paramExecutionControl);
/*    */   
/*    */   public static interface ModifierAdapter<T>
/*    */     extends CustomModifierExecutor<T>, RedirectModifier<T> {
/*    */     default Collection<T> apply(CommandContext<T> context) throws CommandSyntaxException {
/* 17 */       throw new UnsupportedOperationException("This function should not run");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/execution/CustomModifierExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */