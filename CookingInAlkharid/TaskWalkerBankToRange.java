package scripts.CookingInAlkharid;


import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.AntipatternWalker;

public class TaskWalkerBankToRange extends AntipatternWalker {

    final int RAW_FOOD_ID;

    public TaskWalkerBankToRange(ClientContext ctx, Tile[] pathCoordinates, int foodId) {
        super(ctx, pathCoordinates);
        RAW_FOOD_ID = foodId;
    }

    Area areaAlkharidAndRange = new Area(
            new Tile(3265, 3161, 0),
            new Tile(3265, 3183, 0),
            new Tile(3282, 3183, 0),
            new Tile(3282, 3161, 0)
);

    Area areaRangeBuilding = new Area(
            new Tile(3271, 3183, 0),
            new Tile(3271, 3179, 0),
            new Tile(3275, 3179, 0),
            new Tile(3275, 3183, 0)
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
                && !areaRangeBuilding.containsOrIntersects(ctx.players.local())
                && ctx.inventory.select().id(RAW_FOOD_ID).count() > 0
                && !ctx.objects.select(3).id(CLOSED_DOOR_ID).within(areaDoor).poll().valid();

    }
}
