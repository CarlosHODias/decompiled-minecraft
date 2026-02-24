/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.worldupdate.WorldUpgrader;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class OptimizeWorldScreen extends Screen {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final ToIntFunction<ResourceKey<Level>> DIMENSION_COLORS; private final BooleanConsumer callback; private final WorldUpgrader upgrader;
/*     */   static {
/*  33 */     DIMENSION_COLORS = (ToIntFunction<ResourceKey<Level>>)Util.make(new Reference2IntOpenHashMap(), map -> {
/*     */           map.put(Level.OVERWORLD, -13408734);
/*     */           map.put(Level.NETHER, -10075085);
/*     */           map.put(Level.END, -8943531);
/*     */           map.defaultReturnValue(-2236963);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptimizeWorldScreen create(Minecraft minecraft, BooleanConsumer callback, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelSourceAccess, boolean eraseCache) {
/*     */     
/*  45 */     try { WorldOpenFlows worldOpenFlows = minecraft.createWorldOpenFlows();
/*  46 */       PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/*  47 */       WorldStem worldStem = worldOpenFlows.loadWorldStem(levelSourceAccess.getDataTag(), false, packRepository); 
/*  48 */       try { WorldData worldData = worldStem.worldData();
/*  49 */         RegistryAccess.Frozen registryAccess = worldStem.registries().compositeAccess();
/*  50 */         levelSourceAccess.saveDataTag((RegistryAccess)registryAccess, worldData);
/*  51 */         OptimizeWorldScreen optimizeWorldScreen = new OptimizeWorldScreen(callback, dataFixer, levelSourceAccess, worldData, eraseCache, (RegistryAccess)registryAccess);
/*  52 */         if (worldStem != null) worldStem.close();  return optimizeWorldScreen; } catch (Throwable throwable) { if (worldStem != null)
/*  53 */           try { worldStem.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  54 */     { LOGGER.warn("Failed to load datapacks, can't optimize world", e);
/*  55 */       return null; }
/*     */   
/*     */   }
/*     */   
/*     */   private OptimizeWorldScreen(BooleanConsumer callback, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelSource, WorldData worldData, boolean eraseCache, RegistryAccess registryAccess) {
/*  60 */     super((Component)Component.translatable("optimizeWorld.title", new Object[] { worldData.getLevelSettings().levelName() }));
/*  61 */     this.callback = callback;
/*  62 */     this.upgrader = new WorldUpgrader(levelSource, dataFixer, worldData, registryAccess, eraseCache, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  67 */     super.init();
/*     */     
/*  69 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> {
/*     */             this.upgrader.cancel();
/*     */             this.callback.accept(false);
/*  72 */           }).bounds(this.width / 2 - 100, this.height / 4 + 150, 200, 20).build());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  77 */     if (this.upgrader.isFinished()) {
/*  78 */       this.callback.accept(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  84 */     this.callback.accept(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/*  89 */     this.upgrader.cancel();
/*  90 */     this.upgrader.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  95 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/*  97 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, -1);
/*     */     
/*  99 */     int x0 = this.width / 2 - 150;
/* 100 */     int x1 = this.width / 2 + 150;
/* 101 */     int y0 = this.height / 4 + 100;
/* 102 */     int y1 = y0 + 10;
/*     */     
/* 104 */     Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, this.upgrader.getStatus(), this.width / 2, y0 - 9 - 2, -6250336);
/*     */     
/* 106 */     if (this.upgrader.getTotalChunks() > 0) {
/* 107 */       graphics.fill(x0 - 1, y0 - 1, x1 + 1, y1 + 1, -16777216);
/*     */       
/* 109 */       graphics.drawString(this.font, (Component)Component.translatable("optimizeWorld.info.converted", new Object[] { this.upgrader.getConverted() }), x0, 40, -6250336);
/* 110 */       Objects.requireNonNull(this.font); graphics.drawString(this.font, (Component)Component.translatable("optimizeWorld.info.skipped", new Object[] { this.upgrader.getSkipped() }), x0, 40 + 9 + 3, -6250336);
/* 111 */       Objects.requireNonNull(this.font); graphics.drawString(this.font, (Component)Component.translatable("optimizeWorld.info.total", new Object[] { this.upgrader.getTotalChunks() }), x0, 40 + (9 + 3) * 2, -6250336);
/*     */       
/* 113 */       int progress = 0;
/* 114 */       for (ResourceKey<Level> dimension : (Iterable<ResourceKey<Level>>)this.upgrader.levels()) {
/* 115 */         int length = Mth.floor(this.upgrader.dimensionProgress(dimension) * (x1 - x0));
/* 116 */         graphics.fill(x0 + progress, y0, x0 + progress + length, y1, DIMENSION_COLORS.applyAsInt(dimension));
/* 117 */         progress += length;
/*     */       } 
/*     */       
/* 120 */       int totalProgress = this.upgrader.getConverted() + this.upgrader.getSkipped();
/*     */       
/* 122 */       MutableComponent mutableComponent1 = Component.translatable("optimizeWorld.progress.counter", new Object[] { totalProgress, this.upgrader.getTotalChunks() });
/* 123 */       MutableComponent mutableComponent2 = Component.translatable("optimizeWorld.progress.percentage", new Object[] { Mth.floor(this.upgrader.getProgress() * 100.0F) });
/* 124 */       Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, (Component)mutableComponent1, this.width / 2, y0 + 2 * 9 + 2, -6250336);
/* 125 */       Objects.requireNonNull(this.font); graphics.drawCenteredString(this.font, (Component)mutableComponent2, this.width / 2, y0 + (y1 - y0) / 2 - 9 / 2, -6250336);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/OptimizeWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */