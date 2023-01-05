package org.example.task1;

import java.util.HashMap;
import java.util.concurrent.Callable;
import static org.example.task1.ViolationStat.*;

public class TreadWork implements Callable {
    public int yearStart = 0;
    public int yearEnd = 0;

    @Override
    public HashMap<ViolationsList, Violation> call() throws Exception {
        System.out.println("Started:" + Thread.currentThread().getName());
        HashMap<ViolationsList, Violation> temp = FeeCounter(yearStart, yearEnd);
        return temp;
    }
}
