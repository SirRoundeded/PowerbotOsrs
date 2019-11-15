package scripts.FishingInAlkharid;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import scripts.Task;

import java.io.IOException;

/**
 * Catches shrimp/anchovies.
 */
public class TaskCatchFishNet extends Task<ClientContext> {

    final String FISHING_SPOT_NAME = "Fishing spot";
    //int[] FISH_IDS;
    int idleCounter = 0;
    int idleCap = 1;

    Area areaApproachFishingSpots = new Area(
            new Tile(3268, 3146, 0),
            new Tile(3279, 3146, 0));

    public TaskCatchFishNet(ClientContext ctx/*, int[] fishIds*/) {
        super(ctx);
        //this.FISH_IDS = fishIds;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().size() < 28
                && ctx.npcs.select().name(FISHING_SPOT_NAME).nearest().poll().tile().distanceTo(ctx.players.local()) <= 10;
    }

    @Override
    public void execute() throws IOException {

        System.out.println("Idle Counter: "+idleCounter);

        //idle counter counts up, when ever animation is -1
        if(ctx.players.local().animation() == -1
                && !ctx.players.local().inMotion()){
            idleCounter++;
        } else{
            idleCounter = 0;
        }

        //if player was idle for too long, start fishing again
        if(idleCounter >= idleCap) {
            Npc fishingSpot = ctx.npcs.select().name(FISHING_SPOT_NAME).nearest().poll();
            if (!ctx.game.inViewport(fishingSpot.centerPoint())) {
                ctx.camera.turnTo(fishingSpot);
                Condition.sleep(Random.nextInt(200,1000));
            }

            fishingSpot.interact("Net",FISHING_SPOT_NAME);
            idleCounter = 0;
            idleCap = Random.nextInt(4,8);
        }

        if(areaApproachFishingSpots.containsOrIntersects(ctx.players.local())){
            idleCounter = 1000; //some big number to be over the cap and make bot fish immediately when running from bank to fishingspots
        }
        Condition.sleep(Random.nextInt(300,500));

    }
}
