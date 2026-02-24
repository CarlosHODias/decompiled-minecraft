/*    */ package net.minecraft.client.gui.narration;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface NarrationElementOutput {
/*    */   default void add(NarratedElementType type, Component contents) {
/*  8 */     add(type, NarrationThunk.from(contents.getString()));
/*    */   }
/*    */   
/*    */   default void add(NarratedElementType type, String contents) {
/* 12 */     add(type, NarrationThunk.from(contents));
/*    */   }
/*    */   
/*    */   void add(NarratedElementType type, Component... contents) {
/* 16 */     add(type, NarrationThunk.from((List<Component>)ImmutableList.copyOf((Object[])contents)));
/*    */   }
/*    */   
/*    */   void add(NarratedElementType paramNarratedElementType, NarrationThunk<?> paramNarrationThunk);
/*    */   
/*    */   NarrationElementOutput nest();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/narration/NarrationElementOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */