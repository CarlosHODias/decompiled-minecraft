/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.URI;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.NarratorStatus;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.TabOrderedElement;
/*     */ import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.narration.ScreenNarrationCollector;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.Music;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class Screen
/*     */   extends AbstractContainerEventHandler implements Renderable {
/*  53 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  54 */   private static final Component USAGE_NARRATION = (Component)Component.translatable("narrator.screen.usage");
/*     */   
/*  56 */   public static final Identifier MENU_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_background.png");
/*  57 */   public static final Identifier HEADER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/header_separator.png");
/*  58 */   public static final Identifier FOOTER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/footer_separator.png");
/*  59 */   private static final Identifier INWORLD_MENU_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_background.png");
/*  60 */   public static final Identifier INWORLD_HEADER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/inworld_header_separator.png");
/*  61 */   public static final Identifier INWORLD_FOOTER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/inworld_footer_separator.png");
/*     */   
/*     */   protected static final float FADE_IN_TIME = 2000.0F;
/*     */   
/*     */   protected final Component title;
/*     */   
/*  67 */   private final List<GuiEventListener> children = Lists.newArrayList();
/*  68 */   private final List<NarratableEntry> narratables = Lists.newArrayList();
/*     */   protected final Minecraft minecraft;
/*     */   private boolean initialized;
/*     */   public int width;
/*     */   public int height;
/*  73 */   private final List<Renderable> renderables = Lists.newArrayList();
/*     */   
/*     */   protected final Font font;
/*  76 */   private static final long NARRATE_SUPPRESS_AFTER_INIT_TIME = TimeUnit.SECONDS.toMillis(2L);
/*  77 */   private static final long NARRATE_DELAY_NARRATOR_ENABLED = NARRATE_SUPPRESS_AFTER_INIT_TIME;
/*     */   
/*     */   private static final long NARRATE_DELAY_MOUSE_MOVE = 750L;
/*     */   private static final long NARRATE_DELAY_MOUSE_ACTION = 200L;
/*     */   private static final long NARRATE_DELAY_KEYBOARD_ACTION = 200L;
/*  82 */   private final ScreenNarrationCollector narrationState = new ScreenNarrationCollector();
/*  83 */   private long narrationSuppressTime = Long.MIN_VALUE;
/*  84 */   private long nextNarrationTime = Long.MAX_VALUE;
/*     */   
/*     */   protected CycleButton<NarratorStatus> narratorButton;
/*     */   private NarratableEntry lastNarratable;
/*     */   protected final Executor screenExecutor;
/*     */   
/*     */   protected Screen(Component title) {
/*  91 */     this(Minecraft.getInstance(), (Minecraft.getInstance()).font, title);
/*     */   }
/*     */   
/*     */   protected Screen(Minecraft minecraft, Font font, Component title) {
/*  95 */     this.minecraft = minecraft;
/*  96 */     this.font = font;
/*  97 */     this.title = title;
/*     */     
/*  99 */     this.screenExecutor = (runnable -> minecraft.execute(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getTitle() {
/* 107 */     return this.title;
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/* 111 */     return getTitle();
/*     */   }
/*     */   
/*     */   public final void renderWithTooltipAndSubtitles(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 115 */     graphics.nextStratum();
/* 116 */     renderBackground(graphics, mouseX, mouseY, a);
/* 117 */     graphics.nextStratum();
/*     */     
/* 119 */     render(graphics, mouseX, mouseY, a);
/* 120 */     graphics.renderDeferredElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 125 */     for (Renderable renderable : this.renderables) {
/* 126 */       renderable.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 132 */     if (event.isEscape() && shouldCloseOnEsc()) {
/* 133 */       onClose();
/* 134 */       return true;
/*     */     } 
/* 136 */     if (super.keyPressed(event)) {
/* 137 */       return true;
/*     */     }
/* 139 */     switch (event.key()) { case 263: 
/*     */       case 262: 
/*     */       case 265: 
/*     */       case 264: 
/*     */       case 258: 
/*     */       default:
/* 145 */         break; }  FocusNavigationEvent navigationEvent = null;
/*     */     
/* 147 */     if (navigationEvent != null) {
/* 148 */       ComponentPath focusPath = nextFocusPath(navigationEvent);
/*     */       
/* 150 */       if (focusPath == null && navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
/* 151 */         clearFocus();
/* 152 */         focusPath = nextFocusPath(navigationEvent);
/*     */       } 
/* 154 */       if (focusPath != null) {
/* 155 */         changeFocus(focusPath);
/*     */       }
/*     */     } 
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   private FocusNavigationEvent.TabNavigation createTabEvent(boolean forward) {
/* 162 */     return new FocusNavigationEvent.TabNavigation(forward);
/*     */   }
/*     */   
/*     */   private FocusNavigationEvent.ArrowNavigation createArrowEvent(ScreenDirection direction) {
/* 166 */     return new FocusNavigationEvent.ArrowNavigation(direction);
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/* 170 */     if (this.minecraft.getLastInputType().isKeyboard()) {
/* 171 */       FocusNavigationEvent.TabNavigation forwardTabEvent = new FocusNavigationEvent.TabNavigation(true);
/* 172 */       ComponentPath focusPath = nextFocusPath((FocusNavigationEvent)forwardTabEvent);
/* 173 */       if (focusPath != null) {
/* 174 */         changeFocus(focusPath);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setInitialFocus(GuiEventListener target) {
/* 180 */     ComponentPath path = ComponentPath.path((ContainerEventHandler)this, target.nextFocusPath((FocusNavigationEvent)new FocusNavigationEvent.InitialFocus()));
/* 181 */     if (path != null) {
/* 182 */       changeFocus(path);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearFocus() {
/* 187 */     ComponentPath componentPath = getCurrentFocusPath();
/* 188 */     if (componentPath != null) {
/* 189 */       componentPath.applyFocus(false);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected void changeFocus(ComponentPath componentPath) {
/* 195 */     clearFocus();
/* 196 */     componentPath.applyFocus(true);
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/* 200 */     return true;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 204 */     this.minecraft.setScreen(null);
/*     */   }
/*     */   
/*     */   protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
/* 208 */     this.renderables.add((Renderable)widget);
/* 209 */     return (T)addWidget((GuiEventListener)widget);
/*     */   }
/*     */   
/*     */   protected <T extends Renderable> T addRenderableOnly(T renderable) {
/* 213 */     this.renderables.add((Renderable)renderable);
/* 214 */     return renderable;
/*     */   }
/*     */   
/*     */   protected <T extends GuiEventListener & NarratableEntry> T addWidget(T widget) {
/* 218 */     this.children.add((GuiEventListener)widget);
/* 219 */     this.narratables.add((NarratableEntry)widget);
/* 220 */     return widget;
/*     */   }
/*     */   
/*     */   protected void removeWidget(GuiEventListener widget) {
/* 224 */     if (widget instanceof Renderable) {
/* 225 */       this.renderables.remove((Renderable)widget);
/*     */     }
/* 227 */     if (widget instanceof NarratableEntry) {
/* 228 */       this.narratables.remove((NarratableEntry)widget);
/*     */     }
/* 230 */     if (getFocused() == widget) {
/* 231 */       clearFocus();
/*     */     }
/* 233 */     this.children.remove(widget);
/*     */   }
/*     */   
/*     */   protected void clearWidgets() {
/* 237 */     this.renderables.clear();
/* 238 */     this.children.clear();
/* 239 */     this.narratables.clear();
/*     */   }
/*     */   
/*     */   public static List<Component> getTooltipFromItem(Minecraft minecraft, ItemStack itemStack) {
/* 243 */     return itemStack.getTooltipLines(Item.TooltipContext.of((Level)minecraft.level), (Player)minecraft.player, minecraft.options.advancedItemTooltips ? (TooltipFlag)TooltipFlag.Default.ADVANCED : (TooltipFlag)TooltipFlag.Default.NORMAL);
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
/*     */   protected void insertText(String text, boolean replace) {}
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
/*     */   protected static void defaultHandleGameClickEvent(ClickEvent event, Minecraft minecraft, Screen activeScreen) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: getfield player : Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   4: ldc_w 'Player not available'
/*     */     //   7: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   10: checkcast net/minecraft/client/player/LocalPlayer
/*     */     //   13: astore_3
/*     */     //   14: aload_0
/*     */     //   15: dup
/*     */     //   16: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   19: pop
/*     */     //   20: astore #4
/*     */     //   22: iconst_0
/*     */     //   23: istore #5
/*     */     //   25: aload #4
/*     */     //   27: iload #5
/*     */     //   29: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   34: tableswitch default -> 158, 0 -> 60, 1 -> 88, 2 -> 111
/*     */     //   60: aload #4
/*     */     //   62: checkcast net/minecraft/network/chat/ClickEvent$RunCommand
/*     */     //   65: astore #6
/*     */     //   67: aload #6
/*     */     //   69: invokevirtual command : ()Ljava/lang/String;
/*     */     //   72: astore #8
/*     */     //   74: aload #8
/*     */     //   76: astore #7
/*     */     //   78: aload_3
/*     */     //   79: aload #7
/*     */     //   81: aload_2
/*     */     //   82: invokestatic clickCommandAction : (Lnet/minecraft/client/player/LocalPlayer;Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   85: goto -> 164
/*     */     //   88: aload #4
/*     */     //   90: checkcast net/minecraft/network/chat/ClickEvent$ShowDialog
/*     */     //   93: astore #8
/*     */     //   95: aload_3
/*     */     //   96: getfield connection : Lnet/minecraft/client/multiplayer/ClientPacketListener;
/*     */     //   99: aload #8
/*     */     //   101: invokevirtual dialog : ()Lnet/minecraft/core/Holder;
/*     */     //   104: aload_2
/*     */     //   105: invokevirtual showDialog : (Lnet/minecraft/core/Holder;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   108: goto -> 164
/*     */     //   111: aload #4
/*     */     //   113: checkcast net/minecraft/network/chat/ClickEvent$Custom
/*     */     //   116: astore #9
/*     */     //   118: aload_3
/*     */     //   119: getfield connection : Lnet/minecraft/client/multiplayer/ClientPacketListener;
/*     */     //   122: new net/minecraft/network/protocol/common/ServerboundCustomClickActionPacket
/*     */     //   125: dup
/*     */     //   126: aload #9
/*     */     //   128: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   131: aload #9
/*     */     //   133: invokevirtual payload : ()Ljava/util/Optional;
/*     */     //   136: invokespecial <init> : (Lnet/minecraft/resources/Identifier;Ljava/util/Optional;)V
/*     */     //   139: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   142: aload_1
/*     */     //   143: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   146: aload_2
/*     */     //   147: if_acmpeq -> 164
/*     */     //   150: aload_1
/*     */     //   151: aload_2
/*     */     //   152: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   155: goto -> 164
/*     */     //   158: aload_0
/*     */     //   159: aload_1
/*     */     //   160: aload_2
/*     */     //   161: invokestatic defaultHandleClickEvent : (Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   164: goto -> 184
/*     */     //   167: astore #4
/*     */     //   169: new java/lang/MatchException
/*     */     //   172: dup
/*     */     //   173: aload #4
/*     */     //   175: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   178: aload #4
/*     */     //   180: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   183: athrow
/*     */     //   184: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #254	-> 0
/*     */     //   #256	-> 14
/*     */     //   #257	-> 60
/*     */     //   #258	-> 88
/*     */     //   #259	-> 111
/*     */     //   #260	-> 118
/*     */     //   #261	-> 142
/*     */     //   #262	-> 150
/*     */     //   #265	-> 158
/*     */     //   #259	-> 167
/*     */     //   #267	-> 184
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   78	10	7	command	Ljava/lang/String;
/*     */     //   95	16	8	dialog	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*     */     //   118	40	9	custom	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     //   14	153	3	player	Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   0	185	0	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   0	185	1	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   0	185	2	activeScreen	Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   184	1	3	player	Lnet/minecraft/client/player/LocalPlayer;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   69	72	167	java/lang/Throwable
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
/*     */   protected static void defaultHandleClickEvent(ClickEvent event, Minecraft minecraft, Screen activeScreen) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore #4
/*     */     //   8: iconst_0
/*     */     //   9: istore #5
/*     */     //   11: aload #4
/*     */     //   13: iload #5
/*     */     //   15: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   20: tableswitch default -> 168, 0 -> 52, 1 -> 82, 2 -> 104, 3 -> 137
/*     */     //   52: aload #4
/*     */     //   54: checkcast net/minecraft/network/chat/ClickEvent$OpenUrl
/*     */     //   57: astore #6
/*     */     //   59: aload #6
/*     */     //   61: invokevirtual uri : ()Ljava/net/URI;
/*     */     //   64: astore #8
/*     */     //   66: aload #8
/*     */     //   68: astore #7
/*     */     //   70: aload_1
/*     */     //   71: aload_2
/*     */     //   72: aload #7
/*     */     //   74: invokestatic clickUrlAction : (Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)Z
/*     */     //   77: pop
/*     */     //   78: iconst_0
/*     */     //   79: goto -> 181
/*     */     //   82: aload #4
/*     */     //   84: checkcast net/minecraft/network/chat/ClickEvent$OpenFile
/*     */     //   87: astore #8
/*     */     //   89: invokestatic getPlatform : ()Lnet/minecraft/util/Util$OS;
/*     */     //   92: aload #8
/*     */     //   94: invokevirtual file : ()Ljava/io/File;
/*     */     //   97: invokevirtual openFile : (Ljava/io/File;)V
/*     */     //   100: iconst_1
/*     */     //   101: goto -> 181
/*     */     //   104: aload #4
/*     */     //   106: checkcast net/minecraft/network/chat/ClickEvent$SuggestCommand
/*     */     //   109: astore #9
/*     */     //   111: aload #9
/*     */     //   113: invokevirtual command : ()Ljava/lang/String;
/*     */     //   116: astore #11
/*     */     //   118: aload #11
/*     */     //   120: astore #10
/*     */     //   122: aload_2
/*     */     //   123: ifnull -> 133
/*     */     //   126: aload_2
/*     */     //   127: aload #10
/*     */     //   129: iconst_1
/*     */     //   130: invokevirtual insertText : (Ljava/lang/String;Z)V
/*     */     //   133: iconst_1
/*     */     //   134: goto -> 181
/*     */     //   137: aload #4
/*     */     //   139: checkcast net/minecraft/network/chat/ClickEvent$CopyToClipboard
/*     */     //   142: astore #11
/*     */     //   144: aload #11
/*     */     //   146: invokevirtual value : ()Ljava/lang/String;
/*     */     //   149: astore #13
/*     */     //   151: aload #13
/*     */     //   153: astore #12
/*     */     //   155: aload_1
/*     */     //   156: getfield keyboardHandler : Lnet/minecraft/client/KeyboardHandler;
/*     */     //   159: aload #12
/*     */     //   161: invokevirtual setClipboard : (Ljava/lang/String;)V
/*     */     //   164: iconst_1
/*     */     //   165: goto -> 181
/*     */     //   168: getstatic net/minecraft/client/gui/screens/Screen.LOGGER : Lorg/slf4j/Logger;
/*     */     //   171: ldc_w 'Don't know how to handle {}'
/*     */     //   174: aload_0
/*     */     //   175: invokeinterface error : (Ljava/lang/String;Ljava/lang/Object;)V
/*     */     //   180: iconst_1
/*     */     //   181: istore_3
/*     */     //   182: iload_3
/*     */     //   183: ifeq -> 219
/*     */     //   186: aload_1
/*     */     //   187: getfield screen : Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   190: aload_2
/*     */     //   191: if_acmpeq -> 219
/*     */     //   194: aload_1
/*     */     //   195: aload_2
/*     */     //   196: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   199: goto -> 219
/*     */     //   202: astore #4
/*     */     //   204: new java/lang/MatchException
/*     */     //   207: dup
/*     */     //   208: aload #4
/*     */     //   210: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   213: aload #4
/*     */     //   215: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   218: athrow
/*     */     //   219: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #270	-> 0
/*     */     //   #271	-> 52
/*     */     //   #272	-> 70
/*     */     //   #274	-> 78
/*     */     //   #276	-> 82
/*     */     //   #277	-> 89
/*     */     //   #278	-> 100
/*     */     //   #280	-> 104
/*     */     //   #281	-> 122
/*     */     //   #282	-> 126
/*     */     //   #284	-> 133
/*     */     //   #286	-> 137
/*     */     //   #287	-> 155
/*     */     //   #288	-> 164
/*     */     //   #291	-> 168
/*     */     //   #293	-> 180
/*     */     //   #296	-> 182
/*     */     //   #297	-> 194
/*     */     //   #286	-> 202
/*     */     //   #299	-> 219
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   70	12	7	uri	Ljava/net/URI;
/*     */     //   89	15	8	openFile	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*     */     //   122	15	10	command	Ljava/lang/String;
/*     */     //   155	13	12	value	Ljava/lang/String;
/*     */     //   182	20	3	shouldActivateScreen	Z
/*     */     //   0	220	0	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   0	220	1	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   0	220	2	activeScreen	Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   219	1	3	shouldActivateScreen	Z
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   61	64	202	java/lang/Throwable
/*     */     //   113	116	202	java/lang/Throwable
/*     */     //   146	149	202	java/lang/Throwable
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
/*     */   protected static boolean clickUrlAction(Minecraft minecraft, Screen screen, URI uri) {
/* 302 */     if (!((Boolean)minecraft.options.chatLinks().get())) {
/* 303 */       return false;
/*     */     }
/* 305 */     if ((Boolean)minecraft.options.chatLinksPrompt().get()) {
/* 306 */       minecraft.setScreen(new ConfirmLinkScreen(result -> {
/*     */               if (result) {
/*     */                 Util.getPlatform().openUri(uri);
/*     */               }
/*     */               minecraft.setScreen(screen);
/* 311 */             }, uri.toString(), false));
/*     */     } else {
/* 313 */       Util.getPlatform().openUri(uri);
/*     */     } 
/* 315 */     return true;
/*     */   }
/*     */   
/*     */   protected static void clickCommandAction(LocalPlayer player, String command, Screen screenAfterCommand) {
/* 319 */     player.connection.sendUnattendedCommand(Commands.trimOptionalPrefix(command), screenAfterCommand);
/*     */   }
/*     */   
/*     */   public final void init(int width, int height) {
/* 323 */     this.width = width;
/* 324 */     this.height = height;
/* 325 */     if (!this.initialized) {
/* 326 */       init();
/* 327 */       setInitialFocus();
/*     */     } else {
/* 329 */       repositionElements();
/*     */     } 
/* 331 */     this.initialized = true;
/* 332 */     triggerImmediateNarration(false);
/*     */     
/* 334 */     if (this.minecraft.getLastInputType().isKeyboard()) {
/* 335 */       setNarrationSuppressTime(Long.MAX_VALUE);
/*     */     } else {
/* 337 */       suppressNarration(NARRATE_SUPPRESS_AFTER_INIT_TIME);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void rebuildWidgets() {
/* 342 */     clearWidgets();
/* 343 */     clearFocus();
/* 344 */     init();
/* 345 */     setInitialFocus();
/*     */   }
/*     */   
/*     */   protected void fadeWidgets(float widgetFade) {
/* 349 */     for (GuiEventListener button : children()) {
/* 350 */       if (button instanceof AbstractWidget) { AbstractWidget widget = (AbstractWidget)button;
/* 351 */         widget.setAlpha(widgetFade); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 358 */     return this.children;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {}
/*     */ 
/*     */   
/*     */   public void tick() {}
/*     */ 
/*     */   
/*     */   public void removed() {}
/*     */ 
/*     */   
/*     */   public void added() {}
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 374 */     if (isInGameUi()) {
/* 375 */       renderTransparentBackground(graphics);
/*     */     } else {
/* 377 */       if (this.minecraft.level == null) {
/* 378 */         renderPanorama(graphics, a);
/*     */       }
/* 380 */       renderBlurredBackground(graphics);
/* 381 */       renderMenuBackground(graphics);
/*     */     } 
/*     */     
/* 384 */     this.minecraft.gui.renderDeferredSubtitles();
/*     */   }
/*     */   
/*     */   protected void renderBlurredBackground(GuiGraphics graphics) {
/* 388 */     float blurRadius = this.minecraft.options.getMenuBackgroundBlurriness();
/* 389 */     if (blurRadius >= 1.0F) {
/* 390 */       graphics.blurBeforeThisStratum();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void renderPanorama(GuiGraphics graphics, float a) {
/* 395 */     this.minecraft.gameRenderer.getPanorama().render(graphics, this.width, this.height, panoramaShouldSpin());
/*     */   }
/*     */   
/*     */   protected void renderMenuBackground(GuiGraphics graphics) {
/* 399 */     renderMenuBackground(graphics, 0, 0, this.width, this.height);
/*     */   }
/*     */   
/*     */   protected void renderMenuBackground(GuiGraphics graphics, int x, int y, int width, int height) {
/* 403 */     renderMenuBackgroundTexture(graphics, (this.minecraft.level == null) ? MENU_BACKGROUND : INWORLD_MENU_BACKGROUND, x, y, 0.0F, 0.0F, width, height);
/*     */   }
/*     */   
/*     */   public static void renderMenuBackgroundTexture(GuiGraphics graphics, Identifier menuBackground, int x, int y, float u, float v, int width, int height) {
/* 407 */     int size = 32;
/* 408 */     graphics.blit(RenderPipelines.GUI_TEXTURED, menuBackground, x, y, u, v, width, height, 32, 32);
/*     */   }
/*     */   
/*     */   public void renderTransparentBackground(GuiGraphics graphics) {
/* 412 */     graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
/*     */   }
/*     */   
/*     */   public boolean isPauseScreen() {
/* 416 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isInGameUi() {
/* 420 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean panoramaShouldSpin() {
/* 424 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isAllowedInPortal() {
/* 428 */     return isPauseScreen();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 433 */     rebuildWidgets();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(int width, int height) {
/* 438 */     this.width = width;
/* 439 */     this.height = height;
/* 440 */     repositionElements();
/*     */   }
/*     */   
/*     */   public void fillCrashDetails(CrashReport report) {
/* 444 */     CrashReportCategory category = report.addCategory("Affected screen", 1);
/* 445 */     category.setDetail("Screen name", () -> getClass().getCanonicalName());
/*     */   }
/*     */   
/*     */   protected boolean isValidCharacterForName(String currentName, int newChar, int cursorPos) {
/* 449 */     int colonPos = currentName.indexOf(':');
/* 450 */     int slashPos = currentName.indexOf('/');
/*     */     
/* 452 */     if (newChar == 58) {
/* 453 */       return ((slashPos == -1 || cursorPos <= slashPos) && colonPos == -1);
/*     */     }
/*     */     
/* 456 */     if (newChar == 47) {
/* 457 */       return (cursorPos > colonPos);
/*     */     }
/*     */     
/* 460 */     return (newChar == 95 || newChar == 45 || (newChar >= 97 && newChar <= 122) || (newChar >= 48 && newChar <= 57) || newChar == 46);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 465 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onFilesDrop(List<Path> files) {}
/*     */   
/*     */   private void scheduleNarration(long delay, boolean ignoreSuppression) {
/* 472 */     this.nextNarrationTime = Util.getMillis() + delay;
/* 473 */     if (ignoreSuppression) {
/* 474 */       this.narrationSuppressTime = Long.MIN_VALUE;
/*     */     }
/*     */   }
/*     */   
/*     */   private void suppressNarration(long duration) {
/* 479 */     setNarrationSuppressTime(Util.getMillis() + duration);
/*     */   }
/*     */   
/*     */   private void setNarrationSuppressTime(long narrationSuppressTime) {
/* 483 */     this.narrationSuppressTime = narrationSuppressTime;
/*     */   }
/*     */   
/*     */   public void afterMouseMove() {
/* 487 */     scheduleNarration(750L, false);
/*     */   }
/*     */   
/*     */   public void afterMouseAction() {
/* 491 */     scheduleNarration(200L, true);
/*     */   }
/*     */   
/*     */   public void afterKeyboardAction() {
/* 495 */     scheduleNarration(200L, true);
/*     */   }
/*     */   
/*     */   private boolean shouldRunNarration() {
/* 499 */     return (SharedConstants.DEBUG_UI_NARRATION || this.minecraft.getNarrator().isActive());
/*     */   }
/*     */   
/*     */   public void handleDelayedNarration() {
/* 503 */     if (shouldRunNarration()) {
/* 504 */       long currentTime = Util.getMillis();
/* 505 */       if (currentTime > this.nextNarrationTime && currentTime > this.narrationSuppressTime) {
/* 506 */         runNarration(true);
/* 507 */         this.nextNarrationTime = Long.MAX_VALUE;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void triggerImmediateNarration(boolean onlyChanged) {
/* 513 */     if (shouldRunNarration()) {
/* 514 */       runNarration(onlyChanged);
/*     */     }
/*     */   }
/*     */   
/*     */   private void runNarration(boolean onlyChanged) {
/* 519 */     this.narrationState.update(this::updateNarrationState);
/* 520 */     String narration = this.narrationState.collectNarrationText(!onlyChanged);
/* 521 */     if (!narration.isEmpty()) {
/* 522 */       this.minecraft.getNarrator().saySystemNow(narration);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean shouldNarrateNavigation() {
/* 527 */     return true;
/*     */   }
/*     */   
/*     */   protected void updateNarrationState(NarrationElementOutput output) {
/* 531 */     output.add(NarratedElementType.TITLE, getNarrationMessage());
/* 532 */     if (shouldNarrateNavigation()) {
/* 533 */       output.add(NarratedElementType.USAGE, USAGE_NARRATION);
/*     */     }
/* 535 */     updateNarratedWidget(output);
/*     */   }
/*     */   
/*     */   protected void updateNarratedWidget(NarrationElementOutput output) {
/* 539 */     List<? extends NarratableEntry> activeNarratables = this.narratables.stream()
/* 540 */       .flatMap(narratableEntry -> narratableEntry.getNarratables().stream())
/* 541 */       .filter(NarratableEntry::isActive)
/* 542 */       .sorted(Comparator.comparingInt(TabOrderedElement::getTabOrderGroup))
/* 543 */       .toList();
/*     */     
/* 545 */     NarratableSearchResult result = findNarratableWidget(activeNarratables, this.lastNarratable);
/* 546 */     if (result != null) {
/*     */       
/* 548 */       if (result.priority.isTerminal()) {
/* 549 */         this.lastNarratable = result.entry;
/*     */       }
/* 551 */       if (activeNarratables.size() > 1) {
/* 552 */         output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.screen", new Object[] { result.index + 1, activeNarratables.size() }));
/* 553 */         if (result.priority == NarratableEntry.NarrationPriority.FOCUSED) {
/* 554 */           output.add(NarratedElementType.USAGE, getUsageNarration());
/*     */         }
/*     */       } 
/* 557 */       result.entry.updateNarration(output.nest());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Component getUsageNarration() {
/* 562 */     return (Component)Component.translatable("narration.component_list.usage");
/*     */   }
/*     */   
/*     */   public static NarratableSearchResult findNarratableWidget(List<? extends NarratableEntry> narratableEntries, NarratableEntry lastNarratable) {
/* 566 */     NarratableSearchResult result = null;
/* 567 */     NarratableSearchResult lowPrioNarratable = null;
/* 568 */     for (int i = 0, narratablesSize = narratableEntries.size(); i < narratablesSize; i++) {
/* 569 */       NarratableEntry narratable = narratableEntries.get(i);
/* 570 */       NarratableEntry.NarrationPriority priority = narratable.narrationPriority();
/* 571 */       if (priority.isTerminal()) {
/* 572 */         if (narratable == lastNarratable) {
/* 573 */           lowPrioNarratable = new NarratableSearchResult(narratable, i, priority);
/*     */         } else {
/* 575 */           return new NarratableSearchResult(narratable, i, priority);
/*     */         } 
/* 577 */       } else if (priority.compareTo((result != null) ? (Enum)result.priority : (Enum)NarratableEntry.NarrationPriority.NONE) > 0) {
/* 578 */         result = new NarratableSearchResult(narratable, i, priority);
/*     */       } 
/*     */     } 
/*     */     
/* 582 */     return (result != null) ? result : lowPrioNarratable;
/*     */   }
/*     */   
/*     */   public void updateNarratorStatus(boolean wasDisabled) {
/* 586 */     if (wasDisabled) {
/* 587 */       scheduleNarration(NARRATE_DELAY_NARRATOR_ENABLED, false);
/*     */     }
/*     */     
/* 590 */     if (this.narratorButton != null) {
/* 591 */       this.narratorButton.setValue(this.minecraft.options.narrator().get());
/*     */     }
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 596 */     return this.font;
/*     */   }
/*     */   
/*     */   public boolean showsActiveEffects() {
/* 600 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canInterruptWithAnotherScreen() {
/* 604 */     return shouldCloseOnEsc();
/*     */   }
/*     */   public static final class NarratableSearchResult extends Record { private final NarratableEntry entry; private final int index; private final NarratableEntry.NarrationPriority priority;
/* 607 */     public NarratableSearchResult(NarratableEntry entry, int index, NarratableEntry.NarrationPriority priority) { this.entry = entry; this.index = index; this.priority = priority; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #607	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 607 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult; } public NarratableEntry entry() { return this.entry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #607	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #607	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;
/* 607 */       //   0	8	1	o	Ljava/lang/Object; } public int index() { return this.index; } public NarratableEntry.NarrationPriority priority() { return this.priority; }
/*     */      }
/*     */ 
/*     */   
/*     */   public ScreenRectangle getRectangle() {
/* 612 */     return new ScreenRectangle(0, 0, this.width, this.height);
/*     */   }
/*     */   
/*     */   public Music getBackgroundMusic() {
/* 616 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/Screen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */