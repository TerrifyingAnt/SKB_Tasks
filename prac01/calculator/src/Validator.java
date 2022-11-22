import java.util.ArrayList;
import java.util.List;


public class Validator {
    List<String> expression = new ArrayList<String>();
    
    public Validator(List<String> expression) {
        this.expression = expression;
    }

    public boolean validate() {
        if(checkBrackets() > -1){
            return checkContent();
        }

        return false;
    }

    // проверяет на количество скобок 
    public int checkBrackets(){
        //System.out.println(expression);
        Integer open = 0, closed = 0;
        for(int i = 0; i < expression.size(); i++){
            String token = expression.get(i);
            if(token.equals("("))
            open += 1;
            if(token.equals(")"))
            closed += 1;
        }
        //System.out.println("Открывающихся: " + String.valueOf(open) + "; Закрывающихся: " + String.valueOf(closed));
        if(open.equals(closed)){
            return open;
        }
        else
            return -1;

    }

    // проверяет на содержимое в скобках
    public boolean checkContent(){
        int operands = 0, operators = 0;
        List<String> copyExpression = new ArrayList<String>();
        for(int i = 0; i < expression.size(); i++){
            copyExpression.add(expression.get(i));
        }
        List<Integer> open = new ArrayList<Integer>();
        List<Integer> closed = new ArrayList<Integer>();
        for(int i = 0; i < expression.size(); i++){
            String token = expression.get(i);
            if(token.equals(")")){
                closed.add(i);               
            }          
        }
        for(int i = 0; i < closed.size(); i++){
            for(int j = closed.get(i); j > -1; j--){
                if(copyExpression.get(j).equals("(")){
                    open.add(j);
                    copyExpression.set(j,  "null");
                    //System.out.println(copyExpression);
                    break;
                }
            }
        }
        if(Debug.debugMode){
            System.out.println("Indexes of opened brackets: " + open);
            System.out.println("Indexes of closed brackets: " + closed);
        }  
        else
            for(int i = 0; i < open.size(); i++){
                int m = 0;
                operands = 0;
                operators = 0;
                int count = 1;
                for(int j = open.get(i) + 1 + m; j < closed.get(i); j++){
                    if(!expression.get(j).equals("(")){
                        //System.out.println("Checking: " + expression.get(j));
                        if(count % 2 == 0 && !Character.isDigit(expression.get(j).charAt(expression.get(j).length() - 1)) && (!expression.get(j).equals("("))){
                            count++;
                            operators++;
                            //System.out.println(expression.get(j));
                        }
                        else {
                            if(count % 2 != 0 && (Character.isDigit(expression.get(j).charAt(expression.get(j).length() - 1)))){
                                count++;
                                operands++;
                                //System.out.println(expression.get(j));
                            }
                            else{
                                System.out.println("Ruined at: " + expression.get(j));
                                return false;
                            }
                        }
                    }
                    else{
                        operands++;
                        for(m = 0; m < open.size(); m++){
                            if(open.get(m) == j) {
                                j = closed.get(m);
                                count++;
                            }
                        }
                    }
                }
                if(operands - operators != 1){
                    return false;
                }
            }
        if(operands - operators == 1){
            return true;
        }
        else return false;
    }

}

