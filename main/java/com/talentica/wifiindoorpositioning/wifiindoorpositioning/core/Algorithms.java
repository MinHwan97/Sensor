/*
* AnyPlace: A free and open Indoor Navigation Service with superb accuracy!
*
* Anyplace is a first-of-a-kind indoor information service offering GPS-less
* localization, navigation and search inside buildings using ordinary smartphones.
*
* Authors: C. Laoudias, G.Larkou, G. Constantinou, M. Constantinides, S. Nicolaou
* 
* Supervisor: Demetrios Zeinalipour-Yazti
*
* URL: http://anyplace.cs.ucy.ac.cy
* Contact: anyplace@cs.ucy.ac.cy
*
* Copyright (c) 2015, Data Management Systems Lab (DMSL), University of Cyprus.
* All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of
* this software and associated documentation files (the "Software"), to deal in the
* Software without restriction, including without limitation the rights to use, copy,
* modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject to the
* following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*/

package com.talentica.wifiindoorpositioning.wifiindoorpositioning.core;

import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.AccessPoint;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.IndoorProject;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.LocDistance;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.LocationWithNearbyPlaces;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.ReferencePoint;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.WifiDataNetwork;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.utils.AppContants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmList;

public class Algorithms {

	final static String K = "4";

	/**
	 * 
	 * @param latestScanList
	 *            the current scan list of APs
	 * @param proj
	 *            the project details from db for current area
	 * 
	 * @param algorithm_choice
	 *            choice of several algorithms
	 * 
	 * @return the location of user
	 */
	public static LocationWithNearbyPlaces processingAlgorithms(List<WifiDataNetwork> latestScanList, IndoorProject proj, int algorithm_choice) {

		int i, j;
		RealmList<AccessPoint> aps = proj.getAps();
		ArrayList<Float> observedRSSValues = new ArrayList<Float>();
		WifiDataNetwork temp_LR;
		int notFoundCounter = 0;
		// Read parameter of algorithm


		// Check which mac addresses of radio map, we are currently listening.
		for (i = 0; i < aps.size(); ++i) {
			for (j = 0; j < latestScanList.size(); ++j) {
				temp_LR = latestScanList.get(j);
				// MAC Address Matched
				if (aps.get(i).getMac_address().compareTo(temp_LR.getBssid()) == 0) {
					observedRSSValues.add(Float.valueOf(temp_LR.getLevel()).floatValue());
					break;
				}
			}
			// A MAC Address is missing so we place a small value, NaN value
			if (j == latestScanList.size()) {
				observedRSSValues.add(AppContants.NaN);
				++notFoundCounter;
			}
		}

		if (notFoundCounter == aps.size())
			return null;

		// Read parameter of algorithm
		String parameter = K;

		if (parameter == null)
			return null;

		return KNN_WKNN_Algorithm(proj, observedRSSValues, parameter, true);


	}

	/**
	 * Calculates user location based on Weighted/Not Weighted K Nearest
	 * Neighbor (KNN) Algorithm
	 * 
	 * @param proj
	 *            the project details from db for current area
	 * 
	 * @param observedRSSValues
	 *            RSS values currently observed
	 * @param parameter
	 * 
	 * @param isWeighted
	 *            To be weighted or not
	 * 
	 * @return The estimated user location
	 */
	private static LocationWithNearbyPlaces KNN_WKNN_Algorithm(IndoorProject proj, ArrayList<Float> observedRSSValues,
											 String parameter, boolean isWeighted) {

		RealmList<AccessPoint> rssValues;
		float curResult = 0;
		ArrayList<LocDistance> locDistanceResultsList = new ArrayList<LocDistance>();
		String myLocation = null;
		int K;

		try {
			K = Integer.parseInt(parameter);
		} catch (Exception e) {
			return null;
		}

		// Construct a list with locations-distances pairs for currently
		// observed RSS values
		for (ReferencePoint referencePoint : proj.getRps()) {
			rssValues = referencePoint.getReadings();
			curResult = calculateEuclideanDistance(rssValues, observedRSSValues);

			if (curResult == Float.NEGATIVE_INFINITY)
				return null;

			locDistanceResultsList.add(0, new LocDistance(curResult, referencePoint.getLocId(), referencePoint.getName()));
		}

		// Sort locations-distances pairs based on minimum distances
		Collections.sort(locDistanceResultsList, new Comparator<LocDistance>() {
			public int compare(LocDistance gd1, LocDistance gd2) {
				return (gd1.getDistance() > gd2.getDistance() ? 1 : (gd1.getDistance() == gd2.getDistance() ? 0 : -1));
			}
		});

		if (!isWeighted) {
			myLocation = calculateAverageKDistanceLocations(locDistanceResultsList, K);
		} else {
			myLocation = calculateWeightedAverageKDistanceLocations(locDistanceResultsList, K);
		}
		LocationWithNearbyPlaces places = new LocationWithNearbyPlaces(myLocation, locDistanceResultsList);
		return places;

	}


