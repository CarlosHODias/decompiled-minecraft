/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.blaze3d.Blaze3D;
/*     */ import com.mojang.blaze3d.platform.ClipboardManager;
/*     */ import com.mojang.blaze3d.platform.InputConstants;
/*     */ import com.mojang.blaze3d.platform.TextureUtil;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.debug.DebugOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.KeyEvent.Action;
/*     */ import net.minecraft.client.renderer.fog.FogRenderer;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.commands.GameModeCommand;
/*     */ import net.minecraft.server.commands.VersionCommand;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.NativeModuleLister;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeyboardHandler
/*     */ {
/*  65 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int DEBUG_CRASH_TIME = 10000;
/*     */   private final Minecraft minecraft;
/*  69 */   private final ClipboardManager clipboardManager = new ClipboardManager(); private long debugCrashKeyTime;
/*     */   private long debugCrashKeyReportedTime;
/*     */   private long debugCrashKeyReportedCount;
/*     */   private boolean usedDebugKeyAsModifier;
/*     */   
/*     */   public KeyboardHandler(Minecraft minecraft) {
/*  75 */     this.debugCrashKeyTime = -1L;
/*  76 */     this.debugCrashKeyReportedTime = -1L;
/*  77 */     this.debugCrashKeyReportedCount = -1L;
/*     */     this.minecraft = minecraft;
/*     */   } private boolean handleChunkDebugKeys(KeyEvent event) {
/*     */     boolean chunkSectionPaths, renderOctree, fogEnabled, sectionVisibility;
/*  81 */     switch (event.key()) {
/*     */       case 69:
/*  83 */         if (this.minecraft.player == null) {
/*  84 */           return false;
/*     */         }
/*  86 */         chunkSectionPaths = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_SECTION_PATHS);
/*  87 */         debugFeedback("SectionPath: " + (chunkSectionPaths ? "shown" : "hidden"));
/*  88 */         return true;
/*     */       case 76:
/*  90 */         this.minecraft.smartCull = !this.minecraft.smartCull;
/*  91 */         debugFeedbackEnabledStatus("SmartCull: ", this.minecraft.smartCull);
/*  92 */         return true;
/*     */       case 79:
/*  94 */         if (this.minecraft.player == null) {
/*  95 */           return false;
/*     */         }
/*  97 */         renderOctree = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_SECTION_OCTREE);
/*  98 */         debugFeedbackEnabledStatus("Frustum culling Octree: ", renderOctree);
/*  99 */         return true;
/*     */       case 70:
/* 101 */         fogEnabled = FogRenderer.toggleFog();
/* 102 */         debugFeedbackEnabledStatus("Fog: ", fogEnabled);
/* 103 */         return true;
/*     */       case 85:
/* 105 */         if (event.hasShiftDown()) {
/* 106 */           this.minecraft.levelRenderer.killFrustum();
/* 107 */           debugFeedback("Killed frustum");
/*     */         } else {
/* 109 */           this.minecraft.levelRenderer.captureFrustum();
/* 110 */           debugFeedback("Captured frustum");
/*     */         } 
/* 112 */         return true;
/*     */       case 86:
/* 114 */         if (this.minecraft.player == null) {
/* 115 */           return false;
/*     */         }
/* 117 */         sectionVisibility = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_SECTION_VISIBILITY);
/* 118 */         debugFeedbackEnabledStatus("SectionVisibility: ", sectionVisibility);
/* 119 */         return true;
/*     */       case 87:
/* 121 */         this.minecraft.wireframe = !this.minecraft.wireframe;
/* 122 */         debugFeedbackEnabledStatus("WireFrame: ", this.minecraft.wireframe);
/* 123 */         return true;
/*     */     } 
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void debugFeedbackEnabledStatus(String prefix, boolean isEnabled) {
/* 130 */     debugFeedback(prefix + prefix);
/*     */   }
/*     */   
/*     */   private void showDebugChat(Component message) {
/* 134 */     this.minecraft.gui.getChat().addMessage(message);
/* 135 */     this.minecraft.getNarrator().saySystemQueued(message);
/*     */   }
/*     */   
/*     */   private static Component decorateDebugComponent(ChatFormatting formatting, Component component) {
/* 139 */     return (Component)Component.empty().append((Component)Component.translatable("debug.prefix").withStyle(new ChatFormatting[] { formatting, ChatFormatting.BOLD })).append(CommonComponents.SPACE).append(component);
/*     */   }
/*     */   
/*     */   private void debugWarningComponent(Component component) {
/* 143 */     showDebugChat(decorateDebugComponent(ChatFormatting.RED, component));
/*     */   }
/*     */   
/*     */   private void debugFeedbackComponent(Component component) {
/* 147 */     showDebugChat(decorateDebugComponent(ChatFormatting.YELLOW, component));
/*     */   }
/*     */   
/*     */   private void debugFeedbackTranslated(String pattern, Object... args) {
/* 151 */     debugFeedbackComponent((Component)Component.translatable(pattern, args));
/*     */   }
/*     */   
/*     */   private void debugFeedback(String message) {
/* 155 */     debugFeedbackComponent((Component)Component.literal(message));
/*     */   }
/*     */   
/*     */   private boolean handleDebugKeys(KeyEvent event) {
/* 159 */     if (this.debugCrashKeyTime > 0L && this.debugCrashKeyTime < Util.getMillis() - 100L) {
/* 160 */       return true;
/*     */     }
/*     */     
/* 163 */     if (SharedConstants.DEBUG_HOTKEYS && handleChunkDebugKeys(event)) {
/* 164 */       return true;
/*     */     }
/*     */     
/* 167 */     if (SharedConstants.DEBUG_FEATURE_COUNT) {
/* 168 */       switch (event.key()) {
/*     */         case 82:
/* 170 */           FeatureCountTracker.clearCounts();
/* 171 */           return true;
/*     */         
/*     */         case 76:
/* 174 */           FeatureCountTracker.logCounts();
/* 175 */           return true;
/*     */       } 
/*     */ 
/*     */     
/*     */     }
/* 180 */     Options options = this.minecraft.options;
/*     */     boolean debugAction = false;
/* 182 */     if (options.keyDebugReloadChunk.matches(event)) {
/* 183 */       this.minecraft.levelRenderer.allChanged();
/* 184 */       debugFeedbackTranslated("debug.reload_chunks.message", new Object[0]);
/* 185 */       debugAction = true;
/*     */     } 
/*     */     
/* 188 */     if (options.keyDebugShowHitboxes.matches(event) && 
/* 189 */       this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo()) {
/* 190 */       boolean renderHitBoxes = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.ENTITY_HITBOXES);
/* 191 */       debugFeedbackTranslated(renderHitBoxes ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off", new Object[0]);
/* 192 */       debugAction = true;
/*     */     } 
/*     */ 
/*     */     
/* 196 */     if (options.keyDebugClearChat.matches(event)) {
/* 197 */       this.minecraft.gui.getChat().clearMessages(false);
/* 198 */       debugAction = true;
/*     */     } 
/*     */     
/* 201 */     if (options.keyDebugShowChunkBorders.matches(event) && 
/* 202 */       this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo()) {
/* 203 */       boolean displayChunkborder = this.minecraft.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_BORDERS);
/* 204 */       debugFeedbackTranslated(displayChunkborder ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off", new Object[0]);
/* 205 */       debugAction = true;
/*     */     } 
/*     */ 
/*     */     
/* 209 */     if (options.keyDebugShowAdvancedTooltips.matches(event)) {
/* 210 */       options.advancedItemTooltips = !options.advancedItemTooltips;
/* 211 */       debugFeedbackTranslated(options.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off", new Object[0]);
/* 212 */       options.save();
/* 213 */       debugAction = true;
/*     */     } 
/*     */     
/* 216 */     if (options.keyDebugCopyRecreateCommand.matches(event)) {
/* 217 */       if (this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo()) {
/* 218 */         copyRecreateCommand(this.minecraft.player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER), !event.hasShiftDown());
/*     */       }
/* 220 */       debugAction = true;
/*     */     } 
/*     */     
/* 223 */     if (options.keyDebugSpectate.matches(event)) {
/* 224 */       if (this.minecraft.player == null || !GameModeCommand.PERMISSION_CHECK.check(this.minecraft.player.permissions())) {
/* 225 */         debugFeedbackTranslated("debug.creative_spectator.error", new Object[0]);
/* 226 */       } else if (!this.minecraft.player.isSpectator()) {
/* 227 */         this.minecraft.player.connection.send((Packet)new ServerboundChangeGameModePacket(GameType.SPECTATOR));
/*     */       } else {
/* 229 */         GameType newGameType = (GameType)MoreObjects.firstNonNull(this.minecraft.gameMode.getPreviousPlayerMode(), GameType.CREATIVE);
/* 230 */         this.minecraft.player.connection.send((Packet)new ServerboundChangeGameModePacket(newGameType));
/*     */       } 
/* 232 */       debugAction = true;
/*     */     } 
/*     */     
/* 235 */     if (options.keyDebugSwitchGameMode.matches(event) && this.minecraft.level != null && this.minecraft.screen == null) {
/* 236 */       if (this.minecraft.canSwitchGameMode() && GameModeCommand.PERMISSION_CHECK.check(this.minecraft.player.permissions())) {
/* 237 */         this.minecraft.setScreen((Screen)new GameModeSwitcherScreen());
/*     */       } else {
/* 239 */         debugFeedbackTranslated("debug.gamemodes.error", new Object[0]);
/*     */       } 
/* 241 */       debugAction = true;
/*     */     } 
/*     */     
/* 244 */     if (options.keyDebugDebugOptions.matches(event)) {
/* 245 */       if (this.minecraft.screen instanceof DebugOptionsScreen) {
/* 246 */         this.minecraft.screen.onClose();
/* 247 */       } else if (this.minecraft.canInterruptScreen()) {
/* 248 */         if (this.minecraft.screen != null) {
/* 249 */           this.minecraft.screen.onClose();
/*     */         }
/* 251 */         this.minecraft.setScreen((Screen)new DebugOptionsScreen());
/*     */       } 
/* 253 */       debugAction = true;
/*     */     } 
/*     */     
/* 256 */     if (options.keyDebugFocusPause.matches(event)) {
/* 257 */       options.pauseOnLostFocus = !options.pauseOnLostFocus;
/* 258 */       options.save();
/* 259 */       debugFeedbackTranslated(options.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off", new Object[0]);
/* 260 */       debugAction = true;
/*     */     } 
/*     */     
/* 263 */     if (options.keyDebugDumpDynamicTextures.matches(event)) {
/* 264 */       Path gameDirectory = this.minecraft.gameDirectory.toPath().toAbsolutePath();
/* 265 */       Path debugTexturePath = TextureUtil.getDebugTexturePath(gameDirectory);
/* 266 */       this.minecraft.getTextureManager().dumpAllSheets(debugTexturePath);
/*     */       
/* 268 */       MutableComponent mutableComponent = Component.literal(gameDirectory.relativize(debugTexturePath).toString())
/* 269 */         .withStyle(ChatFormatting.UNDERLINE)
/* 270 */         .withStyle(s -> s.withClickEvent((ClickEvent)new ClickEvent.OpenFile(debugTexturePath)));
/*     */       
/* 272 */       debugFeedbackComponent((Component)Component.translatable("debug.dump_dynamic_textures", new Object[] { mutableComponent }));
/* 273 */       debugAction = true;
/*     */     } 
/*     */     
/* 276 */     if (options.keyDebugReloadResourcePacks.matches(event)) {
/* 277 */       debugFeedbackTranslated("debug.reload_resourcepacks.message", new Object[0]);
/* 278 */       this.minecraft.reloadResourcePacks();
/* 279 */       debugAction = true;
/*     */     } 
/*     */     
/* 282 */     if (options.keyDebugProfiling.matches(event)) {
/* 283 */       if (this.minecraft.debugClientMetricsStart(this::debugFeedbackComponent)) {
/* 284 */         debugFeedbackComponent((Component)Component.translatable("debug.profiling.start", new Object[] { 10, options.keyDebugModifier.getTranslatedKeyMessage(), options.keyDebugProfiling.getTranslatedKeyMessage() }));
/*     */       }
/* 286 */       debugAction = true;
/*     */     } 
/*     */     
/* 289 */     if (options.keyDebugCopyLocation.matches(event) && 
/* 290 */       this.minecraft.player != null && !this.minecraft.player.isReducedDebugInfo()) {
/* 291 */       debugFeedbackTranslated("debug.copy_location.message", new Object[0]);
/* 292 */       setClipboard(String.format(Locale.ROOT, "/execute in %s run tp @s %.2f %.2f %.2f %.2f %.2f", new Object[] { this.minecraft.player.level().dimension().identifier(), this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), this.minecraft.player.getYRot(), this.minecraft.player.getXRot() }));
/* 293 */       debugAction = true;
/*     */     } 
/*     */ 
/*     */     
/* 297 */     if (options.keyDebugDumpVersion.matches(event)) {
/* 298 */       debugFeedbackTranslated("debug.version.header", new Object[0]);
/* 299 */       VersionCommand.dumpVersion(this::showDebugChat);
/* 300 */       debugAction = true;
/*     */     } 
/*     */     
/* 303 */     if (options.keyDebugPofilingChart.matches(event)) {
/* 304 */       this.minecraft.getDebugOverlay().toggleProfilerChart();
/* 305 */       debugAction = true;
/*     */     } 
/*     */     
/* 308 */     if (options.keyDebugFpsCharts.matches(event)) {
/* 309 */       this.minecraft.getDebugOverlay().toggleFpsCharts();
/* 310 */       debugAction = true;
/*     */     } 
/*     */     
/* 313 */     if (options.keyDebugNetworkCharts.matches(event)) {
/* 314 */       this.minecraft.getDebugOverlay().toggleNetworkCharts();
/* 315 */       debugAction = true;
/*     */     } 
/*     */     
/* 318 */     return debugAction; } private void copyRecreateCommand(boolean addNbt, boolean pullFromServer) { BlockPos blockPos; Entity entity;
/*     */     Level level;
/*     */     Identifier id;
/*     */     BlockState state;
/* 322 */     HitResult hitResult = this.minecraft.hitResult;
/* 323 */     if (hitResult == null) {
/*     */       return;
/*     */     }
/*     */     
/* 327 */     switch (hitResult.getType()) {
/*     */       case BLOCK:
/* 329 */         blockPos = ((BlockHitResult)hitResult).getBlockPos();
/* 330 */         level = this.minecraft.player.level();
/* 331 */         state = level.getBlockState(blockPos);
/*     */         
/* 333 */         if (addNbt) {
/* 334 */           if (pullFromServer) {
/* 335 */             this.minecraft.player.connection.getDebugQueryHandler().queryBlockEntityTag(blockPos, tag -> {
/*     */                   copyCreateBlockCommand(state, state, blockPos); debugFeedbackTranslated("debug.inspect.server.block", new Object[0]);
/*     */                 });
/*     */             break;
/*     */           } 
/* 340 */           BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 341 */           CompoundTag tag = (blockEntity != null) ? blockEntity.saveWithoutMetadata((HolderLookup.Provider)level.registryAccess()) : null;
/* 342 */           copyCreateBlockCommand(state, blockPos, tag);
/* 343 */           debugFeedbackTranslated("debug.inspect.client.block", new Object[0]);
/*     */           break;
/*     */         } 
/* 346 */         copyCreateBlockCommand(state, blockPos, null);
/* 347 */         debugFeedbackTranslated("debug.inspect.client.block", new Object[0]);
/*     */         break;
/*     */ 
/*     */       
/*     */       case ENTITY:
/* 352 */         entity = ((EntityHitResult)hitResult).getEntity();
/* 353 */         id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
/* 354 */         if (addNbt) {
/* 355 */           if (pullFromServer) {
/* 356 */             this.minecraft.player.connection.getDebugQueryHandler().queryEntityTag(entity.getId(), tag -> {
/*     */                   copyCreateEntityCommand(id, id.position(), entity); debugFeedbackTranslated("debug.inspect.server.entity", new Object[0]);
/*     */                 });
/*     */             break;
/*     */           } 
/* 361 */           ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER); 
/* 362 */           try { TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 363 */             entity.saveWithoutId((ValueOutput)output);
/* 364 */             copyCreateEntityCommand(id, entity.position(), output.buildResult());
/* 365 */             reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 366 */            debugFeedbackTranslated("debug.inspect.client.entity", new Object[0]);
/*     */           break;
/*     */         } 
/* 369 */         copyCreateEntityCommand(id, entity.position(), null);
/* 370 */         debugFeedbackTranslated("debug.inspect.client.entity", new Object[0]);
/*     */         break;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyCreateBlockCommand(BlockState state, BlockPos blockPos, CompoundTag entityTag) {
/* 380 */     StringBuilder description = new StringBuilder(BlockStateParser.serialize(state));
/* 381 */     if (entityTag != null) {
/* 382 */       description.append(entityTag);
/*     */     }
/* 384 */     String command = String.format(Locale.ROOT, "/setblock %d %d %d %s", new Object[] { blockPos.getX(), blockPos.getY(), blockPos.getZ(), description });
/* 385 */     setClipboard(command);
/*     */   }
/*     */   
/*     */   private void copyCreateEntityCommand(Identifier id, Vec3 pos, CompoundTag entityTag) {
/*     */     String command;
/* 390 */     if (entityTag != null) {
/* 391 */       entityTag.remove("UUID");
/* 392 */       entityTag.remove("Pos");
/* 393 */       String snbt = NbtUtils.toPrettyComponent((Tag)entityTag).getString();
/* 394 */       command = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f %s", new Object[] { id, pos.x, pos.y, pos.z, snbt });
/*     */     } else {
/* 396 */       command = String.format(Locale.ROOT, "/summon %s %.2f %.2f %.2f", new Object[] { id, pos.x, pos.y, pos.z });
/*     */     } 
/* 398 */     setClipboard(command);
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
/*     */   private void keyPress(long handle, @KeyEvent.Action int action, KeyEvent event) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   4: invokevirtual getWindow : ()Lcom/mojang/blaze3d/platform/Window;
/*     */     //   7: astore #5
/*     */     //   9: lload_1
/*     */     //   10: aload #5
/*     */     //   12: invokevirtual handle : ()J
/*     */     //   15: lcmp
/*     */     //   16: ifeq -> 20
/*     */     //   19: return
/*     */     //   20: aload_0
/*     */     //   21: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   24: invokevirtual getFramerateLimitTracker : ()Lcom/mojang/blaze3d/platform/FramerateLimitTracker;
/*     */     //   27: invokevirtual onInputReceived : ()V
/*     */     //   30: aload_0
/*     */     //   31: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   34: getfield options : Lnet/minecraft/client/Options;
/*     */     //   37: astore #6
/*     */     //   39: aload #6
/*     */     //   41: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   44: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   47: invokevirtual getValue : ()I
/*     */     //   50: aload #6
/*     */     //   52: getfield keyDebugOverlay : Lnet/minecraft/client/KeyMapping;
/*     */     //   55: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   58: invokevirtual getValue : ()I
/*     */     //   61: if_icmpne -> 68
/*     */     //   64: iconst_1
/*     */     //   65: goto -> 69
/*     */     //   68: iconst_0
/*     */     //   69: istore #7
/*     */     //   71: aload #6
/*     */     //   73: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   76: invokevirtual isDown : ()Z
/*     */     //   79: istore #8
/*     */     //   81: aload #6
/*     */     //   83: getfield keyDebugCrash : Lnet/minecraft/client/KeyMapping;
/*     */     //   86: invokevirtual isUnbound : ()Z
/*     */     //   89: ifne -> 120
/*     */     //   92: aload_0
/*     */     //   93: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   96: invokevirtual getWindow : ()Lcom/mojang/blaze3d/platform/Window;
/*     */     //   99: aload #6
/*     */     //   101: getfield keyDebugCrash : Lnet/minecraft/client/KeyMapping;
/*     */     //   104: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   107: invokevirtual getValue : ()I
/*     */     //   110: invokestatic isKeyDown : (Lcom/mojang/blaze3d/platform/Window;I)Z
/*     */     //   113: ifeq -> 120
/*     */     //   116: iconst_1
/*     */     //   117: goto -> 121
/*     */     //   120: iconst_0
/*     */     //   121: istore #9
/*     */     //   123: aload_0
/*     */     //   124: getfield debugCrashKeyTime : J
/*     */     //   127: lconst_0
/*     */     //   128: lcmp
/*     */     //   129: ifle -> 152
/*     */     //   132: iload #9
/*     */     //   134: ifeq -> 142
/*     */     //   137: iload #8
/*     */     //   139: ifne -> 187
/*     */     //   142: aload_0
/*     */     //   143: ldc2_w -1
/*     */     //   146: putfield debugCrashKeyTime : J
/*     */     //   149: goto -> 187
/*     */     //   152: iload #9
/*     */     //   154: ifeq -> 187
/*     */     //   157: iload #8
/*     */     //   159: ifeq -> 187
/*     */     //   162: aload_0
/*     */     //   163: iload #7
/*     */     //   165: putfield usedDebugKeyAsModifier : Z
/*     */     //   168: aload_0
/*     */     //   169: invokestatic getMillis : ()J
/*     */     //   172: putfield debugCrashKeyTime : J
/*     */     //   175: aload_0
/*     */     //   176: invokestatic getMillis : ()J
/*     */     //   179: putfield debugCrashKeyReportedTime : J
/*     */     //   182: aload_0
/*     */     //   183: lconst_0
/*     */     //   184: putfield debugCrashKeyReportedCount : J
/*     */     //   187: aload_0
/*     */     //   188: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   191: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   194: astore #10
/*     */     //   196: aload #10
/*     */     //   198: ifnull -> 275
/*     */     //   201: aload #4
/*     */     //   203: invokevirtual key : ()I
/*     */     //   206: tableswitch default -> 275, 258 -> 265, 259 -> 275, 260 -> 275, 261 -> 275, 262 -> 252, 263 -> 252, 264 -> 252, 265 -> 252
/*     */     //   252: aload_0
/*     */     //   253: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   256: getstatic net/minecraft/client/InputType.KEYBOARD_ARROW : Lnet/minecraft/client/InputType;
/*     */     //   259: invokevirtual setLastInputType : (Lnet/minecraft/client/InputType;)V
/*     */     //   262: goto -> 275
/*     */     //   265: aload_0
/*     */     //   266: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   269: getstatic net/minecraft/client/InputType.KEYBOARD_TAB : Lnet/minecraft/client/InputType;
/*     */     //   272: invokevirtual setLastInputType : (Lnet/minecraft/client/InputType;)V
/*     */     //   275: iload_3
/*     */     //   276: iconst_1
/*     */     //   277: if_icmpne -> 459
/*     */     //   280: aload_0
/*     */     //   281: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   284: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   287: instanceof net/minecraft/client/gui/screens/options/controls/KeyBindsScreen
/*     */     //   290: ifeq -> 312
/*     */     //   293: aload #10
/*     */     //   295: checkcast net/minecraft/client/gui/screens/options/controls/KeyBindsScreen
/*     */     //   298: getfield lastKeySelection : J
/*     */     //   301: invokestatic getMillis : ()J
/*     */     //   304: ldc2_w 20
/*     */     //   307: lsub
/*     */     //   308: lcmp
/*     */     //   309: ifgt -> 459
/*     */     //   312: aload #6
/*     */     //   314: getfield keyFullscreen : Lnet/minecraft/client/KeyMapping;
/*     */     //   317: aload #4
/*     */     //   319: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   322: ifeq -> 387
/*     */     //   325: aload #5
/*     */     //   327: invokevirtual toggleFullScreen : ()V
/*     */     //   330: aload #5
/*     */     //   332: invokevirtual isFullscreen : ()Z
/*     */     //   335: istore #11
/*     */     //   337: aload #6
/*     */     //   339: invokevirtual fullscreen : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   342: iload #11
/*     */     //   344: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */     //   347: invokevirtual set : (Ljava/lang/Object;)V
/*     */     //   350: aload #6
/*     */     //   352: invokevirtual save : ()V
/*     */     //   355: aload_0
/*     */     //   356: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   359: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   362: astore #13
/*     */     //   364: aload #13
/*     */     //   366: instanceof net/minecraft/client/gui/screens/options/VideoSettingsScreen
/*     */     //   369: ifeq -> 386
/*     */     //   372: aload #13
/*     */     //   374: checkcast net/minecraft/client/gui/screens/options/VideoSettingsScreen
/*     */     //   377: astore #12
/*     */     //   379: aload #12
/*     */     //   381: iload #11
/*     */     //   383: invokevirtual updateFullscreenButton : (Z)V
/*     */     //   386: return
/*     */     //   387: aload #6
/*     */     //   389: getfield keyScreenshot : Lnet/minecraft/client/KeyMapping;
/*     */     //   392: aload #4
/*     */     //   394: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   397: ifeq -> 459
/*     */     //   400: aload #4
/*     */     //   402: invokevirtual hasControlDownWithQuirk : ()Z
/*     */     //   405: ifeq -> 435
/*     */     //   408: getstatic net/minecraft/SharedConstants.DEBUG_PANORAMA_SCREENSHOT : Z
/*     */     //   411: ifeq -> 435
/*     */     //   414: aload_0
/*     */     //   415: aload_0
/*     */     //   416: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   419: aload_0
/*     */     //   420: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   423: getfield gameDirectory : Ljava/io/File;
/*     */     //   426: invokevirtual grabPanoramixScreenshot : (Ljava/io/File;)Lnet/minecraft/network/chat/Component;
/*     */     //   429: invokevirtual showDebugChat : (Lnet/minecraft/network/chat/Component;)V
/*     */     //   432: goto -> 458
/*     */     //   435: aload_0
/*     */     //   436: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   439: getfield gameDirectory : Ljava/io/File;
/*     */     //   442: aload_0
/*     */     //   443: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   446: invokevirtual getMainRenderTarget : ()Lcom/mojang/blaze3d/pipeline/RenderTarget;
/*     */     //   449: aload_0
/*     */     //   450: <illegal opcode> accept : (Lnet/minecraft/client/KeyboardHandler;)Ljava/util/function/Consumer;
/*     */     //   455: invokestatic grab : (Ljava/io/File;Lcom/mojang/blaze3d/pipeline/RenderTarget;Ljava/util/function/Consumer;)V
/*     */     //   458: return
/*     */     //   459: iload_3
/*     */     //   460: ifeq -> 627
/*     */     //   463: aload #10
/*     */     //   465: ifnull -> 493
/*     */     //   468: aload #10
/*     */     //   470: invokevirtual getFocused : ()Lnet/minecraft/client/gui/components/events/GuiEventListener;
/*     */     //   473: instanceof net/minecraft/client/gui/components/EditBox
/*     */     //   476: ifeq -> 493
/*     */     //   479: aload #10
/*     */     //   481: invokevirtual getFocused : ()Lnet/minecraft/client/gui/components/events/GuiEventListener;
/*     */     //   484: checkcast net/minecraft/client/gui/components/EditBox
/*     */     //   487: invokevirtual canConsumeInput : ()Z
/*     */     //   490: ifne -> 497
/*     */     //   493: iconst_1
/*     */     //   494: goto -> 498
/*     */     //   497: iconst_0
/*     */     //   498: istore #11
/*     */     //   500: iload #11
/*     */     //   502: ifeq -> 627
/*     */     //   505: aload #4
/*     */     //   507: invokevirtual hasControlDownWithQuirk : ()Z
/*     */     //   510: ifeq -> 618
/*     */     //   513: aload #4
/*     */     //   515: invokevirtual key : ()I
/*     */     //   518: bipush #66
/*     */     //   520: if_icmpne -> 618
/*     */     //   523: aload_0
/*     */     //   524: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   527: invokevirtual getNarrator : ()Lnet/minecraft/client/GameNarrator;
/*     */     //   530: invokevirtual isActive : ()Z
/*     */     //   533: ifeq -> 618
/*     */     //   536: aload #6
/*     */     //   538: invokevirtual narratorHotkey : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   541: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   544: checkcast java/lang/Boolean
/*     */     //   547: invokevirtual booleanValue : ()Z
/*     */     //   550: ifeq -> 618
/*     */     //   553: aload #6
/*     */     //   555: invokevirtual narrator : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   558: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   561: getstatic net/minecraft/client/NarratorStatus.OFF : Lnet/minecraft/client/NarratorStatus;
/*     */     //   564: if_acmpne -> 571
/*     */     //   567: iconst_1
/*     */     //   568: goto -> 572
/*     */     //   571: iconst_0
/*     */     //   572: istore #12
/*     */     //   574: aload #6
/*     */     //   576: invokevirtual narrator : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   579: aload #6
/*     */     //   581: invokevirtual narrator : ()Lnet/minecraft/client/OptionInstance;
/*     */     //   584: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   587: checkcast net/minecraft/client/NarratorStatus
/*     */     //   590: invokevirtual getId : ()I
/*     */     //   593: iconst_1
/*     */     //   594: iadd
/*     */     //   595: invokestatic byId : (I)Lnet/minecraft/client/NarratorStatus;
/*     */     //   598: invokevirtual set : (Ljava/lang/Object;)V
/*     */     //   601: aload #6
/*     */     //   603: invokevirtual save : ()V
/*     */     //   606: aload #10
/*     */     //   608: ifnull -> 618
/*     */     //   611: aload #10
/*     */     //   613: iload #12
/*     */     //   615: invokevirtual updateNarratorStatus : (Z)V
/*     */     //   618: aload_0
/*     */     //   619: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   622: getfield player : Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   625: astore #12
/*     */     //   627: aload #10
/*     */     //   629: ifnull -> 807
/*     */     //   632: iload_3
/*     */     //   633: iconst_1
/*     */     //   634: if_icmpeq -> 642
/*     */     //   637: iload_3
/*     */     //   638: iconst_2
/*     */     //   639: if_icmpne -> 681
/*     */     //   642: aload #10
/*     */     //   644: invokevirtual afterKeyboardAction : ()V
/*     */     //   647: aload #10
/*     */     //   649: aload #4
/*     */     //   651: invokevirtual keyPressed : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   654: ifeq -> 714
/*     */     //   657: aload_0
/*     */     //   658: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   661: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   664: ifnonnull -> 680
/*     */     //   667: aload #4
/*     */     //   669: invokestatic getKey : (Lnet/minecraft/client/input/KeyEvent;)Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   672: astore #11
/*     */     //   674: aload #11
/*     */     //   676: iconst_0
/*     */     //   677: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   680: return
/*     */     //   681: iload_3
/*     */     //   682: ifne -> 714
/*     */     //   685: aload #10
/*     */     //   687: aload #4
/*     */     //   689: invokevirtual keyReleased : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   692: ifeq -> 714
/*     */     //   695: aload #6
/*     */     //   697: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   700: aload #4
/*     */     //   702: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   705: ifeq -> 713
/*     */     //   708: aload_0
/*     */     //   709: iconst_0
/*     */     //   710: putfield usedDebugKeyAsModifier : Z
/*     */     //   713: return
/*     */     //   714: goto -> 807
/*     */     //   717: astore #11
/*     */     //   719: aload #11
/*     */     //   721: ldc_w 'keyPressed event handler'
/*     */     //   724: invokestatic forThrowable : (Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;
/*     */     //   727: astore #12
/*     */     //   729: aload #10
/*     */     //   731: aload #12
/*     */     //   733: invokevirtual fillCrashDetails : (Lnet/minecraft/CrashReport;)V
/*     */     //   736: aload #12
/*     */     //   738: ldc_w 'Key'
/*     */     //   741: invokevirtual addCategory : (Ljava/lang/String;)Lnet/minecraft/CrashReportCategory;
/*     */     //   744: astore #13
/*     */     //   746: aload #13
/*     */     //   748: ldc_w 'Key'
/*     */     //   751: aload #4
/*     */     //   753: invokevirtual key : ()I
/*     */     //   756: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   759: invokevirtual setDetail : (Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/CrashReportCategory;
/*     */     //   762: pop
/*     */     //   763: aload #13
/*     */     //   765: ldc_w 'Scancode'
/*     */     //   768: aload #4
/*     */     //   770: invokevirtual scancode : ()I
/*     */     //   773: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   776: invokevirtual setDetail : (Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/CrashReportCategory;
/*     */     //   779: pop
/*     */     //   780: aload #13
/*     */     //   782: ldc_w 'Mods'
/*     */     //   785: aload #4
/*     */     //   787: invokevirtual modifiers : ()I
/*     */     //   790: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   793: invokevirtual setDetail : (Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/CrashReportCategory;
/*     */     //   796: pop
/*     */     //   797: new net/minecraft/ReportedException
/*     */     //   800: dup
/*     */     //   801: aload #12
/*     */     //   803: invokespecial <init> : (Lnet/minecraft/CrashReport;)V
/*     */     //   806: athrow
/*     */     //   807: aload #4
/*     */     //   809: invokestatic getKey : (Lnet/minecraft/client/input/KeyEvent;)Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   812: astore #11
/*     */     //   814: aload_0
/*     */     //   815: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   818: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   821: ifnonnull -> 828
/*     */     //   824: iconst_1
/*     */     //   825: goto -> 829
/*     */     //   828: iconst_0
/*     */     //   829: istore #12
/*     */     //   831: iload #12
/*     */     //   833: ifne -> 881
/*     */     //   836: aload_0
/*     */     //   837: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   840: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   843: astore #15
/*     */     //   845: aload #15
/*     */     //   847: instanceof net/minecraft/client/gui/screens/PauseScreen
/*     */     //   850: ifeq -> 868
/*     */     //   853: aload #15
/*     */     //   855: checkcast net/minecraft/client/gui/screens/PauseScreen
/*     */     //   858: astore #14
/*     */     //   860: aload #14
/*     */     //   862: invokevirtual showsPauseMenu : ()Z
/*     */     //   865: ifeq -> 881
/*     */     //   868: aload_0
/*     */     //   869: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   872: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   875: instanceof net/minecraft/client/gui/screens/debug/GameModeSwitcherScreen
/*     */     //   878: ifeq -> 885
/*     */     //   881: iconst_1
/*     */     //   882: goto -> 886
/*     */     //   885: iconst_0
/*     */     //   886: istore #13
/*     */     //   888: iload #7
/*     */     //   890: ifeq -> 938
/*     */     //   893: aload #6
/*     */     //   895: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   898: aload #4
/*     */     //   900: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   903: ifeq -> 938
/*     */     //   906: iload_3
/*     */     //   907: ifne -> 938
/*     */     //   910: aload_0
/*     */     //   911: getfield usedDebugKeyAsModifier : Z
/*     */     //   914: ifeq -> 925
/*     */     //   917: aload_0
/*     */     //   918: iconst_0
/*     */     //   919: putfield usedDebugKeyAsModifier : Z
/*     */     //   922: goto -> 971
/*     */     //   925: aload_0
/*     */     //   926: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   929: getfield debugEntries : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList;
/*     */     //   932: invokevirtual toggleDebugOverlay : ()V
/*     */     //   935: goto -> 971
/*     */     //   938: iload #7
/*     */     //   940: ifne -> 971
/*     */     //   943: aload #6
/*     */     //   945: getfield keyDebugOverlay : Lnet/minecraft/client/KeyMapping;
/*     */     //   948: aload #4
/*     */     //   950: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   953: ifeq -> 971
/*     */     //   956: iload_3
/*     */     //   957: iconst_1
/*     */     //   958: if_icmpne -> 971
/*     */     //   961: aload_0
/*     */     //   962: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   965: getfield debugEntries : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList;
/*     */     //   968: invokevirtual toggleDebugOverlay : ()V
/*     */     //   971: iload_3
/*     */     //   972: ifne -> 982
/*     */     //   975: aload #11
/*     */     //   977: iconst_0
/*     */     //   978: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   981: return
/*     */     //   982: iconst_0
/*     */     //   983: istore #14
/*     */     //   985: iload #13
/*     */     //   987: ifeq -> 1014
/*     */     //   990: aload #4
/*     */     //   992: invokevirtual isEscape : ()Z
/*     */     //   995: ifeq -> 1014
/*     */     //   998: aload_0
/*     */     //   999: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   1002: iload #8
/*     */     //   1004: invokevirtual pauseGame : (Z)V
/*     */     //   1007: iload #8
/*     */     //   1009: istore #14
/*     */     //   1011: goto -> 1144
/*     */     //   1014: iload #8
/*     */     //   1016: ifeq -> 1077
/*     */     //   1019: aload_0
/*     */     //   1020: aload #4
/*     */     //   1022: invokevirtual handleDebugKeys : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   1025: istore #14
/*     */     //   1027: iload #14
/*     */     //   1029: ifeq -> 1144
/*     */     //   1032: aload #10
/*     */     //   1034: instanceof net/minecraft/client/gui/screens/debug/DebugOptionsScreen
/*     */     //   1037: ifeq -> 1074
/*     */     //   1040: aload #10
/*     */     //   1042: checkcast net/minecraft/client/gui/screens/debug/DebugOptionsScreen
/*     */     //   1045: astore #15
/*     */     //   1047: aload #15
/*     */     //   1049: invokevirtual getOptionList : ()Lnet/minecraft/client/gui/screens/debug/DebugOptionsScreen$OptionList;
/*     */     //   1052: astore #16
/*     */     //   1054: aload #16
/*     */     //   1056: ifnull -> 1074
/*     */     //   1059: aload #16
/*     */     //   1061: invokevirtual children : ()Ljava/util/List;
/*     */     //   1064: <illegal opcode> accept : ()Ljava/util/function/Consumer;
/*     */     //   1069: invokeinterface forEach : (Ljava/util/function/Consumer;)V
/*     */     //   1074: goto -> 1144
/*     */     //   1077: iload #13
/*     */     //   1079: ifeq -> 1116
/*     */     //   1082: aload #6
/*     */     //   1084: getfield keyToggleGui : Lnet/minecraft/client/KeyMapping;
/*     */     //   1087: aload #4
/*     */     //   1089: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   1092: ifeq -> 1116
/*     */     //   1095: aload #6
/*     */     //   1097: aload #6
/*     */     //   1099: getfield hideGui : Z
/*     */     //   1102: ifne -> 1109
/*     */     //   1105: iconst_1
/*     */     //   1106: goto -> 1110
/*     */     //   1109: iconst_0
/*     */     //   1110: putfield hideGui : Z
/*     */     //   1113: goto -> 1144
/*     */     //   1116: iload #13
/*     */     //   1118: ifeq -> 1144
/*     */     //   1121: aload #6
/*     */     //   1123: getfield keyToggleSpectatorShaderEffects : Lnet/minecraft/client/KeyMapping;
/*     */     //   1126: aload #4
/*     */     //   1128: invokevirtual matches : (Lnet/minecraft/client/input/KeyEvent;)Z
/*     */     //   1131: ifeq -> 1144
/*     */     //   1134: aload_0
/*     */     //   1135: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   1138: getfield gameRenderer : Lnet/minecraft/client/renderer/GameRenderer;
/*     */     //   1141: invokevirtual togglePostEffect : ()V
/*     */     //   1144: iload #7
/*     */     //   1146: ifeq -> 1160
/*     */     //   1149: aload_0
/*     */     //   1150: dup
/*     */     //   1151: getfield usedDebugKeyAsModifier : Z
/*     */     //   1154: iload #14
/*     */     //   1156: ior
/*     */     //   1157: putfield usedDebugKeyAsModifier : Z
/*     */     //   1160: aload_0
/*     */     //   1161: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   1164: invokevirtual getDebugOverlay : ()Lnet/minecraft/client/gui/components/DebugScreenOverlay;
/*     */     //   1167: invokevirtual showProfilerChart : ()Z
/*     */     //   1170: ifeq -> 1206
/*     */     //   1173: iload #8
/*     */     //   1175: ifne -> 1206
/*     */     //   1178: aload #4
/*     */     //   1180: invokevirtual getDigit : ()I
/*     */     //   1183: istore #15
/*     */     //   1185: iload #15
/*     */     //   1187: iconst_m1
/*     */     //   1188: if_icmpeq -> 1206
/*     */     //   1191: aload_0
/*     */     //   1192: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   1195: invokevirtual getDebugOverlay : ()Lnet/minecraft/client/gui/components/DebugScreenOverlay;
/*     */     //   1198: invokevirtual getProfilerPieChart : ()Lnet/minecraft/client/gui/components/debugchart/ProfilerPieChart;
/*     */     //   1201: iload #15
/*     */     //   1203: invokevirtual profilerPieChartKeyPress : (I)V
/*     */     //   1206: iload #12
/*     */     //   1208: ifne -> 1224
/*     */     //   1211: aload #11
/*     */     //   1213: aload #6
/*     */     //   1215: getfield keyDebugModifier : Lnet/minecraft/client/KeyMapping;
/*     */     //   1218: getfield key : Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   1221: if_acmpne -> 1249
/*     */     //   1224: iload #14
/*     */     //   1226: ifeq -> 1238
/*     */     //   1229: aload #11
/*     */     //   1231: iconst_0
/*     */     //   1232: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   1235: goto -> 1249
/*     */     //   1238: aload #11
/*     */     //   1240: iconst_1
/*     */     //   1241: invokestatic set : (Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V
/*     */     //   1244: aload #11
/*     */     //   1246: invokestatic click : (Lcom/mojang/blaze3d/platform/InputConstants$Key;)V
/*     */     //   1249: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #402	-> 0
/*     */     //   #403	-> 9
/*     */     //   #404	-> 19
/*     */     //   #407	-> 20
/*     */     //   #410	-> 30
/*     */     //   #411	-> 39
/*     */     //   #412	-> 71
/*     */     //   #413	-> 81
/*     */     //   #414	-> 123
/*     */     //   #415	-> 132
/*     */     //   #416	-> 142
/*     */     //   #418	-> 152
/*     */     //   #419	-> 162
/*     */     //   #420	-> 168
/*     */     //   #421	-> 175
/*     */     //   #422	-> 182
/*     */     //   #429	-> 187
/*     */     //   #431	-> 196
/*     */     //   #432	-> 201
/*     */     //   #434	-> 252
/*     */     //   #435	-> 265
/*     */     //   #439	-> 275
/*     */     //   #440	-> 280
/*     */     //   #441	-> 312
/*     */     //   #442	-> 325
/*     */     //   #443	-> 330
/*     */     //   #444	-> 337
/*     */     //   #445	-> 350
/*     */     //   #446	-> 355
/*     */     //   #447	-> 379
/*     */     //   #449	-> 386
/*     */     //   #450	-> 387
/*     */     //   #451	-> 400
/*     */     //   #452	-> 414
/*     */     //   #454	-> 435
/*     */     //   #456	-> 458
/*     */     //   #461	-> 459
/*     */     //   #462	-> 463
/*     */     //   #463	-> 500
/*     */     //   #465	-> 505
/*     */     //   #466	-> 553
/*     */     //   #467	-> 574
/*     */     //   #468	-> 601
/*     */     //   #469	-> 606
/*     */     //   #470	-> 611
/*     */     //   #474	-> 618
/*     */     //   #482	-> 627
/*     */     //   #485	-> 632
/*     */     //   #486	-> 642
/*     */     //   #487	-> 647
/*     */     //   #490	-> 657
/*     */     //   #491	-> 667
/*     */     //   #492	-> 674
/*     */     //   #494	-> 680
/*     */     //   #496	-> 681
/*     */     //   #497	-> 685
/*     */     //   #498	-> 695
/*     */     //   #499	-> 708
/*     */     //   #501	-> 713
/*     */     //   #512	-> 714
/*     */     //   #504	-> 717
/*     */     //   #505	-> 719
/*     */     //   #506	-> 729
/*     */     //   #507	-> 736
/*     */     //   #508	-> 746
/*     */     //   #509	-> 763
/*     */     //   #510	-> 780
/*     */     //   #511	-> 797
/*     */     //   #516	-> 807
/*     */     //   #518	-> 814
/*     */     //   #521	-> 831
/*     */     //   #520	-> 836
/*     */     //   #523	-> 888
/*     */     //   #524	-> 900
/*     */     //   #528	-> 910
/*     */     //   #529	-> 917
/*     */     //   #531	-> 925
/*     */     //   #533	-> 938
/*     */     //   #534	-> 950
/*     */     //   #537	-> 961
/*     */     //   #540	-> 971
/*     */     //   #541	-> 975
/*     */     //   #542	-> 981
/*     */     //   #545	-> 982
/*     */     //   #546	-> 985
/*     */     //   #547	-> 998
/*     */     //   #548	-> 1007
/*     */     //   #549	-> 1014
/*     */     //   #550	-> 1019
/*     */     //   #551	-> 1027
/*     */     //   #552	-> 1032
/*     */     //   #553	-> 1047
/*     */     //   #554	-> 1054
/*     */     //   #555	-> 1059
/*     */     //   #557	-> 1074
/*     */     //   #559	-> 1077
/*     */     //   #560	-> 1095
/*     */     //   #561	-> 1116
/*     */     //   #562	-> 1134
/*     */     //   #565	-> 1144
/*     */     //   #566	-> 1149
/*     */     //   #569	-> 1160
/*     */     //   #570	-> 1178
/*     */     //   #571	-> 1185
/*     */     //   #572	-> 1191
/*     */     //   #576	-> 1206
/*     */     //   #578	-> 1224
/*     */     //   #579	-> 1229
/*     */     //   #581	-> 1238
/*     */     //   #582	-> 1244
/*     */     //   #585	-> 1249
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   379	7	12	videoSettingsScreen	Lnet/minecraft/client/gui/screens/options/VideoSettingsScreen;
/*     */     //   337	50	11	fullscreen	Z
/*     */     //   574	44	12	wasDisabled	Z
/*     */     //   500	127	11	hasNoEditboxFocused	Z
/*     */     //   674	6	11	key	Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   729	78	12	report	Lnet/minecraft/CrashReport;
/*     */     //   746	61	13	keyDetails	Lnet/minecraft/CrashReportCategory;
/*     */     //   719	88	11	t	Ljava/lang/Throwable;
/*     */     //   860	8	14	pauseScreen	Lnet/minecraft/client/gui/screens/PauseScreen;
/*     */     //   1054	20	16	optionList	Lnet/minecraft/client/gui/screens/debug/DebugOptionsScreen$OptionList;
/*     */     //   1047	27	15	debugOptionsScreen	Lnet/minecraft/client/gui/screens/debug/DebugOptionsScreen;
/*     */     //   1185	21	15	digit	I
/*     */     //   0	1250	0	this	Lnet/minecraft/client/KeyboardHandler;
/*     */     //   0	1250	1	handle	J
/*     */     //   0	1250	3	action	I
/*     */     //   0	1250	4	event	Lnet/minecraft/client/input/KeyEvent;
/*     */     //   9	1241	5	window	Lcom/mojang/blaze3d/platform/Window;
/*     */     //   39	1211	6	options	Lnet/minecraft/client/Options;
/*     */     //   71	1179	7	modifierAndOverlayIsSame	Z
/*     */     //   81	1169	8	debugModifierDown	Z
/*     */     //   123	1127	9	debugCrash	Z
/*     */     //   196	1054	10	screen	Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   814	436	11	key	Lcom/mojang/blaze3d/platform/InputConstants$Key;
/*     */     //   831	419	12	handlesGameInput	Z
/*     */     //   888	362	13	handlesGlobalInput	Z
/*     */     //   985	265	14	didDebugAction	Z
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   632	680	717	java/lang/Throwable
/*     */     //   681	713	717	java/lang/Throwable
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
/*     */   private void charTyped(long handle, CharacterEvent event) {
/* 588 */     if (handle != this.minecraft.getWindow().handle()) {
/*     */       return;
/*     */     }
/* 591 */     Screen screen = this.minecraft.screen;
/* 592 */     if (screen == null || this.minecraft.getOverlay() != null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 596 */       screen.charTyped(event);
/* 597 */     } catch (Throwable t) {
/* 598 */       CrashReport report = CrashReport.forThrowable(t, "charTyped event handler");
/* 599 */       screen.fillCrashDetails(report);
/* 600 */       CrashReportCategory keyDetails = report.addCategory("Key");
/* 601 */       keyDetails.setDetail("Codepoint", event.codepoint());
/* 602 */       keyDetails.setDetail("Mods", event.modifiers());
/* 603 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setup(Window window) {
/* 608 */     InputConstants.setupKeyboardCallbacks(window, (window1, keysym, scancode, action, mods) -> {
/*     */           KeyEvent event = new KeyEvent(keysym, scancode, mods);
/*     */           this.minecraft.execute(());
/*     */         }, (window1, codepoint, mods) -> {
/*     */           CharacterEvent event = new CharacterEvent(codepoint, mods);
/*     */           this.minecraft.execute(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClipboard() {
/* 621 */     return this.clipboardManager.getClipboard(this.minecraft.getWindow(), (error, description) -> {
/*     */           if (error != 65545) {
/*     */             this.minecraft.getWindow().defaultErrorCallback(error, description);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void setClipboard(String clipboard) {
/* 629 */     if (!clipboard.isEmpty()) {
/* 630 */       this.clipboardManager.setClipboard(this.minecraft.getWindow(), clipboard);
/*     */     }
/*     */   }
/*     */   
/*     */   public void tick() {
/* 635 */     if (this.debugCrashKeyTime > 0L) {
/* 636 */       long now = Util.getMillis();
/* 637 */       long remainingTime = 10000L - now - this.debugCrashKeyTime;
/* 638 */       long reportedTime = now - this.debugCrashKeyReportedTime;
/* 639 */       if (remainingTime < 0L) {
/* 640 */         if (this.minecraft.hasControlDown()) {
/* 641 */           Blaze3D.youJustLostTheGame();
/*     */         }
/* 643 */         String message = "Manually triggered debug crash";
/* 644 */         CrashReport report = new CrashReport("Manually triggered debug crash", new Throwable("Manually triggered debug crash"));
/* 645 */         CrashReportCategory manualCrashDetails = report.addCategory("Manual crash details");
/* 646 */         NativeModuleLister.addCrashSection(manualCrashDetails);
/* 647 */         throw new ReportedException(report);
/*     */       } 
/* 649 */       if (reportedTime >= 1000L) {
/* 650 */         if (this.debugCrashKeyReportedCount == 0L) {
/* 651 */           debugFeedbackTranslated("debug.crash.message", new Object[] {
/* 652 */                 this.minecraft.options.keyDebugModifier.getTranslatedKeyMessage().getString(), 
/* 653 */                 this.minecraft.options.keyDebugCrash.getTranslatedKeyMessage().getString() });
/*     */         } else {
/* 655 */           debugWarningComponent((Component)Component.translatable("debug.crash.warning", new Object[] { Mth.ceil((float)remainingTime / 1000.0F) }));
/*     */         } 
/* 657 */         this.debugCrashKeyReportedTime = now;
/* 658 */         this.debugCrashKeyReportedCount++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/KeyboardHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */