package ui;


import javafx.scene.image.Image;
import model.Garden;
import model.Plant;
import model.PlantBed;
import persistence.ReaderJson;
import persistence.WriterJson;
import ui.renderers.PlantBedRenderer;
import ui.renderers.PlantRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//TODO: make add plant window close when finished
//TODO: add back button on all pages



// provides the console based ui for using the Garden Manager application
//       this class was made based of the TellerApp class from the TellerApp Project:
//       https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class GardenApp extends JFrame {
    private static final int SPACING = 20;
    private Garden myGarden;
    private Scanner input;
    private static final String SOURCE_JSON = "./data/garden.json";
    private ReaderJson readerJson;


    private JFrame jf;
    private static final int WIDTH = 700;
    private static final int HEIGHT = 450;

    private final int btnWidth = 200;
    private final int btnHeight = 50;
    private final int alignX = 400;
    private JPanel mainPage;
    private JPanel plantBedPage;
    private JPanel saveAndLoadPanel;
    private JPanel waterWayFinderPage;

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
        jf.setSize(700, 450);
    }

    public void initializeGUI() {
        initializeMainPage();
        initializePanels();

        jf.pack();

        jf.setResizable(false);
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveGardenDialog();
            }
        });
        jf.setVisible(true);
    }

    private void initializePanels() {
        saveAndLoadPanel = new JPanel();
        saveAndLoadPanel.setLayout(null);
        initSandL();

        plantBedPage = new JPanel();
        plantBedPage.setLayout(null);
        makePlantBedPage();

        waterWayFinderPage = new JPanel();
        makeWaterPage();
    }

    private void makeWaterPage() {
        JLabel currentBed = new JLabel();
        JLabel currentPlant = new JLabel();
        JButton nextButton = new JButton("Begin");

        nextButton.addActionListener(e -> {
            JToggleButton jb = new JToggleButton("Water and go to next Plant");
            waterWayFinderPage.add(jb);
            for (PlantBed pb : myGarden.getPlantBedArrayList()) {
                currentBed.setText("Current plant bed: " + pb.getName());
                for (Plant p : pb.getPlantArrayList()) {
                    if (p.getDry()) {
                        JOptionPane.showMessageDialog(jf,p.getName() + " at " + pb.getName() + "needs water!");
                        p.setDry(false);
                    }
                }
            }
            JOptionPane.showMessageDialog(jf, "All Plants have been watered!");
            jf.setContentPane(mainPage);
        });

        waterWayFinderPage.add(currentBed);
        waterWayFinderPage.add(currentPlant);
        waterWayFinderPage.add(nextButton);
        waterWayFinderPage.add(createBackButton());
    }

    private void initSandL() {
        JDialog message = new JDialog();

        JLabel j = new JLabel("Would you like to revert to last saved state or save in current state?");
        j.setBounds(150, 100, WIDTH, 20);
        saveAndLoadPanel.add(j);
        JButton b1 = new JButton("Revert to last save");
        b1.addActionListener(e -> showMessage(message, retrieveGarden()));
        saveAndLoadPanel.add(b1);
        b1.setBounds(25, 200, btnWidth, btnHeight);
        JButton b2 = new JButton("Save current Garden");
        b2.addActionListener(e -> showMessage(message, saveGarden()));
        b2.setBounds(b1.getX() + btnWidth + 20, 200, btnWidth, btnHeight);
        saveAndLoadPanel.add(b2);
        JButton backBtn = createBackButton();
        backBtn.setBounds(b2.getX() + btnWidth + 20, 200, btnWidth, btnHeight);
        saveAndLoadPanel.add(backBtn);
    }

    private void showMessage(JDialog jd, String s) {
        JLabel message = new JLabel(s);
        jd.add(message, BorderLayout.CENTER);
        jd.setVisible(true);
    }

    private void makePlantBedPage() {
        JList<PlantBed> pbList = createJListPB();
        JLabel selectedPB = new JLabel("No Plant Bed selected");
        selectedPB.setBounds(alignX,50,300, 300);
        plantBedPage.add(selectedPB);
        JButton goToPlant = new JButton("Go to Plant");
        JDialog plantWindow = new JDialog(jf, true);
        pbList.addListSelectionListener(e -> {
            try {
                PlantBed selectedPlantBed = pbList.getSelectedValue();
                selectedPB.setText("<html>Current selected: " + selectedPlantBed.getName() + "<br/>Number of plants: "
                        + selectedPlantBed.getPlantArrayList().size());
                plantWindow.revalidate();
            } catch (NullPointerException ne) {
                selectedPB.setText("");
            }
        });
        goToPlant.addActionListener(e -> {
            try {
                createPlantWindow(pbList, plantWindow);
            } catch (NullPointerException ne) {
                // do nothing -- happen when trying to go to plant without bed selected
            }

        });
        addPlantBedBtns(pbList, goToPlant);
    }

    private void createPlantWindow(JList<PlantBed> pbList, JDialog plantWindow) {
        plantWindow.setContentPane(new JPanel());
        JList plants = new JList(pbList.getSelectedValue().getPlantArrayList().toArray(new Plant[0]));
        updatePlantWindow(plants, pbList.getSelectedValue(), plantWindow);
        plantWindow.setVisible(true);
    }

    private void addPlantBedBtns(JList<PlantBed> pbList, JButton goToPlant) {
        addDeletePopup(pbList);
        newPlantBedButton(pbList);
        goToPlant.setBounds(alignX, 100, btnWidth, btnHeight);
        plantBedPage.add(goToPlant);
        JButton backButton = createBackButton();
        backButton.setBounds(alignX, 300, btnWidth, btnHeight);
        plantBedPage.add(backButton);
    }

    private void addDeletePopup(JList jlist) {
        JPopupMenu edit = new JPopupMenu("Edit PlantBeds");
        jlist.setComponentPopupMenu(edit);
        JMenuItem delete = new JMenuItem("Delete");
        edit.add(delete);
        delete.addActionListener(e -> {
            PlantBed selectedPB = (PlantBed) jlist.getSelectedValue();
            myGarden.getPlantBedArrayList().remove(selectedPB);
            jlist.clearSelection();
            jlist.setListData(myGarden.getPlantBedArrayList().toArray(new PlantBed[0]));
        });
        plantBedPage.add(edit);
    }

    private void newPlantBedButton(JList jlist) {
        JButton addNewPB = new JButton("Add new plant-bed");
        addNewPB.addActionListener(e -> {
            JDialog jd = new JDialog();
            jd.setLayout(new FlowLayout());
            jd.setResizable(false);
            jd.add(new JLabel("What is the name of the new plant bed?"));
            JTextField nameField = new JTextField(20);
            JButton createBedBtn = new JButton("Create PlantBed");
            createBedBtn.addActionListener(e1 -> {
                myGarden.addPlantBed(new PlantBed(nameField.getText()));
                jd.setVisible(false);
                jlist.setListData(myGarden.getPlantBedArrayList().toArray(new PlantBed[0]));
            });
            jd.add(nameField);
            jd.add(createBedBtn);
            jd.pack();
            jd.setVisible(true);
        });
        addNewPB.setBounds(alignX, 50, btnWidth, btnHeight);
        plantBedPage.add(addNewPB);
    }

    private JList<PlantBed> createJListPB() {
        JList<PlantBed> pbList = new JList<>(myGarden.getPlantBedArrayList().toArray(new PlantBed[0]));
        PlantBedRenderer pbr = new PlantBedRenderer();
        pbList.setCellRenderer(pbr);
        pbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pbList.setBounds(0,0,WIDTH / 2, HEIGHT);
        plantBedPage.add(pbList);
        return pbList;
    }

    private void updatePlantWindow(JList plants, PlantBed pb, JDialog window) {
        PlantRenderer pr = new PlantRenderer();
        plants.setCellRenderer(pr);
        plants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        window.setLayout(new FlowLayout());
        window.add(plants);
        JLabel plantInfo = new JLabel("No plant selected");
        JPopupMenu editPlants = getPlantsPopupMenu(plants, pb, window, plantInfo);

        window.add(editPlants);
        window.add(plantInfo);
        window.pack();
    }

    private JPopupMenu getPlantsPopupMenu(JList plants, PlantBed pb, JDialog window, JLabel plantInfo) {
        plants.addListSelectionListener(e -> {
            makePlantInfoText(plants, window, plantInfo);
        });
        addNewPlant(window, pb);

        JPopupMenu editPlants = new JPopupMenu("Edit Plants");
        plants.setComponentPopupMenu(editPlants);
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem waterPlant = new JMenuItem("Water Plant");

        editPlants.add(delete);
        delete.addActionListener(e -> {
            Plant selectedPlant = (Plant) plants.getSelectedValue();
            pb.getPlantArrayList().remove(selectedPlant);
            plants.setListData(pb.getPlantArrayList().toArray(new Plant[0]));
            plants.clearSelection();
        });

        editPlants.add(waterPlant);
        waterPlant.addActionListener(e -> {
            waterPlantPopup(plants, pb, window);
        });
        return editPlants;
    }

    private void waterPlantPopup(JList plants, PlantBed pb, JDialog window) {
        Plant selectedPlant = (Plant) plants.getSelectedValue();
        if (pb.waterPlant(pb.getPlantArrayList().indexOf(selectedPlant))) {
            JOptionPane.showMessageDialog(window, "Successfully Watered!");
        } else {
            JOptionPane.showMessageDialog(window,
                    "Sorry, this plant is already watered! (do you want to drown it?)");
        }
    }

    private void makePlantInfoText(JList plants, JDialog window, JLabel plantInfo) {
        Plant p = (Plant) plants.getSelectedValue();
        plantInfo.setText("<html>Name: " + p.getName() + "<br/>Type: " + p.getPlantType()
                + " <br/>Watering Cycle: " + p.getWaterCycle() + " <br/> Needs Water? " + p.getDry());
        plantInfo.setVisible(true);
        window.pack();
    }

    private void addNewPlant(JDialog window, PlantBed pb) {
        JButton addNewPlantBtn = new JButton("Add new plant");
        addNewPlantBtn.addActionListener(e -> {
            JPanel jd = new JPanel();
            JTabbedPane jtp = new JTabbedPane();
            jd.add(jtp);
            window.setContentPane(jd);
            JTextField plantName = new JTextField(15);
            JComboBox plantTypeCB = new JComboBox(Plant.PLANT_TYPES);
            JComboBox plantWaterCB = new JComboBox(Plant.WATER_CYCLE);
            JComboBox plantLifeCB = new JComboBox(Plant.LIFE_STAGE);
            setupNewPlantMenu(jtp, plantName, plantTypeCB, plantWaterCB, plantLifeCB);
            JButton createPlant = new JButton("Create Plant");
            createPlant.addActionListener(e1 -> {
                pb.addPlant(plantName.getText(), plantWaterCB.getSelectedItem().toString(),
                        plantTypeCB.getSelectedItem().toString(), plantLifeCB.getSelectedItem().toString());
                JOptionPane.showMessageDialog(jd, "The plant was successfully added",
                        "New Plant", JOptionPane.WARNING_MESSAGE);
            });
            jtp.addTab("Finish", createPlant);
            window.pack();
        });
        window.add(addNewPlantBtn);
    }

    private void setupNewPlantMenu(JTabbedPane jtp, JTextField plantName, JComboBox plantTypeCB,
                                   JComboBox plantWaterCB, JComboBox plantLifeCB) {
        JPanel nameTab = new JPanel();
        nameTab.add(new JLabel("What is the name of your new plant?"));
        nameTab.add(plantName);
        jtp.addTab("Name", nameTab);
        JPanel typeTab = new JPanel();
        typeTab.add(new JLabel("<html>What type of plant is it?"));
        typeTab.add(plantTypeCB);
        jtp.addTab("Type", typeTab);
        JPanel waterTab = new JPanel();
        waterTab.add(new JLabel("<html>How frequently do you need to water this plant?"));
        waterTab.add(plantWaterCB);
        jtp.addTab("Watering Cycle", waterTab);
        JPanel ageTab = new JPanel();
        ageTab.add(new JLabel("<html>At what life stage is this plant in?"));
        ageTab.add(plantLifeCB);
        jtp.addTab("LifeStage", ageTab);
    }


    private void initializeMainPage() {
        mainPage = new JPanel();
        mainPage.setLayout(null);
        mainPage.setBackground(new Color(0x84D384));
        jf.setContentPane(mainPage);

        try {
            BufferedImage img = ImageIO.read(new File("data/imgs/plant.png"));
            JLabel imgLoc = new JLabel(new ImageIcon(img));
            imgLoc.setBounds(0,0,300,HEIGHT);
            jf.add(imgLoc);
        } catch (IOException e) {
            // do nothing
        }
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
            saveGardenDialog();
        });
        quitButton.setBounds(alignX, 300, btnWidth, btnHeight);
        mainPage.add(quitButton);
    }

    private void saveGardenDialog() {
        JDialog saveQuitDialog = new JDialog();
        saveQuitDialog.setLocationRelativeTo(null);
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


    public void addPlantBedsButton() {
        JButton b1 = new JButton("View/Modify Plant Beds");
        b1.setBounds(alignX, 50, btnWidth, btnHeight);
        b1.addActionListener(e -> {
            jf.setContentPane(plantBedPage);
            jf.repaint();
            jf.revalidate();
        });
        mainPage.add(b1);
    }

    public void addGardenStatsButton() {
        JButton b2 = new JButton("View Garden Stats");
        JDialog statsWindow = new JDialog(jf, "Garden Stats", true);
        statsWindow.setLayout(null);
        b2.setBounds(alignX, 100, btnWidth, btnHeight);
        b2.addActionListener(e -> statsWindow.setVisible(true));
        String numDryPlants = generateDryPlants();
        JLabel plantBedNum = new JLabel("\nThere are " + myGarden.getNumOfPlantBeds() + " plant bed(s).");
        plantBedNum.setBounds(70, 50, 300, 50);
        statsWindow.add(plantBedNum);
        JLabel plantsNum = new JLabel("\nThere are " + myGarden.getNumOfPlants() + " plant(s).");
        plantsNum.setBounds(70, 100, 300, 50);
        statsWindow.add(plantsNum);
        JLabel dryPlantsNum = new JLabel(numDryPlants);
        dryPlantsNum.setBounds(70, 150, 300, 50);
        statsWindow.add(dryPlantsNum);
        statsWindow.setSize(400, 300);
        mainPage.add(b2);
    }

    public String generateDryPlants() {
        int count = 0;
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            for (Plant p : pb.getPlantArrayList()) {
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
        b4.addActionListener(e -> {
            jf.setContentPane(saveAndLoadPanel);
            jf.repaint();
            jf.revalidate();
        });
        mainPage.add(b4);
    }

    private void addWateringButton() {
        JButton b3 = new JButton("Watering Way-Finder");
        b3.setBounds(alignX, 200, btnWidth, btnHeight);
        mainPage.add(b3);
        b3.addActionListener(e -> {
            jf.setContentPane(waterWayFinderPage);
            jf.repaint();
            jf.revalidate();
        });
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setSize(btnWidth, btnHeight);
        backButton.addActionListener(e -> {
            jf.setContentPane(mainPage);
            repaint();
            revalidate();
        });
        return backButton;
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
            System.out.println("Unable to retrieve saved data from " + SOURCE_JSON);
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
    public void mainMenuCommands(String userInput) {
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
            WriterJson writerJson = new WriterJson(SOURCE_JSON);
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
            for (Plant p : pb.getPlantArrayList()) {
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
            for (Plant p : pb.getPlantArrayList()) {
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
        for (Plant p : pb.getPlantArrayList()) {
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
        pb.addPlant(new Plant(name, water, type, age));
        System.out.println("The plant '" + name + "' was added to the bed '" + pb.getName() + "'!");
    }

    //MODIFIES: this
    //EFFECTS: initializes example garden data for manipulation
    public void startUp() {
        Plant p1 = new Plant("Rose", "Weekly", "Bulb", "Mature");
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
