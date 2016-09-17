package exel.main;

import excel.exeptions.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Belousov on 11.09.2016.
 */
public class Table {
    private Object [][] _cellObj;
    private TypesCell [][] _typeCell;
    private int _leightLine;
    private int _leightColums;

    private static final String NULL_CONST  = "NULL";
    private static final String ALPH = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String OPER = "+-*/";

    private static final String TEXT_CONST = "`";
    private static final String FUNC_CONST = "=";
    private static final String ERROR_CONST = "#";


    private enum TypesCell {NULL, NUM, TEXT, FUNC, ERROR, NAN}

    private TypesCell getType(String word)
    {
        String nums = "1234567890";
        if(word.length() == 0)
            return  TypesCell.ERROR;

        if(TEXT_CONST.charAt(0) == word.charAt(0))
            return TypesCell.TEXT;

        if(word == NULL_CONST)
            return TypesCell.NULL;

        if(FUNC_CONST.charAt(0) == word.charAt(0))
            return TypesCell.FUNC;


        for(int i=0; i<word.length(); i++)
        {
            Character ch = word.charAt(i);
            if(!nums.contains(ch.toString()))
                return TypesCell.ERROR;
        }

        return TypesCell.NUM;
    }

    public  Table(String pathToFile)
    {
        writeFromFile(pathToFile);
    }

   private void initTable(int sizeColums, int sizeLines)
    {
        if(sizeColums <= 0 || sizeLines <= 0)
            return;

        _leightLine = sizeLines;
        _leightColums = sizeColums;

        _cellObj = new Object[sizeColums][sizeLines];
        _typeCell = new TypesCell[sizeColums][sizeLines];

        for(int i=0; i< _cellObj.length; i++ )
            for(int j=0; j< _cellObj[i].length; j++) {
                _cellObj[i][j] = NULL_CONST;
                _typeCell[i][j] = TypesCell.NAN;
            }
    }


    private Boolean writeFromFile(String pathToFile)
    {
        InputStream inputStream = null;
        String input = "";
        int sizeX = -1;
        int sizeY = -1;
        boolean firstLine = true;
        try
        {
            inputStream = new FileInputStream(pathToFile);
            int readSymb = inputStream.read();
            while( readSymb != -1) {
                input += (char) readSymb;
                readSymb = inputStream.read();
            }
        }
        catch (Exception e){
            return  false;
        }

        writeCellsFromLine(input);
        return true;
    }

    private void initByFirstLine(String firstLine)
    {
        firstLine = firstLine.replace("\r", "");
        firstLine = firstLine.replace("\n", "");
        String[] sizes = firstLine.split("\t");
        int sizeX = Integer.parseInt(sizes[0]);
        int sizeY = Integer.parseInt(sizes[1]);
        initTable(sizeX, sizeY);
    }

    private void writeCellsFromLine(String line)
    {
        boolean lineOverhead = false;
        boolean columsOverhead = false;
        line = line.replace("\r", "");

        String[] splitToLine = line.split("\n");
        if(splitToLine.length >= _leightColums)
            lineOverhead = true;

        initByFirstLine(splitToLine[0]);


        for(int i=1; i<splitToLine.length; i++)
        {
            if(i-1 == _leightColums)
                break;
            String[] cells =  splitToLine[i].split("\t");

            for(int j=0 ; j< cells.length; j++) {
                if(j == _leightLine)
                    break;

                String word = cells[j];
                _cellObj[i-1][j] = word;
            }
        }
    }

