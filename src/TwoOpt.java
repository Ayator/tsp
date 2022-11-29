import java.util.Random;

public class TwoOpt {
    public static void Apply(HamiltonianCycle cycle){
        Vertex[] path = cycle.getPath();
        int n = path.length;
        boolean hasChanged = true;

        int k = 0;
        while(hasChanged){
            hasChanged = false;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    // new edges - current edges
                    // j + 1 may overflow so use % to go the start of the path
                    float df = Edge.computeWeight(path[i], path[j])
                             + Edge.computeWeight(path[i + 1], path[(j + 1) % n]);
                    float di = Edge.computeWeight(path[i], path[i + 1])
                             + Edge.computeWeight(path[j], path[(j + 1) % n]);
                    if(df < di){
                        reverse(path, i + 1, j);
                        hasChanged = true;
                    }
                    k++;
                }
            }
        }
        // expected iterations is used to compute temperature in Simulated Annealing
        System.out.println("Iterations/Expected = " + k + "/" + (int)(n * n * Math.log(n) * 0.5d));
    }

    // recursion leads to stack overflow, chenged to dp
    private static void reverse(Vertex[] path, int i, int j) {
        while(i < j){
            Vertex t = path[i];
            path[i] = path[j];
            path[j] = t;
            i++;
            j--;
        }
    }

    public static void SimulatedAnnealing(HamiltonianCycle cycle){
        Vertex[] path = cycle.getPath();
        int n = path.length;

        Vertex[] bestPath = new Vertex[n];
        shallowCopyArray(path, bestPath);
        float bestTourWeight = HamiltonianCycle.getTourWeight(bestPath);
        
        boolean hasChanged = true;

        double oneOverKMax = 1d / (n * n * Math.log(n) * 0.5d);
        int k = 0;
        int initialTemperature = (int)getAverageWeight(path);
        Random r = new Random();
        int c = 0;

        while(hasChanged){
            hasChanged = false;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int temperature = getTemperature(k++, oneOverKMax, initialTemperature);
                    // new edges - current edges
                    // j + 1 may overflow so use % to go the start of the path
                    float df = Edge.computeWeight(path[i], path[j])
                             + Edge.computeWeight(path[i + 1], path[(j + 1) % n]);
                    float di = Edge.computeWeight(path[i], path[i + 1])
                             + Edge.computeWeight(path[j], path[(j + 1) % n]);
                    if(df < di){
                        reverse(path, i + 1, j);
                        hasChanged = true;
                        float tourWeight = HamiltonianCycle.getTourWeight(path);
                        if(tourWeight < bestTourWeight){
                            shallowCopyArray(path, bestPath);
                            bestTourWeight = tourWeight;
                        }
                    }
                    else if(r.nextDouble() * Math.exp((df - di) / temperature) < 1d){
                        reverse(path, i + 1, j);
                        hasChanged = true;
                        c++;
                    }
                }
            }
        }
        System.out.println("Simulated Annealing: " + c + "/" + k + " times uphill.");
        // if the best weight is less than the final weight, replace the tour and apply TwoOpt to it again
        if(bestTourWeight < HamiltonianCycle.getTourWeight(path)){
            shallowCopyArray(bestPath, path);
            TwoOpt.Apply(cycle);
            System.out.println("2-opt restarted.");
        }
    }

    // returns temperature as a function of current iteration, expected number of iterations, and initial temperature
    // decreasing the first factor (10) will increase the number of times gone uphill
    private static int getTemperature(int k, double oneOverKMax, int initialTemperature){
        return (int)(initialTemperature / Math.exp(10 * k * oneOverKMax));
    }

    private static <T> void shallowCopyArray(T[] from, T[] to){
        if(from.length != to.length) return;
        for (int i = 0; i < to.length; i++) {
            to[i] = from[i];
        }
    }

    private static double getAverageWeight(Vertex[] vertices){
        Vertex u = vertices[0];
        double averageWeight = 0d;
        for (int i = 1; i <= vertices.length; i++) {
            Vertex v = vertices[i % vertices.length];
            averageWeight += Edge.computeDoubleWeight(u, v);
            u = v;
        }
        return averageWeight / vertices.length;
    }
}
