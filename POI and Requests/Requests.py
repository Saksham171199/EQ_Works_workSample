import math
import pandas as pd
from geopandas import GeoDataFrame, GeoSeries
from shapely.geometry import Point
from sklearn.neighbors import BallTree
import geopandas as gpd
import matplotlib.pyplot as plt

#Function needed to compute area of circle around POI
def area_circle(radius):
    return math.pi * radius * radius

def clean_and_label(filepath1, filepath2):

    #For both the datasample file and POI list file we remove rows that have the same Longitude, Latitude and TimeSt
    df = pd.read_csv(filepath1)
    df = df.drop_duplicates(subset=['Latitude', 'Longitude', 'TimeSt'], keep=False)
    POI = pd.read_csv(filepath2)
    POI = POI.drop_duplicates(subset=['Latitude', 'Longitude'], keep='first')

    #BallTree for fast generalized N-point problems
    #Will find nearest neighbour faster than usual knn-algorithm (here k=1)
    #Will find nearest POI for each point in the datasample as well as its distance to the POI
    tree = BallTree(POI[['Latitude', 'Longitude']].values, leaf_size=2)
    df['distance_nearest'], df['nearest_poi'] = tree.query(df[['Latitude', 'Longitude']].values, k=1)
    df['nearest_poi'] = df['nearest_poi'].replace([0, 1, 2],['POI1', 'POI3', 'POI4'])

    #From previous result we can simply group datapoints by POI assigned and computer
    #the average and std for the distance between each POI to its assigned requests
    interm = df.groupby('nearest_poi', as_index=False)['distance_nearest'].mean()
    POI['Average distance'] = interm['distance_nearest'].values
    interm = df.groupby('nearest_poi', as_index=False)['distance_nearest'].std()
    POI['Standard Deviation'] = interm['distance_nearest'].values

    return df, POI

def plot_map(df, POI):

    #split dataframe by assigned POI for further processing
    grouped = df.groupby('nearest_poi')
    poi1 = grouped.get_group("POI1")
    poi3 = grouped.get_group("POI3")
    poi4 = grouped.get_group("POI4")

    #Geometry makes it easier to visualize points on a map using Latitude and Longitude
    geometry1 = [Point(xy) for xy in zip(poi1['Longitude'], poi1['Latitude'])]
    geometry3 = [Point(xy) for xy in zip(poi3['Longitude'], poi3['Latitude'])]
    geometry4 = [Point(xy) for xy in zip(poi4['Longitude'], poi4['Latitude'])]
    geometry_poi = [Point(xy) for xy in zip(POI['Longitude'], POI['Latitude'])]

    #create GeoDataFrame to plot on map
    gdf_poi = GeoDataFrame(POI, geometry=geometry_poi)
    gdf1 = GeoDataFrame(poi1, geometry=geometry1)
    gdf3 = GeoDataFrame(poi3, geometry=geometry3)
    gdf4 = GeoDataFrame(poi4, geometry=geometry4)

    #load a world map from gpd dataset
    world = gpd.read_file(gpd.datasets.get_path('naturalearth_lowres'))
    ax = world.plot(figsize=(10, 6))

    #Plot different requests (red = POI1 assigned, yellow = POI3 assigned, mauve = POI4 assigned
    #                           black = POI)
    gdf1.plot(ax=ax, marker='o', color='r', markersize=15)
    gdf3.plot(ax=ax, marker='o', color='y', markersize=15)
    gdf4.plot(ax=ax, marker='o', color='m', markersize=15)
    gdf_poi.plot(ax=ax, marker='o', color='k', markersize=40)

    #Computing the radius is simply finding the farthest request assigned to a POI
    #since the POI is the center of the circle
    radius_poi1 = poi1['distance_nearest'].max()
    radius_poi3 = poi3['distance_nearest'].max()
    radius_poi4 = poi4['distance_nearest'].max()

    #Computing the density for each POI just counts the number of assigned request/POI
    #divided by the area which we can find from the radius we found previously
    density_poi1 = poi1.shape[0] / area_circle(radius_poi1)
    density_poi3 = poi3.shape[0] / area_circle(radius_poi3)
    density_poi4 = poi4.shape[0] / area_circle(radius_poi4)

    #Report previous calculations in the POI dataframe and export it to a readable csv file
    POI.insert(6, 'Radius', [radius_poi1, radius_poi3, radius_poi4], True)
    POI.insert(7, 'Density', [density_poi1, density_poi3, density_poi4], True)
    pd.set_option('display.max_columns', None)

    #Export map + csv to be read
    plt.savefig("POI_and_Requests")
    df.to_csv("CleanedAndPOIAssigned")
    POI.to_csv("POI_calculations_done")

def main(dataSampleFile, POI_list_file):
    df, POI = clean_and_label(dataSampleFile,
                          POI_list_file)
    plot_map(df, POI)

main("/Users/admin/PycharmProjects/ws-data-spark/data/DataSample.csv",
     "/Users/admin/PycharmProjects/ws-data-spark/data/POIList.csv")