import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int PORTA = 4000;
    private static final int MAX_CLIENTES = 5;

    private static ServerSocket serverSocket;
    private static List<ClientThread> clientes = new ArrayList<>();
    private static List<PrizeDrawThread> prizeThreads = new ArrayList<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORTA);
            System.out.println("Servidor aguardando conexão...");

            while (clientes.size() < MAX_CLIENTES) {
                Socket socket = serverSocket.accept();

                ClientThread clientThread = new ClientThread(socket);
                PrizeDrawThread prizeThread = new PrizeDrawThread(clientThread);

                clientes.add(clientThread);
                prizeThreads.add(prizeThread);

                clientThread.start();
                prizeThread.start();
            }

            // Aguardando todas as threads terminarem
            for (ClientThread ct : clientes) ct.join();
            for (PrizeDrawThread pt : prizeThreads) pt.join();

            System.out.println("Todas as threads encerraram. Servidor finalizando.");
            stopServer();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (IOException ignored) {}
    }
}
