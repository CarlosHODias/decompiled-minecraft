/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import java.util.concurrent.locks.LockSupport;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.thread.BlockableEventLoop;
/*    */ 
/*    */ public class SoundEngineExecutor
/*    */   extends BlockableEventLoop<Runnable>
/*    */ {
/*    */   private Thread thread;
/*    */   private volatile boolean shutdown;
/*    */   
/*    */   public SoundEngineExecutor() {
/* 15 */     super("Sound executor");
/* 16 */     this.thread = createThread();
/*    */   }
/*    */   
/*    */   private Thread createThread() {
/* 20 */     Thread thread = new Thread(this::run);
/* 21 */     thread.setDaemon(true);
/* 22 */     thread.setName("Sound engine");
/* 23 */     thread.setUncaughtExceptionHandler((t, e) -> Minecraft.getInstance().delayCrash(CrashReport.forThrowable(e, "Uncaught exception on thread: " + t.getName())));
/* 24 */     thread.start();
/* 25 */     return thread;
/*    */   }
/*    */ 
/*    */   
/*    */   public Runnable wrapRunnable(Runnable runnable) {
/* 30 */     return runnable;
/*    */   }
/*    */ 
/*    */   
/*    */   public void schedule(Runnable runnable) {
/* 35 */     if (!this.shutdown) {
/* 36 */       super.schedule(runnable);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldRun(Runnable task) {
/* 42 */     return !this.shutdown;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Thread getRunningThread() {
/* 47 */     return this.thread;
/*    */   }
/*    */   
/*    */   private void run() {
/* 51 */     while (!this.shutdown) {
/* 52 */       managedBlock(() -> this.shutdown);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void waitForTasks() {
/* 59 */     LockSupport.park("waiting for tasks");
/*    */   }
/*    */   
/*    */   public void shutDown() {
/* 63 */     this.shutdown = true;
/* 64 */     dropAllTasks();
/* 65 */     this.thread.interrupt();
/*    */     try {
/* 67 */       this.thread.join();
/* 68 */     } catch (InterruptedException e) {
/* 69 */       Thread.currentThread().interrupt();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void startUp() {
/* 74 */     this.shutdown = false;
/* 75 */     this.thread = createThread();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/SoundEngineExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */