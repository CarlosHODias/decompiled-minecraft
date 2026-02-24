/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ public final class NameAndId extends Record {
/*    */   private final java.util.UUID id;
/*    */   private final String name;
/*    */   public static final com.mojang.serialization.Codec<NameAndId> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/players/NameAndId;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/players/NameAndId;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/players/NameAndId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/players/NameAndId;
/*    */   }
/*    */   
/* 16 */   public NameAndId(java.util.UUID id, String name) { this.id = id; this.name = name; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/players/NameAndId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/players/NameAndId;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.UUID id() { return this.id; } public String name() { return this.name; }
/*    */ 
/*    */   
/*    */   static {
/* 20 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.core.UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(NameAndId::id), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.fieldOf("name").forGetter(NameAndId::name)).apply((com.mojang.datafixers.kinds.Applicative)i, NameAndId::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NameAndId(com.mojang.authlib.GameProfile profile) {
/* 26 */     this(profile.id(), profile.name());
/*    */   }
/*    */   
/*    */   public NameAndId(com.mojang.authlib.yggdrasil.response.NameAndId profile) {
/* 30 */     this(profile.id(), profile.name());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NameAndId fromJson(com.google.gson.JsonObject object) {
/*    */     java.util.UUID uuid;
/* 38 */     if (!object.has("uuid") || !object.has("name")) {
/* 39 */       return null;
/*    */     }
/* 41 */     String uuidString = object.get("uuid").getAsString();
/*    */     
/*    */     try {
/* 44 */       uuid = java.util.UUID.fromString(uuidString);
/* 45 */     } catch (Throwable ignored) {
/* 46 */       return null;
/*    */     } 
/* 48 */     return new NameAndId(uuid, object.get("name").getAsString());
/*    */   }
/*    */   
/*    */   public void appendTo(com.google.gson.JsonObject output) {
/* 52 */     output.addProperty("uuid", id().toString());
/* 53 */     output.addProperty("name", name());
/*    */   }
/*    */   
/*    */   public static NameAndId createOffline(String name) {
/* 57 */     java.util.UUID id = net.minecraft.core.UUIDUtil.createOfflinePlayerUUID(name);
/* 58 */     return new NameAndId(id, name);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/players/NameAndId.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */