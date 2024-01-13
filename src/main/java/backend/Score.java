package backend;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Score {

    public static int getScore() {
        int score = 0;

        try {
            File myObj = new File(System.getProperty("user.dir") + File.separator +"/src/main/resources/data.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                score = Integer.valueOf(data);
            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return score;

    }
    public static void addScore(int newScore) {
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + "/src/main/resources/data.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            // Écrivez le nouveau score dans le fichier
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(newScore);
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static int[] getScores(){
        int[] score = new int[100];
        try {
            File myObj = new File(System.getProperty("user.dir") + File.separator + "/src/main/resources/bestscores.txt");

            Scanner myReader = new Scanner(myObj);

            int i =0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                score[i] = Integer.valueOf(data);
                System.out.println(score[i]);
                i++;
            }
            myReader.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Arrays.sort(score);

        return score;

    }

    public static int saveScores(int score) {
        File scorefile = new File(System.getProperty("user.dir") + File.separator + "/src/main/resources/bestscores.txt");

        if (scorefile.length()==0) {
            try (FileWriter writer = new FileWriter(scorefile, true)) {
                writer.write(String.valueOf(score));
                return 1;
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else {
            int max = 0;
            try (Scanner myReader = new Scanner(scorefile)) {
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (!data.isEmpty()) {
                        int currentScore = Integer.parseInt(data);
                        if (max < currentScore) {
                            max = currentScore;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }

            if (score >= max) {
                try (FileWriter writer = new FileWriter(scorefile, true)) {
                    writer.write("\n" + score);
                    return 1;
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
    }



