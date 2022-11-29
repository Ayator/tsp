public class Edge implements Comparable<Edge> {
    // make the edge's vertices final
    private final Vertex u, v;
    private final float weight;// consider making it double

    public Vertex getU() {
        return u;
    }

    public Vertex getV() {
        return v;
    }

    public float getWeight() {
        return weight;
    }

    public Edge(Vertex u, Vertex v){
        this.u = u;
        this.v = v;
        this.weight = computeWeight(u, v);
        u.incrementDegree();
        v.incrementDegree();
    }

    public static float computeWeight(Vertex a, Vertex b){
        float deltaX = a.getX() - b.getX();
        float deltaY = a.getY() - b.getY();
        return deltaX * deltaX + deltaY * deltaY;
    }

    public static double computeDoubleWeight(Vertex a, Vertex b){
        double deltaX = (double) a.getX() - (double) b.getX();
        double deltaY = (double) a.getY() - (double) b.getY();
        return deltaX * deltaX + deltaY * deltaY;
    }

    public String toString(){
        return getU().toString() + " to " + getV().toString() + " : " + getWeight();
    }

    @Override
    public int compareTo(Edge other) {
        float delta = this.getWeight() - other.getWeight();
        if(delta > 0) return 1;
        else if(delta < 0) return -1;
        else{
            int deltaU = this.getU().compareTo(other.getU());
            int deltaV = this.getV().compareTo(other.getV());
            if(deltaU > 0) return 1;
            else if(deltaU < 0) return -1;
            else if(deltaV > 0) return 1;
            else if(deltaV < 0) return -1;
            else return 0;
        }
        
    }
}
