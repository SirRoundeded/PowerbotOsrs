package scripts.CookingInAlkharid;

import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.Task;
import scripts.TaskbasedPollingScript;
import scripts.UtilityMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Takes raw food out of al-kharid bank, walkes to range, cookes raw food, walkes back, deposits everything.
 * Start at Al-kharid bank with empty inventory
 */
@Script.Manifest(name="CookInAlkharid", description = "Start at Al-kharid bank with empty inventory", properties = "topic=0; client=4;")


public class ScriptCookInAlkharid extends TaskbasedPollingScript<ClientContext>{


    Tile[] pathToRange = new Tile[]{
            new Tile(3270, 3168),
            new Tile(3270, 3168),
            new Tile(3271, 3167),
            new Tile(3272, 3167),
            new Tile(3273, 3167),
            new Tile(3274, 3168),
            new Tile(3275, 3169),
            new Tile(3276, 3170),
            new Tile(3276, 3171),
            new Tile(3276, 3172),
            new Tile(3276, 3173),
            new Tile(3276, 3174),
            new Tile(3276, 3175),
            new Tile(3276, 3176),
            new Tile(3276, 3177),
            new Tile(3276, 3178),
            new Tile(3277, 3179),
            new Tile(3277, 3180),
            new Tile(3276, 3180),
            new Tile(3275, 3180),
            new Tile(3274, 3180)

    };

    Tile[] pathToBank = new Tile[]{
            new Tile(3273, 3180),
            new Tile(3273, 3180),
            new Tile(3274, 3180),
            new Tile(3275, 3180),
            new Tile(3276, 3180),
            new Tile(3277, 3180),
            new Tile(3277, 3179),
            new Tile(3276, 3178),
            new Tile(3275, 3177),
            new Tile(3275, 3176),
            new Tile(3275, 3175),
            new Tile(3275, 3174),
            new Tile(3275, 3173),
            new Tile(3275, 3172),
            new Tile(3275, 3171),
            new Tile(3274, 3170),
            new Tile(3273, 3169),
            new Tile(3273, 3168),
            new Tile(3273, 3167),
            new Tile(3272, 3167),
            new Tile(3271, 3167),
            new Tile(3270, 3168),
            new Tile(3270, 3168),
            new Tile(3269, 3168)

    };

    //setup raw food ids
    int CURRENT_FOOD_ID; //321 for anchovies, 317 for shrimps
    List<Integer> allFoodIds = new ArrayList<>(Arrays.asList(317,321));

    boolean needToSetupTasksListAgain = false; //
    boolean doneCooking = false;


    public List<Integer> getAllFoodIds() {
        return allFoodIds;
    }

    @Override
    public void start(){
        System.out.println("Started cooking in Al-kharid.");
        UtilityMethods util = new UtilityMethods(ctx);
        util.startScriptCameraSettings();
        util.openInventory();

        doneCooking = false;
        taskList = new ArrayList<>();
        setupTaskList();

    }


    public void setNeedToSetupTasksListAgain(boolean needToSetupTasksListAgain) {
        this.needToSetupTasksListAgain = needToSetupTasksListAgain;
    }

    public boolean isNeedToSetupTasksListAgain() {
        return needToSetupTasksListAgain;
    }

    @Override
    public void poll() {

        for (Task task : taskList){
            if (task.activate() && !needToSetupTasksListAgain){
                
                    task.execute();
            }
        }

        //if there is no more raw food of the current type in bank, set up the task list with next raw food id; there is probably a better way to do this
        if(needToSetupTasksListAgain){
            setupTaskList();
        }
    }

    public boolean isDoneCooking() {
        return doneCooking;
    }

    public void setupTaskList(){
        if(!allFoodIds.isEmpty()) {
            CURRENT_FOOD_ID = allFoodIds.get(0);
            taskList = new ArrayList<>();
            taskList.addAll(Arrays.asList(
                    new TaskBankingDepositCookedFood(ctx, CURRENT_FOOD_ID, this),
                    new TaskWalkerBankToRange(ctx, pathToRange, CURRENT_FOOD_ID),
                    new TaskCook(ctx, CURRENT_FOOD_ID),
                    new TaskWalkerRangeToBank(ctx, pathToBank, CURRENT_FOOD_ID),
                    new TaskOpenDoor(ctx, CURRENT_FOOD_ID)));
            needToSetupTasksListAgain = false;
        } else{
            System.out.println("No more food in bank! Cooking script is idle!");
            doneCooking = true; //todo: not used, when there is no more raw food in bank, bot is just idle until it auto-logs or another task starts
        }
    }

    public void stop() {
        System.out.println("Stopped cooking in Al-kharid.");
    }
}



