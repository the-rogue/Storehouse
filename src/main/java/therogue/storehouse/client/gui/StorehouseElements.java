
package therogue.storehouse.client.gui;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import therogue.storehouse.client.gui.element.ElementFadingProgressBar;
import therogue.storehouse.client.gui.element.ProgressHandler;

public class StorehouseElements {
	
	public static void register () {
		ElementFactory.addSubElement(ProgressHandler.class, "CRYSTALISER_PB", (tile, parameters) -> {
			if (parameters.size() != 4) throw new IllegalArgumentException("Wrong Number of parameters for the Crystaliser Progress Bar subbuilder:" + parameters.toString());
			return new ElementFadingProgressBar(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)), Integer.parseInt(parameters.get(2)), Integer.parseInt(parameters.get(3)), new Color(15, 26, 95)) {
				
				@Override
				public void drawBottomLayer (GuiBase gui, int mouseX, int mouseY, float progress) {
					super.drawBottomLayer(gui, mouseX, mouseY, progress);
					if (progress != 0.0F)
					{
						GlStateManager.color((FluidRegistry.WATER.getColor() >> 16 & 255) / 255, (FluidRegistry.WATER.getColor() >> 8 & 255)
								/ 255, (FluidRegistry.WATER.getColor() & 255) / 255);
						GuiHelper.drawFluid(new FluidStack(FluidRegistry.WATER, 1000), x, y, width, height);
						GlStateManager.color(becomeColour.getRed() / 255.0F, becomeColour.getGreen() / 255.0F, becomeColour.getBlue() / 255.0F, progress);
						GuiHelper.drawFluid(new FluidStack(FluidRegistry.WATER, 1000), x, y, width, height);
					}
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GuiHelper.bindTexture(gui, GuiHelper.makeStorehouseLocation("textures/gui/icons/crystaliserprogress.png"));
					gui.drawTexturedModalRect(x, y, width, height);
				}
			};
		});
	}
}
