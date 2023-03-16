package com.hainkiwanki.geneticsmod.gui.renderer;

import com.hainkiwanki.geneticsmod.GeneticsMod;
import com.hainkiwanki.geneticsmod.gui.menus.GeneIsolatorMenu;
import com.hainkiwanki.geneticsmod.gui.renderer.components.EnergyInfoArea;
import com.hainkiwanki.geneticsmod.mobdata.MobData;
import com.hainkiwanki.geneticsmod.tags.ModTags;
import com.hainkiwanki.geneticsmod.util.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.w3c.dom.Text;

import java.util.*;

public class GeneIsolatorScreen extends AbstractContainerScreen<GeneIsolatorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GeneticsMod.MOD_ID, "textures/gui/gene_isolator.png");
    private EnergyInfoArea energyInfoArea;

    private Button traitButton;

    private int gridHeight = 64;
    private int gridWidth = 48;
    private int gridSize = 8;
    private int gridOffsetX = 39;
    private int gridOffsetY = 64;

    private int gridSearchRows = 32;
    private int gridSearchCols = 32;
    private int gridSearchWidth = 131;
    private int gridSearchHeight = 76;

    private float maskPosX = 0;
    private float maskPosY = 0;

    private int currentTraitIndex = 0;

    private List<Character> dnaRandomChars;
    private TextComponent dnaRandomText;

    public GeneIsolatorScreen(GeneIsolatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight = 241;
        dnaRandomChars = new ArrayList<>();
        fillCharList();
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();

        int paddingOffset = 3;
        int maxWidth = 55 + paddingOffset;
        traitButton = new Button(leftPos + 36, topPos + 5, maxWidth, 20, new TextComponent("Trait"), (btn) -> {
            List<String> tags = menu.getSampleTags();
            if(tags != null) {
                currentTraitIndex++;
                currentTraitIndex = currentTraitIndex % tags.size();
                TranslatableComponent textComponent = new TranslatableComponent("tooltip.geneticsmod.genesampleitem." + tags.get(currentTraitIndex));
                int textWidth = font.width(textComponent) + paddingOffset;
                if(textWidth > maxWidth)
                    textWidth = maxWidth;
                this.traitButton.setWidth(maxWidth);
                this.traitButton.setMessage(textComponent);

            }
        });
        addRenderableWidget(traitButton);
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 21, y + 26, menu.blockEntity.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY + 73, 4210752);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);

    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(Utils.isMouseAboveArea(pMouseX, pMouseY, x, y, 6, 68, energyInfoArea.DEFAULT_WIDTH, energyInfoArea.DEFAULT_HEIGHT)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.hasFuel()) {
            blit(pPoseStack, x + 22, y + 107 - menu.getScaledFuelProgress(),
                    188, 55 - menu.getScaledFuelProgress(),
                    2, menu.getScaledFuelProgress());
        }
        this.blit(pPoseStack, x + 6, y + 68 + 39 - menu.getEnergyProgress(),
                176, 15 + 39 - menu.getEnergyProgress(),
                12, menu.getEnergyProgress());

        // drawGrid(pPoseStack, 97, 7, 5, 9);

        DrawSearchGrid();
    }

    private void DrawSearchGrid() {
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.getWindow().getGuiScale();
        // 38, 112 to the bottom left position of grid
        int xPos = (int)(scale * (leftPos + 38));
        int yPos = mc.getWindow().getHeight() - (int)(scale * topPos) - (int)(scale * imageHeight) + (int)(scale * 112);
        int width = (int)(scale * gridSearchWidth);
        int height = (int)(scale * gridSearchHeight);

        RenderSystem.enableScissor(xPos,  yPos, width, height);
        int textWidth = font.width(dnaRandomText)/ gridSearchRows;
        font.drawWordWrap(dnaRandomText, leftPos + 38 + (int)maskPosX, topPos + 53 + (int)maskPosY, textWidth, 0xFFFFFFFF);
        RenderSystem.disableScissor();
    }

    private int diffY;
    private int diffX;

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if(pButton == 0) {

            int textWidth = font.width(dnaRandomText) / gridSearchRows;
            diffX =  textWidth - gridSearchWidth;
            diffY = font.wordWrapHeight(dnaRandomText.toString(), textWidth) - gridSearchHeight;

            maskPosX += pDragX;
            // maskPosX = Mth.clamp(maskPosX, -diffX, 0);

            maskPosY += pDragY;
            //maskPosY = Mth.clamp(maskPosY, -diffY, 0);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            int xPos = (int)pMouseX - leftPos - gridOffsetX;
            int yPos = (int)pMouseY - topPos - gridOffsetY;
            boolean clickInGrid = Utils.isMouseAboveArea((int)pMouseX, (int)pMouseY, xPos, yPos,0, 0, gridWidth, gridHeight);
            if(clickInGrid){
                int row = xPos / gridSize;
                int col = yPos / gridSize;
            }
            //System.out.println("GUI Mouse Position: " + xPos + ", " + yPos);
            //System.out.println("Grid pos: " + row + ", " + col);
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private void fillCharList() {
        dnaRandomText = new TextComponent("");
        int max = gridSearchCols * gridSearchRows;
        List<Character> dnaLetters = Arrays.asList(new Character[]{'C', 'G', 'T', 'A'});
        Random rand = new Random();

        Component aComp = new TextComponent("A ").withStyle(ChatFormatting.BLUE);
        Component tComp = new TextComponent("T ").withStyle(ChatFormatting.GREEN);
        Component gComp = new TextComponent("G ").withStyle(ChatFormatting.RED);
        Component cComp = new TextComponent("C ").withStyle(ChatFormatting.YELLOW);

        for (int i = 0; i < max; i++) {
            Character cLetter = dnaLetters.get(rand.nextInt(4));
            dnaRandomChars.add(cLetter);
            if (cLetter == 'G') {
                dnaRandomText.append(gComp);
            } else if (cLetter == 'T') {
                dnaRandomText.append(tComp);
            } else if (cLetter == 'C') {
                dnaRandomText.append(cComp);
            } else if (cLetter == 'A') {
                dnaRandomText.append(aComp);
            }
        }
    }
}
