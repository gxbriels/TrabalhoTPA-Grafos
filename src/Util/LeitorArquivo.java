package Util;

import Model.Grafo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivo {

    public static Grafo<String> carregarGrafo(String caminhoArquivo) {
        Grafo<String> grafo = new Grafo<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int linhaIndex = 0; // Representa o ID da matéria de origem

            while ((linha = br.readLine()) != null) {
                // Remove espaços em branco extras e divide por vírgula
                String[] valores = linha.trim().split(",");

                // Primeiro, garante que o vértice "Origem" exista (caso ele não tenha conexões)
                String nomeOrigem = "Materia " + (linhaIndex + 1);
                grafo.adicionarVertice(nomeOrigem);

                for (int colunaIndex = 0; colunaIndex < valores.length; colunaIndex++) {
                    String valorStr = valores[colunaIndex].trim();

                    if (!valorStr.isEmpty()) {
                        int valor = Integer.parseInt(valorStr);

                        // Se o valor for 1, existe uma conexão (aresta)
                        if (valor == 1) {
                            String nomeDestino = "Materia " + (colunaIndex + 1);

                            // Adiciona a aresta (o método do grafo já cria o vértice destino se precisar)
                            grafo.adicionarAresta(nomeOrigem, nomeDestino, 1.0f);

                            // Apenas para debug, mostrar o que está lendo
                            // System.out.println("Lido: " + nomeOrigem + " -> " + nomeDestino);
                        }
                    }
                }
                linhaIndex++;
            }
            System.out.println("Arquivo carregado com sucesso! Total de vértices: " + linhaIndex);

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formatação no arquivo. Verifique se contém apenas números e vírgulas.");
        }

        return grafo;
    }
}