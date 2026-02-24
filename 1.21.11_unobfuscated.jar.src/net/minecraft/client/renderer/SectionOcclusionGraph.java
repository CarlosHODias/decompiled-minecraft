/*     */ package net.minecraft.client.renderer;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
/*     */ import net.minecraft.client.renderer.chunk.SectionMesh;
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ChunkTrackingView;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector3d;
/*     */ import org.joml.Vector3dc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SectionOcclusionGraph {
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  45 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private static final int MINIMUM_ADVANCED_CULLING_DISTANCE = 60;
/*  48 */   private static final int MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE = SectionPos.blockToSectionCoord(60);
/*     */   
/*  50 */   private static final double CEILED_SECTION_DIAGONAL = Math.ceil(Math.sqrt(3.0D) * 16.0D);
/*     */   
/*     */   private boolean needsFullUpdate = true;
/*     */   
/*     */   private Future<?> fullUpdateTask;
/*     */   private ViewArea viewArea;
/*  56 */   private final AtomicReference<GraphState> currentGraph = new AtomicReference<>();
/*  57 */   private final AtomicReference<GraphEvents> nextGraphEvents = new AtomicReference<>();
/*     */   
/*  59 */   private final AtomicBoolean needsFrustumUpdate = new AtomicBoolean(false);
/*     */   
/*     */   public void waitAndReset(ViewArea viewArea) {
/*  62 */     if (this.fullUpdateTask != null) {
/*     */       try {
/*  64 */         this.fullUpdateTask.get();
/*  65 */         this.fullUpdateTask = null;
/*  66 */       } catch (Exception e) {
/*  67 */         LOGGER.warn("Full update failed", e);
/*     */       } 
/*     */     }
/*  70 */     this.viewArea = viewArea;
/*  71 */     if (viewArea != null) {
/*  72 */       this.currentGraph.set(new GraphState(viewArea));
/*  73 */       invalidate();
/*     */     } else {
/*  75 */       this.currentGraph.set(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void invalidate() {
/*  80 */     this.needsFullUpdate = true;
/*     */   }
/*     */   
/*     */   public void addSectionsInFrustum(Frustum frustum, List<SectionRenderDispatcher.RenderSection> visibleSections, List<SectionRenderDispatcher.RenderSection> nearbyVisibleSection) {
/*  84 */     (((GraphState)this.currentGraph.get()).storage()).sectionTree.visitNodes((node, fullyVisible, depth, isClose) -> { SectionRenderDispatcher.RenderSection renderSection = node.getSection(); if (renderSection != null) { visibleSections.add(renderSection); if (isClose) nearbyVisibleSection.add(renderSection);  }  }, frustum, 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean consumeFrustumUpdate() {
/*  96 */     return this.needsFrustumUpdate.compareAndSet(true, false);
/*     */   }
/*     */   
/*     */   public void onChunkReadyToRender(ChunkPos pos) {
/* 100 */     GraphEvents nextEvents = this.nextGraphEvents.get();
/* 101 */     if (nextEvents != null) {
/* 102 */       addNeighbors(nextEvents, pos);
/*     */     }
/* 104 */     GraphEvents events = ((GraphState)this.currentGraph.get()).events;
/*     */     
/* 106 */     if (events != nextEvents) {
/* 107 */       addNeighbors(events, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void schedulePropagationFrom(SectionRenderDispatcher.RenderSection section) {
/* 112 */     GraphEvents nextEvents = this.nextGraphEvents.get();
/* 113 */     if (nextEvents != null) {
/* 114 */       nextEvents.sectionsToPropagateFrom.add(section);
/*     */     }
/* 116 */     GraphEvents events = ((GraphState)this.currentGraph.get()).events;
/*     */     
/* 118 */     if (events != nextEvents) {
/* 119 */       events.sectionsToPropagateFrom.add(section);
/*     */     }
/*     */   }
/*     */   
/*     */   public void update(boolean smartCull, Camera camera, Frustum frustum, List<SectionRenderDispatcher.RenderSection> visibleSections, LongOpenHashSet loadedEmptySections) {
/* 124 */     Vec3 cameraPos = camera.position();
/* 125 */     if (this.needsFullUpdate && (this.fullUpdateTask == null || this.fullUpdateTask.isDone())) {
/* 126 */       scheduleFullUpdate(smartCull, camera, cameraPos, loadedEmptySections);
/*     */     }
/* 128 */     runPartialUpdate(smartCull, frustum, visibleSections, cameraPos, loadedEmptySections);
/*     */   }
/*     */   
/*     */   private void scheduleFullUpdate(boolean smartCull, Camera camera, Vec3 cameraPos, LongOpenHashSet loadedEmptySections) {
/* 132 */     this.needsFullUpdate = false;
/* 133 */     LongOpenHashSet emptySections = loadedEmptySections.clone();
/* 134 */     this.fullUpdateTask = CompletableFuture.runAsync(() -> {
/*     */           GraphState newState = new GraphState(this.viewArea);
/*     */           this.nextGraphEvents.set(newState.events);
/*     */           Queue<Node> queue = Queues.newArrayDeque();
/*     */           initializeQueueForFullUpdate(camera, queue);
/*     */           queue.forEach(());
/*     */           runUpdates(newState.storage, cameraPos, queue, smartCull, (), emptySections);
/*     */           this.currentGraph.set(newState);
/*     */           this.nextGraphEvents.set(null);
/*     */           this.needsFrustumUpdate.set(true);
/* 144 */         }, (Executor)Util.backgroundExecutor());
/*     */   }
/*     */   
/*     */   private void runPartialUpdate(boolean smartCull, Frustum frustum, List<SectionRenderDispatcher.RenderSection> visibleSections, Vec3 cameraPos, LongOpenHashSet loadedEmptySections) {
/* 148 */     GraphState state = this.currentGraph.get();
/* 149 */     queueSectionsWithNewNeighbors(state);
/* 150 */     if (!state.events.sectionsToPropagateFrom.isEmpty()) {
/* 151 */       Queue<Node> queue = Queues.newArrayDeque();
/* 152 */       while (!state.events.sectionsToPropagateFrom.isEmpty()) {
/* 153 */         SectionRenderDispatcher.RenderSection renderSection = state.events.sectionsToPropagateFrom.poll();
/* 154 */         Node node = state.storage.sectionToNodeMap.get(renderSection);
/*     */         
/* 156 */         if (node != null && node.section == renderSection) {
/* 157 */           queue.add(node);
/*     */         }
/*     */       } 
/* 160 */       Frustum offsetFrustum = LevelRenderer.offsetFrustum(frustum);
/*     */       Consumer<SectionRenderDispatcher.RenderSection> onSectionAdded = section -> {
/*     */           if (offsetFrustum.isVisible(offsetFrustum.getBoundingBox())) {
/*     */             this.needsFrustumUpdate.set(true);
/*     */           }
/*     */         };
/* 166 */       runUpdates(state.storage, cameraPos, queue, smartCull, onSectionAdded, loadedEmptySections);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void queueSectionsWithNewNeighbors(GraphState state) {
/* 171 */     for (LongIterator iterator = state.events.chunksWhichReceivedNeighbors.iterator(); iterator.hasNext(); ) {
/* 172 */       long chunkWithNewNeighbor = iterator.nextLong();
/* 173 */       List<SectionRenderDispatcher.RenderSection> renderSections = (List<SectionRenderDispatcher.RenderSection>)state.storage.chunksWaitingForNeighbors.get(chunkWithNewNeighbor);
/* 174 */       if (renderSections != null && ((SectionRenderDispatcher.RenderSection)renderSections.get(0)).hasAllNeighbors()) {
/* 175 */         state.events.sectionsToPropagateFrom.addAll(renderSections);
/* 176 */         state.storage.chunksWaitingForNeighbors.remove(chunkWithNewNeighbor);
/*     */       } 
/*     */     } 
/* 179 */     state.events.chunksWhichReceivedNeighbors.clear();
/*     */   }
/*     */   
/*     */   private void addNeighbors(GraphEvents events, ChunkPos pos) {
/* 183 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x - 1, pos.z));
/* 184 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x, pos.z - 1));
/* 185 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x + 1, pos.z));
/* 186 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x, pos.z + 1));
/* 187 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x - 1, pos.z - 1));
/* 188 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x - 1, pos.z + 1));
/* 189 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x + 1, pos.z - 1));
/* 190 */     events.chunksWhichReceivedNeighbors.add(ChunkPos.asLong(pos.x + 1, pos.z + 1));
/*     */   }
/*     */   
/*     */   private void initializeQueueForFullUpdate(Camera camera, Queue<Node> queue) {
/* 194 */     BlockPos cameraPosition = camera.blockPosition();
/* 195 */     long cameraSectionNode = SectionPos.asLong(cameraPosition);
/* 196 */     int cameraSectionY = SectionPos.y(cameraSectionNode);
/* 197 */     SectionRenderDispatcher.RenderSection cameraSection = this.viewArea.getRenderSection(cameraSectionNode);
/*     */     
/* 199 */     if (cameraSection == null) {
/*     */       
/* 201 */       LevelHeightAccessor heightAccessor = this.viewArea.getLevelHeightAccessor();
/* 202 */       boolean isBelowTheWorld = (cameraSectionY < heightAccessor.getMinSectionY());
/* 203 */       int sectionY = isBelowTheWorld ? heightAccessor.getMinSectionY() : heightAccessor.getMaxSectionY();
/*     */       
/* 205 */       int viewDistance = this.viewArea.getViewDistance();
/* 206 */       List<Node> toAdd = com.google.common.collect.Lists.newArrayList();
/* 207 */       int cameraSectionX = SectionPos.x(cameraSectionNode);
/* 208 */       int cameraSectionZ = SectionPos.z(cameraSectionNode);
/* 209 */       for (int sectionX = -viewDistance; sectionX <= viewDistance; sectionX++) {
/* 210 */         for (int sectionZ = -viewDistance; sectionZ <= viewDistance; sectionZ++) {
/* 211 */           SectionRenderDispatcher.RenderSection renderSectionAt = this.viewArea.getRenderSection(SectionPos.asLong(sectionX + cameraSectionX, sectionY, sectionZ + cameraSectionZ));
/* 212 */           if (renderSectionAt != null && isInViewDistance(cameraSectionNode, renderSectionAt.getSectionNode())) {
/* 213 */             Direction sourceDirection = isBelowTheWorld ? Direction.UP : Direction.DOWN;
/* 214 */             Node node = new Node(renderSectionAt, sourceDirection, 0);
/* 215 */             node.setDirections(node.directions, sourceDirection);
/* 216 */             if (sectionX > 0) {
/* 217 */               node.setDirections(node.directions, Direction.EAST);
/* 218 */             } else if (sectionX < 0) {
/* 219 */               node.setDirections(node.directions, Direction.WEST);
/*     */             } 
/* 221 */             if (sectionZ > 0) {
/* 222 */               node.setDirections(node.directions, Direction.SOUTH);
/* 223 */             } else if (sectionZ < 0) {
/* 224 */               node.setDirections(node.directions, Direction.NORTH);
/*     */             } 
/* 226 */             toAdd.add(node);
/*     */           } 
/*     */         } 
/*     */       } 
/* 230 */       toAdd.sort(Comparator.comparingDouble(c -> cameraPosition.distSqr((Vec3i)SectionPos.of(c.section.getSectionNode()).center())));
/* 231 */       queue.addAll(toAdd);
/*     */     } else {
/* 233 */       queue.add(new Node(cameraSection, null, 0));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void runUpdates(GraphStorage storage, Vec3 cameraPos, Queue<Node> queue, boolean smartCull, Consumer<SectionRenderDispatcher.RenderSection> onSectionAdded, LongOpenHashSet emptySections) {
/* 238 */     SectionPos cameraSectionPos = SectionPos.of((Position)cameraPos);
/* 239 */     long cameraSectionNode = cameraSectionPos.asLong();
/* 240 */     BlockPos cameraSectionCenter = cameraSectionPos.center();
/*     */     
/* 242 */     while (!queue.isEmpty()) {
/* 243 */       Node node = queue.poll();
/* 244 */       SectionRenderDispatcher.RenderSection currentSection = node.section;
/*     */       
/* 246 */       if (!emptySections.contains(node.section.getSectionNode())) {
/* 247 */         if (storage.sectionTree.add(node.section)) {
/* 248 */           onSectionAdded.accept(node.section);
/*     */         }
/*     */       } else {
/*     */         
/* 252 */         node.section.sectionMesh.compareAndSet(CompiledSectionMesh.UNCOMPILED, CompiledSectionMesh.EMPTY);
/*     */       } 
/*     */       
/* 255 */       long sectionNode = currentSection.getSectionNode();
/* 256 */       boolean distantFromCamera = (
/* 257 */         Math.abs(SectionPos.x(sectionNode) - cameraSectionPos.x()) > MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE || 
/* 258 */         Math.abs(SectionPos.y(sectionNode) - cameraSectionPos.y()) > MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE || 
/* 259 */         Math.abs(SectionPos.z(sectionNode) - cameraSectionPos.z()) > MINIMUM_ADVANCED_CULLING_SECTION_DISTANCE);
/*     */       
/* 261 */       for (Direction direction : DIRECTIONS) {
/* 262 */         SectionRenderDispatcher.RenderSection renderSectionAt = getRelativeFrom(cameraSectionNode, currentSection, direction);
/*     */         
/* 264 */         if (renderSectionAt == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 268 */         if (smartCull && node.hasDirection(direction.getOpposite())) {
/*     */           continue;
/*     */         }
/*     */         
/* 272 */         if (smartCull && node.hasSourceDirections()) {
/* 273 */           SectionMesh sectionMesh = currentSection.getSectionMesh();
/*     */           boolean visible = false;
/* 275 */           for (int i = 0; i < DIRECTIONS.length; i++) {
/* 276 */             if (node.hasSourceDirection(i) && sectionMesh.facesCanSeeEachother(DIRECTIONS[i].getOpposite(), direction)) {
/* 277 */               visible = true;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 282 */           if (!visible) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */         
/* 287 */         if (smartCull && distantFromCamera) {
/* 288 */           int renderSectionOriginX = SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode));
/* 289 */           int renderSectionOriginY = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
/* 290 */           int renderSectionOriginZ = SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode));
/* 291 */           boolean maxX = (direction.getAxis() == Direction.Axis.X) ? ((cameraSectionCenter.getX() > renderSectionOriginX)) : ((cameraSectionCenter.getX() < renderSectionOriginX));
/* 292 */           boolean maxY = (direction.getAxis() == Direction.Axis.Y) ? ((cameraSectionCenter.getY() > renderSectionOriginY)) : ((cameraSectionCenter.getY() < renderSectionOriginY));
/* 293 */           boolean maxZ = (direction.getAxis() == Direction.Axis.Z) ? ((cameraSectionCenter.getZ() > renderSectionOriginZ)) : ((cameraSectionCenter.getZ() < renderSectionOriginZ));
/* 294 */           Vector3d checkPos = new Vector3d((renderSectionOriginX + (
/* 295 */               maxX ? 16 : 0)), (renderSectionOriginY + (
/* 296 */               maxY ? 16 : 0)), (renderSectionOriginZ + (
/* 297 */               maxZ ? 16 : 0)));
/*     */           
/* 299 */           Vector3d step = new Vector3d(cameraPos.x, cameraPos.y, cameraPos.z).sub((Vector3dc)checkPos).normalize().mul(CEILED_SECTION_DIAGONAL);
/*     */           boolean visible = true;
/* 301 */           while (checkPos.distanceSquared(cameraPos.x, cameraPos.y, cameraPos.z) > 3600.0D) {
/* 302 */             checkPos.add((Vector3dc)step);
/* 303 */             LevelHeightAccessor heightAccessor = this.viewArea.getLevelHeightAccessor();
/* 304 */             if (checkPos.y > heightAccessor.getMaxY() || checkPos.y < heightAccessor.getMinY()) {
/*     */               break;
/*     */             }
/* 307 */             SectionRenderDispatcher.RenderSection checkSection = this.viewArea.getRenderSectionAt(BlockPos.containing(checkPos.x, checkPos.y, checkPos.z));
/* 308 */             if (checkSection == null || storage.sectionToNodeMap.get(checkSection) == null) {
/* 309 */               visible = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 313 */           if (!visible) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */         
/* 318 */         Node existingNode = storage.sectionToNodeMap.get(renderSectionAt);
/* 319 */         if (existingNode != null) {
/*     */           
/* 321 */           existingNode.addSourceDirection(direction);
/*     */         }
/*     */         else {
/*     */           
/* 325 */           Node newNode = new Node(renderSectionAt, direction, node.step + 1);
/* 326 */           newNode.setDirections(node.directions, direction);
/*     */           
/* 328 */           if (renderSectionAt.hasAllNeighbors()) {
/* 329 */             queue.add(newNode);
/* 330 */             storage.sectionToNodeMap.put(renderSectionAt, newNode);
/*     */           }
/* 332 */           else if (isInViewDistance(cameraSectionNode, renderSectionAt.getSectionNode())) {
/* 333 */             storage.sectionToNodeMap.put(renderSectionAt, newNode);
/* 334 */             long chunkNode = SectionPos.sectionToChunk(renderSectionAt.getSectionNode());
/* 335 */             ((List<SectionRenderDispatcher.RenderSection>)storage.chunksWaitingForNeighbors.computeIfAbsent(chunkNode, l -> new ArrayList())).add(renderSectionAt);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isInViewDistance(long cameraSectionNode, long sectionNode) {
/* 343 */     return ChunkTrackingView.isInViewDistance(SectionPos.x(cameraSectionNode), SectionPos.z(cameraSectionNode), this.viewArea.getViewDistance(), SectionPos.x(sectionNode), SectionPos.z(sectionNode));
/*     */   }
/*     */   
/*     */   private SectionRenderDispatcher.RenderSection getRelativeFrom(long cameraSectionNode, SectionRenderDispatcher.RenderSection renderSection, Direction direction) {
/* 347 */     long relative = renderSection.getNeighborSectionNode(direction);
/*     */     
/* 349 */     if (!isInViewDistance(cameraSectionNode, relative)) {
/* 350 */       return null;
/*     */     }
/* 352 */     if (Mth.abs(SectionPos.y(cameraSectionNode) - SectionPos.y(relative)) > this.viewArea.getViewDistance()) {
/* 353 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 357 */     return this.viewArea.getRenderSection(relative);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Node getNode(SectionRenderDispatcher.RenderSection section) {
/* 362 */     return ((GraphState)this.currentGraph.get()).storage.sectionToNodeMap.get(section);
/*     */   }
/*     */   private static final class GraphState extends Record { private final SectionOcclusionGraph.GraphStorage storage; private final SectionOcclusionGraph.GraphEvents events;
/* 365 */     public SectionOcclusionGraph.GraphEvents events() { return this.events; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #365	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/* 365 */       //   0	8	1	o	Ljava/lang/Object; } public SectionOcclusionGraph.GraphStorage storage() { return this.storage; } private GraphState(SectionOcclusionGraph.GraphStorage storage, SectionOcclusionGraph.GraphEvents events) { this.storage = storage; this.events = events; } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #365	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */     } public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #365	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */     } private GraphState(ViewArea viewArea) {
/* 370 */       this(new SectionOcclusionGraph.GraphStorage(viewArea), new SectionOcclusionGraph.GraphEvents());
/*     */     } }
/*     */   private static final class GraphEvents extends Record { private final LongSet chunksWhichReceivedNeighbors; private final BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom;
/*     */     
/* 374 */     public BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom() { return this.sectionsToPropagateFrom; } public LongSet chunksWhichReceivedNeighbors() { return this.chunksWhichReceivedNeighbors; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphEvents;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphEvents;
/* 374 */       //   0	8	1	o	Ljava/lang/Object; } private GraphEvents(LongSet chunksWhichReceivedNeighbors, BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom) { this.chunksWhichReceivedNeighbors = chunksWhichReceivedNeighbors; this.sectionsToPropagateFrom = sectionsToPropagateFrom; } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphEvents;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphEvents;
/*     */     } public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphEvents;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphEvents;
/*     */     } private GraphEvents() {
/* 379 */       this((LongSet)new LongOpenHashSet(), new LinkedBlockingQueue<>());
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class GraphStorage {
/*     */     public final SectionOcclusionGraph.SectionToNodeMap sectionToNodeMap;
/*     */     public final Octree sectionTree;
/*     */     public final Long2ObjectMap<List<SectionRenderDispatcher.RenderSection>> chunksWaitingForNeighbors;
/*     */     
/*     */     public GraphStorage(ViewArea viewArea) {
/* 389 */       this.sectionToNodeMap = new SectionOcclusionGraph.SectionToNodeMap(viewArea.sections.length);
/* 390 */       this.sectionTree = new Octree(viewArea.getCameraSectionPos(), viewArea.getViewDistance(), viewArea.sectionGridSizeY, viewArea.level.getMinY());
/* 391 */       this.chunksWaitingForNeighbors = (Long2ObjectMap<List<SectionRenderDispatcher.RenderSection>>)new Long2ObjectOpenHashMap();
/*     */     }
/*     */   }
/*     */   
/*     */   public Octree getOctree() {
/* 396 */     return ((GraphState)this.currentGraph.get()).storage.sectionTree;
/*     */   }
/*     */   
/*     */   private static class SectionToNodeMap {
/*     */     private final SectionOcclusionGraph.Node[] nodes;
/*     */     
/*     */     private SectionToNodeMap(int sectionCount) {
/* 403 */       this.nodes = new SectionOcclusionGraph.Node[sectionCount];
/*     */     }
/*     */     
/*     */     public void put(SectionRenderDispatcher.RenderSection renderSection, SectionOcclusionGraph.Node node) {
/* 407 */       this.nodes[renderSection.index] = node;
/*     */     }
/*     */     
/*     */     public SectionOcclusionGraph.Node get(SectionRenderDispatcher.RenderSection renderSection) {
/* 411 */       int index = renderSection.index;
/* 412 */       if (index < 0 || index >= this.nodes.length) {
/* 413 */         return null;
/*     */       }
/* 415 */       return this.nodes[index];
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public static class Node {
/*     */     @VisibleForDebug
/*     */     protected final SectionRenderDispatcher.RenderSection section;
/*     */     private byte sourceDirections;
/*     */     private byte directions;
/*     */     @VisibleForDebug
/*     */     public final int step;
/*     */     
/*     */     private Node(SectionRenderDispatcher.RenderSection section, Direction sourceDirection, int step) {
/* 429 */       this.section = section;
/* 430 */       if (sourceDirection != null) {
/* 431 */         addSourceDirection(sourceDirection);
/*     */       }
/* 433 */       this.step = step;
/*     */     }
/*     */     
/*     */     private void setDirections(byte oldDirections, Direction direction) {
/* 437 */       this.directions = (byte)(this.directions | oldDirections | 1 << direction.ordinal());
/*     */     }
/*     */     
/*     */     private boolean hasDirection(Direction direction) {
/* 441 */       return ((this.directions & 1 << direction.ordinal()) > 0);
/*     */     }
/*     */     
/*     */     private void addSourceDirection(Direction direction) {
/* 445 */       this.sourceDirections = (byte)(this.sourceDirections | this.sourceDirections | 1 << direction.ordinal());
/*     */     }
/*     */     
/*     */     @VisibleForDebug
/*     */     public boolean hasSourceDirection(int directionOrdinal) {
/* 450 */       return ((this.sourceDirections & 1 << directionOrdinal) > 0);
/*     */     }
/*     */     
/*     */     private boolean hasSourceDirections() {
/* 454 */       return (this.sourceDirections != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 459 */       return Long.hashCode(this.section.getSectionNode());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 464 */       if (!(obj instanceof Node)) {
/* 465 */         return false;
/*     */       }
/* 467 */       Node other = (Node)obj;
/* 468 */       return (this.section.getSectionNode() == other.section.getSectionNode());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SectionOcclusionGraph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */