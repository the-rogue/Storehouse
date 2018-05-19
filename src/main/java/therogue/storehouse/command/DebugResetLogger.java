/*
 * This file is part of Storehouse. Copyright (c) 2016, TheRogue, All rights reserved.
 * 
 * Storehouse is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Storehouse is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Storehouse. If not, see <http://www.gnu.org/licenses/gpl>.
 */

package therogue.storehouse.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import therogue.storehouse.LOG;


public class DebugResetLogger extends CommandBase
{
	public static final DebugResetLogger INSTANCE = new DebugResetLogger();

	private DebugResetLogger()
	{

	}
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    
	@Override
	public String getName()
	{
		return "rlogger";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return new TextComponentTranslation("command.storehouse:rlogger.usage").getFormattedText();
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		LOG.updateSetTimes(args[0], Integer.parseInt(args[1]));
	}
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
    	ArrayList<String> options = new ArrayList<String>();
    	if (args.length == 0){
    		for (String s : LOG.getkeys()){
    			options.add(s);
    		}
    		return options;
    	}
        return Collections.<String>emptyList();
    }
}
