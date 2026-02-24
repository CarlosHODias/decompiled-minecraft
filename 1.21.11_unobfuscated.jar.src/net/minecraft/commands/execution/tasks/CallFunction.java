/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandResultCallback;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.CommandQueueEntry;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ import net.minecraft.commands.execution.TraceCallbacks;
/*    */ import net.minecraft.commands.execution.UnboundEntryAction;
/*    */ import net.minecraft.commands.functions.InstantiatedFunction;
/*    */ 
/*    */ public class CallFunction<T extends ExecutionCommandSource<T>>
/*    */   implements UnboundEntryAction<T> {
/*    */   private final InstantiatedFunction<T> function;
/*    */   private final CommandResultCallback resultCallback;
/*    */   private final boolean returnParentFrame;
/*    */   
/*    */   public CallFunction(InstantiatedFunction<T> function, CommandResultCallback resultCallback, boolean returnParentFrame) {
/* 20 */     this.function = function;
/* 21 */     this.resultCallback = resultCallback;
/* 22 */     this.returnParentFrame = returnParentFrame;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(T sender, ExecutionContext<T> context, Frame frame) {
/* 27 */     context.incrementCost();
/*    */     
/* 29 */     List<UnboundEntryAction<T>> contents = this.function.entries();
/* 30 */     TraceCallbacks tracer = context.tracer();
/* 31 */     if (tracer != null) {
/* 32 */       tracer.onCall(frame.depth(), this.function.id(), this.function.entries().size());
/*    */     }
/*    */     
/* 35 */     int newDepth = frame.depth() + 1;
/* 36 */     Frame.FrameControl frameControl = this.returnParentFrame ? 
/* 37 */       frame.frameControl() : 
/* 38 */       context.frameControlForDepth(newDepth);
/* 39 */     Frame newFrame = new Frame(newDepth, this.resultCallback, frameControl);
/* 40 */     ContinuationTask.schedule(context, newFrame, contents, (frame1, entryAction) -> new CommandQueueEntry(frame1, entryAction.bind(sender)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/execution/tasks/CallFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */