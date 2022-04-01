
# Garden Manager


## About

**Garden Manager** is an application that allows you to manage the plant life of your garden/greenhouse.  
It can record the plant type, watering cycle, and life stage of your plants all in one place. It can also let you know 
what plants in your garden need water,
and also let you easily manage all the information of your plants right from your computer! This application can be used by anyone with a passion for gardening who wants to be more organized; 
whether that be beginner gardeners to professionals farmers. This project is of interest to me as I also share a passion with managing plants, but *unfortunately* forget to water or prune them on time. 
With this application, I could have a centralized space to record all the plants I am maintaining. 

## User Stories

- As a user, I want to be able to add and remove plants and plant beds to my greenhouse/garden
- As a user, I want to be able to inspect a plant to see information such as life stage, watering schedule, or plant type
- As a user, I want to be able to see statistics of my garden, such as total plants and number of plants that need water.
- As a user, I want to be able to view plants I need to water, and then be able to know where they are to go water them.
- As a user, I want to be able to save the current state of my entire garden, with all its plant beds and plants.
- As a user, I want to be able to load a save of my garden to go back to an older state. 
- As a user, I want to be able to save my garden when quitting the program and load it back on the next startup.


## Phase 4: Task 2

-- INITIALIZATION OF DATA ON STARTUP --
- Thu Mar 31 23:16:44 PDT 2022 
- Added plant 'Rose' to plant bed 'Bed 1'.
- Thu Mar 31 23:16:44 PDT 2022
- Added plant 'Carrot' to plant bed 'Bed 1'.
- Thu Mar 31 23:16:44 PDT 2022
- Added plant-bed 'Bed 1' to garden.
- Thu Mar 31 23:16:44 PDT 2022
- Added plant 'Day-lily' to plant bed 'Bed 2'.
- Thu Mar 31 23:16:44 PDT 2022
- Added plant-bed 'Bed 2' to garden.

-- MODIFYING X'S AND Y'S --

- Thu Mar 31 23:16:55 PDT 2022
- Removed plant-bed 'Bed 2' from garden.
- Thu Mar 31 23:17:04 PDT 2022
- Added plant-bed 'Fruits' to garden.
- Thu Mar 31 23:17:15 PDT 2022
- Added plant 'Apple' to plant bed Fruits.
- Thu Mar 31 23:17:25 PDT 2022
- Removed plant 'Carrot' from plant bed 'Bed 1'.
- Thu Mar 31 23:17:31 PDT 2022
- Watered plant 'Apple' in plant bed 'Fruits'.

## Phase 4: Task 3

If I could have done some refactoring, I would have attempted to reduce some redundancy by making an abstract class for
both the plant and plant-beds to extend as they have similar functionality. I could have used the
Composite Pattern from class, as my program is similar to files and folders (plants and plant-beds).
Also, as it stands my GardenApp class is a bit bloated, so I would try to dilute it into 
more classes to improve cohesion would also be a good choice.