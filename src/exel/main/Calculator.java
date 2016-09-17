package exel.main;

import excel.exeptions.WrongFunctionExeption;

import java.util.ArrayList;

/**
 * Created by Belousov on 12.09.2016.
 */
class Calculator extends ArithmeticException {

    private static final int[] WeightOper = {1,1,2,2}; // приоритет бинарных операций  "+-*/"
    private static final String OPER = "+-*/";


    public static String culcFunc(String func) // все токены разбиты пробелами (только бинарные операции!)
    {
        String[] splitFunc = func.split(" ");
        ArrayList<String> listTokens = new ArrayList<>();

        for (String aSplitFunc : splitFunc) listTokens.add(aSplitFunc);

        int max = findMax(WeightOper);

        for(; max > 0 ; max --)
        {
            for(int i=0; i<listTokens.size(); i++)
            {
                int index = OPER.indexOf(splitFunc[i]); //если оператор
                if( index != -1  ) // проверить уровень
                {

                    if(WeightOper[index] == max)
                    {
                        int res = 0;
                        try {
                            res =  culcNums (listTokens.get(i).charAt(0), listTokens.get(i - 1), listTokens.get(i + 1));
                        }
                        catch (Exception E){
                            throw new WrongFunctionExeption(E.getMessage() + ": `" + func + "`");
                        }
                        for (int j = 0; j < 3; j++) { // удалить 2 числа и оператор между ними
                            listTokens.remove(i-1);
                        }

                        listTokens.add(i-1, Integer.toString(res));
                        i--;
                    }
                }
            }
        }

        if(listTokens.size() > 1)
            throw new WrongFunctionExeption(func);

        return listTokens.get(0);
    }

    private static int findMax(int []array)
    {
        int max = Integer.MIN_VALUE;
        for (int anArray : array) {
            if (anArray > max)
                max = anArray;
        }
        return max;
    }
    private static int culcNums(char Oper, String num1, String num2)
    {
        int _num1 = 0;
        int _num2 = 0;
        try {
            _num1 = Integer.parseInt(num1);
            _num2 = Integer.parseInt(num2);
        }
        catch (Exception e)
        {
            throw e;
        }
        if(Oper == '+')
            return _num1+_num2;
        if(Oper == '-')
            return _num1 - _num2;
        if(Oper == '*')
            return _num2*_num1;
        if(Oper == '/') {
            if(_num2 == 0)
                throw new WrongFunctionExeption("Dif by 0");
            return _num1 / _num2;
        }
        return 0;

    }
}
