package app.anull.net.team_null;

import java.util.TimerTask;

/**
 * Created by Sean on 7/12/2017.
 */

public class Interrupt extends TimerTask {

    private boolean done;

    public Interrupt() {
        done = false;
    }

    @Override
    public void run() {
        //done = true;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
