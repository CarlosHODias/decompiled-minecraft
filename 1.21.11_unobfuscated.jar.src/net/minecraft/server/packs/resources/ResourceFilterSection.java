/*    */ package net.minecraft.server.packs.resources;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.IdentifierPattern;
/*    */ 
/*    */ public class ResourceFilterSection {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.list(IdentifierPattern.CODEC).fieldOf("block").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, ResourceFilterSection::new));
/*    */   }
/*    */   
/*    */   private static final Codec<ResourceFilterSection> CODEC;
/* 15 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<ResourceFilterSection> TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("filter", CODEC);
/*    */   
/*    */   private final List<IdentifierPattern> blockList;
/*    */   
/*    */   public ResourceFilterSection(List<IdentifierPattern> blockList) {
/* 20 */     this.blockList = List.copyOf(blockList);
/*    */   }
/*    */   
/*    */   public boolean isNamespaceFiltered(String namespace) {
/* 24 */     return this.blockList.stream().anyMatch(p -> p.namespacePredicate().test(namespace));
/*    */   }
/*    */   
/*    */   public boolean isPathFiltered(String path) {
/* 28 */     return this.blockList.stream().anyMatch(p -> p.pathPredicate().test(path));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/ResourceFilterSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */