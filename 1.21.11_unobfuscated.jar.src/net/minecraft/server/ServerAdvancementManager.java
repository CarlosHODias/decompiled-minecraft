/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementNode;
/*    */ import net.minecraft.advancements.AdvancementTree;
/*    */ import net.minecraft.advancements.TreeNodePosition;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ServerAdvancementManager
/*    */   extends SimpleJsonResourceReloadListener<Advancement> {
/* 24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 26 */   private Map<Identifier, AdvancementHolder> advancements = Map.of();
/* 27 */   private AdvancementTree tree = new AdvancementTree();
/*    */   
/*    */   private final HolderLookup.Provider registries;
/*    */   
/*    */   public ServerAdvancementManager(HolderLookup.Provider registries) {
/* 32 */     super(registries, Advancement.CODEC, Registries.ADVANCEMENT);
/* 33 */     this.registries = registries;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void apply(Map<Identifier, Advancement> preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 38 */     ImmutableMap.Builder<Identifier, AdvancementHolder> builder = ImmutableMap.builder();
/* 39 */     preparations.forEach((id, advancement) -> {
/*    */           validate(builder, advancement);
/*    */           
/*    */           builder.put(builder, new AdvancementHolder(builder, advancement));
/*    */         });
/* 44 */     this.advancements = (Map<Identifier, AdvancementHolder>)builder.buildOrThrow();
/*    */     
/* 46 */     AdvancementTree tree = new AdvancementTree();
/* 47 */     tree.addAll(this.advancements.values());
/*    */     
/* 49 */     for (AdvancementNode root : (Iterable<AdvancementNode>)tree.roots()) {
/* 50 */       if (root.holder().value().display().isPresent()) {
/* 51 */         TreeNodePosition.run(root);
/*    */       }
/*    */     } 
/*    */     
/* 55 */     this.tree = tree;
/*    */   }
/*    */   
/*    */   private void validate(Identifier id, Advancement advancement) {
/* 59 */     ProblemReporter.Collector problemCollector = new ProblemReporter.Collector();
/* 60 */     advancement.validate((ProblemReporter)problemCollector, (HolderGetter.Provider)this.registries);
/*    */     
/* 62 */     if (!problemCollector.isEmpty()) {
/* 63 */       LOGGER.warn("Found validation problems in advancement {}: \n{}", id, problemCollector.getReport());
/*    */     }
/*    */   }
/*    */   
/*    */   public AdvancementHolder get(Identifier id) {
/* 68 */     return this.advancements.get(id);
/*    */   }
/*    */   
/*    */   public AdvancementTree tree() {
/* 72 */     return this.tree;
/*    */   }
/*    */   
/*    */   public Collection<AdvancementHolder> getAllAdvancements() {
/* 76 */     return this.advancements.values();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/ServerAdvancementManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */