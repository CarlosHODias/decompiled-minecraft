/*     */ package net.minecraft.client.gui.layouts;
/*     */ 
/*     */ import com.mojang.math.Divisor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EqualSpacingLayout
/*     */   extends AbstractLayout
/*     */ {
/*     */   private final Orientation orientation;
/*  21 */   private final List<ChildContainer> children = new ArrayList<>();
/*     */   
/*  23 */   private final LayoutSettings defaultChildLayoutSettings = LayoutSettings.defaults();
/*     */   
/*     */   public EqualSpacingLayout(int width, int height, Orientation orientation) {
/*  26 */     this(0, 0, width, height, orientation);
/*     */   }
/*     */   
/*     */   public EqualSpacingLayout(int x, int y, int width, int height, Orientation orientation) {
/*  30 */     super(x, y, width, height);
/*  31 */     this.orientation = orientation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void arrangeElements() {
/*  36 */     super.arrangeElements();
/*     */     
/*  38 */     if (this.children.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  42 */     int totalChildPrimaryLength = 0;
/*  43 */     int maxChildSecondaryLength = this.orientation.getSecondaryLength(this);
/*     */     
/*  45 */     for (ChildContainer child : this.children) {
/*  46 */       totalChildPrimaryLength += this.orientation.getPrimaryLength(child);
/*  47 */       maxChildSecondaryLength = Math.max(maxChildSecondaryLength, this.orientation.getSecondaryLength(child));
/*     */     } 
/*     */     
/*  50 */     int remainingSpace = this.orientation.getPrimaryLength(this) - totalChildPrimaryLength;
/*     */     
/*  52 */     int position = this.orientation.getPrimaryPosition(this);
/*     */     
/*  54 */     Iterator<ChildContainer> childIterator = this.children.iterator();
/*  55 */     ChildContainer firstChild = childIterator.next();
/*  56 */     this.orientation.setPrimaryPosition(firstChild, position);
/*  57 */     position += this.orientation.getPrimaryLength(firstChild);
/*  58 */     if (this.children.size() >= 2) {
/*  59 */       Divisor divisor = new Divisor(remainingSpace, this.children.size() - 1);
/*  60 */       while (divisor.hasNext()) {
/*  61 */         position += divisor.nextInt();
/*  62 */         ChildContainer child = childIterator.next();
/*  63 */         this.orientation.setPrimaryPosition(child, position);
/*  64 */         position += this.orientation.getPrimaryLength(child);
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     int thisSecondaryPosition = this.orientation.getSecondaryPosition(this);
/*  69 */     for (ChildContainer child : this.children) {
/*  70 */       this.orientation.setSecondaryPosition(child, thisSecondaryPosition, maxChildSecondaryLength);
/*     */     }
/*     */     
/*  73 */     switch (this.orientation.ordinal()) { case 0:
/*  74 */         this.height = maxChildSecondaryLength; break;
/*  75 */       case 1: this.width = maxChildSecondaryLength;
/*     */         break; }
/*     */   
/*     */   }
/*     */   
/*     */   public void visitChildren(Consumer<LayoutElement> layoutElementVisitor) {
/*  81 */     this.children.forEach(wrapper -> layoutElementVisitor.accept(wrapper.child));
/*     */   }
/*     */   
/*     */   public LayoutSettings newChildLayoutSettings() {
/*  85 */     return this.defaultChildLayoutSettings.copy();
/*     */   }
/*     */   
/*     */   public LayoutSettings defaultChildLayoutSetting() {
/*  89 */     return this.defaultChildLayoutSettings;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child) {
/*  93 */     return addChild(child, newChildLayoutSettings());
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, LayoutSettings layoutSettings) {
/*  97 */     this.children.add(new ChildContainer((LayoutElement)child, layoutSettings));
/*  98 */     return child;
/*     */   }
/*     */   
/*     */   public <T extends LayoutElement> T addChild(T child, Consumer<LayoutSettings> layoutSettingsAdjustments) {
/* 102 */     return addChild(child, (LayoutSettings)Util.make(newChildLayoutSettings(), layoutSettingsAdjustments));
/*     */   }
/*     */   
/*     */   public enum Orientation {
/* 106 */     HORIZONTAL, VERTICAL;
/*     */     
/*     */     private int getPrimaryLength(LayoutElement widget) {
/* 109 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */         
/* 111 */         widget.getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     private int getPrimaryLength(EqualSpacingLayout.ChildContainer childContainer) {
/* 116 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */         
/* 118 */         childContainer.getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     private int getSecondaryLength(LayoutElement widget) {
/* 123 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */         
/* 125 */         widget.getWidth();
/*     */     }
/*     */ 
/*     */     
/*     */     private int getSecondaryLength(EqualSpacingLayout.ChildContainer childContainer) {
/* 130 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */         
/* 132 */         childContainer.getWidth();
/*     */     }
/*     */ 
/*     */     
/*     */     private void setPrimaryPosition(EqualSpacingLayout.ChildContainer childContainer, int position) {
/* 137 */       switch (ordinal()) { case 0:
/* 138 */           childContainer.setX(position, childContainer.getWidth()); break;
/* 139 */         case 1: childContainer.setY(position, childContainer.getHeight());
/*     */           break; }
/*     */     
/*     */     }
/*     */     private void setSecondaryPosition(EqualSpacingLayout.ChildContainer childContainer, int position, int availableSpace) {
/* 144 */       switch (ordinal()) { case 0:
/* 145 */           childContainer.setY(position, availableSpace); break;
/* 146 */         case 1: childContainer.setX(position, availableSpace);
/*     */           break; }
/*     */     
/*     */     }
/*     */     private int getPrimaryPosition(LayoutElement widget) {
/* 151 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */         
/* 153 */         widget.getY();
/*     */     }
/*     */ 
/*     */     
/*     */     private int getSecondaryPosition(LayoutElement widget) {
/* 158 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*     */         
/* 160 */         widget.getX();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ChildContainer
/*     */     extends AbstractLayout.AbstractChildWrapper {
/*     */     protected ChildContainer(LayoutElement child, LayoutSettings layoutSettings) {
/* 167 */       super(child, layoutSettings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/EqualSpacingLayout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */