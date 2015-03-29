package com.tcsorcs.trailsapp.managers;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

import com.tcsorcs.trailsapp.helpers.Segment;
import com.tcsorcs.trailsapp.helpers.DummyDatabaseHelper; //temporary until database helper is up

/* v1.0 - Full version
 *
 *  Currently FUNCTIONAL but UNTESTED - DummyDatabaseHelper used in place of actual database helper
 *    smarterPathFinder and related helpers not tested thoroughly
 *  DNE = does not exist
 *  Updated 3/28/2015
 */

public class DistanceManager {
	private Stack<String> path = new Stack<String>(); //path so far, saves the String names of the QR codes
	private double totalDistance = 0.0; //keeps track of the total distance of the path
	private long startTimeMillis = -1;

	public static DistanceManager getInstance() {
		return DistanceManager.instance;
	}

	public static DistanceManager instance = new DistanceManager();

	/*
	 * Handles QR input (complete, needs official method names, untested)
	 * given the name of a location QR code, builds path and tells display manager what to display
	 */
	public void processQRCodes(String codeName) {
		//I see a QR code - is either first or not first
		if (path.empty()){            //don't need to pathfind
			path.push(codeName); //add QRcode to path
			startTimeMillis = System.currentTimeMillis(); //start timer
		}
		else {//need to pathfind
			smarterPathFinder(codeName);
		}
		//debug System.out.println("DistanceManager.processQRCodes end");
	}


	/*
	 * Returns total distance walked 
	 * NOTE distance is calculated each time a distanceQR code is scanned
	 */
	public double getDistance() {
		return this.totalDistance;
	}

	/* 
	 * Returns total time on trail in seconds
	 */
	public double getTimeOnTrail() {
		long currentTimeMillis = System.currentTimeMillis();
		long totalTimeMillis = currentTimeMillis - startTimeMillis;
		int totalTimeSeconds = (int)(totalTimeMillis/1000);

		return totalTimeSeconds;
	}

	/* 
	 * Returns average pace since start in feet per minutes
	 */
	public double getPace() {
		double seconds = getTimeOnTrail();
		double minutes = seconds/60;
		double distance = getDistance();
		double pace = distance / minutes;
		pace = Math.round(pace*1000)/1000;

		return pace;
	}

	/*
	 * local class Node
	 * Contains information to help pick shortest path 
	 */
	class Node implements Comparable<Node> {
		String scanName; //QR code name
		Node parent; //parent node, or null
		double nodeDistance; //total distance from start of subpath
		Segment segment; //segment containing this scanName and previous scan name

		Node (String aScanName, Node aNode, double aDistance, Segment aSegment){
			scanName = aScanName;
			parent = aNode;
			nodeDistance = aDistance;
			segment = aSegment;
		}

		@Override
		public int compareTo(Node left) {
			return Double.compare(this.nodeDistance, left.nodeDistance);
		}
	}


	/*
	 * Finds the shortest path from the most recently scanned code (currentScan) to 
	 *   end of alreay travelled path
	 * Assumes:
	 *   -If points have been skipped, will not double back over the last segment in the path 
	 *   	(but may go through other segments already traveled on)
	 * Does: 
	 *   -Handles skipped points
	 * ToDo:
	 *   -Add check side of road before pathfinding
	 */

