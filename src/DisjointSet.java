import java.util.HashMap;
import java.util.Map;

public class DisjointSet<T> {
    private Map<T, T> parent = new HashMap<>();
    private Map<T, Integer> rank = new HashMap<>();

    public void MakeSets(T[] all){
        for(T t : all){
            parent.put(t, t);
            rank.put(t, 0);
        }
    }

    public T FindSet(T t){
        T p = parent.get(t);
        // check if t is not it's own root
        if(p != t){
            // compress it
            parent.put(t, FindSet(p));
        }
        // since the parent was updated recursively, it is found
        return parent.get(t);
    }

    public void UnionSet(T t1, T t2){
        // check if t1 and t2 are already in the same set
        T root1 = FindSet(t1);
        T root2 = FindSet(t2);
        if(root1 == root2)
            return;

        // attach shorter tree under the root of the deeper tree
        int rank1 = rank.get(root1);
        int rank2 = rank.get(root2);
        if(rank1 > rank2)
            parent.put(root2, root1);
        else if(rank1 < rank2)
            parent.put(root1, root2);
        else{
            parent.put(root1, root2);
            rank.put(root2, rank2 + 1);
        }
    }
}