    public void culcAllCells()
    {
        boolean[][] wayRecursion = new boolean[_leightColums][_leightLine];

        String CyrlicExeption = ERROR_CONST + "cyrcle";
        String NullRefence = ERROR_CONST + "non-exist";
        String UnrecognInput = ERROR_CONST + "wr_inp";
        String UnrecognRef = ERROR_CONST + "wr_ref";
        String WrongFunction = ERROR_CONST + "wr_fnc";
        String UnknowExeption = ERROR_CONST + "Unk_Err";

        for(int i=0; i<_leightColums; i++)
            for (int j = 0; j < _leightLine; j++) {
                resetWayRecursion(wayRecursion);
                try {
                    culcCell(i, j, wayRecursion);
                }
                catch (CyclicReferenceExeption e)     {
                    System.out.println(e.getMessage());

                    for (int k = 0; k < _leightColums; k++) {
                        for (int l = 0; l < _leightLine; l++) {
                            if (wayRecursion[k][l]) {
                                _cellObj[k][l] = CyrlicExeption;
                                _typeCell[k][l] = TypesCell.ERROR;
                            }
                        }
                    }
                }
                catch (NullReferenceExeption e) {
                    System.out.println(e.getMessage());
                    _cellObj[i][j] = NullRefence;
                    _typeCell[i][j] = TypesCell.ERROR;
                }
                catch (UnrecognizableInputExeption e) {
                    System.out.println(e.getMessage());
                    _cellObj[i][j] = UnrecognInput;
                    _typeCell[i][j] = TypesCell.ERROR;
                }
                catch (UnrecognuzableReferenceExeption e){
                    System.out.println(e.getMessage());
                    _cellObj[i][j] = UnrecognRef;
                    _typeCell[i][j] = TypesCell.ERROR;
                }
                catch (WrongFunctionExeption e){
                    System.out.println(e.getMessage());
                    _cellObj[i][j] = WrongFunction;
                    _typeCell[i][j] = TypesCell.ERROR;
                }
                catch (Exception e)   {
                    System.out.println(e.toString());
                    _cellObj[i][j] = UnknowExeption;
                    _typeCell[i][j] = TypesCell.ERROR;
                }
            }
    }

    private void culcCell(int indexColumn, int indexLine, boolean[][] wayRecursion) throws UnrecognizableInputExeption , NullReferenceExeption, UnrecognuzableReferenceExeption
    {
        int i = indexColumn;
        int j = indexLine;

        if(_typeCell[i][j] != TypesCell.NAN)
            return;

        String cellText = _cellObj[i][j].toString();

        TypesCell typeCell = getType(cellText);
        if( typeCell == TypesCell.TEXT)
        {
            StringBuilder word = new StringBuilder(cellText);
            word.delete(0,1);
            _cellObj[i][j] = word.toString();
            _typeCell[i][j] = TypesCell.TEXT;
        }

        if( typeCell == TypesCell.NUM) {
            _cellObj[i][j] = Integer.parseInt(cellText);
            _typeCell[i][j] = TypesCell.NUM;
        }

        if (typeCell == TypesCell.ERROR) { // если не разпознала
            _cellObj[i][j] = ERROR_CONST + "Err_Inp";
            _typeCell[i][j] = TypesCell.ERROR;
            throw new UnrecognizableInputExeption("(" + i +":" + j + ") Error Input" );
        }

        if(typeCell == TypesCell.FUNC)
        {
            wayRecursion[i][j] = true;
            _cellObj[i][j] = replaceRef(cellText, wayRecursion);
            _typeCell[i][j] = TypesCell.FUNC;

            _cellObj[i][j] = Calculator.culcFunc(_cellObj[i][j].toString());

        }
    }


