package Client;

import java.io.PrintWriter;
import java.util.ArrayList;

public class shutDownThread extends Thread {

    ArrayList<PrintWriter> outputList;

    public shutDownThread(ArrayList<PrintWriter> outputList)
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

