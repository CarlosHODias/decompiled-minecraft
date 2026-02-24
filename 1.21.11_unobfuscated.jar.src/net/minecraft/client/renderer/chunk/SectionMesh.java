/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public interface SectionMesh
/*    */   extends AutoCloseable
/*    */ {
/*    */   default boolean isDifferentPointOfView(TranslucencyPointOfView pointOfView) {
/* 12 */     return false;
/*    */   }
/*    */   
/*    */   default boolean hasRenderableLayers() {
/* 16 */     return false;
/*    */   }
/*    */   
/*    */   default boolean hasTranslucentGeometry() {
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   default boolean isEmpty(ChunkSectionLayer layer) {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   default List<BlockEntity> getRenderableBlockEntities() {
/* 28 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   boolean facesCanSeeEachother(Direction paramDirection1, Direction paramDirection2);
/*    */   
/*    */   default SectionBuffers getBuffers(ChunkSectionLayer layer) {
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   default void close() {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/SectionMesh.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */