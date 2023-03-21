import java.util.Scanner;
public class Minesweeper {
    private int length;
    private int width;
    Scanner scan = new Scanner(System.in);
    private Grid grid;
    private String mineCoords;
    private int numMines;
    private int nearbyMines;

    public Minesweeper(){
        length = 0;
        width = 0;
        grid = null;
        mineCoords = "";
        numMines = 0;
        nearbyMines = 0;
    }

    public void play() {
        // grid size
        System.out.print("Select a difficulty (easy, medium, hard, custom): ");
        String difficulty = scan.nextLine();
        if (!difficulty.equals("custom")) {
            grid = new Grid(difficulty);
            if (difficulty.equals("easy")) {
                numMines = 10;
            } else if (difficulty.equals("medium")) {
                numMines = 40;
            } else if (difficulty.equals("hard")) {
                numMines = 90;
            }
            length = grid.getLength();
            width = grid.getWidth();
        } else {
            System.out.print("Select a grid length (maximum 20): ");
            length = scan.nextInt();
            scan.nextLine();
            System.out.print("Select a grid width (maximum 20): ");
            width = scan.nextInt();
            scan.nextLine();
            grid = new Grid(length, width);

            // mine generation
            System.out.print("Select a number of mines(Please choose a maximum of " + length * width / 2 + "): ");
            numMines = scan.nextInt();
            scan.nextLine();
            while(numMines>length * width / 2){
                System.out.print("Please choose a maximum of " + length * width / 2 + ": ");
                numMines = scan.nextInt();
                scan.nextLine();
            }
        }

        generateMines();

        // start of game
        grid.printGrid();
        System.out.print("Choose a coordinate (eg. a5) or place a marker (eg. a5m): ");
        String coords = scan.nextLine();
        boolean mark = false;
        while(checkDuplicates(surroundingCoordinates(coords),mineCoords)>0){
            mineCoords = "";
            generateMines();
        }
        if (coords.substring(coords.length()-1,coords.length()).equals("m")){
            mark = true;
            coords = coords.substring(0,coords.length()-1);
        }
        if(mineCoords.indexOf(coords)!=-1 && mineCoords.indexOf(coords)+coords.length() == mineCoords.indexOf(" ",mineCoords.indexOf(coords)+1)&&mark==false){
            if(mineCoords.substring(mineCoords.indexOf(coords),mineCoords.indexOf(" ",mineCoords.indexOf(coords))).equals(coords)){
                mineCoords = mineCoords.substring(0,mineCoords.indexOf(coords)) + mineCoords.substring(mineCoords.indexOf(" ",mineCoords.indexOf(coords)));
                int tempNumMines = numMines;
                numMines = 1;
                generateMines();
                numMines = tempNumMines;
            } else{
                String tempMineCoords = mineCoords.substring(0,mineCoords.indexOf(" ",mineCoords.indexOf(coords))+1);
                mineCoords = mineCoords.substring(mineCoords.indexOf(" ",mineCoords.indexOf(coords))+1);
                mineCoords = tempMineCoords + mineCoords.substring(0,mineCoords.indexOf(coords))+mineCoords.substring(mineCoords.indexOf(" ",mineCoords.indexOf(coords)));
                int tempNumMines = numMines;
                numMines = 1;
                generateMines();
                numMines = tempNumMines;
            }
        }

        start(coords);
        boolean run = true;
        while(run){
            if (mineCoords.indexOf(coords)!=-1 && mineCoords.indexOf(coords)+coords.length() == mineCoords.indexOf(" ",mineCoords.indexOf(coords)+1)&&mark==false){
                run = false;
                System.out.println("Game Over, You lose!");
                fullGrid();
            } else {
                if (mark == true) {
                    grid.editGrid(coords, mark);
                } else {
                    if(surroundingMines(coords)==0){
                        start(coords);
                    }
                    grid.editGrid(coords, surroundingMines(coords));

                }
                if(grid.getGrid().indexOf("   |") == -1){
                    run = false;
                    System.out.println("You win!");
                }
                if(run){
                    System.out.println();
                    System.out.print("Choose a coordinate (eg. a5) or place a marker (eg. a5m): ");
                    coords = scan.nextLine();
                    if (coords.substring(coords.length()-1,coords.length()).equals("m")){
                        mark = true;
                        coords = coords.substring(0,coords.length()-1);
                    } else {
                        mark = false;
                    }
                }
            }
        }
    }


    //returns numeric value of letter
    public int convertCoordinatesToInt(String coords){
        String letter = coords.substring(0,1).toLowerCase();
        return letter.compareTo("a") + 1;
    }

    //returns letter of a number
    public String convertCoordinatesToString(int coords){
        String letters = "abcdefghijklmnopqrstuvwxyz";
        return letters.substring(coords-1,coords);
    }


