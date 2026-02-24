/*    */ package net.minecraft.client.renderer.entity;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.ZombieModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ 
/*    */ public class ZombieRenderer extends AbstractZombieRenderer<Zombie, ZombieRenderState, ZombieModel<ZombieRenderState>> {
/*    */   public ZombieRenderer(EntityRendererProvider.Context context) {
/* 12 */     this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, ModelLayers.ZOMBIE_ARMOR, ModelLayers.ZOMBIE_BABY_ARMOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public ZombieRenderState createRenderState() {
/* 17 */     return new ZombieRenderState();
/*    */   }
/*    */   
/*    */   public ZombieRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
/* 21 */     super(context, new ZombieModel(
/* 22 */           context.bakeLayer(body)), new ZombieModel(
/* 23 */           context.bakeLayer(babyBody)), 
/* 24 */         ArmorModelSet.bake(armorSet, context.getModelSet(), ZombieModel::new), 
/* 25 */         ArmorModelSet.bake(babyArmorSet, context.getModelSet(), ZombieModel::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ZombieRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */