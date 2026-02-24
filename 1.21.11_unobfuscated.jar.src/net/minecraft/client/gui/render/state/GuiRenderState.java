/*     */ package net.minecraft.client.gui.render.state;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiRenderState
/*     */ {
/*     */   private static final int DEBUG_RECTANGLE_COLOR = 2000962815;
/*  26 */   private final List<Node> strata = new ArrayList<>();
/*  27 */   private int firstStratumAfterBlur = Integer.MAX_VALUE;
/*     */   
/*     */   private Node current;
/*  30 */   private final Set<Object> itemModelIdentities = new HashSet();
/*     */   private ScreenRectangle lastElementBounds;
/*     */   
/*     */   public GuiRenderState() {
/*  34 */     nextStratum();
/*     */   }
/*     */   
/*     */   public void nextStratum() {
/*  38 */     this.current = new Node(null);
/*  39 */     this.strata.add(this.current);
/*     */   }
/*     */   
/*     */   public void blurBeforeThisStratum() {
/*  43 */     if (this.firstStratumAfterBlur != Integer.MAX_VALUE) {
/*  44 */       throw new IllegalStateException("Can only blur once per frame");
/*     */     }
/*  46 */     this.firstStratumAfterBlur = this.strata.size() - 1;
/*     */   }
/*     */   
/*     */   public void up() {
/*  50 */     if (this.current.up == null) {
/*  51 */       this.current.up = new Node(this.current);
/*     */     }
/*  53 */     this.current = this.current.up;
/*     */   }
/*     */   
/*     */   public void submitItem(GuiItemRenderState itemState) {
/*  57 */     if (!findAppropriateNode(itemState)) {
/*     */       return;
/*     */     }
/*  60 */     this.itemModelIdentities.add(itemState.itemStackRenderState().getModelIdentity());
/*  61 */     this.current.submitItem(itemState);
/*  62 */     sumbitDebugRectangleIfEnabled(itemState.bounds());
/*     */   }
/*     */   
/*     */   public void submitText(GuiTextRenderState textState) {
/*  66 */     if (!findAppropriateNode(textState)) {
/*     */       return;
/*     */     }
/*  69 */     this.current.submitText(textState);
/*  70 */     sumbitDebugRectangleIfEnabled(textState.bounds());
/*     */   }
/*     */   
/*     */   public void submitPicturesInPictureState(PictureInPictureRenderState picturesInPictureState) {
/*  74 */     if (!findAppropriateNode((ScreenArea)picturesInPictureState)) {
/*     */       return;
/*     */     }
/*  77 */     this.current.submitPicturesInPictureState(picturesInPictureState);
/*  78 */     sumbitDebugRectangleIfEnabled(picturesInPictureState.bounds());
/*     */   }
/*     */   
/*     */   public void submitGuiElement(GuiElementRenderState blitState) {
/*  82 */     if (!findAppropriateNode(blitState)) {
/*     */       return;
/*     */     }
/*  85 */     this.current.submitGuiElement(blitState);
/*  86 */     sumbitDebugRectangleIfEnabled(blitState.bounds());
/*     */   }
/*     */   
/*     */   private void sumbitDebugRectangleIfEnabled(ScreenRectangle bounds) {
/*  90 */     if (!SharedConstants.DEBUG_RENDER_UI_LAYERING_RECTANGLES || bounds == null) {
/*     */       return;
/*     */     }
/*  93 */     up();
/*  94 */     this.current.submitGuiElement(new ColoredRectangleRenderState(RenderPipelines.GUI, TextureSetup.noTexture(), (Matrix3x2fc)new Matrix3x2f(), 0, 0, 10000, 10000, 2000962815, 2000962815, bounds));
/*     */   }
/*     */   
/*     */   private boolean findAppropriateNode(ScreenArea screenArea) {
/*  98 */     ScreenRectangle bounds = screenArea.bounds();
/*  99 */     if (bounds == null) {
/* 100 */       return false;
/*     */     }
/* 102 */     if (this.lastElementBounds != null && this.lastElementBounds.encompasses(bounds)) {
/* 103 */       up();
/*     */     } else {
/* 105 */       navigateToAboveHighestElementWithIntersectingBounds(bounds);
/*     */     } 
/* 107 */     this.lastElementBounds = bounds;
/* 108 */     return true;
/*     */   }
/*     */   
/*     */   private void navigateToAboveHighestElementWithIntersectingBounds(ScreenRectangle bounds) {
/* 112 */     Node node = this.strata.getLast();
/* 113 */     while (node.up != null) {
/* 114 */       node = node.up;
/*     */     }
/*     */     boolean found = false;
/* 117 */     while (!found) {
/*     */ 
/*     */ 
/*     */       
/* 121 */       found = (hasIntersection(bounds, (List)node.elementStates) || hasIntersection(bounds, (List)node.itemStates) || hasIntersection(bounds, (List)node.textStates) || hasIntersection(bounds, (List)node.picturesInPictureStates));
/* 122 */       if (node.parent == null) {
/*     */         break;
/*     */       }
/* 125 */       if (!found) {
/* 126 */         node = node.parent;
/*     */       }
/*     */     } 
/* 129 */     this.current = node;
/* 130 */     if (found) {
/* 131 */       up();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasIntersection(ScreenRectangle bounds, List<? extends ScreenArea> states) {
/* 136 */     if (states != null) {
/* 137 */       for (ScreenArea area : states) {
/* 138 */         ScreenRectangle existingBounds = area.bounds();
/* 139 */         if (existingBounds != null && existingBounds.intersects(bounds)) {
/* 140 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */   
/*     */   public void submitBlitToCurrentLayer(BlitRenderState blitState) {
/* 148 */     this.current.submitGuiElement(blitState);
/*     */   }
/*     */   
/*     */   public void submitGlyphToCurrentLayer(GuiElementRenderState glyphState) {
/* 152 */     this.current.submitGlyph(glyphState);
/*     */   }
/*     */   
/*     */   public Set<Object> getItemModelIdentities() {
/* 156 */     return this.itemModelIdentities;
/*     */   }
/*     */   
/*     */   public void forEachElement(Consumer<GuiElementRenderState> consumer, TraverseRange range) {
/* 160 */     traverse(node -> { if (node.elementStates == null && node.glyphStates == null) return;  if (node.elementStates != null) for (GuiElementRenderState elementState : node.elementStates) consumer.accept(elementState);   if (node.glyphStates != null) for (GuiElementRenderState glyphState : node.glyphStates) consumer.accept(glyphState);   }, range);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEachItem(Consumer<GuiItemRenderState> consumer) {
/* 178 */     Node currentBackup = this.current;
/* 179 */     traverse(node -> { if (consumer.itemStates != null) { this.current = consumer; for (GuiItemRenderState itemState : consumer.itemStates) consumer.accept(itemState);  }  }, TraverseRange.ALL);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     this.current = currentBackup;
/*     */   }
/*     */   
/*     */   public void forEachText(Consumer<GuiTextRenderState> consumer) {
/* 191 */     Node currentBackup = this.current;
/* 192 */     traverse(node -> { if (consumer.textStates != null) for (GuiTextRenderState textState : consumer.textStates) { this.current = consumer; consumer.accept(textState); }   }, TraverseRange.ALL);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     this.current = currentBackup;
/*     */   }
/*     */   
/*     */   public void forEachPictureInPicture(Consumer<PictureInPictureRenderState> consumer) {
/* 204 */     Node currentBackup = this.current;
/* 205 */     traverse(node -> { if (consumer.picturesInPictureStates != null) { this.current = consumer; for (PictureInPictureRenderState pictureInPictureState : consumer.picturesInPictureStates) consumer.accept(pictureInPictureState);  }  }, TraverseRange.ALL);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     this.current = currentBackup;
/*     */   }
/*     */   
/*     */   public void sortElements(Comparator<GuiElementRenderState> comparator) {
/* 217 */     traverse(node -> { if (node.elementStates != null) { if (SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER) Collections.shuffle(node.elementStates);  node.elementStates.sort(comparator); }  }, TraverseRange.ALL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void traverse(Consumer<Node> consumer, TraverseRange range) {
/* 228 */     int startIndex = 0;
/* 229 */     int endIndex = this.strata.size();
/*     */     
/* 231 */     if (range == TraverseRange.BEFORE_BLUR) {
/* 232 */       endIndex = Math.min(this.firstStratumAfterBlur, this.strata.size());
/* 233 */     } else if (range == TraverseRange.AFTER_BLUR) {
/* 234 */       startIndex = this.firstStratumAfterBlur;
/*     */     } 
/*     */     
/* 237 */     for (int i = startIndex; i < endIndex; i++) {
/* 238 */       Node stratum = this.strata.get(i);
/* 239 */       traverse(stratum, consumer);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void traverse(Node node, Consumer<Node> consumer) {
/* 244 */     consumer.accept(node);
/* 245 */     if (node.up != null) {
/* 246 */       traverse(node.up, consumer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Node
/*     */   {
/*     */     public final Node parent;
/*     */     public Node up;
/*     */     public List<GuiElementRenderState> elementStates;
/*     */     public List<GuiElementRenderState> glyphStates;
/*     */     public List<GuiItemRenderState> itemStates;
/*     */     public List<GuiTextRenderState> textStates;
/*     */     public List<PictureInPictureRenderState> picturesInPictureStates;
/*     */     
/*     */     private Node(Node parent) {
/* 261 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void submitItem(GuiItemRenderState itemState) {
/* 265 */       if (this.itemStates == null) {
/* 266 */         this.itemStates = new ArrayList<>();
/*     */       }
/* 268 */       this.itemStates.add(itemState);
/*     */     }
/*     */     
/*     */     public void submitText(GuiTextRenderState textState) {
/* 272 */       if (this.textStates == null) {
/* 273 */         this.textStates = new ArrayList<>();
/*     */       }
/* 275 */       this.textStates.add(textState);
/*     */     }
/*     */     
/*     */     public void submitPicturesInPictureState(PictureInPictureRenderState picturesInPictureState) {
/* 279 */       if (this.picturesInPictureStates == null) {
/* 280 */         this.picturesInPictureStates = new ArrayList<>();
/*     */       }
/* 282 */       this.picturesInPictureStates.add(picturesInPictureState);
/*     */     }
/*     */     
/*     */     public void submitGuiElement(GuiElementRenderState blitState) {
/* 286 */       if (this.elementStates == null) {
/* 287 */         this.elementStates = new ArrayList<>();
/*     */       }
/* 289 */       this.elementStates.add(blitState);
/*     */     }
/*     */     
/*     */     public void submitGlyph(GuiElementRenderState glyphState) {
/* 293 */       if (this.glyphStates == null) {
/* 294 */         this.glyphStates = new ArrayList<>();
/*     */       }
/* 296 */       this.glyphStates.add(glyphState);
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/* 301 */     this.itemModelIdentities.clear();
/* 302 */     this.strata.clear();
/* 303 */     this.firstStratumAfterBlur = Integer.MAX_VALUE;
/* 304 */     nextStratum();
/*     */   }
/*     */   
/*     */   public enum TraverseRange {
/* 308 */     ALL,
/* 309 */     BEFORE_BLUR,
/* 310 */     AFTER_BLUR;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/GuiRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */