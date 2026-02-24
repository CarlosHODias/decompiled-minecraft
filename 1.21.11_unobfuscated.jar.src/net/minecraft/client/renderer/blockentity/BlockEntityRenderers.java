/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockEntityRenderers
/*    */ {
/* 13 */   private static final Map<BlockEntityType<?>, BlockEntityRendererProvider<?, ?>> PROVIDERS = Maps.newHashMap();
/*    */   
/*    */   static {
/* 16 */     register(BlockEntityType.SIGN, SignRenderer::new);
/* 17 */     register(BlockEntityType.HANGING_SIGN, HangingSignRenderer::new);
/* 18 */     register(BlockEntityType.MOB_SPAWNER, SpawnerRenderer::new);
/* 19 */     register(BlockEntityType.PISTON, context1 -> new PistonHeadRenderer());
/* 20 */     register(BlockEntityType.CHEST, ChestRenderer::new);
/* 21 */     register(BlockEntityType.ENDER_CHEST, ChestRenderer::new);
/* 22 */     register(BlockEntityType.TRAPPED_CHEST, ChestRenderer::new);
/* 23 */     register(BlockEntityType.ENCHANTING_TABLE, EnchantTableRenderer::new);
/* 24 */     register(BlockEntityType.LECTERN, LecternRenderer::new);
/* 25 */     register(BlockEntityType.END_PORTAL, context2 -> new TheEndPortalRenderer());
/* 26 */     register(BlockEntityType.END_GATEWAY, context1 -> new TheEndGatewayRenderer());
/* 27 */     register(BlockEntityType.BEACON, context -> new BeaconRenderer());
/* 28 */     register(BlockEntityType.SKULL, SkullBlockRenderer::new);
/* 29 */     register(BlockEntityType.BANNER, BannerRenderer::new);
/* 30 */     register(BlockEntityType.STRUCTURE_BLOCK, context -> new BlockEntityWithBoundingBoxRenderer());
/* 31 */     register(BlockEntityType.TEST_INSTANCE_BLOCK, context -> new TestInstanceRenderer());
/* 32 */     register(BlockEntityType.SHULKER_BOX, ShulkerBoxRenderer::new);
/* 33 */     register(BlockEntityType.BED, BedRenderer::new);
/* 34 */     register(BlockEntityType.CONDUIT, ConduitRenderer::new);
/* 35 */     register(BlockEntityType.BELL, BellRenderer::new);
/* 36 */     register(BlockEntityType.CAMPFIRE, CampfireRenderer::new);
/* 37 */     register(BlockEntityType.BRUSHABLE_BLOCK, BrushableBlockRenderer::new);
/* 38 */     register(BlockEntityType.DECORATED_POT, DecoratedPotRenderer::new);
/* 39 */     register(BlockEntityType.TRIAL_SPAWNER, TrialSpawnerRenderer::new);
/* 40 */     register(BlockEntityType.VAULT, VaultRenderer::new);
/* 41 */     register(BlockEntityType.COPPER_GOLEM_STATUE, CopperGolemStatueBlockRenderer::new);
/* 42 */     register(BlockEntityType.SHELF, ShelfRenderer::new);
/*    */   }
/*    */   
/*    */   private static <T extends net.minecraft.world.level.block.entity.BlockEntity, S extends net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState> void register(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T, S> renderer) {
/* 46 */     PROVIDERS.put(type, renderer);
/*    */   }
/*    */   
/*    */   public static Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>> createEntityRenderers(BlockEntityRendererProvider.Context context) {
/* 50 */     ImmutableMap.Builder<BlockEntityType<?>, BlockEntityRenderer<?, ?>> result = ImmutableMap.builder();
/* 51 */     PROVIDERS.forEach((type, provider) -> {
/*    */           try {
/*    */             result.put(type, provider.create(context));
/* 54 */           } catch (Exception e) {
/*    */             throw new IllegalStateException("Failed to create model for " + String.valueOf(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type)), e);
/*    */           } 
/*    */         });
/* 58 */     return (Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>>)result.build();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BlockEntityRenderers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */