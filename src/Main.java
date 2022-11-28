import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        // runTSP("tsp_100_6");
        LinkedHashMap<String, Float> goals = getGoals();
        LinkedHashMap<String, Boolean> goalsBeaten = getGoalsBeaten(goals);
        for (String filename : goals.keySet()) {
            int n;
            if(goalsBeaten.get(filename)){
                System.out.println(filename + " beaten.");
                n = 0;
            }
            else{
                System.out.println(filename + " not beaten.");
                n = 1;
            }

        //     System.out.println("Running " + filename);
        //     for (int i = 0; i < n; i++) {
        //         runTSP(filename);
        //     }
        //     // runTSP(filename);
        //     System.out.println("Done.");
        //     System.out.println("");
        }
    }

    public static void runTSP(String filename){
        String inputPath = "../input/" + filename;
        String outputPath = "../output/" + filename;
        String bestOutputPath = "../bestOutput/" + filename;

        CompleteGraph graph = CompleteGraph.readGraphFromFile(inputPath);
        if(graph == null)
            return;
        MinimumSpanningTree mst = MinimumSpanningTree.KruskalAlgorithm(graph);
        // mst.printEdges();
        Vertex[] oddDegreeVertices = mst.getOddDegVertices();
        // this doesn't guarantee <= 1.5 optimal solution because it's greedy...
        BipartiteGraph odd = BipartiteGraph.GreedyMinWeightPerfectMatching(graph, oddDegreeVertices);
        // odd.printPerfectMatchingEdges();

        Multigraph multigraph = Multigraph.CombineBipartiteAndMST(mst, odd);
        // multigraph.printEdges();
        DoublyLinkedList<Vertex> eulerianPath = Multigraph.HierholzerAlgorithm(multigraph);
        // eulerianPath.printList();
        HamiltonianCycle hamiltonianCycle = HamiltonianCycle.FromEulerianPath(eulerianPath);
        TwoOpt.Apply(hamiltonianCycle);
        ThreeOpt.Apply(hamiltonianCycle);
        // hamiltonianCycle.printList();
        saveToFile(hamiltonianCycle, outputPath, bestOutputPath);
    }

    public static void saveToFile(HamiltonianCycle tour, String outputPath, String bestOutputPath){
        try {
            // save to output
            FileWriter output = new FileWriter(outputPath);
            float tourCost = tour.getTourCost();
            String tourString = tour.getTourString();
            output.write(tourCost + "\n" + tourString);
            output.close();
            // System.out.println("Tour: " + tourString);
            System.out.println("Tour cost: " + tourCost);
            // check if the best output file already exists
            File bestOutput = new File(bestOutputPath);
            if(bestOutput.exists() && !bestOutput.isDirectory()){
                // if it exists then check if the total cost is lower
                Scanner sc = new Scanner(bestOutput);
                float bestCost = sc.nextFloat();
                sc.close();
                // then copy it
                if(tourCost < bestCost)
                    Files.copy(Path.of(outputPath), Path.of(bestOutputPath), StandardCopyOption.REPLACE_EXISTING);
            }
            else {
                // if it doesn't exist just copy it
                Files.copy(Path.of(outputPath), Path.of(bestOutputPath));
            }
        } catch (IOException e) {
            System.out.println("Error while outputting the file.");
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<String, Boolean> getGoalsBeaten(LinkedHashMap<String, Float> goals){
        LinkedHashMap<String, Boolean> goalsBeaten = new LinkedHashMap<>(77);
        try {
            for (String filename : goals.keySet()) {
                String bestOutputPath = "../bestOutput/" + filename;
                File bestOutput = new File(bestOutputPath);
                if(bestOutput.exists() && !bestOutput.isDirectory()){
                    // if it exists then check if the best cost is lower than the goal
                    Scanner sc = new Scanner(bestOutput);
                    float bestCost = sc.nextFloat();
                    sc.close();
                    float goalCost = goals.get(filename);
                    goalsBeaten.put(filename, bestCost <= goalCost + 0.01f);
                    if(goalsBeaten.get(filename))
                        System.out.println(filename + " beaten: " + bestCost + " < " + goalCost);
                    else
                        System.out.println(filename + " not beaten: " + bestCost + " > " + goalCost);
                }
                else {
                    // if it doesn't exist just put false
                    goalsBeaten.put(filename, false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return goalsBeaten;
    }

    public static LinkedHashMap<String, Float> getGoals(){
        LinkedHashMap<String, Float> goals = new LinkedHashMap<>(77);
        goals.put("tsp_5_1", 4f);
        goals.put("tsp_8_1", 13.9508f);
        goals.put("tsp_51_1", 437.577f);
        goals.put("tsp_70_1", 693.704f);
        goals.put("tsp_76_1", 563.911f);
        goals.put("tsp_76_2", 110542f);
        goals.put("tsp_99_1", 1257.74f);
        goals.put("tsp_100_1", 22013.3f);
        goals.put("tsp_100_2", 22334.4f);
        goals.put("tsp_100_3", 21995.3f);
        goals.put("tsp_100_4", 21743.5f);
        goals.put("tsp_100_5", 22876.8f);
        goals.put("tsp_100_6", 8347.1f);
        goals.put("tsp_101_1", 658.708f);
        goals.put("tsp_105_1", 14869.2f);
        goals.put("tsp_107_1", 45229.8f);
        goals.put("tsp_124_1", 61238.1f);
        goals.put("tsp_127_1", 126883f);
        goals.put("tsp_136_1", 103478f);
        goals.put("tsp_144_1", 61179.7f);
        goals.put("tsp_150_1", 27176.1f);
        goals.put("tsp_150_2", 26956f);
        goals.put("tsp_152_1", 74255.8f);
        goals.put("tsp_159_1", 43375.7f);
        goals.put("tsp_195_1", 2435.67f);
        goals.put("tsp_198_1", 16515.8f);
        goals.put("tsp_200_1", 31541.7f);
        goals.put("tsp_200_2", 30559.7f);
        goals.put("tsp_225_1", 135918f);
        goals.put("tsp_226_1", 87935.5f);
        goals.put("tsp_262_1", 2504.23f);
        goals.put("tsp_264_1", 56142.4f);
        goals.put("tsp_299_1", 49906f);
        goals.put("tsp_318_1", 45047.3f);
        goals.put("tsp_318_2", 44395f);
        goals.put("tsp_400_1", 16249.1f);
        goals.put("tsp_417_1", 12972.4f);
        goals.put("tsp_439_1", 117150f);
        goals.put("tsp_442_1", 54041.1f);
        goals.put("tsp_493_1", 37343.9f);
        goals.put("tsp_574_1", 40000.1f);
        goals.put("tsp_575_1", 7292.92f);
        goals.put("tsp_654_1", 39607.2f);
        goals.put("tsp_657_1", 52477.6f);
        goals.put("tsp_724_1", 44981.4f);
        goals.put("tsp_783_1", 9660.55f);
        goals.put("tsp_1000_1", 20647700f);// 3-opt works fine till here
        
        goals.put("tsp_1060_1", 247086f);//3
        goals.put("tsp_1084_1", 263421f);

        goals.put("tsp_1173_1", 62699f);
        goals.put("tsp_1291_1", 60009.7f);
        goals.put("tsp_1304_1", 297464f);
        goals.put("tsp_1323_1", 318667f);

        goals.put("tsp_1379_1", 60846.9f);

        goals.put("tsp_1400_1", 24147.8f);
        goals.put("tsp_1432_1", 169970f);
        goals.put("tsp_1577_1", 27299.8f);
        goals.put("tsp_1655_1", 72707.3f);
        goals.put("tsp_1748_1", 378852f);
        goals.put("tsp_1817_1", 67363.4f);
        goals.put("tsp_1889_1", 377287f);
        goals.put("tsp_2103_1", 89860.9f);
        goals.put("tsp_2152_1", 75714f);
        goals.put("tsp_2319_1", 261672f);
        goals.put("tsp_2392_1", 378063f);//3
        goals.put("tsp_3038_1", 160642f);
        goals.put("tsp_3795_1", 35093.3f);
        goals.put("tsp_4461_1", 211369f);
        goals.put("tsp_5915_1", 688904f);
        goals.put("tsp_5934_1", 685560f);
        goals.put("tsp_7397_1", 27700000f);
        goals.put("tsp_9432_1", 65994.6f);// 2-opt works fine till here

        // goals.put("tsp_11849_1", 1120000f);// not tested
        // goals.put("tsp_14051_1", 574617f);
        // goals.put("tsp_18512_1", 790940f);
        // goals.put("tsp_33810_1", 78500000f);
        // goals.put("tsp_85900_1", 163000000f);
        return goals;
    }
}