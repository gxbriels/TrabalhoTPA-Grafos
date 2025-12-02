package Controller;

import Model.Grafo;
import Model.Vertice;
import Util.LeitorArquivo;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class SistemaGrade {
    private Grafo<String> grafo;
    private Scanner scanner;

    public SistemaGrade() {
        this.grafo = new Grafo<>();
        this.scanner = new Scanner(System.in);
    }

    public void rodarPrograma() {
        int opcao = -1;

        while (opcao != 0) {
            limparTela();
            exibirCabecalho();
            exibirMenu();
            opcao = lerInteiro();

            // Espaço visual antes do resultado
            System.out.println();

            switch (opcao) {
                case 1: adicionarMateria(); break;
                case 2: adicionarDependencia(); break;
                case 3: verificarCiclos(); break;
                case 4: gerarOrdemEstudo(); break;
                case 5: carregarArquivo(); break;
                case 6: imprimirGrafoBFS(); break;
                case 0: System.out.println(">> Encerrando o sistema..."); break;
                default: System.out.println("!! Opção inválida. Pressione ENTER para tentar novamente.");
            }

            if (opcao != 0) {
                System.out.println("\n------------------------------------------------");
                System.out.println("Pressione ENTER para voltar ao menu...");
                scanner.nextLine(); // Pausa para leitura
            }
        }
        scanner.close();
    }

    // Simula limpeza de tela imprimindo várias linhas
    private void limparTela() {
        for (int i = 0; i < 15; i++) System.out.println();
    }

    private void exibirCabecalho() {
        System.out.println("========================================");
        System.out.println("      SISTEMA DE GRADE CURRICULAR       ");
        System.out.println("   Gestão de Dependências Acadêmicas    ");
        System.out.println("========================================");

        // CORREÇÃO AQUI: Verifica se a lista de vértices não está vazia
        String status;
        if (!grafo.estaVazio()) {
            status = "[V] Grafo Carregado (" + grafo.obterTamanho() + " matérias)";

        } else {
            status = "[ ] Sem grafo para testes";
        }

        System.out.println(" Status: " + status);
        System.out.println("----------------------------------------");
    }

    private void exibirMenu() {
        System.out.println(" 1. [+] Adicionar Matéria");
        System.out.println(" 2. [>] Adicionar Dependência");
        System.out.println(" 3. [?] Verificar Validade (Ciclos)");
        System.out.println(" 4. [#] Gerar Trilha de Estudo");
        System.out.println(" 5. [^] Carregar Arquivo");
        System.out.println(" 6. [O] Visualizar Grade (BFS)");
        System.out.println(" 0. [x] Sair");
        System.out.println("----------------------------------------");
        System.out.print(" Escolha uma opção: ");
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // --- MÉTODOS DE AÇÃO (Com visual melhorado) ---

    private void adicionarMateria() {
        System.out.println(">>> Adicionar Nova Matéria");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        grafo.adicionarVertice(nome);
        System.out.println(" [OK] Matéria '" + nome + "' adicionada com sucesso.");
        System.out.print("Gostaria de adicionar uma depêndencia? ");
        System.out.println("S/N");
        String resposta = scanner.nextLine();
        if (resposta.equals("N")){
            System.out.println(" Matéria criada sem depências");
        }if (resposta.equals("S")){
            adicionarDependencia(nome);
        }
    }

    private void adicionarDependencia(String destino) {
        System.out.println(">>> Nova Dependência");
        System.out.println("Matéria DEPENDENTE: " + destino);
        System.out.print("Matéria PRÉ-REQUISITO: ");
        String origem = scanner.nextLine();
        Vertice vdestino = grafo.criarVertice(destino);
        Vertice vorigem = grafo.criarVertice(origem);
        ArrayList listadevertices = grafo.getVertices();
        if (!listadevertices.contains(vdestino) || !listadevertices.contains(vorigem)){
            System.out.println("Uma ou mais matérias ainda não foram criadas, não é possível criar dependência");
        }else{
            grafo.adicionarAresta(origem, destino, 1.0f);
            System.out.println(" [OK] Vínculo criado: " + origem + " -> " + destino);
        }
    }

    private void adicionarDependencia() {
        System.out.println(">>> Nova Dependência");
        System.out.print("Matéria PRÉ-REQUISITO: ");
        String origem = scanner.nextLine();
        System.out.print("Matéria DEPENDENTE: ");
        String destino = scanner.nextLine();
        Vertice vdestino = grafo.criarVertice(destino);
        Vertice vorigem = grafo.criarVertice(origem);
        ArrayList listadevertices = grafo.getVertices();
        if (!listadevertices.contains(vdestino) || !listadevertices.contains(vorigem)){
            System.out.println("Matérias ainda não criadas, não é possível criar dependência");
        }else{
            grafo.adicionarAresta(origem, destino, 1.0f);
            System.out.println(" [OK] Vínculo criado: " + origem + " -> " + destino);
        }
    }

    private void verificarCiclos() {
        System.out.println(">>> Validando Integridade...");
        if (grafo.temCiclo()) {
            System.out.println(" [X] ERRO CRÍTICO: Ciclo Detectado!");
            System.out.println("     A grade possui dependências circulares.");
        } else {
            System.out.println(" [V] SUCESSO: A grade é válida e consistente.");
        }
    }

    private void gerarOrdemEstudo() {
        System.out.println(">>> Planejamento de Estudos");
        if (grafo.temCiclo()) {
            System.out.println(" [!] ERRO: Não é possível gerar trilha pois há ciclos na grade.");
            return;
        }

        // 1. Mostra a prova da Ordenação Topológica (Linear)
        ArrayList<String> linear = grafo.ordenacaoTopologica();
        if (linear == null || linear.isEmpty()) {
            System.out.println(" [!] Grade vazia.");
            return;
        }

        System.out.println("\n--- [A] Sequência Linear (Ordenação Topológica Pura) ---");
        System.out.println("Esta lista garante que você nunca pegará uma matéria travada:");
        System.out.print("Inicio -> ");
        for (String m : linear) {
            System.out.print("[" + m + "] -> ");
        }
        System.out.println("Fim");

        // 2. Mostra o Agrupamento por Semestres
        System.out.println("\n--- [B] Sugestão de Grade (Agrupada por Semestres) ---");
        System.out.println("Baseado no Caminho Crítico (Longest Path):");

        Map<Integer, ArrayList<String>> semestres = grafo.agruparPorSemestres();

        for (Map.Entry<Integer, ArrayList<String>> entry : semestres.entrySet()) {
            int numSemestre = entry.getKey();
            ArrayList<String> materias = entry.getValue();

            System.out.printf(" %02dº Semestre: %s%n", numSemestre, materias.toString());
        }
    }

    private void carregarArquivo() {
        System.out.println(">>> Carregar de Arquivo");
        System.out.print("Nome do arquivo (ex: teste1.txt): ");
        String arquivo = scanner.nextLine();
        try {
            Grafo<String> novoGrafo = LeitorArquivo.carregarGrafo(arquivo);
            if (novoGrafo != null) {
                this.grafo = novoGrafo;
            }
        } catch (Exception e) {
            System.out.println(" [!] Erro ao carregar.");
        }
    }

    private void imprimirGrafoBFS() {
        System.out.println(">>> Visualização Estrutural (BFS)");
        grafo.buscaEmLargura();
    }
}