package scripts.CookingInAlkharid;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import scripts.Task;

import java.io.IOException;

/**
 * Open bank and deposit everything.
 */
public class TaskBankingDepositCookedFood extends Task<ClientContext> {


    final int RAW_FOOD_ID;
    ScriptCookInAlkharid mainPollScript;

    public TaskBankingDepositCookedFood(ClientContext ctx, int foodId, ScriptCookInAlkharid mainScript) {
        super(ctx);
        RAW_FOOD_ID = foodId;
        mainPollScript = mainScript; //needed to be able to tell the pollingScript to redo the task list... ugly
    }


    @Override
    public boolean activate() {
        return (ctx.inventory.select().id(RAW_FOOD_ID).count() == 0
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) <= 10)
                || ctx.bank.opened()
                /*&& !mainPollScript.isNeedToSetupTasksListAgain()*/;
    }


    @Override
    public void execute() throws IOException {
        if (ctx.bank.opened()){
            if(ctx.bank.select().id(RAW_FOOD_ID).count(true) == 0){
                System.out.println("Not enough raw food in bank! Switching to next type of food.");

                if(!mainPollScript.getAllFoodIds().isEmpty()) {
                    mainPollScript.getAllFoodIds().remove(0);
                    mainPollScript.setNeedToSetupTasksListAgain(true);
                }

            } else {
                System.out.println("banking");
                ctx.bank.depositInventory();
                Condition.wait(() -> ctx.inventory.select().size() == 0, 500, 5);
                Condition.sleep(Random.nextInt(300, 1500));
                ctx.bank.withdraw(RAW_FOOD_ID, Bank.Amount.ALL);
                Condition.sleep(Random.nextInt(300, 1500));
                ctx.bank.close();
                Condition.sleep(Random.nextInt(100, 600));
            }
        } else{
            ctx.bank.open();
            System.out.println("Opening bank");
            Condition.wait(ctx.bank::opened,800,5);
        }
    }
}
