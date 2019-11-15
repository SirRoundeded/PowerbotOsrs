package scripts.CookingInAlkharid;


import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.AntipatternWalker;


public class TaskWalkerRangeToBank extends AntipatternWalker {

    public TaskWalkerRangeToBank(ClientContext ctx, Tile[] pathCoordinates, int foodId) {
        super(ctx, pathCoordinates);
        RAW_FOOD_ID = foodId;
    }

    final int RAW_FOOD_ID;

    Area areaAlkharidAndRange = new Area(
            new Tile(3260, 3155, 0),
            new Tile(3260, 3188, 0),
            new Tile(3282, 3188, 0),
            new Tile(3282, 3155, 0)
);

    Area areaDoor = new Area(
            new Tile(3271, 3183, 0),
            new Tile(3271, 3176, 0),
            new Tile(3278, 3176, 0),
            new Tile(3278, 3183, 0)

    );
    final int CLOSED_DOOR_ID = 1535;

    @Override
    public boolean activate() {
        return areaAlkharidAndRange.containsOrIntersects(ctx.players.local())
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 10
                && ctx.inventory.select().id(RAW_FOOD_ID).count() == 0
                && !ctx.objects.select(10).id(CLOSED_DOOR_ID).within(areaDoor).poll().valid();

    }
}
