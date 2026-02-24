/*     */ package net.minecraft.server;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerFunctionManager {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  26 */   private static final Identifier TICK_FUNCTION_TAG = Identifier.withDefaultNamespace("tick");
/*  27 */   private static final Identifier LOAD_FUNCTION_TAG = Identifier.withDefaultNamespace("load");
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*  31 */   private List<CommandFunction<CommandSourceStack>> ticking = (List<CommandFunction<CommandSourceStack>>)ImmutableList.of();
/*     */   
/*     */   private boolean postReload;
/*     */   private ServerFunctionLibrary library;
/*     */   
/*     */   public ServerFunctionManager(MinecraftServer server, ServerFunctionLibrary library) {
/*  37 */     this.server = server;
/*  38 */     this.library = library;
/*  39 */     postReload(library);
/*     */   }
/*     */   
/*     */   public CommandDispatcher<CommandSourceStack> getDispatcher() {
/*  43 */     return this.server.getCommands().getDispatcher();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  47 */     if (!this.server.tickRateManager().runsNormally()) {
/*     */       return;
/*     */     }
/*  50 */     if (this.postReload) {
/*  51 */       this.postReload = false;
/*  52 */       Collection<CommandFunction<CommandSourceStack>> functions = this.library.getTag(LOAD_FUNCTION_TAG);
/*  53 */       executeTagFunctions(functions, LOAD_FUNCTION_TAG);
/*     */     } 
/*  55 */     executeTagFunctions(this.ticking, TICK_FUNCTION_TAG);
/*     */   }
/*     */   
/*     */   private void executeTagFunctions(Collection<CommandFunction<CommandSourceStack>> functions, Identifier loadFunctionTag) {
/*  59 */     java.util.Objects.requireNonNull(loadFunctionTag); Profiler.get().push(loadFunctionTag::toString);
/*  60 */     for (CommandFunction<CommandSourceStack> function : functions) {
/*  61 */       execute(function, getGameLoopSender());
/*     */     }
/*  63 */     Profiler.get().pop();
/*     */   }
/*     */   
/*     */   public void execute(CommandFunction<CommandSourceStack> functionIn, CommandSourceStack sender) {
/*  67 */     ProfilerFiller profiler = Profiler.get();
/*  68 */     profiler.push(() -> "function " + String.valueOf(functionIn.id()));
/*     */     
/*  70 */     try { InstantiatedFunction<CommandSourceStack> function = functionIn.instantiate(null, getDispatcher());
/*  71 */       Commands.executeCommandInContext(sender, context -> ExecutionContext.queueInitialFunctionCall(context, function, (ExecutionCommandSource)sender, CommandResultCallback.EMPTY)); }
/*  72 */     catch (FunctionInstantiationException functionInstantiationException) {  }
/*  73 */     catch (Exception e)
/*  74 */     { LOGGER.warn("Failed to execute function {}", functionIn.id(), e); }
/*     */     finally
/*  76 */     { profiler.pop(); }
/*     */   
/*     */   }
/*     */   
/*     */   public void replaceLibrary(ServerFunctionLibrary library) {
/*  81 */     this.library = library;
/*  82 */     postReload(library);
/*     */   }
/*     */   
/*     */   private void postReload(ServerFunctionLibrary library) {
/*  86 */     this.ticking = List.copyOf(library.getTag(TICK_FUNCTION_TAG));
/*  87 */     this.postReload = true;
/*     */   }
/*     */   
/*     */   public CommandSourceStack getGameLoopSender() {
/*  91 */     return this.server.createCommandSourceStack().withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput();
/*     */   }
/*     */   
/*     */   public Optional<CommandFunction<CommandSourceStack>> get(Identifier id) {
/*  95 */     return this.library.getFunction(id);
/*     */   }
/*     */   
/*     */   public List<CommandFunction<CommandSourceStack>> getTag(Identifier id) {
/*  99 */     return this.library.getTag(id);
/*     */   }
/*     */   
/*     */   public Iterable<Identifier> getFunctionNames() {
/* 103 */     return this.library.getFunctions().keySet();
/*     */   }
/*     */   
/*     */   public Iterable<Identifier> getTagNames() {
/* 107 */     return this.library.getAvailableTags();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/ServerFunctionManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */