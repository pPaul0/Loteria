import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiverThread extends Thread {
    private Socket socket;

    public ClientReceiverThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha); // imprime na tela
            }

        } catch (Exception e) {
            System.out.println("Conexão com o servidor encerrada.");
        }
    }
}
