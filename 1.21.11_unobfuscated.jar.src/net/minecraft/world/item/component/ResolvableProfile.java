/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public abstract class ResolvableProfile implements TooltipProvider {
/*     */   private static final Codec<ResolvableProfile> FULL_CODEC;
/*     */   
/*     */   static {
/*  35 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.mapEither(ExtraCodecs.STORED_GAME_PROFILE, Partial.MAP_CODEC).forGetter(ResolvableProfile::unpack), (App)PlayerSkin.Patch.MAP_CODEC.forGetter(ResolvableProfile::skinPatch)).apply((Applicative)i, ResolvableProfile::create));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final Codec<ResolvableProfile> CODEC = Codec.withAlternative(FULL_CODEC, ExtraCodecs.PLAYER_NAME, ResolvableProfile::createUnresolved);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final StreamCodec<ByteBuf, ResolvableProfile> STREAM_CODEC = StreamCodec.composite(
/*  47 */       ByteBufCodecs.either(ByteBufCodecs.GAME_PROFILE, Partial.STREAM_CODEC), ResolvableProfile::unpack, PlayerSkin.Patch.STREAM_CODEC, ResolvableProfile::skinPatch, ResolvableProfile::create);
/*     */   
/*     */   protected final GameProfile partialProfile;
/*     */   protected final PlayerSkin.Patch skinPatch;
/*     */   
/*     */   private static ResolvableProfile create(Either<GameProfile, Partial> value, PlayerSkin.Patch patch) {
/*  53 */     return (ResolvableProfile)value.map(full -> new Static(Either.left(full), patch), partial -> 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  59 */         (!partial.properties.isEmpty() || partial.id.isPresent() == partial.name.isPresent()) ? new Static(Either.right(partial), patch) : partial.name.<ResolvableProfile>map(()).orElseGet(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResolvableProfile createResolved(GameProfile gameProfile) {
/*  70 */     return new Static(Either.left(gameProfile), PlayerSkin.Patch.EMPTY);
/*     */   }
/*     */   
/*     */   public static ResolvableProfile createUnresolved(String name) {
/*  74 */     return new Dynamic(Either.left(name), PlayerSkin.Patch.EMPTY);
/*     */   }
/*     */   
/*     */   public static ResolvableProfile createUnresolved(UUID id) {
/*  78 */     return new Dynamic(Either.right(id), PlayerSkin.Patch.EMPTY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResolvableProfile(GameProfile partialProfile, PlayerSkin.Patch skinPatch) {
/*  87 */     this.partialProfile = partialProfile;
/*  88 */     this.skinPatch = skinPatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GameProfile partialProfile() {
/* 102 */     return this.partialProfile;
/*     */   }
/*     */   
/*     */   public PlayerSkin.Patch skinPatch() {
/* 106 */     return this.skinPatch;
/*     */   }
/*     */   
/*     */   private static GameProfile createPartialProfile(Optional<String> maybeName, Optional<UUID> maybeId, PropertyMap properties) {
/* 110 */     String name = maybeName.orElse("");
/*     */     
/* 112 */     UUID id = maybeId.orElseGet(() -> (UUID)maybeName.map(UUIDUtil::createOfflinePlayerUUID).orElse(Util.NIL_UUID));
/* 113 */     return new GameProfile(id, name, properties);
/*     */   } protected abstract Either<GameProfile, Partial> unpack();
/*     */   public abstract CompletableFuture<GameProfile> resolveProfile(ProfileResolver paramProfileResolver);
/*     */   public abstract Optional<String> name();
/*     */   protected static final class Partial extends Record { private final Optional<String> name; private final Optional<UUID> id; private final PropertyMap properties;
/* 118 */     protected Partial(Optional<String> name, Optional<UUID> id, PropertyMap properties) { this.name = name; this.id = id; this.properties = properties; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 118 */       //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial; } public Optional<String> name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial;
/* 118 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<UUID> id() { return this.id; } public PropertyMap properties() { return this.properties; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     public static final Partial EMPTY = new Partial(Optional.empty(), Optional.empty(), PropertyMap.EMPTY);
/*     */     
/*     */     private static final MapCodec<Partial> MAP_CODEC;
/*     */     
/*     */     static {
/* 128 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.PLAYER_NAME.optionalFieldOf("name").forGetter(Partial::name), (App)UUIDUtil.CODEC.optionalFieldOf("id").forGetter(Partial::id), (App)ExtraCodecs.PROPERTY_MAP.optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(Partial::properties)).apply((Applicative)i, Partial::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     public static final StreamCodec<ByteBuf, Partial> STREAM_CODEC = StreamCodec.composite(
/* 135 */         ByteBufCodecs.PLAYER_NAME.apply(ByteBufCodecs::optional), Partial::name, 
/* 136 */         UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional), Partial::id, ByteBufCodecs.GAME_PROFILE_PROPERTIES, Partial::properties, Partial::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private GameProfile createProfile() {
/* 142 */       return ResolvableProfile.createPartialProfile(this.name, this.id, this.properties);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Static
/*     */     extends ResolvableProfile
/*     */   {
/* 150 */     public static final Static EMPTY = new Static(Either.right(ResolvableProfile.Partial.EMPTY), PlayerSkin.Patch.EMPTY);
/*     */     
/*     */     private final Either<GameProfile, ResolvableProfile.Partial> contents;
/*     */     
/*     */     private Static(Either<GameProfile, ResolvableProfile.Partial> contents, PlayerSkin.Patch skinPatch) {
/* 155 */       super((GameProfile)contents.map(gameProfile -> gameProfile, ResolvableProfile.Partial::createProfile), skinPatch);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 160 */       this.contents = contents;
/*     */     }
/*     */ 
/*     */     
/*     */     public CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver) {
/* 165 */       return CompletableFuture.completedFuture(this.partialProfile);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Either<GameProfile, ResolvableProfile.Partial> unpack() {
/* 170 */       return this.contents;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<String> name() {
/* 175 */       return (Optional<String>)this.contents.map(gameProfile -> Optional.of(gameProfile.name()), partial -> partial.name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 183 */       if (this != o) { if (o instanceof Static) { Static that = (Static)o; if (this.contents.equals(that.contents) && this.skinPatch.equals(that.skinPatch)); }  return false; }
/*     */     
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 188 */       int result = 31 + this.contents.hashCode();
/* 189 */       result = 31 * result + this.skinPatch.hashCode();
/* 190 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Dynamic
/*     */     extends ResolvableProfile
/*     */   {
/* 204 */     private static final Component DYNAMIC_TOOLTIP = (Component)Component.translatable("component.profile.dynamic").withStyle(ChatFormatting.GRAY);
/*     */     private final Either<String, UUID> nameOrId;
/*     */     
/*     */     private Dynamic(Either<String, UUID> nameOrId, PlayerSkin.Patch skinPatch) {
/* 208 */       super(ResolvableProfile.createPartialProfile(nameOrId.left(), nameOrId.right(), PropertyMap.EMPTY), skinPatch);
/* 209 */       this.nameOrId = nameOrId;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<String> name() {
/* 215 */       return this.nameOrId.left();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 220 */       if (this != o) { if (o instanceof Dynamic) { Dynamic that = (Dynamic)o; if (this.nameOrId.equals(that.nameOrId) && this.skinPatch.equals(that.skinPatch)); }  return false; }
/*     */     
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 225 */       int result = 31 + this.nameOrId.hashCode();
/* 226 */       result = 31 * result + this.skinPatch.hashCode();
/* 227 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Either<GameProfile, ResolvableProfile.Partial> unpack() {
/* 232 */       return Either.right(new ResolvableProfile.Partial(this.nameOrId.left(), this.nameOrId.right(), PropertyMap.EMPTY));
/*     */     }
/*     */ 
/*     */     
/*     */     public CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver) {
/* 237 */       return CompletableFuture.supplyAsync(() -> (GameProfile)profileResolver.fetchByNameOrId(this.nameOrId).orElse(this.partialProfile), (Executor)Util.nonCriticalIoPool());
/*     */     }
/*     */ 
/*     */     
/*     */     public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 242 */       consumer.accept(DYNAMIC_TOOLTIP);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/ResolvableProfile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */