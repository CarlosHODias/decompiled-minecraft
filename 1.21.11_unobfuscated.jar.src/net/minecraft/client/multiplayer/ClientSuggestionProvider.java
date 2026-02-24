/*     */ package net.minecraft.client.multiplayer;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ClientSuggestionProvider implements SharedSuggestionProvider {
/*     */   private final ClientPacketListener connection;
/*     */   private final Minecraft minecraft;
/*  37 */   private int pendingSuggestionsId = -1;
/*     */   private CompletableFuture<Suggestions> pendingSuggestionsFuture;
/*  39 */   private final Set<String> customCompletionSuggestions = new HashSet<>();
/*     */   private final PermissionSet permissions;
/*     */   
/*     */   public ClientSuggestionProvider(ClientPacketListener connection, Minecraft minecraft, PermissionSet permissions) {
/*  43 */     this.connection = connection;
/*  44 */     this.minecraft = minecraft;
/*  45 */     this.permissions = permissions;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getOnlinePlayerNames() {
/*  50 */     List<String> result = Lists.newArrayList();
/*     */     
/*  52 */     for (PlayerInfo info : this.connection.getOnlinePlayers()) {
/*  53 */       result.add(info.getProfile().name());
/*     */     }
/*     */     
/*  56 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getCustomTabSugggestions() {
/*  61 */     if (this.customCompletionSuggestions.isEmpty()) {
/*  62 */       return getOnlinePlayerNames();
/*     */     }
/*  64 */     Set<String> result = new HashSet<>(getOnlinePlayerNames());
/*  65 */     result.addAll(this.customCompletionSuggestions);
/*  66 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getSelectedEntities() {
/*  71 */     if (this.minecraft.hitResult != null && this.minecraft.hitResult.getType() == HitResult.Type.ENTITY) {
/*  72 */       return Collections.singleton(((EntityHitResult)this.minecraft.hitResult).getEntity().getStringUUID());
/*     */     }
/*  74 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getAllTeams() {
/*  79 */     return this.connection.scoreboard().getTeamNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<Identifier> getAvailableSounds() {
/*  84 */     return this.minecraft.getSoundManager().getAvailableSounds().stream();
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionSet permissions() {
/*  89 */     return this.permissions;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> key, SharedSuggestionProvider.ElementSuggestionType elements, SuggestionsBuilder builder, CommandContext<?> context) {
/*  94 */     return registryAccess().lookup(key).map(registry -> {
/*     */           suggestRegistryElements((HolderLookup)builder, elements, elements);
/*     */           return elements.buildFuture();
/*  97 */         }).orElseGet(() -> customSuggestion(context));
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> customSuggestion(CommandContext<?> context) {
/* 102 */     if (this.pendingSuggestionsFuture != null) {
/* 103 */       this.pendingSuggestionsFuture.cancel(false);
/*     */     }
/* 105 */     this.pendingSuggestionsFuture = new CompletableFuture<>();
/* 106 */     int id = ++this.pendingSuggestionsId;
/* 107 */     this.connection.send((Packet<?>)new ServerboundCommandSuggestionPacket(id, context.getInput()));
/* 108 */     return this.pendingSuggestionsFuture;
/*     */   }
/*     */   
/*     */   private static String prettyPrint(double value) {
/* 112 */     return String.format(Locale.ROOT, "%.2f", new Object[] { value });
/*     */   }
/*     */   
/*     */   private static String prettyPrint(int value) {
/* 116 */     return Integer.toString(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<SharedSuggestionProvider.TextCoordinates> getRelevantCoordinates() {
/* 121 */     HitResult hitResult = this.minecraft.hitResult;
/* 122 */     if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
/* 123 */       return super.getRelevantCoordinates();
/*     */     }
/*     */     
/* 126 */     BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
/* 127 */     return Collections.singleton(new SharedSuggestionProvider.TextCoordinates(prettyPrint(pos.getX()), prettyPrint(pos.getY()), prettyPrint(pos.getZ())));
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<SharedSuggestionProvider.TextCoordinates> getAbsoluteCoordinates() {
/* 132 */     HitResult hitResult = this.minecraft.hitResult;
/* 133 */     if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
/* 134 */       return super.getAbsoluteCoordinates();
/*     */     }
/*     */     
/* 137 */     Vec3 pos = hitResult.getLocation();
/* 138 */     return Collections.singleton(new SharedSuggestionProvider.TextCoordinates(prettyPrint(pos.x), prettyPrint(pos.y), prettyPrint(pos.z)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ResourceKey<Level>> levels() {
/* 143 */     return this.connection.levels();
/*     */   }
/*     */ 
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 148 */     return (RegistryAccess)this.connection.registryAccess();
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet enabledFeatures() {
/* 153 */     return this.connection.enabledFeatures();
/*     */   }
/*     */   
/*     */   public void completeCustomSuggestions(int id, Suggestions result) {
/* 157 */     if (id == this.pendingSuggestionsId) {
/* 158 */       this.pendingSuggestionsFuture.complete(result);
/* 159 */       this.pendingSuggestionsFuture = null;
/* 160 */       this.pendingSuggestionsId = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void modifyCustomCompletions(ClientboundCustomChatCompletionsPacket.Action action, List<String> entries) {
/* 165 */     switch (action) { case ADD:
/* 166 */         this.customCompletionSuggestions.addAll(entries); break;
/* 167 */       case REMOVE: Objects.requireNonNull(this.customCompletionSuggestions); entries.forEach(this.customCompletionSuggestions::remove); break;
/*     */       case SET:
/* 169 */         this.customCompletionSuggestions.clear();
/* 170 */         this.customCompletionSuggestions.addAll(entries);
/*     */         break; }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientSuggestionProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */