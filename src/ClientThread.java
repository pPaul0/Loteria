import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {
    private Socket socket;
    private PrintStream saida;

    private int inicio;
    private int fim;
    private int qtd;

    // lista de apostas do cliente
    private List<List<Integer>> apostas = new ArrayList<>();

    public ClientThread(Socket socket) {
        this.socket = socket;
        try {
            this.saida = new PrintStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                // lê os dados iniciais
                inicio = Integer.parseInt(reader.readLine());
                fim = Integer.parseInt(reader.readLine());
                qtd = Integer.parseInt(reader.readLine());

                // lê os números da aposta
                List<Integer> aposta = new ArrayList<>();
                for (int i = 0; i < qtd; i++) {
                    String linha = reader.readLine();
                    if (linha != null) {
                        aposta.add(Integer.parseInt(linha));
                    }
                }

                // armazena aposta
                adicionarAposta(aposta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
            Main.clienteSaiu();
        }
    }

    // métodos sincronizados para acessar/manipular a lista de apostas
    public synchronized void adicionarAposta(List<Integer> aposta) {
        apostas.add(aposta);
    }

    public synchronized List<List<Integer>> pegarEZerarApostas() {
        List<List<Integer>> copia = new ArrayList<>(apostas);
        apostas.clear();
        return copia;
    }

    public synchronized int getInicio() { return inicio; }
    public synchronized int getFim() { return fim; }
    public synchronized int getQtd() { return qtd; }

    public synchronized void enviarMensagem(String msg) {
        saida.println(msg);
        saida.flush();
    }
}
