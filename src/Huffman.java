import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Alice de Oliveira Duarte - 10419323
// Pedro Roberto Fernandes Noronha - 10443434
// Guilherme Silveira Giacomini - 10435311
// Carlos Eduardo Diniz de Almeida - 10444407

// Huffman compression/decompression usando MinHeap e árvore binária
public class Huffman {

    // Compress usando Huffman algorithm
    public static void compress(String inputPath, String outputPath) throws IOException {
        // Lê todo o arquivo e constrói a tabela de frequências (256 posições)
        byte[] dados = Files.readAllBytes(Path.of(inputPath));
        int[] frequencies = new int[256];
        for (byte b : dados) {
            frequencies[b & 0xFF]++;
        }
        // Imprime a tabela de frequências
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 1: Tabela de Frequencia de Caracteres");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < 256; i++) {
            int f = frequencies[i];
            if (f > 0) {
                char c = (char) i;
                System.out.printf("Caractere '%c' (ASCII: %d): %d%n", c, i, f);
            }
        }
        // Cria lista de nodes folha e imprime o heap inicial
        java.util.ArrayList<No> folhas = new java.util.ArrayList<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                folhas.add(new No((char) i, frequencies[i]));
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 2: Min-Heap Inicial (Vetor)");
        System.out.println("--------------------------------------------------");
        System.out.print("[ ");
        for (int i = 0; i < folhas.size(); i++) {
            No n = folhas.get(i);
            System.out.printf("No('%c',%d)", n.caractere, n.frequencia);
            if (i < folhas.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" ]");
        // Constrói a árvore de Huffman
        No raiz = buildHuffTree(frequencies);
        // Imprime a árvore (percorrendo em largura)
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 3: Arvore de Huffman");
        System.out.println("--------------------------------------------------");
        java.util.Queue<No> fila = new java.util.LinkedList<>();
        fila.add(raiz);
        boolean primeiro = true;
        while (!fila.isEmpty()) {
            No atual = fila.poll();
            if (primeiro) {
                System.out.printf("- (RAIZ, %d)%n", atual.frequencia);
                primeiro = false;
            } else if (atual.ehFolha()) {
                System.out.printf("- ('%c', %d)%n", atual.caractere, atual.frequencia);
            } else {
                System.out.printf("- (Interno, %d)%n", atual.frequencia);
            }
            if (atual.esquerda != null) fila.add(atual.esquerda);
            if (atual.direita != null) fila.add(atual.direita);
        }
        // Gera a tabela de códigos (códigos binários em forma de string)
        String[] codigos = new String[256];
        buildCodes(raiz, "", codigos);
        // Imprime a tabela de códigos
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 4: Tabela de Codigos de Huffman");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < codigos.length; i++) {
            String codigo = codigos[i];
            if (codigo != null) {
                System.out.printf("Caractere '%c': %s%n", (char) i, codigo);
            }
        }
        // Calcula estatísticas de size original e comprimido (somente dados)
        long originalBits = (long) dados.length * 8;
        long compressedBits = 0;
        for (byte b : dados) {
            compressedBits += codigos[b & 0xFF].length();
        }
        long compressedBytes = (compressedBits + 7) / 8;
        double taxa = 0;
        if (originalBits > 0) {
            taxa = 100.0 * (1.0 - ((double) compressedBits / originalBits));
        }
        // Imprime resumo da compressão
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 5: Resumo da Compressao");
        System.out.println("--------------------------------------------------");
        System.out.printf("Tamanho original....: %d bits (%d bytes)%n", originalBits, dados.length);
        System.out.printf("Tamanho comprimido..: %d bits (%d bytes)%n", compressedBits, compressedBytes);
        System.out.printf("Taxa de compressao..: %.2f%%%n", taxa);
        System.out.println("--------------------------------------------------");
        // Escreve cabeçalho (frequências) e o fluxo de bits comprimido
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputPath)))) {
            for (int i = 0; i < 256; i++) {
                out.writeInt(frequencies[i]);
            }
            int currByte = 0;
            int bitCount = 0;
            for (byte b : dados) {
                String codigo = codigos[b & 0xFF];
                for (int i = 0; i < codigo.length(); i++) {
                    currByte <<= 1;
                    if (codigo.charAt(i) == '1') {
                        currByte |= 1;
                    }
                    bitCount++;
                    if (bitCount == 8) {
                        out.write(currByte);
                        currByte = 0;
                        bitCount = 0;
                    }
                }
            }
            // Se restarem bits, completa com zeros à direita
            if (bitCount > 0) {
                currByte <<= (8 - bitCount);
                out.write(currByte);
            }
        }
    }

    // Decompress Huffman encoded file
    public static void decompress(String inputPath, String outputPath) throws IOException {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(inputPath)))) {
            int[] frequencies = new int[256];
            for (int i = 0; i < 256; i++) {
                frequencies[i] = in.readInt();
            }
            No raiz = buildHuffTree(frequencies);
            // Calcula o número total de caracteres a serem restaurados
            int totalCharcount = 0;
            for (int f : frequencies) {
                totalCharcount += f;
            }
            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputPath))) {
                No currNode = raiz;
                int escritos = 0;
                // Lê byte a byte do fluxo comprimido
                while (escritos < totalCharcount) {
                    int b = in.read();
                    if (b == -1) {
                        break;
                    }
                    for (int i = 7; i >= 0; i--) {
                        int bit = (b >> i) & 1;
                        currNode = (bit == 0) ? currNode.esquerda : currNode.direita;
                        if (currNode.ehFolha()) {
                            out.write((byte) currNode.caractere);
                            escritos++;
                            if (escritos >= totalCharcount) {
                                break;
                            }
                            currNode = raiz;
                        }
                    }
                }
            }
        }
    }

    // Constrói Huffman tree usando MinHeap
    private static No buildHuffTree(int[] frequencies) {
        MinHeap heap = new MinHeap();
        // Cria nós folha para cada frequência não nula
        for (int i = 0; i < frequencies.length; i++) {
            int f = frequencies[i];
            if (f > 0) {
                heap.insert(new No((char) i, f));
            }
        }
        // Caso especial: apenas um caractere distinto
        if (heap.size() == 1) {
            No folha = heap.removeMin();
            No pai = new No((char) 0, folha.frequencia, folha, null);
            heap.insert(pai);
        }
        // Combina até restar um nó (raiz)
        while (heap.size() > 1) {
            No a = heap.removeMin();
            No b = heap.removeMin();
            No pai = new No((char) 0, a.frequencia + b.frequencia, a, b);
            heap.insert(pai);
        }
        return heap.removeMin();
    }

    // Gera códigos recursivamente percorrendo a árvore
    private static void buildCodes(No no, String prefixo, String[] codigos) {
        if (no == null) {
            return;
        }
        if (no.ehFolha()) {
            int idx = no.caractere & 0xFF;
            codigos[idx] = prefixo.isEmpty() ? "0" : prefixo;
        } else {
            buildCodes(no.esquerda, prefixo + '0', codigos);
            buildCodes(no.direita, prefixo + '1', codigos);
        }
    }

    // Mede tempo de execução e imprime resultado
    public static void measureElapsedTime(String titulo, Runnable tarefa) {
        long inicio = System.nanoTime();
        tarefa.run();
        long fim = System.nanoTime();
        long nanos = fim - inicio;
        double milissegundos = nanos / 1_000_000.0;
        System.out.printf("%s concluído em %.3f ms%n", titulo, milissegundos);
    }

    // Main method - processa args da linha de comando
    public static void main(String[] args) {
        if (args.length != 3) {
            printInstructions();
            return;
        }
        String modo = args[0];
        String entrada = args[1];
        String saida = args[2];
        try {
            switch (modo) {
                case "-c":
                    measureElapsedTime("Compressão", () -> {
                        try {
                            compress(entrada, saida);
                        } catch (Exception e) {
                            System.err.println("Erro na compressão: " + e.getMessage());
                        }
                    });
                    break;
                case "-d":
                    measureElapsedTime("Descompressão", () -> {
                        try {
                            decompress(entrada, saida);
                        } catch (Exception e) {
                            System.err.println("Erro na descompressão: " + e.getMessage());
                        }
                    });
                    break;
                default:
                    printInstructions();
                    break;
            }
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }

    // Imprime usage instructions
    private static void printInstructions() {
        System.out.println("Uso:\n  java Huffman -c <entrada> <saida>  // comprimir\n  java Huffman -d <entrada> <saida>  // decompress");
    }
}