package therogue.storehouse;

import therogue.storehouse.proxy.IProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = storehouse.MODID, name = storehouse.NAME, version = storehouse.VERSION, acceptedMinecraftVersions = storehouse.MCVERSIONS, useMetadata = storehouse.useMetadata)
public class storehouse
{
    public static final String MODID = "storehouse";
    public static final String NAME = "Storehouse";
    public static final String VERSION = "1.7.2-1.0";
    public static final String MCVERSIONS = "[1.10.2]";
    public static final boolean useMetadata = true;
    
    @Instance
    public static storehouse instance;
    
    @SidedProxy(clientSide = "therogue.storehouse.proxy.ClientProxy", serverSide = "therogue.storehouse.proxy.ServerProxy")
    public static IProxy proxy;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
    }
}
