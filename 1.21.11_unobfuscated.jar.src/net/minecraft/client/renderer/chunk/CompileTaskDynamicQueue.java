/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.List;
/*    */ import java.util.ListIterator;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class CompileTaskDynamicQueue
/*    */ {
/*    */   private static final int MAX_RECOMPILE_QUOTA = 2;
/* 12 */   private int recompileQuota = 2;
/* 13 */   private final List<SectionRenderDispatcher.RenderSection.CompileTask> tasks = (List<SectionRenderDispatcher.RenderSection.CompileTask>)new ObjectArrayList();
/*    */   
/*    */   public synchronized void add(SectionRenderDispatcher.RenderSection.CompileTask task) {
/* 16 */     this.tasks.add(task);
/*    */   }
/*    */   
/*    */   public synchronized SectionRenderDispatcher.RenderSection.CompileTask poll(Vec3 cameraPos) {
/* 20 */     int bestInitialCompileTaskIndex = -1;
/* 21 */     int bestRecompileTaskIndex = -1;
/* 22 */     double bestInitialCompileDistance = Double.MAX_VALUE;
/* 23 */     double bestRecompileDistance = Double.MAX_VALUE;
/*    */     
/* 25 */     ListIterator<SectionRenderDispatcher.RenderSection.CompileTask> iterator = this.tasks.listIterator();
/* 26 */     while (iterator.hasNext()) {
/* 27 */       int taskIndex = iterator.nextIndex();
/* 28 */       SectionRenderDispatcher.RenderSection.CompileTask task = iterator.next();
/* 29 */       if (task.isCancelled.get()) {
/* 30 */         iterator.remove();
/*    */         continue;
/*    */       } 
/* 33 */       double distance = task.getRenderOrigin().distToCenterSqr((Position)cameraPos);
/* 34 */       if (!task.isRecompile() && distance < bestInitialCompileDistance) {
/* 35 */         bestInitialCompileDistance = distance;
/* 36 */         bestInitialCompileTaskIndex = taskIndex;
/*    */       } 
/* 38 */       if (task.isRecompile() && distance < bestRecompileDistance) {
/* 39 */         bestRecompileDistance = distance;
/* 40 */         bestRecompileTaskIndex = taskIndex;
/*    */       } 
/*    */     } 
/*    */     
/* 44 */     boolean hasRecompileTask = (bestRecompileTaskIndex >= 0);
/* 45 */     boolean hasInitialCompileTask = (bestInitialCompileTaskIndex >= 0);
/*    */     
/* 47 */     if (hasRecompileTask && (!hasInitialCompileTask || (this.recompileQuota > 0 && bestRecompileDistance < bestInitialCompileDistance))) {
/* 48 */       this.recompileQuota--;
/* 49 */       return removeTaskByIndex(bestRecompileTaskIndex);
/*    */     } 
/*    */     
/* 52 */     this.recompileQuota = 2;
/* 53 */     return removeTaskByIndex(bestInitialCompileTaskIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 58 */     return this.tasks.size();
/*    */   }
/*    */   
/*    */   private SectionRenderDispatcher.RenderSection.CompileTask removeTaskByIndex(int taskIndex) {
/* 62 */     if (taskIndex >= 0) {
/* 63 */       return this.tasks.remove(taskIndex);
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void clear() {
/* 70 */     for (SectionRenderDispatcher.RenderSection.CompileTask task : this.tasks) {
/* 71 */       task.cancel();
/*    */     }
/* 73 */     this.tasks.clear();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/CompileTaskDynamicQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */