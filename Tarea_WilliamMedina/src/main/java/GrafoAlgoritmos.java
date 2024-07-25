import java.io.*;
import java.util.*;

public class GrafoAlgoritmos {

    public static void main(String[] args) {
        try {
            // Cargar y procesar el grafo30
            Grafo grafo30 = new Grafo(false);
            grafo30.cargarDesdeArchivo("C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/Grafo30.txt");

            // Ejecutar y guardar resultados de Kruskal
            List<Arista> mstKruskal30 = kruskal(grafo30);
            grafo30.guardarEnArchivo(mstKruskal30, "C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/resultado_grafo30_kruskal.txt");

            // Ejecutar y guardar resultados de Prim
            List<Arista> mstPrim30 = prim(grafo30, 1); // Cambié el nodo inicial a 1
            grafo30.guardarEnArchivo(mstPrim30, "C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/resultado_grafo30_prim.txt");

            // Ejecutar y guardar resultados de Dijkstra
            ResultadoDijkstra resultadoDijkstra30 = dijkstra(grafo30, 1); // Cambié el nodo inicial a 1
            guardarRutasDijkstra(resultadoDijkstra30, 1, "C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/resultado_grafo30_dijkstra.txt");

            // Cargar y procesar el grafo50
            Grafo grafo50 = new Grafo(false);
            grafo50.cargarDesdeArchivo("C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/Grafo50.txt");

            // Ejecutar y guardar resultados de Kruskal
            List<Arista> mstKruskal50 = kruskal(grafo50);
            grafo50.guardarEnArchivo(mstKruskal50, "C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/resultado_grafo50_kruskal.txt");

            // Ejecutar y guardar resultados de Prim
            List<Arista> mstPrim50 = prim(grafo50, 1); // Cambié el nodo inicial a 1
            grafo50.guardarEnArchivo(mstPrim50, "C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/resultado_grafo50_prim.txt");

            // Ejecutar y guardar resultados de Dijkstra
            ResultadoDijkstra resultadoDijkstra50 = dijkstra(grafo50, 1); // Cambié el nodo inicial a 1
            guardarRutasDijkstra(resultadoDijkstra50, 1, "C:/Users/Usuario iTC/Desktop/UTPL 4to Ciclo/ANALISIS DE ALGORITMOS/resultado_grafo50_dijkstra.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class Grafo {
        private final Map<Integer, List<Arista>> grafo = new HashMap<>();
        private final boolean dirigido;

        public Grafo(boolean dirigido) {
            this.dirigido = dirigido;
        }

        public void agregarArista(int origen, int destino, int peso) {
            grafo.computeIfAbsent(origen, k -> new ArrayList<>()).add(new Arista(origen, destino, peso));
            if (!dirigido) {
                grafo.computeIfAbsent(destino, k -> new ArrayList<>()).add(new Arista(destino, origen, peso));
            }
        }

        public void cargarDesdeArchivo(String archivo) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    int origen = Integer.parseInt(partes[0]);
                    int destino = Integer.parseInt(partes[1]);
                    int peso = Integer.parseInt(partes[2]);
                    agregarArista(origen, destino, peso);
                }
            }
        }

