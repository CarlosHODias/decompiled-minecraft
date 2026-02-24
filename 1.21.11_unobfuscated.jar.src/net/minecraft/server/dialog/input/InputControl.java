/*   */ package net.minecraft.server.dialog.input;
/*   */ 
/*   */ import com.mojang.serialization.MapCodec;
/*   */ import net.minecraft.core.registries.BuiltInRegistries;
/*   */ 
/*   */ public interface InputControl {
/*   */   static {
/* 8 */     MAP_CODEC = BuiltInRegistries.INPUT_CONTROL_TYPE.byNameCodec().dispatchMap(InputControl::mapCodec, c -> c);
/*   */   }
/*   */   
/*   */   public static final MapCodec<InputControl> MAP_CODEC;
/*   */   
/*   */   MapCodec<? extends InputControl> mapCodec();
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/input/InputControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */