package xyz.dragonnest.saesentsessis;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Main {
    String[] cities = {"Moscow", "Paris", "Mykolaiv", "Tokyo", "New York", "London", "Oslo"};

    public static void main(String[] args) {
        Main prog = new Main();
        prog.run();
    }

    private void run() {
        ArrayList<Train> trains = new ArrayList<>(); //createEmptyTrainArray(100);
        userInput(trains, false, false);
    }

    private void userInput(ArrayList<Train> trains, boolean hasArray, boolean stop) {
        if (stop) return;
        Scanner in = new Scanner(System.in);
        String choise; boolean b = false, t = false;
        StringBuilder mmsg = new StringBuilder("What do you wan't to do?\n- randomly fill train array (r)\n- fill train array from file (f)\n- fill train array from binary file (b)");
        if (hasArray) mmsg.append("\n- skip this step and work with array you have (s)");
        System.out.println(mmsg);
        while(true) {
            String input = in.nextLine();
            if (input.charAt(0) == 'r') {
                choise = "rand";
                break;
            } else if (input.charAt(0) == 'f') {
                choise = "file"; b = false;
                break;
            } else if (input.charAt(0) == 'b') {
                choise = "file"; b = true;
                break;
            } else if (input.charAt(0) == 's' && hasArray) {
                choise = "skip";
                break;
            } else {
                System.out.println("Invalid input! Try to write r/f/b!");
            }
        }
        String fName;
        switch (choise) {
            case "file": {
                System.out.println("Enter filename without .txt");
                while (true) {
                    String input = in.nextLine();
                    if (input.endsWith(".txt")) {
                        System.out.println("Invalid name! Try to write filename without .txt!");
                    } else {
                        fName = input;
                        break;
                    }
                }
                if (b) readFromFileBinary(trains, fName);
                else readFromFile(trains, fName);
                break;
            }
            case "rand": {
                fillArrayListRandomly(100 ,trains);
                break;
            }
            case "skip": {
                break;
            }
        }
        System.out.println("Ok!\n\nWhat to do next?\n- print all trains (p)\n- search in array(s)\n- print all trains, sorted by way point (w)\n- print all trains, sorted by way point and time (t)\n- print all trains, sorted by time and train number(A)\n- print all cities, sorted by amount of trains going there(D)\n- add train to array(a)\n- write in file (f)\n- write in file binary (b)");
        while (true) {
            String input = in.nextLine();
            if (input.charAt(0) == 'p') {
                choise = "printall";
                break;
            } else if (input.charAt(0) == 'A') {
                choise = "printA";
                break;
            } else if (input.charAt(0) == 'D') {
                choise = "printD";
                break;
            } else if (input.charAt(0) == 'w') {
                choise = "printwp"; t = false;
                break;
            } else if (input.charAt(0) == 't') {
                choise = "printwp"; t = true;
                break;
            } else if (input.charAt(0) == 'a') {
                choise = "add";
                break;
            } else if (input.charAt(0) == 's') {
                choise = "find";
                break;
            } else if (input.charAt(0) == 'f') {
                choise = "file"; b = false;
                break;
            } else if (input.charAt(0) == 'b') {
                choise = "file"; b = true;
                break;
            } else {
                System.out.println("Invalid input! Try to write p/w/t/a/f/b!");
            }
        }
        switch (choise) {
            case "printall": {
                printTrains(trains);
                break;
            }
            case "printA": {
                printAVariant(trains);
                break;
            }
            case "printD": {
                printDVariant(trains);
                break;
            }
            case "printwp": {
                StringBuilder msg = new StringBuilder("Enter one city from that list to sort by it:\n");
                for (int i = 0; i < cities.length; i++) {
                    msg.append(cities[i]);
                    if (i != cities.length-1) {
                        msg.append(',');
                    }
                }
                System.out.println(msg);
                String wayP = "";
                while(true) {
                    String input = in.nextLine();
                    b = false;
                    for (int i = 0; i < cities.length; i++) {
                        if (cities[i].equals(input)) {
                            wayP = input;
                            b = true;
                            break;
                        }
                    }
                    if (b) break;
                    else System.out.println("Invalid city! Try to enter correct one!");
                } // print with time
                if (t) {
                    System.out.println("Enter arrive time in format HH:MM");
                    int hours, mins;
                    while(true) {
                        String input = in.nextLine();
                        if (input.charAt(2) == ':') {
                            hours = Integer.parseInt(input.substring(0, 1));
                            mins = Integer.parseInt(input.substring(3, 4));
                            break;
                        } else {
                            System.out.println("Invalid input! Try to enter data in format HH:MM");
                        }
                    }
                    printWayPointTimeTrains(trains, wayP, hours, mins);
                } else {
                    printWayPointTrains(trains, wayP);
                }
                break;
            }
            case "file": {
                System.out.println("Enter filename to export, without .txt");
                while (true) {
                    String input = in.nextLine();
                    if (input.endsWith(".txt")) {
                        System.out.println("Invalid name! Try to write filename without .txt!");
                    } else {
                        fName = input;
                        break;
                    }
                }
                if (b) writeInFileBinary(trains, fName);
                else writeInFile(trains, fName);
                break;
            }
            case "add": {
                // здесь должно быть добавление поезда в массив с данными введенными от пользователя, но оно добавит рандомный поезд.
                addTrainToArrayList(trains, createRandomTrain(trains.size()));
                System.out.println("Added: " + trains.get(trains.size()-1).toString());
                break;
            }
            case "find": {
                System.out.println("Ok! Type something to search:");
                String[] input = in.nextLine().split(" ");
                boolean hasCity = false;
                int i = 0, str = 0;
                for (String city: cities) {
                    for (int j = 0; j < input.length; j++) {
                        if (city.contains(input[j])) {
                            hasCity = true;
                            str = j;
                            break;
                        }
                    }
                    if (hasCity) break;
                }
                System.out.println("Here's your search results:");
                while (i < trains.size()) {
                    if (trains.get(i).getWayP().contains(input[str])) {
                        System.out.println(trains.get(i).toString());
                    } else {
                        for (String inp: input) {
                            if (String.valueOf(trains.get(i).getTrNum()).equals(inp) || String.valueOf(trains.get(i).getHours()).equals(inp) || String.valueOf(trains.get(i).getMinutes()).equals(inp) || String.valueOf(trains.get(i).getpKoupe()).equals(inp) || String.valueOf(trains.get(i).getpPlatz()).equals(inp) || String.valueOf(trains.get(i).getpLux()).equals(inp)) {
                                System.out.println(trains.get(i).toString());
                            }
                        }
                    }
                    i++;
                }
                break;
            }
        }
        System.out.println("Do you want to continue work? y/n");
        while (true) {
            String input = in.nextLine();
            if (input.charAt(0) == 'y') {
                userInput(trains, true, false);
                return;
            } else if (input.charAt(0) == 'n') {
                return;
            } else {
                System.out.println("Incorrect input! Try to write y/n");
            }
        }
    }

    private void printTrains(ArrayList<Train> t) {
        for (Train train : t) {
            System.out.println(train);
        }
    }

    private void printAVariant(ArrayList<Train> trains) {
        Train[] export = trains.toArray(new Train[0]);
        for (int i = 0; i < trains.size(); i++) {
            for (int j = i+1; j < export.length; j++) {
                if (export[i].getHours() > export[j].getHours()) {
                    Train buff = export[i];
                    export[i] = export[j];
                    export[j] = buff;
                } else if (export[i].getHours() == export[j].getHours() && export[i].getMinutes() > export[j].getMinutes()) {
                    Train buff = export[i];
                    export[i] = export[j];
                    export[j] = buff;
                } else if (export[i].getHours() == export[j].getHours() && export[i].getMinutes() == export[j].getMinutes() && export[i].getTrNum() > export[j].getTrNum()) {
                    Train buff = export[i];
                    export[i] = export[j];
                    export[j] = buff;
                }
            }
            System.out.println(export[i].toString());
        }
    }

    private void printDVariant(ArrayList<Train> trains) {
        int[] counter = new int[cities.length];
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < trains.size(); j++) {
                if (trains.get(j).getWayP().equals(cities[i])) {
                    counter[i]++;
                }
            }
        }
        String[] export = cities;
        for (int i = 0; i < counter.length; i++) {
            for (int j = i+1; j < counter.length; j++) {
                if (counter[i] < counter[j]) {
                    String buff_ = export[i];
                    export[i] = export[j];
                    export[j] = buff_;
                    int buff = counter[i];
                    counter[i] = counter[j];
                    counter[j] = buff;
                } else if (counter[i] == counter[j]) {
                    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                    int[] p = new int[2]; boolean b = false;
                    for (int l = 0; l < cities[i].length(); l++) {
                        if (cities[i].charAt(l) != cities[j].charAt(l)) {
                            for (int k = 0; k < alphabet.length(); k++) {
                                if (cities[i].charAt(l) == alphabet.charAt(k)) {
                                    if (!b) {p[0] = k;b=true;} else {p[1] = k;}
                                }
                            }
                            if (b) break;
                        }
                    }
                    if (p[0] > p[1]) {
                        String buff_ = export[i];
                        export[i] = export[j];
                        export[j] = buff_;
                        int buff = counter[i];
                        counter[i] = counter[j];
                        counter[j] = buff;
                    }
                }
            }
            System.out.println(export[i] + ": "+counter[i]);
        }
    }

    private void printWayPointTrains(ArrayList<Train> t, String wayPointSort) {
        for (Train train : t) {
            if(train.getWayP().equals(wayPointSort)) {
                System.out.println(train);
            }
        }
    }

    private void printWayPointTimeTrains(ArrayList<Train> t, String wayPointSort, int hours, int minutes) {
        for (Train train : t) {
            if(train.getWayP().equals(wayPointSort)) {
                if (train.getHours() < hours) {
                    //System.out.println(train);
                    if(train.getHours() == hours){
                        if (train.getMinutes() > minutes) {
                            System.out.println(train);
                        }
                    }
                }
            }
        }
    }

    private void fillArrayListRandomly(int amount, ArrayList<Train> array) {
        array.clear();
        int i = array.size(); int buff = i;
        while (i < amount+buff) {
            array.add(createRandomTrain(i)); i++;
        }
    }

    private void addTrainToArrayList(ArrayList<Train> array, Train trainToAdd) {
        array.add(trainToAdd);
    }

    private Train createRandomTrain(int id) {
        Random rnd = new Random();
        String[] cities = {"Moscow", "Paris", "Mykolaiv", "Tokyo", "New York", "London", "Oslo"};
        return new Train(cities[rnd.nextInt(cities.length)], id, rnd.nextInt(24), rnd.nextInt(59),60+rnd.nextInt(100), 10+rnd.nextInt(50), rnd.nextInt(40) );
    }

    private void readFromFile(ArrayList<Train> arrToFill, String fileName) {
        try (Scanner in = new Scanner(new File(fileName+".txt"))) {
            for (int i = 0; in.hasNext(); i++) {
                String wp = in.nextLine().split("\n", 1)[0];
                int tnum = in.nextInt(), timeH = in.nextInt(), timeM = in.nextInt(), pKoupe = in.nextInt(), pPlatz = in.nextInt(), pLux = in.nextInt();
                arrToFill.add(new Train(wp, tnum, timeH, timeM, pKoupe, pPlatz, pLux));
            }
        } catch (IOException ex) {
            System.err.println("Error reading file!");
        }
    }

    private void writeInFile(ArrayList<Train> arrToWrite, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName+".txt"));) {
            for (int i = 0; i < arrToWrite.size(); i++) {
                if (arrToWrite.get(i) == null) break;
                String str = arrToWrite.get(i).getData();
                writer.write(str);
            }
            writer.close();
        } catch (IOException ex) {
            System.err.println("Error writing in file!");
        }
    }

    private void readFromFileBinary(ArrayList<Train> arrToFill, String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName+".txt"))) {
            int length = in.readInt();
            for (int i = 0; i < length; i++) {
                String wp = (String)in.readObject();
                System.out.println(wp);
                int tnum = in.readInt(), timeH = in.readInt(), timeM = in.readInt(), pKoupe = in.readInt(), pPlatz = in.readInt(), pLux = in.readInt();
                arrToFill.add(new Train(wp, tnum, timeH, timeM, pKoupe, pPlatz, pLux));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeInFileBinary(ArrayList<Train> arrToWrite, String fileName) {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(fileName+".txt"))){
            writer.writeInt(arrToWrite.size());
            for (Train train : arrToWrite) {
                if (train == null) break;
                String str = train.getWayP();
                writer.writeObject(str);
                int num = train.getTrNum();
                int h = train.getHours();
                int m = train.getMinutes();
                int pk = train.getpKoupe();
                int pp = train.getpPlatz();
                int pl = train.getpLux();
                writer.writeInt(num);
                writer.writeInt(h);
                writer.writeInt(m);
                writer.writeInt(pk);
                writer.writeInt(pp);
                writer.writeInt(pl);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