    //returns the coordinates of the spaces around mid
    private String surroundingCoordinates(String mid){

        String surrounding = "";
        int midRow = Integer.parseInt(mid.substring(1));
        int upperRow = midRow-1;
        int lowerRow = midRow+1;
        int midColumn = convertCoordinatesToInt(mid);
        int leftColumn = midColumn-1;
        int rightColumn = midColumn+1;
        surrounding += "" + leftColumn + "/" + upperRow + " ";
        surrounding += "" + midColumn + "/" + upperRow + " ";
        surrounding += "" + rightColumn + "/" + upperRow + " ";
        surrounding += "" + leftColumn + "/" + midRow + " ";
        surrounding += "" + rightColumn + "/" + midRow + " ";
        surrounding += "" + leftColumn + "/" + lowerRow + " ";
        surrounding += "" + midColumn + "/" + lowerRow + " ";
        surrounding += "" + rightColumn + "/" + lowerRow + " ";

        String newSurroundings = "";
        for (int i = 1;i<=8;i++){
            String tempCoord = surrounding.substring(0,surrounding.indexOf(" "));
            int coordLength = Integer.parseInt(tempCoord.substring(0,surrounding.indexOf("/")));
            int coordWidth = Integer.parseInt(tempCoord.substring(surrounding.indexOf("/")+1));
            if (coordLength>0 && coordLength <= length && coordWidth <= width && coordWidth>0){
                newSurroundings += convertCoordinatesToString(coordLength) + coordWidth + " ";
            }
            surrounding = surrounding.substring(surrounding.indexOf(" ")+1);
        }
        return newSurroundings;
    }

    //returns the number of mines around coords
    private int surroundingMines(String coords){
        return checkDuplicates(surroundingCoordinates(coords), mineCoords);
    }


    //checks how many times something in one string appears in the other
    private int checkDuplicates(String dupes,String newDupes){
        int count = 0;
        String dupe1 = dupes;
        while(dupe1.indexOf(" ")!=-1){
            int space = dupe1.indexOf(" ");
            String tempCoord = dupe1.substring(0,space);
            String dupe2 = newDupes;
            while(dupe2.indexOf(" ")!=-1){
                int space1 = dupe2.indexOf(" ");
                String tempCoord1 = dupe2.substring(0,space1);
                if(tempCoord.equals(tempCoord1)){
                    count++;
                    dupe2 = dupe2.substring(0,space1-tempCoord1.length())+dupe2.substring(space1+1);
                }
                if(space1+1<=dupe2.length()){
                    dupe2 = dupe2.substring(space1+1);
                }

            }
            if(space+1<=dupe1.length()){
                dupe1 = dupe1.substring(space+1);
            }
        }
        return count;
    }

    //removes similar coordinates
    private String removeDuplicates(String str){
        String newStr = "";
        while (str.contains(" ")){
            String coord = str.substring(0,str.indexOf(" "));
            str = str.substring(str.indexOf(" ")+1);
            if(!str.contains(coord) && str.indexOf(coord)+coord.length() != str.indexOf(" ")){
                newStr+=coord + " ";
            }
        }
        return newStr;
    }

    //creates the mines
    private void generateMines(){
        for (int i = 0; i < numMines;i++) {
            int row = (int) (Math.random() * grid.getWidth()) + 1;
            String column = convertCoordinatesToString((int)(Math.random() * grid.getLength()) + 1);
            String newCoord = column + row;
            if (mineCoords.indexOf(newCoord) != -1 && !mineCoords.substring(mineCoords.indexOf(newCoord), mineCoords.indexOf(newCoord) + newCoord.length()+1).equals(newCoord)) {
                i--;
            } else {
                mineCoords += newCoord + " ";
            }
        }
    }

    //displays all empty spots and nearby mines for the center coords
    private void start(String coords){
        String inner = coords;
        String outer = surroundingCoordinates(coords);
        int rounds = 0;
        if(length>width){
            rounds = length;
        } else {
            rounds = width;
        }
        for(int i = 0;i<rounds;i++){
            String tempOuter = new String(outer);
            while(tempOuter.indexOf(" ") != -1){
                String coord = tempOuter.substring(0,tempOuter.indexOf(" "));
                if(checkDuplicates(inner,surroundingCoordinates(coord))>0){
                    if(surroundingMines(coord)==0){
                        grid.editGrid2(coord,0);
                    } else{
                        grid.editGrid2(coord,surroundingMines(coord));
                    }
                }
                tempOuter = tempOuter.substring(tempOuter.indexOf(" ")+1);
            }
            inner = new String(outer);
            tempOuter = new String(outer);


            outer = "";
            while(tempOuter.contains(" ")){
                String coord2 = tempOuter.substring(0,tempOuter.indexOf(" "));
                if(surroundingMines(coord2) == 0){
                    outer += surroundingCoordinates(coord2);
                }
                tempOuter = tempOuter.substring(tempOuter.indexOf(" ")+1);
            }
            outer = removeDuplicates(outer);
        }
    }
    //displays the entire grid
    private void fullGrid() {
        for (int a = 1; a <= length; a ++) {
            for (int b = 1; b <= width; b ++) {
                String coords = convertCoordinatesToString(a) + b;
                if (mineCoords.indexOf(coords + " ") != -1)  {
                    grid.editGridMine(coords);
                }
            }
        }
        System.out.println(grid.getGrid());
    }
}
