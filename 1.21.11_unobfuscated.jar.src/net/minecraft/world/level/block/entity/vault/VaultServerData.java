/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class VaultServerData {
/*     */   static {
/*  21 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("rewarded_players", Set.of()).forGetter(()), (App)Codec.LONG.lenientOptionalFieldOf("state_updating_resumes_at", 0L).forGetter(()), (App)ItemStack.CODEC.listOf().lenientOptionalFieldOf("items_to_eject", List.of()).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("total_ejections_needed", 0).forGetter(())).apply((Applicative)i, VaultServerData::new));
/*     */   }
/*     */ 
/*     */   
/*     */   static final String TAG_NAME = "server_data";
/*     */   
/*     */   static Codec<VaultServerData> CODEC;
/*     */   
/*     */   private static final int MAX_REWARD_PLAYERS = 128;
/*  30 */   private final Set<UUID> rewardedPlayers = (Set<UUID>)new it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet();
/*     */   private long stateUpdatingResumesAt;
/*  32 */   private final List<ItemStack> itemsToEject = (List<ItemStack>)new ObjectArrayList();
/*     */   private long lastInsertFailTimestamp;
/*     */   private int totalEjectionsNeeded;
/*     */   boolean isDirty;
/*     */   
/*     */   VaultServerData(Set<UUID> rewardedPlayers, long stateUpdatingResumesAt, List<ItemStack> itemsToEject, int totalEjectionsNeeded) {
/*  38 */     this.rewardedPlayers.addAll(rewardedPlayers);
/*  39 */     this.stateUpdatingResumesAt = stateUpdatingResumesAt;
/*  40 */     this.itemsToEject.addAll(itemsToEject);
/*  41 */     this.totalEjectionsNeeded = totalEjectionsNeeded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setLastInsertFailTimestamp(long lastInsertFailTimestamp) {
/*  48 */     this.lastInsertFailTimestamp = lastInsertFailTimestamp;
/*     */   }
/*     */   
/*     */   long getLastInsertFailTimestamp() {
/*  52 */     return this.lastInsertFailTimestamp;
/*     */   }
/*     */   
/*     */   Set<UUID> getRewardedPlayers() {
/*  56 */     return this.rewardedPlayers;
/*     */   }
/*     */   
/*     */   boolean hasRewardedPlayer(Player player) {
/*  60 */     return this.rewardedPlayers.contains(player.getUUID());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void addToRewardedPlayers(Player player) {
/*  65 */     this.rewardedPlayers.add(player.getUUID());
/*     */     
/*  67 */     if (this.rewardedPlayers.size() > 128) {
/*  68 */       Iterator<UUID> iterator = this.rewardedPlayers.iterator();
/*  69 */       if (iterator.hasNext()) {
/*  70 */         iterator.next();
/*  71 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     markChanged();
/*     */   }
/*     */   
/*     */   long stateUpdatingResumesAt() {
/*  79 */     return this.stateUpdatingResumesAt;
/*     */   }
/*     */   
/*     */   void pauseStateUpdatingUntil(long stateUpdatingResumesAt) {
/*  83 */     this.stateUpdatingResumesAt = stateUpdatingResumesAt;
/*  84 */     markChanged();
/*     */   }
/*     */   
/*     */   List<ItemStack> getItemsToEject() {
/*  88 */     return this.itemsToEject;
/*     */   }
/*     */   
/*     */   void markEjectionFinished() {
/*  92 */     this.totalEjectionsNeeded = 0;
/*  93 */     markChanged();
/*     */   }
/*     */   
/*     */   void setItemsToEject(List<ItemStack> newItemsToEject) {
/*  97 */     this.itemsToEject.clear();
/*  98 */     this.itemsToEject.addAll(newItemsToEject);
/*  99 */     this.totalEjectionsNeeded = this.itemsToEject.size();
/* 100 */     markChanged();
/*     */   }
/*     */   
/*     */   ItemStack getNextItemToEject() {
/* 104 */     if (this.itemsToEject.isEmpty()) {
/* 105 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 108 */     return Objects.<ItemStack>requireNonNullElse(this.itemsToEject.get(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   ItemStack popNextItemToEject() {
/* 112 */     if (this.itemsToEject.isEmpty()) {
/* 113 */       return ItemStack.EMPTY;
/*     */     }
/* 115 */     markChanged();
/*     */     
/* 117 */     return Objects.<ItemStack>requireNonNullElse(this.itemsToEject.remove(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   void set(VaultServerData from) {
/* 121 */     this.stateUpdatingResumesAt = from.stateUpdatingResumesAt();
/* 122 */     this.itemsToEject.clear();
/* 123 */     this.itemsToEject.addAll(from.itemsToEject);
/* 124 */     this.rewardedPlayers.clear();
/* 125 */     this.rewardedPlayers.addAll(from.rewardedPlayers);
/*     */   }
/*     */   
/*     */   private void markChanged() {
/* 129 */     this.isDirty = true;
/*     */   }
/*     */   
/*     */   public float ejectionProgress() {
/* 133 */     if (this.totalEjectionsNeeded == 1) {
/* 134 */       return 1.0F;
/*     */     }
/*     */     
/* 137 */     return 1.0F - Mth.inverseLerp(getItemsToEject().size(), 1.0F, this.totalEjectionsNeeded);
/*     */   }
/*     */   
/*     */   VaultServerData() {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/vault/VaultServerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */