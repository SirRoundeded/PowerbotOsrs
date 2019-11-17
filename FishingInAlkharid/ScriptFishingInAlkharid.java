package scripts.FishingInAlkharid;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import scripts.Task;
import scripts.TaskbasedPollingScript;
import scripts.UtilityMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Fishes with net in al-kharid, banks fish. May get attack by lvl 14 scorpion, if under cb29.
 * Start at Al-Kharid bank with only a small net in inventory or bank.
 */
@Script.Manifest(name="FishingInAlkharid", description = "Start at Al-Kharid bank with small net in inventory or bank.", properties = "topic=0; client=4;")


public class ScriptFishingInAlkharid extends TaskbasedPollingScript<ClientContext> {


    final int FISH1_ID = 317;
    final int FISH2_ID = 321;

    int[] FISH_IDS = new int[]{FISH1_ID,FISH2_ID};

    Tile[] pathToFish = new Tile[]{
            new Tile(3270, 3166),
            new Tile(3270, 3166),
            new Tile(3271, 3166),
            new Tile(3272, 3166),
            new Tile(3273, 3166),
            new Tile(3273, 3165),
            new Tile(3273, 3164),
            new Tile(3273, 3163),
            new Tile(3273, 3162),
            new Tile(3273, 3161),
            new Tile(3273, 3160),
            new Tile(3273, 3159),
            new Tile(3273, 3158),
            new Tile(3273, 3157),
            new Tile(3273, 3156),
            new Tile(3273, 3155),
            new Tile(3272, 3154),
            new Tile(3272, 3153),
            new Tile(3271, 3152),
            new Tile(3271, 3151),
            new Tile(3271, 3150),
            new Tile(3271, 3149),
            new Tile(3270, 3148),
            new Tile(3269, 3147),
            new Tile(3270, 3146),
            new Tile(3270, 3146),
            new Tile(3271, 3145),
            new Tile(3272, 3144),
            new Tile(3273, 3143),
            new Tile(3274, 3142),
            new Tile(3274, 3142)
    };

    Tile[] pathToBank = new Tile[]{
            new Tile(3275, 3143),
            new Tile(3275, 3143),
            new Tile(3274, 3144),
            new Tile(3274, 3145),
            new Tile(3273, 3146),
            new Tile(3272, 3147),
            new Tile(3271, 3148),
            new Tile(3271, 3149),
            new Tile(3271, 3149),
            new Tile(3272, 3150),
            new Tile(3272, 3151),
            new Tile(3273, 3152),
            new Tile(3273, 3153),
            new Tile(3273, 3154),
            new Tile(3273, 3155),
            new Tile(3273, 3156),
            new Tile(3273, 3157),
            new Tile(3273, 3158),
            new Tile(3273, 3159),
            new Tile(3273, 3160),
            new Tile(3273, 3161),
            new Tile(3273, 3162),
            new Tile(3273, 3163),
            new Tile(3273, 3164),
            new Tile(3273, 3165),
            new Tile(3273, 3166),
            new Tile(3272, 3166),
            new Tile(3271, 3166),
            new Tile(3270, 3166)
    };

    final boolean dropping = false; //change this to drop or bank the fish

    @Override
    public void start(){
        System.out.println("Started Fishing in Al-kharid.");
        UtilityMethods util = new UtilityMethods(ctx);
        util.startScriptCameraSettings();
        util.openInventory();

        //taskList = new ArrayList<>();

        if(dropping) {
            taskList.addAll(Arrays.asList(
                    new TaskCatchFishNet(ctx),
                    new TaskDropFish(ctx, FISH_IDS)
            ));
        } else {
            taskList.addAll(Arrays.asList(
                    new TaskBankingDepositFish(ctx),
                    new TaskWalkerBankToFish(ctx,pathToFish),
                    new TaskCatchFishNet(ctx),
                    new TaskWalkerFishToBank(ctx,pathToBank)
            ));
        }
    }

    @Override
    public void poll() {


        for (Task task : taskList){
            if (task.activate()){

                    task.execute();
            }
        }

    }
    @Override
    public void stop() {
        System.out.println("Stopped Fishing in Al-kharid.");
    }


}

