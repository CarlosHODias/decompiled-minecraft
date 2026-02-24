/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.google.common.base.Suppliers;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class MissingItemModel
/*    */   implements ItemModel
/*    */ {
/*    */   private final List<BakedQuad> quads;
/*    */   private final Supplier<Vector3fc[]> extents;
/*    */   private final ModelRenderProperties properties;
/*    */   
/*    */   public MissingItemModel(List<BakedQuad> quads, ModelRenderProperties properties) {
/* 22 */     this.quads = quads;
/* 23 */     this.properties = properties;
/* 24 */     this.extents = (Supplier<Vector3fc[]>)Suppliers.memoize(() -> BlockModelWrapper.computeExtents(this.quads));
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/* 29 */     output.appendModelIdentityElement(this);
/* 30 */     ItemStackRenderState.LayerRenderState layer = output.newLayer();
/* 31 */     layer.setRenderType(Sheets.cutoutBlockSheet());
/* 32 */     this.properties.applyToLayer(layer, displayContext);
/* 33 */     layer.setExtents(this.extents);
/* 34 */     layer.prepareQuadList().addAll(this.quads);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/MissingItemModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */