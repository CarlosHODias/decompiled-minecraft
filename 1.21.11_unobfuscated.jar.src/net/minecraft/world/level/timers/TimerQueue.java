/*     */ package net.minecraft.world.level.timers;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.Table;
/*     */ import com.google.common.primitives.UnsignedLong;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TimerQueue<T> {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final String CALLBACK_DATA_TAG = "Callback";
/*     */   private static final String TIMER_NAME_TAG = "Name";
/*     */   private static final String TIMER_TRIGGER_TIME_TAG = "TriggerTime";
/*     */   private final TimerCallbacks<T> callbacksRegistry;
/*     */   
/*     */   public static class Event<T> { public final long triggerTime;
/*     */     public final UnsignedLong sequentialId;
/*     */     public final String id;
/*     */     public final TimerCallback<T> callback;
/*     */     
/*     */     private Event(long triggerTime, UnsignedLong sequentialId, String id, TimerCallback<T> callback) {
/*  35 */       this.triggerTime = triggerTime;
/*  36 */       this.sequentialId = sequentialId;
/*  37 */       this.id = id;
/*  38 */       this.callback = callback;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static <T> Comparator<Event<T>> createComparator() {
/*  43 */     return Comparator.comparingLong(l -> l.triggerTime).thenComparing(l -> l.sequentialId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  48 */   private final Queue<Event<T>> queue = new PriorityQueue<>((Comparator)createComparator());
/*     */   
/*  50 */   private UnsignedLong sequentialId = UnsignedLong.ZERO;
/*     */   
/*  52 */   private final Table<String, Long, Event<T>> events = (Table<String, Long, Event<T>>)HashBasedTable.create();
/*     */   
/*     */   public TimerQueue(TimerCallbacks<T> callbacksRegistry, Stream<? extends Dynamic<?>> eventData) {
/*  55 */     this(callbacksRegistry);
/*  56 */     this.queue.clear();
/*  57 */     this.events.clear();
/*  58 */     this.sequentialId = UnsignedLong.ZERO;
/*     */     
/*  60 */     eventData.forEach(input -> {
/*     */           Tag tag = (Tag)input.convert((DynamicOps)NbtOps.INSTANCE).getValue();
/*     */           if (tag instanceof CompoundTag) {
/*     */             CompoundTag compoundTag = (CompoundTag)tag;
/*     */             loadEvent(compoundTag);
/*     */           } else {
/*     */             LOGGER.warn("Invalid format of events: {}", tag);
/*     */           } 
/*     */         });
/*     */   }
/*     */   public TimerQueue(TimerCallbacks<T> callbacksRegistry) {
/*  71 */     this.callbacksRegistry = callbacksRegistry;
/*     */   }
/*     */   
/*     */   public void tick(T context, long currentTick) {
/*     */     while (true) {
/*  76 */       Event<T> event = this.queue.peek();
/*  77 */       if (event == null || event.triggerTime > currentTick) {
/*     */         break;
/*     */       }
/*     */       
/*  81 */       this.queue.remove();
/*  82 */       this.events.remove(event.id, currentTick);
/*     */       
/*  84 */       event.callback.handle(context, this, currentTick);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void schedule(String id, long time, TimerCallback<T> callback) {
/*  89 */     if (this.events.contains(id, time)) {
/*     */       return;
/*     */     }
/*  92 */     this.sequentialId = this.sequentialId.plus(UnsignedLong.ONE);
/*  93 */     Event<T> newEvent = new Event<>(time, this.sequentialId, id, callback);
/*  94 */     this.events.put(id, time, newEvent);
/*  95 */     this.queue.add(newEvent);
/*     */   }
/*     */   
/*     */   public int remove(String id) {
/*  99 */     Collection<Event<T>> eventsToRemove = this.events.row(id).values();
/* 100 */     Objects.requireNonNull(this.queue); eventsToRemove.forEach(this.queue::remove);
/* 101 */     int size = eventsToRemove.size();
/* 102 */     eventsToRemove.clear();
/* 103 */     return size;
/*     */   }
/*     */   
/*     */   public Set<String> getEventsIds() {
/* 107 */     return Collections.unmodifiableSet(this.events.rowKeySet());
/*     */   }
/*     */   
/*     */   private void loadEvent(CompoundTag tag) {
/* 111 */     TimerCallback<T> callback = tag.read("Callback", this.callbacksRegistry.codec()).orElse(null);
/* 112 */     if (callback != null) {
/* 113 */       String id = tag.getStringOr("Name", "");
/* 114 */       long time = tag.getLongOr("TriggerTime", 0L);
/* 115 */       schedule(id, time, callback);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompoundTag storeEvent(Event<T> event) {
/* 120 */     CompoundTag result = new CompoundTag();
/* 121 */     result.putString("Name", event.id);
/* 122 */     result.putLong("TriggerTime", event.triggerTime);
/* 123 */     result.store("Callback", this.callbacksRegistry.codec(), event.callback);
/* 124 */     return result;
/*     */   }
/*     */   
/*     */   public ListTag store() {
/* 128 */     ListTag result = new ListTag();
/* 129 */     Objects.requireNonNull(result); this.queue.stream().sorted(createComparator()).map(this::storeEvent).forEach(result::add);
/* 130 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/timers/TimerQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */