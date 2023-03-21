public class Grid{
    private int length;
    private int width;
    Minesweeper Minesweeper = new Minesweeper();
    String grid = "";


    public Grid(int length, int width){
        this.length = length;
        this.width = width;
    }

    public Grid(String difficulty) {
        if (difficulty.equals("easy")) {
            length = 8;
            width = 8;
        } else if (difficulty.equals("medium")) {
            length = 14;
            width = 14;
        } else if (difficulty.equals("hard")) {
            length = 20;
            width = 20;
        }
    }

    public String getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public void printGrid(){
        //letters on top
        String letters = "    A   B   C   D   E   F   G   H   I   J   K   L   M   N   L   O   Q  R   S   T   U   V   W   X   Y   Z  ";
        grid += "\n" + letters.substring(0,length*4+1) + "\n";
        //number of rows
        for (int i = 1;i<=width;i++){
            //top part of columns
            grid += "  ";
            for (int j = 1;j<=length*4+1;j++){
                grid += "-";
            }

            grid+="\n";
            //numbers on side
            if (i >= 10) {
                grid += i;
            } else {
                grid += i + " ";
            }
            //middle part of columns
            for (int k = 1; k <= length + 1; k++) {
                grid += "|   ";
            }
            grid += "\n";
        }
        // bottom row
        grid += "  ";
        for (int j = 1;j<=length*4+1;j++){
            grid += "-";
        }
        grid += "\n";
        System.out.println(grid);
    }

    public void editGrid(String coords, Boolean flag){
        //formula: (letters + top part + numbers + middle parts + rest of the row)
        int formula = (length*4+3) + ((Integer.parseInt(coords.substring(1)))*(length*4+4)) + (Integer.parseInt(coords.substring(1))*3) + ((Integer.parseInt(coords.substring(1))-1)*(4*(length+1))) + (4 * ((Minesweeper.convertCoordinatesToInt(coords)-1))) + 1;
        grid = grid.substring(0,formula) + "m" + grid.substring(formula + 1);
        System.out.println(grid);
    }

    public void editGrid(String coords, int mines){
        //formula: (letters + top part + numbers + middle parts + rest of the row)
        int formula = (length*4+3) + ((Integer.parseInt(coords.substring(1)))*(length*4+4)) + (Integer.parseInt(coords.substring(1))*3) + ((Integer.parseInt(coords.substring(1))-1)*(4*(length+1))) + (4 * ((Minesweeper.convertCoordinatesToInt(coords)-1))) + 1;

        grid = grid.substring(0,formula) + mines + grid.substring(formula + 1);
        System.out.println(grid);
    }

    public void editGridMine(String coords) {
        int formula = (length*4+3) + ((Integer.parseInt(coords.substring(1)))*(length*4+4)) + (Integer.parseInt(coords.substring(1))*3) + ((Integer.parseInt(coords.substring(1))-1)*(4*(length+1))) + (4 * ((Minesweeper.convertCoordinatesToInt(coords)-1))) + 1;
        grid = grid.substring(0,formula) + "*" + grid.substring(formula + 1);
    }

    public void editGrid2(String coords, int mines){
        //formula: (letters + top part + numbers + middle parts + rest of the row)
        int formula = (length*4+3) + ((Integer.parseInt(coords.substring(1)))*(length*4+4)) + (Integer.parseInt(coords.substring(1))*3) + ((Integer.parseInt(coords.substring(1))-1)*(4*(length+1))) + (4 * ((Minesweeper.convertCoordinatesToInt(coords)-1))) + 1;
        grid = grid.substring(0,formula) + mines + grid.substring(formula + 1);
    }




}