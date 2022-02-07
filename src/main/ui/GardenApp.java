package ui;

import model.Garden;
import model.Plant;
import model.PlantBed;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;


public class GardenApp {
    private Garden myGarden;
    private Scanner input;

    public GardenApp() {
        runApp();
    }

    //EFFECTS: runs menu and user inputs
    private void runApp() {
        boolean activeProgram = true;
        input = new Scanner(System.in);
        String userInput = null;
        startUp();

        System.out.println("----Welcome to Garden Manager!---- ");
        while (activeProgram) {
            mainMenu();
            userInput = input.next();
            userInput.toLowerCase();

            if (userInput.equals("q")) {
                activeProgram = false;
            } else {
                doCommand(userInput);
            }
        }
        System.out.println("\nGoodbye!");
    }

    private void doCommand(String userInput) {
        if (userInput.equals("a")) {
            System.out.println("cool");
        } else if (userInput == "b") {
            System.out.println("nice");
        }
    }

    private void mainMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("-------------------------");
        System.out.println("\n[a] - See statistics about your garden");
        System.out.println("[b] - View/Modify a plant bed");
        System.out.println("[c] - Watering Way-finder");
        System.out.println("\n");
    }

    private void startUp() {
        Plant p1 = new Plant("Rose","Weekly","Bulb","Mature");
        Plant p2 = new Plant("Carrot", "Weekly", "Vegetable", "Young");
        Plant p3 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        PlantBed bed1 = new PlantBed("Bed 1");
        bed1.addPlant(p1);
        bed1.addPlant(p2);
        PlantBed bed2 = new PlantBed("Bed 2");
        bed2.addPlant(p3);
        ArrayList<PlantBed> testPlantBeds = new ArrayList<>();
        testPlantBeds.add(bed1);
        testPlantBeds.add(bed2);
        myGarden = new Garden(testPlantBeds);
    }


}