    private String replaceRef(String funcText, boolean[][] wayRecursion) throws UnrecognuzableReferenceExeption, NullReferenceExeption, CyclicReferenceExeption {
        StringBuilder func = new StringBuilder(funcText);
        func.delete(0,1);
        // разбиваем на елементы
        String tokens = "";  // елементы отделены пробелом
        Character tmp;
        for(int i=0; i<func.length(); i++)
        {
            tmp = func.charAt(i);
            if(OPER.contains(tmp.toString())) { // могут создаватс€ пустые €чейки "" при ошиочном вводе 2 операторов вместе
                tokens += " ";
                tokens += tmp;
                tokens += " ";
            }
            else
                tokens += tmp;
        }
        String[] tokensArr = tokens.split(" ");
        TypesCell type;
        for (int i = 0; i < tokensArr.length; i++) { // ищем ссылки на другие €чейки и замен€ем их
            type = getType(tokensArr[i]);
            if(type != TypesCell.NUM )
            {
                if (tokensArr[i].length() == 0) // может случитьс€ только при ошибке ввода формулы
                    throw new WrongFunctionExeption("Wrong Function : "  + funcText);
                Character ch = tokensArr[i].charAt(0);
                if(!OPER.contains(ch.toString()))
                {
                    AtomicReference indexLine = new AtomicReference(0);
                    AtomicReference indexColum = new AtomicReference(0);

                    if( !cellIndexToCoord(tokensArr[i], indexColum, indexLine )) // если неправильный индекс’
                         throw new UnrecognuzableReferenceExeption("Error recognizabel reference: " + tokensArr[i].toString() );

                    int indexLineRef = (int)indexLine.get();
                    int indexColumRef = (int)indexColum.get();

                    if(!isCorrectReference(indexColumRef, indexColumRef))
                            throw new NullReferenceExeption("Ref to non-ex cell: " + tokensArr[i] );
                    ///TODO

                    if(wayRecursion[indexColumRef][indexLineRef]) // если зациклило
                        throw new CyclicReferenceExeption("Start cycle in ref: " + tokensArr[i]);

                    wayRecursion[indexColumRef][indexLineRef] = true;
                    culcCell(indexColumRef, indexLineRef, wayRecursion); // калькул€ци€ непросчитаного индекса




                    tokensArr[i] = _cellObj[indexColumRef][indexLineRef].toString();  // замена
                }
            }
        }

        tokens = "";
        for (int i = 0; i < tokensArr.length; i++) {
            tokens += tokensArr[i];
            if(i != tokensArr.length -1 )
                tokens += " ";
        }

        return tokens;
    }


    private String replaceRef(int indexColumn, int indexLine , boolean[][] watRec) throws UnrecognuzableReferenceExeption, NullReferenceExeption, CyclicReferenceExeption
    {
       return replaceRef(_cellObj[indexColumn][indexLine].toString(), watRec);
    }

    private void resetWayRecursion(boolean[][] wayRecursion)
    {
        for (int i=0; i<wayRecursion.length; i++)
            for (int j = 0; j < wayRecursion[0].length; j++) {
                wayRecursion[i][j] = false;
            }
    }

    private boolean isCorrectReference(int indexColum, int indexLine)
    {
        if(indexColum > _leightColums && indexLine > _leightLine)
            return false;

        return !(indexColum < 0 || indexLine < 0);

    }


    private boolean cellIndexToCoord(String index, AtomicReference outColumCord, AtomicReference outLineCord)
    {
        if(index.length() < 2 )
            return false;

        StringBuilder indexBulder = new StringBuilder(index);
        Character firstLetter = index.charAt(0);
        Character secondLetter = index.charAt(1);
        int firstindex;
        int secondIndex;
        firstindex = ALPH.indexOf(firstLetter);
        secondIndex = ALPH.toLowerCase().indexOf(secondLetter);

        if( firstindex == -1)
            return false;

        if(secondIndex == -1)
             outLineCord.set(firstindex);
        else
            outLineCord.set(ALPH.length()*(firstindex+1) + secondIndex);

        indexBulder.delete(0,1);
        if(secondIndex != -1)
            indexBulder.delete(0,1);

        outColumCord.set( Integer.parseInt(indexBulder.toString()) -1 ); // A1 == [0][0]

        return true;
    }

    public void printTable()
    {
        String res = toString();
        System.out.println(res);
    }
    @Override
    public String toString() {

        String output = "";
        for (int i = 0; i < _cellObj.length; i++) {
            for (int j = 0; j < _cellObj[i].length; j++) {
                output += _cellObj[i][j].toString();
                output += "\t";
            }
            output += "\n";
        }
      return output;
    }
}