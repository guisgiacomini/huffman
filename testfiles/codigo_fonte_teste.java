public class ExemploCodigoFonte {
    private static final String CONSTANTE = "teste";
    private int valor;
    
    public ExemploCodigoFonte(int valor) {
        this.valor = valor;
    }
    
    public int getValor() {
        return this.valor;
    }
    
    public void setValor(int novoValor) {
        this.valor = novoValor;
    }
    
    public static void main(String[] args) {
        ExemploCodigoFonte exemplo = new ExemploCodigoFonte(10);
        System.out.println("Valor: " + exemplo.getValor());
        exemplo.setValor(20);
        System.out.println("Novo valor: " + exemplo.getValor());
        
        for (int i = 0; i < 5; i++) {
            System.out.println("Iteracao: " + i);
        }
        
        if (exemplo.getValor() > 15) {
            System.out.println("Valor maior que 15");
        } else {
            System.out.println("Valor menor ou igual a 15");
        }
    }
}