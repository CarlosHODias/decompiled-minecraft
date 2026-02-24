/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerFunctionLibrary implements PreparableReloadListener {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*  36 */   public static final ResourceKey<Registry<CommandFunction<CommandSourceStack>>> TYPE_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("function"));
/*     */   
/*  38 */   private static final FileToIdConverter LISTER = new FileToIdConverter(Registries.elementsDirPath(TYPE_KEY), ".mcfunction");
/*     */   
/*  40 */   private volatile Map<Identifier, CommandFunction<CommandSourceStack>> functions = (Map<Identifier, CommandFunction<CommandSourceStack>>)ImmutableMap.of(); private final TagLoader<CommandFunction<CommandSourceStack>> tagsLoader; private volatile Map<Identifier, List<CommandFunction<CommandSourceStack>>> tags; private final PermissionSet functionCompilationPermissions; private final CommandDispatcher<CommandSourceStack> dispatcher;
/*  41 */   public ServerFunctionLibrary(PermissionSet functionCompilationPermissions, CommandDispatcher<CommandSourceStack> dispatcher) { this.tagsLoader = new TagLoader((id, required) -> getFunction(id), Registries.tagsDirPath(TYPE_KEY));
/*  42 */     this.tags = Map.of();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.functionCompilationPermissions = functionCompilationPermissions;
/*  65 */     this.dispatcher = dispatcher; } public Optional<CommandFunction<CommandSourceStack>> getFunction(Identifier id) {
/*     */     return Optional.ofNullable(this.functions.get(id));
/*     */   } public Map<Identifier, CommandFunction<CommandSourceStack>> getFunctions() {
/*     */     return this.functions;
/*     */   } public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/*  70 */     ResourceManager manager = currentReload.resourceManager();
/*  71 */     CompletableFuture<Map<Identifier, List<TagLoader.EntryWithSource>>> tags = CompletableFuture.supplyAsync(() -> this.tagsLoader.load(manager), taskExecutor);
/*     */ 
/*     */     
/*  74 */     CompletableFuture<Map<Identifier, CompletableFuture<CommandFunction<CommandSourceStack>>>> functions = CompletableFuture.supplyAsync(() -> LISTER.listMatchingResources(manager), taskExecutor)
/*  75 */       .thenCompose(functionsToLoad -> {
/*     */           Map<Identifier, CompletableFuture<CommandFunction<CommandSourceStack>>> result = Maps.newHashMap();
/*     */ 
/*     */           
/*     */           CommandSourceStack compilationContext = Commands.createCompilationContext(this.functionCompilationPermissions);
/*     */ 
/*     */           
/*     */           for (Map.Entry<Identifier, Resource> entry : (Iterable<Map.Entry<Identifier, Resource>>)taskExecutor.entrySet()) {
/*     */             Identifier resourceId = entry.getKey(), id = LISTER.fileToId(resourceId);
/*     */ 
/*     */             
/*     */             result.put(id, CompletableFuture.supplyAsync((), taskExecutor));
/*     */           } 
/*     */ 
/*     */           
/*     */           CompletableFuture[] arrayOfCompletableFuture = (CompletableFuture[])result.values().toArray((Object[])new CompletableFuture[0]);
/*     */           
/*     */           return CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).handle(());
/*     */         });
/*     */     
/*  95 */     Objects.requireNonNull(preparationBarrier); return tags.thenCombine(functions, Pair::of).thenCompose(preparationBarrier::wait)
/*  96 */       .thenAcceptAsync(data -> {
/*     */           Map<Identifier, CompletableFuture<CommandFunction<CommandSourceStack>>> functionFutures = (Map<Identifier, CompletableFuture<CommandFunction<CommandSourceStack>>>)data.getSecond();
/*     */           ImmutableMap.Builder<Identifier, CommandFunction<CommandSourceStack>> newFunctions = ImmutableMap.builder();
/*     */           functionFutures.forEach(());
/*     */           this.functions = (Map<Identifier, CommandFunction<CommandSourceStack>>)newFunctions.build();
/*     */           this.tags = this.tagsLoader.build((Map)data.getFirst());
/*     */         }, reloadExecutor);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<CommandFunction<CommandSourceStack>> getTag(Identifier tag) {
/*     */     return this.tags.getOrDefault(tag, List.of());
/*     */   }
/*     */   
/*     */   public Iterable<Identifier> getAvailableTags() {
/*     */     return this.tags.keySet();
/*     */   }
/*     */   
/*     */   private static List<String> readLines(Resource resource) {
/*     */     
/* 116 */     try { BufferedReader reader = resource.openAsReader(); 
/* 117 */       try { List<String> list = reader.lines().toList();
/* 118 */         if (reader != null) reader.close();  return list; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
/* 119 */     { throw new CompletionException(ex); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/ServerFunctionLibrary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */