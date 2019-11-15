package scripts.FishingInAlkharid;


import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.AntipatternWalker;

public class TaskWalkerBankToFish extends AntipatternWalker {

    public TaskWalkerBankToFish(ClientContext ctx, Tile[] pathCoordinates) {
        super(ctx, pathCoordinates);
    }

    final int FISHING_NET_ID = 303;
    final String FISHING_SPOT_NAME = "Fishing Spot";

    Area areaAlkharidBankToFish = new Area(
            new Tile(3260, 3174, 0),
            new Tile(3260, 3135, 0),
            new Tile(3282, 3135, 0),
            new Tile(3282, 3174, 0)
            );

    @Override
    public boolean activate() {
        return areaAlkharidBankToFish.containsOrIntersects(ctx.players.local())
                && ctx.inventory.select().id(FISHING_NET_ID).count() == 1
                && ctx.inventory.select().count() == 1
                && ctx.npcs.select().name(FISHING_SPOT_NAME).nearest().poll().tile().distanceTo(ctx.players.local()) > 10;

    }
}
