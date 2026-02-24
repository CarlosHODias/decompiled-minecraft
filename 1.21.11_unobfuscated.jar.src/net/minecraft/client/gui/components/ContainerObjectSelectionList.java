/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenAxis;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public abstract class ContainerObjectSelectionList<E extends ContainerObjectSelectionList.Entry<E>>
/*     */   extends AbstractSelectionList<E> {
/*     */   public ContainerObjectSelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
/*  23 */     super(minecraft, width, height, y, itemHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/*  28 */     if (getItemCount() == 0)
/*     */     {
/*  30 */       return null;
/*     */     }
/*  32 */     if (navigationEvent instanceof FocusNavigationEvent.ArrowNavigation) { FocusNavigationEvent.ArrowNavigation arrowNavigation = (FocusNavigationEvent.ArrowNavigation)navigationEvent;
/*  33 */       Entry entry1 = (Entry)getFocused();
/*  34 */       if (arrowNavigation.direction().getAxis() == ScreenAxis.HORIZONTAL && entry1 != null) {
/*  35 */         return ComponentPath.path(this, entry1.nextFocusPath(navigationEvent));
/*     */       }
/*     */ 
/*     */       
/*  39 */       int index = -1;
/*  40 */       ScreenDirection direction = arrowNavigation.direction();
/*  41 */       if (entry1 != null)
/*     */       {
/*  43 */         index = entry1.children().indexOf(entry1.getFocused());
/*     */       }
/*  45 */       if (index == -1) {
/*  46 */         switch (direction) {
/*     */           
/*     */           case LEFT:
/*  49 */             index = Integer.MAX_VALUE;
/*  50 */             direction = ScreenDirection.DOWN;
/*     */             break;
/*     */           
/*     */           case RIGHT:
/*  54 */             index = 0;
/*  55 */             direction = ScreenDirection.DOWN; break;
/*     */           default:
/*  57 */             index = 0;
/*     */             break;
/*     */         } 
/*     */       }
/*  61 */       Entry entry2 = entry1;
/*     */       while (true) {
/*  63 */         entry2 = (Entry)nextEntry(direction, e -> !e.children().isEmpty(), (E)entry2);
/*  64 */         if (entry2 == null) {
/*  65 */           return null;
/*     */         }
/*  67 */         ComponentPath componentPath = entry2.focusPathAtIndex((FocusNavigationEvent)arrowNavigation, index);
/*  68 */         if (componentPath != null) {
/*  69 */           return ComponentPath.path(this, componentPath);
/*     */         }
/*     */       }  }
/*     */     
/*  73 */     return super.nextFocusPath(navigationEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFocused(GuiEventListener focused) {
/*  78 */     if (getFocused() == focused) {
/*     */       return;
/*     */     }
/*  81 */     super.setFocused(focused);
/*  82 */     if (focused == null) {
/*  83 */       setSelected(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NarratableEntry.NarrationPriority narrationPriority() {
/*  89 */     if (isFocused())
/*     */     {
/*  91 */       return NarratableEntry.NarrationPriority.FOCUSED;
/*     */     }
/*  93 */     return super.narrationPriority();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean entriesCanBeSelected() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 103 */     E e = getHovered(); if (e instanceof Entry) { Entry entry = (Entry)e;
/* 104 */       entry.updateNarration(output.nest());
/* 105 */       narrateListElementPosition(output, (E)entry); }
/* 106 */     else { e = getFocused(); if (e instanceof Entry) { Entry entry = (Entry)e;
/* 107 */         entry.updateNarration(output.nest());
/* 108 */         narrateListElementPosition(output, (E)entry); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   public static abstract class Entry<E extends Entry<E>>
/*     */     extends AbstractSelectionList.Entry<E>
/*     */     implements ContainerEventHandler {
/*     */     private GuiEventListener focused;
/*     */     private NarratableEntry lastNarratable;
/*     */     private boolean dragging;
/*     */     
/*     */     public boolean isDragging() {
/* 121 */       return this.dragging;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDragging(boolean dragging) {
/* 126 */       this.dragging = dragging;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 131 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFocused(GuiEventListener focused) {
/* 136 */       if (this.focused != null) {
/* 137 */         this.focused.setFocused(false);
/*     */       }
/* 139 */       if (focused != null) {
/* 140 */         focused.setFocused(true);
/*     */       }
/* 142 */       this.focused = focused;
/*     */     }
/*     */ 
/*     */     
/*     */     public GuiEventListener getFocused() {
/* 147 */       return this.focused;
/*     */     }
/*     */     
/*     */     public ComponentPath focusPathAtIndex(FocusNavigationEvent navigationEvent, int currentIndex) {
/* 151 */       if (children().isEmpty()) {
/* 152 */         return null;
/*     */       }
/* 154 */       ComponentPath componentPath = ((GuiEventListener)children().get(Math.min(currentIndex, children().size() - 1))).nextFocusPath(navigationEvent);
/* 155 */       return ComponentPath.path(this, componentPath);
/*     */     }
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
/*     */     public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: instanceof net/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation
/*     */       //   4: ifeq -> 180
/*     */       //   7: aload_1
/*     */       //   8: checkcast net/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation
/*     */       //   11: astore_2
/*     */       //   12: getstatic net/minecraft/client/gui/components/ContainerObjectSelectionList$1.$SwitchMap$net$minecraft$client$gui$navigation$ScreenDirection : [I
/*     */       //   15: aload_2
/*     */       //   16: invokevirtual direction : ()Lnet/minecraft/client/gui/navigation/ScreenDirection;
/*     */       //   19: invokevirtual ordinal : ()I
/*     */       //   22: iaload
/*     */       //   23: tableswitch default -> 52, 1 -> 66, 2 -> 70, 3 -> 62, 4 -> 62
/*     */       //   52: new java/lang/MatchException
/*     */       //   55: dup
/*     */       //   56: aconst_null
/*     */       //   57: aconst_null
/*     */       //   58: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   61: athrow
/*     */       //   62: iconst_0
/*     */       //   63: goto -> 71
/*     */       //   66: iconst_m1
/*     */       //   67: goto -> 71
/*     */       //   70: iconst_1
/*     */       //   71: istore_3
/*     */       //   72: iload_3
/*     */       //   73: ifne -> 78
/*     */       //   76: aconst_null
/*     */       //   77: areturn
/*     */       //   78: iload_3
/*     */       //   79: aload_0
/*     */       //   80: invokevirtual children : ()Ljava/util/List;
/*     */       //   83: aload_0
/*     */       //   84: invokevirtual getFocused : ()Lnet/minecraft/client/gui/components/events/GuiEventListener;
/*     */       //   87: invokeinterface indexOf : (Ljava/lang/Object;)I
/*     */       //   92: iadd
/*     */       //   93: iconst_0
/*     */       //   94: aload_0
/*     */       //   95: invokevirtual children : ()Ljava/util/List;
/*     */       //   98: invokeinterface size : ()I
/*     */       //   103: iconst_1
/*     */       //   104: isub
/*     */       //   105: invokestatic clamp : (III)I
/*     */       //   108: istore #4
/*     */       //   110: iload #4
/*     */       //   112: istore #5
/*     */       //   114: iload #5
/*     */       //   116: iflt -> 180
/*     */       //   119: iload #5
/*     */       //   121: aload_0
/*     */       //   122: invokevirtual children : ()Ljava/util/List;
/*     */       //   125: invokeinterface size : ()I
/*     */       //   130: if_icmpge -> 180
/*     */       //   133: aload_0
/*     */       //   134: invokevirtual children : ()Ljava/util/List;
/*     */       //   137: iload #5
/*     */       //   139: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   144: checkcast net/minecraft/client/gui/components/events/GuiEventListener
/*     */       //   147: astore #6
/*     */       //   149: aload #6
/*     */       //   151: aload_1
/*     */       //   152: invokeinterface nextFocusPath : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent;)Lnet/minecraft/client/gui/ComponentPath;
/*     */       //   157: astore #7
/*     */       //   159: aload #7
/*     */       //   161: ifnull -> 171
/*     */       //   164: aload_0
/*     */       //   165: aload #7
/*     */       //   167: invokestatic path : (Lnet/minecraft/client/gui/components/events/ContainerEventHandler;Lnet/minecraft/client/gui/ComponentPath;)Lnet/minecraft/client/gui/ComponentPath;
/*     */       //   170: areturn
/*     */       //   171: iload #5
/*     */       //   173: iload_3
/*     */       //   174: iadd
/*     */       //   175: istore #5
/*     */       //   177: goto -> 114
/*     */       //   180: aload_0
/*     */       //   181: aload_1
/*     */       //   182: invokespecial nextFocusPath : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent;)Lnet/minecraft/client/gui/ComponentPath;
/*     */       //   185: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       //   #161	-> 12
/*     */       //   #162	-> 62
/*     */       //   #163	-> 66
/*     */       //   #164	-> 70
/*     */       //   #166	-> 72
/*     */       //   #167	-> 76
/*     */       //   #169	-> 78
/*     */       //   #170	-> 110
/*     */       //   #171	-> 133
/*     */       //   #172	-> 149
/*     */       //   #173	-> 159
/*     */       //   #174	-> 164
/*     */       //   #170	-> 171
/*     */       //   #179	-> 180
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   149	22	6	child	Lnet/minecraft/client/gui/components/events/GuiEventListener;
/*     */       //   159	12	7	componentPath	Lnet/minecraft/client/gui/ComponentPath;
/*     */       //   114	66	5	i	I
/*     */       //   72	108	3	delta	I
/*     */       //   110	70	4	index	I
/*     */       //   12	168	2	arrowNavigation	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$ArrowNavigation;
/*     */       //   0	186	0	this	Lnet/minecraft/client/gui/components/ContainerObjectSelectionList$Entry;
/*     */       //   0	186	1	navigationEvent	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	186	0	this	Lnet/minecraft/client/gui/components/ContainerObjectSelectionList$Entry<TE;>;
/*     */     }
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
/*     */     void updateNarration(NarrationElementOutput output) {
/* 185 */       List<? extends NarratableEntry> narratables = narratables();
/* 186 */       Screen.NarratableSearchResult result = Screen.findNarratableWidget(narratables, this.lastNarratable);
/* 187 */       if (result != null) {
/* 188 */         if (result.priority().isTerminal()) {
/* 189 */           this.lastNarratable = result.entry();
/*     */         }
/* 191 */         if (narratables.size() > 1) {
/* 192 */           output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.object_list", new Object[] { result.index() + 1, narratables.size() }));
/*     */         }
/* 194 */         result.entry().updateNarration(output.nest());
/*     */       } 
/*     */     }
/*     */     
/*     */     public abstract List<? extends NarratableEntry> narratables();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/ContainerObjectSelectionList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */