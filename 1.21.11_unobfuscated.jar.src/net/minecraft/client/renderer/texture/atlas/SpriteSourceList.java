/*    */ package net.minecraft.client.renderer.texture.atlas;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.BufferedReader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.client.renderer.texture.SpriteContents;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SpriteSourceList {
/* 25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 27 */   private static final FileToIdConverter ATLAS_INFO_CONVERTER = new FileToIdConverter("atlases", ".json");
/*    */   
/*    */   private final List<SpriteSource> sources;
/*    */   
/*    */   private SpriteSourceList(List<SpriteSource> sources) {
/* 32 */     this.sources = sources;
/*    */   }
/*    */   
/*    */   public List<SpriteSource.Loader> list(ResourceManager resourceManager) {
/* 36 */     final Map<Identifier, SpriteSource.DiscardableLoader> sprites = new HashMap<>();
/* 37 */     SpriteSource.Output output = new SpriteSource.Output(this)
/*    */       {
/*    */         public void add(Identifier id, SpriteSource.DiscardableLoader sprite) {
/* 40 */           SpriteSource.DiscardableLoader previous = sprites.put(id, sprite);
/* 41 */           if (previous != null) {
/* 42 */             previous.discard();
/*    */           }
/*    */         }
/*    */ 
/*    */         
/*    */         public void removeAll(Predicate<Identifier> predicate) {
/* 48 */           Iterator<Map.Entry<Identifier, SpriteSource.DiscardableLoader>> it = sprites.entrySet().iterator();
/* 49 */           while (it.hasNext()) {
/* 50 */             Map.Entry<Identifier, SpriteSource.DiscardableLoader> entry = it.next();
/* 51 */             if (predicate.test(entry.getKey())) {
/* 52 */               ((SpriteSource.DiscardableLoader)entry.getValue()).discard();
/* 53 */               it.remove();
/*    */             } 
/*    */           } 
/*    */         }
/*    */       };
/*    */     
/* 59 */     this.sources.forEach(s -> s.run(resourceManager, output));
/*    */     
/* 61 */     ImmutableList.Builder<SpriteSource.Loader> result = ImmutableList.builder();
/* 62 */     result.add(loader -> MissingTextureAtlasSprite.create());
/* 63 */     result.addAll(sprites.values());
/* 64 */     return (List<SpriteSource.Loader>)result.build();
/*    */   }
/*    */   
/*    */   public static SpriteSourceList load(ResourceManager resourceManager, Identifier atlasId) {
/* 68 */     Identifier resourceId = ATLAS_INFO_CONVERTER.idToFile(atlasId);
/* 69 */     List<SpriteSource> loaders = new ArrayList<>();
/* 70 */     for (Resource entry : (Iterable<Resource>)resourceManager.getResourceStack(resourceId)) { 
/* 71 */       try { BufferedReader reader = entry.openAsReader(); 
/* 72 */         try { Dynamic<JsonElement> contents = new Dynamic((DynamicOps)JsonOps.INSTANCE, net.minecraft.util.StrictJsonParser.parse(reader));
/* 73 */           loaders.addAll((Collection<? extends SpriteSource>)SpriteSources.FILE_CODEC.parse(contents).getOrThrow());
/* 74 */           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 75 */       { LOGGER.error("Failed to parse atlas definition {} in pack {}", new Object[] { resourceId, entry.sourcePackId(), e }); }
/*    */        }
/*    */     
/* 78 */     return new SpriteSourceList(loaders);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/SpriteSourceList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */