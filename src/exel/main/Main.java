package exel.main;
public class Main {

    public static void main(String[] args) {
	    Table miniExel = new Table("C:\\Users\\Belousov\\Documents\\Java projects\\MiniExcel_1\\Input\\TestTable.txt");
        miniExel.culcAllCells();
        miniExel.printTable();

    }
}
