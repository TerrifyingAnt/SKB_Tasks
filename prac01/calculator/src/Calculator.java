import java.util.List;
import java.util.Stack;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Calculator {

    Stack<String> stack = new Stack<>();
    Stack<String> postfix = new Stack<>();
    List<String> infixTokens = new ArrayList<>();
    List<String> fullInfixTokens = new ArrayList<>();
    List<Complex> complexs = new ArrayList<>();
    Stack<String> operands = new Stack<>();
    Stack<String> tempDigits = new Stack<>();
    Stack<String> operators = new Stack<>();

    String temp = "";

    // Парсер выражения
    public void tokenize(String expression) {
        complexs = new ArrayList<>();
        postfix = new Stack<>();
        infixTokens = new ArrayList<>();
        fullInfixTokens = new ArrayList<>();
        operators = new Stack<>();
        operands = new Stack<>();
        tempDigits = new Stack<>();
        //expression = expression.replaceAll("\\s+", "");
        for(int i = 0; i < expression.length(); i++) {
            if(isOperator(String.valueOf(expression.charAt(i)))) {
                infixTokens.add(String.valueOf(expression.charAt(i)));
            }
            else {
                if(Character.isDigit(expression.charAt(i))) {
                    int k = 0;
                    while (i + k < expression.length() && (
                            Character.isDigit(expression.charAt(i + k)) ||
                            expression.charAt(i + k) == '.' || 
                            expression.charAt(i + k) == 'i'
                        )) {
                        temp += String.valueOf(expression.charAt(i + k));
                        k++;
                    }
                    if(temp.indexOf("i") != -1){
                        String imPart = "";
                        for(int j = 0; j < temp.length() - 1; j++) {
                            imPart += temp.charAt(j);
                        }
                        Complex complex = new Complex(0, Double.parseDouble(imPart));
                        //System.out.println("Complexs" + String.valueOf(complexs.size()) + ": " + imPart);
                        temp = "complexs" + String.valueOf(complexs.size());

                        complexs.add(complex);
                    }
                    infixTokens.add(temp);
                    i += k - 1;
                    temp = "";
                }
            }
        }
    }

    // Перевод в польскую нотацию
    public int infixToRpn() {
        for (int i = 0; i < infixTokens.size(); i++) {
            String token = infixTokens.get(i);
 
            if (Character.isDigit(token.charAt(0)))
                postfix.push(token);
 
            else if (token.charAt(0) == '(')
                stack.push(token);

            else if (token.charAt(0) == ')') {
                while (!stack.isEmpty()
                       && stack.peek().charAt(0) != '(')
                    postfix.push(stack.pop());
 
                stack.pop();
            }
 
            else {
                while (
                    !stack.isEmpty()
                    && getPrecedence(token.charAt(0))
                           <= getPrecedence(stack.peek().charAt(0))
                    && hasLeftAssociativity(token.charAt(0))) {
 
                    postfix.push(stack.pop());
                }
                stack.push(token);
            }
        }
 
        while (!stack.isEmpty()) {
            if (stack.peek().charAt(0) == '('){
                System.out.println("This expression is invalid");
                return 0;
            }
            postfix.push(stack.pop());
        }
        return 1;
    }

    // Метод отображения всех элементов выражения
    public void showInfTokens() {
        System.out.println(infixTokens);
    }

    // Метод вывода токенов в порядке польской нотации
    public void showPostfix() {
        System.out.println(postfix);
    }

    // Метод для выявления действий для подсчета
    public String calculate() {
        for(int i = 0; i < postfix.size(); i++) {
            String token = postfix.get(i);
            if(Character.isDigit(token.charAt(token.length() - 1))) {
                tempDigits.push(token);
                //stack.pop();
            }
            else {
                if(tempDigits.size() > 1){
                String n1 = tempDigits.peek();
                tempDigits.pop();
                String n2 = tempDigits.peek();
                tempDigits.pop();
                String sign = token;
                String solved = solve(n2, n1, sign);
                //System.out.println(solved);
                if(!solved.equals(Debug.DIVISION_BY_ZERO) && !solved.equals(Debug.POWER_OF_I))
                    tempDigits.push(solved);
                else{
                    tempDigits.push(solved);
                    return solved;
                }
                //System.out.println("Текущий стек для подсчета: " + String.valueOf(tempDigits));
                //System.out.println(n2 + " " + sign + " " + n1 + " = " + solved);

                if(Debug.debugMode == true)
                    showLittleBlackMagic(n2, n1, sign, solved);
                }
                else {
                    operators.push(token);
                }
            }
        }
        while(operators.size() > 0) {
            String n1 = tempDigits.peek();
                tempDigits.pop();
                String n2 = tempDigits.peek();
                tempDigits.pop();
                String sign = operators.peek();
                String solved = solve(n2, n1, sign);
                //System.out.println(solved);
                if(!solved.equals(Debug.DIVISION_BY_ZERO) && !solved.equals(Debug.POWER_OF_I)){
                    tempDigits.push(solved);
                    operators.pop();
                }
                else{
                    tempDigits.push(solved);
                    //System.out.println(solved);
                    return solved;
                }
                if(Debug.debugMode == true)
                    showLittleBlackMagic(n2, n1, sign, solved);
        }
        if((Character.isDigit(tempDigits.get(0).charAt(0)) || tempDigits.get(0).charAt(0) == '-') && !tempDigits.peek().equals("Zero Division") && !tempDigits.peek().equals("You can't do x power of i")){
            //System.out.println("\nTEMP OPERATION" + tempDigits.get(0));
            return tempDigits.get(0);
        }
        else{
            Complex complex = new Complex(0, 0);
            complex = getComplex(tempDigits.get(0));
            //System.out.println(tempDigits);
            //System.out.println("\nTEMP OPERATION" + String.valueOf(complex.getReal()) + " " + String.valueOf(complex.getIm()) + "i");
            return getComplex(tempDigits.get(0)).toString();
        }
        
     
    }

    // Метод для определения типов чисел
    public String solve(String n2, String n1, String sign) {
        if(Character.isDigit(n2.charAt(0)) && Character.isDigit(n1.charAt(0))){
            return realSign(Double.parseDouble(n2), Double.parseDouble(n1), sign);
        }
        else
            if(n2.indexOf("complexs") != -1 && Character.isDigit(n1.charAt(0))){
                return complexSign(getComplex(n2), Double.parseDouble(n1), sign);
            }
        else
            if(n1.indexOf("complexs") != -1 && Character.isDigit(n2.charAt(0))){
                return complexSign(Double.parseDouble(n2), getComplex(n1), sign);
            }
            else
                return complexSign(getComplex(n2), getComplex(n1), sign);
    }

    // Метод вычисления для НЕКОМПЛЕКСНЫХ чисел
    public String realSign(Double n2, Double n1, String sign){
        if(sign.equals("+")){
            return String.valueOf(BigDecimal.valueOf(n2 + n1).setScale(3, RoundingMode.HALF_UP).doubleValue());
        }
        else
        if(sign.equals("-")){
            return String.valueOf(BigDecimal.valueOf(n2 - n1).setScale(3, RoundingMode.HALF_UP).doubleValue());
        }
        else
        if(sign.equals("*")){
            return String.valueOf(BigDecimal.valueOf(n2 * n1).setScale(3, RoundingMode.HALF_UP).doubleValue());
        }
        else
        if(sign.equals("/")){
            if(n1 != 0){
                return String.valueOf(BigDecimal.valueOf(n2 / n1).setScale(3, RoundingMode.HALF_UP).doubleValue());
            }
                else{
                    return Debug.DIVISION_BY_ZERO;
                }
        }
        else
            return String.valueOf(BigDecimal.valueOf(Math.pow(n2, n1)).setScale(3, RoundingMode.HALF_UP).doubleValue());
    }
   
    // Метод вычисления для КОМПЛЕКСНОГО и НЕКОМПЛЕКСНОГО числа
    public String complexSign(Complex n2, Double n1, String sign) {
        if(sign.equals("+")){
            Complex complex = new Complex(0, 0);
            complex = complex.Add(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("-")){
            Complex complex = new Complex(0, 0);
            complex = complex.Sub(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("*")){
            Complex complex = new Complex(0, 0);
            complex = complex.Mul(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("/")){
            Complex complex = new Complex(0, 0);
            if(n1 != 0){
                complex = complex.Div(n2, n1);
                String complexNum = "complexs" + complexs.size();
                complexs.add(complex);
                return complexNum;
            }
            return Debug.DIVISION_BY_ZERO;
        }
        else{
            return Debug.POWER_OF_I;
        }
    }
    
    // Метод вычисления для НЕКОМПЛЕКСНОГО и КОМПЛЕКСНОГО числа
    public String complexSign(Double n2, Complex n1, String sign) {
        if(sign.equals("+")){
            Complex complex = new Complex(0, 0);
            complex = complex.Add(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("-")){
            Complex complex = new Complex(0, 0);
            complex = complex.Sub(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("*")){
            Complex complex = new Complex(0, 0);
            complex = complex.Mul(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("/")){
            Complex complex = new Complex(0, 0);
            if(n1.getReal() != 0 || n1.getIm() != 0){
                complex = complex.Div(n2, n1);
                String complexNum = "complexs" + complexs.size();
                complexs.add(complex);
                return complexNum;
            }
            else
            return Debug.DIVISION_BY_ZERO;
        }
        else{
            Complex complex = new Complex(0, 0);
            complex = complex.Pow(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
    }

    // Метод вычисления для КОМПЛЕКСНОГО и КОМПЛЕКСНОГО числа
    public String complexSign(Complex n2, Complex n1, String sign) {
        if(sign.equals("+")){
            Complex complex = new Complex(0, 0);
            complex = complex.Add(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("-")){
            Complex complex = new Complex(0, 0);
            complex = complex.Sub(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("*")){
            Complex complex = new Complex(0, 0);
            complex = complex.Mul(n2, n1);
            String complexNum = "complexs" + complexs.size();
            complexs.add(complex);
            return complexNum;
        }
        else
        if(sign.equals("/")){
            Complex complex = new Complex(0, 0);
                if(n1.getReal() != 0 && n2.getIm() != 0){
                complex = complex.Div(n2, n1);
                String complexNum = "complexs" + complexs.size();
                complexs.add(complex);
                return complexNum;
            }
            else
                return Debug.DIVISION_BY_ZERO;
        }
        else
            return Debug.POWER_OF_I;
    }
    
    // Метод черной магии
    public String makeSomeBlackMagic(String expression) {
        tokenize(expression); // - разбиваем на токены
        Validator validator = new Validator(infixTokens);
        if(validator.validate()){
            infixToRpn(); // - запихиваем все это в польскую нотацию
            if(Debug.debugMode){
                showInfTokens(); // показываем токены в том порядке, в котором они идут в выражении
                showPostfix(); // показываем токены в порядке польской нотации
            }
            
            return "Ответ: " + " " + calculate();
        }
        else
            return Debug.VALID_ERROR;
    }

    // Метод выводит по дейстивям (немножко кривовато) то, что делается в методе выше
    public void showLittleBlackMagic(String n2, String n1, String sign, String solved){
        String withNumbers = "";
        if(n2.indexOf("complexs") != -1) {
            if(String.valueOf(getComplex(n2).getIm()).charAt(0) != '-'){
                withNumbers += String.valueOf(getComplex(n2).getReal()) + "+" + String.valueOf(getComplex(n2).getIm() + "i") + " ";
            }
            else
            withNumbers += String.valueOf(getComplex(n2).getReal()) + String.valueOf(getComplex(n2).getIm() + "i") + " ";
        }
        else
        if(Character.isDigit(n2.charAt(0))) {
            withNumbers += n2 + " ";
        }
        if(n1.indexOf("complexs") != -1) {
            if(String.valueOf(getComplex(n1).getIm()).charAt(0) != '-'){
                withNumbers += sign + " " + String.valueOf(getComplex(n1).getReal()) + "+" + String.valueOf(getComplex(n1).getIm() + "i");
            }
            else
            withNumbers += sign + " " + String.valueOf(getComplex(n1).getReal()) + String.valueOf(getComplex(n1).getIm() + "i") + " = ";
        }
        else
        if(Character.isDigit(n1.charAt(0))) {
            withNumbers += sign + " " + n1 + " = ";
        }
        if(solved.indexOf("complexs") != -1) {
            if(String.valueOf(getComplex(solved).getIm()).charAt(0) != '-'){
                withNumbers += String.valueOf(getComplex(solved).toString());
            }
        }
        else
        if(Character.isDigit(solved.charAt(0))) {
            withNumbers += solved;
        }
        System.out.println(withNumbers);
    }
    
    // Вспомогательные методы
    // Метод поиска комплексного числа по названию
    public Complex getComplex(String complex) {
        String num = "";
        for(int i = "complexs".length(); i < complex.length(); i++) {
            if(Character.isDigit(complex.charAt(i)))
                num += complex.charAt(i);
        }
        //System.out.println("Number of complex number is " + String.valueOf(num));
        //System.out.println(String.valueOf(complexs.get(Integer.valueOf(num)).getReal()) + " " + String.valueOf(complexs.get(Integer.valueOf(num)).getIm()) + "i");
        return complexs.get(Integer.valueOf(num));
    }

    // Метод проверки, является ли символ оператором
    public boolean isOperator(String c) {
        if(c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("^") || c.equals("(") || c.equals(")"))
            return true;
        else
            return false;
    }

    // Метод возвращает приоритет оператора
    int getPrecedence(char c) {
        if (c == '+' || c == '-') return 1;
        else if (c == '*' || c == '/') return 2;
        else if (c == '^') return 3;
        else return -1;
    }

    // Метод для правильной работы степени
    boolean hasLeftAssociativity(char c) {
        if (c == '+' || c == '-' || c == '/' || c == '*') {
            return true;
        } else {
            return false;
        }
    }


}
