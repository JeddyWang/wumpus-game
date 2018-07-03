public abstract class GameItem {

    private char c;
    public GameItem(char c) {
        this.c = c;
    }
    public void display() {
        System.out.print(c);
    }

    public abstract String sense();


}
