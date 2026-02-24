/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.nio.file.Path;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.DefaultedRegistry;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class RegistryDumpReport implements DataProvider {
/*    */   private final PackOutput output;
/*    */   
/*    */   public RegistryDumpReport(PackOutput output) {
/* 20 */     this.output = output;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput cache) {
/* 25 */     JsonObject root = new JsonObject();
/*    */     
/* 27 */     BuiltInRegistries.REGISTRY.listElements().forEach(e -> root.add(e.key().identifier().toString(), dumpRegistry((Registry)e.value())));
/*    */     
/* 29 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("registries.json");
/* 30 */     return DataProvider.saveStable(cache, (JsonElement)root, path);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> JsonElement dumpRegistry(Registry<T> registry) {
/* 35 */     JsonObject result = new JsonObject();
/*    */     
/* 37 */     if (registry instanceof DefaultedRegistry) {
/* 38 */       Identifier defaultKey = ((DefaultedRegistry)registry).getDefaultKey();
/* 39 */       result.addProperty("default", defaultKey.toString());
/*    */     } 
/*    */     
/* 42 */     int registryId = BuiltInRegistries.REGISTRY.getId(registry);
/* 43 */     result.addProperty("protocol_id", registryId);
/*    */     
/* 45 */     JsonObject entries = new JsonObject();
/* 46 */     registry.listElements().forEach(holder -> {
/*    */           T value = (T)holder.value();
/*    */           
/*    */           int protocolId = registry.getId(value);
/*    */           
/*    */           JsonObject entry = new JsonObject();
/*    */           entry.addProperty("protocol_id", protocolId);
/*    */           entries.add(holder.key().identifier().toString(), (JsonElement)entry);
/*    */         });
/* 55 */     result.add("entries", (JsonElement)entries);
/* 56 */     return (JsonElement)result;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 61 */     return "Registry Dump";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/info/RegistryDumpReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */