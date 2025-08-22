import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientThread extends Thread {
    private Socket socket;
    private volatile boolean running = true;
    private PrintStream saida;

    private int inicio = 0;
    private int fim = 100;
    private int qtd = 5;

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
            // Envia mensagem de conexão com horário
            String horario = new SimpleDateFormat("HH:mm:ss").format(new Date());
            saida.println(horario + ": CONECTADO!!");
            saida.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String linha;

            while (running && (linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                if (linha.startsWith(":")) {
                    String[] partes = linha.split(" ");
                    if (partes.length < 2) {
                        enviarMensagem("Erro: comando inválido. Use :inicio <NUM>, :fim <NUM>, :qtd <NUM>");
                        continue;
                    }

                    switch (partes[0]) {
                        case ":inicio":
                            try {
                                inicio = Integer.parseInt(partes[1]);
                                enviarMensagem("Inicio atualizado: " + inicio);
                            } catch (NumberFormatException e) {
                                enviarMensagem("Erro: número inválido para :inicio");
                            }
                            break;

                        case ":fim":
                            try {
                                fim = Integer.parseInt(partes[1]);
                                enviarMensagem("Fim atualizado: " + fim);
                            } catch (NumberFormatException e) {
                                enviarMensagem("Erro: número inválido para :fim");
                            }
                            break;

                        case ":qtd":
                            try {
                                qtd = Integer.parseInt(partes[1]);
                                enviarMensagem("Qtd de números atualizada: " + qtd);
                            } catch (NumberFormatException e) {
                                enviarMensagem("Erro: número inválido para :qtd");
                            }
                            break;

                        default:
                            enviarMensagem("Comando desconhecido: " + partes[0]);
                            break;
                    }

                } else {
                    // Aposta
                    String[] numeros = linha.split(" ");
                    if (numeros.length != qtd) {
                        enviarMensagem("Número de apostas incorreto. Deve enviar " + qtd + " números.");
                        continue;
                    }

                    List<Integer> aposta = new ArrayList<>();
                    try {
                        for (String n : numeros) {
                            int num = Integer.parseInt(n);
                            if (num < inicio || num > fim) throw new NumberFormatException();
                            aposta.add(num);
                        }
                        adicionarAposta(aposta);
                        enviarMensagem("Aposta registrada: " + aposta);
                    } catch (NumberFormatException e) {
                        enviarMensagem("Erro: números inválidos ou fora do intervalo (" + inicio + "-" + fim + ").");
                    }
                }
            }

        } catch (java.net.SocketException e) {
            System.out.println("Cliente desconectou: " + socket.getInetAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharSocket();
        }
    }

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

    public void stopThread() {
        running = false;
        fecharSocket();
    }

    private void fecharSocket() {
        try {
            if (!socket.isClosed()) socket.close();
        } catch (Exception ignored) {}
    }
}
