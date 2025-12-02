package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implementação genérica de um Model.Grafo utilizando Listas de Adjacência.
 * Implementa algoritmos clássicos: DFS (Ciclo/Topológica) e BFS.
 * @param <T> Tipo do objeto armazenado no vértice (ex: String, Integer).
 */
public class Grafo<T> {
    private ArrayList<Aresta> arestas;
    private ArrayList<Vertice<T>> vertices;

    public Grafo() {
        this.arestas = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    /**
     * Adiciona um vértice ao grafo.
     * Otimização: Verifica se o vértice já existe para evitar duplicatas,
     * garantindo integridade dos dados e economizando memória.
     */
    public Vertice<T> criarVertice(T valor){
        Vertice<T> existente = obterVertice(valor);
        if (existente != null) {
            return existente;
        }
        Vertice<T> novo = new Vertice<T>(valor);
        return novo;
    }

    public Vertice<T> adicionarVertice(T valor){
        Vertice<T> existente = obterVertice(valor);
        if (existente != null) {
            return existente;
        }
        Vertice<T> novo = new Vertice<T>(valor);
        this.vertices.add(novo);
        return novo;
    }

    // Busca linear simples O(V). Poderia ser otimizado com HashMap para O(1),
    // mas ArrayList atende ao requisito de estruturas básicas.
    private Vertice obterVertice(T valor){
        for(Vertice<T> v : this.vertices){
            if(v.getValor().equals(valor))
                return v;
        }
        return null;
    }

    public void adicionarAresta(T origem, T destino, float peso){
        // Garante que os vértices existam antes de ligá-los
        Vertice verticeOrigem = adicionarVertice(origem);
        Vertice verticeDestino = adicionarVertice(destino);

        Aresta novaAresta = new Aresta(verticeOrigem, verticeDestino, peso);
        this.arestas.add(novaAresta);
    }

    /**
     * Algoritmo: Busca em Largura (BFS).
     * Utiliza uma FILA para explorar o grafo camada por camada.
     * Complexidade: O(V + E).
     */
    public void buscaEmLargura(){
        if (this.vertices.isEmpty()) {
            System.out.println("Model.Grafo vazio.");
            return;
        }

        ArrayList<Vertice<T>> marcados = new ArrayList<>();
        ArrayList<Vertice<T>> fila = new ArrayList<>();

        Vertice<T> atual = this.vertices.get(0);
        marcados.add(atual);
        fila.add(atual);

        while(!fila.isEmpty()){
            atual = fila.remove(0);
            System.out.println(" -> Visitando: " + atual.getValor());

            for (Aresta a : obterDestinos(atual)) {
                Vertice<T> dest = (Vertice<T>) a.getDestino();
                if(!marcados.contains(dest)){
                    marcados.add(dest);
                    fila.add(dest);
                }
            }
        }
    }

    // Auxiliar para pegar arestas que saem de um vértice
    private ArrayList<Aresta> obterDestinos(Vertice v){
        ArrayList<Aresta> destinos = new ArrayList<>();
        for (Aresta a : this.arestas) {
            if (a.getOrigem().equals(v)) destinos.add(a);
        }
        return destinos;
    }

    // =====================================================================
    //  ALGORITMOS ESCOLHIDOS: DETECÇÃO DE CICLO E ORDENAÇÃO TOPOLÓGICA
    // =====================================================================

    /**
     * Verifica se o grafo possui ciclos (Dependências circulares).
     * Estratégia: DFS (Busca em Profundidade) com Pilha de Recursão.
     * Se encontrarmos um vértice que JÁ está na pilha de recursão atual, temos um ciclo.
     * @return true se houver ciclo, false caso contrário.
     */
    public boolean temCiclo() {
        HashSet<Vertice<T>> visitados = new HashSet<>();
        HashSet<Vertice<T>> pilhaRecursao = new HashSet<>();

        for (Vertice<T> vertice : this.vertices) {
            if (temCicloDFS(vertice, visitados, pilhaRecursao)) {
                return true;
            }
        }
        return false;
    }

    private boolean temCicloDFS(Vertice<T> v, HashSet<Vertice<T>> visitados, HashSet<Vertice<T>> pilhaRecursao) {
        if (pilhaRecursao.contains(v)) return true; // Ciclo detectado!
        if (visitados.contains(v)) return false;    // Já processado e seguro

        visitados.add(v);
        pilhaRecursao.add(v);

        for (Aresta a : obterDestinos(v)) {
            Vertice<T> vizinho = (Vertice<T>) a.getDestino();
            if (temCicloDFS(vizinho, visitados, pilhaRecursao)) return true;
        }

        pilhaRecursao.remove(v); // Remove da pilha ao sair da recursão (backtracking)
        return false;
    }

    /**
     * Realiza a Ordenação Topológica do grafo (DAG).
     * Utilidade: Definir ordem de execução de tarefas com pré-requisitos.
     * Estratégia: DFS. Adiciona o vértice à pilha APÓS visitar todos os seus filhos.
     * @return Lista com a ordem correta ou null se houver ciclo.
     */
    public ArrayList<T> ordenacaoTopologica() {
        if (temCiclo()) return null; // Pré-condição: Model.Grafo deve ser Acíclico

        Stack<Vertice<T>> pilha = new Stack<>();
        HashSet<Vertice<T>> visitados = new HashSet<>();

        for (Vertice<T> v : this.vertices) {
            if (!visitados.contains(v)) topologicaDFS(v, visitados, pilha);
        }

        ArrayList<T> ordem = new ArrayList<>();
        while (!pilha.isEmpty()) ordem.add(pilha.pop().getValor());

        return ordem;
    }

    // =====================================================================
    //  FUNCIONALIDADE EXTRA: AGRUPAMENTO POR SEMESTRES (CAMINHO CRÍTICO)
    // =====================================================================

    public Map<Integer, ArrayList<T>> agruparPorSemestres() {
        // Passo 1: Pega a lista linear válida (Topological Sort)
        ArrayList<T> ordemLinear = ordenacaoTopologica();
        if (ordemLinear == null) return null; // Tem ciclo

        // Mapa para guardar o "nível" (semestre) de cada matéria
        Map<T, Integer> niveis = new HashMap<>();

        // Inicializa todas as matérias como Semestre 1
        for (Vertice<T> v : this.vertices) {
            niveis.put(v.getValor(), 1);
        }

        // Passo 2: Processa a lista linear para ajustar os níveis
        // Se A -> B, então o nível de B deve ser, no mínimo, nível de A + 1
        for (T valorAtual : ordemLinear) {
            Vertice<T> atual = obterVertice(valorAtual);
            int nivelAtual = niveis.get(valorAtual);

            for (Aresta a : obterDestinos(atual)) {
                Vertice<T> destino = (Vertice<T>) a.getDestino();
                T valorDestino = destino.getValor();

                int nivelAntigo = niveis.get(valorDestino);
                int nivelNovo = Math.max(nivelAntigo, nivelAtual + 1);

                niveis.put(valorDestino, nivelNovo);
            }
        }

        // Passo 3: Organiza o resultado para retorno (Semestre -> Lista de Matérias)
        // TreeMap garante que as chaves fiquem ordenadas (1, 2, 3...)
        Map<Integer, ArrayList<T>> resultado = new TreeMap<>();

        for (Map.Entry<T, Integer> entry : niveis.entrySet()) {
            int semestre = entry.getValue();
            T materia = entry.getKey();

            // Se ainda não tem lista para esse semestre, cria uma
            resultado.putIfAbsent(semestre, new ArrayList<>());
            // Adiciona a matéria na lista do semestre correspondente
            resultado.get(semestre).add(materia);
        }

        return resultado;
    }

    private void topologicaDFS(Vertice<T> v, HashSet<Vertice<T>> visitados, Stack<Vertice<T>> pilha) {
        visitados.add(v);
        for (Aresta a : obterDestinos(v)) {
            Vertice<T> vizinho = (Vertice<T>) a.getDestino();
            if (!visitados.contains(vizinho)) topologicaDFS(vizinho, visitados, pilha);
        }
        pilha.push(v); // Empilha somente quando não há mais dependências para explorar
    }

    public boolean estaVazio() {
        return this.vertices.isEmpty();
    }


    public int obterTamanho() {
        return this.vertices.size();
    }

    public ArrayList<Vertice<T>> getVertices(){return this.vertices;};
}