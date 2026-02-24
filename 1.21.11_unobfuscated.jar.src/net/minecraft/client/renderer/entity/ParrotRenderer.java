/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.parrot.ParrotModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ParrotRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*    */ 
/*    */ public class ParrotRenderer extends MobRenderer<Parrot, ParrotRenderState, ParrotModel> {
/* 11 */   private static final Identifier RED_BLUE = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_red_blue.png");
/* 12 */   private static final Identifier BLUE = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_blue.png");
/* 13 */   private static final Identifier GREEN = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_green.png");
/* 14 */   private static final Identifier YELLOW_BLUE = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_yellow_blue.png");
/* 15 */   private static final Identifier GREY = Identifier.withDefaultNamespace("textures/entity/parrot/parrot_grey.png");
/*    */   
/*    */   public ParrotRenderer(EntityRendererProvider.Context context) {
/* 18 */     super(context, new ParrotModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.PARROT)), 0.3F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ParrotRenderState state) {
/* 23 */     return getVariantTexture(state.variant);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParrotRenderState createRenderState() {
/* 28 */     return new ParrotRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Parrot entity, ParrotRenderState state, float partialTicks) {
/* 33 */     super.extractRenderState(entity, state, partialTicks);
/* 34 */     state.variant = entity.getVariant();
/* 35 */     float flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
/* 36 */     float flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
/* 37 */     state.flapAngle = (Mth.sin(flap) + 1.0F) * flapSpeed;
/* 38 */     state.pose = ParrotModel.getPose(entity);
/*    */   }
/*    */   
/*    */   public static Identifier getVariantTexture(Parrot.Variant variant) {
/* 42 */     switch (variant) { default: throw new MatchException(null, null);case RED_BLUE: case BLUE: case GREEN: case YELLOW_BLUE: case GRAY: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 47 */       GREY;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ParrotRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */