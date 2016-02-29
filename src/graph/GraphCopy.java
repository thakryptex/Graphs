package graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class GraphCopy<Data extends Comparable, Weight extends Comparable> {

    public static void main(String[] args) throws FileNotFoundException {
        GraphCopy<String, Integer> graph = GraphCopy.readGraphFromFile("cities.txt");

        graph.problemA();

//        System.out.println("Cities: " + graph.vertices);
//        System.out.println("Roads:  " + graph.edges);
//        System.out.println();

//        graph.GraphCopy.graph.Vertex vertex = graph.vertices.get(0);

//        for (graph.GraphCopy.graph.Vertex vrtx: graph.vertices) {
//            System.out.println("City:       " + vrtx);
//            System.out.println("Adjacents: " + vrtx.adjacent());
//        }

//        System.out.print("Breadth First Search: ");
//        graph.BreadthFirstSearch(vertex);
//        System.out.print("Depth First Search:   ");
//        graph.DepthFirstSearch(vertex);
    }




    private List<Vertex> vertices = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public boolean addVertex(Data data) {
        return addVertex(new Vertex(data));
    }

    public boolean addVertex(Vertex vertex) {
        if (vertices.contains(vertex))
            return false;
        return vertices.add(vertex);
    }

    public boolean removeVertex(Vertex vertex) {
        edges.removeAll(vertex.incidents);
        return vertices.remove(vertex);
    }

    public boolean addEdge(Data from, Data to, Weight weight) {
        return addEdge(new Vertex(from), new Vertex(to), weight);
    }

    public boolean addEdge(Vertex from, Vertex to, Weight weight) {
        if (!(vertices.contains(from) && vertices.contains(to)))
            return false;
        Edge edge = new Edge(from, to, weight);
        vertices.get(vertices.indexOf(from)).incidents.add(edge);
        vertices.get(vertices.indexOf(to)).incidents.add(edge);
        return edges.add(edge);
    }

    public boolean removeEdge(Vertex from, Vertex to) {
        Edge removingEdge = from.incidents.stream().filter(edge -> edge.from.equals(from) && edge.to.equals(to)).findFirst().orElseGet(() -> null);
        vertices.get(vertices.indexOf(from)).incidents.remove(removingEdge);
        vertices.get(vertices.indexOf(to)).incidents.remove(removingEdge);
        return edges.remove(removingEdge);
    }

    public void visit(Vertex vertex) {
        System.out.print(vertex.data + " ");
    }

//    public void BreadthFirstSearch(graph.Vertex vertex) {
//        Set<graph.Vertex> visited = new HashSet<>();
//        Queue<graph.Vertex> queue = new ArrayDeque<>();
//
//        queue.add(vertex);
//
//        while (!queue.isEmpty()) {
//            graph.Vertex v = queue.remove();
//            if (!visited.contains(v)) {
//                visit(v);
//                visited.add(v);
//
//                List<graph.Vertex> adj = v.adjacent();
//                List<graph.Vertex> filter = adj.stream().filter(n -> !visited.contains(n)).collect(Collectors.toList());
//                filter.forEach(queue::add);
//            }
//        }
//
//        System.out.println();
//        visited.clear();
//    }
//
//    public void DepthFirstSearch(graph.Vertex vertex) {
//        List<graph.Vertex> visited = new ArrayList<>();
//        Stack<graph.Vertex> stack = new Stack<>();
//        stack.push(vertex);
//
//        while(!stack.isEmpty()) {
//            graph.Vertex v = stack.pop();
//            visit(v);
//            visited.add(v);
//            vertex.adjacent().stream().filter(neighbour -> !visited.contains(neighbour)).forEach(stack::push);
//        }
//
//        System.out.println();
//        visited.clear();
//    }

//    public void DepthFirstSearch(graph.Vertex vertex) {
//        visited = new ArrayList<>();
//        DFS(vertex);
//        visited.clear();
//    }
//
//    private void DFS(graph.Vertex vertex) {
//        visit(vertex);
//        for(graph.Vertex neighbour: vertex.adjacent()) {
//            if (!visited.contains(neighbour))
//                DFS(neighbour);
//        }
//    }






    public static GraphCopy<String, Integer> readGraphFromFile(String name) throws FileNotFoundException {
        GraphCopy<String, Integer> graph = new GraphCopy<>();
        Scanner scanner = new Scanner(new FileReader(name));
        String line1 = scanner.nextLine();
        String line2 = scanner.nextLine();
        scanner.close();
        Arrays.asList(line1.split(" ")).forEach(graph::addVertex);
        String[] edges = line2.split(" ");
        for (int i = 0; i < edges.length; i+=2) {
            graph.addEdge(edges[i], edges[i+1], 1);
        }
        return graph;
    }

    public void problemA() {
        String[] names = {"Elista-R", "Vladikavkaz-R"};
        StringBuilder sb = new StringBuilder();

        for (String s: names) {
            int index = vertices.indexOf(new Vertex((Data) s));
            GraphCopy.Vertex vertex = vertices.get(index);
            GraphCopy.Vertex[] list = new GraphCopy.Vertex[vertex.incidents.size()];
//            if (index != -1) {
//                for (graph.GraphCopy.graph.Vertex v: vertex.adjacent()) {
//
//                }
//            }
//
//                list = vertex.adjacent().toArray(list);
            HeapSort.sort(list);
            for (GraphCopy.Vertex v : list) {
                sb.append(v + " ");
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb.toString());
        }
    }









    class Vertex implements Comparable {
        Data data;
        List<Edge> incidents = new ArrayList<>();

        public Vertex(Data data) {
            this.data = data;
        }

        public Data getValue() {
            return data;
        }

        public List<Vertex> adjacent() {
            List<Vertex> adjacent = new ArrayList<>();
            incidents.forEach(edge -> adjacent.add(edge.from.equals(this) ? edge.to : edge.from));
            return adjacent;
        }

        @Override
        public String toString() {
            return data.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vertex vertex = (Vertex) o;

            return data != null ? data.equals(vertex.data) : vertex.data == null;
        }

        @Override
        public int compareTo(Object o) {
            if (o == null || getClass() != o.getClass()) throw new ClassCastException();
            Vertex v = (Vertex) o;
            if (data.equals(v.data))
                return 0;
            return data.compareTo(v.data) > 0 ? 1 : -1;
        }
    }

    class Edge {
        Vertex from, to;
        Weight weight;

        public Edge(Vertex from, Vertex to, Weight weight) {
            this.to = to;
            this.from = from;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "{" + from +
                    " <-> " + to +
                    (weight.equals(1) ? "}" : " (" + weight + ")}");
        }
    }

    static class HeapSort {
        private static int n;

        public static void sort(Comparable array[]) {
            n = array.length-1;
            for (int i = n /2; i >= 0; i--) {
                maxheap(array, i);
            }
            for (int i = n; i > 0; i--) {
                Comparable tmp = array[0];
                array[0] = array[i];
                array[i] = tmp;
                n = n - 1;
                maxheap(array, 0);
            }
        }

        private static void maxheap(Comparable array[], int i) {
            int left = 2*i ;
            int right = 2*i + 1;
            int max = i;
            if (left <= n && array[left].compareTo(array[i]) > 0)
                max = left;
            if (right <= n && array[right].compareTo(array[max]) > 0)
                max = right;

            if (max != i) {
                Comparable tmp = array[i];
                array[i] = array[max];
                array[max] = tmp;
                maxheap(array, max);
            }
        }
    }
}