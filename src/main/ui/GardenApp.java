package ui;

import model.Garden;
import model.Plant;
import model.PlantBed;

import java.util.ArrayList;
import java.util.Scanner;

//TODO: add specificaiton for these methods
//TODO: take out check expects

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
        startUp();

        System.out.println("----Welcome to Garden Manager!---- ");
        while (activeProgram) {
            mainMenuOptions();
            String userInput = input.next();
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
            waterGuide();
        } else {
            System.out.println("Unrecognized command, please try again.");
        }

    }

    public void waterGuide() {
        input = new Scanner(System.in);

        System.out.println("----WATER WAY-FINDER---");
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            System.out.println("\nFrom plant bed '" + pb.getName() + "':");
            for (Plant p: pb.getPlants()) {
                if (p.getDry()) {
                    System.out.println("Plant " + p.getName() + " needs water!");
                    System.out.println("(enter any key after to go to next plant)");
                    if (!input.next().isEmpty()) {
                        p.water();
                        System.out.println("-------------------");
                    }
                }
            }
        }
        System.out.println("You watered all the plants, good job!");
    }

    private void showStatistics() {
        System.out.println("Garden Statistics ---------");
        int numOfBed = myGarden.getNumOfPlantBeds();
        System.out.println("\nThere are " + numOfBed + " plant bed(s).");
        int numOfPlant = myGarden.getNumOfPlants();
        System.out.println("\nThere are " + numOfPlant + " plant(s).");
        int count = 0;
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            for (Plant p: pb.getPlants()) {
                if (p.getDry()) {
                    count += 1;
                }
            }
        }
        System.out.println("\nThere are " + count + " plant(s) that need water!");
    }


    private void enterGarden() {
        boolean activeProgram = true;
        input = new Scanner(System.in);

        while (activeProgram) {
            int count = 0;
            System.out.println("-------GARDEN MENU-------");
            for (PlantBed pb : myGarden.getPlantBedArrayList()) {
                System.out.println("[" + count + "]" + " - " + pb.getName());
                count++;
            }
            gardenMenuOptions();

            String userInput = input.next();
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

        System.out.println("What is the name of the new plant bed you wish to add?");
        String userInput = input.nextLine();
        myGarden.addPlantBed(new PlantBed(userInput));
        System.out.println("The plant bed '" + userInput + "' was added!");
    }

    private void plantBedMenu() {
        boolean activeProgram = true;
        input = new Scanner(System.in);

        System.out.println("Which plant bed would you like to visit?");
        String userInput = input.next();
        PlantBed pb = myGarden.getPlantBedArrayList().get(Integer.parseInt(userInput));

        System.out.println("Plant Bed: " + pb.getName() + "\nPlants: ");
        for (Plant p: pb.getPlants()) {
            System.out.println(p.getName());
        }
        while (activeProgram) {
            plantBedMenuOptions();
            userInput = input.next();

            if (userInput.equals("q")) {
                activeProgram = false;
            } else {
                readPlantBedCommand(userInput, pb);
            }
        }
    }

    public void readPlantBedCommand(String s, PlantBed pb) {
        if (s.equals("a")) {
            viewPlant(pb);
        } else if (s.equals("b")) {
            removePlant(pb);
        } else if (s.equals("c")) {
            waterPlant(pb);
        } else if (s.equals("d")) {
            addPlant(pb);
        }
    }

    public void viewPlant(PlantBed pb) {
        input = new Scanner(System.in);
        boolean activeProgram = true;

        showPlants(pb);
        while (activeProgram) {


            String userInput = input.next();
            if (userInput.equals("q")) {
                activeProgram = false;
            } else {
                int index = Integer.parseInt(userInput);
                Plant p = pb.getPlants().get(index);
                System.out.println("--------------------");
                System.out.println("Name: " + p.getName());
                System.out.println("Type: " + p.getPlantType());
                System.out.println("Current Life Stage: " + p.getLifeStage());
                System.out.println("Watering Frequency: " + p.getWaterCycle());
                System.out.println("Needs Water?: " + p.getDry());
                System.out.println("--------------------");
                System.out.println("Check out another plant from above, or 'q' to quit.");
            }

        }

    }

    public void showPlants(PlantBed pb) {
        int count = 0;
        System.out.println("Which plant would you like to see more info about?");
        for (Plant p : pb.getPlants()) {
            System.out.println("[" + count + "]" + " - " + p.getName());
            count++;
        }
        System.out.println("Enter 'q' to go back to the plant bed menu");
    }

    private void waterPlant(PlantBed pb) {
        input = new Scanner(System.in);
        String userInput = null;
        int count = 0;

        System.out.println("Which plant would you like to water?");
        for (Plant p : pb.getPlants()) {
            System.out.println("[" + count + "]" + " - " + p.getName() + " (Dry = " + p.getDry() + ")");
            count++;
        }
        userInput = input.next();
        int index = Integer.parseInt(userInput);

        if (pb.waterPlant(index)) {
            System.out.println("Successfully Watered!");
        } else {
            System.out.println("Sorry, this plant is already watered! (do you want to drown it?)");
        }
    }

    private void removePlant(PlantBed pb) {
        input = new Scanner(System.in);
        String userInput = null;
        int count = 0;

        System.out.println("Which plant would you like to uproot?");
        for (Plant p : pb.getPlants()) {
            System.out.println("[" + count + "]" + " - " + p.getName());
            count++;
        }
        userInput = input.next();
        int index = Integer.parseInt(userInput);

        if (pb.uprootPlant(index)) {
            System.out.println("Removed successfully!");
        } else {
            System.out.println("Error!");
        }
    }

    private void addPlant(PlantBed pb) {
        Scanner input = new Scanner(System.in);
        System.out.println("What is the name of the plant?");
        String name = input.nextLine();
        System.out.println("What type of plant is this?");
        System.out.println("\"Perennial\", \"Biennial\", \"Cacti\", \"Bulb\", \"Shrub\", \"Fruit\", \"Vegetable\"");
        String type = input.nextLine();
        System.out.println("What is the life-stage of the plant?");
        System.out.println("\"Seed\", \"Sprout\", \"Young\", \"Mature\"");
        String age = input.nextLine();
        System.out.println("How often do you need to water the plant?");
        System.out.println("\"Daily\", \"Every 2 Days\", \"Every 3 Days\", \"Weekly\", \"Monthly\"");
        String water = input.nextLine();
        pb.addPlant(new Plant(name,water,type,age));
        System.out.println("The plant '" + name + "' was added to the bed '" + pb.getName() + "'!");
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
        System.out.println("[a] - View more info about a plant");
        System.out.println("[b] - Uproot a plant");
        System.out.println("[c] - Water a plant");
        System.out.println("[d] - Add a plant");
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
