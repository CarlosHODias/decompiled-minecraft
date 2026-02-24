/*     */ package net.minecraft.client;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
/*     */ import java.io.Reader;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PeriodicNotificationManager extends SimplePreparableReloadListener<Map<String, List<PeriodicNotificationManager.Notification>>> implements AutoCloseable {
/*     */   private static final Codec<Map<String, List<Notification>>> CODEC;
/*     */   
/*     */   static {
/*  31 */     CODEC = (Codec<Map<String, List<Notification>>>)Codec.unboundedMap((Codec)Codec.STRING, RecordCodecBuilder.create(i -> i.group((App)Codec.LONG.optionalFieldOf("delay", 0L).forGetter(Notification::delay), (App)Codec.LONG.fieldOf("period").forGetter(Notification::period), (App)Codec.STRING.fieldOf("title").forGetter(Notification::title), (App)Codec.STRING.fieldOf("message").forGetter(Notification::message)).apply((Applicative)i, Notification::new))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  36 */         .listOf());
/*     */   }
/*  38 */   private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   
/*     */   private final Identifier notifications;
/*     */   
/*     */   private final Object2BooleanFunction<String> selector;
/*     */   private Timer timer;
/*     */   private NotificationTask notificationTask;
/*     */   
/*     */   public PeriodicNotificationManager(Identifier notifications, Object2BooleanFunction<String> selector) {
/*  47 */     this.notifications = notifications;
/*  48 */     this.selector = selector;
/*     */   }
/*     */   
/*     */   protected Map<String, List<Notification>> prepare(ResourceManager manager, ProfilerFiller profiler) {
/*     */     
/*  53 */     try { Reader reader = manager.openAsReader(this.notifications); 
/*  54 */       try { Map<String, List<Notification>> map = CODEC.parse((com.mojang.serialization.DynamicOps)com.mojang.serialization.JsonOps.INSTANCE, net.minecraft.util.StrictJsonParser.parse(reader)).result().orElseThrow();
/*  55 */         if (reader != null) reader.close();  return map; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  56 */     { LOGGER.warn("Failed to load {}", this.notifications, e);
/*     */       
/*  58 */       return (Map<String, List<Notification>>)com.google.common.collect.ImmutableMap.of(); }
/*     */   
/*     */   }
/*     */   
/*     */   protected void apply(Map<String, List<Notification>> preparations, ResourceManager manager, ProfilerFiller profiler) {
/*  63 */     List<Notification> notifications = (List<Notification>)preparations.entrySet().stream()
/*  64 */       .filter(e -> (Boolean)this.selector.apply(e.getKey()))
/*  65 */       .map(Map.Entry::getValue)
/*  66 */       .flatMap(Collection::stream)
/*  67 */       .collect(Collectors.toList());
/*     */     
/*  69 */     if (notifications.isEmpty()) {
/*  70 */       stopTimer();
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     if (notifications.stream().anyMatch(n -> (n.period == 0L))) {
/*  75 */       Util.logAndPauseIfInIde("A periodic notification in " + String.valueOf(this.notifications) + " has a period of zero minutes");
/*  76 */       stopTimer();
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     long delay = calculateInitialDelay(notifications);
/*  81 */     long period = calculateOptimalPeriod(notifications, delay);
/*     */     
/*  83 */     if (this.timer == null) {
/*  84 */       this.timer = new Timer();
/*     */     }
/*     */     
/*  87 */     if (this.notificationTask == null) {
/*  88 */       this.notificationTask = new NotificationTask(notifications, delay, period);
/*     */     } else {
/*  90 */       this.notificationTask = this.notificationTask.reset(notifications, period);
/*     */     } 
/*     */     
/*  93 */     this.timer.scheduleAtFixedRate(this.notificationTask, TimeUnit.MINUTES.toMillis(delay), TimeUnit.MINUTES.toMillis(period));
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  98 */     stopTimer();
/*     */   }
/*     */   
/*     */   private void stopTimer() {
/* 102 */     if (this.timer != null) {
/* 103 */       this.timer.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   private long calculateOptimalPeriod(List<Notification> notifications, long initialDelay) {
/* 108 */     return notifications.stream()
/* 109 */       .mapToLong(c -> {
/*     */           long delayPeriods = c.delay - initialDelay;
/*     */           
/*     */           return LongMath.gcd(delayPeriods, c.period);
/* 113 */         }).reduce(LongMath::gcd)
/* 114 */       .orElseThrow(() -> new IllegalStateException("Empty notifications from: " + String.valueOf(this.notifications)));
/*     */   }
/*     */   
/*     */   private long calculateInitialDelay(List<Notification> notifications) {
/* 118 */     return notifications.stream()
/* 119 */       .mapToLong(c -> c.delay)
/* 120 */       .min()
/* 121 */       .orElse(0L);
/*     */   }
/*     */   public static final class Notification extends Record { private final long delay; private final long period; private final String title; private final String message;
/* 124 */     public String message() { return this.message; } public String title() { return this.title; } public long period() { return this.period; } public long delay() { return this.delay; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/PeriodicNotificationManager$Notification;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #124	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/PeriodicNotificationManager$Notification;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public Notification(long delay, long period, String title, String message) {
/* 126 */       this.delay = (delay != 0L) ? delay : period;
/* 127 */       this.period = period;
/* 128 */       this.title = title;
/* 129 */       this.message = message;
/*     */     } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/PeriodicNotificationManager$Notification;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #124	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/PeriodicNotificationManager$Notification;
/*     */     } public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/PeriodicNotificationManager$Notification;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #124	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/PeriodicNotificationManager$Notification;
/* 134 */     } } private static class NotificationTask extends TimerTask { private final Minecraft minecraft = Minecraft.getInstance();
/*     */     
/*     */     private final List<PeriodicNotificationManager.Notification> notifications;
/*     */     
/*     */     private final long period;
/*     */     private final AtomicLong elapsed;
/*     */     
/*     */     public NotificationTask(List<PeriodicNotificationManager.Notification> notifications, long elapsed, long period) {
/* 142 */       this.notifications = notifications;
/* 143 */       this.period = period;
/* 144 */       this.elapsed = new AtomicLong(elapsed);
/*     */     }
/*     */     
/*     */     public NotificationTask reset(List<PeriodicNotificationManager.Notification> notifications, long period) {
/* 148 */       cancel();
/*     */       
/* 150 */       return new NotificationTask(notifications, this.elapsed.get(), period);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 155 */       long currentMinute = this.elapsed.getAndAdd(this.period);
/* 156 */       long nextMinute = this.elapsed.get();
/*     */       
/* 158 */       for (PeriodicNotificationManager.Notification notification : this.notifications) {
/* 159 */         if (currentMinute < notification.delay) {
/*     */           continue;
/*     */         }
/*     */         
/* 163 */         long elapsedPeriods = currentMinute / notification.period;
/* 164 */         long currentPeriods = nextMinute / notification.period;
/*     */         
/* 166 */         if (elapsedPeriods != currentPeriods) {
/* 167 */           this.minecraft.execute(() -> SystemToast.add(Minecraft.getInstance().getToastManager(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, (Component)Component.translatable(notification.title, new Object[] { elapsedPeriods }), (Component)Component.translatable(notification.message, new Object[] { elapsedPeriods })));
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/PeriodicNotificationManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */