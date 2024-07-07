public class Main {
    public static void main(String[] args) {
        Game g = new Game(3);

        while (!g.nextTurn()) {}
    } 
}
