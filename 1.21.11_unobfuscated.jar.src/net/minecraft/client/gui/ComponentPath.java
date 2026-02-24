/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ 
/*    */ public interface ComponentPath
/*    */ {
/*    */   static ComponentPath leaf(GuiEventListener component) {
/*  9 */     return new Leaf(component);
/*    */   }
/*    */   
/*    */   static ComponentPath path(ContainerEventHandler container, ComponentPath childPath) {
/* 13 */     if (childPath == null) {
/* 14 */       return null;
/*    */     }
/* 16 */     return new Path(container, childPath);
/*    */   }
/*    */   
/*    */   static ComponentPath path(GuiEventListener target, ContainerEventHandler... containerPath) {
/* 20 */     ComponentPath path = leaf(target);
/* 21 */     for (ContainerEventHandler container : containerPath) {
/* 22 */       path = path(container, path);
/*    */     }
/* 24 */     return path;
/*    */   }
/*    */   GuiEventListener component();
/*    */   void applyFocus(boolean paramBoolean);
/*    */   public static final class Path extends Record implements ComponentPath { private final ContainerEventHandler component;
/*    */     private final ComponentPath childPath;
/*    */     
/* 31 */     public Path(ContainerEventHandler component, ComponentPath childPath) { this.component = component; this.childPath = childPath; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/ComponentPath$Path;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 31 */       //   0	7	0	this	Lnet/minecraft/client/gui/ComponentPath$Path; } public ContainerEventHandler component() { return this.component; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/ComponentPath$Path;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/ComponentPath$Path; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/ComponentPath$Path;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/ComponentPath$Path;
/* 31 */       //   0	8	1	o	Ljava/lang/Object; } public ComponentPath childPath() { return this.childPath; }
/*    */     
/*    */     public void applyFocus(boolean focused) {
/* 34 */       if (!focused) {
/* 35 */         this.component.setFocused(null);
/*    */       } else {
/* 37 */         this.component.setFocused(this.childPath.component());
/*    */       } 
/* 39 */       this.childPath.applyFocus(focused);
/*    */     } }
/*    */   public static final class Leaf extends Record implements ComponentPath { private final GuiEventListener component;
/*    */     
/* 43 */     public Leaf(GuiEventListener component) { this.component = component; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/ComponentPath$Leaf;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/ComponentPath$Leaf; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/ComponentPath$Leaf;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/ComponentPath$Leaf; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/ComponentPath$Leaf;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/ComponentPath$Leaf;
/* 43 */       //   0	8	1	o	Ljava/lang/Object; } public GuiEventListener component() { return this.component; }
/*    */     
/*    */     public void applyFocus(boolean focused) {
/* 46 */       this.component.setFocused(focused);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/ComponentPath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */