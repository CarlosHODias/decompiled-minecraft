/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.packs.PackLocationInfo;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.server.packs.repository.Pack;
/*    */ import net.minecraft.server.packs.repository.PackRepository;
/*    */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*    */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*    */ import net.minecraft.server.packs.resources.MultiPackResourceManager;
/*    */ 
/*    */ public class KnownPacksManager {
/* 18 */   private final PackRepository repository = ServerPacksSource.createVanillaTrustedRepository();
/*    */   
/*    */   private final Map<KnownPack, String> knownPackToId;
/*    */   
/*    */   public KnownPacksManager() {
/* 23 */     this.repository.reload();
/* 24 */     ImmutableMap.Builder<KnownPack, String> knownPacks = ImmutableMap.builder();
/* 25 */     this.repository.getAvailablePacks().forEach(pack -> {
/*    */           PackLocationInfo location = pack.location();
/*    */           location.knownPackInfo().ifPresent(());
/*    */         });
/* 29 */     this.knownPackToId = (Map<KnownPack, String>)knownPacks.build();
/*    */   }
/*    */   
/*    */   public List<KnownPack> trySelectingPacks(List<KnownPack> packsToSelect) {
/* 33 */     List<KnownPack> response = new ArrayList<>(packsToSelect.size());
/* 34 */     List<String> selectedPacks = new ArrayList<>(packsToSelect.size());
/* 35 */     for (KnownPack knownPack : packsToSelect) {
/* 36 */       String knownPackId = this.knownPackToId.get(knownPack);
/* 37 */       if (knownPackId != null) {
/* 38 */         selectedPacks.add(knownPackId);
/* 39 */         response.add(knownPack);
/*    */       } 
/*    */     } 
/* 42 */     this.repository.setSelected(selectedPacks);
/* 43 */     return response;
/*    */   }
/*    */   
/*    */   public CloseableResourceManager createResourceManager() {
/* 47 */     List<PackResources> openedPacks = this.repository.openAllSelected();
/* 48 */     return (CloseableResourceManager)new MultiPackResourceManager(PackType.SERVER_DATA, openedPacks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/KnownPacksManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */