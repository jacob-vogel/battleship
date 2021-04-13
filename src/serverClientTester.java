import com.company.Player;

public class serverClientTester {
    public static void main(String[] args){
        Player player1 = new Player(true);
        Player player2 = new Player(false);
        if(player1.isConnected()){
            System.out.println("Player 1 is connected to Server");
        }
        if(player2.isConnected()){
            System.out.println("Player 2 is connected to Server");
        }
    }
}
