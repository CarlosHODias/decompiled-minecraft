/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AdvancementTree
/*     */ {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  20 */   private final Map<Identifier, AdvancementNode> nodes = (Map<Identifier, AdvancementNode>)new Object2ObjectOpenHashMap();
/*  21 */   private final Set<AdvancementNode> roots = (Set<AdvancementNode>)new ObjectLinkedOpenHashSet();
/*  22 */   private final Set<AdvancementNode> tasks = (Set<AdvancementNode>)new ObjectLinkedOpenHashSet();
/*     */   private Listener listener;
/*     */   
/*     */   private void remove(AdvancementNode node) {
/*  26 */     for (AdvancementNode child : node.children()) {
/*  27 */       remove(child);
/*     */     }
/*     */     
/*  30 */     LOGGER.info("Forgot about advancement {}", node.holder());
/*  31 */     this.nodes.remove(node.holder().id());
/*  32 */     if (node.parent() == null) {
/*  33 */       this.roots.remove(node);
/*  34 */       if (this.listener != null) {
/*  35 */         this.listener.onRemoveAdvancementRoot(node);
/*     */       }
/*     */     } else {
/*  38 */       this.tasks.remove(node);
/*  39 */       if (this.listener != null) {
/*  40 */         this.listener.onRemoveAdvancementTask(node);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void remove(Set<Identifier> ids) {
/*  46 */     for (Identifier id : ids) {
/*  47 */       AdvancementNode advancement = this.nodes.get(id);
/*  48 */       if (advancement == null) {
/*  49 */         LOGGER.warn("Told to remove advancement {} but I don't know what that is", id); continue;
/*     */       } 
/*  51 */       remove(advancement);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(Collection<AdvancementHolder> advancements) {
/*  57 */     List<AdvancementHolder> advancementsToAdd = new ArrayList<>(advancements);
/*  58 */     while (!advancementsToAdd.isEmpty()) {
/*  59 */       if (!advancementsToAdd.removeIf(this::tryInsert)) {
/*  60 */         LOGGER.error("Couldn't load advancements: {}", advancementsToAdd);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  65 */     LOGGER.info("Loaded {} advancements", this.nodes.size());
/*     */   }
/*     */   
/*     */   private boolean tryInsert(AdvancementHolder holder) {
/*  69 */     Optional<Identifier> parentId = holder.value().parent();
/*  70 */     Objects.requireNonNull(this.nodes); AdvancementNode parentNode = parentId.<AdvancementNode>map(this.nodes::get).orElse(null);
/*  71 */     if (parentNode == null && parentId.isPresent()) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     AdvancementNode node = new AdvancementNode(holder, parentNode);
/*  76 */     if (parentNode != null) {
/*  77 */       parentNode.addChild(node);
/*     */     }
/*     */     
/*  80 */     this.nodes.put(holder.id(), node);
/*  81 */     if (parentNode == null) {
/*  82 */       this.roots.add(node);
/*  83 */       if (this.listener != null) {
/*  84 */         this.listener.onAddAdvancementRoot(node);
/*     */       }
/*     */     } else {
/*  87 */       this.tasks.add(node);
/*  88 */       if (this.listener != null) {
/*  89 */         this.listener.onAddAdvancementTask(node);
/*     */       }
/*     */     } 
/*     */     
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  97 */     this.nodes.clear();
/*  98 */     this.roots.clear();
/*  99 */     this.tasks.clear();
/* 100 */     if (this.listener != null) {
/* 101 */       this.listener.onAdvancementsCleared();
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterable<AdvancementNode> roots() {
/* 106 */     return this.roots;
/*     */   }
/*     */   
/*     */   public Collection<AdvancementNode> nodes() {
/* 110 */     return this.nodes.values();
/*     */   }
/*     */   
/*     */   public AdvancementNode get(Identifier id) {
/* 114 */     return this.nodes.get(id);
/*     */   }
/*     */   
/*     */   public AdvancementNode get(AdvancementHolder advancement) {
/* 118 */     return this.nodes.get(advancement.id());
/*     */   }
/*     */   
/*     */   public void setListener(Listener listener) {
/* 122 */     this.listener = listener;
/* 123 */     if (listener != null) {
/* 124 */       for (AdvancementNode root : this.roots) {
/* 125 */         listener.onAddAdvancementRoot(root);
/*     */       }
/* 127 */       for (AdvancementNode task : this.tasks)
/* 128 */         listener.onAddAdvancementTask(task); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     void onAddAdvancementRoot(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onRemoveAdvancementRoot(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onAddAdvancementTask(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onRemoveAdvancementTask(AdvancementNode param1AdvancementNode);
/*     */     
/*     */     void onAdvancementsCleared();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/AdvancementTree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */