import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static String LocallHost = "192.168.15.7";
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket(LocallHost, 4000);
            System.out.println("Conectado com sucesso");

            InputThread inputThread = new InputThread(clientSocket);
            inputThread.start();

        }catch (Exception e){
            System.out.println("Erro ao conectar");
        }
    }
}
