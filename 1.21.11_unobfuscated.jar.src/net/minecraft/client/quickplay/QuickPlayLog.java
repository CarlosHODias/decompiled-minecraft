/*     */ package net.minecraft.client.quickplay;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class QuickPlayLog {
/*  25 */   private static final QuickPlayLog INACTIVE = new QuickPlayLog("")
/*     */     {
/*     */       public void log(Minecraft minecraft) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public void setWorldData(QuickPlayLog.Type type, String id, String name) {}
/*     */     };
/*     */ 
/*     */   
/*  35 */   private static final Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*  36 */   private static final com.google.gson.Gson GSON = new GsonBuilder().create();
/*     */   
/*     */   private final Path path;
/*     */   
/*     */   private QuickPlayWorld worldData;
/*     */   
/*     */   private QuickPlayLog(String quickPlayPath) {
/*  43 */     this.path = (Minecraft.getInstance()).gameDirectory.toPath().resolve(quickPlayPath);
/*     */   }
/*     */   
/*     */   public static QuickPlayLog of(String path) {
/*  47 */     if (path == null) {
/*  48 */       return INACTIVE;
/*     */     }
/*  50 */     return new QuickPlayLog(path);
/*     */   }
/*     */   
/*     */   public void setWorldData(Type type, String id, String name) {
/*  54 */     this.worldData = new QuickPlayWorld(type, id, name);
/*     */   }
/*     */   
/*     */   public void log(Minecraft minecraft) {
/*  58 */     if (minecraft.gameMode == null || this.worldData == null) {
/*  59 */       LOGGER.error("Failed to log session for quickplay. Missing world data or gamemode");
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     Util.ioPool().execute(() -> {
/*     */           try {
/*     */             Files.deleteIfExists(this.path);
/*  66 */           } catch (IOException e) {
/*     */             LOGGER.error("Failed to delete quickplay log file {}", this.path, e);
/*     */           } 
/*     */           QuickPlayEntry quickPlayEntry = new QuickPlayEntry(this.worldData, Instant.now(), minecraft.gameMode.getPlayerMode());
/*     */           Objects.requireNonNull(LOGGER);
/*     */           Codec.list(QuickPlayEntry.CODEC).encodeStart((DynamicOps)JsonOps.INSTANCE, List.of(quickPlayEntry)).resultOrPartial(Util.prefix("Quick Play: ", LOGGER::error)).ifPresent(());
/*     */         });
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
/*     */   public enum Type
/*     */     implements StringRepresentable
/*     */   {
/*  86 */     SINGLEPLAYER("singleplayer"),
/*  87 */     MULTIPLAYER("multiplayer"),
/*  88 */     REALMS("realms");
/*     */     
/*  90 */     private static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Type(String name) {
/*  95 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 100 */       return this.name;
/*     */     } }
/*     */   private static final class QuickPlayWorld extends Record { private final QuickPlayLog.Type type; private final String id; private final String name; public static final com.mojang.serialization.MapCodec<QuickPlayWorld> MAP_CODEC;
/*     */     
/* 104 */     private QuickPlayWorld(QuickPlayLog.Type type, String id, String name) { this.type = type; this.id = id; this.name = name; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 104 */       //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld; } public QuickPlayLog.Type type() { return this.type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayWorld;
/* 104 */       //   0	8	1	o	Ljava/lang/Object; } public String id() { return this.id; } public String name() { return this.name; } static {
/* 105 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)QuickPlayLog.Type.CODEC.fieldOf("type").forGetter(QuickPlayWorld::type), (App)ExtraCodecs.ESCAPED_STRING.fieldOf("id").forGetter(QuickPlayWorld::id), (App)Codec.STRING.fieldOf("name").forGetter(QuickPlayWorld::name)).apply((Applicative)i, QuickPlayWorld::new));
/*     */     } }
/*     */   private static final class QuickPlayEntry extends Record { private final QuickPlayLog.QuickPlayWorld quickPlayWorld;
/*     */     private final Instant lastPlayedTime;
/*     */     private final GameType gamemode;
/*     */     public static final Codec<QuickPlayEntry> CODEC;
/*     */     
/* 112 */     private QuickPlayEntry(QuickPlayLog.QuickPlayWorld quickPlayWorld, Instant lastPlayedTime, GameType gamemode) { this.quickPlayWorld = quickPlayWorld; this.lastPlayedTime = lastPlayedTime; this.gamemode = gamemode; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #112	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;
/* 112 */       //   0	8	1	o	Ljava/lang/Object; } public QuickPlayLog.QuickPlayWorld quickPlayWorld() { return this.quickPlayWorld; } public Instant lastPlayedTime() { return this.lastPlayedTime; } public GameType gamemode() { return this.gamemode; } static {
/* 113 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)QuickPlayLog.QuickPlayWorld.MAP_CODEC.forGetter(QuickPlayEntry::quickPlayWorld), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("lastPlayedTime").forGetter(QuickPlayEntry::lastPlayedTime), (App)GameType.CODEC.fieldOf("gamemode").forGetter(QuickPlayEntry::gamemode)).apply((Applicative)i, QuickPlayEntry::new));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/quickplay/QuickPlayLog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */