package ui;

import model.Garden;
import model.Plant;
import model.PlantBed;

import java.util.ArrayList;
import java.util.Scanner;

//TODO: add specificaiton for these methods

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
            mainMenuOptions();
            userInput = input.next();
            userInput = userInput.toLowerCase();

            if (userInput.equals("q")) {
                activeProgram = false;
            } else {
                mainMenuCommand(userInput);
            }
        }
        System.out.println("\nGoodbye!");
    }

    private void mainMenuCommand(String userInput) {
        if (userInput.equals("a")) {
            showStatistics();
        } else if (userInput.equals("b")) {
            enterGarden();
        } else if (userInput.equals("c")) {
            System.out.println("CC");
        } else {
            System.out.println("Unrecognized command, please try again.");
        }

    }

    private void showStatistics() {
        System.out.println("Garden Statistics ---------");
        int numOfBed = myGarden.getNumOfPlantBeds();
        System.out.println("\nThere are " + numOfBed + " plant beds.");
        int numOfPlant = myGarden.getNumOfPlants();
        System.out.println("\nThere are " + numOfPlant + " plants.");
    }


    private void enterGarden() {
        boolean activeProgram = true;
        input = new Scanner(System.in);
        String userInput = null;

        while (activeProgram) {
            int count = 0;
            System.out.println("-------GARDEN MENU-------");
            for (PlantBed pb : myGarden.getPlantBedArrayList()) {
                System.out.println("[" + count + "]" + " - " + pb.getName());
                count++;
            }
            gardenMenuOptions();

            userInput = input.next();
            if (userInput.equals("q")) {
                activeProgram = false;
            } else if (userInput.equals("a")) {
                addPBMenu();
            } else if (userInput.equals("b")) {
                removePBMenu();
            } else if (userInput.equals("c")) {
                plantBedMenu();
            }
        }
    }

    private void removePBMenu() {
        input = new Scanner(System.in);
        String userInput = null;


        int count = 0;
        System.out.println("Which plant bed would you like to remove?");
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            System.out.println("[" + count + "]" + " - " + pb.getName());
            count++;
        }
        userInput = input.next();
        int index = Integer.parseInt(userInput);

        if (myGarden.removePlantBed(index)) {
            System.out.println("Removed successfully!");
        } else {
            System.out.println("Error!");
        }


    }

    private void addPBMenu() {
        input = new Scanner(System.in);
        String userInput = null;

        System.out.println("What is the name of the new plant bed you wish to add?");
        userInput = input.nextLine();
        myGarden.addPlantBed(new PlantBed(userInput));
        System.out.println("The plant bed '" + userInput + "' was added!");
    }

    private void plantBedMenu() {
        boolean activeProgram = true;
        input = new Scanner(System.in);

        System.out.println("Which plant bed would you like to visit?");
        String userInput = input.next();
        int index = Integer.parseInt(userInput);
        PlantBed pb = myGarden.getPlantBedArrayList().get(index);

        System.out.println("Plant Bed: " + pb.getName() + "\nPlants: ");
        for (Plant p: pb.getPlants()) {
            System.out.println(p.getName());
        }
        while (activeProgram) {
            plantBedMenuOptions();
            userInput = input.next();

            if (userInput.equals("q")) {
                activeProgram = false;
            } else if (userInput.equals("a")) {
                addPlant(pb);
            } else if (userInput.equals("b")) {
                removePlant(pb);
            } else if (userInput.equals("c")) {
                waterPlant(pb);
            }
        }
    }

    private void waterPlant(PlantBed pb) {
    }

    private void removePlant(PlantBed pb) {
        input = new Scanner(System.in);
        String userInput = null;
        int count = 0;

        System.out.println("Which plant bed would you like to remove?");
        for (Plant p : pb.getPlants()) {
            System.out.println("[" + count + "]" + " - " + p.getName());
            count++;
        }
        userInput = input.next();
        int index = Integer.parseInt(userInput);

        if (myGarden.removePlantBed(index)) {
            System.out.println("Removed successfully!");
        } else {
            System.out.println("Error!");
        }
    }

    private void addPlant(PlantBed pb) {
    }


    private void mainMenuOptions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("------MAIN MENU------");
        System.out.println("[a] - See statistics about your garden");
        System.out.println("[b] - View/Modify a plant bed");
        System.out.println("[c] - Watering Way-finder");
        System.out.println("[q] - Quit the Program");
    }

    private void gardenMenuOptions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("[a] - Add a new plant bed");
        System.out.println("[b] - Remove a plant bed");
        System.out.println("[c] - View a plant bed");
        System.out.println("[q] - Quit to Main Menu");
    }

    private void plantBedMenuOptions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("[a] - Add a new plant");
        System.out.println("[b] - Uproot a plant");
        System.out.println("[c] - Water a plant");
        System.out.println("[q] - Quit to Garden Menu");
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
