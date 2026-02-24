/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GpuWarnlistManager
/*     */   extends SimplePreparableReloadListener<GpuWarnlistManager.Preparations>
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  28 */   private static final Identifier GPU_WARNLIST_LOCATION = Identifier.withDefaultNamespace("gpu_warnlist.json");
/*     */   
/*  30 */   private ImmutableMap<String, String> warnings = ImmutableMap.of();
/*     */   private boolean showWarning;
/*     */   private boolean warningDismissed;
/*     */   
/*     */   public boolean hasWarnings() {
/*  35 */     return !this.warnings.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean willShowWarning() {
/*  39 */     return (hasWarnings() && !this.warningDismissed);
/*     */   }
/*     */   
/*     */   public void showWarning() {
/*  43 */     this.showWarning = true;
/*     */   }
/*     */   
/*     */   public void dismissWarning() {
/*  47 */     this.warningDismissed = true;
/*     */   }
/*     */   
/*     */   public boolean isShowingWarning() {
/*  51 */     return (this.showWarning && !this.warningDismissed);
/*     */   }
/*     */   
/*     */   public void resetWarnings() {
/*  55 */     this.showWarning = false;
/*  56 */     this.warningDismissed = false;
/*     */   }
/*     */   
/*     */   public String getRendererWarnings() {
/*  60 */     return (String)this.warnings.get("renderer");
/*     */   }
/*     */   
/*     */   public String getVersionWarnings() {
/*  64 */     return (String)this.warnings.get("version");
/*     */   }
/*     */   
/*     */   public String getVendorWarnings() {
/*  68 */     return (String)this.warnings.get("vendor");
/*     */   }
/*     */   
/*     */   public String getAllWarnings() {
/*  72 */     StringBuilder sb = new StringBuilder();
/*  73 */     this.warnings.forEach((k, v) -> sb.append(k).append(": ").append(v));
/*  74 */     return sb.isEmpty() ? null : sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Preparations prepare(ResourceManager manager, ProfilerFiller profiler) {
/*  79 */     List<Pattern> rendererPatterns = Lists.newArrayList();
/*  80 */     List<Pattern> versionPatterns = Lists.newArrayList();
/*  81 */     List<Pattern> vendorPatterns = Lists.newArrayList();
/*     */     
/*  83 */     JsonObject root = parseJson(manager, profiler);
/*  84 */     if (root != null) {
/*  85 */       Zone ignored = profiler.zone("compile_regex"); 
/*  86 */       try { compilePatterns(root.getAsJsonArray("renderer"), rendererPatterns);
/*  87 */         compilePatterns(root.getAsJsonArray("version"), versionPatterns);
/*  88 */         compilePatterns(root.getAsJsonArray("vendor"), vendorPatterns);
/*  89 */         if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*     */           try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */     
/*  92 */     }  return new Preparations(rendererPatterns, versionPatterns, vendorPatterns);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void apply(Preparations preparations, ResourceManager manager, ProfilerFiller profiler) {
/*  97 */     this.warnings = preparations.apply();
/*     */   }
/*     */   
/*     */   private static void compilePatterns(JsonArray jsonArray, List<Pattern> patternList) {
/* 101 */     jsonArray.forEach(e -> patternList.add(Pattern.compile(e.getAsString(), 2)));
/*     */   }
/*     */   
/*     */   private static JsonObject parseJson(ResourceManager manager, ProfilerFiller profiler) {
/*     */     
/* 106 */     try { Zone ignored = profiler.zone("parse_json"); 
/* 107 */       try { Reader resource = manager.openAsReader(GPU_WARNLIST_LOCATION);
/*     */         
/* 109 */         try { JsonObject jsonObject = StrictJsonParser.parse(resource).getAsJsonObject();
/* 110 */           if (resource != null) resource.close();  if (ignored != null) ignored.close();  return jsonObject; } catch (Throwable throwable) { if (resource != null) try { resource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (ignored != null) try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonSyntaxException e)
/* 111 */     { LOGGER.warn("Failed to load GPU warnlist", e);
/*     */       
/* 113 */       return null; }
/*     */   
/*     */   }
/*     */   
/*     */   protected static final class Preparations { private final List<Pattern> rendererPatterns;
/*     */     private final List<Pattern> versionPatterns;
/*     */     private final List<Pattern> vendorPatterns;
/*     */     
/*     */     private Preparations(List<Pattern> rendererPatterns, List<Pattern> versionPatterns, List<Pattern> vendorPatterns) {
/* 122 */       this.rendererPatterns = rendererPatterns;
/* 123 */       this.versionPatterns = versionPatterns;
/* 124 */       this.vendorPatterns = vendorPatterns;
/*     */     }
/*     */     
/*     */     private static String matchAny(List<Pattern> patterns, String input) {
/* 128 */       List<String> allMatches = Lists.newArrayList();
/* 129 */       for (Pattern pattern : patterns) {
/* 130 */         Matcher matcher = pattern.matcher(input);
/* 131 */         while (matcher.find()) {
/* 132 */           allMatches.add(matcher.group());
/*     */         }
/*     */       } 
/* 135 */       return String.join(", ", (Iterable)allMatches);
/*     */     }
/*     */     
/*     */     private ImmutableMap<String, String> apply() {
/* 139 */       ImmutableMap.Builder<String, String> map = new ImmutableMap.Builder();
/* 140 */       GpuDevice device = RenderSystem.getDevice();
/* 141 */       if (device.getBackendName().equals("OpenGL")) {
/* 142 */         String rendererFails = matchAny(this.rendererPatterns, device.getRenderer());
/* 143 */         if (!rendererFails.isEmpty()) {
/* 144 */           map.put("renderer", rendererFails);
/*     */         }
/*     */         
/* 147 */         String versionFails = matchAny(this.versionPatterns, device.getVersion());
/* 148 */         if (!versionFails.isEmpty()) {
/* 149 */           map.put("version", versionFails);
/*     */         }
/*     */         
/* 152 */         String vendorFails = matchAny(this.vendorPatterns, device.getVendor());
/* 153 */         if (!vendorFails.isEmpty()) {
/* 154 */           map.put("vendor", vendorFails);
/*     */         }
/*     */       } 
/*     */       
/* 158 */       return map.build();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/GpuWarnlistManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */