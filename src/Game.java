import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    //creating board
    public GameItem[][] board = new GameItem[4][4];

    //creat an ArrayList to hold grids that are assigned with GameItems
    ArrayList<GameItem> taken = new ArrayList<>();

    //Define class variables
    Random random = new Random();
    int playerX;
    int playerY;
    int wumpusX;
    int wumpusY;
    int pitX ;
    int pitY;
    int goldX;
    int goldY;
    int goldFound = 0;
    int goldGenerated = random.nextInt(3) + 1;

    public void runGame() {
        setBoard();
        display();
        menu();
    }

    //Instantiate board as cleargound
    private void setBoard() {
        for (int i = 0; i <board.length ; i++) {
            for (int j = 0; j <board[0].length ; j++) {
                board[i][j] = new ClearGround('.');
            }
        }
    //Place player on board
        playerX = random.nextInt(board.length);
        playerY = random.nextInt(board[0].length);
        taken.add(board[playerX][playerY]);

    //Place Wumpus on board
        int wumpusCount = 0;
        while (wumpusCount!=1){
            wumpusX = random.nextInt(board.length);
            wumpusY = random.nextInt(board[0].length);
            if (wumpusX != playerX && wumpusY != playerY) {
                board[wumpusX][wumpusY] = new Wumpus('W');
                taken.add(board[wumpusX][wumpusY]);
                wumpusCount++;
            }
        }

    //Place 3 pits on board
        int pitCount = 0;
        while (pitCount <3) {
            pitX = random.nextInt(board.length);
            pitY = random.nextInt(board.length);
            if (!taken.contains(board[pitX][pitY])) {
                board[pitX][pitY] = new Pit('P');
                taken.add(board[pitX][pitY]);
                pitCount++;
            }
        }

    //Place 1 to 3 gold on board
        int goldCount = 0;
        while (goldCount != goldGenerated) {
            goldX = random.nextInt(board.length);
            goldY = random.nextInt(board.length);
            if (!taken.contains(board[goldX][goldY])) {
                board[goldX][goldY] = new Gold('G');
                taken.add(board[goldX][goldY]);
                goldCount++;
            }
        }
    }

    //Display board with all items
    private void display() {
        for (int i = 0; i < board.length; i++) {
            System.out.print("|");
            for (int j = 0; j < board[0].length; j++) {
                if (i==playerX&&j==playerY) {
                    System.out.print('*');
                }
                board[i][j].display();
                System.out.print("|");
            }
            System.out.println();
        }
    }

    //Sence Wumpus and object on board
    private void senseNearby() {

        int topX = playerX-1;
        if (topX<0) {
            topX = board.length-1;
        }

        int topY = playerY;

        int bottomX = playerX +1;
        if (bottomX > board.length-1) {
            bottomX = 0;
        }

        int bottomY = playerY;

        int leftX = playerX;

        int leftY = playerY - 1;
        if (leftY < 0) {
            leftY = board.length-1;
        }

        int rightX = playerX;

        int rightY = playerY + 1;
        if (rightY > board.length-1) {
            rightY = 0;
        }
        //Using instance of function to determine what to sense also polymorphoriam to
        // delegate sense function to subclasses
        if (board[topX][topY] instanceof Gold || board[topX][topY] instanceof Pit ||
                board[topX][topY] instanceof Wumpus) {
            System.out.println("The player sensed a " + board[topX][topY].sense() + " from top");
        }
        if (board[bottomX][bottomY] instanceof Gold || board[bottomX][bottomY] instanceof Pit ||
                board[bottomX][bottomY] instanceof Wumpus) {
            System.out.println("The player sensed a " + board[bottomX][bottomY].sense() + " from bottom");
        }
        if (board[leftX][leftY] instanceof Gold ||board[leftX][leftY] instanceof Pit ||
                board[leftX][leftY] instanceof Wumpus) {
            System.out.println("The player sensed a " + board[leftX][leftY].sense() + " from left");
        }
        if (board[rightX][rightY] instanceof Gold || board[rightX][rightY] instanceof Pit ||
                board[rightX][rightY] instanceof Wumpus) {
            System.out.println("The player sensed a " + board[rightX][rightY].sense() + " from right");
        }
        System.out.println();
    }

    //Input menu
    private void menu() {
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("====Wumpus Menu====");
            System.out.println("1.Move player left");
            System.out.println("2.Move player right");
            System.out.println("3.Move player up");
            System.out.println("4.Move player down");
            System.out.println("5.Quit");
            try {
                int input = in.nextInt();
                switch (input) {
                    case 1:
                        moveLeft();
                        break;
                    case 2:
                        moveRight();
                        break;
                    case 3:
                        moveUp();
                        break;
                    case 4:
                        moveDown();
                        break;
                    case 5:
                        System.out.println("exit");
                        System.exit(0);
                        in.close();
                        break;

                    default:
                        System.out.println("Please select a number between 1 to 5");
                        menu();

                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            //If player mioves to gold the gold is replaced by clearground and gold found +1
            if (board[playerX][playerY] instanceof Gold) {
                System.out.println("You have found one Gold!");
                System.out.println();
                board[playerX][playerY] = new ClearGround('.');
                goldFound++;

                //Game ends if all gold is found
                if (goldFound == goldGenerated) {
                    System.out.println("You have found all the gold. Game Ended");
                    System.exit(0);
                }

                //If player runs into Wupus the player dies and game ends
            } else if (board[playerX][playerY] instanceof Wumpus) {
                System.out.println("You run into the Wumpus. Game Lost");
                System.exit(0);

                //If player falls into pit the player dies and game ends
            } else if (board[playerX][playerY] instanceof Pit) {
                System.out.println("You fell into a Pit. Game Lost");
                System.exit(0);
            }
            display();
            senseNearby();
            menu();
        }
        while (in.nextInt()<=0&&in.nextInt() > 5);
    }

    public void moveDown() {
        playerX+=1;
        if (playerX > board.length-1) {
            playerX = 0;
        }
    }

    public void moveUp() {
        playerX-=1;
        if (playerX < 0) {
            playerX = board.length - 1;
        }
    }

    public void moveRight() {
        playerY += 1;
        if (playerY > board[0].length-1) {
            playerY = 0;
        }
    }

    public void moveLeft() {
        playerY -= 1;
        if (playerY < 0) {
            playerY = board[0].length - 1;
        }
    }

}