        public void guardarEnArchivo(List<Arista> aristas, String archivo) throws IOException {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                for (Arista arista : aristas) {
                    bw.write(arista.origen + "," + arista.destino + "," + arista.peso);
                    bw.newLine();
                }
            }
        }

        public Map<Integer, List<Arista>> getGrafo() {
            return grafo;
        }
    }


    static class Arista {
        int origen;
        int destino;
        int peso;

        Arista(int origen, int destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }

    // Implementación del algoritmo de Kruskal
    public static List<Arista> kruskal(Grafo grafo) {
        Map<Integer, Integer> padre = new HashMap<>();
        Map<Integer, Integer> rango = new HashMap<>();

        List<Arista> aristas = new ArrayList<>();
        for (Map.Entry<Integer, List<Arista>> entrada : grafo.getGrafo().entrySet()) {
            for (Arista arista : entrada.getValue()) {
                if (entrada.getKey() < arista.destino) {
                    aristas.add(arista);
                }
            }
        }

        aristas.sort(Comparator.comparingInt(a -> a.peso));

        for (Integer nodo : grafo.getGrafo().keySet()) {
            padre.put(nodo, nodo);
            rango.put(nodo, 0);
        }

        List<Arista> mst = new ArrayList<>();
        for (Arista arista : aristas) {
            int raiz1 = encontrar(padre, arista.origen);
            int raiz2 = encontrar(padre, arista.destino);

            if (raiz1 != raiz2) {
                unir(padre, rango, raiz1, raiz2);
                mst.add(arista);
            }
        }

        return mst;
    }

    private static int encontrar(Map<Integer, Integer> padre, int nodo) {
        if (padre.get(nodo) != nodo) {
            padre.put(nodo, encontrar(padre, padre.get(nodo)));
        }
        return padre.get(nodo);
    }

    private static void unir(Map<Integer, Integer> padre, Map<Integer, Integer> rango, int raiz1, int raiz2) {
        if (rango.get(raiz1) > rango.get(raiz2)) {
            padre.put(raiz2, raiz1);
        } else {
            padre.put(raiz1, raiz2);
            if (rango.get(raiz1).equals(rango.get(raiz2))) {
                rango.put(raiz2, rango.get(raiz2) + 1);
            }
        }
    }

    // Implementación del algoritmo de Prim
    public static List<Arista> prim(Grafo grafo, int nodoInicio) {
        Set<Integer> visitados = new HashSet<>();
        PriorityQueue<Arista> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.peso));
        List<Arista> mst = new ArrayList<>();

        visitados.add(nodoInicio);
        for (Arista arista : grafo.getGrafo().getOrDefault(nodoInicio, Collections.emptyList())) {
            pq.add(new Arista(nodoInicio, arista.destino, arista.peso));
        }

        while (!pq.isEmpty()) {
            Arista arista = pq.poll();
            if (visitados.contains(arista.destino)) continue;

            visitados.add(arista.destino);
            mst.add(new Arista(arista.origen, arista.destino, arista.peso));

            for (Arista siguienteArista : grafo.getGrafo().getOrDefault(arista.destino, Collections.emptyList())) {
                if (!visitados.contains(siguienteArista.destino)) {
                    pq.add(new Arista(arista.destino, siguienteArista.destino, siguienteArista.peso));
                }
            }
        }

        return mst;
    }

    // Implementación del algoritmo de Dijkstra
    public static ResultadoDijkstra dijkstra(Grafo grafo, int nodoInicio) {
        Map<Integer, Integer> distancias = new HashMap<>();
        Map<Integer, Integer> predecesores = new HashMap<>();
        PriorityQueue<Arista> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.peso));

        for (Integer nodo : grafo.getGrafo().keySet()) {
            distancias.put(nodo, Integer.MAX_VALUE);
            predecesores.put(nodo, null);
        }
        distancias.put(nodoInicio, 0);
        pq.add(new Arista(nodoInicio, nodoInicio, 0));

        while (!pq.isEmpty()) {
            Arista arista = pq.poll();
            int nodoActual = arista.destino;

            if (arista.peso > distancias.get(nodoActual)) continue;

            for (Arista adyacente : grafo.getGrafo().getOrDefault(nodoActual, Collections.emptyList())) {
                int nuevaDistancia = distancias.get(nodoActual) + adyacente.peso;
                if (nuevaDistancia < distancias.get(adyacente.destino)) {
                    distancias.put(adyacente.destino, nuevaDistancia);
                    predecesores.put(adyacente.destino, nodoActual);
                    pq.add(new Arista(nodoActual, adyacente.destino, nuevaDistancia));
                }
            }
        }

        return new ResultadoDijkstra(predecesores, distancias);
    }

    public static void guardarRutasDijkstra(ResultadoDijkstra resultado, int nodoInicio, String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Map.Entry<Integer, Integer> entrada : resultado.distancias.entrySet()) {
                int destino = entrada.getKey();
                int peso = entrada.getValue();
                bw.write(nodoInicio + "," + peso + "," + destino);
                bw.newLine();
            }
        }
    }


    static class ResultadoDijkstra {
        Map<Integer, Integer> predecesores;
        Map<Integer, Integer> distancias;

        ResultadoDijkstra(Map<Integer, Integer> predecesores, Map<Integer, Integer> distancias) {
            this.predecesores = predecesores;
            this.distancias = distancias;
        }
    }
}
