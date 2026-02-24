/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CompassAngle implements RangeSelectItemModelProperty {
/*    */   public static final MapCodec<CompassAngle> MAP_CODEC;
/*    */   
/*    */   static {
/* 10 */     MAP_CODEC = CompassAngleState.MAP_CODEC.xmap(CompassAngle::new, c -> c.state);
/*    */   }
/*    */   private final CompassAngleState state;
/*    */   
/*    */   public CompassAngle(boolean wobble, CompassAngleState.CompassTarget compassTarget) {
/* 15 */     this(new CompassAngleState(wobble, compassTarget));
/*    */   }
/*    */   
/*    */   private CompassAngle(CompassAngleState state) {
/* 19 */     this.state = state;
/*    */   }
/*    */ 
/*    */   
/*    */   public float get(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, ItemOwner owner, int seed) {
/* 24 */     return this.state.get(itemStack, level, owner, seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<CompassAngle> type() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/CompassAngle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */