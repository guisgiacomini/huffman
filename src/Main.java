import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length == 0 || args.length > 2){
            throw new RuntimeException("É necessário 2 argumentos. Argumentos recebidos: " + args.length);
        }

        String arquivoOriginal = args[0];
        String arquivoComprimido = args[1];

        String texto = "";
        String linha = "";
        BufferedReader reader = new BufferedReader(new FileReader(arquivoOriginal));


        linha = reader.readLine();
        while (linha != null){
            texto = texto + "" + linha;
            linha = reader.readLine();
        }
        reader.close();

        System.out.println(texto);
    }
}