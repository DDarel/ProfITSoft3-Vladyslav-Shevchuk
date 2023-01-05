package org.example.task1;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static org.example.task1.ViolationStat.FeeCounter;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        int numOfThreads = 4;
        int numOfFiles = 11;
        int difference = numOfFiles % numOfThreads;
        int min = numOfFiles / numOfThreads;
        int startYear = 2013;
        int endYear = 2023;

        ExecutorService es = Executors.newFixedThreadPool(numOfThreads);
        ArrayList<HashMap<ViolationsList, Violation>> listOfYears  = new ArrayList<>();
        CompletableFuture<HashMap<ViolationsList, Violation>> compfut = new CompletableFuture<>();
        final long startTime = System.currentTimeMillis();

        while(startYear <= endYear){
            if (difference!=0){
                int finalStartYear = startYear;
                compfut = CompletableFuture.supplyAsync(()-> {
                    try {
                        //System.out.println(finalStartYear + " : " + (finalStartYear+min));
                        return FeeCounter(finalStartYear, finalStartYear + min);
                    } catch (ParserConfigurationException | IOException | SAXException e) {
                        throw new RuntimeException(e);
                    }
                });
                listOfYears.add(compfut.get());
                difference--;
                startYear += (min + 1);
            } else {
                int finalStartYear = startYear;
                compfut = CompletableFuture.supplyAsync(()-> {
                    try {
                        //System.out.println(finalStartYear + " : " + (finalStartYear+min-1));
                        return FeeCounter(finalStartYear, finalStartYear + min - 1);
                    } catch (ParserConfigurationException | IOException | SAXException e) {
                        throw new RuntimeException(e);
                    }
                });
                listOfYears.add(compfut.get());
                startYear += min;
            }
        }

        final long endTime = System.currentTimeMillis();
        //System.out.println("Total execution time: " + (endTime - startTime));

        HashMap<ViolationsList, Violation> result = new HashMap<>();
        for(var value : listOfYears){
            for (Map.Entry<ViolationsList, Violation> pair: value.entrySet()){
                if(!result.containsKey(pair.getKey())){
                    result.put(pair.getKey(), pair.getValue());
                } else {
                    result.put(pair.getKey(), result.get(pair.getKey()).setNewAmounts(pair.getValue().getMaxValueFee(), pair.getValue().getSumOfFees()));
                }
            }
        }
        String path = "src/main/resources/output.txt";
        FileWriter out = new FileWriter(path);

        int size = result.size();
        for (int i = 0; i < size; i++){
            float max = 0;
            float sum = 0;
            ViolationsList type = ViolationsList.UNDEFINED;
            for (Map.Entry<ViolationsList, Violation> pair: result.entrySet()) {
                if (pair.getValue().getMaxValueFee() > max){
                    max = pair.getValue().getMaxValueFee();
                    sum = pair.getValue().getSumOfFees();
                    type = pair.getKey();
                }
            }
            out.write("#" + (i+1) + " Violation: " + type + " Sum:" + sum + " MaxFee:"+ max + "\n");
            result.remove(type);
        }

        out.close();
        es.shutdown();
    }
}