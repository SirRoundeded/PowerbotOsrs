package scripts.FishingInAlkharid;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import scripts.Task;
import scripts.UtilityMethods;

import java.io.IOException;

public class TaskDropFish extends Task<ClientContext> {


    int[] FISH_IDS;
    UtilityMethods util = new UtilityMethods(ctx);

    public TaskDropFish(ClientContext ctx, int[] fishIds) {
        super(ctx);
        FISH_IDS = fishIds;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().size() == 28 && hasFish();
    }

    @Override
    public void execute() throws IOException {
        for (int i = 0; i < FISH_IDS.length; i++) {
            System.out.println("Fish id: "+FISH_IDS[i]);
            System.out.println("Count of id "+FISH_IDS[i]+": "+ctx.inventory.select().id(FISH_IDS[i]).count());
            while(ctx.inventory.select().id(FISH_IDS[i]).count() > 0){

                util.shiftDropAntipattern(ctx.inventory.select().id(FISH_IDS[i]));
                Condition.sleep(Random.nextInt(500,1000));
            }

        }
    }

    public boolean hasFish(){
        for (int i = 0; i < FISH_IDS.length; i++) {
            if(ctx.inventory.select().id(FISH_IDS[i]).count() > 0){
                System.out.println("Has fish");
                return true;
            }
        }
        return false;
    }


}
