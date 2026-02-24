/*    */ package net.minecraft.client.renderer.texture.atlas.sources;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ 
/*    */ public final class SingleFile extends Record implements SpriteSource {
/*    */   private final Identifier resourceId;
/*    */   private final Optional<Identifier> spriteId;
/*    */   
/* 14 */   public SingleFile(Identifier resourceId, Optional<Identifier> spriteId) { this.resourceId = resourceId; this.spriteId = spriteId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/SingleFile;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SingleFile; } public Identifier resourceId() { return this.resourceId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/SingleFile;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SingleFile; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/SingleFile;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SingleFile;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Identifier> spriteId() { return this.spriteId; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger(); public static final com.mojang.serialization.MapCodec<SingleFile> MAP_CODEC;
/*    */   static {
/* 20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("resource").forGetter(SingleFile::resourceId), (App)Identifier.CODEC.optionalFieldOf("sprite").forGetter(SingleFile::spriteId)).apply((com.mojang.datafixers.kinds.Applicative)i, SingleFile::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SingleFile(Identifier resourceId) {
/* 26 */     this(resourceId, Optional.empty());
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(net.minecraft.server.packs.resources.ResourceManager resourceManager, SpriteSource.Output output) {
/* 31 */     Identifier fullResourceId = TEXTURE_ID_CONVERTER.idToFile(this.resourceId);
/* 32 */     Optional<Resource> resource = resourceManager.getResource(fullResourceId);
/* 33 */     if (resource.isPresent()) {
/* 34 */       output.add(this.spriteId.orElse(this.resourceId), resource.get());
/*    */     } else {
/* 36 */       LOGGER.warn("Missing sprite: {}", fullResourceId);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SingleFile> codec() {
/* 42 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/sources/SingleFile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */