package Server;

import java.io.PrintWriter;
import java.util.ArrayList;

public class ServershutDownThread extends Thread {

    ArrayList<PrintWriter> outputList;

    ServershutDownThread(ArrayList<PrintWriter> outputList)
    {

        super("shutDownThread");
        this.outputList = outputList;

    }

    @Override
    public void run() {

        for (PrintWriter o : outputList)
        {

            o.println("exit");

        }
        this.outputList.clear();

    }

}