	/**
	 * Calculates the Euclidean distance between the currently observed RSS
	 * values and the RSS values for a specific location.
	 * 
	 * @param l1
	 *            RSS values of a location in stored in AP obj of locations
	 * @param l2
	 *            RSS values currently observed
	 * 
	 * @return The Euclidean distance, or MIN_VALUE for error
	 */
	private static float calculateEuclideanDistance(RealmList<AccessPoint> l1, ArrayList<Float> l2) {

		float finalResult = 0;
		float v1;
		float v2;
		float temp;

		for (int i = 0; i < l1.size(); ++i) {

			try {
				l1.get(i).getMeanRss();
				v1 = (float) l1.get(i).getMeanRss();
				v2 = l2.get(i);
			} catch (Exception e) {
				return Float.NEGATIVE_INFINITY;
			}

			// do the procedure
			temp = v1 - v2;
			temp *= temp;

			// do the procedure
			finalResult += temp;
		}
		return ((float) Math.sqrt(finalResult));
	}



	/**
	 * Calculates the Average of the K locations that have the shortest
	 * distances D
	 * 
	 * @param LocDistance_Results_List
	 *            Locations-Distances pairs sorted by distance
	 * @param K
	 *            The number of locations used
	 * @return The estimated user location, or null for error
	 */
	private static String calculateAverageKDistanceLocations(ArrayList<LocDistance> LocDistance_Results_List, int K) {
		float sumX = 0.0f;
		float sumY = 0.0f;

		String[] LocationArray = new String[2];
		float x, y;

		int K_Min = K < LocDistance_Results_List.size() ? K : LocDistance_Results_List.size();

		// Calculate the sum of X and Y
		for (int i = 0; i < K_Min; ++i) {
			LocationArray = LocDistance_Results_List.get(i).getLocation().split(" ");

			try {
				x = Float.valueOf(LocationArray[0].trim()).floatValue();
				y = Float.valueOf(LocationArray[1].trim()).floatValue();
			} catch (Exception e) {
				return null;
			}

			sumX += x;
			sumY += y;
		}

		// Calculate the average
		sumX /= K_Min;
		sumY /= K_Min;

		return sumX + " " + sumY;
	}

	/**
	 * Calculates the Weighted Average of the K locations that have the shortest
	 * distances D
	 * 
	 * @param LocDistance_Results_List
	 *            Locations-Distances pairs sorted by distance
	 * @param K
	 *            The number of locations used
	 * @return The estimated user location, or null for error
	 */
	private static String calculateWeightedAverageKDistanceLocations(ArrayList<LocDistance> LocDistance_Results_List, int K) {
		double LocationWeight = 0.0f;
		double sumWeights = 0.0f;
		double WeightedSumX = 0.0f;
		double WeightedSumY = 0.0f;

		String[] LocationArray = new String[2];
		float x, y;

		int K_Min = K < LocDistance_Results_List.size() ? K : LocDistance_Results_List.size();

		// Calculate the weighted sum of X and Y
		for (int i = 0; i < K_Min; ++i) {
			if (LocDistance_Results_List.get(i).getDistance() != 0.0) {
				LocationWeight = 1 / LocDistance_Results_List.get(i).getDistance();
			} else {
				LocationWeight = 100;
			}
			LocationArray = LocDistance_Results_List.get(i).getLocation().split(" ");

			try {
				x = Float.valueOf(LocationArray[0].trim()).floatValue();
				y = Float.valueOf(LocationArray[1].trim()).floatValue();
			} catch (Exception e) {
				return null;
			}

			sumWeights += LocationWeight;
			WeightedSumX += LocationWeight * x;
			WeightedSumY += LocationWeight * y;

		}

		WeightedSumX /= sumWeights;
		WeightedSumY /= sumWeights;

		return WeightedSumX + " " + WeightedSumY;
	}


}