	private void smarterPathFinder(String currentScan){
		// Check if points are adjacent 
		String lastScan; //most recent point from global path stack
		ArrayList<Segment> attachedSegments; //list of segments, returned from DB //DNE
		Segment currentSegment = null; //current segment we're working with
		boolean pointsAdjacent = false; //if two points are on the same segment

		lastScan = path.peek();//most recent point from path

		attachedSegments = DummyDatabaseHelper.getInstance().getSegmentsWithPoint(currentScan); //DNE //Query DB //segments with currentScan //if nothing found MUST return an empty array list, not null

		//if something goes wrong and attachedSegments is null, skip gracefully (redundant if getSegmentsWithPoint succeeds)
		if (attachedSegments == null){
			System.err.println("Segment list not created. Pathfinding cannot be done. Returning to processQRCode");
			return;
		}

		//if getSegmentsWithPoint grabs nothing b/c error, don't continue because will break
		if (attachedSegments.size() < 1){
			System.err.println("Scanned point "+currentScan+" does not seem to be part of a segment. Pathfinding cannot be done. Returning to processQRCode");
			return;
		}

		//checks if last 2 points scanned are on the same segment
		for (int i = 0; i < attachedSegments.size() && !pointsAdjacent; i++){
			currentSegment = attachedSegments.get(i);
			pointsAdjacent = currentSegment.segmentHasPoints(lastScan, currentScan);
		}

		//we know our next segment, yay!
		if(pointsAdjacent){
			path.push(currentScan);
			totalDistance += currentSegment.getSegmentDistance();//if not initialized, will not be hit
			DisplayManager.getInstance().drawSegment(currentSegment);//display segment
			return;
		}

		// if points are not adjacent, pathfind

		//And magic happens - 100% chance this will fail first time it's run...
		//have currentScan(start with this), lastScan(end of the path we've already built)
		/*
		 * pathfinder will build unknown path starting with most recent scanned, last point added to
		 * pathfinder list will be last point on main path. We then remove last point from list
		 * and pop it onto path (adding distance as we go) so that last point left in pathfinder list
		 * is most recent point scanned, and now the end of the main path.
		 */
		Node currentNode; //first node we add
		TreeMap<Double, Node> subPath; //subpath we're building
		Node shortestNode; //stores the shortest node (list)
		//ArrayList<Segment> nextSegments;
		Segment subSegment;
		Node subNode;
		String parentScanName;//name of parent for database query
		Double tempDistance; //temp storage for distance

		currentNode = new Node(currentScan, null, 0.0, null);
		subPath = new TreeMap<Double, Node>();

		subPath.put(0.0, currentNode);//add first

		shortestNode = subPath.get(subPath.firstKey()); //get shortest node

		while(!shortestNode.scanName.equals(lastScan)) { //while the shortest scan is not the lastScan from path
			if (shortestNode.parent == null){
				parentScanName = null;
			} else {
				parentScanName = shortestNode.parent.scanName;
			}
			attachedSegments = DummyDatabaseHelper.getSegmentsWithPoint(shortestNode.scanName, parentScanName);//DNE //Tim


			while (!attachedSegments.isEmpty()) {
				subSegment = attachedSegments.remove(0); //removes and returns, decreasing list //(is this less optimal than using a for loop?)

				/*
				 * If there's further elimination we want to do (ex. don't add segments that cross the road)
				 *   add them here
				 */

				//DEBUG - if something goes wrong, it's probably here!
				tempDistance = (subSegment.getSegmentDistance() + shortestNode.nodeDistance);
				subNode = new Node(subSegment.getOtherPoint(shortestNode.scanName), shortestNode, tempDistance, subSegment);//Segment.secondPoint is the point found attached, not the point given to search for
				subPath.put(tempDistance, subNode);
			}

			//all connected points should be in nodes, so must remove original node from subPath
			subPath.pollFirstEntry(); //removes and returns first in set (shortest path here)

			shortestNode = subPath.get(subPath.firstKey()); //get shortest
		}//end while

		//we have our path
		addSubPathToPath(shortestNode);

		/*
		 * If we want to let the user change the path they took, add that code here.
		 */

		this.totalDistance += shortestNode.nodeDistance; //updates total distance
	}

	/* helper to smarterPathFinder - might fold into smarterPathFinder later
	 * given connectingNode.scanName should be lastScan
	 * This travels through the list of nodes in connectingNode until end - where end is currentScan
	 * - and adds to path
	 * 
	 * UNTESTED
	 */
	private void addSubPathToPath(Node shortestNode){
		totalDistance += shortestNode.nodeDistance; //adds subPath distance to total distance
		Node currentNode = shortestNode.parent;
		ArrayList<Segment> segmentsList = new ArrayList<Segment>(); //stores for DisplayManager

		while(currentNode != null){
			path.push(currentNode.scanName);
			currentNode = currentNode.parent;
			segmentsList.add(currentNode.segment);
		}

		//display stuff
		DisplayManager.getInstance().drawSegments(segmentsList);//DNE //Dave

	}
}