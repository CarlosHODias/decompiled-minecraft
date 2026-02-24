/*     */ package net.minecraft.commands.execution;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ExecutionContext<T>
/*     */   implements AutoCloseable
/*     */ {
/*     */   private static final int MAX_QUEUE_DEPTH = 10000000;
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final int commandLimit;
/*     */   
/*     */   private final int forkLimit;
/*     */   private final ProfilerFiller profiler;
/*     */   private TraceCallbacks tracer;
/*     */   private int commandQuota;
/*     */   private boolean queueOverflow;
/*  31 */   private final Deque<CommandQueueEntry<T>> commandQueue = Queues.newArrayDeque();
/*  32 */   private final List<CommandQueueEntry<T>> newTopCommands = (List<CommandQueueEntry<T>>)new ObjectArrayList();
/*     */   private int currentFrameDepth;
/*     */   
/*     */   public ExecutionContext(int commandLimit, int forkLimit, ProfilerFiller profiler) {
/*  36 */     this.commandLimit = commandLimit;
/*  37 */     this.forkLimit = forkLimit;
/*  38 */     this.profiler = profiler;
/*     */     
/*  40 */     this.commandQuota = commandLimit;
/*     */   }
/*     */   
/*     */   private static <T extends ExecutionCommandSource<T>> Frame createTopFrame(ExecutionContext<T> context, CommandResultCallback frameResult) {
/*  44 */     if (context.currentFrameDepth == 0) {
/*  45 */       Objects.requireNonNull(context.commandQueue); return new Frame(0, frameResult, context.commandQueue::clear);
/*     */     } 
/*  47 */     int reentrantFrameDepth = context.currentFrameDepth + 1;
/*  48 */     return new Frame(reentrantFrameDepth, frameResult, context.frameControlForDepth(reentrantFrameDepth));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends ExecutionCommandSource<T>> void queueInitialFunctionCall(ExecutionContext<T> context, InstantiatedFunction<T> function, T sender, CommandResultCallback functionReturn) {
/*  53 */     context.queueNext(new CommandQueueEntry<>(createTopFrame(context, functionReturn), new CallFunction(function, sender.callback(), false).bind(sender)));
/*     */   }
/*     */   
/*     */   public static <T extends ExecutionCommandSource<T>> void queueInitialCommandExecution(ExecutionContext<T> context, String command, ContextChain<T> executionChain, T sender, CommandResultCallback commandReturn) {
/*  57 */     context.queueNext(new CommandQueueEntry<>(createTopFrame(context, commandReturn), (EntryAction<T>)new BuildContexts.TopLevel(command, executionChain, (ExecutionCommandSource)sender)));
/*     */   }
/*     */   
/*     */   private void handleQueueOverflow() {
/*  61 */     this.queueOverflow = true;
/*     */     
/*  63 */     this.newTopCommands.clear();
/*  64 */     this.commandQueue.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueNext(CommandQueueEntry<T> entry) {
/*  70 */     if (this.newTopCommands.size() + this.commandQueue.size() > 10000000) {
/*  71 */       handleQueueOverflow();
/*     */     }
/*     */     
/*  74 */     if (!this.queueOverflow) {
/*  75 */       this.newTopCommands.add(entry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void discardAtDepthOrHigher(int depthToDiscard) {
/*  81 */     while (!this.commandQueue.isEmpty() && ((CommandQueueEntry)this.commandQueue.peek()).frame().depth() >= depthToDiscard) {
/*  82 */       this.commandQueue.removeFirst();
/*     */     }
/*     */   }
/*     */   
/*     */   public Frame.FrameControl frameControlForDepth(int depthToDiscard) {
/*  87 */     return () -> discardAtDepthOrHigher(depthToDiscard);
/*     */   }
/*     */   
/*     */   public void runCommandQueue() {
/*  91 */     pushNewCommands();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  96 */       if (this.commandQuota <= 0) {
/*  97 */         LOGGER.info("Command execution stopped due to limit (executed {} commands)", this.commandLimit);
/*     */         
/*     */         break;
/*     */       } 
/* 101 */       CommandQueueEntry<T> command = this.commandQueue.pollFirst();
/* 102 */       if (command == null) {
/*     */         return;
/*     */       }
/* 105 */       this.currentFrameDepth = command.frame().depth();
/* 106 */       command.execute(this);
/*     */       
/* 108 */       if (this.queueOverflow) {
/* 109 */         LOGGER.error("Command execution stopped due to command queue overflow (max {})", 10000000);
/*     */         
/*     */         break;
/*     */       } 
/* 113 */       pushNewCommands();
/*     */     } 
/* 115 */     this.currentFrameDepth = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void pushNewCommands() {
/* 120 */     for (int i = this.newTopCommands.size() - 1; i >= 0; i--) {
/* 121 */       this.commandQueue.addFirst(this.newTopCommands.get(i));
/*     */     }
/* 123 */     this.newTopCommands.clear();
/*     */   }
/*     */   
/*     */   public void tracer(TraceCallbacks tracer) {
/* 127 */     this.tracer = tracer;
/*     */   }
/*     */   
/*     */   public TraceCallbacks tracer() {
/* 131 */     return this.tracer;
/*     */   }
/*     */   
/*     */   public ProfilerFiller profiler() {
/* 135 */     return this.profiler;
/*     */   }
/*     */   
/*     */   public int forkLimit() {
/* 139 */     return this.forkLimit;
/*     */   }
/*     */   
/*     */   public void incrementCost() {
/* 143 */     this.commandQuota--;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 148 */     if (this.tracer != null)
/* 149 */       this.tracer.close(); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/execution/ExecutionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */