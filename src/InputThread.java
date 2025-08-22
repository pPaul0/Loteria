import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class InputThread extends Thread {
    private Socket socket;
    private Scanner sc = new Scanner(System.in);

    public InputThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintStream saida = new PrintStream(socket.getOutputStream());

            while (true) {
                String linha = sc.nextLine().trim();
                if (linha.isEmpty()) continue;

                saida.println(linha);
                saida.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
