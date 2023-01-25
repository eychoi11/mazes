package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order

        List<E> chosenEdges = new ArrayList<>();

        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();

        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }

        for (int i = 0; i < edges.size(); i++) {
            E edge = edges.get(i);
            int set1 = disjointSets.findSet(edge.from());
            int set2 = disjointSets.findSet(edge.to());
            if (set1 != set2) {
                chosenEdges.add(edge);
                disjointSets.union(edge.from(), edge.to());
            }
        }

        MinimumSpanningTree<V, E> result = new MinimumSpanningTree.Success<>(chosenEdges);

        // theres gotta be a better way to do this
        int allSets = 0;
        for (V vertex : graph.allVertices()) {
            allSets = disjointSets.findSet(vertex);
            break;
        }
        for (V vertex : graph.allVertices()) {
            if (disjointSets.findSet(vertex) != allSets) {
                result = new MinimumSpanningTree.Failure<>();
            }
        }
        return result;
    }
}
