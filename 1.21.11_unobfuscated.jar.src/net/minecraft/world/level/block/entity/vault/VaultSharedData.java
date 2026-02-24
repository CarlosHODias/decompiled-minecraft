/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class VaultSharedData {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ItemStack.lenientOptionalFieldOf("display_item").forGetter(()), (App)UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("connected_players", Set.of()).forGetter(()), (App)Codec.DOUBLE.lenientOptionalFieldOf("connected_particles_range", VaultConfig.DEFAULT.deactivationRange()).forGetter(())).apply((Applicative)i, VaultSharedData::new));
/*    */   }
/*    */ 
/*    */   
/*    */   static final String TAG_NAME = "shared_data";
/*    */   static Codec<VaultSharedData> CODEC;
/* 23 */   private ItemStack displayItem = ItemStack.EMPTY;
/* 24 */   private Set<UUID> connectedPlayers = (Set<UUID>)new it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet();
/* 25 */   private double connectedParticlesRange = VaultConfig.DEFAULT.deactivationRange();
/*    */   
/*    */   boolean isDirty;
/*    */   
/*    */   VaultSharedData(ItemStack displayItem, Set<UUID> connectedPlayers, double connectedParticlesRange) {
/* 30 */     this.displayItem = displayItem;
/* 31 */     this.connectedPlayers.addAll(connectedPlayers);
/* 32 */     this.connectedParticlesRange = connectedParticlesRange;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getDisplayItem() {
/* 39 */     return this.displayItem;
/*    */   }
/*    */   
/*    */   public boolean hasDisplayItem() {
/* 43 */     return !this.displayItem.isEmpty();
/*    */   }
/*    */   
/*    */   public void setDisplayItem(ItemStack stack) {
/* 47 */     if (ItemStack.matches(this.displayItem, stack)) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     this.displayItem = stack.copy();
/* 52 */     markDirty();
/*    */   }
/*    */   
/*    */   boolean hasConnectedPlayers() {
/* 56 */     return !this.connectedPlayers.isEmpty();
/*    */   }
/*    */   
/*    */   Set<UUID> getConnectedPlayers() {
/* 60 */     return this.connectedPlayers;
/*    */   }
/*    */   
/*    */   double connectedParticlesRange() {
/* 64 */     return this.connectedParticlesRange;
/*    */   }
/*    */   
/*    */   void updateConnectedPlayersWithinRange(ServerLevel serverLevel, BlockPos pos, VaultServerData serverData, VaultConfig config, double limit) {
/* 68 */     Set<UUID> currentConnectedPlayers = (Set<UUID>)config.playerDetector().detect(serverLevel, config.entitySelector(), pos, limit, false)
/* 69 */       .stream()
/* 70 */       .filter(uuid -> !serverData.getRewardedPlayers().contains(uuid))
/* 71 */       .collect(Collectors.toSet());
/*    */     
/* 73 */     if (!this.connectedPlayers.equals(currentConnectedPlayers)) {
/* 74 */       this.connectedPlayers = currentConnectedPlayers;
/* 75 */       markDirty();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void markDirty() {
/* 80 */     this.isDirty = true;
/*    */   }
/*    */   
/*    */   void set(VaultSharedData from) {
/* 84 */     this.displayItem = from.displayItem;
/* 85 */     this.connectedPlayers = from.connectedPlayers;
/* 86 */     this.connectedParticlesRange = from.connectedParticlesRange;
/*    */   }
/*    */   
/*    */   VaultSharedData() {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/vault/VaultSharedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */