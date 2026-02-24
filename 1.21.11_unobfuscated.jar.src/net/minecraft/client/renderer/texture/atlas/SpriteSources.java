/*    */ package net.minecraft.client.renderer.texture.atlas;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
/*    */ import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
/*    */ import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
/*    */ import net.minecraft.client.renderer.texture.atlas.sources.SourceFilter;
/*    */ import net.minecraft.client.renderer.texture.atlas.sources.Unstitcher;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SpriteSources {
/*    */   public static final Codec<SpriteSource> CODEC;
/* 16 */   private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpriteSource>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   static {
/* 18 */     CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(SpriteSource::codec, c -> c);
/*    */   }
/*    */   public static void bootstrap() {
/* 21 */     ID_MAPPER.put(Identifier.withDefaultNamespace("single"), SingleFile.MAP_CODEC);
/* 22 */     ID_MAPPER.put(Identifier.withDefaultNamespace("directory"), DirectoryLister.MAP_CODEC);
/* 23 */     ID_MAPPER.put(Identifier.withDefaultNamespace("filter"), SourceFilter.MAP_CODEC);
/* 24 */     ID_MAPPER.put(Identifier.withDefaultNamespace("unstitch"), Unstitcher.MAP_CODEC);
/* 25 */     ID_MAPPER.put(Identifier.withDefaultNamespace("paletted_permutations"), PalettedPermutations.MAP_CODEC);
/*    */   }
/*    */ 
/*    */   
/* 29 */   public static final Codec<List<SpriteSource>> FILE_CODEC = CODEC.listOf().fieldOf("sources").codec();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/SpriteSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */