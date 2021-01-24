# EQ_Works_workSample
Data Role

IMPORTANT NOTE: If you want to run the code "Requests.py", please use the files "DataSample.csv" and "POIList.csv" that I provided in 
                this repository. I made some minor modifications to the column names (removed whitespaces for example) 
                that are needed for my code to run. DO NOT USE THE ORIGINAL FILES PROVIDED, the code will raise errors.
1) Cleanup
  - Cleaned the sample dataset by removing requests with same [Latitude, Longitude, Timest]
  - In the POI List file, POI1 and POI3 have the same geoinfo, hence are the same POI,
    so I took the initiative of removing one of them (POI2)

2) Label
  - Using a BallTree which is a datastructure that provides fast performance on the generalized
    problem of K-nearest-neighbours, we can assign to each request, its nearest POI as well as
    the distance to this POI (See: "CleanedAndPOIAssigned")
    
3) Analysis
  - From found nearest POI, we can calculate the mean distance and standard deviation for each POI
    that we report in the final file "POI_calculations_done"
  - Then I tried to visualize the requests on a world map (See "POI_and_Requests.png"). To do this:
        1) Grouped requests by assigned POI
        2) Plotted each request on a world map using their Latitudes and Longitudes
            -> red = POI1 assigned
            -> yellow = POI3 assigned
            -> mauve = POI4 assigned
        3) Plotted each POI in black on the map
  - To calculate the radius for each POI, I simply found the farthest request assigned to it
    since the POI represents the center of the circle (also reported on : "POI_calculations_done")
  - The density was also calculated by totalling the number of requests assigned to a POI and dividing
    by the area (pi * r^2) of the circle of radius = r found for each POI (also reported on: "POI_calculations_done")
    
  NOTE: For a certain reason, I couldn't find a way to draw the circles of radius found on the map which is why I chose
        to assign to each request a different color so that visualization is easier. 
