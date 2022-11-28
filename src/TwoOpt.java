public class TwoOpt {
    public static void Apply(HamiltonianCycle cycle){
        Vertex[] path = cycle.getPath();
        int n = path.length;
        boolean hasChanged = true;

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
                }
            }
        }
    }

    private static void reverse(Vertex[] path, int i, int j) {
        if(i >= j) return;
        Vertex t = path[i];
        path[i] = path[j];
        path[j] = t;
        reverse(path, i + 1, j - 1);
    }
}
