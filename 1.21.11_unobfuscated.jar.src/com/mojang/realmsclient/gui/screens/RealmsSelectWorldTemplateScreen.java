/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.WorldTemplate;
/*     */ import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.util.RealmsTextureManager;
/*     */ import com.mojang.realmsclient.util.TextRenderingUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsSelectWorldTemplateScreen extends RealmsScreen {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  46 */   private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
/*     */   
/*  48 */   private static final Component SELECT_BUTTON_NAME = (Component)Component.translatable("mco.template.button.select");
/*  49 */   private static final Component TRAILER_BUTTON_NAME = (Component)Component.translatable("mco.template.button.trailer");
/*  50 */   private static final Component PUBLISHER_BUTTON_NAME = (Component)Component.translatable("mco.template.button.publisher");
/*     */   
/*     */   private static final int BUTTON_WIDTH = 100;
/*     */   
/*  54 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private final Consumer<WorldTemplate> callback;
/*     */   
/*     */   private WorldTemplateList worldTemplateList;
/*     */   
/*     */   private final RealmsServer.WorldType worldType;
/*     */   private final List<Component> subtitle;
/*     */   private Button selectButton;
/*     */   private Button trailerButton;
/*     */   private Button publisherButton;
/*  65 */   private WorldTemplate selectedTemplate = null;
/*     */   
/*     */   private String currentLink;
/*     */   
/*     */   private List<TextRenderingUtils.Line> noTemplatesMessage;
/*     */   
/*     */   public RealmsSelectWorldTemplateScreen(Component title, Consumer<WorldTemplate> callback, RealmsServer.WorldType worldType, WorldTemplatePaginatedList alreadyFetched) {
/*  72 */     this(title, callback, worldType, alreadyFetched, List.of());
/*     */   }
/*     */   
/*     */   public RealmsSelectWorldTemplateScreen(Component title, Consumer<WorldTemplate> callback, RealmsServer.WorldType worldType, WorldTemplatePaginatedList alreadyFetched, List<Component> subtitle) {
/*  76 */     super(title);
/*  77 */     this.callback = callback;
/*  78 */     this.worldType = worldType;
/*  79 */     if (alreadyFetched == null) {
/*     */       
/*  81 */       this.worldTemplateList = new WorldTemplateList();
/*  82 */       fetchTemplatesAsync(new WorldTemplatePaginatedList(10));
/*     */     } else {
/*     */       
/*  85 */       this.worldTemplateList = new WorldTemplateList(Lists.newArrayList(alreadyFetched.templates()));
/*  86 */       fetchTemplatesAsync(alreadyFetched);
/*     */     } 
/*  88 */     this.subtitle = subtitle;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  93 */     Objects.requireNonNull(getFont()); this.layout.setHeaderHeight(33 + this.subtitle.size() * (9 + 4));
/*  94 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  95 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  96 */     header.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*  97 */     this.subtitle.forEach(warning -> header.addChild((LayoutElement)new StringWidget(header, this.font)));
/*     */     
/*  99 */     this.worldTemplateList = (WorldTemplateList)this.layout.addToContents((LayoutElement)new WorldTemplateList(this.worldTemplateList.getTemplates()));
/*     */     
/* 101 */     LinearLayout bottomButtons = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 102 */     bottomButtons.defaultCellSetting().alignHorizontallyCenter();
/* 103 */     this.trailerButton = (Button)bottomButtons.addChild((LayoutElement)Button.builder(TRAILER_BUTTON_NAME, button -> onTrailer()).width(100).build());
/* 104 */     this.selectButton = (Button)bottomButtons.addChild((LayoutElement)Button.builder(SELECT_BUTTON_NAME, button -> selectTemplate()).width(100).build());
/* 105 */     bottomButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).width(100).build());
/* 106 */     this.publisherButton = (Button)bottomButtons.addChild((LayoutElement)Button.builder(PUBLISHER_BUTTON_NAME, button -> onPublish()).width(100).build());
/*     */     
/* 108 */     updateButtonStates();
/*     */     
/* 110 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 111 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 116 */     this.worldTemplateList.updateSize(this.width, this.layout);
/* 117 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 122 */     List<Component> parts = Lists.newArrayListWithCapacity(2);
/* 123 */     parts.add(this.title);
/* 124 */     parts.addAll(this.subtitle);
/* 125 */     return CommonComponents.joinLines(parts);
/*     */   }
/*     */   
/*     */   private void updateButtonStates() {
/* 129 */     this.publisherButton.visible = (this.selectedTemplate != null && !this.selectedTemplate.link().isEmpty());
/* 130 */     this.trailerButton.visible = (this.selectedTemplate != null && !this.selectedTemplate.trailer().isEmpty());
/* 131 */     this.selectButton.active = (this.selectedTemplate != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 136 */     this.callback.accept(null);
/*     */   }
/*     */   
/*     */   private void selectTemplate() {
/* 140 */     if (this.selectedTemplate != null) {
/* 141 */       this.callback.accept(this.selectedTemplate);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onTrailer() {
/* 146 */     if (this.selectedTemplate != null && !this.selectedTemplate.trailer().isBlank()) {
/* 147 */       ConfirmLinkScreen.confirmLinkNow((Screen)this, this.selectedTemplate.trailer());
/*     */     }
/*     */   }
/*     */   
/*     */   private void onPublish() {
/* 152 */     if (this.selectedTemplate != null && !this.selectedTemplate.link().isBlank()) {
/* 153 */       ConfirmLinkScreen.confirmLinkNow((Screen)this, this.selectedTemplate.link());
/*     */     }
/*     */   }
/*     */   
/*     */   private void fetchTemplatesAsync(final WorldTemplatePaginatedList startPage) {
/* 158 */     new Thread("realms-template-fetcher")
/*     */       {
/*     */         public void run() {
/* 161 */           WorldTemplatePaginatedList page = startPage;
/* 162 */           RealmsClient client = RealmsClient.getOrCreate();
/* 163 */           while (page != null) {
/*     */             
/* 165 */             Either<WorldTemplatePaginatedList, Exception> result = RealmsSelectWorldTemplateScreen.this.fetchTemplates(page, client);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 190 */             page = RealmsSelectWorldTemplateScreen.this.minecraft.submit(() -> { if (result.right().isPresent()) { RealmsSelectWorldTemplateScreen.LOGGER.error("Couldn't fetch templates", result.right().get()); if (RealmsSelectWorldTemplateScreen.this.worldTemplateList.isEmpty()) RealmsSelectWorldTemplateScreen.this.noTemplatesMessage = TextRenderingUtils.decompose(I18n.get("mco.template.select.failure", new Object[0]), new TextRenderingUtils.LineSegment[0]);  return null; }  WorldTemplatePaginatedList currentPage = result.left().get(); for (WorldTemplate template : (Iterable<WorldTemplate>)currentPage.templates()) RealmsSelectWorldTemplateScreen.this.worldTemplateList.addEntry(template);  if (currentPage.templates().isEmpty()) { if (RealmsSelectWorldTemplateScreen.this.worldTemplateList.isEmpty()) { String withoutLink = I18n.get("mco.template.select.none", new Object[] { "%link" }); TextRenderingUtils.LineSegment link = TextRenderingUtils.LineSegment.link(I18n.get("mco.template.select.none.linkTitle", new Object[0]), CommonLinks.REALMS_CONTENT_CREATION.toString()); RealmsSelectWorldTemplateScreen.this.noTemplatesMessage = TextRenderingUtils.decompose(withoutLink, new TextRenderingUtils.LineSegment[] { link }); }  return null; }  return currentPage; }).join();
/*     */           } 
/*     */         }
/* 193 */       }.start();
/*     */   }
/*     */   
/*     */   private Either<WorldTemplatePaginatedList, Exception> fetchTemplates(WorldTemplatePaginatedList paginatedList, RealmsClient client) {
/*     */     try {
/* 198 */       return Either.left(client.fetchWorldTemplates(paginatedList.page() + 1, paginatedList.size(), this.worldType));
/* 199 */     } catch (RealmsServiceException e) {
/* 200 */       return Either.right(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 206 */     super.render(graphics, xm, ym, a);
/*     */     
/* 208 */     this.currentLink = null;
/*     */     
/* 210 */     if (this.noTemplatesMessage != null) {
/* 211 */       renderMultilineMessage(graphics, xm, ym, this.noTemplatesMessage);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderMultilineMessage(GuiGraphics graphics, int xm, int ym, List<TextRenderingUtils.Line> noTemplatesMessage) {
/* 216 */     for (int i = 0; i < noTemplatesMessage.size(); i++) {
/* 217 */       TextRenderingUtils.Line line = noTemplatesMessage.get(i);
/* 218 */       int lineY = row(4 + i);
/* 219 */       int lineWidth = line.segments.stream().mapToInt(s -> this.font.width(s.renderedText())).sum();
/* 220 */       int startX = this.width / 2 - lineWidth / 2;
/* 221 */       for (TextRenderingUtils.LineSegment segment : (Iterable<TextRenderingUtils.LineSegment>)line.segments) {
/* 222 */         int color = segment.isLink() ? -13408581 : -1;
/* 223 */         String text = segment.renderedText();
/* 224 */         graphics.drawString(this.font, text, startX, lineY, color);
/* 225 */         int endX = startX + this.font.width(text);
/* 226 */         if (segment.isLink() && xm > startX && xm < endX && ym > lineY - 3 && ym < lineY + 8) {
/* 227 */           graphics.setTooltipForNextFrame((Component)Component.literal(segment.getLinkUrl()), xm, ym);
/* 228 */           this.currentLink = segment.getLinkUrl();
/*     */         } 
/* 230 */         startX = endX;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private class WorldTemplateList extends ObjectSelectionList<Entry> {
/*     */     public WorldTemplateList() {
/* 237 */       this(java.util.Collections.emptyList());
/*     */     }
/*     */     
/*     */     public WorldTemplateList(Iterable<WorldTemplate> templates) {
/* 241 */       super(Minecraft.getInstance(), RealmsSelectWorldTemplateScreen.this.width, RealmsSelectWorldTemplateScreen.this.layout.getContentHeight(), RealmsSelectWorldTemplateScreen.this.layout.getHeaderHeight(), 46);
/* 242 */       templates.forEach(this::addEntry);
/*     */     }
/*     */     
/*     */     public void addEntry(WorldTemplate template) {
/* 246 */       addEntry((AbstractSelectionList.Entry)new RealmsSelectWorldTemplateScreen.Entry(template));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 251 */       if (RealmsSelectWorldTemplateScreen.this.currentLink != null) {
/* 252 */         ConfirmLinkScreen.confirmLinkNow((Screen)RealmsSelectWorldTemplateScreen.this, RealmsSelectWorldTemplateScreen.this.currentLink);
/* 253 */         return true;
/*     */       } 
/* 255 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSelected(RealmsSelectWorldTemplateScreen.Entry selected) {
/* 260 */       super.setSelected((AbstractSelectionList.Entry)selected);
/* 261 */       RealmsSelectWorldTemplateScreen.this.selectedTemplate = (selected == null) ? null : selected.template;
/* 262 */       RealmsSelectWorldTemplateScreen.this.updateButtonStates();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 267 */       return 300;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 271 */       return (getItemCount() == 0);
/*     */     }
/*     */     
/*     */     public List<WorldTemplate> getTemplates() {
/* 275 */       return (List<WorldTemplate>)children().stream().map(c -> c.template).collect(Collectors.toList());
/*     */     }
/*     */   }
/*     */   
/*     */   private class Entry extends ObjectSelectionList.Entry<Entry> {
/* 280 */     private static final WidgetSprites WEBSITE_LINK_SPRITES = new WidgetSprites(
/* 281 */         Identifier.withDefaultNamespace("icon/link"), 
/* 282 */         Identifier.withDefaultNamespace("icon/link_highlighted"));
/*     */     
/* 284 */     private static final WidgetSprites TRAILER_LINK_SPRITES = new WidgetSprites(
/* 285 */         Identifier.withDefaultNamespace("icon/video_link"), 
/* 286 */         Identifier.withDefaultNamespace("icon/video_link_highlighted"));
/*     */ 
/*     */     
/* 289 */     private static final Component PUBLISHER_LINK_TOOLTIP = (Component)Component.translatable("mco.template.info.tooltip");
/* 290 */     private static final Component TRAILER_LINK_TOOLTIP = (Component)Component.translatable("mco.template.trailer.tooltip");
/*     */     
/*     */     public final WorldTemplate template;
/*     */     
/*     */     private ImageButton websiteButton;
/*     */     private ImageButton trailerButton;
/*     */     
/*     */     public Entry(WorldTemplate template) {
/* 298 */       this.template = template;
/* 299 */       if (!template.link().isBlank()) {
/* 300 */         this
/*     */           
/* 302 */           .websiteButton = new ImageButton(15, 15, WEBSITE_LINK_SPRITES, ConfirmLinkScreen.confirmLink((Screen)RealmsSelectWorldTemplateScreen.this, template.link()), PUBLISHER_LINK_TOOLTIP);
/*     */         
/* 304 */         this.websiteButton.setTooltip(Tooltip.create(PUBLISHER_LINK_TOOLTIP));
/*     */       } 
/* 306 */       if (!template.trailer().isBlank()) {
/* 307 */         this
/*     */           
/* 309 */           .trailerButton = new ImageButton(15, 15, TRAILER_LINK_SPRITES, ConfirmLinkScreen.confirmLink((Screen)RealmsSelectWorldTemplateScreen.this, template.trailer()), TRAILER_LINK_TOOLTIP);
/*     */         
/* 311 */         this.trailerButton.setTooltip(Tooltip.create(TRAILER_LINK_TOOLTIP));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 317 */       RealmsSelectWorldTemplateScreen.this.selectedTemplate = this.template;
/* 318 */       RealmsSelectWorldTemplateScreen.this.updateButtonStates();
/* 319 */       if (doubleClick && isFocused()) {
/* 320 */         RealmsSelectWorldTemplateScreen.this.callback.accept(this.template);
/*     */       }
/* 322 */       if (this.websiteButton != null) {
/* 323 */         this.websiteButton.mouseClicked(event, doubleClick);
/*     */       }
/* 325 */       if (this.trailerButton != null) {
/* 326 */         this.trailerButton.mouseClicked(event, doubleClick);
/*     */       }
/*     */       
/* 329 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 334 */       graphics.blit(RenderPipelines.GUI_TEXTURED, RealmsTextureManager.worldTemplate(this.template.id(), this.template.image()), getContentX() + 1, getContentY() + 1 + 1, 0.0F, 0.0F, 38, 38, 38, 38);
/* 335 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsSelectWorldTemplateScreen.SLOT_FRAME_SPRITE, getContentX(), getContentY() + 1, 40, 40);
/* 336 */       int padding = 5;
/* 337 */       int versionTextWidth = RealmsSelectWorldTemplateScreen.this.font.width(this.template.version());
/*     */       
/* 339 */       if (this.websiteButton != null) {
/* 340 */         this.websiteButton.setPosition(getContentRight() - versionTextWidth - this.websiteButton.getWidth() - 10, getContentY());
/* 341 */         this.websiteButton.render(graphics, mouseX, mouseY, a);
/*     */       } 
/* 343 */       if (this.trailerButton != null) {
/* 344 */         this.trailerButton.setPosition(getContentRight() - versionTextWidth - this.trailerButton.getWidth() * 2 - 15, getContentY());
/* 345 */         this.trailerButton.render(graphics, mouseX, mouseY, a);
/*     */       } 
/* 347 */       int textX = getContentX() + 45 + 20;
/* 348 */       int textY = getContentY() + 5;
/* 349 */       graphics.drawString(RealmsSelectWorldTemplateScreen.this.font, this.template.name(), textX, textY, -1);
/* 350 */       graphics.drawString(RealmsSelectWorldTemplateScreen.this.font, this.template.version(), getContentRight() - versionTextWidth - 5, textY, -6250336);
/* 351 */       Objects.requireNonNull(RealmsSelectWorldTemplateScreen.this.font); graphics.drawString(RealmsSelectWorldTemplateScreen.this.font, this.template.author(), textX, textY + 9 + 5, -6250336);
/* 352 */       if (!this.template.recommendedPlayers().isBlank()) {
/* 353 */         Objects.requireNonNull(RealmsSelectWorldTemplateScreen.this.font); graphics.drawString(RealmsSelectWorldTemplateScreen.this.font, this.template.recommendedPlayers(), textX, getContentBottom() - 9 / 2 - 5, -8355712);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 359 */       Component entryName = CommonComponents.joinLines(new Component[] {
/* 360 */             (Component)Component.literal(this.template.name()), 
/* 361 */             (Component)Component.translatable("mco.template.select.narrate.authors", new Object[] { this.template.author()
/* 362 */               }), (Component)Component.literal(this.template.recommendedPlayers()), 
/* 363 */             (Component)Component.translatable("mco.template.select.narrate.version", new Object[] { this.template.version() })
/*     */           });
/* 365 */       return (Component)Component.translatable("narrator.select", new Object[] { entryName });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsSelectWorldTemplateScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */