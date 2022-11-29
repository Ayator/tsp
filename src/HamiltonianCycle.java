public class HamiltonianCycle {
    Vertex[] path;

    public Vertex[] getPath() {
        return path;
    }

    // used to find TSP tour from Eulerian tour
    // doesn't include the starting vertex at the end of the cycle
    private HamiltonianCycle(DoublyLinkedList<Vertex> path){
        int size = path.convertToHamiltonianCycle();
        this.path = new Vertex[size];
        int i = 0;
        for (Vertex vertex : path) {
            this.path[i++] = vertex;
        }
    }

    private HamiltonianCycle(Vertex[] path){
        this.path = path;
    }

    // assumes the given list is an eulerian tour
    public static HamiltonianCycle FromEulerianPath(DoublyLinkedList<Vertex> path){
        return new HamiltonianCycle(path);
    }

    // assumes the given array is an eulerian tour
    public static HamiltonianCycle FromVertexArray(Vertex[] path){
        return new HamiltonianCycle(path);
    }

    public String getTourString(){
        String str = path[0].getVertexId() + "";
        for (int i = 1; i < path.length; i++) {
            str += " " + path[i].getVertexId();
        }
        return str;
    }

    public float getTourCost(){
        float tourCost = 0f;
        Vertex u = this.getPath()[0];
        int n = this.getPath().length;
        for (int i = 1; i <= n; i++) {
            Vertex v = this.getPath()[i % n];
            tourCost += Math.sqrt(Edge.computeWeight(u, v));
            u = v;
        }
        return tourCost;
    }

    public static float getTourWeight(Vertex[] vertices){
        float tourWeight = 0f;
        Vertex u = vertices[0];
        int n = vertices.length;
        for (int i = 1; i <= n; i++) {
            Vertex v = vertices[i % n];
            tourWeight += Edge.computeWeight(u, v);
            u = v;
        }
        return tourWeight;
    }

    public void printList(){
        System.out.println("Hamiltonian Cycle:");
        for (int i = 0; i < path.length; i++) {
            System.out.println("-> " + path[i].toString());
        }
    }
}
