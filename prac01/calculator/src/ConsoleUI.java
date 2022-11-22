import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;

public class ConsoleUI {
    Calculator calculator = new Calculator();
    
    // Метод для вывода меню программы
    public void showMenu() throws IOException{
        int i = 6;
        System.out.println("Калькулятор, задача №1 для СКБ");
        while(i != 5){
            System.out.println("Вводить выражение нужно изначально в скобках, не поддерживается возведение в степень i");
            System.out.println("Меню:");
            System.out.println("1) Прочитать выражение с консоли и вывести в консоль");
            System.out.println("2) Прочитать выражение с консоли и вывести в файл");
            System.out.println("3) Прочитать выражение с файла и вывести в консоль");
            System.out.println("4) Прочитать выражение с файла и вывести в файл");
            System.out.println("5) Завершить");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введи номер действия:");
            try {
                i = Integer.parseInt(br.readLine());
                switch(i){
                    case 1:
                        readFromConsoleToConsole();
                        break;
                    
                    case 2:
                        readFromConsoleToFile();
                        break;
                    
                    case 3:
                        readFromFileToConsole();
                        break;

                    case 4:
                        readFromFileToFile();
                        break;
                }
                
            } catch(NumberFormatException nfe) {
                System.err.println("Тут всего 5 цифр, попробуй ввести одну из них еще раз...");
            }
        }
    }

    // Метод для решения одиночного выражения
    public void readFromConsoleToConsole() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введи выражение:");
        String s = br.readLine();
        System.out.println(calculator.makeSomeBlackMagic(s));
    }

    // Метод для решения одиночного выражения и записи его в файл
    public void readFromConsoleToFile() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введи выражение:");
        String s = br.readLine();
        System.out.print("Введи название файла: ");
        String name = br.readLine();
        PrintWriter out = new PrintWriter(name + ".txt", "UTF-8");
        out.println(calculator.makeSomeBlackMagic(s));
        out.close();
    }

    // Метод для решения выражений из файла и вывода в консоль
    public void readFromFileToConsole() throws IOException{
        List<String> expr = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введи полный путь до файла:");
        String s = br.readLine();
        try(BufferedReader in = new BufferedReader(new FileReader(s))) {
            String str;
            while ((str = in.readLine()) != null) {
                String[] ar=str.split(";");
                //System.out.println(ar);
                //expr = ar;
                for (String ex : ar) {
                    //System.out.println(calculator.makeSomeBlackMagic(ex));
                    //System.out.println(calculator.makeSomeBlackMagic(ex));
                    expr.add(ex);
                }
            }
        }
        catch (IOException e) {
            System.out.println("Файл не найден");
        }
        for(int i = 0; i < expr.size(); i++){
            System.out.println(expr.get(i));
            System.out.println(calculator.makeSomeBlackMagic(expr.get(i)));
        }

    }

    // Метод для решения выражений из файла и вывода в файл
    public void readFromFileToFile() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введи полный путь до файла:");
        String s = br.readLine();
        System.out.print("Введи название файла: ");
        String name = br.readLine();
        PrintWriter out = new PrintWriter(name + ".txt", "UTF-8");
        try(BufferedReader in = new BufferedReader(new FileReader(s))) {
            String str;
            while ((str = in.readLine()) != null) {
                String[] ar=str.split(";");
                for (String ex : ar) {
                    out.println(calculator.makeSomeBlackMagic(ex));
                }
            }
            out.close();
        }
        catch (IOException e) {
            System.out.println("File Read Error");
        }
    }

}
