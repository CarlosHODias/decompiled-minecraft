/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.mojang.math.Quadrant;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.block.model.BlockElement;
/*    */ import net.minecraft.client.renderer.block.model.BlockElementFace;
/*    */ import net.minecraft.client.renderer.block.model.BlockModel;
/*    */ import net.minecraft.client.renderer.block.model.ItemTransforms;
/*    */ import net.minecraft.client.renderer.block.model.SimpleUnbakedGeometry;
/*    */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class MissingBlockModel
/*    */ {
/*    */   private static final String TEXTURE_SLOT = "missingno";
/* 23 */   public static final Identifier LOCATION = Identifier.withDefaultNamespace("builtin/missing");
/*    */   
/*    */   public static UnbakedModel missingModel() {
/* 26 */     BlockElementFace.UVs fullFaceUv = new BlockElementFace.UVs(0.0F, 0.0F, 16.0F, 16.0F);
/*    */     
/* 28 */     Map<Direction, BlockElementFace> faces = Util.makeEnumMap(Direction.class, direction -> new BlockElementFace(direction, -1, "missingno", fullFaceUv, Quadrant.R0));
/*    */ 
/*    */ 
/*    */     
/* 32 */     BlockElement cube = new BlockElement((Vector3fc)new Vector3f(0.0F, 0.0F, 0.0F), (Vector3fc)new Vector3f(16.0F, 16.0F, 16.0F), faces);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 38 */     return (UnbakedModel)new BlockModel((UnbakedGeometry)new SimpleUnbakedGeometry(
/* 39 */           List.of(cube)), null, null, ItemTransforms.NO_TRANSFORMS, new TextureSlots.Data.Builder()
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 44 */         .addReference("particle", "missingno")
/* 45 */         .addTexture("missingno", new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()))
/* 46 */         .build(), null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/MissingBlockModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */