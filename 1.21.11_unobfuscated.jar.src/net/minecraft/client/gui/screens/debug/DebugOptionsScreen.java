/*     */ package net.minecraft.client.gui.screens.debug;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.floats.FloatComparators;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.debug.DebugEntryCategory;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntry;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntryStatus;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenProfile;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class DebugOptionsScreen
/*     */   extends Screen {
/*  43 */   private static final Component TITLE = (Component)Component.translatable("debug.options.title");
/*  44 */   private static final Component SUBTITLE = (Component)Component.translatable("debug.options.warning").withColor(-2142128);
/*  45 */   private static final Component ENABLED_TEXT = (Component)Component.translatable("debug.entry.always");
/*  46 */   private static final Component IN_OVERLAY_TEXT = (Component)Component.translatable("debug.entry.overlay");
/*  47 */   private static final Component DISABLED_TEXT = CommonComponents.OPTION_OFF;
/*  48 */   private static final Component NOT_ALLOWED_TOOLTIP = (Component)Component.translatable("debug.options.notAllowed.tooltip");
/*  49 */   private static final Component SEARCH = (Component)Component.translatable("debug.options.search").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*  51 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
/*     */   
/*     */   private OptionList optionList;
/*     */   
/*     */   private EditBox searchBox;
/*  56 */   private final List<Button> profileButtons = new ArrayList<>();
/*     */   
/*     */   public DebugOptionsScreen() {
/*  59 */     super(TITLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  64 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(8));
/*     */     
/*  66 */     this.optionList = new OptionList();
/*  67 */     int optionListWidth = this.optionList.getRowWidth();
/*     */     
/*  69 */     LinearLayout title = LinearLayout.horizontal().spacing(8);
/*  70 */     title.addChild((LayoutElement)new SpacerElement(optionListWidth / 3, 1));
/*  71 */     title.addChild((LayoutElement)new StringWidget(TITLE, this.font), title.newCellSettings().alignVerticallyMiddle());
/*  72 */     this.searchBox = new EditBox(this.font, 0, 0, optionListWidth / 3, 20, this.searchBox, SEARCH);
/*  73 */     this.searchBox.setResponder(value -> this.optionList.updateSearch(value));
/*  74 */     this.searchBox.setHint(SEARCH);
/*  75 */     title.addChild((LayoutElement)this.searchBox);
/*  76 */     header.addChild((LayoutElement)title, LayoutSettings::alignHorizontallyCenter);
/*     */     
/*  78 */     header.addChild((LayoutElement)new MultiLineTextWidget(SUBTITLE, this.font).setMaxWidth(optionListWidth).setCentered(true), LayoutSettings::alignHorizontallyCenter);
/*     */     
/*  80 */     this.layout.addToContents((LayoutElement)this.optionList);
/*     */     
/*  82 */     LinearLayout bottomButtons = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  83 */     addProfileButton(DebugScreenProfile.DEFAULT, bottomButtons);
/*  84 */     addProfileButton(DebugScreenProfile.PERFORMANCE, bottomButtons);
/*  85 */     bottomButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(60).build());
/*     */     
/*  87 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  88 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBlurredBackground(GuiGraphics graphics) {
/*  93 */     this.minecraft.gui.renderDebugOverlay(graphics);
/*  94 */     super.renderBlurredBackground(graphics);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  99 */     setInitialFocus((GuiEventListener)this.searchBox);
/*     */   }
/*     */   
/*     */   private void addProfileButton(DebugScreenProfile profile, LinearLayout bottomButtons) {
/* 103 */     Button profileButton = Button.builder((Component)Component.translatable(profile.translationKey()), button -> {
/*     */           this.minecraft.debugEntries.loadProfile(profile);
/*     */           this.minecraft.debugEntries.save();
/*     */           this.optionList.refreshEntries();
/*     */           for (Button listButton : this.profileButtons) {
/*     */             listButton.active = true;
/*     */           }
/*     */           profile.active = false;
/* 111 */         }).width(120).build();
/* 112 */     profileButton.active = !this.minecraft.debugEntries.isUsingProfile(profile);
/* 113 */     this.profileButtons.add(profileButton);
/* 114 */     bottomButtons.addChild((LayoutElement)profileButton);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 119 */     this.layout.arrangeElements();
/* 120 */     if (this.optionList != null) {
/* 121 */       this.optionList.updateSize(this.width, this.layout);
/*     */     }
/*     */   }
/*     */   
/*     */   public OptionList getOptionList() {
/* 126 */     return this.optionList;
/*     */   }
/*     */   public class OptionList extends ContainerObjectSelectionList<AbstractOptionEntry> { private static final Comparator<Map.Entry<Identifier, DebugScreenEntry>> COMPARATOR; private static final int ITEM_HEIGHT = 20;
/*     */     static {
/* 130 */       COMPARATOR = ((o1, o2) -> {
/*     */           int byCategory = FloatComparators.NATURAL_COMPARATOR.compare(((DebugScreenEntry)o1.getValue()).category().sortKey(), ((DebugScreenEntry)o2.getValue()).category().sortKey());
/*     */           return (byCategory != 0) ? byCategory : ((Identifier)o1.getKey()).compareTo((Identifier)o2.getKey());
/*     */         });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OptionList() {
/* 140 */       super(Minecraft.getInstance(), DebugOptionsScreen.this.width, DebugOptionsScreen.this.layout.getContentHeight(), DebugOptionsScreen.this.layout.getHeaderHeight(), 20);
/* 141 */       updateSearch("");
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 146 */       super.renderWidget(graphics, mouseX, mouseY, a);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 151 */       return 350;
/*     */     }
/*     */     
/*     */     public void refreshEntries() {
/* 155 */       children().forEach(DebugOptionsScreen.AbstractOptionEntry::refreshEntry);
/*     */     }
/*     */     
/*     */     public void updateSearch(String value) {
/* 159 */       clearEntries();
/* 160 */       List<Map.Entry<Identifier, DebugScreenEntry>> all = new ArrayList<>(DebugScreenEntries.allEntries().entrySet());
/* 161 */       all.sort(COMPARATOR);
/* 162 */       DebugEntryCategory currentCategory = null;
/* 163 */       for (Map.Entry<Identifier, DebugScreenEntry> entry : all) {
/* 164 */         if (((Identifier)entry.getKey()).getPath().contains(value)) {
/* 165 */           DebugEntryCategory newCategory = ((DebugScreenEntry)entry.getValue()).category();
/* 166 */           if (!newCategory.equals(currentCategory)) {
/* 167 */             addEntry((AbstractSelectionList.Entry)new DebugOptionsScreen.CategoryEntry(newCategory.label()));
/* 168 */             currentCategory = newCategory;
/*     */           } 
/* 170 */           addEntry((AbstractSelectionList.Entry)new DebugOptionsScreen.OptionEntry(entry.getKey()));
/*     */         } 
/*     */       } 
/* 173 */       notifyListUpdated();
/*     */     }
/*     */     
/*     */     private void notifyListUpdated() {
/* 177 */       refreshScrollAmount();
/* 178 */       DebugOptionsScreen.this.triggerImmediateNarration(true);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static abstract class AbstractOptionEntry extends ContainerObjectSelectionList.Entry<AbstractOptionEntry> {
/*     */     public abstract void refreshEntry();
/*     */   }
/*     */   
/*     */   private class CategoryEntry extends AbstractOptionEntry {
/*     */     private final Component category;
/*     */     
/*     */     public CategoryEntry(Component category) {
/* 190 */       this.category = category;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 195 */       graphics.drawCenteredString(DebugOptionsScreen.this.minecraft.font, this.category, getContentX() + getContentWidth() / 2, getContentY() + 5, -1);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 200 */       return (List<? extends GuiEventListener>)ImmutableList.of();
/*     */     }
/*     */     
/*     */     public List<? extends NarratableEntry> narratables()
/*     */     {
/* 205 */       return (List<? extends NarratableEntry>)ImmutableList.of(new NarratableEntry()
/*     */           {
/*     */             public NarratableEntry.NarrationPriority narrationPriority() {
/* 208 */               return NarratableEntry.NarrationPriority.HOVERED;
/*     */             }
/*     */             
/*     */             public void updateNarration(NarrationElementOutput output)
/*     */             {
/* 213 */               output.add(NarratedElementType.TITLE, DebugOptionsScreen.CategoryEntry.this.category); } }); } public void refreshEntry() {} } class null implements NarratableEntry { public void updateNarration(NarrationElementOutput output) { output.add(NarratedElementType.TITLE, DebugOptionsScreen.CategoryEntry.this.category); }
/*     */ 
/*     */ 
/*     */     
/*     */     public NarratableEntry.NarrationPriority narrationPriority() {
/*     */       return NarratableEntry.NarrationPriority.HOVERED;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private class OptionEntry
/*     */     extends AbstractOptionEntry
/*     */   {
/*     */     private static final int BUTTON_WIDTH = 60;
/*     */     private final Identifier location;
/* 228 */     protected final List<AbstractWidget> children = Lists.newArrayList();
/*     */     private final CycleButton<Boolean> always;
/*     */     private final CycleButton<Boolean> overlay;
/*     */     private final CycleButton<Boolean> never;
/*     */     private final String name;
/*     */     private final boolean isAllowed;
/*     */     
/*     */     public OptionEntry(Identifier location) {
/* 236 */       this.location = location;
/* 237 */       DebugScreenEntry entry = DebugScreenEntries.getEntry(location);
/* 238 */       this.isAllowed = (entry != null && entry.isAllowed(DebugOptionsScreen.this.minecraft.showOnlyReducedInfo()));
/* 239 */       String name = location.getPath();
/*     */       
/* 241 */       if (this.isAllowed) {
/* 242 */         this.name = name;
/*     */       } else {
/* 244 */         this.name = String.valueOf(ChatFormatting.ITALIC) + String.valueOf(ChatFormatting.ITALIC);
/*     */       } 
/*     */       
/* 247 */       this
/*     */ 
/*     */         
/* 250 */         .always = CycleButton.booleanBuilder((Component)DebugOptionsScreen.ENABLED_TEXT.copy().withColor(-2142128), (Component)DebugOptionsScreen.ENABLED_TEXT.copy().withColor(-4539718), false).displayOnlyValue().withCustomNarration(this::narrateButton).create(10, 5, 60, 16, (Component)Component.literal(name), (button, newValue) -> setValue(location, DebugScreenEntryStatus.ALWAYS_ON));
/* 251 */       this
/*     */ 
/*     */         
/* 254 */         .overlay = CycleButton.booleanBuilder((Component)DebugOptionsScreen.IN_OVERLAY_TEXT.copy().withColor(-171), (Component)DebugOptionsScreen.IN_OVERLAY_TEXT.copy().withColor(-4539718), false).displayOnlyValue().withCustomNarration(this::narrateButton).create(10, 5, 60, 16, (Component)Component.literal(name), (button, newValue) -> setValue(location, DebugScreenEntryStatus.IN_OVERLAY));
/* 255 */       this
/*     */ 
/*     */         
/* 258 */         .never = CycleButton.booleanBuilder((Component)DebugOptionsScreen.DISABLED_TEXT.copy().withColor(-1), (Component)DebugOptionsScreen.DISABLED_TEXT.copy().withColor(-4539718), false).displayOnlyValue().withCustomNarration(this::narrateButton).create(10, 5, 60, 16, (Component)Component.literal(name), (button, newValue) -> setValue(location, DebugScreenEntryStatus.NEVER));
/* 259 */       this.children.add(this.never);
/* 260 */       this.children.add(this.overlay);
/* 261 */       this.children.add(this.always);
/*     */       
/* 263 */       refreshEntry();
/*     */     }
/*     */     
/*     */     private MutableComponent narrateButton(CycleButton<Boolean> booleanCycleButton) {
/* 267 */       DebugScreenEntryStatus status = DebugOptionsScreen.this.minecraft.debugEntries.getStatus(this.location);
/* 268 */       MutableComponent current = Component.translatable("debug.entry.currently." + status.getSerializedName(), new Object[] { this.name });
/* 269 */       return CommonComponents.optionNameValue((Component)current, booleanCycleButton.getMessage());
/*     */     }
/*     */     
/*     */     private void setValue(Identifier location, DebugScreenEntryStatus never) {
/* 273 */       DebugOptionsScreen.this.minecraft.debugEntries.setStatus(location, never);
/* 274 */       for (Button profileButton : DebugOptionsScreen.this.profileButtons) {
/* 275 */         profileButton.active = true;
/*     */       }
/* 277 */       refreshEntry();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 282 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 287 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 292 */       int x = getContentX();
/* 293 */       int y = getContentY();
/* 294 */       graphics.drawString(DebugOptionsScreen.this.minecraft.font, this.name, x, y + 5, this.isAllowed ? -1 : -8355712);
/*     */       
/* 296 */       int buttonsStartX = x + getContentWidth() - this.never.getWidth() - this.overlay.getWidth() - this.always.getWidth();
/*     */       
/* 298 */       if (!this.isAllowed && hovered && mouseX < buttonsStartX) {
/* 299 */         graphics.setTooltipForNextFrame(DebugOptionsScreen.NOT_ALLOWED_TOOLTIP, mouseX, mouseY);
/*     */       }
/*     */       
/* 302 */       this.never.setX(buttonsStartX);
/* 303 */       this.overlay.setX(this.never.getX() + this.never.getWidth());
/* 304 */       this.always.setX(this.overlay.getX() + this.overlay.getWidth());
/*     */       
/* 306 */       this.always.setY(y);
/* 307 */       this.overlay.setY(y);
/* 308 */       this.never.setY(y);
/*     */       
/* 310 */       this.always.render(graphics, mouseX, mouseY, a);
/* 311 */       this.overlay.render(graphics, mouseX, mouseY, a);
/* 312 */       this.never.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */ 
/*     */     
/*     */     public void refreshEntry() {
/* 317 */       DebugScreenEntryStatus status = DebugOptionsScreen.this.minecraft.debugEntries.getStatus(this.location);
/* 318 */       this.always.setValue((status == DebugScreenEntryStatus.ALWAYS_ON));
/* 319 */       this.overlay.setValue((status == DebugScreenEntryStatus.IN_OVERLAY));
/* 320 */       this.never.setValue((status == DebugScreenEntryStatus.NEVER));
/* 321 */       this.always.active = !((Boolean)this.always.getValue());
/* 322 */       this.overlay.active = !((Boolean)this.overlay.getValue());
/* 323 */       this.never.active = !((Boolean)this.never.getValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/debug/DebugOptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */