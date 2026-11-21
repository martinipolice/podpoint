Pod Point - Agriculture Data Management System
CMSC 22 CD-4L, individual project by Charles Gian L. Santos

Pod Point is a sales and inventory system for Cacao Culture (Davao City),
made with Java and JavaFX. It has a Manager and a Clerk.

HOW TO RUN (Eclipse)
1. File > Import > General > Existing Projects into Workspace, then pick this folder.
2. The project was made with JDK 26 and JavaFX 26.0.2. If the JavaFX jars
   show errors, open Project > Properties > Java Build Path > Libraries, remove
   the broken JavaFX jars and add your own JavaFX library or jars.
3. Run > Run Configurations > Java Application. Set the project to AgriDMS and the
   main class to app.Main. Under Arguments > VM arguments, type:
   --module-path "<your JavaFX lib folder>" --add-modules javafx.controls,javafx.fxml
4. Click Run. Run it from the project (Eclipse does this by default), so the
   data and resources folders are found.

ACCOUNTS
Manager: manager / 1234
Clerk:   clerk1 / 1111
Clerk:   clerk2 / 2222

FOLDERS
src        the code (app, model, ui, util, css)
resources  the logo images
data       the saved files (made by the program)

START FRESH
Close the app, then delete data/inventory.dat and data/transactions.dat.
The next start has the 10 products again, with 20 in stock each.

CONSOLE TESTS
The classes ending in Test in the app package are small tests without a window.
Run ProductTest first, because the other tests read the products it saves.