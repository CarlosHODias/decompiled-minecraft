/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class BeaconRenderState extends BlockEntityRenderState {
/*    */   public float animationTime;
/*    */   public float beamRadiusScale;
/*  9 */   public List<Section> sections = new ArrayList<>();
/*    */   public static final class Section extends Record { private final int color; private final int height;
/* 11 */     public Section(int color, int height) { this.color = color; this.height = height; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState$Section;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #11	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 11 */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState$Section; } public int color() { return this.color; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState$Section;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #11	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState$Section; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState$Section;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #11	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState$Section;
/* 11 */       //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/BeaconRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */