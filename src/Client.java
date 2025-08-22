import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4000);

            // thread para enviar comandos e apostas
            InputThread inputThread = new InputThread(socket);
            inputThread.start();

            // thread para receber mensagens do servidor
            ClientReceiverThread receiverThread = new ClientReceiverThread(socket);
            receiverThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
