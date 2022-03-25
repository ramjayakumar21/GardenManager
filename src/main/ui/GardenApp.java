package ui;


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

// provides the graphics based ui for using the Garden Manager application
//       this class was originally based off the TellerApp class from the TellerApp Project:
//       https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class GardenApp extends JFrame {
    private Garden myGarden;
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
    private JPanel saveAndLoadPage;
    private JPanel waterWayFinderPage;
    private JDialog statsDialog;

    //EFFECTS: calls main menu method to initialize the GUI
    public GardenApp() {
        mainMenu();
    }

    //MODIFIES: this, jf, myGarden
    //EFFECTS: creates and initializes main frame for application,
    //         also initializes GUI and data for startup
    public void mainMenu() {
        jf = new JFrame("Garden Manager");

        loadStartUp();
        initializeGUI();

        jf.setResizable(false);
        jf.pack();
        jf.setSize(WIDTH, HEIGHT);
        jf.setVisible(true);

        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveGardenDialog();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to mainPage and initializes pages for first use
    public void initializeGUI() {
        initializeMainPageButtons();
        initializePages();
    }

    // MODIFIES: plantBedPage, saveAndLoadPage, waterWayFinderPage
    // EFFECTS: sets pages up before first use
    public void initializePages() {
        saveAndLoadPage = new JPanel();
        saveAndLoadPage.setLayout(null);
        makeSLPage();

        makePlantBedPage();

        waterWayFinderPage = new JPanel();
        waterWayFinderPage.setLayout(null);
        waterWayFinderPage.setBackground(new Color(0x5D5DE0));
        makeWaterPage();
    }

    // MODIFIES: this
    // EFFECTS: initializes main page  and adds buttons to main page for garden app
    private void initializeMainPageButtons() {
        mainPage = new JPanel();
        mainPage.setLayout(null);
        mainPage.setBackground(new Color(0x84D384));
        jf.setContentPane(mainPage);

        try {
            BufferedImage img = ImageIO.read(new File("data/img/plant.png"));
            JLabel imgLoc = new JLabel(new ImageIcon(img));
            imgLoc.setBounds(0,0,300,HEIGHT);
            jf.add(imgLoc);
        } catch (IOException e) {
            // do nothing -- when unable to get image
        }
        addViewPlantBedsButton();
        addGardenStatsButton();
        addWateringButton();
        addSaveAndLoadButton();
        createBackButton();

        JButton quitButton = new JButton("Quit Program");
        quitButton.addActionListener(e -> saveGardenDialog());
        quitButton.setBounds(alignX, 300, btnWidth, btnHeight);
        mainPage.add(quitButton);
    }

    // MODIFIES: this, mainPage
    // EFFECTS: adds button to view plantBedPage from main page
    public void addViewPlantBedsButton() {
        JButton b1 = new JButton("View/Modify Plant Beds");
        b1.setBounds(alignX, 50, btnWidth, btnHeight);
        b1.addActionListener(e -> {
            makePlantBedPage();
            jf.setContentPane(plantBedPage);
            jf.repaint();
            jf.revalidate();
        });
        mainPage.add(b1);
    }

    // MODIFIES: this, mainPage
    // EFFECTS: adds garden stats button to main page
    public void addGardenStatsButton() {
        JButton b2 = new JButton("View Garden Stats");
        statsDialog = new JDialog(jf, "Garden Stats", true);
        statsDialog.setLayout(null);
        b2.setBounds(alignX, 100, btnWidth, btnHeight);
        b2.addActionListener(e -> {
                    setGardenStats();
                    statsDialog.setVisible(true);
                }
        );
        mainPage.add(b2);
    }

    // MODIFIES: this, mainPage
    // EFFECTS: adds save and load button to main page
    private void addSaveAndLoadButton() {
        JButton b4 = new JButton("Save/Load");
        b4.setBounds(alignX, 150, btnWidth, btnHeight);
        b4.addActionListener(e -> {
            jf.setContentPane(saveAndLoadPage);
            jf.repaint();
            jf.revalidate();
        });
        mainPage.add(b4);
    }

    // MODIFIES: this, mainPage
    // EFFECTS: adds watering way-finder button to main page
    private void addWateringButton() {
        JButton b3 = new JButton("Watering Way-Finder");
        b3.setBounds(alignX, 200, btnWidth, btnHeight);
        b3.addActionListener(e -> {
            jf.setContentPane(waterWayFinderPage);
            jf.repaint();
            jf.revalidate();
        });
        mainPage.add(b3);
    }

    // MODIFIES: waterWayFinderPage
    // EFFECTS: adds buttons and functionality to waterWayFinderPage
    public void makeWaterPage() {
        JButton nextButton = new JButton("Begin WaterWayFinder");
        nextButton.setBounds(150, 150, btnWidth * 2, btnHeight * 2);
        nextButton.setIcon(createImageIcon("data/img/watering.png","",100,100));

        nextButton.addActionListener(e -> {
            JToggleButton jb = new JToggleButton("Water and go to next Plant");
            waterWayFinderPage.add(jb);
            for (PlantBed pb : myGarden.getPlantBedArrayList()) {
                for (Plant p : pb.getPlantArrayList()) {
                    if (p.getDry()) {
                        JOptionPane.showMessageDialog(jf,
                                p.getName() + " at " + pb.getName() + " needs water!","Watering WayFinder",
                                JOptionPane.WARNING_MESSAGE,
                                createImageIcon("data/img/waterIcon.png", "d", 100, 100));
                        p.setDry(false);
                    }
                }
            }
            JOptionPane.showMessageDialog(jf, "All Plants have been watered!");
            jf.setContentPane(mainPage);
        });
        waterWayFinderPage.add(nextButton);
        waterWayFinderPage.add(createBackButton());
    }

    // MODIFIES: saveAndLoadPage
    // EFFECTS: adds buttons and functionality to saveAndLoadPage
    public void makeSLPage() {
        JLabel j = new JLabel("Would you like to revert to last saved state or save in current state?");
        j.setBounds(150, 100, WIDTH, 20);
        saveAndLoadPage.add(j);
        JButton b1 = new JButton("Revert to last save");
        b1.addActionListener(e -> JOptionPane.showMessageDialog(jf, new JLabel(retrieveGarden())));
        saveAndLoadPage.add(b1);
        b1.setBounds(25, 200, btnWidth, btnHeight);
        JButton b2 = new JButton("Save current Garden");
        b2.addActionListener(e -> JOptionPane.showMessageDialog(jf, new JLabel(saveGarden())));
        b2.setBounds(b1.getX() + btnWidth + 20, 200, btnWidth, btnHeight);
        saveAndLoadPage.add(b2);
        JButton backBtn = createBackButton();
        backBtn.setBounds(b2.getX() + btnWidth + 20, 200, btnWidth, btnHeight);
        saveAndLoadPage.add(backBtn);
    }

    // MODIFIES: plantBedPage
    // EFFECTS: adds buttons and functionality to plantBedPage
    public void makePlantBedPage() {
        plantBedPage = new JPanel();
        plantBedPage.setLayout(null);
        JList<PlantBed> pbList = createPBList();
        JLabel selectedPB = createPlantBedInfoLabel();
        JButton goToPlant = new JButton("Visit selected plant-bed");
        JDialog plantWindow = new JDialog(jf, true);
        pbList.addListSelectionListener(e -> {
            try {
                PlantBed selectPB = pbList.getSelectedValue();
                selectedPB.setText("<html>Current plant-bed selected: " + selectPB.getName() + "<br/>Number of plants: "
                        + selectPB.getPlantArrayList().size() + "<br/>Number of dry plants: "
                        + generateDryPlants(selectPB)  + "<br/<br/><i>*right click bed to delete*</i>");
            } catch (NullPointerException ne) {
                selectedPB.setText("");
            }
        });
        goToPlant.addActionListener(e -> {
            try {
                createPlantWindow(pbList, plantWindow);
            } catch (NullPointerException ne) {
                // do nothing -- happens when trying to go to plant without bed selected
            }
        });
        addPBPageButtons(pbList, goToPlant);
    }

    // EFFECTS: returns a label for plant bed info on the plantBedPage
    public JLabel createPlantBedInfoLabel() {
        JLabel selectedPB = new JLabel("No plant-bed selected");
        selectedPB.setBounds(alignX,50,300, 300);
        plantBedPage.add(selectedPB);
        return selectedPB;
    }

    // MODIFIES: plantWindow, pbList
    // EFFECTS: creates new panel for plant window based on selected plant-bed in pbList
    public void createPlantWindow(JList<PlantBed> pbList, JDialog plantWindow) {
        plantWindow.setContentPane(new JPanel());
        JList plants = new JList(pbList.getSelectedValue().getPlantArrayList().toArray(new Plant[0]));
        plants.setBounds(new Rectangle(100, 100));
        updatePlantWindow(plants, pbList.getSelectedValue(), plantWindow);
        plantWindow.setVisible(true);
    }

    // MODIFIES: plantBedPage, pbList, goToPlant
    // EFFECTS: adds list of plant-beds in garden and popup menu for deleting plant beds
    //          also adds buttons to access selected plantBed and go back to menu
    public void addPBPageButtons(JList<PlantBed> pbList, JButton goToPlant) {
        initDeletePBPopup(pbList);
        newPlantBedButton(pbList);
        goToPlant.setBounds(alignX, 100, btnWidth, btnHeight);
        plantBedPage.add(goToPlant);
        JButton backButton = createBackButton();
        backButton.setBounds(alignX, 300, btnWidth, btnHeight);
        plantBedPage.add(backButton);
    }

    // MODIFIES: plantBedPage, pbList, goToPlant
    // EFFECTS: adds list of plant-beds in garden and popup menu for deleting plant beds
    //          also adds buttons to access selected plantBed and go back to menu
    public void initDeletePBPopup(JList jlist) {
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

    // MODIFIES: allPlantBeds
    // EFFECTS: shows dialog and creates new plant bed with given name
    public void newPlantBedButton(JList allPlantBeds) {
        JButton addNewPB = new JButton("Add new plant-bed");
        addNewPB.addActionListener(e -> {
            JDialog jd = new JDialog();
            jd.setLayout(new FlowLayout());
            jd.setResizable(false);
            jd.add(new JLabel("What is the name of the new plant-bed?"));
            JTextField nameField = new JTextField(20);
            JButton createBedBtn = new JButton("Create plant-bed");
            createBedBtn.addActionListener(e1 -> {
                myGarden.addPlantBed(new PlantBed(nameField.getText()));
                jd.setVisible(false);
                allPlantBeds.setListData(myGarden.getPlantBedArrayList().toArray(new PlantBed[0]));
            });
            jd.add(nameField);
            jd.add(createBedBtn);
            jd.pack();
            jd.setVisible(true);
        });
        addNewPB.setBounds(alignX, 50, btnWidth, btnHeight);
        plantBedPage.add(addNewPB);
    }

    // MODIFIES: this
    // EFFECTS: creates new list with plant-beds in garden
    public JList<PlantBed> createPBList() {
        JList<PlantBed> pbList = new JList<>(myGarden.getPlantBedArrayList().toArray(new PlantBed[0]));
        PlantBedRenderer pbr = new PlantBedRenderer();
        pbList.setCellRenderer(pbr);
        pbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pbList.setBounds(0,0,WIDTH / 2, HEIGHT);
        plantBedPage.add(pbList);
        return pbList;
    }

    // MODIFIES: plants, pb, window
    // EFFECTS: creates plant dialog window from selected plant bed pb
    public void updatePlantWindow(JList plants, PlantBed pb, JDialog window) {
        PlantRenderer pr = new PlantRenderer();
        plants.setCellRenderer(pr);
        plants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        window.setLayout(new FlowLayout());
        plants.setBounds(0,0,300,300);
        window.add(plants);
        JLabel plantInfo = new JLabel("No plant selected");
        JPopupMenu editPlants = getPlantsPopupMenu(plants, pb, window, plantInfo);

        window.pack();
        window.add(editPlants);
        window.add(plantInfo);
    }

    // MODIFIES: plants, pb, window, plantInfo
    // EFFECTS: creates popup window for given plants to allow deleting and watering of plants
    public JPopupMenu getPlantsPopupMenu(JList plants, PlantBed pb, JDialog window, JLabel plantInfo) {
        plants.addListSelectionListener(e -> makePlantInfoText(plants, window, plantInfo));
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
            plantInfo.setText("");
            plants.clearSelection();
        });

        editPlants.add(waterPlant);
        waterPlant.addActionListener(e -> {
            waterPlantPopup(plants, pb, window);
        });
        return editPlants;
    }

    // MODIFIES: plants
    // EFFECTS: on right click of a plant in JList plants, lets user water a plant
    //          and shows window with response string
    private void waterPlantPopup(JList plants, PlantBed pb, JDialog window) {
        Plant selectedPlant = (Plant) plants.getSelectedValue();
        if (pb.waterPlant(pb.getPlantArrayList().indexOf(selectedPlant))) {
            JOptionPane.showMessageDialog(window, "Successfully Watered!");
        } else {
            JOptionPane.showMessageDialog(window,
                    "Sorry, this plant is already watered! (do you want to drown it?)");
        }
    }

    // MODIFIES: window, plantInfo
    // EFFECTS: creates text info for current select plant in plants
    private void makePlantInfoText(JList plants, JDialog window, JLabel plantInfo) {
        Plant p = (Plant) plants.getSelectedValue();
        if (p == null) {
            return;
        }
        plantInfo.setText("<html>Name: " + p.getName() + "<br/>Type: " + p.getPlantType()
                + " <br/>Watering Cycle: " + p.getWaterCycle() + " <br/> Needs Water?: " + p.getDry()
                + " <br/>Age: " + p.getLifeStage()
                + "<br/<br/><i>*right click plant to delete/water*</i>");
        plantInfo.setVisible(true);
        window.pack();
    }

    // MODIFIES: this, pb, window
    // EFFECTS: adds new plant via a tabbed plane for options to choose
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
            setupNewPlantDialog(jtp, plantName, plantTypeCB, plantWaterCB, plantLifeCB);
            JButton createPlant = new JButton("Create Plant");
            createPlant.addActionListener(e1 -> {
                pb.addPlant(plantName.getText(), plantWaterCB.getSelectedItem().toString(),
                        plantTypeCB.getSelectedItem().toString(), plantLifeCB.getSelectedItem().toString());
                JOptionPane.showMessageDialog(jd, "The plant was added!", "New Plant", 2);
                makePlantBedPage();
                window.dispose();
            });
            jtp.addTab("Finish", createPlant);
            window.pack();
        });
        window.add(addNewPlantBtn);
    }

    // MODIFIES: jtp, plantName, plantTypeCB, plantWaterCB, plantLifeCB
    // EFFECTS: setup for jtp tabbed pane to have a text box/combo box for user to create new plant
    private void setupNewPlantDialog(JTabbedPane jtp, JTextField plantName, JComboBox plantTypeCB,
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




    // MODIFIES: this
    // EFFECTS: allows user to choose to save when closing program
    private void saveGardenDialog() {
        JDialog saveQuitDialog = new JDialog();
        saveQuitDialog.setLocationRelativeTo(null);
        saveQuitDialog.setLayout(new FlowLayout());
        saveQuitDialog.add(new JLabel("Would you like to save before quitting?"));
        JButton yesBtn = new JButton("Yes");
        JButton quitBtn = new JButton("No");
        quitBtn.addActionListener(e1 -> System.exit(0));
        yesBtn.addActionListener(e1 -> {
            saveAndQuitResult(quitBtn);
        });
        saveQuitDialog.add(yesBtn);
        saveQuitDialog.add(quitBtn);
        saveQuitDialog.setLocationRelativeTo(null);
        saveQuitDialog.pack();
        saveQuitDialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: attempts to save garden in current state and prints result string
    private void saveAndQuitResult(JButton quit) {
        JDialog jd = new JDialog();
        jd.setLayout(new FlowLayout());
        jd.add(new JLabel(saveGarden()));
        quit.setText("Quit");
        jd.add(quit);
        jd.pack();
        jd.setLocationRelativeTo(null);
        jd.setVisible(true);
    }

    // MODIFIES: this, statsDialog
    // EFFECTS: updates statsDialog popup with number of plant-beds, plants and dry plants
    private void setGardenStats() {
        statsDialog = new JDialog(jf, "Garden Stats", true);
        statsDialog.setSize(300, 300);
        statsDialog.setLayout(null);
        JLabel plantBedNum = new JLabel("\nThere are " + myGarden.getNumOfPlantBeds() + " plant bed(s).");
        plantBedNum.setBounds(70, 50, 300, 50);
        statsDialog.add(plantBedNum);
        JLabel plantsNum = new JLabel("\nThere are " + myGarden.getNumOfPlants() + " plant(s).");
        plantsNum.setBounds(70, 100, 300, 50);
        statsDialog.add(plantsNum);
        int totalDryPlants = 0;
        for (PlantBed pb : myGarden.getPlantBedArrayList()) {
            totalDryPlants += generateDryPlants(pb);
        }
        JLabel dryPlantsNum = new JLabel("\nThere are " + totalDryPlants + " dry plant(s).");
        dryPlantsNum.setBounds(70, 150, 300, 50);
        statsDialog.add(dryPlantsNum);
    }

    // EFFECTS: returns number of dry plants in given plant-bed
    public int generateDryPlants(PlantBed pb) {
        int count = 0;
        for (Plant p : pb.getPlantArrayList()) {
            if (p.getDry()) {
                count += 1;
            }
        }
        return count;
    }

    // EFFECTS: creates new back button that returns to main page on click
    //          and then returns the button
    private JButton createBackButton() {
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setSize(btnWidth, btnHeight);
        backButton.addActionListener(e -> {
            jf.setContentPane(mainPage);
            repaint();
            revalidate();
        });
        backButton.setBackground(new Color(0xE78E8E));
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


    //MODIFIES: myGarden
    //EFFECTS: retrieves garden from JSON file, replaces current garden with it, and returns success string
    //         if unable to do so, return error string
    public String retrieveGarden() {
        try {
            readerJson = new ReaderJson(SOURCE_JSON);
            myGarden = readerJson.readSource();
            return "Loaded Garden from " + SOURCE_JSON + "!";
        } catch (IOException e) {
            return "Unable to read from file " + SOURCE_JSON;
        }
    }

    //EFFECT: saves current garden to JSON file at SOURCE_JSON and returns success string
    //        if unable to do so, return error string
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

    // SOURCE : https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
    // EFFECTS: Returns an ImageIcon from given path, or null if the path was invalid.
    protected ImageIcon createImageIcon(String path,
                                        String description, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            ImageIcon i = new ImageIcon(img, description);
            return i;
        } catch (IOException e) {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
