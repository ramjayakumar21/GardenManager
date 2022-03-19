package ui;


import model.Garden;
import model.Plant;
import model.PlantBed;
import persistence.ReaderJson;
import persistence.WriterJson;
import ui.renderers.PlantBedRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


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
    private static final int WIDTH = 700;
    private static final int HEIGHT = 450;

    private int btnWidth = 200;
    private int btnHeight = 50;
    private int alignX = 400;
    private JPanel mainPage;
    private JPanel plantBedPage;
    private JPanel saveAndLoadPanel;
    private JButton backButton;

    //EFFECTS: starts main  menu method to start the ui
    public GardenApp() {
        mainMenu();
    }

    //MODIFIES: myGarden
    //EFFECTS: shows main menu options and takes user input
    public void mainMenu() {
        jf = new JFrame("Garden Manager");
        loadStartUp();
        initializeGUI();
        jf.setSize(700,450);
    }

    public void initializeGUI() {
        initializeMainPage();
        initializePanels();


        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    private void initializePanels() {
        plantBedPage = new JPanel();
        plantBedPage.setLayout(new GridLayout(2,3));
        makePlantBedPage();

        saveAndLoadPanel = new JPanel();
        saveAndLoadPanel.setLayout(new GridBagLayout());
        initSandL();
    }

    private void initSandL() {
        GridBagConstraints gc = new GridBagConstraints();
        JDialog message = new JDialog();

        JLabel j = new JLabel("Would you like to revert to last saved state or save in current state?");
        gc.gridx = 1;
        gc.gridy = 0;
        saveAndLoadPanel.add(j,gc);
        JButton b1 = new JButton("Revert to last save");
        b1.addActionListener(e -> {
            showMessage(message, retrieveGarden());
        });
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.5;
        gc.gridx = 0;
        gc.gridy = 1;
        saveAndLoadPanel.add(b1,gc);
        JButton b2 = new JButton("Save current Garden");
        b2.addActionListener(e -> {
            showMessage(message, saveGarden());
        });
        gc.weightx = 0.5;
        gc.gridx = 1;
        saveAndLoadPanel.add(b2,gc);
        saveAndLoadPanel.add(backButton);
    }

    private void showMessage(JDialog jd, String s) {
        JLabel message = new JLabel(s);
        jd.add(message, BorderLayout.CENTER);
        jd.setVisible(true);
    }

    private void makePlantBedPage() {
        JList<PlantBed> pbList = createJListPB();
        JTextField selectedPB = new JTextField();
        plantBedPage.add(selectedPB);
        JPanel plantBedActions = new JPanel();
        JButton goToPlant = new JButton("Go to Plant");
        JDialog plantWindow = new JDialog(jf, true);
        pbList.addListSelectionListener(e -> {
            PlantBed selectedPlantBed = pbList.getSelectedValue();
            selectedPB.setText("Current selected: " + selectedPlantBed.getName() + " \nNumber of plants: "
                    + selectedPlantBed.getPlantArrayList().size());
            plantWindow.removeAll();
            plantWindow.revalidate();
        });
        goToPlant.addActionListener(e -> {
            plantWindow.repaint();
            plantWindow.revalidate();
            updatePlantWindow(pbList.getSelectedValue(), plantWindow);
            plantWindow.setVisible(true);
        });
        newPlantBedButton();
        plantBedPage.add(goToPlant);
        plantBedPage.add(backButton);
    }

    private void newPlantBedButton() {
        JButton addNewPB = new JButton("Add new plant-bed");
        addNewPB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jd = new JDialog();
                jd.setLayout(new FlowLayout());
                jd.setResizable(false);
                jd.add(new JLabel("What is the name of the new plant bed?"));
                JTextField nameField = new JTextField(20);
                JButton createBedBtn = new JButton("Create PlantBed");
                createBedBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        myGarden.addPlantBed(new PlantBed(nameField.getText()));
                        jd.setVisible(false);
                    }
                });
                jd.add(nameField);
                jd.add(createBedBtn);
                jd.pack();
                jd.setVisible(true);
            }
        });
        plantBedPage.add(addNewPB);
    }

    private JList<PlantBed> createJListPB() {
        JList<PlantBed> pbList = new JList<>(myGarden.getPlantBedArrayList().toArray(new PlantBed[0]));
        PlantBedRenderer pbr = new PlantBedRenderer();
        pbList.setCellRenderer(pbr);
        pbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        plantBedPage.add(pbList);
        return pbList;
    }

    private void updatePlantWindow(PlantBed pb, JDialog window) {
        JLabel pbName = new JLabel(pb.getName());
        window.add(pbName);
        window.setSize(300,300);
        window.repaint();
        window.revalidate();
    }


    private void initializeMainPage() {
        mainPage = new JPanel();
        jf.setContentPane(mainPage);
        addPlantBedsButton();
        addGardenStatsButton();
        addWateringButton();
        addSaveAndLoadUIButton();
        addQuitButton();
        createBackButton();
    }

    private void addQuitButton() {
        JButton quitButton = new JButton("Quit Program");
        quitButton.addActionListener(e -> {
            JDialog saveQuitDialog = new JDialog();
            saveQuitDialog.setLayout(new FlowLayout());
            saveQuitDialog.add(new JLabel("Would you like to save before quitting?"));
            JButton yesBtn = new JButton("Yes");
            JButton quitBtn = new JButton("No");
            quitBtn.addActionListener(e1 -> System.exit(0));
            yesBtn.addActionListener(e1 -> {
                saveAndQuit(quitBtn);
            });
            saveQuitDialog.add(yesBtn);
            saveQuitDialog.add(quitBtn);
            saveQuitDialog.setLocationRelativeTo(null);
            saveQuitDialog.pack();
            saveQuitDialog.setVisible(true);
        });
        mainPage.add(quitButton);
    }

    private void saveAndQuit(JButton quit) {
        JDialog jd = new JDialog();
        jd.setLayout(new FlowLayout());
        jd.add(new JLabel(saveGarden()));
        quit.setText("Quit");
        jd.add(quit);
        jd.pack();
        jd.setLocationRelativeTo(null);
        jd.setVisible(true);
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
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setContentPane(saveAndLoadPanel);
                jf.repaint();
                jf.revalidate();
            }
        });
        mainPage.add(b4);
    }

    private void setupSavePanel() {
    }

    private void addWateringButton() {
        JButton b3 = new JButton("Watering Way-Finder");
        b3.setBounds(alignX, 200, btnWidth, btnHeight);
        mainPage.add(b3);
    }

    private void createBackButton() {
        backButton = new JButton("Back to Main Menu");
        backButton.setSize(btnWidth,btnHeight);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setContentPane(mainPage);
                repaint();
                revalidate();
            }
        });
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
        switch (userInput) {
            case "a":
                statisticsMenu();
                break;
            case "b":
                gardenMenu();
                break;
            case "c":
                waterWayFinderMenu();
                break;
            case "d":
                saveAndRetrieveMenu();
                break;
            default:
                System.out.println("Unrecognized command, please try again.");
                break;
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
    public String retrieveGarden() {
        try {
            readerJson = new ReaderJson(SOURCE_JSON);
            myGarden = readerJson.readSource();
            return "Loaded Garden from " + SOURCE_JSON + "!";
        } catch (IOException e) {
            return "Unable to read from file " + SOURCE_JSON;
        }
    }

    //EFFECT: saves current garden to JSON file at SOURCE_JSON
    //        if unable to do so, print out message and do nothing
    public String saveGarden() {
        try {
            writerJson = new WriterJson(SOURCE_JSON);
            writerJson.open();
            writerJson.writeToJson(myGarden);
            writerJson.close();
            return "Saved Garden to " + SOURCE_JSON + "!";
        } catch (IOException e) {
            return "Unable to save from file " + SOURCE_JSON;
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
        switch (s) {
            case "a":
                viewPlant(pb);
                break;
            case "b":
                removePlant(pb);
                break;
            case "c":
                waterPlant(pb);
                break;
            case "d":
                addPlant(pb);
                break;
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
