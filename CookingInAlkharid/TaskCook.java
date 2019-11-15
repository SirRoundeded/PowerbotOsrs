package scripts.CookingInAlkharid;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import scripts.Task;

import java.io.IOException;

/**
 * Cook the current type of raw food.
 */
public class TaskCook extends Task<ClientContext> {


    final int RANGE_ID = 26181;
    final int RAW_FOOD_ID;
    final int CLOSED_DOOR_ID = 1535;
    int idleCounter = 0;
    int idleCap = 10;


    Area areaAkharidAndRange = new Area(
            new Tile(3265, 3161, 0),
            new Tile(3265, 3183, 0),
            new Tile(3279, 3183, 0),
            new Tile(3279, 3161, 0)
            );

    Area areaRangeBuilding = new Area(
            new Tile(3271, 3183, 0),
            new Tile(3271, 3179, 0),
            new Tile(3275, 3179, 0),
            new Tile(3275, 3183, 0)
    );

    Area areaApproachRange = new Area(
            new Tile(3275, 3179, 0),
            new Tile(3275, 3181, 0),
            new Tile(3273, 3181, 0),
            new Tile(3273, 3179, 0)
    );

    Area areaDoor = new Area(
            new Tile(3271, 3183, 0),
            new Tile(3271, 3176, 0),
            new Tile(3278, 3176, 0),
            new Tile(3278, 3183, 0)

    );

    public TaskCook(ClientContext ctx, int foodId) {
        super(ctx);
        RAW_FOOD_ID = foodId;
    }

    @Override
    public boolean activate() {
        return areaAkharidAndRange.containsOrIntersects(ctx.players.local())
                && areaRangeBuilding.containsOrIntersects(ctx.players.local())
                && ctx.inventory.select().id(RAW_FOOD_ID).count() > 0
                && !ctx.objects.select(10).id(CLOSED_DOOR_ID).within(areaDoor).poll().valid(); //to distinguish between door-opening task
    }

    @Override
    public void execute() throws IOException {

        //System.out.println("Animation: "+ctx.players.local().animation());
        System.out.println("Idle counter: "+idleCounter);

        //idle counter counts up, when ever animation is -1
        if(ctx.players.local().animation() == -1 && !ctx.players.local().inMotion()) {
            idleCounter++;
        } else{
            idleCounter = 0;
        }

        if(areaApproachRange.containsOrIntersects(ctx.players.local())){
            idleCounter = 1000; //just a big number, that is over the idle cap, to skip the waiting time when entering building
        }

        //if player was idle for too long, start cooking again
        if((ctx.players.local().animation() == -1 && idleCounter >= idleCap)) {
            GameObject range = ctx.objects.select(10).id(RANGE_ID).poll();
            range.interact("Cook");

            idleCounter = 0;
            Condition.sleep(Random.nextInt(500, 1500));
        }
        if(ctx.widgets.widget(270).component(14).visible()) {
            ctx.widgets.widget(270).component(14).click();
            idleCap = Random.nextInt(5,10);
            Condition.sleep(Random.nextInt(500, 2000));
        }

    }
}
