/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.AnimationState;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemState;
/*    */ import net.minecraft.world.level.block.WeatheringCopper;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CopperGolemRenderState
/*    */   extends ArmedEntityRenderState {
/* 11 */   public WeatheringCopper.WeatherState weathering = WeatheringCopper.WeatherState.UNAFFECTED;
/* 12 */   public CopperGolemState copperGolemState = CopperGolemState.IDLE;
/* 13 */   public final AnimationState idleAnimationState = new AnimationState();
/* 14 */   public final AnimationState interactionGetItem = new AnimationState();
/* 15 */   public final AnimationState interactionGetNoItem = new AnimationState();
/* 16 */   public final AnimationState interactionDropItem = new AnimationState();
/* 17 */   public final AnimationState interactionDropNoItem = new AnimationState();
/* 18 */   public Optional<BlockState> blockOnAntenna = Optional.empty();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/CopperGolemRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */