public class ThreeOpt {
    public static void Apply(HamiltonianCycle cycle){
        Vertex[] path = cycle.getPath();
        int n = path.length;
        boolean hasChanged;
        do {
            hasChanged = false;
            for (int i = 0; i < n; i++) {
                for (int j = i + 2; j < n; j++) {
                    for (int k = j + 2; k < n + (i > 0 ? 1 : 0); k++) {
                        if(reverseSegmentIfBetter(path, i, j, k))
                            hasChanged = true;
                    }
                }
            }
        } while(hasChanged);
    }

    private static boolean reverseSegmentIfBetter(Vertex[] path, int i, int j, int k) {
        int n = path.length;
        double[] d = new double[5];
        d[0] = Edge.computeDoubleWeight(path[Math.floorMod(i - 1, n)], path[i])
             + Edge.computeDoubleWeight(path[j - 1], path[j])
             + Edge.computeDoubleWeight(path[k - 1], path[k % n]);
        d[1] = Edge.computeDoubleWeight(path[Math.floorMod(i - 1, n)], path[j - 1])
             + Edge.computeDoubleWeight(path[i], path[j])
             + Edge.computeDoubleWeight(path[k - 1], path[k % n]);
        if(d[1] < d[0]){
            reverse(path, i, j - 1);
            return true;
        }
        d[2] = Edge.computeDoubleWeight(path[Math.floorMod(i - 1, n)], path[i])
             + Edge.computeDoubleWeight(path[j - 1], path[k - 1])
             + Edge.computeDoubleWeight(path[j], path[k % n]);
        if(d[2] < d[0]){
            reverse(path, j, k - 1);
            return true;
        }
        d[4] = Edge.computeDoubleWeight(path[k % n], path[i])
             + Edge.computeDoubleWeight(path[j - 1], path[j])
             + Edge.computeDoubleWeight(path[k - 1], path[Math.floorMod(i - 1, n)]);
        if(d[4] < d[0]){
            reverse(path, i, k - 1);
            return true;
        }
        d[3] = Edge.computeDoubleWeight(path[Math.floorMod(i - 1, n)], path[j])
             + Edge.computeDoubleWeight(path[k - 1], path[i])
             + Edge.computeDoubleWeight(path[j - 1], path[k % n]);
        if(d[3] < d[0]){
            swapChunks(path, i, j, k);
            return true;
        }
        return false;
    }

    private static void reverse(Vertex[] path, int i, int j) {
        if(i >= j) return;
        Vertex t = path[i];
        path[i] = path[j];
        path[j] = t;
        reverse(path, i + 1, j - 1);
    }

    private static void swapChunks(Vertex[] path, int i, int j, int k){
        Vertex[] ij = new Vertex[j - i];
        for (int l = i; l < j; l++)
            ij[l - i] = path[l];
        for (int l = 0; l < k - j; l++)
            path[i + l] = path[j + l];
        for (int l = 0; l < j - i; l++)
            path[i + k - j + l] = ij[l];
    }
}
