package scripts.BreakHandler;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import scripts.Task;
import scripts.UtilityMethods;

import java.io.IOException;

public class TaskLogout extends Task<ClientContext> {

    UtilityMethods util = new UtilityMethods(ctx);

    public TaskLogout(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.game.loggedIn();
    }

    @Override
    public void execute() throws IOException {

        if(ctx.bank.opened()){
            ctx.bank.close();
        } else {

            util.logOut();
            Condition.sleep(Random.nextInt(2000, 3000));
        }
    }
}
