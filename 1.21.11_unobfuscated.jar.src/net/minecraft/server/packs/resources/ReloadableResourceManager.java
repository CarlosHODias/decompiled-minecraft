/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.util.Unit;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadableResourceManager
/*    */   implements AutoCloseable, ResourceManager {
/* 22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private CloseableResourceManager resources;
/* 25 */   private final List<PreparableReloadListener> listeners = Lists.newArrayList();
/*    */   private final PackType type;
/*    */   
/*    */   public ReloadableResourceManager(PackType type) {
/* 29 */     this.type = type;
/* 30 */     this.resources = new MultiPackResourceManager(type, List.of());
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 35 */     this.resources.close();
/*    */   }
/*    */   
/*    */   public void registerReloadListener(PreparableReloadListener listener) {
/* 39 */     this.listeners.add(listener);
/*    */   }
/*    */   
/*    */   public ReloadInstance createReload(Executor backgroundExecutor, Executor mainThreadExecutor, CompletableFuture<Unit> initialTask, List<PackResources> resourcePacks) {
/* 43 */     LOGGER.info("Reloading ResourceManager: {}", LogUtils.defer(() -> resourcePacks.stream().map(PackResources::packId).collect(Collectors.joining(", "))));
/*    */     
/* 45 */     this.resources.close();
/* 46 */     this.resources = new MultiPackResourceManager(this.type, resourcePacks);
/* 47 */     return SimpleReloadInstance.create(this.resources, this.listeners, backgroundExecutor, mainThreadExecutor, initialTask, LOGGER.isDebugEnabled());
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Resource> getResource(Identifier location) {
/* 52 */     return this.resources.getResource(location);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getNamespaces() {
/* 57 */     return this.resources.getNamespaces();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Resource> getResourceStack(Identifier location) {
/* 62 */     return this.resources.getResourceStack(location);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filenameFilter) {
/* 67 */     return this.resources.listResources(directory, filenameFilter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) {
/* 72 */     return this.resources.listResourceStacks(directory, filter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<PackResources> listPacks() {
/* 77 */     return this.resources.listPacks();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/ReloadableResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */