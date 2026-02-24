/*     */ package net.minecraft.client.gui.components.events;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenAxis;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import org.joml.Vector2i;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ContainerEventHandler
/*     */   extends GuiEventListener
/*     */ {
/*     */   default Optional<GuiEventListener> getChildAt(double x, double y) {
/*  30 */     for (GuiEventListener child : children()) {
/*  31 */       if (child.isMouseOver(x, y)) {
/*  32 */         return Optional.of(child);
/*     */       }
/*     */     } 
/*  35 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   default boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  40 */     Optional<GuiEventListener> child = getChildAt(event.x(), event.y());
/*  41 */     if (child.isEmpty()) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     GuiEventListener widget = child.get();
/*  46 */     if (widget.mouseClicked(event, doubleClick) && 
/*  47 */       widget.shouldTakeFocusAfterInteraction()) {
/*  48 */       setFocused(widget);
/*  49 */       if (event.button() == 0) {
/*  50 */         setDragging(true);
/*     */       }
/*     */     } 
/*     */     
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   default boolean mouseReleased(MouseButtonEvent event) {
/*  59 */     if (event.button() == 0 && isDragging()) {
/*  60 */       setDragging(false);
/*  61 */       if (getFocused() != null) {
/*  62 */         return getFocused().mouseReleased(event);
/*     */       }
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   default boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/*  70 */     if (getFocused() != null && isDragging() && event.button() == 0) {
/*  71 */       return getFocused().mouseDragged(event, dx, dy);
/*     */     }
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/*  82 */     return getChildAt(x, y).filter(child -> child.mouseScrolled(x, y, scrollX, scrollY)).isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean keyPressed(KeyEvent event) {
/*  88 */     return (getFocused() != null && getFocused().keyPressed(event));
/*     */   }
/*     */ 
/*     */   
/*     */   default boolean keyReleased(KeyEvent event) {
/*  93 */     return (getFocused() != null && getFocused().keyReleased(event));
/*     */   }
/*     */ 
/*     */   
/*     */   default boolean charTyped(CharacterEvent event) {
/*  98 */     return (getFocused() != null && getFocused().charTyped(event));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void setFocused(boolean focused) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isFocused() {
/* 112 */     return (getFocused() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   default ComponentPath getCurrentFocusPath() {
/* 117 */     GuiEventListener focused = getFocused();
/* 118 */     if (focused != null) {
/* 119 */       return ComponentPath.path(this, focused.getCurrentFocusPath());
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   default ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 126 */     GuiEventListener focus = getFocused();
/*     */ 
/*     */     
/* 129 */     if (focus != null) {
/* 130 */       ComponentPath focusPath = focus.nextFocusPath(navigationEvent);
/* 131 */       if (focusPath != null) {
/* 132 */         return ComponentPath.path(this, focusPath);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 137 */     if (navigationEvent instanceof FocusNavigationEvent.TabNavigation) { FocusNavigationEvent.TabNavigation tabNavigation = (FocusNavigationEvent.TabNavigation)navigationEvent;
/* 138 */       return handleTabNavigation(tabNavigation); }
/*     */     
/* 140 */     if (navigationEvent instanceof FocusNavigationEvent.ArrowNavigation) { FocusNavigationEvent.ArrowNavigation arrowNavigation = (FocusNavigationEvent.ArrowNavigation)navigationEvent;
/* 141 */       return handleArrowNavigation(arrowNavigation); }
/*     */ 
/*     */     
/* 144 */     return null;
/*     */   }
/*     */   private ComponentPath handleTabNavigation(FocusNavigationEvent.TabNavigation tabNavigation) {
/*     */     int newIndex;
/* 148 */     boolean forward = tabNavigation.forward();
/* 149 */     GuiEventListener focus = getFocused();
/* 150 */     List<? extends GuiEventListener> sortedChildren = new ArrayList<>(children());
/* 151 */     Collections.sort(sortedChildren, Comparator.comparingInt(child -> child.getTabOrderGroup()));
/*     */ 
/*     */     
/* 154 */     int index = sortedChildren.indexOf(focus);
/* 155 */     if (focus != null && index >= 0) {
/* 156 */       newIndex = index + (forward ? 1 : 0);
/*     */     }
/* 158 */     else if (forward) {
/* 159 */       newIndex = 0;
/*     */     } else {
/* 161 */       newIndex = sortedChildren.size();
/*     */     } 
/*     */ 
/*     */     
/* 165 */     ListIterator<? extends GuiEventListener> iterator = sortedChildren.listIterator(newIndex);
/*     */     
/* 167 */     Objects.requireNonNull(iterator); Objects.requireNonNull(iterator); BooleanSupplier test = forward ? iterator::hasNext : iterator::hasPrevious;
/* 168 */     Objects.requireNonNull(iterator); Objects.requireNonNull(iterator); Supplier<? extends GuiEventListener> getter = forward ? iterator::next : iterator::previous;
/*     */     
/* 170 */     while (test.getAsBoolean()) {
/* 171 */       GuiEventListener child = getter.get();
/* 172 */       ComponentPath focusPath = child.nextFocusPath((FocusNavigationEvent)tabNavigation);
/* 173 */       if (focusPath != null) {
/* 174 */         return ComponentPath.path(this, focusPath);
/*     */       }
/*     */     } 
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   private ComponentPath handleArrowNavigation(FocusNavigationEvent.ArrowNavigation arrowNavigation) {
/* 181 */     GuiEventListener focus = getFocused();
/* 182 */     if (focus == null) {
/* 183 */       ScreenDirection direction = arrowNavigation.direction();
/* 184 */       ScreenRectangle borderRectangle = getBorderForArrowNavigation(direction.getOpposite());
/* 185 */       return ComponentPath.path(this, nextFocusPathInDirection(borderRectangle, direction, null, (FocusNavigationEvent)arrowNavigation));
/*     */     } 
/* 187 */     ScreenRectangle focusedRectangle = focus.getRectangle();
/* 188 */     return ComponentPath.path(this, nextFocusPathInDirection(focusedRectangle, arrowNavigation.direction(), focus, (FocusNavigationEvent)arrowNavigation));
/*     */   }
/*     */   
/*     */   private ComponentPath nextFocusPathInDirection(ScreenRectangle focusedRectangle, ScreenDirection direction, GuiEventListener excluded, FocusNavigationEvent navigationEvent) {
/* 192 */     ScreenAxis axis = direction.getAxis();
/* 193 */     ScreenAxis otherAxis = axis.orthogonal();
/* 194 */     ScreenDirection positiveDirectionOtherAxis = otherAxis.getPositive();
/* 195 */     int focusedFirstBound = focusedRectangle.getBoundInDirection(direction.getOpposite());
/*     */     
/* 197 */     List<GuiEventListener> potentialChildren = new ArrayList<>();
/* 198 */     for (GuiEventListener child : children()) {
/* 199 */       if (child == excluded) {
/*     */         continue;
/*     */       }
/* 202 */       ScreenRectangle childRectangle = child.getRectangle();
/* 203 */       if (childRectangle.overlapsInAxis(focusedRectangle, otherAxis)) {
/* 204 */         int childFirstBound = childRectangle.getBoundInDirection(direction.getOpposite());
/* 205 */         if (direction.isAfter(childFirstBound, focusedFirstBound)) {
/* 206 */           potentialChildren.add(child); continue;
/* 207 */         }  if (childFirstBound == focusedFirstBound && 
/* 208 */           direction.isAfter(childRectangle.getBoundInDirection(direction), focusedRectangle.getBoundInDirection(direction))) {
/* 209 */           potentialChildren.add(child);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 215 */     Comparator<GuiEventListener> primaryComparator = Comparator.comparing(child -> child.getRectangle().getBoundInDirection(direction.getOpposite()), (Comparator<?>)direction.coordinateValueComparator());
/* 216 */     Comparator<GuiEventListener> secondaryComparator = Comparator.comparing(child -> child.getRectangle().getBoundInDirection(positiveDirectionOtherAxis.getOpposite()), (Comparator<?>)positiveDirectionOtherAxis.coordinateValueComparator());
/* 217 */     potentialChildren.sort(primaryComparator.thenComparing(secondaryComparator));
/* 218 */     for (GuiEventListener child : potentialChildren) {
/* 219 */       ComponentPath componentPath = child.nextFocusPath(navigationEvent);
/* 220 */       if (componentPath != null) {
/* 221 */         return componentPath;
/*     */       }
/*     */     } 
/* 224 */     return nextFocusPathVaguelyInDirection(focusedRectangle, direction, excluded, navigationEvent);
/*     */   }
/*     */   
/*     */   private ComponentPath nextFocusPathVaguelyInDirection(ScreenRectangle focusedRectangle, ScreenDirection direction, GuiEventListener excluded, FocusNavigationEvent navigationEvent) {
/* 228 */     ScreenAxis axis = direction.getAxis();
/* 229 */     ScreenAxis otherAxis = axis.orthogonal();
/*     */     
/* 231 */     List<Pair<GuiEventListener, Long>> potentialChildren = new ArrayList<>();
/*     */     
/* 233 */     ScreenPosition focusedSideCenter = ScreenPosition.of(axis, focusedRectangle.getBoundInDirection(direction), focusedRectangle.getCenterInAxis(otherAxis));
/* 234 */     for (GuiEventListener child : children()) {
/* 235 */       if (child == excluded) {
/*     */         continue;
/*     */       }
/* 238 */       ScreenRectangle childRectangle = child.getRectangle();
/* 239 */       ScreenPosition childOpposingSideCenter = ScreenPosition.of(axis, childRectangle.getBoundInDirection(direction.getOpposite()), childRectangle.getCenterInAxis(otherAxis));
/*     */       
/* 241 */       if (direction.isAfter(childOpposingSideCenter.getCoordinate(axis), focusedSideCenter.getCoordinate(axis))) {
/* 242 */         long distanceSquared = Vector2i.distanceSquared(focusedSideCenter.x(), focusedSideCenter.y(), childOpposingSideCenter.x(), childOpposingSideCenter.y());
/* 243 */         potentialChildren.add(Pair.of(child, distanceSquared));
/*     */       } 
/*     */     } 
/* 246 */     potentialChildren.sort(Comparator.comparingDouble(Pair::getSecond));
/* 247 */     for (Pair<GuiEventListener, Long> child : potentialChildren) {
/* 248 */       ComponentPath componentPath = ((GuiEventListener)child.getFirst()).nextFocusPath(navigationEvent);
/* 249 */       if (componentPath != null) {
/* 250 */         return componentPath;
/*     */       }
/*     */     } 
/* 253 */     return null;
/*     */   }
/*     */   
/*     */   List<? extends GuiEventListener> children();
/*     */   
/*     */   boolean isDragging();
/*     */   
/*     */   void setDragging(boolean paramBoolean);
/*     */   
/*     */   GuiEventListener getFocused();
/*     */   
/*     */   void setFocused(GuiEventListener paramGuiEventListener);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/events/ContainerEventHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */