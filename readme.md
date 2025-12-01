# 🎓 Sistema de Gestão de Grades Curriculares com Grafos

> Um sistema baseado em Teoria dos Grafos para validação de pré-requisitos e geração de trilhas de estudo acadêmicas.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-Concluído-brightgreen?style=for-the-badge)

## 📄 Sobre o Projeto

Este projeto foi desenvolvido como parte da disciplina de **Técnicas de Programação Avançada**. O objetivo é aplicar conceitos de **Teoria dos Grafos** para resolver um problema real: o gerenciamento de dependências entre matérias em uma grade curricular universitária.

O sistema permite modelar as matérias como vértices e os pré-requisitos como arestas direcionadas, utilizando algoritmos clássicos para garantir que a grade seja logicamente válida (sem ciclos) e para sugerir a melhor ordem de estudo.

## 🚀 Funcionalidades

* **Validação de Integridade (Detecção de Ciclos):** Verifica se a grade possui dependências circulares (deadlocks) que impediriam a conclusão do curso.
* **Geração de Trilha de Estudos (Ordenação Topológica):** Cria uma sequência linear de matérias respeitando todos os pré-requisitos.
* **Agrupamento por Semestres (Caminho Crítico):** Organiza a trilha linear em semestres, permitindo visualizar quais matérias podem ser cursadas paralelamente.
* **Visualização Estrutural (BFS):** Exibe a estrutura do grafo camada por camada utilizando Busca em Largura.
* **Importação de Dados:** Permite carregar grafos complexos via arquivos de texto (Matriz de Adjacência).

## 🛠️ Arquitetura e Tecnologias

O projeto foi estruturado seguindo o padrão **MVC (Model-View-Controller)** para garantir modularidade, organização e facilidade de manutenção.

* **Linguagem:** Java (JDK 8+)
* **Estrutura de Pastas:**
    * `src/model`: Contém a lógica de dados (`Grafo`, `Vertice`, `Aresta`) e a implementação dos algoritmos.
    * `src/controller`: Gerencia o fluxo da aplicação e a interação com o usuário (`SistemaGrade`).
    * `src/util`: Utilitários de I/O para leitura de arquivos.

### Algoritmos Implementados

1.  **Busca em Profundidade (DFS):** Utilizada como motor para a detecção de ciclos e para a ordenação topológica.
2.  **Busca em Largura (BFS):** Utilizada para o caminhamento e visualização hierárquica do grafo.

## 📦 Como Rodar o Projeto

### Pré-requisitos
* Ter o [Java JDK](https://www.oracle.com/java/technologies/downloads/) instalado.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/gxbriels/TrabalhoTPA-Grafos.git](https://github.com/gxbriels/TrabalhoTPA.git)
    ```
2.  **Navegue até a pasta do projeto:**
    ```bash
    cd nome-da-pasta
    ```
3.  **Compile o código:**
    ```bash
    javac -d bin -sourcepath src src/Main.java
    ```
4.  **Execute a aplicação:**
    ```bash
    java -cp bin Main
    ```

## 📝 Formato do Arquivo de Entrada

Para importar uma grade via arquivo, crie um arquivo `.txt` na raiz do projeto contendo a **Matriz de Adjacência** (separada por vírgulas).
* **1:** Indica que existe pré-requisito (Linha -> Coluna).
* **0:** Sem dependência.

**Exemplo (`teste.txt`):**
```text
0, 1, 0, 0
0, 0, 1, 0
0, 0, 0, 1
0, 0, 0, 0