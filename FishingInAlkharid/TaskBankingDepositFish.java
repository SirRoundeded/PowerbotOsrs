package scripts.FishingInAlkharid;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import scripts.Task;

import java.awt.*;
import java.io.IOException;

/**
 * Deposits everything except the small net.
 */
public class TaskBankingDepositFish extends Task<ClientContext> {

    public TaskBankingDepositFish(ClientContext ctx) {
        super(ctx);
    }


    final int FISHING_NET_ID = 303;


    @Override
    public boolean activate() {
        return ((ctx.inventory.select().size() != 1
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) <= 5)
                    || ctx.bank.opened())
                        /*&& ctx.inventory.select().id(FISHING_NET_ID).count() == 1*/;
    }


    @Override
    public void execute() throws IOException {
        if (ctx.bank.opened()){
            System.out.println("banking");
            ctx.bank.depositAllExcept(FISHING_NET_ID);
            if(ctx.inventory.select().id(FISHING_NET_ID).count() < 1){
                Condition.sleep(Random.nextInt(200,800));
                ctx.bank.withdraw(FISHING_NET_ID,1);
            }
            Condition.wait(() -> ctx.inventory.select().size() == 1,500,5);
            Condition.sleep(Random.nextInt(200,800));
            ctx.bank.close();
            Condition.sleep(Random.nextInt(300,500));
        } else{
                ctx.bank.open();
                System.out.println("Opening bank");
                Condition.wait(ctx.bank::opened, 800, 5);
        }
    }
}
