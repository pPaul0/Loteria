import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int PORTA = 4000;
    private static final int MAX_CLIENTES = 5;
    private static int activeClients = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor aguardando conexão...");

            while (true) {
                if (activeClients < MAX_CLIENTES) {
                    Socket socket = serverSocket.accept();
                    activeClients++;

                    ClientThread clientThread = new ClientThread(socket);
                    PrizeDrawThread prizeDrawThread = new PrizeDrawThread(clientThread);
                    prizeDrawThread.start();
                    clientThread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void clienteSaiu() {
        activeClients--;
    }
}
