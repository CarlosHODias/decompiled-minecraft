/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleReloadInstance;
/*    */ import net.minecraft.server.permissions.PermissionSet;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ReloadableServerResources {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 24 */   private static final CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
/*    */   
/*    */   private final ReloadableServerRegistries.Holder fullRegistryHolder;
/*    */   
/*    */   private final Commands commands;
/*    */   private final RecipeManager recipes;
/*    */   private final ServerAdvancementManager advancements;
/*    */   private final ServerFunctionLibrary functionLibrary;
/*    */   private final List<Registry.PendingTags<?>> postponedTags;
/*    */   
/*    */   private ReloadableServerResources(LayeredRegistryAccess<RegistryLayer> fullLayers, HolderLookup.Provider loadingContext, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, List<Registry.PendingTags<?>> postponedTags, PermissionSet functionCompilationPermissions) {
/* 35 */     this.fullRegistryHolder = new ReloadableServerRegistries.Holder((HolderLookup.Provider)fullLayers.compositeAccess());
/*    */     
/* 37 */     this.postponedTags = postponedTags;
/*    */     
/* 39 */     this.recipes = new RecipeManager(loadingContext);
/* 40 */     this.commands = new Commands(commandSelection, CommandBuildContext.simple(loadingContext, enabledFeatures));
/* 41 */     this.advancements = new ServerAdvancementManager(loadingContext);
/* 42 */     this.functionLibrary = new ServerFunctionLibrary(functionCompilationPermissions, this.commands.getDispatcher());
/*    */   }
/*    */   
/*    */   public ServerFunctionLibrary getFunctionLibrary() {
/* 46 */     return this.functionLibrary;
/*    */   }
/*    */   
/*    */   public ReloadableServerRegistries.Holder fullRegistries() {
/* 50 */     return this.fullRegistryHolder;
/*    */   }
/*    */   
/*    */   public RecipeManager getRecipeManager() {
/* 54 */     return this.recipes;
/*    */   }
/*    */   
/*    */   public Commands getCommands() {
/* 58 */     return this.commands;
/*    */   }
/*    */   
/*    */   public ServerAdvancementManager getAdvancements() {
/* 62 */     return this.advancements;
/*    */   }
/*    */   
/*    */   public List<PreparableReloadListener> listeners() {
/* 66 */     return (List)List.of(this.recipes, this.functionLibrary, this.advancements);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CompletableFuture<ReloadableServerResources> loadResources(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> contextLayers, List<Registry.PendingTags<?>> updatedContextTags, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, PermissionSet functionCompilationPermissions, Executor backgroundExecutor, Executor mainThreadExecutor) {
/* 79 */     return ReloadableServerRegistries.reload(contextLayers, updatedContextTags, resourceManager, backgroundExecutor).thenCompose(fullRegistries -> {
/*    */           ReloadableServerResources result = new ReloadableServerResources(fullRegistries.layers(), fullRegistries.lookupWithUpdatedTags(), enabledFeatures, commandSelection, updatedContextTags, functionCompilationPermissions);
/*    */           return SimpleReloadInstance.create(resourceManager, result.listeners(), backgroundExecutor, mainThreadExecutor, DATA_RELOAD_INITIAL_TASK, LOGGER.isDebugEnabled()).done().thenApply(());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateStaticRegistryTags() {
/* 87 */     this.postponedTags.forEach(Registry.PendingTags::apply);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/ReloadableServerResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */