/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.mojang.math.Quadrant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.texture.SpriteContents;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ModelDebugName;
/*     */ import net.minecraft.client.resources.model.ModelState;
/*     */ import net.minecraft.client.resources.model.QuadCollection;
/*     */ import net.minecraft.client.resources.model.UnbakedGeometry;
/*     */ import net.minecraft.client.resources.model.UnbakedModel;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ItemModelGenerator
/*     */   implements UnbakedModel
/*     */ {
/*  26 */   public static final Identifier GENERATED_ITEM_MODEL_ID = Identifier.withDefaultNamespace("builtin/generated");
/*     */   
/*  28 */   public static final List<String> LAYERS = List.of("layer0", "layer1", "layer2", "layer3", "layer4");
/*     */   
/*     */   private static final float MIN_Z = 7.5F;
/*     */   private static final float MAX_Z = 8.5F;
/*  32 */   private static final TextureSlots.Data TEXTURE_SLOTS = new TextureSlots.Data.Builder().addReference("particle", "layer0").build();
/*     */   
/*  34 */   private static final BlockElementFace.UVs SOUTH_FACE_UVS = new BlockElementFace.UVs(0.0F, 0.0F, 16.0F, 16.0F);
/*  35 */   private static final BlockElementFace.UVs NORTH_FACE_UVS = new BlockElementFace.UVs(16.0F, 0.0F, 0.0F, 16.0F);
/*     */   
/*     */   private static final float UV_SHRINK = 0.1F;
/*     */   
/*     */   public TextureSlots.Data textureSlots() {
/*  40 */     return TEXTURE_SLOTS;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnbakedGeometry geometry() {
/*  45 */     return ItemModelGenerator::bake;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnbakedModel.GuiLight guiLight() {
/*  50 */     return UnbakedModel.GuiLight.FRONT;
/*     */   }
/*     */   
/*     */   private static QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName name) {
/*  54 */     List<BlockElement> elements = new ArrayList<>();
/*  55 */     for (int layerIndex = 0; layerIndex < LAYERS.size(); layerIndex++) {
/*  56 */       String textureReference = LAYERS.get(layerIndex);
/*  57 */       Material material = textureSlots.getMaterial(textureReference);
/*  58 */       if (material == null) {
/*     */         break;
/*     */       }
/*     */       
/*  62 */       SpriteContents sprite = modelBaker.sprites().get(material, name).contents();
/*  63 */       elements.addAll(processFrames(layerIndex, textureReference, sprite));
/*     */     } 
/*     */     
/*  66 */     return SimpleUnbakedGeometry.bake(elements, textureSlots, modelBaker, modelState, name);
/*     */   }
/*     */   
/*     */   private static List<BlockElement> processFrames(int tintIndex, String textureName, SpriteContents sprite) {
/*  70 */     Map<Direction, BlockElementFace> frontAndBackFaces = Map.of(Direction.SOUTH, new BlockElementFace(null, tintIndex, textureName, SOUTH_FACE_UVS, Quadrant.R0), Direction.NORTH, new BlockElementFace(null, tintIndex, textureName, NORTH_FACE_UVS, Quadrant.R0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     List<BlockElement> elements = new ArrayList<>();
/*  76 */     elements.add(new BlockElement((Vector3fc)new Vector3f(0.0F, 0.0F, 7.5F), (Vector3fc)new Vector3f(16.0F, 16.0F, 8.5F), frontAndBackFaces));
/*     */     
/*  78 */     elements.addAll(createSideElements(sprite, textureName, tintIndex));
/*     */     
/*  80 */     return elements;
/*     */   }
/*     */   
/*     */   private static List<BlockElement> createSideElements(SpriteContents sprite, String textureName, int tintIndex) {
/*  84 */     float xScale = 16.0F / sprite.width();
/*  85 */     float yScale = 16.0F / sprite.height();
/*  86 */     List<BlockElement> result = new ArrayList<>();
/*     */     
/*  88 */     for (SideFace sideFace : getSideFaces(sprite)) {
/*  89 */       float v0, v1; float x = sideFace.x();
/*  90 */       float y = sideFace.y();
/*     */       
/*  92 */       SideDirection sideDirection = sideFace.facing();
/*  93 */       float u0 = x + 0.1F;
/*  94 */       float u1 = x + 1.0F - 0.1F;
/*     */ 
/*     */       
/*  97 */       if (sideDirection.isHorizontal()) {
/*  98 */         v0 = y + 0.1F;
/*  99 */         v1 = y + 1.0F - 0.1F;
/*     */       } else {
/* 101 */         v0 = y + 1.0F - 0.1F;
/* 102 */         v1 = y + 0.1F;
/*     */       } 
/*     */       
/* 105 */       float startX = x;
/* 106 */       float startY = y;
/* 107 */       float endX = x;
/* 108 */       float endY = y;
/* 109 */       switch (sideDirection.ordinal()) { case 0:
/* 110 */           endX++; break;
/*     */         case 1:
/* 112 */           endX++;
/* 113 */           startY++;
/* 114 */           endY++; break;
/*     */         case 2:
/* 116 */           endY++; break;
/*     */         case 3:
/* 118 */           startX++;
/* 119 */           endX++;
/* 120 */           endY++;
/*     */           break; }
/*     */ 
/*     */       
/* 124 */       startX *= xScale;
/* 125 */       endX *= xScale;
/* 126 */       startY *= yScale;
/* 127 */       endY *= yScale;
/*     */       
/* 129 */       startY = 16.0F - startY;
/* 130 */       endY = 16.0F - endY;
/*     */       
/* 132 */       Map<Direction, BlockElementFace> faces = Map.of(
/* 133 */           sideDirection.getDirection(), new BlockElementFace(null, tintIndex, textureName, new BlockElementFace.UVs(u0 * xScale, v0 * xScale, u1 * yScale, v1 * yScale), Quadrant.R0));
/*     */ 
/*     */       
/* 136 */       switch (sideDirection.ordinal()) {
/*     */         case 0:
/* 138 */           result.add(new BlockElement((Vector3fc)new Vector3f(startX, startY, 7.5F), (Vector3fc)new Vector3f(endX, startY, 8.5F), faces));
/*     */         case 1:
/* 140 */           result.add(new BlockElement((Vector3fc)new Vector3f(startX, endY, 7.5F), (Vector3fc)new Vector3f(endX, endY, 8.5F), faces));
/*     */         case 2:
/* 142 */           result.add(new BlockElement((Vector3fc)new Vector3f(startX, startY, 7.5F), (Vector3fc)new Vector3f(startX, endY, 8.5F), faces));
/*     */         case 3:
/* 144 */           result.add(new BlockElement((Vector3fc)new Vector3f(endX, startY, 7.5F), (Vector3fc)new Vector3f(endX, endY, 8.5F), faces));
/*     */       } 
/*     */     
/*     */     } 
/* 148 */     return result;
/*     */   }
/*     */   
/*     */   private static Collection<SideFace> getSideFaces(SpriteContents sprite) {
/* 152 */     int width = sprite.width();
/* 153 */     int height = sprite.height();
/*     */     
/* 155 */     Set<SideFace> sideFaces = new HashSet<>();
/* 156 */     sprite.getUniqueFrames().forEach(frame -> {
/*     */           for (int y = 0; y < height; y++) {
/*     */             for (int x = 0; x < width; x++) {
/*     */               boolean thisOpaque = !isTransparent(sprite, frame, x, y, width, height);
/*     */               
/*     */               if (thisOpaque) {
/*     */                 checkTransition(SideDirection.UP, sideFaces, sprite, frame, x, y, width, height);
/*     */                 checkTransition(SideDirection.DOWN, sideFaces, sprite, frame, x, y, width, height);
/*     */                 checkTransition(SideDirection.LEFT, sideFaces, sprite, frame, x, y, width, height);
/*     */                 checkTransition(SideDirection.RIGHT, sideFaces, sprite, frame, x, y, width, height);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/* 170 */     return sideFaces;
/*     */   }
/*     */   
/*     */   private static void checkTransition(SideDirection facing, Set<SideFace> sideFaces, SpriteContents sprite, int frame, int x, int y, int width, int height) {
/* 174 */     if (isTransparent(sprite, frame, x - facing.direction.getStepX(), y - facing.direction.getStepY(), width, height)) {
/* 175 */       sideFaces.add(new SideFace(facing, x, y));
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isTransparent(SpriteContents sprite, int frame, int x, int y, int width, int height) {
/* 180 */     if (x < 0 || y < 0 || x >= width || y >= height) {
/* 181 */       return true;
/*     */     }
/* 183 */     return sprite.isTransparent(frame, x, y);
/*     */   }
/*     */   
/*     */   private enum SideDirection {
/* 187 */     UP(Direction.UP),
/* 188 */     DOWN(Direction.DOWN),
/* 189 */     LEFT(Direction.EAST),
/* 190 */     RIGHT(Direction.WEST);
/*     */     
/*     */     private final Direction direction;
/*     */     
/*     */     SideDirection(Direction direction) {
/* 195 */       this.direction = direction;
/*     */     }
/*     */     
/*     */     public Direction getDirection() {
/* 199 */       return this.direction;
/*     */     }
/*     */     
/*     */     private boolean isHorizontal() {
/* 203 */       return (this == DOWN || this == UP);
/*     */     } }
/*     */   private static final class SideFace extends Record { private final ItemModelGenerator.SideDirection facing; private final int x; private final int y;
/*     */     
/* 207 */     private SideFace(ItemModelGenerator.SideDirection facing, int x, int y) { this.facing = facing; this.x = x; this.y = y; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/ItemModelGenerator$SideFace;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 207 */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ItemModelGenerator$SideFace; } public ItemModelGenerator.SideDirection facing() { return this.facing; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/ItemModelGenerator$SideFace;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/ItemModelGenerator$SideFace; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/ItemModelGenerator$SideFace;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/ItemModelGenerator$SideFace;
/* 207 */       //   0	8	1	o	Ljava/lang/Object; } public int x() { return this.x; } public int y() { return this.y; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/ItemModelGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */