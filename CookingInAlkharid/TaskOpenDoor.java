package scripts.CookingInAlkharid;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import scripts.Task;

import java.io.IOException;


/**
 * Open door, in case someone closed it
 */
public class TaskOpenDoor extends Task<ClientContext> {


    Area areaDoor = new Area(
            new Tile(3271, 3183, 0),
            new Tile(3271, 3176, 0),
            new Tile(3278, 3176, 0),
            new Tile(3278, 3183, 0)

    );
    final int CLOSED_DOOR_ID = 1535;
    final int RAW_FOOD_ID;

    public TaskOpenDoor(ClientContext ctx, int foodId) {
        super(ctx);
        RAW_FOOD_ID = foodId;
    }

    @Override
    public boolean activate() {
        return ctx.objects.select(10).id(CLOSED_DOOR_ID).within(areaDoor).poll().valid()
                && areaDoor.containsOrIntersects(ctx.players.local())
                && (ctx.inventory.select().id(RAW_FOOD_ID).count() == 28 || ctx.inventory.select().id(RAW_FOOD_ID).count() == 0);
    }

    @Override
    public void execute() throws IOException { //todo: open door missclicks
        GameObject doorClosed = ctx.objects.select(10).id(CLOSED_DOOR_ID).within(areaDoor).poll();
        Tile correctedDoorTile = new Tile(3276,3180,0);
        ctx.input.move(correctedDoorTile.matrix(ctx).centerPoint());
        MenuCommand[] menuEntries = ctx.menu.commands();

        if(menuEntries[0].toString().contains("Open")){
            ctx.input.click(true);
        } else {
            ctx.input.move(doorClosed.centerPoint());
            Condition.sleep(Random.nextInt(500,700));
            menuEntries = ctx.menu.commands();

            if(menuEntries[0].toString().contains("Open")){
                ctx.input.click(true);
            } else {
                ctx.camera.turnTo(doorClosed);
            }
        }

        Condition.sleep(Random.nextInt(500,1000));
        Condition.wait(() -> !ctx.players.local().inMotion());


    }


}
