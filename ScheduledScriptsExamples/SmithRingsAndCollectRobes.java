package scripts.ScheduledScriptsExamples;

        import org.powerbot.script.Script;
        import scripts.BreakHandler.ScriptBreakHandler;
        import scripts.BreakHandler.TaskLogout;
        import scripts.CollectMonkRobes.CollectMonkRobes;
        import scripts.CollectMonkRobes.TaskBankingDepositEverything;
        import scripts.ScheduleTaskEntry;
        import scripts.ScriptScheduler;
        import scripts.SmithGoldRings.SmithGoldRings;
        import scripts.SmithGoldRings.TaskBankingDepositRings;

@Script.Manifest(name="SmithAndCollectRobes", description = "", properties = "topic=0; client=4;")


public class SmithRingsAndCollectRobes extends ScriptScheduler {

    @Override
    public void start() { //todo: test this


        scheduleList.add(new ScheduleTaskEntry<>(new CollectMonkRobes(),TaskBankingDepositEverything.class,1800000)); //30min
        scheduleList.add(new ScheduleTaskEntry<>(new SmithGoldRings(),TaskBankingDepositRings.class,1150700)); //20min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptBreakHandler(605100),TaskLogout.class, DEFAULT_VALUE_BREAK)); //10min
        scheduleList.add(new ScheduleTaskEntry<>(new SmithGoldRings(),TaskBankingDepositRings.class,2603000)); //45min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptBreakHandler(130000),TaskLogout.class, DEFAULT_VALUE_BREAK)); //2min
        scheduleList.add(new ScheduleTaskEntry<>(new SmithGoldRings(),TaskBankingDepositRings.class,2086001)); //35min
        scheduleList.add(new ScheduleTaskEntry<>(new ScriptBreakHandler(190500),TaskLogout.class, DEFAULT_VALUE_BREAK)); //3min
        scheduleList.add(new ScheduleTaskEntry<>(new SmithGoldRings(),TaskBankingDepositRings.class,1877000)); //30min

        changeActiveScript(scheduleList.get(0));
    }
}