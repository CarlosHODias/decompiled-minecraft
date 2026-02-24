/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.commands.CommandResultCallback;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.EntryAction;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.ExecutionControl;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ public class IsolatedCall<T extends ExecutionCommandSource<T>>
/*    */   implements EntryAction<T> {
/*    */   private final Consumer<ExecutionControl<T>> taskProducer;
/*    */   private final CommandResultCallback output;
/*    */   
/*    */   public IsolatedCall(Consumer<ExecutionControl<T>> taskOutput, CommandResultCallback output) {
/* 17 */     this.taskProducer = taskOutput;
/* 18 */     this.output = output;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(ExecutionContext<T> context, Frame frame) {
/* 23 */     int newFrameDepth = frame.depth() + 1;
/* 24 */     Frame newFrame = new Frame(newFrameDepth, this.output, context.frameControlForDepth(newFrameDepth));
/* 25 */     this.taskProducer.accept(ExecutionControl.create(context, newFrame));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/execution/tasks/IsolatedCall.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */