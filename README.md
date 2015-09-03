# septa-live-status-android-app

Instructions:
-There are 3 main activity classses
- MainActivity parses the information entered into the spinners and binds them togethor and uses the Next to Arrive link to get the list of trains in the route.
- Mapsactivity class displays the first 3 trains currently running.



Design & Implementation

- The main menu screen has 2 spinners and a GO button to select and finalize the source and desitnation.

- The view map button is used to display the train as markers obtained from the Train view URL and after getting access to the Google Map API.

- Google map API is present in the google_map_api.xml
- The .csv file with stations are parsed using a Java Code in Eclipse. THe link url is used to download and read the data in the csv. The output is printed as <item> station </item>

- The output is pasted in the Strings.xml file to get the values of the Spinners.
