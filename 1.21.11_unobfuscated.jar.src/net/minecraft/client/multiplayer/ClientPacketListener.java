/*      */ package net.minecraft.client.multiplayer;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.hash.HashCode;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.brigadier.CommandDispatcher;
/*      */ import com.mojang.brigadier.ParseResults;
/*      */ import com.mojang.brigadier.arguments.ArgumentType;
/*      */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*      */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.advancements.AdvancementHolder;
/*      */ import net.minecraft.client.ClientRecipeBook;
/*      */ import net.minecraft.client.DebugQueryHandler;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.Options;
/*      */ import net.minecraft.client.gui.components.ChatComponent;
/*      */ import net.minecraft.client.gui.components.toasts.RecipeToast;
/*      */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*      */ import net.minecraft.client.gui.components.toasts.Toast;
/*      */ import net.minecraft.client.gui.screens.ChatScreen;
/*      */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*      */ import net.minecraft.client.gui.screens.DeathScreen;
/*      */ import net.minecraft.client.gui.screens.DemoIntroScreen;
/*      */ import net.minecraft.client.gui.screens.LevelLoadingScreen;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.WinScreen;
/*      */ import net.minecraft.client.gui.screens.achievement.StatsScreen;
/*      */ import net.minecraft.client.gui.screens.dialog.DialogConnectionAccess;
/*      */ import net.minecraft.client.gui.screens.inventory.BookViewScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.NautilusInventoryScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.TestInstanceBlockEditScreen;
/*      */ import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
/*      */ import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
/*      */ import net.minecraft.client.particle.ItemPickupParticle;
/*      */ import net.minecraft.client.particle.Particle;
/*      */ import net.minecraft.client.player.ClientInput;
/*      */ import net.minecraft.client.player.KeyboardInput;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.player.RemotePlayer;
/*      */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*      */ import net.minecraft.client.resources.sounds.BeeAggressiveSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.BeeFlyingSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.MinecartSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SnifferSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.client.resources.sounds.TickableSoundInstance;
/*      */ import net.minecraft.client.waypoints.ClientWaypointManager;
/*      */ import net.minecraft.commands.CommandBuildContext;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.commands.arguments.ArgumentSignatures;
/*      */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.RegistrySynchronization;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.component.TypedDataComponent;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.HashedPatchMap;
/*      */ import net.minecraft.network.PacketListener;
/*      */ import net.minecraft.network.TickablePacketListener;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.LastSeenMessagesTracker;
/*      */ import net.minecraft.network.chat.LocalChatSession;
/*      */ import net.minecraft.network.chat.MessageSignature;
/*      */ import net.minecraft.network.chat.MessageSignatureCache;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.PlayerChatMessage;
/*      */ import net.minecraft.network.chat.RemoteChatSession;
/*      */ import net.minecraft.network.chat.SignableCommand;
/*      */ import net.minecraft.network.chat.SignedMessageBody;
/*      */ import net.minecraft.network.chat.SignedMessageChain;
/*      */ import net.minecraft.network.chat.SignedMessageLink;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.PacketUtils;
/*      */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*      */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*      */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*      */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBundlePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunkBatchFinishedPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunkBatchStartPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugEntityValuePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDebugSamplePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDeleteChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundExplodePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameTestHighlightPosPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacketData;
/*      */ import net.minecraft.network.protocol.game.ClientboundLoginPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMountScreenOpenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveMinecartPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEndPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatEnterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundProjectilePowerPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRecipeBookRemovePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRecipeBookSettingsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetSimulationDistancePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTabListPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTestInstanceBlockStatus;
/*      */ import net.minecraft.network.protocol.game.ClientboundTickingStatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTickingStepPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
/*      */ import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
/*      */ import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChunkBatchReceivedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundConfigurationAcknowledgedPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
/*      */ import net.minecraft.network.protocol.game.VecDeltaCodec;
/*      */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.RegistryOps;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ClientInformation;
/*      */ import net.minecraft.server.permissions.Permission;
/*      */ import net.minecraft.server.permissions.PermissionCheck;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.StatsCounter;
/*      */ import net.minecraft.tags.TagNetworkSerialization;
/*      */ import net.minecraft.util.Crypt;
/*      */ import net.minecraft.util.HashOps;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.SignatureValidator;
/*      */ import net.minecraft.util.debug.DebugValueAccess;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.Leashable;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.PositionMoveRotation;
/*      */ import net.minecraft.world.entity.Relative;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.animal.bee.Bee;
/*      */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Guardian;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.player.ProfileKeyPair;
/*      */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*      */ import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
/*      */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*      */ import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
/*      */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*      */ import net.minecraft.world.inventory.HorseInventoryMenu;
/*      */ import net.minecraft.world.inventory.InventoryMenu;
/*      */ import net.minecraft.world.inventory.MerchantMenu;
/*      */ import net.minecraft.world.inventory.NautilusInventoryMenu;
/*      */ import net.minecraft.world.item.CreativeModeTabs;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.crafting.RecipeAccess;
/*      */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*      */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LightLayer;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.DataLayer;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.TagValueInput;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.ScoreAccess;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*      */ import net.minecraft.world.waypoints.TrackedWaypointManager;
/*      */ 
/*      */ public class ClientPacketListener extends ClientCommonPacketListenerImpl implements ClientGamePacketListener, TickablePacketListener {
/*  344 */   private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  346 */   private static final Component UNSECURE_SERVER_TOAST_TITLE = (Component)Component.translatable("multiplayer.unsecureserver.toast.title");
/*  347 */   private static final Component UNSERURE_SERVER_TOAST = (Component)Component.translatable("multiplayer.unsecureserver.toast");
/*  348 */   private static final Component INVALID_PACKET = (Component)Component.translatable("multiplayer.disconnect.invalid_packet");
/*  349 */   private static final Component RECONFIGURE_SCREEN_MESSAGE = (Component)Component.translatable("connect.reconfiguring");
/*  350 */   private static final Component BAD_CHAT_INDEX = (Component)Component.translatable("multiplayer.disconnect.bad_chat_index");
/*  351 */   private static final Component COMMAND_SEND_CONFIRM_TITLE = (Component)Component.translatable("multiplayer.confirm_command.title");
/*  352 */   private static final Component BUTTON_RUN_COMMAND = (Component)Component.translatable("multiplayer.confirm_command.run_command");
/*  353 */   private static final Component BUTTON_SUGGEST_COMMAND = (Component)Component.translatable("multiplayer.confirm_command.suggest_command");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int PENDING_OFFSET_THRESHOLD = 64;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int TELEPORT_INTERPOLATION_THRESHOLD = 64;
/*      */ 
/*      */ 
/*      */   
/*  366 */   private static final Permission RESTRICTED_COMMAND = (Permission)Permission.Atom.create("client/commands/restricted");
/*      */   
/*  368 */   private static final PermissionCheck RESTRICTED_COMMAND_CHECK = (PermissionCheck)new PermissionCheck.Require(RESTRICTED_COMMAND); private static final PermissionSet ALLOW_RESTRICTED_COMMANDS;
/*      */   static {
/*  370 */     ALLOW_RESTRICTED_COMMANDS = (permission -> permission.equals(RESTRICTED_COMMAND));
/*      */   }
/*  372 */   private static final ClientboundCommandsPacket.NodeBuilder<ClientSuggestionProvider> COMMAND_NODE_BUILDER = new ClientboundCommandsPacket.NodeBuilder<ClientSuggestionProvider>()
/*      */     {
/*      */       public ArgumentBuilder<ClientSuggestionProvider, ?> createLiteral(String id)
/*      */       {
/*  376 */         return (ArgumentBuilder<ClientSuggestionProvider, ?>)LiteralArgumentBuilder.literal(id);
/*      */       }
/*      */ 
/*      */       
/*      */       public ArgumentBuilder<ClientSuggestionProvider, ?> createArgument(String id, ArgumentType<?> argumentType, Identifier suggestionId) {
/*  381 */         RequiredArgumentBuilder<ClientSuggestionProvider, ?> builder = RequiredArgumentBuilder.argument(id, argumentType);
/*  382 */         if (suggestionId != null) {
/*  383 */           builder.suggests(SuggestionProviders.getProvider(suggestionId));
/*      */         }
/*  385 */         return (ArgumentBuilder<ClientSuggestionProvider, ?>)builder;
/*      */       }
/*      */ 
/*      */       
/*      */       public ArgumentBuilder<ClientSuggestionProvider, ?> configure(ArgumentBuilder<ClientSuggestionProvider, ?> builder, boolean executable, boolean restricted) {
/*  390 */         if (executable) {
/*  391 */           builder.executes(c -> 0);
/*      */         }
/*  393 */         if (restricted) {
/*  394 */           builder.requires((Predicate)Commands.hasPermission(ClientPacketListener.RESTRICTED_COMMAND_CHECK));
/*      */         }
/*  396 */         return builder;
/*      */       }
/*      */     };
/*      */   
/*      */   private final GameProfile localGameProfile;
/*      */   private ClientLevel level;
/*      */   private ClientLevel.ClientLevelData levelData;
/*  403 */   private final Map<UUID, PlayerInfo> playerInfoMap = Maps.newHashMap();
/*  404 */   private final Set<PlayerInfo> listedPlayers = (Set<PlayerInfo>)new ReferenceOpenHashSet();
/*      */   private final ClientAdvancements advancements;
/*      */   private final ClientSuggestionProvider suggestionsProvider;
/*      */   private final ClientSuggestionProvider restrictedSuggestionsProvider;
/*  408 */   private final DebugQueryHandler debugQueryHandler = new DebugQueryHandler(this);
/*  409 */   private int serverChunkRadius = 3;
/*  410 */   private int serverSimulationDistance = 3;
/*      */ 
/*      */   
/*  413 */   private final RandomSource random = RandomSource.createThreadSafe();
/*  414 */   private CommandDispatcher<ClientSuggestionProvider> commands = new CommandDispatcher();
/*  415 */   private ClientRecipeContainer recipes = new ClientRecipeContainer(Map.of(), SelectableRecipe.SingleInputSet.empty());
/*  416 */   private final UUID id = UUID.randomUUID();
/*      */   
/*      */   private Set<ResourceKey<Level>> levels;
/*      */   
/*      */   private final RegistryAccess.Frozen registryAccess;
/*      */   
/*      */   private final FeatureFlagSet enabledFeatures;
/*      */   private final PotionBrewing potionBrewing;
/*      */   private FuelValues fuelValues;
/*      */   private final HashedPatchMap.HashGenerator decoratedHashOpsGenerator;
/*  426 */   private OptionalInt removedPlayerVehicleId = OptionalInt.empty();
/*      */   
/*      */   private LocalChatSession chatSession;
/*  429 */   private SignedMessageChain.Encoder signedMessageEncoder = SignedMessageChain.Encoder.UNSIGNED;
/*      */   
/*      */   private int nextChatIndex;
/*  432 */   private LastSeenMessagesTracker lastSeenMessages = new LastSeenMessagesTracker(20);
/*  433 */   private MessageSignatureCache messageSignatureCache = MessageSignatureCache.createDefault();
/*      */   
/*      */   private CompletableFuture<Optional<ProfileKeyPair>> keyPairFuture;
/*      */   
/*      */   private ClientInformation remoteClientInformation;
/*      */   
/*  439 */   private final ChunkBatchSizeCalculator chunkBatchSizeCalculator = new ChunkBatchSizeCalculator();
/*      */   
/*      */   private final PingDebugMonitor pingDebugMonitor;
/*      */   
/*      */   private final ClientDebugSubscriber debugSubscriber;
/*      */   
/*      */   private LevelLoadTracker levelLoadTracker;
/*      */   private boolean serverEnforcesSecureChat;
/*      */   private volatile boolean closed;
/*  448 */   private final Scoreboard scoreboard = new Scoreboard();
/*  449 */   private final ClientWaypointManager waypointManager = new ClientWaypointManager();
/*      */   
/*  451 */   private final SessionSearchTrees searchTrees = new SessionSearchTrees();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  456 */   private final List<WeakReference<CacheSlot<?, ?>>> cacheSlots = new ArrayList<>();
/*      */   
/*      */   private boolean clientLoaded;
/*      */   
/*      */   public ClientPacketListener(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
/*  461 */     super(minecraft, connection, cookie);
/*  462 */     this.localGameProfile = cookie.localGameProfile();
/*  463 */     this.registryAccess = cookie.receivedRegistries();
/*  464 */     RegistryOps<HashCode> hashOps = this.registryAccess.createSerializationContext((DynamicOps)HashOps.CRC32C_INSTANCE);
/*  465 */     this.decoratedHashOpsGenerator = (component -> ((HashCode)component.encodeValue((DynamicOps)hashOps).getOrThrow(())).asInt());
/*  466 */     this.enabledFeatures = cookie.enabledFeatures();
/*  467 */     this.advancements = new ClientAdvancements(minecraft, this.telemetryManager);
/*      */     
/*      */     PermissionSet playerPermissions = permission -> {
/*      */         LocalPlayer player = minecraft.player;
/*  471 */         return (player != null && player.permissions().hasPermission(permission));
/*      */       };
/*      */     
/*  474 */     this.suggestionsProvider = new ClientSuggestionProvider(this, minecraft, playerPermissions.union(ALLOW_RESTRICTED_COMMANDS));
/*      */     
/*  476 */     this.restrictedSuggestionsProvider = new ClientSuggestionProvider(this, minecraft, PermissionSet.NO_PERMISSIONS);
/*  477 */     this.pingDebugMonitor = new PingDebugMonitor(this, minecraft.getDebugOverlay().getPingLogger());
/*  478 */     this.debugSubscriber = new ClientDebugSubscriber(this, minecraft.getDebugOverlay());
/*  479 */     if (cookie.chatState() != null) {
/*  480 */       minecraft.gui.getChat().restoreState(cookie.chatState());
/*      */     }
/*  482 */     this.potionBrewing = PotionBrewing.bootstrap(this.enabledFeatures);
/*  483 */     this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)cookie.receivedRegistries(), this.enabledFeatures);
/*  484 */     this.levelLoadTracker = cookie.levelLoadTracker();
/*      */   }
/*      */   
/*      */   public ClientSuggestionProvider getSuggestionsProvider() {
/*  488 */     return this.suggestionsProvider;
/*      */   }
/*      */   
/*      */   public void close() {
/*  492 */     this.closed = true;
/*  493 */     clearLevel();
/*  494 */     this.telemetryManager.onDisconnect();
/*      */   }
/*      */   
/*      */   public void clearLevel() {
/*  498 */     clearCacheSlots();
/*  499 */     this.level = null;
/*  500 */     this.levelLoadTracker = null;
/*      */   }
/*      */   
/*      */   private void clearCacheSlots() {
/*  504 */     for (WeakReference<CacheSlot<?, ?>> cacheSlot : this.cacheSlots) {
/*  505 */       CacheSlot<?, ?> slot = cacheSlot.get();
/*  506 */       if (slot != null) {
/*  507 */         slot.clear();
/*      */       }
/*      */     } 
/*  510 */     this.cacheSlots.clear();
/*      */   }
/*      */   
/*      */   public RecipeAccess recipes() {
/*  514 */     return this.recipes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleLogin(ClientboundLoginPacket packet) {
/*  521 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/*  523 */     this.minecraft.gameMode = new MultiPlayerGameMode(this.minecraft, this);
/*  524 */     CommonPlayerSpawnInfo spawnInfo = packet.commonPlayerSpawnInfo();
/*      */     
/*  526 */     List<ResourceKey<Level>> levels = Lists.newArrayList(packet.levels());
/*  527 */     java.util.Collections.shuffle(levels);
/*      */     
/*  529 */     this.levels = Sets.newLinkedHashSet(levels);
/*  530 */     ResourceKey<Level> dimension = spawnInfo.dimension();
/*  531 */     Holder<DimensionType> dimensionType = spawnInfo.dimensionType();
/*      */     
/*  533 */     this.serverChunkRadius = packet.chunkRadius();
/*  534 */     this.serverSimulationDistance = packet.simulationDistance();
/*      */     
/*  536 */     boolean isDebug = spawnInfo.isDebug();
/*  537 */     boolean isFlat = spawnInfo.isFlat();
/*  538 */     int seaLevel = spawnInfo.seaLevel();
/*  539 */     ClientLevel.ClientLevelData levelData = new ClientLevel.ClientLevelData(Difficulty.NORMAL, packet.hardcore(), isFlat);
/*  540 */     this.levelData = levelData;
/*  541 */     this.level = new ClientLevel(this, levelData, dimension, dimensionType, this.serverChunkRadius, this.serverSimulationDistance, this.minecraft.levelRenderer, isDebug, spawnInfo.seed(), seaLevel);
/*  542 */     this.minecraft.setLevel(this.level);
/*      */ 
/*      */     
/*  545 */     if (this.minecraft.player == null) {
/*  546 */       this.minecraft.player = this.minecraft.gameMode.createPlayer(this.level, new StatsCounter(), new ClientRecipeBook());
/*  547 */       this.minecraft.player.setYRot(-180.0F);
/*  548 */       if (this.minecraft.getSingleplayerServer() != null) {
/*  549 */         this.minecraft.getSingleplayerServer().setUUID(this.minecraft.player.getUUID());
/*      */       }
/*      */     } 
/*      */     
/*  553 */     setClientLoaded(false);
/*  554 */     this.debugSubscriber.clear();
/*  555 */     this.minecraft.levelRenderer.debugRenderer.refreshRendererList();
/*      */     
/*  557 */     this.minecraft.player.resetPos();
/*  558 */     this.minecraft.player.setId(packet.playerId());
/*  559 */     this.level.addEntity((Entity)this.minecraft.player);
/*  560 */     this.minecraft.player.input = (ClientInput)new KeyboardInput(this.minecraft.options);
/*  561 */     this.minecraft.gameMode.adjustPlayer((Player)this.minecraft.player);
/*  562 */     this.minecraft.setCameraEntity((Entity)this.minecraft.player);
/*  563 */     startWaitingForNewLevel(this.minecraft.player, this.level, LevelLoadingScreen.Reason.OTHER);
/*  564 */     this.minecraft.player.setReducedDebugInfo(packet.reducedDebugInfo());
/*  565 */     this.minecraft.player.setShowDeathScreen(packet.showDeathScreen());
/*  566 */     this.minecraft.player.setDoLimitedCrafting(packet.doLimitedCrafting());
/*  567 */     this.minecraft.player.setLastDeathLocation(spawnInfo.lastDeathLocation());
/*  568 */     this.minecraft.player.setPortalCooldown(spawnInfo.portalCooldown());
/*  569 */     this.minecraft.gameMode.setLocalMode(spawnInfo.gameType(), spawnInfo.previousGameType());
/*  570 */     this.minecraft.options.setServerRenderDistance(packet.chunkRadius());
/*      */ 
/*      */ 
/*      */     
/*  574 */     this.chatSession = null;
/*  575 */     this.signedMessageEncoder = SignedMessageChain.Encoder.UNSIGNED;
/*  576 */     this.nextChatIndex = 0;
/*  577 */     this.lastSeenMessages = new LastSeenMessagesTracker(20);
/*  578 */     this.messageSignatureCache = MessageSignatureCache.createDefault();
/*  579 */     if (this.connection.isEncrypted()) {
/*  580 */       prepareKeyPair();
/*      */     }
/*      */     
/*  583 */     this.telemetryManager.onPlayerInfoReceived(spawnInfo.gameType(), packet.hardcore());
/*  584 */     this.minecraft.quickPlayLog().log(this.minecraft);
/*      */     
/*  586 */     this.serverEnforcesSecureChat = packet.enforcesSecureChat();
/*      */     
/*  588 */     if (this.serverData != null && !this.seenInsecureChatWarning && !enforcesSecureChat()) {
/*  589 */       SystemToast toast = SystemToast.multiline(this.minecraft, SystemToast.SystemToastId.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_TOAST_TITLE, UNSERURE_SERVER_TOAST);
/*  590 */       this.minecraft.getToastManager().addToast((Toast)toast);
/*  591 */       this.seenInsecureChatWarning = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAddEntity(ClientboundAddEntityPacket packet) {
/*  597 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/*  599 */     if (this.removedPlayerVehicleId.isPresent() && this.removedPlayerVehicleId.getAsInt() == packet.getId()) {
/*  600 */       this.removedPlayerVehicleId = OptionalInt.empty();
/*      */     }
/*      */     
/*  603 */     Entity entity = createEntityFromPacket(packet);
/*  604 */     if (entity != null) {
/*  605 */       entity.recreateFromPacket(packet);
/*  606 */       this.level.addEntity(entity);
/*      */       
/*  608 */       postAddEntitySoundInstance(entity);
/*      */     } else {
/*  610 */       LOGGER.warn("Skipping Entity with id {}", packet.getType());
/*      */     } 
/*      */     
/*  613 */     if (entity instanceof Player) { Player player = (Player)entity;
/*  614 */       UUID uuid = player.getUUID();
/*  615 */       PlayerInfo playerInfo = this.playerInfoMap.get(uuid);
/*  616 */       if (playerInfo != null) {
/*  617 */         this.seenPlayers.put(uuid, playerInfo);
/*      */       } }
/*      */   
/*      */   }
/*      */   
/*      */   private Entity createEntityFromPacket(ClientboundAddEntityPacket packet) {
/*  623 */     EntityType<?> type = packet.getType();
/*  624 */     if (type == EntityType.PLAYER) {
/*  625 */       PlayerInfo playerInfo = getPlayerInfo(packet.getUUID());
/*  626 */       if (playerInfo == null) {
/*  627 */         LOGGER.warn("Server attempted to add player prior to sending player info (Player id {})", packet.getUUID());
/*  628 */         return null;
/*      */       } 
/*  630 */       return (Entity)new RemotePlayer(this.level, playerInfo.getProfile());
/*      */     } 
/*  632 */     return type.create(this.level, net.minecraft.world.entity.EntitySpawnReason.LOAD);
/*      */   }
/*      */   
/*      */   private void postAddEntitySoundInstance(Entity entity) {
/*  636 */     if (entity instanceof AbstractMinecart) { AbstractMinecart minecart = (AbstractMinecart)entity;
/*  637 */       this.minecraft.getSoundManager().play((SoundInstance)new MinecartSoundInstance(minecart)); }
/*  638 */     else if (entity instanceof Bee) { BeeFlyingSoundInstance beeFlyingSoundInstance; Bee bee = (Bee)entity;
/*  639 */       boolean angry = bee.isAngry();
/*      */       
/*  641 */       if (angry) {
/*  642 */         BeeAggressiveSoundInstance beeAggressiveSoundInstance = new BeeAggressiveSoundInstance(bee);
/*      */       } else {
/*  644 */         beeFlyingSoundInstance = new BeeFlyingSoundInstance(bee);
/*      */       } 
/*  646 */       this.minecraft.getSoundManager().queueTickingSound((TickableSoundInstance)beeFlyingSoundInstance); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetEntityMotion(ClientboundSetEntityMotionPacket packet) {
/*  652 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  653 */     Entity entity = this.level.getEntity(packet.getId());
/*  654 */     if (entity == null) {
/*      */       return;
/*      */     }
/*  657 */     entity.lerpMotion(packet.getMovement());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetEntityData(ClientboundSetEntityDataPacket packet) {
/*  662 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  663 */     Entity entity = this.level.getEntity(packet.id());
/*  664 */     if (entity != null) {
/*  665 */       entity.getEntityData().assignValues(packet.packedItems());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityPositionSync(ClientboundEntityPositionSyncPacket packet) {
/*  671 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  672 */     Entity entity = this.level.getEntity(packet.id());
/*  673 */     if (entity == null) {
/*      */       return;
/*      */     }
/*      */     
/*  677 */     Vec3 pos = packet.values().position();
/*  678 */     entity.getPositionCodec().setBase(pos);
/*  679 */     if (entity.isLocalInstanceAuthoritative()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  684 */     float yRot = packet.values().yRot();
/*  685 */     float xRot = packet.values().xRot();
/*  686 */     boolean tooBigToInterpolate = (entity.position().distanceToSqr(pos) > 4096.0D);
/*  687 */     if (this.level.isTickingEntity(entity) && !tooBigToInterpolate) {
/*  688 */       entity.moveOrInterpolateTo(pos, yRot, xRot);
/*      */     } else {
/*  690 */       entity.snapTo(pos, yRot, xRot);
/*      */     } 
/*      */     
/*  693 */     if (!entity.isInterpolating() && entity.hasIndirectPassenger((Entity)this.minecraft.player)) {
/*  694 */       entity.positionRider((Entity)this.minecraft.player);
/*  695 */       this.minecraft.player.setOldPosAndRot();
/*      */     } 
/*      */     
/*  698 */     entity.setOnGround(packet.onGround());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTeleportEntity(ClientboundTeleportEntityPacket packet) {
/*  703 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  704 */     Entity entity = this.level.getEntity(packet.id());
/*  705 */     if (entity == null) {
/*  706 */       if (this.removedPlayerVehicleId.isPresent() && this.removedPlayerVehicleId.getAsInt() == packet.id()) {
/*  707 */         LOGGER.debug("Trying to teleport entity with id {}, that was formerly player vehicle, applying teleport to player instead", packet.id());
/*  708 */         setValuesFromPositionPacket(packet.change(), packet.relatives(), (Entity)this.minecraft.player, false);
/*  709 */         this.connection.send((Packet)new ServerboundMovePlayerPacket.PosRot(this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), this.minecraft.player.getYRot(), this.minecraft.player.getXRot(), false, false));
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  714 */     boolean hasRelative = (packet.relatives().contains(Relative.X) || packet.relatives().contains(Relative.Y) || packet.relatives().contains(Relative.Z));
/*  715 */     boolean interpolate = (this.level.isTickingEntity(entity) || !entity.isLocalInstanceAuthoritative() || hasRelative);
/*  716 */     boolean wasInterpolated = setValuesFromPositionPacket(packet.change(), packet.relatives(), entity, interpolate);
/*  717 */     entity.setOnGround(packet.onGround());
/*      */     
/*  719 */     if (!wasInterpolated && entity.hasIndirectPassenger((Entity)this.minecraft.player)) {
/*  720 */       entity.positionRider((Entity)this.minecraft.player);
/*  721 */       this.minecraft.player.setOldPosAndRot();
/*  722 */       if (entity.isLocalInstanceAuthoritative()) {
/*  723 */         this.connection.send((Packet)ServerboundMoveVehiclePacket.fromEntity(entity));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTickingState(ClientboundTickingStatePacket packet) {
/*  730 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  731 */     if (this.minecraft.level == null) {
/*      */       return;
/*      */     }
/*  734 */     TickRateManager manager = this.minecraft.level.tickRateManager();
/*  735 */     manager.setTickRate(packet.tickRate());
/*  736 */     manager.setFrozen(packet.isFrozen());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTickingStep(ClientboundTickingStepPacket packet) {
/*  741 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  742 */     if (this.minecraft.level == null) {
/*      */       return;
/*      */     }
/*  745 */     TickRateManager manager = this.minecraft.level.tickRateManager();
/*  746 */     manager.setFrozenTicksToRun(packet.tickSteps());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetHeldSlot(ClientboundSetHeldSlotPacket packet) {
/*  751 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  752 */     if (Inventory.isHotbarSlot(packet.slot())) {
/*  753 */       this.minecraft.player.getInventory().setSelectedSlot(packet.slot());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMoveEntity(ClientboundMoveEntityPacket packet) {
/*  759 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  760 */     Entity entity = packet.getEntity(this.level);
/*  761 */     if (entity == null) {
/*      */       return;
/*      */     }
/*      */     
/*  765 */     if (entity.isLocalInstanceAuthoritative()) {
/*  766 */       VecDeltaCodec positionCodec = entity.getPositionCodec();
/*  767 */       Vec3 pos = positionCodec.decode(packet.getXa(), packet.getYa(), packet.getZa());
/*  768 */       positionCodec.setBase(pos);
/*      */       
/*      */       return;
/*      */     } 
/*  772 */     if (packet.hasPosition()) {
/*  773 */       VecDeltaCodec positionCodec = entity.getPositionCodec();
/*  774 */       Vec3 pos = positionCodec.decode(packet.getXa(), packet.getYa(), packet.getZa());
/*  775 */       positionCodec.setBase(pos);
/*  776 */       if (packet.hasRotation()) {
/*  777 */         entity.moveOrInterpolateTo(pos, packet.getYRot(), packet.getXRot());
/*      */       } else {
/*  779 */         entity.moveOrInterpolateTo(pos);
/*      */       } 
/*  781 */     } else if (packet.hasRotation()) {
/*  782 */       entity.moveOrInterpolateTo(packet.getYRot(), packet.getXRot());
/*      */     } 
/*  784 */     entity.setOnGround(packet.isOnGround());
/*      */   }
/*      */   
/*      */   public void handleMinecartAlongTrack(ClientboundMoveMinecartPacket packet) {
/*      */     AbstractMinecart minecart;
/*  789 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  790 */     Entity entity = packet.getEntity(this.level);
/*  791 */     if (entity instanceof AbstractMinecart) { minecart = (AbstractMinecart)entity; }
/*      */     else
/*      */     { return; }
/*  794 */      MinecartBehavior minecartBehavior = minecart.getBehavior(); if (minecartBehavior instanceof NewMinecartBehavior) { NewMinecartBehavior newMinecartBehavior = (NewMinecartBehavior)minecartBehavior;
/*  795 */       newMinecartBehavior.lerpSteps.addAll(packet.lerpSteps()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRotateMob(ClientboundRotateHeadPacket packet) {
/*  801 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  802 */     Entity entity = packet.getEntity(this.level);
/*  803 */     if (entity == null) {
/*      */       return;
/*      */     }
/*  806 */     entity.lerpHeadTo(packet.getYHeadRot(), 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRemoveEntities(ClientboundRemoveEntitiesPacket packet) {
/*  811 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  812 */     packet.getEntityIds().forEach(entityId -> {
/*      */           Entity entity = this.level.getEntity(entityId);
/*      */           if (entity == null) {
/*      */             return;
/*      */           }
/*      */           if (entity.hasIndirectPassenger((Entity)this.minecraft.player)) {
/*      */             LOGGER.debug("Remove entity {}:{} that has player as passenger", entity.getType(), entityId);
/*      */             this.removedPlayerVehicleId = OptionalInt.of(entityId);
/*      */           } 
/*      */           this.level.removeEntity(entityId, Entity.RemovalReason.DISCARDED);
/*      */           this.debugSubscriber.dropEntity(entity);
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMovePlayer(ClientboundPlayerPositionPacket packet) {
/*  828 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  829 */     LocalPlayer localPlayer = this.minecraft.player;
/*  830 */     if (!localPlayer.isPassenger())
/*      */     {
/*  832 */       setValuesFromPositionPacket(packet.change(), packet.relatives(), (Entity)localPlayer, false);
/*      */     }
/*  834 */     this.connection.send((Packet)new ServerboundAcceptTeleportationPacket(packet.id()));
/*  835 */     this.connection.send((Packet)new ServerboundMovePlayerPacket.PosRot(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ(), localPlayer.getYRot(), localPlayer.getXRot(), false, false));
/*      */   }
/*      */   
/*      */   private static boolean setValuesFromPositionPacket(PositionMoveRotation change, Set<Relative> relatives, Entity entity, boolean interpolate) {
/*  839 */     PositionMoveRotation currentValues = PositionMoveRotation.of(entity);
/*      */     
/*  841 */     PositionMoveRotation newValues = PositionMoveRotation.calculateAbsolute(currentValues, change, relatives);
/*      */     
/*  843 */     boolean tooBigToInterpolate = (currentValues.position().distanceToSqr(newValues.position()) > 4096.0D);
/*  844 */     if (interpolate && !tooBigToInterpolate) {
/*  845 */       entity.moveOrInterpolateTo(newValues.position(), newValues.yRot(), newValues.xRot());
/*  846 */       entity.setDeltaMovement(newValues.deltaMovement());
/*  847 */       return true;
/*      */     } 
/*      */     
/*  850 */     entity.setPos(newValues.position());
/*  851 */     entity.setDeltaMovement(newValues.deltaMovement());
/*  852 */     entity.setYRot(newValues.yRot());
/*  853 */     entity.setXRot(newValues.xRot());
/*      */ 
/*      */     
/*  856 */     PositionMoveRotation currentInterpolationValues = new PositionMoveRotation(entity.oldPosition(), Vec3.ZERO, entity.yRotO, entity.xRotO);
/*  857 */     PositionMoveRotation interpolationValues = PositionMoveRotation.calculateAbsolute(currentInterpolationValues, change, relatives);
/*  858 */     entity.setOldPosAndRot(interpolationValues.position(), interpolationValues.yRot(), interpolationValues.xRot());
/*  859 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRotatePlayer(ClientboundPlayerRotationPacket packet) {
/*  864 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  865 */     LocalPlayer localPlayer = this.minecraft.player;
/*      */     
/*  867 */     Set<Relative> relatives = Relative.rotation(packet.relativeY(), packet.relativeX());
/*  868 */     PositionMoveRotation currentValues = PositionMoveRotation.of((Entity)localPlayer);
/*  869 */     PositionMoveRotation newValues = PositionMoveRotation.calculateAbsolute(currentValues, currentValues.withRotation(packet.yRot(), packet.xRot()), relatives);
/*      */     
/*  871 */     localPlayer.setYRot(newValues.yRot());
/*  872 */     localPlayer.setXRot(newValues.xRot());
/*  873 */     localPlayer.setOldRot();
/*  874 */     this.connection.send((Packet)new ServerboundMovePlayerPacket.Rot(localPlayer.getYRot(), localPlayer.getXRot(), false, false));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChunkBlocksUpdate(ClientboundSectionBlocksUpdatePacket packet) {
/*  879 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/*  881 */     packet.runUpdates((pos, state) -> this.level.setServerVerifiedBlockState(pos, state, 19));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket packet) {
/*  886 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  887 */     int x = packet.getX();
/*  888 */     int z = packet.getZ();
/*  889 */     updateLevelChunk(x, z, packet.getChunkData());
/*  890 */     ClientboundLightUpdatePacketData lightData = packet.getLightData();
/*  891 */     this.level.queueLightUpdate(() -> {
/*      */           applyLightData(x, z, lightData, false);
/*      */           LevelChunk chunk = this.level.getChunkSource().getChunk(x, z, false);
/*      */           if (chunk != null) {
/*      */             enableChunkLight(chunk, x, z);
/*      */             this.minecraft.levelRenderer.onChunkReadyToRender(chunk.getPos());
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChunksBiomes(ClientboundChunksBiomesPacket packet) {
/*  903 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/*  905 */     for (ClientboundChunksBiomesPacket.ChunkBiomeData data : (Iterable<ClientboundChunksBiomesPacket.ChunkBiomeData>)packet.chunkBiomeData()) {
/*  906 */       this.level.getChunkSource().replaceBiomes((data.pos()).x, (data.pos()).z, data.getReadBuffer());
/*      */     }
/*      */     
/*  909 */     for (ClientboundChunksBiomesPacket.ChunkBiomeData data : (Iterable<ClientboundChunksBiomesPacket.ChunkBiomeData>)packet.chunkBiomeData()) {
/*  910 */       this.level.onChunkLoaded(new ChunkPos((data.pos()).x, (data.pos()).z));
/*      */     }
/*      */     
/*  913 */     for (ClientboundChunksBiomesPacket.ChunkBiomeData data : (Iterable<ClientboundChunksBiomesPacket.ChunkBiomeData>)packet.chunkBiomeData()) {
/*  914 */       for (int xOffset = -1; xOffset <= 1; xOffset++) {
/*  915 */         for (int zOffset = -1; zOffset <= 1; zOffset++) {
/*  916 */           for (int y = this.level.getMinSectionY(); y <= this.level.getMaxSectionY(); y++) {
/*  917 */             this.minecraft.levelRenderer.setSectionDirty((data.pos()).x + xOffset, y, (data.pos()).z + zOffset);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateLevelChunk(int x, int z, ClientboundLevelChunkPacketData chunkData) {
/*  925 */     this.level.getChunkSource().replaceWithPacketData(x, z, chunkData.getReadBuffer(), 
/*  926 */         chunkData.getHeightmaps(), chunkData.getBlockEntitiesTagsConsumer(x, z));
/*      */   }
/*      */   
/*      */   private void enableChunkLight(LevelChunk chunk, int x, int z) {
/*  930 */     LevelLightEngine lightEngine = this.level.getChunkSource().getLightEngine();
/*  931 */     LevelChunkSection[] sections = chunk.getSections();
/*  932 */     ChunkPos chunkPos = chunk.getPos();
/*  933 */     for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/*  934 */       LevelChunkSection section = sections[sectionIndex];
/*  935 */       int sectionY = this.level.getSectionYFromSectionIndex(sectionIndex);
/*  936 */       lightEngine.updateSectionStatus(SectionPos.of(chunkPos, sectionY), section.hasOnlyAir());
/*      */     } 
/*  938 */     this.level.setSectionRangeDirty(x - 1, this.level.getMinSectionY(), z - 1, x + 1, this.level.getMaxSectionY(), z + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleForgetLevelChunk(ClientboundForgetLevelChunkPacket packet) {
/*  943 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  944 */     this.level.getChunkSource().drop(packet.pos());
/*  945 */     this.debugSubscriber.dropChunk(packet.pos());
/*  946 */     queueLightRemoval(packet);
/*      */   }
/*      */   
/*      */   private void queueLightRemoval(ClientboundForgetLevelChunkPacket packet) {
/*  950 */     ChunkPos chunkPos = packet.pos();
/*  951 */     this.level.queueLightUpdate(() -> {
/*      */           LevelLightEngine lightEngine = this.level.getLightEngine();
/*      */           lightEngine.setLightEnabled(chunkPos, false);
/*      */           for (int sectionY = lightEngine.getMinLightSection(); sectionY < lightEngine.getMaxLightSection(); sectionY++) {
/*      */             SectionPos sectionPos = SectionPos.of(chunkPos, sectionY);
/*      */             lightEngine.queueSectionData(LightLayer.BLOCK, sectionPos, null);
/*      */             lightEngine.queueSectionData(LightLayer.SKY, sectionPos, null);
/*      */           } 
/*      */           for (int i = this.level.getMinSectionY(); i <= this.level.getMaxSectionY(); i++) {
/*      */             lightEngine.updateSectionStatus(SectionPos.of(chunkPos, i), true);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockUpdate(ClientboundBlockUpdatePacket packet) {
/*  967 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  968 */     this.level.setServerVerifiedBlockState(packet.getPos(), packet.getBlockState(), 19);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleConfigurationStart(ClientboundStartConfigurationPacket packet) {
/*  973 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */ 
/*      */ 
/*      */     
/*  977 */     this.minecraft.getChatListener().flushQueue();
/*      */     
/*  979 */     sendChatAcknowledgement();
/*      */     
/*  981 */     ChatComponent.State chatState = this.minecraft.gui.getChat().storeState();
/*  982 */     this.minecraft.clearClientLevel((Screen)new ServerReconfigScreen(RECONFIGURE_SCREEN_MESSAGE, this.connection));
/*      */     
/*  984 */     this.connection.setupInboundProtocol(ConfigurationProtocols.CLIENTBOUND, (PacketListener)new ClientConfigurationPacketListenerImpl(this.minecraft, this.connection, new CommonListenerCookie(new LevelLoadTracker(), this.localGameProfile, this.telemetryManager, this.registryAccess, this.enabledFeatures, this.serverBrand, this.serverData, this.postDisconnectScreen, this.serverCookies, chatState, this.customReportDetails, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  999 */             serverLinks(), this.seenPlayers, this.seenInsecureChatWarning)));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1004 */     send((Packet<?>)ServerboundConfigurationAcknowledgedPacket.INSTANCE);
/* 1005 */     this.connection.setupOutboundProtocol(ConfigurationProtocols.SERVERBOUND);
/*      */   }
/*      */   
/*      */   public void handleTakeItemEntity(ClientboundTakeItemEntityPacket packet) {
/*      */     LocalPlayer localPlayer;
/* 1010 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1011 */     Entity from = this.level.getEntity(packet.getItemId());
/* 1012 */     LivingEntity to = (LivingEntity)this.level.getEntity(packet.getPlayerId());
/* 1013 */     if (to == null) {
/* 1014 */       localPlayer = this.minecraft.player;
/*      */     }
/* 1016 */     if (from != null) {
/* 1017 */       if (from instanceof net.minecraft.world.entity.ExperienceOrb) {
/* 1018 */         this.level.playLocalSound(from.getX(), from.getY(), from.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, (this.random.nextFloat() - this.random.nextFloat()) * 0.35F + 0.9F, false);
/*      */       } else {
/* 1020 */         this.level.playLocalSound(from.getX(), from.getY(), from.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 1.4F + 2.0F, false);
/*      */       } 
/*      */       
/* 1023 */       EntityRenderState itemState = this.minecraft.getEntityRenderDispatcher().extractEntity(from, 1.0F);
/* 1024 */       this.minecraft.particleEngine.add((Particle)new ItemPickupParticle(this.level, itemState, (Entity)localPlayer, from.getDeltaMovement()));
/* 1025 */       if (from instanceof ItemEntity) { ItemEntity itemEntity = (ItemEntity)from;
/*      */         
/* 1027 */         ItemStack itemStack = itemEntity.getItem();
/* 1028 */         if (!itemStack.isEmpty()) {
/* 1029 */           itemStack.shrink(packet.getAmount());
/*      */         }
/* 1031 */         if (itemStack.isEmpty()) {
/* 1032 */           this.level.removeEntity(packet.getItemId(), Entity.RemovalReason.DISCARDED);
/*      */         } }
/* 1034 */       else if (!(from instanceof net.minecraft.world.entity.ExperienceOrb))
/* 1035 */       { this.level.removeEntity(packet.getItemId(), Entity.RemovalReason.DISCARDED); }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSystemChat(ClientboundSystemChatPacket packet) {
/* 1042 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1043 */     this.minecraft.getChatListener().handleSystemMessage(packet.content(), packet.overlay());
/*      */   }
/*      */   
/*      */   public void handlePlayerChat(ClientboundPlayerChatPacket packet) {
/*      */     SignedMessageLink link;
/* 1048 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1050 */     int expectedChatIndex = this.nextChatIndex++;
/* 1051 */     if (packet.globalIndex() != expectedChatIndex) {
/* 1052 */       LOGGER.error("Missing or out-of-order chat message from server, expected index {} but got {}", expectedChatIndex, packet.globalIndex());
/* 1053 */       this.connection.disconnect(BAD_CHAT_INDEX);
/*      */       
/*      */       return;
/*      */     } 
/* 1057 */     Optional<SignedMessageBody> body = packet.body().unpack(this.messageSignatureCache);
/* 1058 */     if (body.isEmpty()) {
/* 1059 */       LOGGER.error("Message from player with ID {} referenced unrecognized signature id", packet.sender());
/*      */       
/* 1061 */       this.connection.disconnect(INVALID_PACKET);
/*      */       
/*      */       return;
/*      */     } 
/* 1065 */     this.messageSignatureCache.push(body.get(), packet.signature());
/*      */ 
/*      */     
/* 1068 */     UUID senderId = packet.sender();
/* 1069 */     PlayerInfo sender = getPlayerInfo(senderId);
/* 1070 */     if (sender == null) {
/* 1071 */       LOGGER.error("Received player chat packet for unknown player with ID: {}", senderId);
/* 1072 */       this.minecraft.getChatListener().handleChatMessageError(senderId, packet.signature(), packet.chatType());
/*      */       
/*      */       return;
/*      */     } 
/* 1076 */     RemoteChatSession chatSession = sender.getChatSession();
/*      */     
/* 1078 */     if (chatSession != null) {
/* 1079 */       link = new SignedMessageLink(packet.index(), senderId, chatSession.sessionId());
/*      */     } else {
/* 1081 */       link = SignedMessageLink.unsigned(senderId);
/*      */     } 
/*      */     
/* 1084 */     PlayerChatMessage message = new PlayerChatMessage(link, packet.signature(), body.get(), packet.unsignedContent(), packet.filterMask());
/* 1085 */     message = sender.getMessageValidator().updateAndValidate(message);
/* 1086 */     if (message != null) {
/* 1087 */       this.minecraft.getChatListener().handlePlayerChatMessage(message, sender.getProfile(), packet.chatType());
/*      */     } else {
/* 1089 */       this.minecraft.getChatListener().handleChatMessageError(senderId, packet.signature(), packet.chatType());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDisguisedChat(ClientboundDisguisedChatPacket packet) {
/* 1095 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1096 */     this.minecraft.getChatListener().handleDisguisedChatMessage(packet.message(), packet.chatType());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDeleteChat(ClientboundDeleteChatPacket packet) {
/* 1101 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1103 */     Optional<MessageSignature> signature = packet.messageSignature().unpack(this.messageSignatureCache);
/* 1104 */     if (signature.isEmpty()) {
/* 1105 */       this.connection.disconnect(INVALID_PACKET);
/*      */       
/*      */       return;
/*      */     } 
/* 1109 */     this.lastSeenMessages.ignorePending(signature.get());
/*      */     
/* 1111 */     if (!this.minecraft.getChatListener().removeFromDelayedMessageQueue(signature.get())) {
/* 1112 */       this.minecraft.gui.getChat().deleteMessage(signature.get());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAnimate(ClientboundAnimatePacket packet) {
/* 1118 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1119 */     Entity entity = this.level.getEntity(packet.getId());
/* 1120 */     if (entity == null) {
/*      */       return;
/*      */     }
/* 1123 */     if (packet.getAction() == 0) {
/* 1124 */       LivingEntity mob = (LivingEntity)entity;
/* 1125 */       mob.swing(InteractionHand.MAIN_HAND);
/* 1126 */     } else if (packet.getAction() == 3) {
/* 1127 */       LivingEntity mob = (LivingEntity)entity;
/* 1128 */       mob.swing(InteractionHand.OFF_HAND);
/* 1129 */     } else if (packet.getAction() == 2) {
/* 1130 */       Player player = (Player)entity;
/* 1131 */       player.stopSleepInBed(false, false);
/* 1132 */     } else if (packet.getAction() == 4) {
/* 1133 */       this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.CRIT);
/* 1134 */     } else if (packet.getAction() == 5) {
/* 1135 */       this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.ENCHANTED_HIT);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleHurtAnimation(ClientboundHurtAnimationPacket packet) {
/* 1141 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1142 */     Entity entity = this.level.getEntity(packet.id());
/* 1143 */     if (entity == null) {
/*      */       return;
/*      */     }
/* 1146 */     entity.animateHurt(packet.yaw());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetTime(ClientboundSetTimePacket packet) {
/* 1151 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1152 */     this.level.setTimeFromServer(packet.gameTime(), packet.dayTime(), packet.tickDayTime());
/* 1153 */     this.telemetryManager.setTime(packet.gameTime());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetSpawn(ClientboundSetDefaultSpawnPositionPacket packet) {
/* 1158 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1159 */     this.minecraft.level.setRespawnData(packet.respawnData());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetEntityPassengersPacket(ClientboundSetPassengersPacket packet) {
/* 1164 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1165 */     Entity vehicle = this.level.getEntity(packet.getVehicle());
/* 1166 */     if (vehicle == null) {
/* 1167 */       LOGGER.warn("Received passengers for unknown entity");
/*      */       
/*      */       return;
/*      */     } 
/* 1171 */     boolean wasPlayerMounted = vehicle.hasIndirectPassenger((Entity)this.minecraft.player);
/* 1172 */     vehicle.ejectPassengers();
/*      */     
/* 1174 */     for (int id : packet.getPassengers()) {
/* 1175 */       Entity passenger = this.level.getEntity(id);
/* 1176 */       if (passenger != null) {
/* 1177 */         passenger.startRiding(vehicle, true, false);
/* 1178 */         if (passenger == this.minecraft.player) {
/* 1179 */           this.removedPlayerVehicleId = OptionalInt.empty();
/* 1180 */           if (!wasPlayerMounted) {
/* 1181 */             if (vehicle instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) {
/* 1182 */               this.minecraft.player.yRotO = vehicle.getYRot();
/* 1183 */               this.minecraft.player.setYRot(vehicle.getYRot());
/* 1184 */               this.minecraft.player.setYHeadRot(vehicle.getYRot());
/*      */             } 
/* 1186 */             MutableComponent mutableComponent = Component.translatable("mount.onboard", new Object[] { this.minecraft.options.keyShift.getTranslatedKeyMessage() });
/* 1187 */             this.minecraft.gui.setOverlayMessage((Component)mutableComponent, false);
/* 1188 */             this.minecraft.getNarrator().saySystemNow((Component)mutableComponent);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityLinkPacket(ClientboundSetEntityLinkPacket packet) {
/* 1197 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1198 */     Entity sourceEntity = this.level.getEntity(packet.getSourceId());
/* 1199 */     if (sourceEntity instanceof Leashable) { Leashable leashable = (Leashable)sourceEntity;
/* 1200 */       leashable.setDelayedLeashHolderId(packet.getDestId()); }
/*      */   
/*      */   }
/*      */   
/*      */   private static ItemStack findTotem(Player player) {
/* 1205 */     for (InteractionHand hand : InteractionHand.values()) {
/* 1206 */       ItemStack itemStack = player.getItemInHand(hand);
/* 1207 */       if (itemStack.has(DataComponents.DEATH_PROTECTION)) {
/* 1208 */         return itemStack;
/*      */       }
/*      */     } 
/* 1211 */     return new ItemStack((ItemLike)Items.TOTEM_OF_UNDYING);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityEvent(ClientboundEntityEventPacket packet) {
/* 1216 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1217 */     Entity entity = packet.getEntity(this.level);
/* 1218 */     if (entity != null) {
/*      */       int tickLength;
/* 1220 */       switch (packet.getEventId()) {
/*      */         case 63:
/* 1222 */           this.minecraft.getSoundManager().play((SoundInstance)new SnifferSoundInstance((Sniffer)entity)); break;
/*      */         case 21:
/* 1224 */           this.minecraft.getSoundManager().play((SoundInstance)new net.minecraft.client.resources.sounds.GuardianAttackSoundInstance((Guardian)entity)); break;
/*      */         case 35:
/* 1226 */           tickLength = 40;
/* 1227 */           this.minecraft.particleEngine.createTrackingEmitter(entity, (ParticleOptions)ParticleTypes.TOTEM_OF_UNDYING, 30);
/* 1228 */           this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
/* 1229 */           if (entity == this.minecraft.player)
/* 1230 */             this.minecraft.gameRenderer.displayItemActivation(findTotem((Player)this.minecraft.player)); 
/*      */           break;
/*      */         default:
/* 1233 */           entity.handleEntityEvent(packet.getEventId());
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleDamageEvent(ClientboundDamageEventPacket packet) {
/* 1240 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1241 */     Entity entity = this.level.getEntity(packet.entityId());
/* 1242 */     if (entity == null) {
/*      */       return;
/*      */     }
/*      */     
/* 1246 */     entity.handleDamageEvent(packet.getSource(this.level));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetHealth(ClientboundSetHealthPacket packet) {
/* 1251 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1252 */     this.minecraft.player.hurtTo(packet.getHealth());
/* 1253 */     this.minecraft.player.getFoodData().setFoodLevel(packet.getFood());
/* 1254 */     this.minecraft.player.getFoodData().setSaturation(packet.getSaturation());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetExperience(ClientboundSetExperiencePacket packet) {
/* 1259 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1260 */     this.minecraft.player.setExperienceValues(packet.getExperienceProgress(), packet.getTotalExperience(), packet.getExperienceLevel());
/*      */   }
/*      */   
/*      */   public void handleRespawn(ClientboundRespawnPacket packet) {
/*      */     LocalPlayer newPlayer;
/* 1265 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1266 */     CommonPlayerSpawnInfo spawnInfo = packet.commonPlayerSpawnInfo();
/* 1267 */     ResourceKey<Level> dimensionKey = spawnInfo.dimension();
/* 1268 */     Holder<DimensionType> dimensionType = spawnInfo.dimensionType();
/* 1269 */     LocalPlayer oldPlayer = this.minecraft.player;
/* 1270 */     ResourceKey<Level> oldDimensionKey = oldPlayer.level().dimension();
/* 1271 */     boolean dimensionChanged = (dimensionKey != oldDimensionKey);
/* 1272 */     LevelLoadingScreen.Reason levelLoadingReason = determineLevelLoadingReason(oldPlayer.isDeadOrDying(), dimensionKey, oldDimensionKey);
/* 1273 */     if (dimensionChanged) {
/* 1274 */       Map<MapId, MapItemSavedData> mapData = this.level.getAllMapData();
/* 1275 */       boolean isDebug = spawnInfo.isDebug();
/* 1276 */       boolean isFlat = spawnInfo.isFlat();
/* 1277 */       int seaLevel = spawnInfo.seaLevel();
/* 1278 */       ClientLevel.ClientLevelData levelData = new ClientLevel.ClientLevelData(this.levelData.getDifficulty(), this.levelData.isHardcore(), isFlat);
/* 1279 */       this.levelData = levelData;
/* 1280 */       this.level = new ClientLevel(this, levelData, dimensionKey, dimensionType, this.serverChunkRadius, this.serverSimulationDistance, this.minecraft.levelRenderer, isDebug, spawnInfo.seed(), seaLevel);
/* 1281 */       this.level.addMapData(mapData);
/* 1282 */       this.minecraft.setLevel(this.level);
/* 1283 */       this.debugSubscriber.dropLevel();
/*      */     } 
/*      */     
/* 1286 */     this.minecraft.setCameraEntity(null);
/* 1287 */     if (oldPlayer.hasContainerOpen()) {
/* 1288 */       oldPlayer.closeContainer();
/*      */     }
/*      */ 
/*      */     
/* 1292 */     if (packet.shouldKeep((byte)2)) {
/* 1293 */       newPlayer = this.minecraft.gameMode.createPlayer(this.level, oldPlayer.getStats(), oldPlayer.getRecipeBook(), oldPlayer.getLastSentInput(), oldPlayer.isSprinting());
/*      */     } else {
/* 1295 */       newPlayer = this.minecraft.gameMode.createPlayer(this.level, oldPlayer.getStats(), oldPlayer.getRecipeBook());
/*      */     } 
/*      */     
/* 1298 */     setClientLoaded(false);
/* 1299 */     startWaitingForNewLevel(newPlayer, this.level, levelLoadingReason);
/*      */     
/* 1301 */     newPlayer.setId(oldPlayer.getId());
/* 1302 */     this.minecraft.player = newPlayer;
/* 1303 */     if (dimensionChanged) {
/* 1304 */       this.minecraft.getMusicManager().stopPlaying();
/*      */     }
/* 1306 */     this.minecraft.setCameraEntity((Entity)newPlayer);
/*      */     
/* 1308 */     if (packet.shouldKeep((byte)2)) {
/* 1309 */       List<SynchedEntityData.DataValue<?>> data = oldPlayer.getEntityData().getNonDefaultValues();
/* 1310 */       if (data != null) {
/* 1311 */         newPlayer.getEntityData().assignValues(data);
/*      */       }
/* 1313 */       newPlayer.setDeltaMovement(oldPlayer.getDeltaMovement());
/* 1314 */       newPlayer.setYRot(oldPlayer.getYRot());
/* 1315 */       newPlayer.setXRot(oldPlayer.getXRot());
/*      */     } else {
/* 1317 */       newPlayer.resetPos();
/* 1318 */       newPlayer.setYRot(-180.0F);
/*      */     } 
/*      */     
/* 1321 */     if (packet.shouldKeep((byte)1)) {
/* 1322 */       newPlayer.getAttributes().assignAllValues(oldPlayer.getAttributes());
/*      */     } else {
/* 1324 */       newPlayer.getAttributes().assignBaseValues(oldPlayer.getAttributes());
/*      */     } 
/*      */     
/* 1327 */     this.level.addEntity((Entity)newPlayer);
/* 1328 */     newPlayer.input = (ClientInput)new KeyboardInput(this.minecraft.options);
/* 1329 */     this.minecraft.gameMode.adjustPlayer((Player)newPlayer);
/* 1330 */     newPlayer.setReducedDebugInfo(oldPlayer.isReducedDebugInfo());
/* 1331 */     newPlayer.setShowDeathScreen(oldPlayer.shouldShowDeathScreen());
/* 1332 */     newPlayer.setLastDeathLocation(spawnInfo.lastDeathLocation());
/* 1333 */     newPlayer.setPortalCooldown(spawnInfo.portalCooldown());
/* 1334 */     newPlayer.portalEffectIntensity = oldPlayer.portalEffectIntensity;
/* 1335 */     newPlayer.oPortalEffectIntensity = oldPlayer.oPortalEffectIntensity;
/* 1336 */     if (this.minecraft.screen instanceof DeathScreen || this.minecraft.screen instanceof DeathScreen.TitleConfirmScreen) {
/* 1337 */       this.minecraft.setScreen(null);
/*      */     }
/* 1339 */     this.minecraft.gameMode.setLocalMode(spawnInfo.gameType(), spawnInfo.previousGameType());
/*      */   }
/*      */   
/*      */   private LevelLoadingScreen.Reason determineLevelLoadingReason(boolean playerDied, ResourceKey<Level> dimensionKey, ResourceKey<Level> oldDimensionKey) {
/* 1343 */     LevelLoadingScreen.Reason levelLoadingReason = LevelLoadingScreen.Reason.OTHER;
/* 1344 */     if (!playerDied) {
/* 1345 */       if (dimensionKey == Level.NETHER || oldDimensionKey == Level.NETHER) {
/* 1346 */         levelLoadingReason = LevelLoadingScreen.Reason.NETHER_PORTAL;
/* 1347 */       } else if (dimensionKey == Level.END || oldDimensionKey == Level.END) {
/* 1348 */         levelLoadingReason = LevelLoadingScreen.Reason.END_PORTAL;
/*      */       } 
/*      */     }
/* 1351 */     return levelLoadingReason;
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleExplosion(ClientboundExplodePacket packet) {
/* 1356 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1357 */     Vec3 center = packet.center();
/* 1358 */     this.minecraft.level.playLocalSound(center.x(), center.y(), center.z(), (net.minecraft.sounds.SoundEvent)packet.explosionSound().value(), SoundSource.BLOCKS, 4.0F, (1.0F + (this.minecraft.level.random.nextFloat() - this.minecraft.level.random.nextFloat()) * 0.2F) * 0.7F, false);
/* 1359 */     this.minecraft.level.addParticle(packet.explosionParticle(), center.x(), center.y(), center.z(), 1.0D, 0.0D, 0.0D);
/* 1360 */     this.minecraft.level.trackExplosionEffects(center, packet.radius(), packet.blockCount(), packet.blockParticles());
/* 1361 */     Objects.requireNonNull(this.minecraft.player); packet.playerKnockback().ifPresent(this.minecraft.player::addDeltaMovement);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMountScreenOpen(ClientboundMountScreenOpenPacket packet) {
/* 1366 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1367 */     Entity entity = this.level.getEntity(packet.getEntityId());
/* 1368 */     LocalPlayer player = this.minecraft.player;
/* 1369 */     int inventoryColumns = packet.getInventoryColumns();
/* 1370 */     SimpleContainer container = new SimpleContainer(AbstractMountInventoryMenu.getInventorySize(inventoryColumns));
/* 1371 */     if (entity instanceof AbstractHorse) { AbstractHorse horse = (AbstractHorse)entity;
/* 1372 */       HorseInventoryMenu menu = new HorseInventoryMenu(packet.getContainerId(), player.getInventory(), (Container)container, horse, inventoryColumns);
/* 1373 */       player.containerMenu = (AbstractContainerMenu)menu;
/* 1374 */       this.minecraft.setScreen((Screen)new net.minecraft.client.gui.screens.inventory.HorseInventoryScreen(menu, player.getInventory(), horse, inventoryColumns)); }
/* 1375 */     else if (entity instanceof AbstractNautilus) { AbstractNautilus nautilus = (AbstractNautilus)entity;
/* 1376 */       NautilusInventoryMenu menu = new NautilusInventoryMenu(packet.getContainerId(), player.getInventory(), (Container)container, nautilus, inventoryColumns);
/* 1377 */       player.containerMenu = (AbstractContainerMenu)menu;
/* 1378 */       this.minecraft.setScreen((Screen)new NautilusInventoryScreen(menu, player.getInventory(), nautilus, inventoryColumns)); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleOpenScreen(ClientboundOpenScreenPacket packet) {
/* 1384 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1385 */     net.minecraft.client.gui.screens.MenuScreens.create(packet.getType(), this.minecraft, packet.getContainerId(), packet.getTitle());
/*      */   }
/*      */   
/*      */   public void handleContainerSetSlot(ClientboundContainerSetSlotPacket packet) {
/*      */     boolean creative;
/* 1390 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1391 */     LocalPlayer localPlayer = this.minecraft.player;
/* 1392 */     ItemStack itemStack = packet.getItem();
/* 1393 */     int slot = packet.getSlot();
/*      */     
/* 1395 */     this.minecraft.getTutorial().onGetItem(itemStack);
/*      */ 
/*      */ 
/*      */     
/* 1399 */     Screen screen = this.minecraft.screen; if (screen instanceof CreativeModeInventoryScreen) { CreativeModeInventoryScreen creativeModeInventoryScreen = (CreativeModeInventoryScreen)screen;
/* 1400 */       creative = !creativeModeInventoryScreen.isInventoryOpen(); }
/*      */     else
/* 1402 */     { creative = false; }
/*      */ 
/*      */     
/* 1405 */     if (packet.getContainerId() == 0) {
/* 1406 */       if (InventoryMenu.isHotbarSlot(slot) && 
/* 1407 */         !itemStack.isEmpty()) {
/* 1408 */         ItemStack lastItemStack = ((Player)localPlayer).inventoryMenu.getSlot(slot).getItem();
/* 1409 */         if (lastItemStack.isEmpty() || lastItemStack.getCount() < itemStack.getCount()) {
/* 1410 */           itemStack.setPopTime(5);
/*      */         }
/*      */       } 
/*      */       
/* 1414 */       ((Player)localPlayer).inventoryMenu.setItem(slot, packet.getStateId(), itemStack);
/* 1415 */     } else if (packet.getContainerId() == ((Player)localPlayer).containerMenu.containerId && (packet.getContainerId() != 0 || !creative)) {
/* 1416 */       ((Player)localPlayer).containerMenu.setItem(slot, packet.getStateId(), itemStack);
/*      */     } 
/*      */     
/* 1419 */     if (this.minecraft.screen instanceof CreativeModeInventoryScreen) {
/*      */ 
/*      */ 
/*      */       
/* 1423 */       ((Player)localPlayer).inventoryMenu.setRemoteSlot(slot, itemStack);
/* 1424 */       ((Player)localPlayer).inventoryMenu.broadcastChanges();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCursorItem(ClientboundSetCursorItemPacket packet) {
/* 1430 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1432 */     this.minecraft.getTutorial().onGetItem(packet.contents());
/*      */ 
/*      */     
/* 1435 */     if (!(this.minecraft.screen instanceof CreativeModeInventoryScreen)) {
/* 1436 */       this.minecraft.player.containerMenu.setCarried(packet.contents());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetPlayerInventory(ClientboundSetPlayerInventoryPacket packet) {
/* 1442 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1444 */     this.minecraft.getTutorial().onGetItem(packet.contents());
/*      */     
/* 1446 */     this.minecraft.player.getInventory().setItem(packet.slot(), packet.contents());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerContent(ClientboundContainerSetContentPacket packet) {
/* 1451 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1452 */     LocalPlayer localPlayer = this.minecraft.player;
/* 1453 */     if (packet.containerId() == 0) {
/* 1454 */       ((Player)localPlayer).inventoryMenu.initializeContents(packet.stateId(), packet.items(), packet.carriedItem());
/* 1455 */     } else if (packet.containerId() == ((Player)localPlayer).containerMenu.containerId) {
/* 1456 */       ((Player)localPlayer).containerMenu.initializeContents(packet.stateId(), packet.items(), packet.carriedItem());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleOpenSignEditor(ClientboundOpenSignEditorPacket packet) {
/* 1462 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1463 */     BlockPos pos = packet.getPos();
/*      */     
/* 1465 */     BlockEntity blockEntity = this.level.getBlockEntity(pos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity sign = (SignBlockEntity)blockEntity;
/* 1466 */       this.minecraft.player.openTextEdit(sign, packet.isFrontText()); }
/*      */     else
/* 1468 */     { LOGGER.warn("Ignoring openTextEdit on an invalid entity: {} at pos {}", this.level.getBlockEntity(pos), pos); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockEntityData(ClientboundBlockEntityDataPacket packet) {
/* 1474 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1475 */     BlockPos pos = packet.getPos();
/* 1476 */     this.minecraft.level.getBlockEntity(pos, packet.getType()).ifPresent(blockEntity -> { ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(packet.problemPath(), LOGGER); try { packet.loadWithComponents(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)this.registryAccess, packet.getTag())); reporter.close(); }
/* 1477 */           catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1)
/*      */             { throwable.addSuppressed(throwable1); }
/*      */             
/*      */             throw throwable; }
/*      */           
/*      */           if (packet instanceof net.minecraft.world.level.block.entity.CommandBlockEntity && this.minecraft.screen instanceof CommandBlockEditScreen) {
/*      */             ((CommandBlockEditScreen)this.minecraft.screen).updateGui();
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   public void handleContainerSetData(ClientboundContainerSetDataPacket packet) {
/* 1489 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1490 */     LocalPlayer localPlayer = this.minecraft.player;
/* 1491 */     if (((Player)localPlayer).containerMenu.containerId == packet.getContainerId()) {
/* 1492 */       ((Player)localPlayer).containerMenu.setData(packet.getId(), packet.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetEquipment(ClientboundSetEquipmentPacket packet) {
/* 1498 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1499 */     Entity entity = this.level.getEntity(packet.getEntity());
/* 1500 */     if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 1501 */       packet.getSlots().forEach(e -> livingEntity.setItemSlot((EquipmentSlot)e.getFirst(), (ItemStack)e.getSecond())); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerClose(ClientboundContainerClosePacket packet) {
/* 1507 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1508 */     this.minecraft.player.clientSideCloseContainer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockEvent(ClientboundBlockEventPacket packet) {
/* 1513 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1514 */     this.minecraft.level.blockEvent(packet.getPos(), packet.getBlock(), packet.getB0(), packet.getB1());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockDestruction(ClientboundBlockDestructionPacket packet) {
/* 1519 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1520 */     this.minecraft.level.destroyBlockProgress(packet.getId(), packet.getPos(), packet.getProgress());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleGameEvent(ClientboundGameEventPacket packet) {
/* 1525 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1526 */     LocalPlayer localPlayer = this.minecraft.player;
/* 1527 */     ClientboundGameEventPacket.Type event = packet.getEvent();
/* 1528 */     float paramFloat = packet.getParam();
/* 1529 */     int param = Mth.floor(paramFloat + 0.5F);
/* 1530 */     if (event == ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE) {
/* 1531 */       localPlayer.displayClientMessage((Component)Component.translatable("block.minecraft.spawn.not_valid"), false);
/* 1532 */     } else if (event == ClientboundGameEventPacket.START_RAINING) {
/* 1533 */       this.level.getLevelData().setRaining(true);
/* 1534 */       this.level.setRainLevel(0.0F);
/* 1535 */     } else if (event == ClientboundGameEventPacket.STOP_RAINING) {
/* 1536 */       this.level.getLevelData().setRaining(false);
/* 1537 */       this.level.setRainLevel(1.0F);
/* 1538 */     } else if (event == ClientboundGameEventPacket.CHANGE_GAME_MODE) {
/* 1539 */       this.minecraft.gameMode.setLocalMode(net.minecraft.world.level.GameType.byId(param));
/* 1540 */     } else if (event == ClientboundGameEventPacket.WIN_GAME) {
/* 1541 */       this.minecraft.setScreen((Screen)new WinScreen(true, () -> {
/*      */               this.minecraft.player.connection.send((Packet<?>)new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
/*      */               this.minecraft.setScreen(null);
/*      */             }));
/* 1545 */     } else if (event == ClientboundGameEventPacket.DEMO_EVENT) {
/* 1546 */       MutableComponent mutableComponent; Options options = this.minecraft.options;
/* 1547 */       Component message = null;
/* 1548 */       if (paramFloat == 0.0F) {
/* 1549 */         this.minecraft.setScreen((Screen)new DemoIntroScreen());
/* 1550 */       } else if (paramFloat == 101.0F) {
/* 1551 */         mutableComponent = Component.translatable("demo.help.movement", new Object[] { options.keyUp.getTranslatedKeyMessage(), options.keyLeft.getTranslatedKeyMessage(), options.keyDown.getTranslatedKeyMessage(), options.keyRight.getTranslatedKeyMessage() });
/* 1552 */       } else if (paramFloat == 102.0F) {
/* 1553 */         mutableComponent = Component.translatable("demo.help.jump", new Object[] { options.keyJump.getTranslatedKeyMessage() });
/* 1554 */       } else if (paramFloat == 103.0F) {
/* 1555 */         mutableComponent = Component.translatable("demo.help.inventory", new Object[] { options.keyInventory.getTranslatedKeyMessage() });
/* 1556 */       } else if (paramFloat == 104.0F) {
/* 1557 */         mutableComponent = Component.translatable("demo.day.6", new Object[] { options.keyScreenshot.getTranslatedKeyMessage() });
/*      */       } 
/*      */       
/* 1560 */       if (mutableComponent != null) {
/* 1561 */         this.minecraft.gui.getChat().addMessage((Component)mutableComponent);
/* 1562 */         this.minecraft.getNarrator().saySystemQueued((Component)mutableComponent);
/*      */       } 
/* 1564 */     } else if (event == ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND) {
/* 1565 */       this.level.playSound((Entity)localPlayer, localPlayer.getX(), localPlayer.getEyeY(), localPlayer.getZ(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.18F, 0.45F);
/* 1566 */     } else if (event == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
/* 1567 */       this.level.setRainLevel(paramFloat);
/* 1568 */     } else if (event == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
/* 1569 */       this.level.setThunderLevel(paramFloat);
/* 1570 */     } else if (event == ClientboundGameEventPacket.PUFFER_FISH_STING) {
/* 1571 */       this.level.playSound((Entity)localPlayer, localPlayer.getX(), localPlayer.getY(), localPlayer.getZ(), SoundEvents.PUFFER_FISH_STING, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 1572 */     } else if (event == ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT) {
/* 1573 */       this.level.addParticle((ParticleOptions)ParticleTypes.ELDER_GUARDIAN, localPlayer.getX(), localPlayer.getY(), localPlayer.getZ(), 0.0D, 0.0D, 0.0D);
/*      */       
/* 1575 */       if (param == 1) {
/* 1576 */         this.level.playSound((Entity)localPlayer, localPlayer.getX(), localPlayer.getY(), localPlayer.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.HOSTILE, 1.0F, 1.0F);
/*      */       }
/* 1578 */     } else if (event == ClientboundGameEventPacket.IMMEDIATE_RESPAWN) {
/* 1579 */       this.minecraft.player.setShowDeathScreen((paramFloat == 0.0F));
/* 1580 */     } else if (event == ClientboundGameEventPacket.LIMITED_CRAFTING) {
/* 1581 */       this.minecraft.player.setDoLimitedCrafting((paramFloat == 1.0F));
/* 1582 */     } else if (event == ClientboundGameEventPacket.LEVEL_CHUNKS_LOAD_START && 
/* 1583 */       this.levelLoadTracker != null) {
/* 1584 */       this.levelLoadTracker.loadingPacketsReceived();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void startWaitingForNewLevel(LocalPlayer player, ClientLevel level, LevelLoadingScreen.Reason reason) {
/* 1590 */     if (this.levelLoadTracker == null)
/*      */     {
/* 1592 */       this.levelLoadTracker = new LevelLoadTracker();
/*      */     }
/* 1594 */     this.levelLoadTracker.startClientLoad(player, level, this.minecraft.levelRenderer);
/* 1595 */     Screen screen = this.minecraft.screen; if (screen instanceof LevelLoadingScreen) { LevelLoadingScreen loadingScreen = (LevelLoadingScreen)screen;
/* 1596 */       loadingScreen.update(this.levelLoadTracker, reason); }
/*      */     else
/* 1598 */     { this.minecraft.gui.getChat().preserveCurrentChatScreen();
/* 1599 */       this.minecraft.setScreenAndShow((Screen)new LevelLoadingScreen(this.levelLoadTracker, reason)); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMapItemData(ClientboundMapItemDataPacket packet) {
/* 1605 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1606 */     MapId id = packet.mapId();
/* 1607 */     MapItemSavedData data = this.minecraft.level.getMapData(id);
/*      */     
/* 1609 */     if (data == null) {
/* 1610 */       data = MapItemSavedData.createForClient(packet.scale(), packet.locked(), this.minecraft.level.dimension());
/* 1611 */       this.minecraft.level.overrideMapData(id, data);
/*      */     } 
/*      */     
/* 1614 */     packet.applyToMap(data);
/* 1615 */     this.minecraft.getMapTextureManager().update(id, data);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleLevelEvent(ClientboundLevelEventPacket packet) {
/* 1620 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1621 */     if (packet.isGlobalEvent()) {
/* 1622 */       this.minecraft.level.globalLevelEvent(packet.getType(), packet.getPos(), packet.getData());
/*      */     } else {
/* 1624 */       this.minecraft.level.levelEvent(packet.getType(), packet.getPos(), packet.getData());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateAdvancementsPacket(ClientboundUpdateAdvancementsPacket packet) {
/* 1630 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1631 */     this.advancements.update(packet);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSelectAdvancementsTab(ClientboundSelectAdvancementsTabPacket packet) {
/* 1636 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1637 */     Identifier id = packet.getTab();
/* 1638 */     if (id == null) {
/* 1639 */       this.advancements.setSelectedTab(null, false);
/*      */     } else {
/* 1641 */       AdvancementHolder advancement = this.advancements.get(id);
/* 1642 */       this.advancements.setSelectedTab(advancement, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCommands(ClientboundCommandsPacket packet) {
/* 1648 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1649 */     this.commands = new CommandDispatcher(packet.getRoot(CommandBuildContext.simple((HolderLookup.Provider)this.registryAccess, this.enabledFeatures), COMMAND_NODE_BUILDER));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStopSoundEvent(ClientboundStopSoundPacket packet) {
/* 1654 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1655 */     this.minecraft.getSoundManager().stop(packet.getName(), packet.getSource());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCommandSuggestions(ClientboundCommandSuggestionsPacket packet) {
/* 1660 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1661 */     this.suggestionsProvider.completeCustomSuggestions(packet.id(), packet.toSuggestions());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateRecipes(ClientboundUpdateRecipesPacket packet) {
/* 1666 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1667 */     this.recipes = new ClientRecipeContainer(packet.itemSets(), packet.stonecutterRecipes());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleLookAt(ClientboundPlayerLookAtPacket packet) {
/* 1672 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1673 */     Vec3 pos = packet.getPosition(this.level);
/* 1674 */     if (pos != null) {
/* 1675 */       this.minecraft.player.lookAt(packet.getFromAnchor(), pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTagQueryPacket(ClientboundTagQueryPacket packet) {
/* 1681 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1683 */     if (!this.debugQueryHandler.handleResponse(packet.getTransactionId(), packet.getTag())) {
/* 1684 */       LOGGER.debug("Got unhandled response to tag query {}", packet.getTransactionId());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAwardStats(ClientboundAwardStatsPacket packet) {
/* 1690 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1692 */     for (ObjectIterator<Object2IntMap.Entry<Stat<?>>> objectIterator = packet.stats().object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<Stat<?>> entry = objectIterator.next();
/* 1693 */       Stat<?> stat = (Stat)entry.getKey();
/* 1694 */       int amount = entry.getIntValue();
/*      */       
/* 1696 */       this.minecraft.player.getStats().setValue((Player)this.minecraft.player, stat, amount); }
/*      */ 
/*      */     
/* 1699 */     Screen screen = this.minecraft.screen; if (screen instanceof StatsScreen) { StatsScreen statsScreen = (StatsScreen)screen;
/* 1700 */       statsScreen.onStatsUpdated(); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRecipeBookAdd(ClientboundRecipeBookAddPacket packet) {
/* 1706 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1708 */     ClientRecipeBook recipeBook = this.minecraft.player.getRecipeBook();
/*      */     
/* 1710 */     if (packet.replace()) {
/* 1711 */       recipeBook.clear();
/*      */     }
/*      */     
/* 1714 */     for (ClientboundRecipeBookAddPacket.Entry entry : (Iterable<ClientboundRecipeBookAddPacket.Entry>)packet.entries()) {
/* 1715 */       recipeBook.add(entry.contents());
/* 1716 */       if (entry.highlight()) {
/* 1717 */         recipeBook.addHighlight(entry.contents().id());
/*      */       }
/*      */       
/* 1720 */       if (entry.notification()) {
/* 1721 */         RecipeToast.addOrUpdate(this.minecraft.getToastManager(), entry.contents().display());
/*      */       }
/*      */     } 
/* 1724 */     refreshRecipeBook(recipeBook);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRecipeBookRemove(ClientboundRecipeBookRemovePacket packet) {
/* 1729 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1730 */     ClientRecipeBook recipeBook = this.minecraft.player.getRecipeBook();
/* 1731 */     for (RecipeDisplayId id : (Iterable<RecipeDisplayId>)packet.recipes()) {
/* 1732 */       recipeBook.remove(id);
/*      */     }
/*      */     
/* 1735 */     refreshRecipeBook(recipeBook);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRecipeBookSettings(ClientboundRecipeBookSettingsPacket packet) {
/* 1740 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1741 */     ClientRecipeBook recipeBook = this.minecraft.player.getRecipeBook();
/* 1742 */     recipeBook.setBookSettings(packet.bookSettings());
/* 1743 */     refreshRecipeBook(recipeBook);
/*      */   }
/*      */   
/*      */   private void refreshRecipeBook(ClientRecipeBook recipeBook) {
/* 1747 */     recipeBook.rebuildCollections();
/* 1748 */     this.searchTrees.updateRecipes(recipeBook, this.level);
/*      */     
/* 1750 */     Screen screen = this.minecraft.screen; if (screen instanceof RecipeUpdateListener) { RecipeUpdateListener updateListener = (RecipeUpdateListener)screen;
/* 1751 */       updateListener.recipesUpdated(); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateMobEffect(ClientboundUpdateMobEffectPacket packet) {
/* 1757 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1758 */     Entity entity = this.level.getEntity(packet.getEntityId());
/* 1759 */     if (!(entity instanceof LivingEntity)) {
/*      */       return;
/*      */     }
/*      */     
/* 1763 */     Holder<MobEffect> effect = packet.getEffect();
/* 1764 */     MobEffectInstance mobEffectInstance = new MobEffectInstance(effect, packet.getEffectDurationTicks(), packet.getEffectAmplifier(), packet.isEffectAmbient(), packet.isEffectVisible(), packet.effectShowsIcon(), null);
/* 1765 */     if (!packet.shouldBlend()) {
/* 1766 */       mobEffectInstance.skipBlending();
/*      */     }
/* 1768 */     ((LivingEntity)entity).forceAddEffect(mobEffectInstance, null);
/*      */   }
/*      */   
/*      */   private <T> Registry.PendingTags<T> updateTags(ResourceKey<? extends Registry<? extends T>> registryKey, TagNetworkSerialization.NetworkPayload payload) {
/* 1772 */     Registry<T> registry = this.registryAccess.lookupOrThrow(registryKey);
/* 1773 */     return registry.prepareTagReload(payload.resolve(registry));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateTags(ClientboundUpdateTagsPacket packet) {
/* 1778 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1780 */     List<Registry.PendingTags<?>> pendingTags = new ArrayList<>(packet.getTags().size());
/* 1781 */     boolean ignoreSharedTags = this.connection.isMemoryConnection();
/* 1782 */     packet.getTags().forEach((key, networkPayload) -> {
/*      */           if (!ignoreSharedTags || RegistrySynchronization.isNetworkable(ignoreSharedTags)) {
/*      */             ignoreSharedTags.add(updateTags(ignoreSharedTags, networkPayload));
/*      */           }
/*      */         });
/*      */     
/* 1788 */     pendingTags.forEach(Registry.PendingTags::apply);
/* 1789 */     this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)this.registryAccess, this.enabledFeatures);
/* 1790 */     List<ItemStack> searchItems = List.copyOf(CreativeModeTabs.searchTab().getDisplayItems());
/* 1791 */     this.searchTrees.updateCreativeTags(searchItems);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handlePlayerCombatEnd(ClientboundPlayerCombatEndPacket packet) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void handlePlayerCombatEnter(ClientboundPlayerCombatEnterPacket packet) {}
/*      */ 
/*      */   
/*      */   public void handlePlayerCombatKill(ClientboundPlayerCombatKillPacket packet) {
/* 1804 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1806 */     Entity player = this.level.getEntity(packet.playerId());
/* 1807 */     if (player == this.minecraft.player) {
/* 1808 */       if (this.minecraft.player.shouldShowDeathScreen()) {
/* 1809 */         this.minecraft.setScreen((Screen)new DeathScreen(packet.message(), this.level.getLevelData().isHardcore(), this.minecraft.player));
/*      */       } else {
/* 1811 */         this.minecraft.player.respawn();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChangeDifficulty(ClientboundChangeDifficultyPacket packet) {
/* 1818 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1819 */     this.levelData.setDifficulty(packet.difficulty());
/* 1820 */     this.levelData.setDifficultyLocked(packet.locked());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCamera(ClientboundSetCameraPacket packet) {
/* 1825 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1826 */     Entity entity = packet.getEntity(this.level);
/* 1827 */     if (entity != null) {
/* 1828 */       this.minecraft.setCameraEntity(entity);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleInitializeBorder(ClientboundInitializeBorderPacket packet) {
/* 1834 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1835 */     WorldBorder border = this.level.getWorldBorder();
/*      */     
/* 1837 */     border.setCenter(packet.getNewCenterX(), packet.getNewCenterZ());
/*      */     
/* 1839 */     long lerpTime = packet.getLerpTime();
/* 1840 */     if (lerpTime > 0L) {
/* 1841 */       border.lerpSizeBetween(packet.getOldSize(), packet.getNewSize(), lerpTime, this.level.getGameTime());
/*      */     } else {
/* 1843 */       border.setSize(packet.getNewSize());
/*      */     } 
/*      */     
/* 1846 */     border.setAbsoluteMaxSize(packet.getNewAbsoluteMaxSize());
/* 1847 */     border.setWarningBlocks(packet.getWarningBlocks());
/* 1848 */     border.setWarningTime(packet.getWarningTime());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetBorderCenter(ClientboundSetBorderCenterPacket packet) {
/* 1853 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1854 */     this.level.getWorldBorder().setCenter(packet.getNewCenterX(), packet.getNewCenterZ());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetBorderLerpSize(ClientboundSetBorderLerpSizePacket packet) {
/* 1859 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1860 */     this.level.getWorldBorder().lerpSizeBetween(packet.getOldSize(), packet.getNewSize(), packet.getLerpTime(), this.level.getGameTime());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetBorderSize(ClientboundSetBorderSizePacket packet) {
/* 1865 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1866 */     this.level.getWorldBorder().setSize(packet.getSize());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetBorderWarningDistance(ClientboundSetBorderWarningDistancePacket packet) {
/* 1871 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1872 */     this.level.getWorldBorder().setWarningBlocks(packet.getWarningBlocks());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetBorderWarningDelay(ClientboundSetBorderWarningDelayPacket packet) {
/* 1877 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1878 */     this.level.getWorldBorder().setWarningTime(packet.getWarningDelay());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTitlesClear(ClientboundClearTitlesPacket packet) {
/* 1883 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1884 */     this.minecraft.gui.clearTitles();
/* 1885 */     if (packet.shouldResetTimes()) {
/* 1886 */       this.minecraft.gui.resetTitleTimes();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleServerData(ClientboundServerDataPacket packet) {
/* 1892 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1893 */     if (this.serverData == null) {
/*      */       return;
/*      */     }
/*      */     
/* 1897 */     this.serverData.motd = packet.motd();
/* 1898 */     Objects.requireNonNull(this.serverData); packet.iconBytes().map(ServerData::validateIcon).ifPresent(this.serverData::setIconBytes);
/*      */     
/* 1900 */     ServerList.saveSingleServer(this.serverData);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCustomChatCompletions(ClientboundCustomChatCompletionsPacket packet) {
/* 1905 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1906 */     this.suggestionsProvider.modifyCustomCompletions(packet.action(), packet.entries());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setActionBarText(ClientboundSetActionBarTextPacket packet) {
/* 1911 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1912 */     this.minecraft.gui.setOverlayMessage(packet.text(), false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTitleText(ClientboundSetTitleTextPacket packet) {
/* 1917 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1918 */     this.minecraft.gui.setTitle(packet.text());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSubtitleText(ClientboundSetSubtitleTextPacket packet) {
/* 1923 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1924 */     this.minecraft.gui.setSubtitle(packet.text());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTitlesAnimation(ClientboundSetTitlesAnimationPacket packet) {
/* 1929 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1930 */     this.minecraft.gui.setTimes(packet.getFadeIn(), packet.getStay(), packet.getFadeOut());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTabListCustomisation(ClientboundTabListPacket packet) {
/* 1935 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1936 */     this.minecraft.gui.getTabList().setHeader(packet.header().getString().isEmpty() ? null : packet.header());
/* 1937 */     this.minecraft.gui.getTabList().setFooter(packet.footer().getString().isEmpty() ? null : packet.footer());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRemoveMobEffect(ClientboundRemoveMobEffectPacket packet) {
/* 1942 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1943 */     Entity entity = packet.getEntity(this.level); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 1944 */       livingEntity.removeEffectNoUpdate(packet.effect()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerInfoRemove(ClientboundPlayerInfoRemovePacket packet) {
/* 1950 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 1951 */     for (UUID profileId : (Iterable<UUID>)packet.profileIds()) {
/* 1952 */       this.minecraft.getPlayerSocialManager().removePlayer(profileId);
/* 1953 */       PlayerInfo info = this.playerInfoMap.remove(profileId);
/* 1954 */       if (info != null) {
/* 1955 */         this.listedPlayers.remove(info);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket packet) {
/* 1962 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 1964 */     for (ClientboundPlayerInfoUpdatePacket.Entry entry : (Iterable<ClientboundPlayerInfoUpdatePacket.Entry>)packet.newEntries()) {
/* 1965 */       PlayerInfo playerInfo = new PlayerInfo(Objects.<GameProfile>requireNonNull(entry.profile()), enforcesSecureChat());
/* 1966 */       if (this.playerInfoMap.putIfAbsent(entry.profileId(), playerInfo) == null) {
/* 1967 */         this.minecraft.getPlayerSocialManager().addPlayer(playerInfo);
/*      */       }
/*      */     } 
/*      */     
/* 1971 */     for (ClientboundPlayerInfoUpdatePacket.Entry entry : (Iterable<ClientboundPlayerInfoUpdatePacket.Entry>)packet.entries()) {
/* 1972 */       PlayerInfo info = this.playerInfoMap.get(entry.profileId());
/* 1973 */       if (info == null) {
/* 1974 */         LOGGER.warn("Ignoring player info update for unknown player {} ({})", entry.profileId(), packet.actions());
/*      */         
/*      */         continue;
/*      */       } 
/* 1978 */       for (ClientboundPlayerInfoUpdatePacket.Action action : (Iterable<ClientboundPlayerInfoUpdatePacket.Action>)packet.actions()) {
/* 1979 */         applyPlayerInfoUpdate(action, entry, info);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void applyPlayerInfoUpdate(ClientboundPlayerInfoUpdatePacket.Action action, ClientboundPlayerInfoUpdatePacket.Entry entry, PlayerInfo info) {
/* 1985 */     switch (action) { case INITIALIZE_CHAT:
/* 1986 */         initializeChatSession(entry, info); break;
/*      */       case UPDATE_GAME_MODE:
/* 1988 */         if (info.getGameMode() != entry.gameMode() && this.minecraft.player != null && 
/*      */           
/* 1990 */           this.minecraft.player.getUUID().equals(entry.profileId()))
/*      */         {
/* 1992 */           this.minecraft.player.onGameModeChanged(entry.gameMode());
/*      */         }
/* 1994 */         info.setGameMode(entry.gameMode());
/*      */         break;
/*      */       case UPDATE_LISTED:
/* 1997 */         if (entry.listed()) {
/* 1998 */           this.listedPlayers.add(info); break;
/*      */         } 
/* 2000 */         this.listedPlayers.remove(info);
/*      */         break;
/*      */       case UPDATE_LATENCY:
/* 2003 */         info.setLatency(entry.latency()); break;
/* 2004 */       case UPDATE_DISPLAY_NAME: info.setTabListDisplayName(entry.displayName()); break;
/* 2005 */       case UPDATE_HAT: info.setShowHat(entry.showHat()); break;
/* 2006 */       case UPDATE_LIST_ORDER: info.setTabListOrder(entry.listOrder());
/*      */         break; }
/*      */   
/*      */   }
/*      */   private void initializeChatSession(ClientboundPlayerInfoUpdatePacket.Entry entry, PlayerInfo info) {
/* 2011 */     GameProfile profile = info.getProfile();
/* 2012 */     SignatureValidator signatureValidator = this.minecraft.services().profileKeySignatureValidator();
/* 2013 */     if (signatureValidator == null) {
/* 2014 */       LOGGER.warn("Ignoring chat session from {} due to missing Services public key", profile.name());
/* 2015 */       info.clearChatSession(enforcesSecureChat());
/*      */       return;
/*      */     } 
/* 2018 */     RemoteChatSession.Data chatSessionData = entry.chatSession();
/* 2019 */     if (chatSessionData != null) {
/*      */       try {
/* 2021 */         RemoteChatSession chatSession = chatSessionData.validate(profile, signatureValidator);
/* 2022 */         info.setChatSession(chatSession);
/* 2023 */       } catch (ProfilePublicKey.ValidationException e) {
/* 2024 */         LOGGER.error("Failed to validate profile key for player: '{}'", profile.name(), e);
/* 2025 */         info.clearChatSession(enforcesSecureChat());
/*      */       } 
/*      */     } else {
/* 2028 */       info.clearChatSession(enforcesSecureChat());
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean enforcesSecureChat() {
/* 2033 */     return (this.minecraft.services().canValidateProfileKeys() && this.serverEnforcesSecureChat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerAbilities(ClientboundPlayerAbilitiesPacket packet) {
/* 2038 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2039 */     LocalPlayer localPlayer = this.minecraft.player;
/* 2040 */     (localPlayer.getAbilities()).flying = packet.isFlying();
/* 2041 */     (localPlayer.getAbilities()).instabuild = packet.canInstabuild();
/* 2042 */     (localPlayer.getAbilities()).invulnerable = packet.isInvulnerable();
/* 2043 */     (localPlayer.getAbilities()).mayfly = packet.canFly();
/* 2044 */     localPlayer.getAbilities().setFlyingSpeed(packet.getFlyingSpeed());
/* 2045 */     localPlayer.getAbilities().setWalkingSpeed(packet.getWalkingSpeed());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSoundEvent(ClientboundSoundPacket packet) {
/* 2050 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2051 */     this.minecraft.level.playSeededSound((Entity)this.minecraft.player, packet.getX(), packet.getY(), packet.getZ(), packet.getSound(), packet.getSource(), packet.getVolume(), packet.getPitch(), packet.getSeed());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSoundEntityEvent(ClientboundSoundEntityPacket packet) {
/* 2056 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2057 */     Entity entity = this.level.getEntity(packet.getId());
/* 2058 */     if (entity == null) {
/*      */       return;
/*      */     }
/* 2061 */     this.minecraft.level.playSeededSound((Entity)this.minecraft.player, entity, packet.getSound(), packet.getSource(), packet.getVolume(), packet.getPitch(), packet.getSeed());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBossUpdate(ClientboundBossEventPacket packet) {
/* 2066 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2067 */     this.minecraft.gui.getBossOverlay().update(packet);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleItemCooldown(ClientboundCooldownPacket packet) {
/* 2072 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2073 */     if (packet.duration() == 0) {
/* 2074 */       this.minecraft.player.getCooldowns().removeCooldown(packet.cooldownGroup());
/*      */     } else {
/* 2076 */       this.minecraft.player.getCooldowns().addCooldown(packet.cooldownGroup(), packet.duration());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMoveVehicle(ClientboundMoveVehiclePacket packet) {
/* 2082 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2083 */     Entity vehicle = this.minecraft.player.getRootVehicle();
/* 2084 */     if (vehicle != this.minecraft.player && vehicle.isLocalInstanceAuthoritative()) {
/* 2085 */       Vec3 currentTarget; Vec3 target = packet.position();
/*      */       
/* 2087 */       if (vehicle.isInterpolating()) {
/* 2088 */         currentTarget = vehicle.getInterpolation().position();
/*      */       } else {
/* 2090 */         currentTarget = vehicle.position();
/*      */       } 
/* 2092 */       if (target.distanceTo(currentTarget) > 9.999999747378752E-6D) {
/* 2093 */         if (vehicle.isInterpolating()) {
/* 2094 */           vehicle.getInterpolation().cancel();
/*      */         }
/* 2096 */         vehicle.absSnapTo(target.x(), target.y(), target.z(), packet.yRot(), packet.xRot());
/*      */       } 
/* 2098 */       this.connection.send((Packet)ServerboundMoveVehiclePacket.fromEntity(vehicle));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleOpenBook(ClientboundOpenBookPacket packet) {
/* 2104 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2105 */     ItemStack held = this.minecraft.player.getItemInHand(packet.getHand());
/* 2106 */     BookViewScreen.BookAccess bookAccess = BookViewScreen.BookAccess.fromItem(held);
/* 2107 */     if (bookAccess != null) {
/* 2108 */       this.minecraft.setScreen((Screen)new BookViewScreen(bookAccess));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleCustomPayload(CustomPacketPayload payload) {
/* 2117 */     handleUnknownCustomPayload(payload);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleUnknownCustomPayload(CustomPacketPayload payload) {
/* 2123 */     LOGGER.warn("Unknown custom packet payload: {}", payload.type().id());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAddObjective(ClientboundSetObjectivePacket packet) {
/* 2128 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 2130 */     String objectiveName = packet.getObjectiveName();
/* 2131 */     if (packet.getMethod() == 0) {
/* 2132 */       this.scoreboard.addObjective(objectiveName, ObjectiveCriteria.DUMMY, packet.getDisplayName(), packet.getRenderType(), false, packet.getNumberFormat().orElse(null));
/*      */     } else {
/* 2134 */       Objective objective = this.scoreboard.getObjective(objectiveName);
/* 2135 */       if (objective != null) {
/* 2136 */         if (packet.getMethod() == 1) {
/* 2137 */           this.scoreboard.removeObjective(objective);
/* 2138 */         } else if (packet.getMethod() == 2) {
/* 2139 */           objective.setRenderType(packet.getRenderType());
/* 2140 */           objective.setDisplayName(packet.getDisplayName());
/* 2141 */           objective.setNumberFormat(packet.getNumberFormat().orElse(null));
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetScore(ClientboundSetScorePacket packet) {
/* 2149 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2150 */     String objectiveName = packet.objectiveName();
/*      */     
/* 2152 */     ScoreHolder scoreHolder = ScoreHolder.forNameOnly(packet.owner());
/* 2153 */     Objective objective = this.scoreboard.getObjective(objectiveName);
/* 2154 */     if (objective != null) {
/* 2155 */       ScoreAccess score = this.scoreboard.getOrCreatePlayerScore(scoreHolder, objective, true);
/* 2156 */       score.set(packet.score());
/* 2157 */       score.display(packet.display().orElse(null));
/* 2158 */       score.numberFormatOverride(packet.numberFormat().orElse(null));
/*      */     } else {
/* 2160 */       LOGGER.warn("Received packet for unknown scoreboard objective: {}", objectiveName);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleResetScore(ClientboundResetScorePacket packet) {
/* 2166 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2167 */     String objectiveName = packet.objectiveName();
/*      */     
/* 2169 */     ScoreHolder scoreHolder = ScoreHolder.forNameOnly(packet.owner());
/* 2170 */     if (objectiveName == null) {
/* 2171 */       this.scoreboard.resetAllPlayerScores(scoreHolder);
/*      */     } else {
/* 2173 */       Objective objective = this.scoreboard.getObjective(objectiveName);
/* 2174 */       if (objective != null) {
/* 2175 */         this.scoreboard.resetSinglePlayerScore(scoreHolder, objective);
/*      */       } else {
/* 2177 */         LOGGER.warn("Received packet for unknown scoreboard objective: {}", objectiveName);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetDisplayObjective(ClientboundSetDisplayObjectivePacket packet) {
/* 2184 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 2186 */     String objectiveName = packet.getObjectiveName();
/* 2187 */     Objective objective = (objectiveName == null) ? null : this.scoreboard.getObjective(objectiveName);
/* 2188 */     this.scoreboard.setDisplayObjective(packet.getSlot(), objective);
/*      */   }
/*      */   
/*      */   public void handleSetPlayerTeamPacket(ClientboundSetPlayerTeamPacket packet) {
/*      */     PlayerTeam team;
/* 2193 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */ 
/*      */     
/* 2196 */     ClientboundSetPlayerTeamPacket.Action teamAction = packet.getTeamAction();
/*      */     
/* 2198 */     if (teamAction == ClientboundSetPlayerTeamPacket.Action.ADD) {
/* 2199 */       team = this.scoreboard.addPlayerTeam(packet.getName());
/*      */     } else {
/* 2201 */       team = this.scoreboard.getPlayerTeam(packet.getName());
/* 2202 */       if (team == null) {
/* 2203 */         LOGGER.warn("Received packet for unknown team {}: team action: {}, player action: {}", new Object[] { packet.getName(), packet.getTeamAction(), packet.getPlayerAction() });
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 2208 */     Optional<ClientboundSetPlayerTeamPacket.Parameters> parameters = packet.getParameters();
/*      */     
/* 2210 */     parameters.ifPresent(p -> {
/*      */           team.setDisplayName(p.getDisplayName());
/*      */           
/*      */           team.setColor(p.getColor());
/*      */           team.unpackOptions(p.getOptions());
/*      */           team.setNameTagVisibility(p.getNametagVisibility());
/*      */           team.setCollisionRule(p.getCollisionRule());
/*      */           team.setPlayerPrefix(p.getPlayerPrefix());
/*      */           team.setPlayerSuffix(p.getPlayerSuffix());
/*      */         });
/* 2220 */     ClientboundSetPlayerTeamPacket.Action playerAction = packet.getPlayerAction();
/* 2221 */     if (playerAction == ClientboundSetPlayerTeamPacket.Action.ADD) {
/* 2222 */       for (String player : (Iterable<String>)packet.getPlayers()) {
/* 2223 */         this.scoreboard.addPlayerToTeam(player, team);
/*      */       }
/* 2225 */     } else if (playerAction == ClientboundSetPlayerTeamPacket.Action.REMOVE) {
/* 2226 */       for (String player : (Iterable<String>)packet.getPlayers()) {
/* 2227 */         this.scoreboard.removePlayerFromTeam(player, team);
/*      */       }
/*      */     } 
/*      */     
/* 2231 */     if (teamAction == ClientboundSetPlayerTeamPacket.Action.REMOVE) {
/* 2232 */       this.scoreboard.removePlayerTeam(team);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleParticleEvent(ClientboundLevelParticlesPacket packet) {
/* 2238 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2239 */     if (packet.getCount() == 0) {
/* 2240 */       double xa = (packet.getMaxSpeed() * packet.getXDist());
/* 2241 */       double ya = (packet.getMaxSpeed() * packet.getYDist());
/* 2242 */       double za = (packet.getMaxSpeed() * packet.getZDist());
/*      */       try {
/* 2244 */         this.level.addParticle(packet.getParticle(), packet.isOverrideLimiter(), packet.alwaysShow(), packet.getX(), packet.getY(), packet.getZ(), xa, ya, za);
/* 2245 */       } catch (Throwable ignored) {
/* 2246 */         LOGGER.warn("Could not spawn particle effect {}", packet.getParticle());
/*      */       } 
/*      */     } else {
/* 2249 */       for (int i = 0; i < packet.getCount(); i++) {
/* 2250 */         double xVarience = this.random.nextGaussian() * packet.getXDist();
/* 2251 */         double yVarience = this.random.nextGaussian() * packet.getYDist();
/* 2252 */         double zVarience = this.random.nextGaussian() * packet.getZDist();
/* 2253 */         double xa = this.random.nextGaussian() * packet.getMaxSpeed();
/* 2254 */         double ya = this.random.nextGaussian() * packet.getMaxSpeed();
/* 2255 */         double za = this.random.nextGaussian() * packet.getMaxSpeed();
/*      */         try {
/* 2257 */           this.level.addParticle(packet.getParticle(), packet.isOverrideLimiter(), packet.alwaysShow(), packet.getX() + xVarience, packet.getY() + yVarience, packet.getZ() + zVarience, xa, ya, za);
/* 2258 */         } catch (Throwable ignored) {
/* 2259 */           LOGGER.warn("Could not spawn particle effect {}", packet.getParticle());
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUpdateAttributes(ClientboundUpdateAttributesPacket packet) {
/* 2268 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2269 */     Entity entity = this.level.getEntity(packet.getEntityId());
/* 2270 */     if (entity == null) {
/*      */       return;
/*      */     }
/* 2273 */     if (!(entity instanceof LivingEntity)) {
/* 2274 */       throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + String.valueOf(entity) + ")");
/*      */     }
/*      */     
/* 2277 */     AttributeMap attributes = ((LivingEntity)entity).getAttributes();
/* 2278 */     for (ClientboundUpdateAttributesPacket.AttributeSnapshot attribute : (Iterable<ClientboundUpdateAttributesPacket.AttributeSnapshot>)packet.getValues()) {
/* 2279 */       AttributeInstance instance = attributes.getInstance(attribute.attribute());
/*      */       
/* 2281 */       if (instance == null) {
/* 2282 */         LOGGER.warn("Entity {} does not have attribute {}", entity, attribute.attribute().getRegisteredName());
/*      */         continue;
/*      */       } 
/* 2285 */       instance.setBaseValue(attribute.base());
/* 2286 */       instance.removeModifiers();
/*      */       
/* 2288 */       for (AttributeModifier modifier : (Iterable<AttributeModifier>)attribute.modifiers()) {
/* 2289 */         instance.addTransientModifier(modifier);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlaceRecipe(ClientboundPlaceGhostRecipePacket packet) {
/* 2296 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*      */     
/* 2298 */     AbstractContainerMenu containerMenu = this.minecraft.player.containerMenu;
/* 2299 */     if (containerMenu.containerId != packet.containerId()) {
/*      */       return;
/*      */     }
/*      */     
/* 2303 */     Screen screen = this.minecraft.screen; if (screen instanceof RecipeUpdateListener) { RecipeUpdateListener listener = (RecipeUpdateListener)screen;
/* 2304 */       listener.fillGhostRecipe(packet.recipeDisplay()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleLightUpdatePacket(ClientboundLightUpdatePacket packet) {
/* 2310 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2311 */     int x = packet.getX();
/* 2312 */     int z = packet.getZ();
/* 2313 */     ClientboundLightUpdatePacketData lightData = packet.getLightData();
/* 2314 */     this.level.queueLightUpdate(() -> applyLightData(x, z, lightData, true));
/*      */   }
/*      */   
/*      */   private void applyLightData(int x, int z, ClientboundLightUpdatePacketData lightData, boolean scheduleRebuild) {
/* 2318 */     LevelLightEngine lightEngine = this.level.getChunkSource().getLightEngine();
/* 2319 */     BitSet skyYMask = lightData.getSkyYMask();
/* 2320 */     BitSet emptySkyYMask = lightData.getEmptySkyYMask();
/* 2321 */     Iterator<byte[]> skyUpdates = (Iterator)lightData.getSkyUpdates().iterator();
/*      */     
/* 2323 */     readSectionList(x, z, lightEngine, LightLayer.SKY, skyYMask, emptySkyYMask, skyUpdates, scheduleRebuild);
/*      */     
/* 2325 */     BitSet blockYMask = lightData.getBlockYMask();
/* 2326 */     BitSet emptyBlockYMask = lightData.getEmptyBlockYMask();
/* 2327 */     Iterator<byte[]> blockUpdates = (Iterator)lightData.getBlockUpdates().iterator();
/* 2328 */     readSectionList(x, z, lightEngine, LightLayer.BLOCK, blockYMask, emptyBlockYMask, blockUpdates, scheduleRebuild);
/*      */     
/* 2330 */     lightEngine.setLightEnabled(new ChunkPos(x, z), true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMerchantOffers(ClientboundMerchantOffersPacket packet) {
/* 2335 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2336 */     AbstractContainerMenu menu = this.minecraft.player.containerMenu;
/* 2337 */     if (packet.getContainerId() == menu.containerId && menu instanceof MerchantMenu) { MerchantMenu merchantMenu = (MerchantMenu)menu;
/* 2338 */       merchantMenu.setOffers(packet.getOffers());
/* 2339 */       merchantMenu.setXp(packet.getVillagerXp());
/* 2340 */       merchantMenu.setMerchantLevel(packet.getVillagerLevel());
/* 2341 */       merchantMenu.setShowProgressBar(packet.showProgress());
/* 2342 */       merchantMenu.setCanRestock(packet.canRestock()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetChunkCacheRadius(ClientboundSetChunkCacheRadiusPacket packet) {
/* 2348 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2349 */     this.serverChunkRadius = packet.getRadius();
/* 2350 */     this.minecraft.options.setServerRenderDistance(this.serverChunkRadius);
/* 2351 */     this.level.getChunkSource().updateViewRadius(packet.getRadius());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetSimulationDistance(ClientboundSetSimulationDistancePacket packet) {
/* 2356 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2357 */     this.serverSimulationDistance = packet.simulationDistance();
/* 2358 */     this.level.setServerSimulationDistance(this.serverSimulationDistance);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetChunkCacheCenter(ClientboundSetChunkCacheCenterPacket packet) {
/* 2363 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2364 */     this.level.getChunkSource().updateViewCenter(packet.getX(), packet.getZ());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockChangedAck(ClientboundBlockChangedAckPacket packet) {
/* 2369 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2370 */     this.level.handleBlockChangedAck(packet.sequence());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBundlePacket(ClientboundBundlePacket packet) {
/* 2375 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2376 */     for (Packet<? super ClientGamePacketListener> subPacket : (Iterable<Packet<? super ClientGamePacketListener>>)packet.subPackets()) {
/* 2377 */       subPacket.handle((PacketListener)this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleProjectilePowerPacket(ClientboundProjectilePowerPacket packet) {
/* 2383 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2384 */     Entity entity = this.level.getEntity(packet.getId());
/* 2385 */     if (entity instanceof AbstractHurtingProjectile) { AbstractHurtingProjectile projectile = (AbstractHurtingProjectile)entity;
/* 2386 */       projectile.accelerationPower = packet.getAccelerationPower(); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChunkBatchStart(ClientboundChunkBatchStartPacket packet) {
/* 2392 */     this.chunkBatchSizeCalculator.onBatchStart();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChunkBatchFinished(ClientboundChunkBatchFinishedPacket packet) {
/* 2397 */     this.chunkBatchSizeCalculator.onBatchFinished(packet.batchSize());
/* 2398 */     send((Packet<?>)new ServerboundChunkBatchReceivedPacket(this.chunkBatchSizeCalculator.getDesiredChunksPerTick()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDebugSample(ClientboundDebugSamplePacket packet) {
/* 2403 */     this.minecraft.getDebugOverlay().logRemoteSample(packet.sample(), packet.debugSampleType());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePongResponse(ClientboundPongResponsePacket packet) {
/* 2408 */     this.pingDebugMonitor.onPongReceived(packet);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTestInstanceBlockStatus(ClientboundTestInstanceBlockStatus packet) {
/* 2413 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2414 */     Screen screen = this.minecraft.screen; if (screen instanceof TestInstanceBlockEditScreen) { TestInstanceBlockEditScreen editScreen = (TestInstanceBlockEditScreen)screen;
/* 2415 */       editScreen.setStatus(packet.status(), packet.size()); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleWaypoint(ClientboundTrackedWaypointPacket packet) {
/* 2421 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2422 */     packet.apply((TrackedWaypointManager)this.waypointManager);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDebugChunkValue(ClientboundDebugChunkValuePacket packet) {
/* 2427 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2428 */     this.debugSubscriber.updateChunk(this.level.getGameTime(), packet.chunkPos(), packet.update());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDebugBlockValue(ClientboundDebugBlockValuePacket packet) {
/* 2433 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2434 */     this.debugSubscriber.updateBlock(this.level.getGameTime(), packet.blockPos(), packet.update());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDebugEntityValue(ClientboundDebugEntityValuePacket packet) {
/* 2439 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2440 */     Entity entity = this.level.getEntity(packet.entityId());
/* 2441 */     if (entity != null) {
/* 2442 */       this.debugSubscriber.updateEntity(this.level.getGameTime(), entity, packet.update());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleDebugEvent(ClientboundDebugEventPacket packet) {
/* 2448 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2449 */     this.debugSubscriber.pushEvent(this.level.getGameTime(), packet.event());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleGameTestHighlightPos(ClientboundGameTestHighlightPosPacket packet) {
/* 2454 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 2455 */     this.minecraft.levelRenderer.gameTestBlockHighlightRenderer.highlightPos(packet.absolutePos(), packet.relativePos());
/*      */   }
/*      */   
/*      */   private void readSectionList(int chunkX, int chunkZ, LevelLightEngine lightEngine, LightLayer layer, BitSet yMask, BitSet emptyYMask, Iterator<byte[]> updates, boolean scheduleRebuild) {
/* 2459 */     for (int sectionIndex = 0; sectionIndex < lightEngine.getLightSectionCount(); sectionIndex++) {
/* 2460 */       int sectionY = lightEngine.getMinLightSection() + sectionIndex;
/* 2461 */       boolean haveData = yMask.get(sectionIndex);
/* 2462 */       boolean haveEmpty = emptyYMask.get(sectionIndex);
/* 2463 */       if (haveData || haveEmpty) {
/* 2464 */         lightEngine.queueSectionData(layer, SectionPos.of(chunkX, sectionY, chunkZ), haveData ? new DataLayer((byte[])((byte[])updates.next()).clone()) : new DataLayer());
/* 2465 */         if (scheduleRebuild) {
/* 2466 */           this.level.setSectionDirtyWithNeighbors(chunkX, sectionY, chunkZ);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public Connection getConnection() {
/* 2473 */     return this.connection;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAcceptingMessages() {
/* 2478 */     return (this.connection.isConnected() && !this.closed);
/*      */   }
/*      */   
/*      */   public Collection<PlayerInfo> getListedOnlinePlayers() {
/* 2482 */     return this.listedPlayers;
/*      */   }
/*      */   
/*      */   public Collection<PlayerInfo> getOnlinePlayers() {
/* 2486 */     return this.playerInfoMap.values();
/*      */   }
/*      */   
/*      */   public Collection<UUID> getOnlinePlayerIds() {
/* 2490 */     return this.playerInfoMap.keySet();
/*      */   }
/*      */   
/*      */   public PlayerInfo getPlayerInfo(UUID player) {
/* 2494 */     return this.playerInfoMap.get(player);
/*      */   }
/*      */   
/*      */   public PlayerInfo getPlayerInfo(String player) {
/* 2498 */     for (PlayerInfo playerInfo : this.playerInfoMap.values()) {
/* 2499 */       if (playerInfo.getProfile().name().equals(player)) {
/* 2500 */         return playerInfo;
/*      */       }
/*      */     } 
/*      */     
/* 2504 */     return null;
/*      */   }
/*      */   
/*      */   public Map<UUID, PlayerInfo> getSeenPlayers() {
/* 2508 */     return this.seenPlayers;
/*      */   }
/*      */   
/*      */   public PlayerInfo getPlayerInfoIgnoreCase(String player) {
/* 2512 */     for (PlayerInfo playerInfo : this.playerInfoMap.values()) {
/* 2513 */       if (playerInfo.getProfile().name().equalsIgnoreCase(player)) {
/* 2514 */         return playerInfo;
/*      */       }
/*      */     } 
/*      */     
/* 2518 */     return null;
/*      */   }
/*      */   
/*      */   public GameProfile getLocalGameProfile() {
/* 2522 */     return this.localGameProfile;
/*      */   }
/*      */   
/*      */   public ClientAdvancements getAdvancements() {
/* 2526 */     return this.advancements;
/*      */   }
/*      */   
/*      */   public CommandDispatcher<ClientSuggestionProvider> getCommands() {
/* 2530 */     return this.commands;
/*      */   }
/*      */   
/*      */   public ClientLevel getLevel() {
/* 2534 */     return this.level;
/*      */   }
/*      */   
/*      */   public DebugQueryHandler getDebugQueryHandler() {
/* 2538 */     return this.debugQueryHandler;
/*      */   }
/*      */   
/*      */   public UUID getId() {
/* 2542 */     return this.id;
/*      */   }
/*      */   
/*      */   public Set<ResourceKey<Level>> levels() {
/* 2546 */     return this.levels;
/*      */   }
/*      */   
/*      */   public RegistryAccess.Frozen registryAccess() {
/* 2550 */     return this.registryAccess;
/*      */   }
/*      */   
/*      */   public void markMessageAsProcessed(MessageSignature signature, boolean wasShown) {
/* 2554 */     if (this.lastSeenMessages.addPending(signature, wasShown) && 
/* 2555 */       this.lastSeenMessages.offset() > 64) {
/* 2556 */       sendChatAcknowledgement();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendChatAcknowledgement() {
/* 2562 */     int offset = this.lastSeenMessages.getAndClearOffset();
/* 2563 */     if (offset > 0) {
/* 2564 */       send((Packet<?>)new net.minecraft.network.protocol.game.ServerboundChatAckPacket(offset));
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendChat(String content) {
/* 2569 */     Instant timeStamp = Instant.now();
/* 2570 */     long salt = Crypt.SaltSupplier.getLong();
/* 2571 */     LastSeenMessagesTracker.Update lastSeenUpdate = this.lastSeenMessages.generateAndApplyUpdate();
/* 2572 */     MessageSignature signature = this.signedMessageEncoder.pack(new SignedMessageBody(content, timeStamp, salt, lastSeenUpdate.lastSeen()));
/* 2573 */     send((Packet<?>)new ServerboundChatPacket(content, timeStamp, salt, signature, lastSeenUpdate.update()));
/*      */   }
/*      */   
/*      */   public void sendCommand(String command) {
/* 2577 */     SignableCommand<ClientSuggestionProvider> signableCommand = SignableCommand.of(this.commands.parse(command, this.suggestionsProvider));
/* 2578 */     if (signableCommand.arguments().isEmpty()) {
/* 2579 */       send((Packet<?>)new ServerboundChatCommandPacket(command));
/*      */       
/*      */       return;
/*      */     } 
/* 2583 */     Instant timeStamp = Instant.now();
/* 2584 */     long salt = Crypt.SaltSupplier.getLong();
/* 2585 */     LastSeenMessagesTracker.Update lastSeenUpdate = this.lastSeenMessages.generateAndApplyUpdate();
/* 2586 */     ArgumentSignatures argumentSignatures = ArgumentSignatures.signCommand(signableCommand, argument -> {
/*      */           SignedMessageBody signedBody = new SignedMessageBody(lastSeenUpdate, timeStamp, timeStamp, salt.lastSeen());
/*      */           return this.signedMessageEncoder.pack(signedBody);
/*      */         });
/* 2590 */     send((Packet<?>)new ServerboundChatCommandSignedPacket(command, timeStamp, salt, argumentSignatures, lastSeenUpdate.update()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendUnattendedCommand(String command, Screen screenAfterCommand) {
/* 2598 */     switch (verifyCommand(command).ordinal()) {
/*      */       case 0:
/* 2600 */         send((Packet<?>)new ServerboundChatCommandPacket(command));
/* 2601 */         this.minecraft.setScreen(screenAfterCommand);
/*      */         break;
/*      */       case 1:
/* 2604 */         openCommandSendConfirmationWindow(command, "multiplayer.confirm_command.parse_errors", screenAfterCommand); break;
/*      */       case 3:
/* 2606 */         openCommandSendConfirmationWindow(command, "multiplayer.confirm_command.permissions_required", screenAfterCommand); break;
/*      */       case 2:
/* 2608 */         openSignedCommandSendConfirmationWindow(command, "multiplayer.confirm_command.signature_required", screenAfterCommand);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/* 2613 */   private enum CommandCheckResult { NO_ISSUES,
/* 2614 */     PARSE_ERRORS,
/* 2615 */     SIGNATURE_REQUIRED,
/* 2616 */     PERMISSIONS_REQUIRED; }
/*      */ 
/*      */   
/*      */   private CommandCheckResult verifyCommand(String command) {
/* 2620 */     ParseResults<ClientSuggestionProvider> parseWithCurrentPermissions = this.commands.parse(command, this.suggestionsProvider);
/* 2621 */     if (!isValidCommand(parseWithCurrentPermissions)) {
/* 2622 */       return CommandCheckResult.PARSE_ERRORS;
/*      */     }
/*      */     
/* 2625 */     if (SignableCommand.hasSignableArguments(parseWithCurrentPermissions)) {
/* 2626 */       return CommandCheckResult.SIGNATURE_REQUIRED;
/*      */     }
/*      */     
/* 2629 */     ParseResults<ClientSuggestionProvider> parseWithoutPermissions = this.commands.parse(command, this.restrictedSuggestionsProvider);
/* 2630 */     if (!isValidCommand(parseWithoutPermissions)) {
/* 2631 */       return CommandCheckResult.PERMISSIONS_REQUIRED;
/*      */     }
/*      */     
/* 2634 */     return CommandCheckResult.NO_ISSUES;
/*      */   }
/*      */   
/*      */   private static boolean isValidCommand(ParseResults<?> parseResults) {
/* 2638 */     return (!parseResults.getReader().canRead() && 
/* 2639 */       parseResults.getExceptions().isEmpty() && 
/* 2640 */       parseResults.getContext().getLastChild().getCommand() != null);
/*      */   }
/*      */   
/*      */   private void openSendConfirmationWindow(String command, String messageKey, Component acceptButton, Runnable onAccept) {
/* 2644 */     Screen currentScreen = this.minecraft.screen;
/* 2645 */     this.minecraft.setScreen((Screen)new ConfirmScreen(result -> { if (currentScreen) { onAccept.run(); } else { this.minecraft.setScreen(onAccept); }  }, COMMAND_SEND_CONFIRM_TITLE, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2654 */           (Component)Component.translatable(messageKey, new Object[] { Component.literal(command).withStyle(ChatFormatting.YELLOW) }), acceptButton, 
/*      */           
/* 2656 */           (currentScreen != null) ? CommonComponents.GUI_BACK : CommonComponents.GUI_CANCEL));
/*      */   }
/*      */ 
/*      */   
/*      */   private void openCommandSendConfirmationWindow(String command, String messageKey, Screen screenAfterCommand) {
/* 2661 */     openSendConfirmationWindow(command, messageKey, BUTTON_RUN_COMMAND, () -> {
/*      */           send((Packet<?>)new ServerboundChatCommandPacket(command));
/*      */           this.minecraft.setScreen(screenAfterCommand);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void openSignedCommandSendConfirmationWindow(String command, String messageKey, Screen screenAfterCommand) {
/* 2673 */     boolean canOpenChatScreen = (screenAfterCommand == null && this.minecraft.getChatStatus().isChatAllowed(this.minecraft.isLocalServer()));
/* 2674 */     openSendConfirmationWindow(command, messageKey, 
/*      */ 
/*      */         
/* 2677 */         canOpenChatScreen ? BUTTON_SUGGEST_COMMAND : CommonComponents.GUI_COPY_TO_CLIPBOARD, () -> {
/*      */           if (canOpenChatScreen) {
/*      */             this.minecraft.openChatScreen(ChatComponent.ChatMethod.COMMAND);
/*      */             Screen patt0$temp = this.minecraft.screen;
/*      */             if (patt0$temp instanceof ChatScreen) {
/*      */               ChatScreen chatScreen = (ChatScreen)patt0$temp;
/*      */               chatScreen.insertText(command, false);
/*      */             } 
/*      */           } else {
/*      */             this.minecraft.keyboardHandler.setClipboard("/" + command);
/*      */             this.minecraft.setScreen(screenAfterCommand);
/*      */           } 
/*      */         });
/*      */   }
/*      */   
/*      */   public void broadcastClientInformation(ClientInformation information) {
/* 2693 */     if (!information.equals(this.remoteClientInformation)) {
/* 2694 */       send((Packet<?>)new ServerboundClientInformationPacket(information));
/* 2695 */       this.remoteClientInformation = information;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/* 2701 */     if (this.chatSession != null && this.minecraft.getProfileKeyPairManager().shouldRefreshKeyPair()) {
/* 2702 */       prepareKeyPair();
/*      */     }
/*      */     
/* 2705 */     if (this.keyPairFuture != null && this.keyPairFuture.isDone()) {
/* 2706 */       ((Optional)this.keyPairFuture.join()).ifPresent(this::setKeyPair);
/* 2707 */       this.keyPairFuture = null;
/*      */     } 
/*      */     
/* 2710 */     sendDeferredPackets();
/*      */     
/* 2712 */     if (this.minecraft.getDebugOverlay().showNetworkCharts()) {
/* 2713 */       this.pingDebugMonitor.tick();
/*      */     }
/*      */     
/* 2716 */     if (this.level != null) {
/* 2717 */       this.debugSubscriber.tick(this.level.getGameTime());
/*      */     }
/*      */     
/* 2720 */     this.telemetryManager.tick();
/*      */     
/* 2722 */     if (this.levelLoadTracker != null) {
/* 2723 */       this.levelLoadTracker.tickClientLoad();
/* 2724 */       if (this.levelLoadTracker.isLevelReady()) {
/* 2725 */         notifyPlayerLoaded();
/* 2726 */         this.levelLoadTracker = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void notifyPlayerLoaded() {
/* 2732 */     if (!hasClientLoaded()) {
/*      */       
/* 2734 */       this.connection.send((Packet)new ServerboundPlayerLoadedPacket());
/* 2735 */       setClientLoaded(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void prepareKeyPair() {
/* 2740 */     this.keyPairFuture = this.minecraft.getProfileKeyPairManager().prepareKeyPair();
/*      */   }
/*      */ 
/*      */   
/*      */   private void setKeyPair(ProfileKeyPair keyPair) {
/* 2745 */     if (!this.minecraft.isLocalPlayer(this.localGameProfile.id())) {
/*      */       return;
/*      */     }
/* 2748 */     if (this.chatSession != null && this.chatSession.keyPair().equals(keyPair)) {
/*      */       return;
/*      */     }
/*      */     
/* 2752 */     this.chatSession = LocalChatSession.create(keyPair);
/* 2753 */     this.signedMessageEncoder = this.chatSession.createMessageEncoder(this.localGameProfile.id());
/* 2754 */     send((Packet<?>)new ServerboundChatSessionUpdatePacket(this.chatSession.asRemote().asData()));
/*      */   }
/*      */ 
/*      */   
/*      */   protected DialogConnectionAccess createDialogAccess() {
/* 2759 */     return new ClientCommonPacketListenerImpl.CommonDialogAccess()
/*      */       {
/*      */         public void runCommand(String command, Screen activeScreen) {
/* 2762 */           ClientPacketListener.this.sendUnattendedCommand(command, activeScreen);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public ServerData getServerData() {
/* 2768 */     return this.serverData;
/*      */   }
/*      */   
/*      */   public FeatureFlagSet enabledFeatures() {
/* 2772 */     return this.enabledFeatures;
/*      */   }
/*      */   
/*      */   public boolean isFeatureEnabled(FeatureFlagSet requiredFlags) {
/* 2776 */     return requiredFlags.isSubsetOf(enabledFeatures());
/*      */   }
/*      */   
/*      */   public Scoreboard scoreboard() {
/* 2780 */     return this.scoreboard;
/*      */   }
/*      */   
/*      */   public PotionBrewing potionBrewing() {
/* 2784 */     return this.potionBrewing;
/*      */   }
/*      */   
/*      */   public FuelValues fuelValues() {
/* 2788 */     return this.fuelValues;
/*      */   }
/*      */   
/*      */   public void updateSearchTrees() {
/* 2792 */     this.searchTrees.rebuildAfterLanguageChange();
/*      */   }
/*      */   
/*      */   public SessionSearchTrees searchTrees() {
/* 2796 */     return this.searchTrees;
/*      */   }
/*      */   
/*      */   public void registerForCleaning(CacheSlot<?, ?> slot) {
/* 2800 */     this.cacheSlots.add(new WeakReference<>(slot));
/*      */   }
/*      */   
/*      */   public HashedPatchMap.HashGenerator decoratedHashOpsGenenerator() {
/* 2804 */     return this.decoratedHashOpsGenerator;
/*      */   }
/*      */   
/*      */   public ClientWaypointManager getWaypointManager() {
/* 2808 */     return this.waypointManager;
/*      */   }
/*      */   
/*      */   public DebugValueAccess createDebugValueAccess() {
/* 2812 */     return this.debugSubscriber.createDebugValueAccess(this.level);
/*      */   }
/*      */   
/*      */   public boolean hasClientLoaded() {
/* 2816 */     return this.clientLoaded;
/*      */   }
/*      */   
/*      */   private void setClientLoaded(boolean loaded) {
/* 2820 */     this.clientLoaded = loaded;
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */