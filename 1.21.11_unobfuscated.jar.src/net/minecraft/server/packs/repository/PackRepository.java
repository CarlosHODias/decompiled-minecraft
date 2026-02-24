/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ public class PackRepository
/*     */ {
/*     */   private final Set<RepositorySource> sources;
/*  23 */   private Map<String, Pack> available = (Map<String, Pack>)ImmutableMap.of();
/*  24 */   private List<Pack> selected = (List<Pack>)ImmutableList.of();
/*     */   
/*     */   public PackRepository(RepositorySource... sources) {
/*  27 */     this.sources = (Set<RepositorySource>)ImmutableSet.copyOf((Object[])sources);
/*     */   }
/*     */   
/*     */   public static String displayPackList(Collection<Pack> packs) {
/*  31 */     return packs.stream().map(pack -> pack.getId() + pack.getId()).collect(Collectors.joining(", "));
/*     */   }
/*     */   
/*     */   public void reload() {
/*  35 */     List<String> currentlySelectedNames = (List<String>)this.selected.stream().map(Pack::getId).collect(ImmutableList.toImmutableList());
/*  36 */     this.available = discoverAvailable();
/*  37 */     this.selected = rebuildSelected(currentlySelectedNames);
/*     */   }
/*     */   
/*     */   private Map<String, Pack> discoverAvailable() {
/*  41 */     Map<String, Pack> discovered = Maps.newTreeMap();
/*  42 */     for (RepositorySource source : this.sources) {
/*  43 */       source.loadPacks(pack -> discovered.put(pack.getId(), pack));
/*     */     }
/*  45 */     return (Map<String, Pack>)ImmutableMap.copyOf(discovered);
/*     */   }
/*     */   
/*     */   public boolean isAbleToClearAnyPack() {
/*  49 */     List<Pack> newSelected = rebuildSelected(List.of());
/*  50 */     return !this.selected.equals(newSelected);
/*     */   }
/*     */   
/*     */   public void setSelected(Collection<String> packs) {
/*  54 */     this.selected = rebuildSelected(packs);
/*     */   }
/*     */   
/*     */   public boolean addPack(String packId) {
/*  58 */     Pack pack = this.available.get(packId);
/*  59 */     if (pack != null && !this.selected.contains(pack)) {
/*  60 */       List<Pack> selectedCopy = Lists.newArrayList(this.selected);
/*  61 */       selectedCopy.add(pack);
/*  62 */       this.selected = selectedCopy;
/*  63 */       return true;
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removePack(String packId) {
/*  69 */     Pack pack = this.available.get(packId);
/*  70 */     if (pack != null && this.selected.contains(pack)) {
/*  71 */       List<Pack> selectedCopy = Lists.newArrayList(this.selected);
/*  72 */       selectedCopy.remove(pack);
/*  73 */       this.selected = selectedCopy;
/*  74 */       return true;
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   private List<Pack> rebuildSelected(Collection<String> selectedNames) {
/*  80 */     List<Pack> selectedAndPresent = getAvailablePacks(selectedNames).collect(Util.toMutableList());
/*     */     
/*  82 */     for (Pack pack : this.available.values()) {
/*     */       
/*  84 */       if (pack.isRequired() && !selectedAndPresent.contains(pack)) {
/*  85 */         pack.getDefaultPosition().insert(selectedAndPresent, pack, Pack::selectionConfig, false);
/*     */       }
/*     */     } 
/*  88 */     return (List<Pack>)ImmutableList.copyOf(selectedAndPresent);
/*     */   }
/*     */   
/*     */   private Stream<Pack> getAvailablePacks(Collection<String> ids) {
/*  92 */     Objects.requireNonNull(this.available); return ids.stream().map(this.available::get).filter(Objects::nonNull);
/*     */   }
/*     */   
/*     */   public Collection<String> getAvailableIds() {
/*  96 */     return this.available.keySet();
/*     */   }
/*     */   
/*     */   public Collection<Pack> getAvailablePacks() {
/* 100 */     return this.available.values();
/*     */   }
/*     */   
/*     */   public Collection<String> getSelectedIds() {
/* 104 */     return (Collection<String>)this.selected.stream().map(Pack::getId).collect(ImmutableSet.toImmutableSet());
/*     */   }
/*     */   
/*     */   public FeatureFlagSet getRequestedFeatureFlags() {
/* 108 */     return getSelectedPacks().stream().map(Pack::getRequestedFeatures).reduce(FeatureFlagSet::join).orElse(FeatureFlagSet.of());
/*     */   }
/*     */   
/*     */   public Collection<Pack> getSelectedPacks() {
/* 112 */     return this.selected;
/*     */   }
/*     */   
/*     */   public Pack getPack(String id) {
/* 116 */     return this.available.get(id);
/*     */   }
/*     */   
/*     */   public boolean isAvailable(String id) {
/* 120 */     return this.available.containsKey(id);
/*     */   }
/*     */   
/*     */   public List<PackResources> openAllSelected() {
/* 124 */     return (List<PackResources>)this.selected.stream().map(Pack::open).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/repository/PackRepository.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */