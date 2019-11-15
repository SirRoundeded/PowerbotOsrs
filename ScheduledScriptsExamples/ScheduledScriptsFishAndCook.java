package scripts.ScheduledScriptsExamples;

import org.powerbot.script.Script;
import scripts.BreakHandler.ScriptBreakHandler;
import scripts.BreakHandler.TaskLogout;
import scripts.CookingInAlkharid.ScriptCookInAlkharid;
import scripts.CookingInAlkharid.TaskBankingDepositCookedFood;
import scripts.FishingInAlkharid.ScriptFishingInAlkharid;
import scripts.FishingInAlkharid.TaskBankingDepositFish;
import scripts.ScheduleTaskEntry;
import scripts.ScriptScheduler;

@Script.Manifest(name="FishAndCookInAlkharid", description = "", properties = "topic=0; client=4;")


public class ScheduledScriptsFishAndCook extends ScriptScheduler {

    @Override
    public void start() {
        //example schedule, change as needed:

        //fish for 30min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptFishingInAlkharid(),TaskBankingDepositFish.class,1800000));
        //logout and wait for 3min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptBreakHandler(180000),TaskLogout.class, DEFAULT_VALUE_BREAK));
        //cook for 25min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptCookInAlkharid(),TaskBankingDepositCookedFood.class,1500000));
        //fish for 15min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptFishingInAlkharid(),TaskBankingDepositFish.class,900000));
        //logout and wait for 10min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptBreakHandler(600000),TaskLogout.class, DEFAULT_VALUE_BREAK));
        //cook for 30min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptCookInAlkharid(),TaskBankingDepositCookedFood.class,1800000));


        changeActiveScript(scheduleList.get(0));
    }
}
