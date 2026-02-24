/*     */ package net.minecraft.client.gui.components.debug;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DebugScreenEntryList {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int DEFAULT_DEBUG_PROFILE_VERSION = 4649;
/*     */   
/*     */   private Map<Identifier, DebugScreenEntryStatus> allStatuses;
/*  34 */   private final List<Identifier> currentlyEnabled = new ArrayList<>();
/*     */   private boolean isOverlayVisible = false;
/*     */   private DebugScreenProfile profile;
/*     */   private final File debugProfileFile;
/*     */   private long currentlyEnabledVersion;
/*     */   private final Codec<SerializedOptions> codec;
/*     */   
/*     */   public DebugScreenEntryList(File workingDirectory) {
/*  42 */     this.debugProfileFile = new File(workingDirectory, "debug-profile.json");
/*  43 */     this.codec = DataFixTypes.DEBUG_PROFILE.wrapCodec(SerializedOptions.CODEC, Minecraft.getInstance().getFixerUpper(), 4649);
/*  44 */     load();
/*     */   }
/*     */   
/*     */   public void load() {
/*     */     try {
/*  49 */       if (!this.debugProfileFile.isFile()) {
/*  50 */         loadDefaultProfile();
/*  51 */         rebuildCurrentList();
/*     */         return;
/*     */       } 
/*  54 */       Dynamic<JsonElement> data = new Dynamic((DynamicOps)JsonOps.INSTANCE, StrictJsonParser.parse(FileUtils.readFileToString(this.debugProfileFile, StandardCharsets.UTF_8)));
/*  55 */       SerializedOptions serializedOptions = (SerializedOptions)this.codec.parse(data).getOrThrow(error -> new IOException("Could not parse debug profile JSON: " + error));
/*  56 */       if (serializedOptions.profile().isPresent()) {
/*  57 */         loadProfile(serializedOptions.profile().get());
/*     */       } else {
/*  59 */         this.allStatuses = new HashMap<>();
/*  60 */         if (serializedOptions.custom().isPresent()) {
/*  61 */           this.allStatuses.putAll(serializedOptions.custom().get());
/*     */         }
/*  63 */         this.profile = null;
/*     */       } 
/*  65 */     } catch (IOException|com.google.gson.JsonSyntaxException e) {
/*  66 */       LOGGER.error("Couldn't read debug profile file {}, resetting to default", this.debugProfileFile, e);
/*  67 */       loadDefaultProfile();
/*  68 */       save();
/*     */     } 
/*  70 */     rebuildCurrentList();
/*     */   }
/*     */   
/*     */   public void loadProfile(DebugScreenProfile profile) {
/*  74 */     this.profile = profile;
/*  75 */     Map<Identifier, DebugScreenEntryStatus> statuses = DebugScreenEntries.PROFILES.get(profile);
/*  76 */     this.allStatuses = new HashMap<>(statuses);
/*  77 */     rebuildCurrentList();
/*     */   }
/*     */   
/*     */   private void loadDefaultProfile() {
/*  81 */     this.profile = DebugScreenProfile.DEFAULT;
/*  82 */     this.allStatuses = new HashMap<>(DebugScreenEntries.PROFILES.get(DebugScreenProfile.DEFAULT));
/*     */   }
/*     */   
/*     */   public DebugScreenEntryStatus getStatus(Identifier location) {
/*  86 */     DebugScreenEntryStatus status = this.allStatuses.get(location);
/*  87 */     if (status == null) {
/*  88 */       return DebugScreenEntryStatus.NEVER;
/*     */     }
/*  90 */     return status;
/*     */   }
/*     */   
/*     */   public boolean isCurrentlyEnabled(Identifier location) {
/*  94 */     return this.currentlyEnabled.contains(location);
/*     */   }
/*     */   
/*     */   public void setStatus(Identifier location, DebugScreenEntryStatus status) {
/*  98 */     this.profile = null;
/*  99 */     this.allStatuses.put(location, status);
/* 100 */     rebuildCurrentList();
/* 101 */     save();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean toggleStatus(Identifier location) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield allStatuses : Ljava/util/Map;
/*     */     //   4: aload_1
/*     */     //   5: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   10: checkcast net/minecraft/client/gui/components/debug/DebugScreenEntryStatus
/*     */     //   13: astore_2
/*     */     //   14: aload_2
/*     */     //   15: astore_3
/*     */     //   16: iconst_0
/*     */     //   17: istore #4
/*     */     //   19: aload_3
/*     */     //   20: iload #4
/*     */     //   22: <illegal opcode> enumSwitch : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;I)I
/*     */     //   27: tableswitch default -> 121, -1 -> 121, 0 -> 56, 1 -> 66, 2 -> 93
/*     */     //   56: aload_0
/*     */     //   57: aload_1
/*     */     //   58: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.NEVER : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   61: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   64: iconst_0
/*     */     //   65: ireturn
/*     */     //   66: aload_0
/*     */     //   67: getfield isOverlayVisible : Z
/*     */     //   70: ifeq -> 83
/*     */     //   73: aload_0
/*     */     //   74: aload_1
/*     */     //   75: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.NEVER : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   78: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   81: iconst_0
/*     */     //   82: ireturn
/*     */     //   83: aload_0
/*     */     //   84: aload_1
/*     */     //   85: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.ALWAYS_ON : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   88: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   91: iconst_1
/*     */     //   92: ireturn
/*     */     //   93: aload_0
/*     */     //   94: getfield isOverlayVisible : Z
/*     */     //   97: ifeq -> 111
/*     */     //   100: aload_0
/*     */     //   101: aload_1
/*     */     //   102: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.IN_OVERLAY : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   105: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   108: goto -> 119
/*     */     //   111: aload_0
/*     */     //   112: aload_1
/*     */     //   113: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.ALWAYS_ON : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   116: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   119: iconst_1
/*     */     //   120: ireturn
/*     */     //   121: aload_0
/*     */     //   122: aload_1
/*     */     //   123: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.ALWAYS_ON : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   126: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   129: iconst_1
/*     */     //   130: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     //   #106	-> 14
/*     */     //   #108	-> 56
/*     */     //   #109	-> 64
/*     */     //   #112	-> 66
/*     */     //   #113	-> 73
/*     */     //   #114	-> 81
/*     */     //   #116	-> 83
/*     */     //   #117	-> 91
/*     */     //   #121	-> 93
/*     */     //   #122	-> 100
/*     */     //   #124	-> 111
/*     */     //   #126	-> 119
/*     */     //   #129	-> 121
/*     */     //   #130	-> 129
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	131	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList;
/*     */     //   0	131	1	location	Lnet/minecraft/resources/Identifier;
/*     */     //   14	117	2	status	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Identifier> getCurrentlyEnabled() {
/* 136 */     return this.currentlyEnabled;
/*     */   }
/*     */   
/*     */   public void toggleDebugOverlay() {
/* 140 */     setOverlayVisible(!this.isOverlayVisible);
/*     */   }
/*     */   
/*     */   public void setOverlayVisible(boolean visible) {
/* 144 */     if (this.isOverlayVisible != visible) {
/* 145 */       this.isOverlayVisible = visible;
/* 146 */       rebuildCurrentList();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOverlayVisible() {
/* 151 */     return this.isOverlayVisible;
/*     */   }
/*     */   
/*     */   public void rebuildCurrentList() {
/* 155 */     this.currentlyEnabled.clear();
/*     */     
/* 157 */     boolean isReducedDebugInfo = Minecraft.getInstance().showOnlyReducedInfo();
/* 158 */     for (Map.Entry<Identifier, DebugScreenEntryStatus> entry : this.allStatuses.entrySet()) {
/* 159 */       if (entry.getValue() == DebugScreenEntryStatus.ALWAYS_ON || (this.isOverlayVisible && entry.getValue() == DebugScreenEntryStatus.IN_OVERLAY)) {
/* 160 */         DebugScreenEntry debug = DebugScreenEntries.getEntry(entry.getKey());
/* 161 */         if (debug != null && debug.isAllowed(isReducedDebugInfo)) {
/* 162 */           this.currentlyEnabled.add(entry.getKey());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     this.currentlyEnabled.sort(Identifier::compareTo);
/* 168 */     this.currentlyEnabledVersion++;
/*     */   }
/*     */   
/*     */   public long getCurrentlyEnabledVersion() {
/* 172 */     return this.currentlyEnabledVersion;
/*     */   }
/*     */   
/*     */   public boolean isUsingProfile(DebugScreenProfile profile) {
/* 176 */     return (this.profile == profile);
/*     */   }
/*     */   
/*     */   public void save() {
/* 180 */     SerializedOptions serializedOptions = new SerializedOptions(Optional.ofNullable(this.profile), (this.profile == null) ? Optional.<Map<Identifier, DebugScreenEntryStatus>>of(this.allStatuses) : Optional.<Map<Identifier, DebugScreenEntryStatus>>empty());
/*     */     try {
/* 182 */       FileUtils.writeStringToFile(this.debugProfileFile, ((JsonElement)this.codec.encodeStart((DynamicOps)JsonOps.INSTANCE, serializedOptions).getOrThrow()).toString(), StandardCharsets.UTF_8);
/* 183 */     } catch (IOException e) {
/* 184 */       LOGGER.error("Failed to save debug profile file {}", this.debugProfileFile, e);
/*     */     } 
/*     */   }
/*     */   static final class SerializedOptions extends Record { private final Optional<DebugScreenProfile> profile; private final Optional<Map<Identifier, DebugScreenEntryStatus>> custom;
/* 188 */     SerializedOptions(Optional<DebugScreenProfile> profile, Optional<Map<Identifier, DebugScreenEntryStatus>> custom) { this.profile = profile; this.custom = custom; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #188	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 188 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions; } public Optional<DebugScreenProfile> profile() { return this.profile; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #188	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #188	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;
/* 188 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Map<Identifier, DebugScreenEntryStatus>> custom() { return this.custom; }
/* 189 */      private static final Codec<Map<Identifier, DebugScreenEntryStatus>> CUSTOM_ENTRIES_CODEC = (Codec<Map<Identifier, DebugScreenEntryStatus>>)Codec.unboundedMap(Identifier.CODEC, (Codec)DebugScreenEntryStatus.CODEC); public static final Codec<SerializedOptions> CODEC;
/*     */     static {
/* 191 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)DebugScreenProfile.CODEC.optionalFieldOf("profile").forGetter(SerializedOptions::profile), (App)CUSTOM_ENTRIES_CODEC.optionalFieldOf("custom").forGetter(SerializedOptions::custom)).apply((Applicative)i, SerializedOptions::new));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugScreenEntryList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */