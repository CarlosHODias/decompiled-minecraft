/*     */ package net.minecraft.client.renderer.texture;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.gui.screens.AddRealmPopupScreen;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextureManager implements PreparableReloadListener, AutoCloseable {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  31 */   public static final Identifier INTENTIONAL_MISSING_TEXTURE = Identifier.withDefaultNamespace("");
/*     */   
/*  33 */   private final Map<Identifier, AbstractTexture> byPath = new HashMap<>();
/*     */   
/*  35 */   private final Set<TickableTexture> tickableTextures = new HashSet<>();
/*     */   
/*     */   private final ResourceManager resourceManager;
/*     */   
/*     */   public TextureManager(ResourceManager resourceManager) {
/*  40 */     this.resourceManager = resourceManager;
/*     */     
/*  42 */     NativeImage checkerboard = MissingTextureAtlasSprite.generateMissingImage();
/*  43 */     register(MissingTextureAtlasSprite.getLocation(), new DynamicTexture(() -> "(intentionally-)Missing Texture", checkerboard));
/*     */   }
/*     */   
/*     */   public void registerAndLoad(Identifier textureId, ReloadableTexture texture) {
/*     */     try {
/*  48 */       texture.apply(loadContentsSafe(textureId, texture));
/*  49 */     } catch (Throwable t) {
/*  50 */       CrashReport report = CrashReport.forThrowable(t, "Uploading texture");
/*  51 */       CrashReportCategory category = report.addCategory("Uploaded texture");
/*  52 */       category.setDetail("Resource location", texture.resourceId());
/*  53 */       category.setDetail("Texture id", textureId);
/*  54 */       throw new ReportedException(report);
/*     */     } 
/*     */     
/*  57 */     register(textureId, texture);
/*     */   }
/*     */   
/*     */   private TextureContents loadContentsSafe(Identifier textureId, ReloadableTexture texture) {
/*     */     try {
/*  62 */       return loadContents(this.resourceManager, textureId, texture);
/*  63 */     } catch (Exception e) {
/*     */       
/*  65 */       LOGGER.error("Failed to load texture {} into slot {}", new Object[] { texture.resourceId(), textureId, e });
/*  66 */       return TextureContents.createMissing();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerForNextReload(Identifier location) {
/*  74 */     register(location, new SimpleTexture(location));
/*     */   }
/*     */   
/*     */   public void register(Identifier location, AbstractTexture texture) {
/*  78 */     AbstractTexture prev = this.byPath.put(location, texture);
/*  79 */     if (prev != texture) {
/*  80 */       if (prev != null) {
/*  81 */         safeClose(location, prev);
/*     */       }
/*  83 */       if (texture instanceof TickableTexture) { TickableTexture tickableTexture = (TickableTexture)texture;
/*  84 */         this.tickableTextures.add(tickableTexture); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void safeClose(Identifier id, AbstractTexture texture) {
/*  91 */     this.tickableTextures.remove(texture);
/*     */     try {
/*  93 */       texture.close();
/*  94 */     } catch (Exception e) {
/*  95 */       LOGGER.warn("Failed to close texture {}", id, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public AbstractTexture getTexture(Identifier location) {
/* 100 */     AbstractTexture textureObject = this.byPath.get(location);
/* 101 */     if (textureObject != null) {
/* 102 */       return textureObject;
/*     */     }
/*     */ 
/*     */     
/* 106 */     SimpleTexture texture = new SimpleTexture(location);
/* 107 */     registerAndLoad(location, texture);
/* 108 */     return texture;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 112 */     for (TickableTexture tickableTexture : this.tickableTextures) {
/* 113 */       tickableTexture.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   public void release(Identifier location) {
/* 118 */     AbstractTexture texture = this.byPath.remove(location);
/* 119 */     if (texture != null) {
/* 120 */       safeClose(location, texture);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 126 */     this.byPath.forEach(this::safeClose);
/* 127 */     this.byPath.clear();
/* 128 */     this.tickableTextures.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 133 */     ResourceManager manager = currentReload.resourceManager();
/* 134 */     List<PendingReload> reloads = new ArrayList<>();
/* 135 */     this.byPath.forEach((id, texture) -> {
/*     */           if (texture instanceof ReloadableTexture) {
/*     */             ReloadableTexture reloadableTexture = (ReloadableTexture)texture;
/*     */             
/*     */             reloads.add(scheduleLoad(manager, id, reloadableTexture, taskExecutor));
/*     */           } 
/*     */         });
/* 142 */     Objects.requireNonNull(preparationBarrier); return CompletableFuture.allOf((CompletableFuture<?>[])reloads.stream().map(PendingReload::newContents).toArray(x$0 -> new CompletableFuture[x$0])).thenCompose(preparationBarrier::wait)
/* 143 */       .thenAcceptAsync(unused -> {
/*     */           AddRealmPopupScreen.updateCarouselImages(this.resourceManager);
/*     */           for (PendingReload reload : (Iterable<PendingReload>)reloads) {
/*     */             reload.texture.apply(reload.newContents.join());
/*     */           }
/*     */         }, reloadExecutor);
/*     */   }
/*     */   
/*     */   public void dumpAllSheets(Path targetDir) {
/*     */     try {
/* 153 */       Files.createDirectories(targetDir, (FileAttribute<?>[])new FileAttribute[0]);
/* 154 */     } catch (IOException e) {
/* 155 */       LOGGER.error("Failed to create directory {}", targetDir, e);
/*     */       
/*     */       return;
/*     */     } 
/* 159 */     this.byPath.forEach((location, texture) -> {
/*     */           if (texture instanceof Dumpable) {
/*     */             Dumpable dumpable = (Dumpable)texture; try {
/*     */               dumpable.dumpContents(location, targetDir);
/* 163 */             } catch (Exception e) {
/*     */               LOGGER.error("Failed to dump texture {}", location, e);
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static TextureContents loadContents(ResourceManager manager, Identifier location, ReloadableTexture texture) throws IOException {
/*     */     try {
/* 172 */       return texture.loadContents(manager);
/* 173 */     } catch (FileNotFoundException e) {
/* 174 */       if (location != INTENTIONAL_MISSING_TEXTURE) {
/* 175 */         LOGGER.warn("Missing resource {} referenced from {}", texture.resourceId(), location);
/*     */       }
/* 177 */       return TextureContents.createMissing();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static PendingReload scheduleLoad(ResourceManager manager, Identifier location, ReloadableTexture texture, Executor executor) {
/* 182 */     return new PendingReload(texture, CompletableFuture.supplyAsync(() -> {
/*     */             try {
/*     */               return loadContents(manager, location, texture);
/* 185 */             } catch (IOException e) {
/*     */               throw new UncheckedIOException(e);
/*     */             } 
/*     */           }, executor));
/*     */   }
/*     */   private static final class PendingReload extends Record { private final ReloadableTexture texture; private final CompletableFuture<TextureContents> newContents;
/* 191 */     private PendingReload(ReloadableTexture texture, CompletableFuture<TextureContents> newContents) { this.texture = texture; this.newContents = newContents; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #191	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 191 */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload; } public ReloadableTexture texture() { return this.texture; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #191	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #191	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/TextureManager$PendingReload;
/* 191 */       //   0	8	1	o	Ljava/lang/Object; } public CompletableFuture<TextureContents> newContents() { return this.newContents; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/TextureManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */