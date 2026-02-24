/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.AdvancementTree;
/*     */ import net.minecraft.advancements.DisplayInfo;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.AdvancementToast;
/*     */ import net.minecraft.client.gui.components.toasts.Toast;
/*     */ import net.minecraft.client.telemetry.WorldSessionTelemetryManager;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ClientAdvancements {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   private final WorldSessionTelemetryManager telemetryManager;
/*  27 */   private final AdvancementTree tree = new AdvancementTree();
/*     */   
/*  29 */   private final Map<AdvancementHolder, AdvancementProgress> progress = (Map<AdvancementHolder, AdvancementProgress>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private Listener listener;
/*     */   private AdvancementHolder selectedTab;
/*     */   
/*     */   public ClientAdvancements(Minecraft minecraft, WorldSessionTelemetryManager telemetryManager) {
/*  35 */     this.minecraft = minecraft;
/*  36 */     this.telemetryManager = telemetryManager;
/*     */   }
/*     */   
/*     */   public void update(ClientboundUpdateAdvancementsPacket packet) {
/*  40 */     if (packet.shouldReset()) {
/*  41 */       this.tree.clear();
/*  42 */       this.progress.clear();
/*     */     } 
/*     */     
/*  45 */     this.tree.remove(packet.getRemoved());
/*  46 */     this.tree.addAll(packet.getAdded());
/*  47 */     for (Map.Entry<Identifier, AdvancementProgress> entry : (Iterable<Map.Entry<Identifier, AdvancementProgress>>)packet.getProgress().entrySet()) {
/*  48 */       AdvancementNode node = this.tree.get(entry.getKey());
/*  49 */       if (node != null) {
/*  50 */         AdvancementProgress progress = entry.getValue();
/*  51 */         progress.update(node.advancement().requirements());
/*  52 */         this.progress.put(node.holder(), progress);
/*  53 */         if (this.listener != null) {
/*  54 */           this.listener.onUpdateAdvancementProgress(node, progress);
/*     */         }
/*  56 */         if (!packet.shouldReset() && progress.isDone()) {
/*  57 */           if (this.minecraft.level != null) {
/*  58 */             this.telemetryManager.onAdvancementDone(this.minecraft.level, node.holder());
/*     */           }
/*  60 */           Optional<DisplayInfo> display = node.advancement().display();
/*  61 */           if (packet.shouldShowAdvancements() && display.isPresent() && ((DisplayInfo)display.get()).shouldShowToast())
/*  62 */             this.minecraft.getToastManager().addToast((Toast)new AdvancementToast(node.holder())); 
/*     */         } 
/*     */         continue;
/*     */       } 
/*  66 */       LOGGER.warn("Server informed client about progress for unknown advancement {}", entry.getKey());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AdvancementTree getTree() {
/*  72 */     return this.tree;
/*     */   }
/*     */   
/*     */   public void setSelectedTab(AdvancementHolder selectedTab, boolean tellServer) {
/*  76 */     ClientPacketListener connection = this.minecraft.getConnection();
/*  77 */     if (connection != null && selectedTab != null && tellServer) {
/*  78 */       connection.send((Packet<?>)ServerboundSeenAdvancementsPacket.openedTab(selectedTab));
/*     */     }
/*  80 */     if (this.selectedTab != selectedTab) {
/*  81 */       this.selectedTab = selectedTab;
/*  82 */       if (this.listener != null) {
/*  83 */         this.listener.onSelectedTabChanged(selectedTab);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setListener(Listener listener) {
/*  89 */     this.listener = listener;
/*  90 */     this.tree.setListener(listener);
/*  91 */     if (listener != null) {
/*  92 */       this.progress.forEach((holder, progress) -> {
/*     */             AdvancementNode node = this.tree.get(listener);
/*     */             if (node != null) {
/*     */               listener.onUpdateAdvancementProgress(node, progress);
/*     */             }
/*     */           });
/*  98 */       listener.onSelectedTabChanged(this.selectedTab);
/*     */     } 
/*     */   }
/*     */   
/*     */   public AdvancementHolder get(Identifier id) {
/* 103 */     AdvancementNode node = this.tree.get(id);
/* 104 */     return (node != null) ? node.holder() : null;
/*     */   }
/*     */   
/*     */   public static interface Listener extends AdvancementTree.Listener {
/*     */     void onUpdateAdvancementProgress(AdvancementNode param1AdvancementNode, AdvancementProgress param1AdvancementProgress);
/*     */     
/*     */     void onSelectedTabChanged(AdvancementHolder param1AdvancementHolder);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */