import Controller.SistemaGrade;

/**
 * Classe principal responsável apenas por inicializar a aplicação.
 * Mantém o ponto de entrada limpo e desacoplado da lógica de negócio.
 */
public class Main {
    public static void main(String[] args) {
        // Instancia a aplicação e inicia a execução
        SistemaGrade sistema = new SistemaGrade();
        sistema.rodarPrograma();
    }
}