package ui;


import model.Garden;
import model.Plant;
import model.PlantBed;
import persistence.ReaderJson;
import persistence.WriterJson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static javafx.application.Platform.exit;

// provides the console based ui for using the Garden Manager application
//       this class was made based of the TellerApp class from the TellerApp Project:
//       https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class GardenApp extends JFrame {
    private Garden myGarden;
    private Scanner input;
    private static final String SOURCE_JSON = "./data/garden.json";
    private WriterJson writerJson;
    private ReaderJson readerJson;
    private JFrame jf;
    private int btnWidth = 200;
    private int btnHeight = 50;
    private int alignX = 400;
    private JPanel mainPage;
    private JPanel plantBedPage;

    //EFFECTS: starts main  menu method to start the ui
    public GardenApp() {
        mainMenu();
    }

    //MODIFIES: myGarden
    //EFFECTS: shows main menu options and takes user input
    public void mainMenu() {
        boolean activeProgram = true;
        input = new Scanner(System.in);
        loadStartUp();
        initializeGUI();
    }

    public void initializeGUI() {
        jf = new JFrame("Garden Manager");
        jf.setLayout(new CardLayout());

        initializeMainPage();
        initializePanels();

        JButton quitButton = new JButton("Quit Program");
        quitButton.setBounds(alignX, 300, btnWidth, btnHeight);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        mainPage.add(quitButton);

        jf.setSize(700,500);
        jf.setLayout(null);
        jf.setVisible(true);//making the frame visible
    }

    private void initializePanels() {
        makePlantBedPage();
    }

    private void makePlantBedPage() {
        plantBedPage = new JPanel();
        plantBedPage.setLayout(new GridLayout(3,5,5,5));
        PlantBedRenderer pbr = new PlantBedRenderer();
        JComboBox<PlantBed> plantBedList = new
                JComboBox<>(myGarden.getPlantBedArrayList().toArray(new PlantBed[0]));
        //plantList.setCellRenderer(pbr);
        plantBedList.setRenderer(pbr);
        plantBedPage.add(new JLabel("Current Selected PlantBed:"));
        plantBedPage.add(plantBedList);
        plantBedPage.add(new JLabel("Current Selected Plant:"));
        PlantRenderer p = new PlantRenderer();
        JComboBox<PlantBed> plantList = new JComboBox(
                myGarden.getPlantBedByIndex(plantBedList.getSelectedIndex()).getPlantArrayList().toArray(new Plant[0]));
        plantList.setRenderer(p);
        plantBedPage.add(plantList);
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
        plantBedPage.add(new JTextArea("Current Selected PlantBed:"));
    }


    private void initializeMainPage() {
        mainPage = new JPanel();
        jf.setContentPane(mainPage);
        addPlantBedsButton();
        addGardenStatsButton();
        addWateringButton();
        addSaveAndLoadUIButton();
    }

    public void saveOnQuit() {
    }

    public void addPlantBedsButton() {
        JButton b1 = new JButton("View/Modify Plant Beds");
        b1.setBounds(alignX, 50, btnWidth, btnHeight);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setContentPane(plantBedPage);
                jf.repaint();
                jf.revalidate();
            }
        });
        mainPage.add(b1);
    }

    public void addGardenStatsButton() {
        JButton b2 = new JButton("View Garden Stats");
        JDialog statsWindow = new JDialog(jf, "Garden Stats", true);
        statsWindow.setLayout(new BorderLayout());
        b2.setBounds(alignX, 100, btnWidth, btnHeight);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statsWindow.setVisible(true);
            }
        });
        String numDryPlants = generateDryPlants();
        statsWindow.add(new JLabel(
                "\nThere are " + myGarden.getNumOfPlantBeds() + " plant bed(s)."),BorderLayout.NORTH);
        statsWindow.add(new JLabel(
                "\nThere are " + myGarden.getNumOfPlants() + " plant(s)."),BorderLayout.CENTER);
        statsWindow.add(new JLabel(numDryPlants),BorderLayout.SOUTH);
        statsWindow.setSize(300,300);
        mainPage.add(b2);
    }

    public String generateDryPlants() {
        int count = 0;
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            for (Plant p: pb.getPlantArrayList()) {
                if (p.getDry()) {
                    count += 1;
                }
            }
        }
        return "\nThere are " + count + " plant(s) that need water!";
    }


    private void addSaveAndLoadUIButton() {
        JButton b4 = new JButton("Save/Load");
        b4.setBounds(alignX, 150, btnWidth, btnHeight);
        mainPage.add(b4);
    }

    private void addWateringButton() {
        JButton b3 = new JButton("Watering Way-Finder");
        b3.setBounds(alignX, 200, btnWidth, btnHeight);
        mainPage.add(b3);
    }


    //MODIFIES: this
    //EFFECTS: reads from SOURCE_JSON on startup and initializes garden
    //         if unable to do so call with demo data instead
    public void loadStartUp() {
        System.out.println("\nRetrieving Garden from last saved state...");
        try {
            readerJson = new ReaderJson(SOURCE_JSON);
            myGarden = readerJson.readSource();
            System.out.println("Loaded Garden from " + SOURCE_JSON + "!");
        } catch (IOException e) {
            System.out.println("Unable to retrieve saved data from "  + SOURCE_JSON);
            System.out.println("Using demo data...");
            startUp();
        }
    }


    //EFFECTS: prints out all main menu options
    public void mainMenuOptions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("------MAIN MENU------");
        System.out.println("[a] - See statistics about your garden");
        System.out.println("[b] - View/Modify a plant bed");
        System.out.println("[c] - Watering WayFinder");
        System.out.println("[d] - Save/Retrieve from previous version");
        System.out.println("[q] - Quit the Program");
    }


    //MODIFIES: myGarden
    //EFFECTS: takes user input from main menu and executes corresponding method
    public void mainMenuCommands(String userInput)  {
        if (userInput.equals("a")) {
            statisticsMenu();
        } else if (userInput.equals("b")) {
            gardenMenu();
        } else if (userInput.equals("c")) {
            waterWayFinderMenu();
        } else if (userInput.equals("d")) {
            saveAndRetrieveMenu();
        } else {
            System.out.println("Unrecognized command, please try again.");
        }
    }

    //MODIFIES: myGarden
    //EFFECTS: takes user input and allows them to either save or retrieve garden
    public void saveAndRetrieveMenu() {
        boolean activeProgram = true;
        input = new Scanner(System.in);

        while (activeProgram) {
            System.out.println("\n------SAVE/RETRIEVE MENU------");
            System.out.println("[a] - Save garden in current state");
            System.out.println("[b] - Retrieve garden from last saved state");
            System.out.println("[q] - Quit the save/retrieve menu");
            String userInput = input.next();

            if (userInput.equals("a")) {
                saveGarden();
            } else if (userInput.equals("b")) {
                retrieveGarden();
            } else if (userInput.equals("q")) {
                activeProgram = false;
            }
        }

    }

    //MODIFIES: myGarden
    //EFFECTS: retrieves garden from JSON file and replaces current garden with it
    //         if unable to do so, print out message and do nothing
    public void retrieveGarden() {
        try {
            readerJson = new ReaderJson(SOURCE_JSON);
            myGarden = readerJson.readSource();
            System.out.println("Loaded Garden from " + SOURCE_JSON + "!");
        } catch (IOException e) {
            System.out.println("Unable to read from file " + SOURCE_JSON);
        }
    }

    //EFFECT: saves current garden to JSON file at SOURCE_JSON
    //        if unable to do so, print out message and do nothing
    public void saveGarden() {
        try {
            writerJson = new WriterJson(SOURCE_JSON);
            writerJson.open();
            writerJson.writeToJson(myGarden);
            writerJson.close();
            System.out.println("Saved Garden to " + SOURCE_JSON + "!");
        } catch (IOException e) {
            System.out.println("Unable to save from file " + SOURCE_JSON);
        }
    }

    //MODIFIES: myGarden
    //EFFECTS: runs Water WayFinder, where plants that need to be watered
    //         are shown from each plant bed and can then be watered
    public void waterWayFinderMenu() {
        input = new Scanner(System.in);

        System.out.println("----WATER WAY-FINDER---");
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            System.out.println("\nFrom plant bed '" + pb.getName() + "':");
            for (Plant p: pb.getPlantArrayList()) {
                if (p.getDry()) {
                    System.out.println("Plant " + p.getName() + " needs water!");
                    System.out.println("(enter any character to go to next plant)");
                    if (!input.next().isEmpty()) {
                        p.water();
                        System.out.println("-------------------");
                    }
                }
            }
        }
        System.out.println("You watered all the plants, good job!");
    }

    //EFFECTS: prints number of plants, plant beds, and dry plants in garden
    public void statisticsMenu() {
        System.out.println("Garden Statistics ---------");
        int numOfBed = myGarden.getNumOfPlantBeds();
        System.out.println("\nThere are " + numOfBed + " plant bed(s).");
        int numOfPlant = myGarden.getNumOfPlants();
        System.out.println("\nThere are " + numOfPlant + " plant(s).");
        int count = 0;
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            for (Plant p: pb.getPlantArrayList()) {
                if (p.getDry()) {
                    count += 1;
                }
            }
        }
        System.out.println("\nThere are " + count + " plant(s) that need water!");
    }

    //MODIFIES: myGarden, this
    //EFFECTS: shows garden menu options and takes user input
    public void gardenMenu() {
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
                addPB();
            } else if (userInput.equals("b")) {
                removePB();
            } else if (userInput.equals("c")) {
                plantBedMenu();
            }
        }
    }

    //EFFECTS: prints out all garden menu options
    public void gardenMenuOptions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("[a] - Add a new plant bed");
        System.out.println("[b] - Remove a plant bed");
        System.out.println("[c] - View a plant bed");
        System.out.println("[q] - Quit to Main Menu");
    }

    //MODIFIES: myGarden, this
    //EFFECTS: removes plant bed with index from user input
    public void removePB() {
        input = new Scanner(System.in);
        String userInput;

        int count = 0;
        System.out.println("Which plant bed would you like to remove? (give index number)");
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

    //MODIFIES: myGarden, this
    //EFFECTS: adds plant bed with index from user input
    public void addPB() {
        input = new Scanner(System.in);

        System.out.println("What is the name of the new plant bed you wish to add?");
        String userInput = input.nextLine();
        myGarden.addPlantBed(new PlantBed(userInput));
        System.out.println("The plant bed '" + userInput + "' was added!");
    }

    //MODIFIES: myGarden, this
    //EFFECTS: shows plant bed menu options and takes user input
    public void plantBedMenu() {
        boolean activeProgram = true;
        input = new Scanner(System.in);

        System.out.println("Which plant bed would you like to visit? (give index number)");
        String userInput = input.next();
        PlantBed pb = myGarden.getPlantBedArrayList().get(Integer.parseInt(userInput));

        System.out.println("Plant Bed: " + pb.getName() + "\nPlants: ");
        for (Plant p: pb.getPlantArrayList()) {
            System.out.println(p.getName());
        }
        while (activeProgram) {
            plantBedMenuOptions();
            userInput = input.next();

            if (userInput.equals("q")) {
                activeProgram = false;
            } else {
                plantBedCommands(userInput, pb);
            }
        }
    }

    //EFFECTS: prints out all plant bed menu options
    public void plantBedMenuOptions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("[a] - View more info about a plant");
        System.out.println("[b] - Uproot a plant");
        System.out.println("[c] - Water a plant");
        System.out.println("[d] - Add a plant");
        System.out.println("[q] - Quit to Garden Menu");
    }

    //REQUIRES: string s should be one of "a", "b", "c", "d"
    //EFFECTS: takes user input from plant bed menu and executes corresponding method
    public void plantBedCommands(String s, PlantBed pb) {
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

    //MODIFIES: this, pb
    //EFFECTS: shows plant info of selected plant from given plant bed
    public void viewPlant(PlantBed pb) {
        input = new Scanner(System.in);
        boolean activeProgram = true;

        showPlant(pb);
        while (activeProgram) {


            String userInput = input.next();
            if (userInput.equals("q")) {
                activeProgram = false;
            } else {
                int index = Integer.parseInt(userInput);
                Plant p = pb.getPlantArrayList().get(index);
                System.out.println("--------------------");
                System.out.println("Name: " + p.getName());
                System.out.println("Type: " + p.getPlantType());
                System.out.println("Current Life Stage: " + p.getLifeStage());
                System.out.println("Watering Frequency: " + p.getWaterCycle());
                System.out.println("Needs Water?: " + p.getDry());
                System.out.println("--------------------");
                System.out.println("Check out another plant from above by giving their index, or 'q' to quit.");
            }

        }

    }

    //EFFECTS: shows possible plants that can be viewed to see more info
    public void showPlant(PlantBed pb) {
        int count = 0;
        System.out.println("Which plant would you like to see more info about?");
        for (Plant p : pb.getPlantArrayList()) {
            System.out.println("[" + count + "]" + " - " + p.getName());
            count++;
        }
        System.out.println("Enter 'q' to go back to the plant bed menu");
    }

    //MODIFIES: this, pb
    //EFFECTS: waters plant in plant bed with index at user input
    public void waterPlant(PlantBed pb) {
        input = new Scanner(System.in);
        String userInput;
        int count = 0;

        System.out.println("Which plant would you like to water?");
        for (Plant p : pb.getPlantArrayList()) {
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

    //MODIFIES: this, pb
    //EFFECTS: removes plant with index from user input from plant bed pb
    public void removePlant(PlantBed pb) {
        input = new Scanner(System.in);
        String userInput;
        int count = 0;

        System.out.println("Which plant would you like to uproot?");
        for (Plant p : pb.getPlantArrayList()) {
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

    //MODIFIES: pb
    //EFFECTS: adds plant with user inputs to plant bed pb
    public void addPlant(PlantBed pb) {
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

    //MODIFIES: this
    //EFFECTS: initializes example garden data for manipulation
    public void startUp() {
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
