package graph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) throws IOException {
        Graph<String, Integer> graph = readGraphFromFile("cities.txt");

//        problemA(graph);
//        problemB(graph);
        problemC(graph);
//        problemD(graph);

    }

    public static Graph<String, Integer> readGraphFromFile(String name) throws FileNotFoundException {
        Graph<String, Integer> graph = new Graph<>();
        Scanner scanner = new Scanner(new FileReader(name));
        String line1 = scanner.nextLine();
        String line2 = scanner.nextLine();
        scanner.close();
        Arrays.asList(line1.split(" ")).forEach(graph::addVertex);
        String[] vertices = line2.split(" ");
        for (int i = 0; i < vertices.length; i+=2) {
            graph.addEdge(vertices[i], vertices[i+1], 1);
        }
        return graph;
    }

    public static void applyTheRules(Graph<String, Integer> graph) {
        List<Edge<String, Integer>> concurrentCopy = new ArrayList<>(graph.edges());
        for (Edge<String, Integer> edge : concurrentCopy) {
            if (    (edge.from.data.endsWith("-R") && edge.to.data.endsWith("-DU")) ||
                    (edge.to.data.endsWith("-R") && edge.from.data.endsWith("-DU"))) {
                graph.removeEdge(edge); continue;
            }
            if (    (edge.from.data.endsWith("-R") && edge.to.data.endsWith("-DG")) ||
                    (edge.to.data.endsWith("-R") && edge.from.data.endsWith("-DG"))) {
                graph.removeEdge(edge); continue;
            }
        }
    }

    public static void problemA(Graph<String, Integer> graph) throws IOException {
        FileWriter writer = new FileWriter("around.txt");
        String[] names = {"Donetsk-DU", "Kiev-U", "Lviv-U", "Batumi-G", "Rostov-R"};
        StringBuilder sb = new StringBuilder();

        for (String s: names) {
            Vertex<String, Integer> vertex = graph.vertices().stream().filter(v -> v.data.equals(s)).findFirst().orElseGet(() -> null);
            List<String> list = null;
            if (vertex != null) {
                list = vertex.adjacent().stream().map(v -> v.data).collect(Collectors.toList());
                HeapSort.sort(list);
                for (String st : list) {
                    sb.append(st + " ");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("\n");
        }

        sb.deleteCharAt(sb.length() - 1);
        writer.append(sb.toString());
        writer.close();
    }

    public static void problemB(Graph<String, Integer> graph) throws IOException {
        applyTheRules(graph);
        FileWriter writer = new FileWriter("able.txt");
        Vertex<String, Integer> rostov = graph.vertices().stream().filter(v -> v.data.contains("Rostov")).findFirst().get();

        String path = graph.BreadthFirstSearch(rostov).trim();
        writer.append(path.split("\\s+").length == graph.vertices().size() ? "yes" : "no");
        writer.append(" ");

        Edge<String, Integer> edge = graph.edges().stream().filter(e ->
                        (e.from.data.equals("Vladikavkaz-R") && e.to.data.equals("Tbilisi-G")) ||
                        (e.to.data.equals("Vladikavkaz-R") && e.from.data.equals("Tbilisi-G"))).findFirst().get();
        graph.removeEdge(edge);

        path = graph.BreadthFirstSearch(rostov).trim();
        writer.append( path.split("\\s+").length == graph.vertices().size() ? "yes" : "no" );

        writer.close();
    }

    public static void problemC(Graph<String, Integer> graph) throws IOException {
        FileWriter writer = new FileWriter("travel.txt");
        StringJoiner joiner = new StringJoiner(" ");

        Vertex<String, Integer> from_1 = graph.vertices().stream().filter(v -> v.data.contains("Melitopol")).findFirst().get();
        Vertex<String, Integer> to_1   = graph.vertices().stream().filter(v -> v.data.contains("Rostov"))   .findFirst().get();
        Vertex<String, Integer> from_2 = graph.vertices().stream().filter(v -> v.data.contains("Sukhumi"))  .findFirst().get();
        Vertex<String, Integer> to_2   = graph.vertices().stream().filter(v -> v.data.contains("Lugansk"))  .findFirst().get();

        List<Vertex<String, Integer>> path1 = graph.ShortestPath(from_1, to_1);
        List<Vertex<String, Integer>> path2 = graph.ShortestPath(from_2, to_2);

        joiner.add(String.valueOf(path1.size() - 1));
        path1.stream().map(v -> v.data).forEach(joiner::add);

        writer.append(joiner.toString() + "\n");
        joiner = new StringJoiner(" ");

        joiner.add(String.valueOf(path2.size() - 1));
        path2.stream().map(v -> v.data).forEach(joiner::add);

        writer.append(joiner.toString());
        writer.close();
    }

    public static void problemD(Graph<String, Integer> graph) throws IOException {
        applyTheRules(graph);
        FileWriter writer = new FileWriter("travel-now.txt");
        StringJoiner joiner = new StringJoiner(" ");

        Vertex<String, Integer> from_1 = graph.vertices().stream().filter(v -> v.data.contains("Melitopol")).findFirst().get();
        Vertex<String, Integer> to_1   = graph.vertices().stream().filter(v -> v.data.contains("Rostov"))   .findFirst().get();
        Vertex<String, Integer> from_2 = graph.vertices().stream().filter(v -> v.data.contains("Sukhumi"))  .findFirst().get();
        Vertex<String, Integer> to_2   = graph.vertices().stream().filter(v -> v.data.contains("Lugansk"))  .findFirst().get();

        List<Vertex<String, Integer>> path1 = graph.ShortestPath(from_1, to_1);
        List<Vertex<String, Integer>> path2 = graph.ShortestPath(from_2, to_2);

        joiner.add(String.valueOf(path1.size() - 1));
        path1.stream().map(v -> v.data).forEach(joiner::add);

        writer.append(joiner.toString() + "\n");
        joiner = new StringJoiner(" ");

        joiner.add(String.valueOf(path2.size() - 1));
        path2.stream().map(v -> v.data).forEach(joiner::add);

        writer.append(joiner.toString());
        writer.close();
    }

}








class Graph<Data extends Comparable, Weight extends Comparable> {

    private List<Vertex<Data, Weight>> vertices = new ArrayList<>();
    private List<Edge<Data, Weight>> edges = new ArrayList<>();

    public boolean addVertex(Data data) {
        return addVertex(new Vertex<>(data));
    }

    public boolean addVertex(Vertex<Data, Weight> vertex) {
        if (vertices.contains(vertex))
            return false;
        return vertices.add(vertex);
    }

    public boolean removeVertex(Vertex<Data, Weight> vertex) {
        edges.removeAll(vertex.incidents);
        return vertices.remove(vertex);
    }

    public boolean addEdge(Data from, Data to, Weight weight) {
        Vertex<Data, Weight> v1 = vertices.stream().filter(v -> v.data.equals(from)).findFirst().orElseGet(() -> null);
        Vertex<Data, Weight> v2 = vertices.stream().filter(v -> v.data.equals(to)).findFirst().orElseGet(() -> null);
        return !(v1 == null || v2 == null) && addEdge(v1, v2, weight);
    }

    public boolean addEdge(Vertex<Data, Weight> from, Vertex<Data, Weight> to, Weight weight) {
        Edge<Data, Weight> edge = new Edge<>(from, to, weight);
        from.incidents.add(edge);
        to.incidents.add(edge);
        return edges.add(edge);
    }

    public boolean removeEdge(Edge<Data, Weight> edge) {
        return removeEdge(edge.to, edge.from);
    }

    public boolean removeEdge(Vertex<Data,Weight> from, Vertex<Data,Weight> to) {
        Edge<Data,Weight> removingEdge = edges.stream().filter(e ->
                (e.from.equals(from) && e.to.equals(to)) ||
                (e.from.equals(to) && e.to.equals(from)) ).findFirst().orElseGet(() -> null);
        from.incidents.remove(removingEdge);
        to.incidents.remove(removingEdge);
        return edges.remove(removingEdge);
    }

    public List<Vertex<Data, Weight>> vertices() {
        return vertices;
    }

    public List<Edge<Data, Weight>> edges() {
        return edges;
    }

    public String visit(Vertex<Data,Weight> vertex) {
        return vertex.data + " ";
    }

    public String BreadthFirstSearch(Vertex<Data, Weight> vertex) {
        StringJoiner joiner = new StringJoiner(" ");
        Set<Vertex<Data, Weight>> visited = new HashSet<>();
        Queue<Vertex<Data,Weight>> queue = new LinkedList<>();

        queue.add(vertex);

        while (!queue.isEmpty()) {
            Vertex<Data,Weight> v = queue.remove();
            if (!visited.contains(v)) {
                joiner.add( visit(v) );
                visited.add(v);

                List<Vertex<Data,Weight>> adj = v.adjacent();
                List<Vertex<Data,Weight>> filter = adj.stream().filter(n -> !visited.contains(n)).collect(Collectors.toList());
                filter.forEach(queue::add);
            }
        }
        visited.clear();

        return joiner.toString();
    }

    public List<Vertex<Data, Weight>> ShortestPath(Vertex<Data, Weight> start, Vertex<Data, Weight> finish) {
        Set<Vertex<Data, Weight>> visited = new HashSet<>();
        Queue<Vertex<Data, Weight>> queue = new LinkedList<>();
        Map<Vertex<Data, Weight>, Vertex<Data, Weight>> prev = new HashMap<>();

        Vertex<Data, Weight> current = start;
        visited.add(current);
        queue.add(current);

        while(!queue.isEmpty()){
            current = queue.remove();
            if (!current.equals(finish)){
                for(Vertex<Data, Weight> vertex : current.adjacent()){
                    if(!visited.contains(vertex)){
                        queue.add(vertex);
                        visited.add(vertex);
                        prev.put(vertex, current);
                    }
                }
            } else break;
        }
        visited.clear();

        List<Vertex<Data, Weight>> path = new LinkedList<>();

        for(Vertex<Data, Weight> vertex = finish; vertex != null; vertex = prev.get(vertex))
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }

    public String DepthFirstSearch(Vertex<Data, Weight> vertex) {
        StringJoiner joiner = new StringJoiner(" ");
        Set<Vertex<Data, Weight>> visited = new HashSet<>();
        Stack<Vertex<Data,Weight>> stack = new Stack<>();

        stack.push(vertex);

        while (!stack.isEmpty()) {
            Vertex<Data,Weight> v = stack.pop();
            if (!visited.contains(v)) {
                joiner.add( visit(v) );
                visited.add(v);

                List<Vertex<Data,Weight>> adj = v.adjacent();
                List<Vertex<Data,Weight>> filter = adj.stream().filter(n -> !visited.contains(n)).collect(Collectors.toList());
                filter.forEach(stack::push);
            }
        }
        visited.clear();

        return joiner.toString();
    }

}

class Vertex<Data extends Comparable, Weight extends Comparable> {
    Data data;
    List<Edge<Data, Weight>> incidents = new ArrayList<>();

    public Vertex(Data data) {
        this.data = data;
    }

    public Data getValue() {
        return data;
    }

    public List<Vertex<Data, Weight>> adjacent() {
        List<Vertex<Data, Weight>> adjacent = new ArrayList<>();
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

        Vertex<?, ?> vertex = (Vertex<?, ?>) o;

        return data != null ? data.equals(vertex.data) : vertex.data == null;
    }

}

class Edge<Data extends Comparable, Weight extends Comparable> {
    Vertex<Data, Weight> from, to;
    Weight weight;

    public Edge(Vertex<Data, Weight> from, Vertex<Data, Weight> to, Weight weight) {
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











class HeapSort {
    private static int n;

    public static void sort(List<String> list) {
        n = list.size()-1;
        for (int i = n /2; i >= 0; i--) {
            maxheap(list, i);
        }
        for (int i = n; i > 0; i--) {
            String tmp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, tmp);
            n = n - 1;
            maxheap(list, 0);
        }
    }

    private static void maxheap(List<String> list, int i) {
        int left = 2*i ;
        int right = 2*i + 1;
        int max = i;
        if (left <= n && list.get(left).compareTo(list.get(i)) > 0)
            max = left;
        if (right <= n && list.get(right).compareTo(list.get(max)) > 0)
            max = right;

        if (max != i) {
            String tmp = list.get(i);
            list.set(i, list.get(max));
            list.set(max, tmp);
            maxheap(list, max);
        }
    }
}

/**
 My old quick sort that I wrote for the DSA tutorial #5
 */
class QuickSort {

    public static void sortQuick(int[] data) {
        sortQuick(data, 0, data.length - 1);
    }

    private static void sortQuick(int[] data, int left, int right) {
        int index = partition(data, left, right);
        if (left < index - 1)
            sortQuick(data, left, index - 1);
        if (index < right)
            sortQuick(data, index, right);
    }

    private static int partition(int arr[], int left, int right) {
        int i = left, j = right;
        int tmp;
        int pivot = arr[(left + right) / 2];
        while (i <= j) {
            while (arr[i] < pivot)
                i++;
            while (arr[j] > pivot)
                j--;
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }
}