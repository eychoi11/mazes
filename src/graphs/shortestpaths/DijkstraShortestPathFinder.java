package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {

        Map<V, Double> distTo = new HashMap<>();

        ExtrinsicMinPQ<V> pq = createMinPQ();
        Map<V, E> edgeTo = new HashMap<>();

        // Map<V, E> spt = new HashMap<>();
        if (Objects.equals(start, end)) {
            return edgeTo;
        }

        distTo.put(start, 0.0);
        pq.add(start, 0);
        // for (E edge : graph.outgoingEdgesFrom(start)) {
        //     distTo.put(edge.to(), Double.POSITIVE_INFINITY);
        //     if (!pq.contains(edge.to())) {
        //         pq.add(edge.to(), edge.weight());
        //         edgeTo.put(edge.to(), edge);
        //     } else {
        //         double oldWeight = edgeTo.get(edge.to()).weight();
        //         double newWeight = edge.weight();
        //         if (newWeight < oldWeight) {
        //             pq.changePriority(edge.to(), edge.weight());
        //             edgeTo.put(edge.to(), edge);
        //         }
        //     }
        // }

        while (!pq.isEmpty()) {
            V v = pq.removeMin();
            if (Objects.equals(v, end)) {
                break;
            }
            // if (!spt.containsKey(v)) {
            //     spt.put(v, edgeTo.get(v));
            // }

            for (E edge : graph.outgoingEdgesFrom(v)) {
                V vertex = edge.to();
                if (!distTo.containsKey(vertex)) {
                    distTo.put(vertex, Double.POSITIVE_INFINITY);
                }

                double oldDist = distTo.get(vertex);
                double newDist = distTo.get(v) + edge.weight();
                if (newDist < oldDist) {
                    distTo.put(vertex, newDist);
                    edgeTo.put(vertex, edge);
                    if (!pq.contains(vertex)) {
                        pq.add(vertex, edge.weight() + distTo.get(v));
                    } else {
                        pq.changePriority(vertex, edge.weight() + distTo.get(v));
                    }
                }




            }


            // for (E edge : graph.outgoingEdgesFrom(v)) {
            //     V vertex = edge.to();
            //     if (!spt.containsKey(vertex) && !vertex.equals(start)) {
            //         if (!distTo.containsKey(vertex)) {
            //             distTo.put(vertex, Double.POSITIVE_INFINITY);
            //         }
            //         if (pq.contains(vertex)) {
            //             pq.changePriority(vertex, edge.weight() + distTo.get(v));
            //         } else {
            //             pq.add(vertex, edge.weight() + distTo.get(v));
            //         }
            //     }
            //     double oldDist = distTo.get(vertex);
            //     double newDist = distTo.get(v) + edge.weight();
            //     if (newDist < oldDist) {
            //         distTo.put(vertex, newDist);
            //         if (!spt.containsKey(edge.to())) {
            //             spt.put(vertex, edge);
            //         }
            //     }
            // }
        }
        // System.out.println(spt);    // test output
        return edgeTo;

    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {

        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }
        List<E> finalPath = new ArrayList<>();
        V vertex = end;
        while (!vertex.equals(start)) {
            E e = spt.get(vertex);
            finalPath.add(e);
            vertex = e.from();
        }
        java.util.Collections.reverse(finalPath);
        // System.out.println(finalPath);  // test output
        // System.out.println(spt);    // test output
        return new ShortestPath.Success<V, E>(finalPath);
    }
}
