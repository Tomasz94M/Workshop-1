package pl.coderslab;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class TaskManager2 {



    public static final String BLUE = "\u001B[34m";

    public static final String CYAN = "\u001B[36m";

    public static final String RESET = "\u001B[0m";

    public static final String YELLOW = "\u001B[33m";

    public static final String RED_BOLD = "\u001B[31;1m";

    public static final String GREEN = "\u001B[32m";





    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        boolean running = true;



        while (running) {

            String option = ListTasks(scanner);



            switch (option) {

                case "add":



                    System.out.println("Please add task description: ");

                    String name = scanner.nextLine();



                    String date;

                    date = checkDate(scanner);



                    String TrueFalse;

                    TrueFalse = checkTrueFalse(scanner);



                    AppendToCSV("./src/tasks.csv", name, date, TrueFalse);

                    break;



                case "remove":

                    System.out.println("Please specify the line you want to remove: ");

                    RemoveLines(scanner);

                    break;



                case "list":

                    String[][] taskArray = readCSV();

                    printCSV(taskArray);

                    break;



                case "exit":

                    running = false;

                    System.out.println(CYAN + "Bye Bye" + RESET);

                    break;



                default:

                    System.out.println(RED_BOLD + "Please select a correct option" + RESET);

                    break;



            }

        }

        scanner.close();

    }



    private static String ListTasks(Scanner scanner) {

        System.out.println(BLUE + "Please select an option: " + RESET);

        System.out.println("add");

        System.out.println("remove");

        System.out.println("list");

        System.out.println("exit");

        String option = scanner.nextLine().trim().toLowerCase();

        return option;

    }



    private static void RemoveLines(Scanner scanner) {

        try {

            String[][] updated_taskArray = readCSV();

            int nextLine;

            while (true) {

                if (scanner.hasNextInt()) {

                    nextLine = scanner.nextInt();

                    scanner.nextLine(); //Clear the buffer



                    if (updated_taskArray == null || updated_taskArray.length == 0) {

                        System.out.println(RED_BOLD + "List empty" + RESET);

                        break;



                    } else if (nextLine <= 0 || nextLine > updated_taskArray.length) {

                        System.out.println(RED_BOLD + "Incorrect argument passed. Number must be an int which is greater than 0 and within the table size" + RESET);

                        break;



                    } else {

                        DeletFromCSV("./src/tasks.csv", nextLine);

                        System.out.println(GREEN + "You have deleted line: " + RESET + nextLine);



                        break;

                    }

                } else {

                    System.out.println(RED_BOLD + "Input must be an Integer, try again " + RESET);

                }

            }



        } catch (NumberFormatException e) {

            System.out.println(RED_BOLD + "Invalid input. It must be an int." + RESET);

        }



    }



    private static String checkTrueFalse(Scanner scanner) {

        String TrueFalse;

        do {

            System.out.println("Is your task important: Please answer with true/false");

            TrueFalse = scanner.nextLine().trim().toLowerCase();

            if (TrueFalse.equals("true") || TrueFalse.equals("false")) {

                System.out.println(GREEN + "Valid input: " + RESET + TrueFalse);

                break;

            } else {

                System.out.println(RED_BOLD + "Invalid input: " + RESET);

            }

        } while (true);

        return TrueFalse;

    }



    private static String checkDate(Scanner scanner) {

        String date;

        do {

            System.out.println("Please add task due date using the following format <YYYY-MM-DD> ");

            date = scanner.nextLine();

            if (!ValidDateTime(date)) {

                System.out.println(RED_BOLD + "Invalid Date Format: " + RESET);

            } else {

                System.out.println(GREEN + "Valid Date Format: " + RESET + date);

                break;

            }

        } while (true);

        return date;

    }



    public static String[][] list_tasks(Path path) {

        try {

            List<String> lines = Files.readAllLines(path);

            int NumRows = lines.size();



            if (NumRows == 0) {

                return null;

            }



            String[][] tasksArray = new String[NumRows][];

            for (int i = 0; i < NumRows; i++) {

                tasksArray[i] = lines.get(i).split(",");

            }

            return tasksArray;

        } catch (IOException ex) {

            ex.printStackTrace();

        }

        return null;

    }



    public static String[][] readCSV() {

        return list_tasks(Path.of("./src/tasks.csv"));

    }



    public static void printCSV(String[][] tasksArray) {

        System.out.println(YELLOW + "list of tasks: " + RESET);

        if (tasksArray != null) {

            for (int i = 0; i < tasksArray.length; i++) {

                System.out.print(i + 1 + " : ");

                for (int j = 0; j < tasksArray[i].length; j++) {

                    System.out.print(tasksArray[i][j] + " ");

                }

                System.out.println();

            }

        }

    }



    public static void AppendToCSV(String path, String dataToAppend, String dataToAppend1, String dataToAppend2) {

        try (FileWriter fw = new FileWriter(path, true)) {

            fw.write(dataToAppend + ", " + dataToAppend1 + ", " + dataToAppend2 + System.lineSeparator());



        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    public static void DeletFromCSV(String path, int deleteLine) {

        String tempFile = "temp.csv";

        File oldFile = new File(path);

        File newFile = new File(tempFile);



        int line = 0;

        String currentLine;



        try{

            FileWriter fw = new FileWriter(tempFile,true);

            FileReader fr = new FileReader(path);

            BufferedReader br = new BufferedReader(fr);



            while((currentLine = br.readLine()) != null){

                line++;

                if (deleteLine != line){

                    fw.write(currentLine + System.lineSeparator());

                }

            }

            fw.close();

            br.close();





            oldFile.delete();

            File dump = new File(path);

            newFile.renameTo(dump);

        }

        catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }



    public static boolean ValidDateTime(String date) {

        try {

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate.parse(date, format);

            return true;

        } catch (DateTimeParseException e) {

            return false;

        }

    }

}