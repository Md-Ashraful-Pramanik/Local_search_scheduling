import java.util.HashSet;
import java.util.Stack;

public class LocalSearch {

    public Graph applyLocalSearch(Graph graph){
        Graph minPenaltyNeighbour = graph;
        Graph temp;

        for (int i = 0; i < 1000; i++) {
            temp = useKempeChain(minPenaltyNeighbour);
            //temp = useKempeChain2(graph);
            if(minPenaltyNeighbour.getPenalty() > temp.getPenalty())
                minPenaltyNeighbour = temp;
            else
                return minPenaltyNeighbour;

            if(i%20 == 0)
                System.out.println("Step: " + i + " -> " + minPenaltyNeighbour.getPenalty());
        }
        return minPenaltyNeighbour;
    }

    public Graph useKempeChain(Graph graph){
        Graph minPenaltyNeighbour = graph;
        Vertex v;
        Graph g;
        HashSet<Vertex> kempeChain;

        boolean[][] isVisited = new boolean[graph.getVertexCount()][graph.getVertexCount()];

        for (int i=0; i< graph.getVertexCount(); i++){

            for (Vertex neighbour: graph.getVertex(i).getNeighbours()) {
                int neighbourColor = neighbour.getScheduling();
                int vColor = graph.getVertex(i).getScheduling();

                if(isVisited[i][neighbour.getNb()] || isVisited[neighbour.getNb()][i])
                    continue;

                g = graph.deepCopy();
                v = g.getVertex(i);

                kempeChain = buildKempeChain(v, neighbour);

                for (Vertex vertex: kempeChain) {
                    if(vertex.getScheduling() == vColor)
                        vertex.setScheduling(neighbourColor);
                    else
                        vertex.setScheduling(vColor);
                }

                if(g.getPenalty() < minPenaltyNeighbour.getPenalty()){
                    minPenaltyNeighbour = g;
                    //System.out.println("Updating");
                }

                for (Vertex vertex:kempeChain) {
                    for (Vertex vertex2:kempeChain) {
                        isVisited[vertex.getNb()][vertex2.getNb()] = true;
                        isVisited[vertex2.getNb()][vertex.getNb()] = true;
                    }
                }

                //isVisited[vColor][neighbourColor] = true;
                //isVisited[neighbourColor][vColor] = true;
            }

        }

        return minPenaltyNeighbour;
    }

    public Graph useKempeChain2(Graph graph){
        Graph minPenaltyNeighbour = graph;
        Vertex v;
        Graph g;
        HashSet<Vertex> kempeChain;

        for (int i=0; i< graph.getVertexCount(); i++){
            g = graph.deepCopy();
            v = g.getVertex(i);
            HashSet<Vertex> maxKempeChain = new HashSet<>();
            int neighbourColor = -1;
            int vColor = v.getScheduling();

            for (Vertex neighbour: v.getNeighbours()) {
                kempeChain = buildKempeChain(v, neighbour);

                if(kempeChain.size() >= maxKempeChain.size()){
                    maxKempeChain = kempeChain;
                    neighbourColor = neighbour.getScheduling();
                }
            }

            for (Vertex vertex: maxKempeChain) {
                if(vertex.getScheduling() == vColor)
                    vertex.setScheduling(neighbourColor);
                else
                    vertex.setScheduling(vColor);
            }

            if(g.getPenalty() < minPenaltyNeighbour.getPenalty())
                minPenaltyNeighbour = g;
        }

        return minPenaltyNeighbour;
    }

    public HashSet<Vertex> buildKempeChain(Vertex u, Vertex v){
        HashSet<Vertex> kempeChain = new HashSet<>(u.getNeighbours().size());
        Stack<Vertex> temp = new Stack<>();

        temp.add(u);
        while (!temp.empty()){
            Vertex vertex = temp.pop();

            if(kempeChain.contains(vertex))
                continue;

            kempeChain.add(vertex);

            for (Vertex neighbour : vertex.getNeighbours()) {
                if(neighbour.getScheduling() == u.getScheduling() ||
                    neighbour.getScheduling() == v.getScheduling())
                {
                    temp.add(neighbour);
                }
            }
        }
        //System.out.println(kempeChain.size());
        return kempeChain;
    }
}
