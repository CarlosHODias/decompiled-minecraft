/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSelectionList<E extends AbstractSelectionList.Entry<E>>
/*     */   extends AbstractContainerWidget
/*     */ {
/*  34 */   private static final Identifier MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
/*  35 */   private static final Identifier INWORLD_MENU_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
/*     */   
/*     */   private static final int SEPARATOR_HEIGHT = 2;
/*     */   protected final Minecraft minecraft;
/*     */   protected final int defaultEntryHeight;
/*  40 */   private final List<E> children = new TrackedList();
/*     */   protected boolean centerListVertically = true;
/*     */   private E selected;
/*     */   private E hovered;
/*     */   
/*     */   public AbstractSelectionList(Minecraft minecraft, int width, int height, int y, int defaultEntryHeight) {
/*  46 */     super(0, y, width, height, CommonComponents.EMPTY);
/*  47 */     this.minecraft = minecraft;
/*  48 */     this.defaultEntryHeight = defaultEntryHeight;
/*     */   }
/*     */   
/*     */   public E getSelected() {
/*  52 */     return this.selected;
/*     */   }
/*     */   
/*     */   public void setSelected(E selected) {
/*  56 */     this.selected = selected;
/*  57 */     if (selected != null) {
/*  58 */       boolean topClipped = (selected.getContentY() < getY());
/*  59 */       boolean bottomClipped = (selected.getContentBottom() > getBottom());
/*  60 */       if (this.minecraft.getLastInputType().isKeyboard() || topClipped || bottomClipped) {
/*  61 */         scrollToEntry(selected);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E getFocused() {
/*  69 */     return (E)super.getFocused();
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<E> children() {
/*  74 */     return Collections.unmodifiableList(this.children);
/*     */   }
/*     */   
/*     */   protected void sort(Comparator<E> comparator) {
/*  78 */     this.children.sort(comparator);
/*  79 */     repositionEntries();
/*     */   }
/*     */   
/*     */   protected void swap(int firstIndex, int secondIndex) {
/*  83 */     Collections.swap(this.children, firstIndex, secondIndex);
/*  84 */     repositionEntries();
/*  85 */     scrollToEntry(this.children.get(secondIndex));
/*     */   }
/*     */   
/*     */   protected void clearEntries() {
/*  89 */     this.children.clear();
/*     */     
/*  91 */     this.selected = null;
/*     */   }
/*     */   
/*     */   protected void clearEntriesExcept(E exception) {
/*  95 */     this.children.removeIf(entry -> (entry != exception));
/*  96 */     if (this.selected != exception) {
/*  97 */       setSelected(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceEntries(Collection<E> newChildren) {
/* 102 */     clearEntries();
/* 103 */     for (Entry entry : newChildren) {
/* 104 */       addEntry((E)entry);
/*     */     }
/*     */   }
/*     */   
/*     */   private int getFirstEntryY() {
/* 109 */     return getY() + 2;
/*     */   }
/*     */   
/*     */   public int getNextY() {
/* 113 */     int y = getFirstEntryY() - (int)scrollAmount();
/* 114 */     for (Entry entry : this.children) {
/* 115 */       y += entry.getHeight();
/*     */     }
/* 117 */     return y;
/*     */   }
/*     */   
/*     */   protected int addEntry(E entry) {
/* 121 */     return addEntry(entry, this.defaultEntryHeight);
/*     */   }
/*     */   
/*     */   protected int addEntry(E entry, int height) {
/* 125 */     entry.setX(getRowLeft());
/* 126 */     entry.setWidth(getRowWidth());
/* 127 */     entry.setY(getNextY());
/* 128 */     entry.setHeight(height);
/* 129 */     this.children.add(entry);
/* 130 */     return this.children.size() - 1;
/*     */   }
/*     */   
/*     */   protected void addEntryToTop(E entry) {
/* 134 */     addEntryToTop(entry, this.defaultEntryHeight);
/*     */   }
/*     */   
/*     */   protected void addEntryToTop(E entry, int height) {
/* 138 */     double scrollFromBottom = maxScrollAmount() - scrollAmount();
/* 139 */     entry.setHeight(height);
/* 140 */     this.children.addFirst(entry);
/* 141 */     repositionEntries();
/* 142 */     setScrollAmount(maxScrollAmount() - scrollFromBottom);
/*     */   }
/*     */   
/*     */   private void repositionEntries() {
/* 146 */     int y = getFirstEntryY() - (int)scrollAmount();
/* 147 */     for (Entry entry : this.children) {
/* 148 */       entry.setY(y);
/* 149 */       y += entry.getHeight();
/* 150 */       entry.setX(getRowLeft());
/* 151 */       entry.setWidth(getRowWidth());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void removeEntryFromTop(E entry) {
/* 156 */     double scrollFromBottom = maxScrollAmount() - scrollAmount();
/* 157 */     removeEntry(entry);
/* 158 */     setScrollAmount(maxScrollAmount() - scrollFromBottom);
/*     */   }
/*     */   
/*     */   protected int getItemCount() {
/* 162 */     return children().size();
/*     */   }
/*     */   
/*     */   protected boolean entriesCanBeSelected() {
/* 166 */     return true;
/*     */   }
/*     */   
/*     */   protected final E getEntryAtPosition(double posX, double posY) {
/* 170 */     for (Entry entry : this.children) {
/* 171 */       if (entry.isMouseOver(posX, posY)) {
/* 172 */         return (E)entry;
/*     */       }
/*     */     } 
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSize(int width, HeaderAndFooterLayout layout) {
/* 180 */     updateSizeAndPosition(width, layout.getContentHeight(), layout.getHeaderHeight());
/*     */   }
/*     */   
/*     */   public void updateSizeAndPosition(int width, int height, int y) {
/* 184 */     updateSizeAndPosition(width, height, 0, y);
/*     */   }
/*     */   
/*     */   public void updateSizeAndPosition(int width, int height, int x, int y) {
/* 188 */     setSize(width, height);
/* 189 */     setPosition(x, y);
/* 190 */     repositionEntries();
/* 191 */     if (getSelected() != null) {
/* 192 */       scrollToEntry(getSelected());
/*     */     }
/* 194 */     refreshScrollAmount();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int contentHeight() {
/* 199 */     int totalHeight = 0;
/* 200 */     for (Entry entry : this.children) {
/* 201 */       totalHeight += entry.getHeight();
/*     */     }
/* 203 */     return totalHeight + 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 208 */     this.hovered = isMouseOver(mouseX, mouseY) ? getEntryAtPosition(mouseX, mouseY) : null;
/*     */     
/* 210 */     renderListBackground(graphics);
/*     */     
/* 212 */     enableScissor(graphics);
/* 213 */     renderListItems(graphics, mouseX, mouseY, a);
/* 214 */     graphics.disableScissor();
/*     */     
/* 216 */     renderListSeparators(graphics);
/*     */     
/* 218 */     renderScrollbar(graphics, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected void renderListSeparators(GuiGraphics graphics) {
/* 222 */     Identifier headerSeparator = (this.minecraft.level == null) ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
/* 223 */     Identifier footerSeparator = (this.minecraft.level == null) ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
/* 224 */     graphics.blit(RenderPipelines.GUI_TEXTURED, headerSeparator, getX(), getY() - 2, 0.0F, 0.0F, getWidth(), 2, 32, 2);
/* 225 */     graphics.blit(RenderPipelines.GUI_TEXTURED, footerSeparator, getX(), getBottom(), 0.0F, 0.0F, getWidth(), 2, 32, 2);
/*     */   }
/*     */   
/*     */   protected void renderListBackground(GuiGraphics graphics) {
/* 229 */     Identifier menuListBackground = (this.minecraft.level == null) ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
/* 230 */     graphics.blit(RenderPipelines.GUI_TEXTURED, menuListBackground, getX(), getY(), getRight(), (getBottom() + (int)scrollAmount()), getWidth(), getHeight(), 32, 32);
/*     */   }
/*     */   
/*     */   protected void enableScissor(GuiGraphics graphics) {
/* 234 */     graphics.enableScissor(getX(), getY(), getRight(), getBottom());
/*     */   }
/*     */   
/*     */   protected void scrollToEntry(E entry) {
/* 238 */     int topDelta = entry.getY() - getY() - 2;
/* 239 */     if (topDelta < 0) {
/* 240 */       scroll(topDelta);
/*     */     }
/* 242 */     int bottomDelta = getBottom() - entry.getY() - entry.getHeight() - 2;
/* 243 */     if (bottomDelta < 0) {
/* 244 */       scroll(-bottomDelta);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void centerScrollOn(E entry) {
/* 249 */     int y = 0;
/* 250 */     for (Entry entry1 : this.children) {
/* 251 */       if (entry1 == entry) {
/* 252 */         y += entry1.getHeight() / 2;
/*     */         break;
/*     */       } 
/* 255 */       y += entry1.getHeight();
/*     */     } 
/* 257 */     setScrollAmount(y - this.height / 2.0D);
/*     */   }
/*     */   
/*     */   private void scroll(int amount) {
/* 261 */     setScrollAmount(scrollAmount() + amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/* 266 */     super.setScrollAmount(scrollAmount);
/* 267 */     repositionEntries();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double scrollRate() {
/* 272 */     return this.defaultEntryHeight / 2.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int scrollBarX() {
/* 277 */     return getRowRight() + 6 + 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<GuiEventListener> getChildAt(double x, double y) {
/* 282 */     return Optional.ofNullable((GuiEventListener)getEntryAtPosition(x, y));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(boolean focused) {
/* 287 */     super.setFocused(focused);
/* 288 */     if (!focused) {
/* 289 */       setFocused(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(GuiEventListener focused) {
/* 295 */     E oldFocus = getFocused();
/*     */     
/* 297 */     if (oldFocus != focused && oldFocus instanceof ContainerEventHandler) { ContainerEventHandler oldFocusContainer = (ContainerEventHandler)oldFocus;
/* 298 */       oldFocusContainer.setFocused(null); }
/*     */ 
/*     */     
/* 301 */     super.setFocused(focused);
/*     */     
/* 303 */     int index = this.children.indexOf(focused);
/* 304 */     if (index >= 0) {
/* 305 */       Entry entry = (Entry)this.children.get(index);
/* 306 */       setSelected((E)entry);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected E nextEntry(ScreenDirection dir) {
/* 311 */     return nextEntry(dir, entry -> true);
/*     */   }
/*     */   
/*     */   protected E nextEntry(ScreenDirection dir, Predicate<E> canSelect) {
/* 315 */     return nextEntry(dir, canSelect, getSelected());
/*     */   }
/*     */   
/*     */   protected E nextEntry(ScreenDirection dir, Predicate<E> canSelect, E startEntry) {
/* 319 */     switch (dir) { default: throw new MatchException(null, null);
/*     */       case RIGHT: case LEFT: 
/*     */       case UP: 
/* 322 */       case DOWN: break; }  int delta = 1;
/*     */     
/* 324 */     if (!children().isEmpty() && delta != 0) {
/*     */       int index;
/* 326 */       if (startEntry == null) {
/* 327 */         index = (delta > 0) ? 0 : (children().size() - 1);
/*     */       } else {
/* 329 */         index = children().indexOf(startEntry) + delta;
/*     */       } 
/*     */       
/* 332 */       for (int i = index; i >= 0 && i < this.children.size(); i += delta) {
/* 333 */         Entry entry = (Entry)children().get(i);
/* 334 */         if (canSelect.test((E)entry)) {
/* 335 */           return (E)entry;
/*     */         }
/*     */       } 
/*     */     } 
/* 339 */     return null;
/*     */   }
/*     */   
/*     */   protected void renderListItems(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 343 */     for (Entry entry : this.children) {
/* 344 */       if (entry.getY() + entry.getHeight() >= getY() && entry.getY() <= getBottom()) {
/* 345 */         renderItem(graphics, mouseX, mouseY, a, (E)entry);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void renderItem(GuiGraphics graphics, int mouseX, int mouseY, float a, E entry) {
/* 351 */     if (entriesCanBeSelected() && getSelected() == entry) {
/* 352 */       int outlineColor = isFocused() ? -1 : -8355712;
/* 353 */       renderSelection(graphics, entry, outlineColor);
/*     */     } 
/*     */     
/* 356 */     entry.renderContent(graphics, mouseX, mouseY, Objects.equals(this.hovered, entry), a);
/*     */   }
/*     */   
/*     */   protected void renderSelection(GuiGraphics graphics, E entry, int outlineColor) {
/* 360 */     int outlineX0 = entry.getX();
/* 361 */     int outlineY0 = entry.getY();
/* 362 */     int outlineX1 = outlineX0 + entry.getWidth();
/* 363 */     int outlineY1 = outlineY0 + entry.getHeight();
/* 364 */     graphics.fill(outlineX0, outlineY0, outlineX1, outlineY1, outlineColor);
/* 365 */     graphics.fill(outlineX0 + 1, outlineY0 + 1, outlineX1 - 1, outlineY1 - 1, -16777216);
/*     */   }
/*     */   
/*     */   public int getRowLeft() {
/* 369 */     return getX() + this.width / 2 - getRowWidth() / 2;
/*     */   }
/*     */   
/*     */   public int getRowRight() {
/* 373 */     return getRowLeft() + getRowWidth();
/*     */   }
/*     */   
/*     */   public int getRowTop(int row) {
/* 377 */     return ((Entry)this.children.get(row)).getY();
/*     */   }
/*     */   
/*     */   public int getRowBottom(int row) {
/* 381 */     Entry entry = (Entry)this.children.get(row);
/* 382 */     return entry.getY() + entry.getHeight();
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 386 */     return 220;
/*     */   }
/*     */ 
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/* 391 */     if (isFocused()) {
/* 392 */       return NarratableEntry.NarrationPriority.FOCUSED;
/*     */     }
/* 394 */     if (this.hovered != null) {
/* 395 */       return NarratableEntry.NarrationPriority.HOVERED;
/*     */     }
/* 397 */     return NarratableEntry.NarrationPriority.NONE;
/*     */   }
/*     */   
/*     */   protected void removeEntries(List<E> entries) {
/* 401 */     entries.forEach(this::removeEntry);
/*     */   }
/*     */   
/*     */   protected void removeEntry(E entry) {
/* 405 */     boolean removed = this.children.remove(entry);
/* 406 */     if (removed) {
/* 407 */       repositionEntries();
/* 408 */       if (entry == getSelected()) {
/* 409 */         setSelected(null);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected E getHovered() {
/* 415 */     return this.hovered;
/*     */   }
/*     */   
/*     */   private void bindEntryToSelf(Entry<E> entry) {
/* 419 */     entry.list = this;
/*     */   }
/*     */   
/*     */   protected void narrateListElementPosition(NarrationElementOutput output, E element) {
/* 423 */     List<E> children = children();
/* 424 */     if (children.size() > 1) {
/* 425 */       int index = children.indexOf(element);
/* 426 */       if (index != -1)
/* 427 */         output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.list", new Object[] { index + 1, children.size() })); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static abstract class Entry<E extends Entry<E>>
/*     */     implements LayoutElement, GuiEventListener {
/*     */     public static final int CONTENT_PADDING = 2;
/* 434 */     private int x = 0;
/* 435 */     private int y = 0;
/* 436 */     private int width = 0;
/*     */ 
/*     */     
/*     */     private int height;
/*     */     
/*     */     @Deprecated
/*     */     private AbstractSelectionList<E> list;
/*     */ 
/*     */     
/*     */     public void setFocused(boolean focused) {}
/*     */ 
/*     */     
/*     */     public boolean isFocused() {
/* 449 */       return (this.list.getFocused() == this);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract void renderContent(GuiGraphics param1GuiGraphics, int param1Int1, int param1Int2, boolean param1Boolean, float param1Float);
/*     */     
/*     */     public boolean isMouseOver(double mx, double my) {
/* 456 */       return getRectangle().containsPoint((int)mx, (int)my);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setX(int x) {
/* 461 */       this.x = x;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setY(int y) {
/* 466 */       this.y = y;
/*     */     }
/*     */     
/*     */     public void setWidth(int width) {
/* 470 */       this.width = width;
/*     */     }
/*     */     
/*     */     public void setHeight(int height) {
/* 474 */       this.height = height;
/*     */     }
/*     */     
/*     */     public int getContentX() {
/* 478 */       return getX() + 2;
/*     */     }
/*     */     
/*     */     public int getContentY() {
/* 482 */       return getY() + 2;
/*     */     }
/*     */     
/*     */     public int getContentHeight() {
/* 486 */       return getHeight() - 4;
/*     */     }
/*     */     
/*     */     public int getContentYMiddle() {
/* 490 */       return getContentY() + getContentHeight() / 2;
/*     */     }
/*     */     
/*     */     public int getContentBottom() {
/* 494 */       return getContentY() + getContentHeight();
/*     */     }
/*     */     
/*     */     public int getContentWidth() {
/* 498 */       return getWidth() - 4;
/*     */     }
/*     */     
/*     */     public int getContentXMiddle() {
/* 502 */       return getContentX() + getContentWidth() / 2;
/*     */     }
/*     */     
/*     */     public int getContentRight() {
/* 506 */       return getContentX() + getContentWidth();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getX() {
/* 511 */       return this.x;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getY() {
/* 516 */       return this.y;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getWidth() {
/* 521 */       return this.width;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 526 */       return this.height;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {}
/*     */ 
/*     */     
/*     */     public ScreenRectangle getRectangle() {
/* 535 */       return super.getRectangle();
/*     */     }
/*     */   }
/*     */   
/*     */   private class TrackedList extends AbstractList<E> {
/* 540 */     private final List<E> delegate = Lists.newArrayList();
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 544 */       return this.delegate.get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 549 */       return this.delegate.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public E set(int index, E element) {
/* 554 */       AbstractSelectionList.Entry entry = (AbstractSelectionList.Entry)this.delegate.set(index, element);
/* 555 */       AbstractSelectionList.this.bindEntryToSelf((AbstractSelectionList.Entry<E>)element);
/* 556 */       return (E)entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, E element) {
/* 561 */       this.delegate.add(index, element);
/* 562 */       AbstractSelectionList.this.bindEntryToSelf((AbstractSelectionList.Entry<E>)element);
/*     */     }
/*     */ 
/*     */     
/*     */     public E remove(int index) {
/* 567 */       return this.delegate.remove(index);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/AbstractSelectionList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */