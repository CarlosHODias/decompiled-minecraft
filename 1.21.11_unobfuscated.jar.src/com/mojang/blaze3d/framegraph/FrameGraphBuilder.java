/*     */ package com.mojang.blaze3d.framegraph;
/*     */ 
/*     */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*     */ import com.mojang.blaze3d.resource.ResourceDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ public class FrameGraphBuilder
/*     */ {
/*  18 */   private final List<InternalVirtualResource<?>> internalResources = new ArrayList<>();
/*  19 */   private final List<ExternalResource<?>> externalResources = new ArrayList<>();
/*  20 */   private final List<Pass> passes = new ArrayList<>();
/*     */   
/*     */   public FramePass addPass(String name) {
/*  23 */     Pass pass = new Pass(this.passes.size(), name);
/*  24 */     this.passes.add(pass);
/*  25 */     return pass;
/*     */   }
/*     */   
/*     */   public <T> ResourceHandle<T> importExternal(String name, T resource) {
/*  29 */     ExternalResource<T> holder = new ExternalResource<>(name, null, resource);
/*  30 */     this.externalResources.add(holder);
/*  31 */     return holder.handle;
/*     */   }
/*     */   
/*     */   public <T> ResourceHandle<T> createInternal(String name, ResourceDescriptor<T> descriptor) {
/*  35 */     return (createInternalResource(name, descriptor, null)).handle;
/*     */   }
/*     */   
/*     */   private <T> InternalVirtualResource<T> createInternalResource(String name, ResourceDescriptor<T> descriptor, Pass createdBy) {
/*  39 */     int id = this.internalResources.size();
/*  40 */     InternalVirtualResource<T> resource = new InternalVirtualResource<>(id, name, createdBy, descriptor);
/*  41 */     this.internalResources.add(resource);
/*  42 */     return resource;
/*     */   }
/*     */   
/*     */   public void execute(GraphicsResourceAllocator resourceAllocator) {
/*  46 */     execute(resourceAllocator, Inspector.NONE);
/*     */   }
/*     */   
/*     */   public void execute(GraphicsResourceAllocator resourceAllocator, Inspector inspector) {
/*  50 */     BitSet passesToKeep = identifyPassesToKeep();
/*     */     
/*  52 */     List<Pass> passesInOrder = new ArrayList<>(passesToKeep.cardinality());
/*  53 */     BitSet visiting = new BitSet(this.passes.size());
/*  54 */     for (Pass pass : this.passes) {
/*  55 */       resolvePassOrder(pass, passesToKeep, visiting, passesInOrder);
/*     */     }
/*     */     
/*  58 */     assignResourceLifetimes(passesInOrder);
/*     */     
/*  60 */     for (Pass pass : passesInOrder) {
/*  61 */       for (InternalVirtualResource<?> resource : pass.resourcesToAcquire) {
/*  62 */         inspector.acquireResource(resource.name);
/*  63 */         resource.acquire(resourceAllocator);
/*     */       } 
/*     */       
/*  66 */       inspector.beforeExecutePass(pass.name);
/*  67 */       pass.task.run();
/*  68 */       inspector.afterExecutePass(pass.name);
/*     */       
/*  70 */       for (int id = pass.resourcesToRelease.nextSetBit(0); id >= 0; id = pass.resourcesToRelease.nextSetBit(id + 1)) {
/*  71 */         InternalVirtualResource<?> resource = this.internalResources.get(id);
/*  72 */         inspector.releaseResource(resource.name);
/*  73 */         resource.release(resourceAllocator);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private BitSet identifyPassesToKeep() {
/*  79 */     Deque<Pass> scratchQueue = new ArrayDeque<>(this.passes.size());
/*  80 */     BitSet passesToKeep = new BitSet(this.passes.size());
/*     */ 
/*     */     
/*  83 */     for (VirtualResource<?> resource : this.externalResources) {
/*  84 */       Pass pass = resource.handle.createdBy;
/*  85 */       if (pass != null) {
/*  86 */         discoverAllRequiredPasses(pass, passesToKeep, scratchQueue);
/*     */       }
/*     */     } 
/*  89 */     for (Pass pass : this.passes) {
/*  90 */       if (pass.disableCulling) {
/*  91 */         discoverAllRequiredPasses(pass, passesToKeep, scratchQueue);
/*     */       }
/*     */     } 
/*     */     
/*  95 */     return passesToKeep;
/*     */   }
/*     */   
/*     */   private void discoverAllRequiredPasses(Pass sourcePass, BitSet visited, Deque<Pass> passesToTrace) {
/*  99 */     passesToTrace.add(sourcePass);
/* 100 */     while (!passesToTrace.isEmpty()) {
/* 101 */       Pass pass = passesToTrace.poll();
/* 102 */       if (visited.get(pass.id)) {
/*     */         continue;
/*     */       }
/* 105 */       visited.set(pass.id);
/* 106 */       for (int id = pass.requiredPassIds.nextSetBit(0); id >= 0; id = pass.requiredPassIds.nextSetBit(id + 1)) {
/* 107 */         passesToTrace.add(this.passes.get(id));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resolvePassOrder(Pass pass, BitSet passesToFind, BitSet visiting, List<Pass> output) {
/* 114 */     if (visiting.get(pass.id)) {
/* 115 */       String involvedPasses = visiting.stream()
/* 116 */         .<CharSequence>mapToObj(id -> ((Pass)this.passes.get(id)).name)
/* 117 */         .collect(Collectors.joining(", "));
/* 118 */       throw new IllegalStateException("Frame graph cycle detected between " + involvedPasses);
/*     */     } 
/*     */     
/* 121 */     if (!passesToFind.get(pass.id)) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     visiting.set(pass.id);
/* 126 */     passesToFind.clear(pass.id);
/*     */ 
/*     */     
/* 129 */     for (int id = pass.requiredPassIds.nextSetBit(0); id >= 0; id = pass.requiredPassIds.nextSetBit(id + 1)) {
/* 130 */       resolvePassOrder(this.passes.get(id), passesToFind, visiting, output);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 135 */     for (Handle<?> handle : pass.writesFrom) {
/* 136 */       for (int i = handle.readBy.nextSetBit(0); i >= 0; i = handle.readBy.nextSetBit(i + 1)) {
/* 137 */         if (i != pass.id) {
/* 138 */           resolvePassOrder(this.passes.get(i), passesToFind, visiting, output);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     output.add(pass);
/* 144 */     visiting.clear(pass.id);
/*     */   }
/*     */   
/*     */   private void assignResourceLifetimes(Collection<Pass> passesInOrder) {
/* 148 */     Pass[] lastPassByResource = new Pass[this.internalResources.size()];
/* 149 */     for (Pass pass : passesInOrder) {
/* 150 */       for (int id = pass.requiredResourceIds.nextSetBit(0); id >= 0; id = pass.requiredResourceIds.nextSetBit(id + 1)) {
/* 151 */         InternalVirtualResource<?> resource = this.internalResources.get(id);
/* 152 */         Pass lastPass = lastPassByResource[id];
/* 153 */         lastPassByResource[id] = pass;
/* 154 */         if (lastPass == null) {
/* 155 */           pass.resourcesToAcquire.add(resource);
/*     */         } else {
/* 157 */           lastPass.resourcesToRelease.clear(id);
/*     */         } 
/* 159 */         pass.resourcesToRelease.set(id);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private class Pass implements FramePass {
/*     */     private final int id;
/*     */     private final String name;
/* 167 */     private final List<FrameGraphBuilder.Handle<?>> writesFrom = new ArrayList<>();
/* 168 */     private final BitSet requiredResourceIds = new BitSet();
/* 169 */     private final BitSet requiredPassIds = new BitSet();
/*     */     private Runnable task = () -> {
/*     */       
/*     */       };
/* 173 */     private final List<FrameGraphBuilder.InternalVirtualResource<?>> resourcesToAcquire = new ArrayList<>();
/* 174 */     private final BitSet resourcesToRelease = new BitSet();
/*     */     
/*     */     private boolean disableCulling;
/*     */     
/*     */     public Pass(int id, String name) {
/* 179 */       this.id = id;
/* 180 */       this.name = name;
/*     */     }
/*     */     
/*     */     private <T> void markResourceRequired(FrameGraphBuilder.Handle<T> handle) {
/* 184 */       FrameGraphBuilder.VirtualResource<T> virtualResource = handle.holder; if (virtualResource instanceof FrameGraphBuilder.InternalVirtualResource) { FrameGraphBuilder.InternalVirtualResource<?> resource = (FrameGraphBuilder.InternalVirtualResource)virtualResource;
/* 185 */         this.requiredResourceIds.set(resource.id); }
/*     */     
/*     */     }
/*     */     
/*     */     private void markPassRequired(Pass pass) {
/* 190 */       this.requiredPassIds.set(pass.id);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> ResourceHandle<T> createsInternal(String name, ResourceDescriptor<T> descriptor) {
/* 195 */       FrameGraphBuilder.InternalVirtualResource<T> resource = FrameGraphBuilder.this.createInternalResource(name, descriptor, this);
/* 196 */       this.requiredResourceIds.set(resource.id);
/* 197 */       return resource.handle;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> void reads(ResourceHandle<T> handle) {
/* 202 */       _reads((FrameGraphBuilder.Handle)handle);
/*     */     }
/*     */     
/*     */     private <T> void _reads(FrameGraphBuilder.Handle<T> handle) {
/* 206 */       markResourceRequired(handle);
/*     */       
/* 208 */       if (handle.createdBy != null) {
/* 209 */         markPassRequired(handle.createdBy);
/*     */       }
/* 211 */       handle.readBy.set(this.id);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> ResourceHandle<T> readsAndWrites(ResourceHandle<T> handle) {
/* 216 */       return _readsAndWrites((FrameGraphBuilder.Handle<T>)handle);
/*     */     }
/*     */ 
/*     */     
/*     */     public void requires(FramePass pass) {
/* 221 */       this.requiredPassIds.set(((Pass)pass).id);
/*     */     }
/*     */ 
/*     */     
/*     */     public void disableCulling() {
/* 226 */       this.disableCulling = true;
/*     */     }
/*     */     
/*     */     private <T> FrameGraphBuilder.Handle<T> _readsAndWrites(FrameGraphBuilder.Handle<T> handle) {
/* 230 */       this.writesFrom.add(handle);
/* 231 */       _reads(handle);
/* 232 */       return handle.writeAndAlias(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void executes(Runnable task) {
/* 237 */       this.task = task;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 242 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Handle<T> implements ResourceHandle<T> {
/*     */     private final FrameGraphBuilder.VirtualResource<T> holder;
/*     */     private final int version;
/*     */     private final FrameGraphBuilder.Pass createdBy;
/* 250 */     private final BitSet readBy = new BitSet();
/*     */     private Handle<T> aliasedBy;
/*     */     
/*     */     private Handle(FrameGraphBuilder.VirtualResource<T> holder, int version, FrameGraphBuilder.Pass createdBy) {
/* 254 */       this.holder = holder;
/* 255 */       this.version = version;
/* 256 */       this.createdBy = createdBy;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 261 */       return this.holder.get();
/*     */     }
/*     */     
/*     */     private Handle<T> writeAndAlias(FrameGraphBuilder.Pass pass) {
/* 265 */       if (this.holder.handle != this) {
/* 266 */         throw new IllegalStateException("Handle " + String.valueOf(this) + " is no longer valid, as its contents were moved into " + String.valueOf(this.aliasedBy));
/*     */       }
/* 268 */       Handle<T> newHandle = new Handle(this.holder, this.version + 1, pass);
/* 269 */       this.holder.handle = newHandle;
/* 270 */       this.aliasedBy = newHandle;
/* 271 */       return newHandle;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 276 */       if (this.createdBy != null) {
/* 277 */         return String.valueOf(this.holder) + "#" + String.valueOf(this.holder) + " (from " + this.version + ")";
/*     */       }
/* 279 */       return String.valueOf(this.holder) + "#" + String.valueOf(this.holder);
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class VirtualResource<T> {
/*     */     public final String name;
/*     */     public FrameGraphBuilder.Handle<T> handle;
/*     */     
/*     */     public VirtualResource(String name, FrameGraphBuilder.Pass createdBy) {
/* 288 */       this.name = name;
/* 289 */       this.handle = new FrameGraphBuilder.Handle<>(this, 0, createdBy);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract T get();
/*     */     
/*     */     public String toString() {
/* 296 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InternalVirtualResource<T>
/*     */     extends VirtualResource<T>
/*     */   {
/*     */     private final int id;
/*     */     private final ResourceDescriptor<T> descriptor;
/*     */     private T physicalResource;
/*     */     
/*     */     public InternalVirtualResource(int id, String name, FrameGraphBuilder.Pass createdBy, ResourceDescriptor<T> descriptor) {
/* 308 */       super(name, createdBy);
/* 309 */       this.id = id;
/* 310 */       this.descriptor = descriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 315 */       return Objects.requireNonNull(this.physicalResource, "Resource is not currently available");
/*     */     }
/*     */     
/*     */     public void acquire(GraphicsResourceAllocator allocator) {
/* 319 */       if (this.physicalResource != null) {
/* 320 */         throw new IllegalStateException("Tried to acquire physical resource, but it was already assigned");
/*     */       }
/* 322 */       this.physicalResource = (T)allocator.acquire(this.descriptor);
/*     */     }
/*     */     
/*     */     public void release(GraphicsResourceAllocator allocator) {
/* 326 */       if (this.physicalResource == null) {
/* 327 */         throw new IllegalStateException("Tried to release physical resource that was not allocated");
/*     */       }
/* 329 */       allocator.release(this.descriptor, this.physicalResource);
/* 330 */       this.physicalResource = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ExternalResource<T> extends VirtualResource<T> {
/*     */     private final T resource;
/*     */     
/*     */     public ExternalResource(String name, FrameGraphBuilder.Pass createdBy, T resource) {
/* 338 */       super(name, createdBy);
/* 339 */       this.resource = resource;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 344 */       return this.resource;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Inspector {
/* 349 */     public static final Inspector NONE = new Inspector() {
/*     */       
/*     */       };
/*     */     
/*     */     default void acquireResource(String name) {}
/*     */     
/*     */     default void releaseResource(String name) {}
/*     */     
/*     */     default void beforeExecutePass(String name) {}
/*     */     
/*     */     default void afterExecutePass(String name) {}
/*     */   }
/*     */   
/*     */   class null implements Inspector {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/framegraph/FrameGraphBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */