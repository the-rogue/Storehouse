
package therogue.storehouse.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class TileTickingRegistry {
	
	public static final TileTickingRegistry INSTANCE = new TileTickingRegistry();
	private List<ITickableModule> CURRENT_TICKERS = new ArrayList<>();
	private List<ITickableModule> DORMANT_TICKERS = new ArrayList<>();
	private int tick;
	
	public void tick (TickEvent.ServerTickEvent event) {
		if (event.phase == Phase.END) return;
		CURRENT_TICKERS.forEach(t -> t.update());
		if (((tick == 39 ? tick = 0 : ++tick) % 40) != 0) return;
		Stream<ITickableModule> tickers1 = Stream.concat(CURRENT_TICKERS.stream(), DORMANT_TICKERS.stream());
		Stream<ITickableModule> tickers2 = Stream.concat(CURRENT_TICKERS.stream(), DORMANT_TICKERS.stream());
		CURRENT_TICKERS = tickers1.filter(t -> t.stillTicking() && !t.shouldRemove()).collect(Collectors.toList());
		DORMANT_TICKERS = tickers2.filter(t -> !t.stillTicking() && !t.shouldRemove()).collect(Collectors.toList());
	}
	
	public void registerTickingModule (ITickableModule tickModule) {
		if (CURRENT_TICKERS.contains(tickModule) || DORMANT_TICKERS.contains(tickModule)) return;
		CURRENT_TICKERS.add(tickModule);
	}
}
