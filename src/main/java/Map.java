import java.io.Serializable;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;


/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable {		//Boaz
	private int[][] _map;
	private boolean _cyclicFlag = true;

	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 *
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {			///Boaz
		init(w, h, v);
	}

	/**
	 * Constructs a square map (size*size).
	 *
	 * @param size
	 */
	public Map(int size) {						///Boaz
		this(size, size, 0);
	}

	/**
	 * Constructs a map from a given 2D array.
	 *
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

	@Override				//ishay
	public void init(int w, int h, int v) {
		_map = new int[w][h];                      //Construct a  integers array of 2D w*h.

		for (int i = 0; i < w; i++) {          		 //a loop that does the value of all the entries in the array is "v"
			for (int j = 0; j < h; j++) {
				_map[i][j] = v;
			}
		}
	}



	@Override								//ishay
	public void init(int[][] arr) {                 //Constructs deep copy.
		if (arr == null) {                                              	//pass and checks -  not null, not empty' not rageed
			throw new RuntimeException("The array is null");
		}
		if (arr.length == 0 || arr[0].length == 0) {
			throw new RuntimeException("The array is empty");
		}
		int num = arr[0].length;

		for (int i = 0; i < arr.length; i++) {
			if (arr[i].length != num) {
				throw new RuntimeException("The array is regged");
			}
		}

		_map = new int[arr.length][num];                                              //Creating a deep copy

		for (int i = 0; i < _map.length; i++)
			for (int j = 0; j < _map[0].length; j++) {
				_map[i][j] = arr[i][j];
			}
	}



	@Override					///ishay
	public int[][] getMap() {                         // Computes a deep copy of the underline 2D matrix.
		int[][] ans = new int[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.getWidth(); i++)                   //  Putting the values of the matrix in the array
			for (int j = 0; j < this.getHeight(); j++) {
				ans[i][j] = _map[i][j];
			}

		return ans;
	}

	@Override				///Boaz
	public int getWidth() {
		return _map.length;
	}

	@Override				///Boaz
	public int getHeight() {
		return _map[0].length;
	}

	@Override				///Boaz
	public int getPixel(int x, int y) {
		return _map[x][y];
	}

	@Override				///Boaz
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(), p.getY());
	}

	@Override			///Boaz
	public void setPixel(int x, int y, int v) {
		_map[x][y] = v;
	}

	@Override				//Boaz
	public void setPixel(Pixel2D p, int v) {
		setPixel(p.getX(), p.getY(), v);
	}


	@Override				//ishay
	/**
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {

		int ans = 0;                    // the number of "filled" pixels.

		Pixel2D start = new Index2D(xy);
		if (!this.isInside(start)) {            //if the starting point is outside
			throw new RuntimeException("The start point is outside");
		}

		if (this.getPixel(start) == new_v) {            //If the starting point is already in the new color
			return 0;
		}

		ArrayDeque<Pixel2D> list = new ArrayDeque<>();
						//Creating  a list that will store all the points that we will check their neighbors
		list.add(start);


		int color = this.getPixel(start);  		                        //Get the original color of the start pixel
		this.setPixel(start, new_v);                                    //Set the new color to the start pixel


		while (!list.isEmpty()) {                        // As long as the list of points we want to check is not empty
			start = list.removeFirst();                        //Selecting a point from the list, putting it on start and removing it

			ans ++;                                             //Add one following the pixel where fill was performed



			Pixel2D left = new Index2D(start.getX() - 1, start.getY());         //Defining the neighbors of the chosen point
			Pixel2D right = new Index2D(start.getX() + 1, start.getY());
			Pixel2D down = new Index2D(start.getX(), start.getY() - 1);
			Pixel2D up = new Index2D(start.getX(), start.getY() + 1);
			if (isCyclic()) {                                                          //Defining the neighboring points at the edges of the map if the map is cyclic

				if (start.getX() == 0) {
					left = new Index2D(getWidth() - 1, start.getY());
				}
				if (start.getX() == this.getWidth() - 1) {
					right = new Index2D(0, start.getY());
				}
				if (start.getY() == 0) {
					down = new Index2D(start.getX(), getHeight() - 1);
				}
				if (start.getY() == getHeight() - 1) {
					up = new Index2D(start.getX(), 0);
				}

				 		   //Apply the new color to the neighboring points inside the map that have the original color and add them to the lis
				if (isInside(left)) {
					if (this.getPixel(left) == color) {
						this.setPixel(left, new_v);
						list.add(left);

					}
				}

				if (isInside(right)) {
					if (this.getPixel(right) == color) {
						this.setPixel(right, new_v);
						list.add(right);

					}
				}

				if (isInside(up)) {
					if (this.getPixel(up) == color) {
						this.setPixel(up, new_v);
						list.add(up);

					}
				}

				if (isInside(down)) {
					if (this.getPixel(down) == color) {
						this.setPixel(down, new_v);
						list.add(down);

					}
				}

			} else {                             //If the map is not Cyclic - put the new color to the neighboring points inside the map that have the original color and add them to the list


				if (isInside(left)) {
					if (this.getPixel(left) == color) {
						this.setPixel(left, new_v);
						list.add(left);

					}
				}

				if (isInside(right)) {
					if (this.getPixel(right) == color) {
						this.setPixel(right, new_v);
						list.add(right);

					}
				}

				if (isInside(up)) {
					if (this.getPixel(up) == color) {
						this.setPixel(up, new_v);
						list.add(up);

					}
				}

				if (isInside(down)) {
					if (this.getPixel(down) == color) {
						this.setPixel(down, new_v);
						list.add(down);

					}
				}

			}

		}
		return ans;                                      //return the number of "filled" pixels.
	}

	/**
	 * Computes the distance of the shortest path (minimal number of consecutive neighbors) from p1 to p2.
	 * Notes: the distance is using computing the shortest path and returns its length-1, as the distance fro  a point
	 * to itself is 0, while the path contains a single point.
	 */
	public int shortestPathDist(Pixel2D p1, Pixel2D p2, int obsColor) {			//ishay

		if (!isInside(p1) || !isInside(p2)) { //if one of the points outside the map
			throw new RuntimeException("One or two points off the map");
		}
		if (this.getPixel(p1) == obsColor || this.getPixel(p2) == obsColor) { //if one of the points are on obsColor
			throw new RuntimeException("One or two points is obs color");
		}

		Map2D allDistance = allDistance(p1, obsColor); 				//Creating a distance map from point 1
		if (allDistance.getPixel(p2) == -1) {        //if we couldnt reach p2
			throw new RuntimeException("There is no path between the two points");
		}

		return allDistance.getPixel(p2);

	}

	@Override				//ishay
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
		Pixel2D[] ans = null;  // the result.
		if (!isInside(p1) || !isInside(p2)) { //if one of the points outside the map
			throw new RuntimeException("One or two points off the map");
		}
		if (this.getPixel(p1) == obsColor || this.getPixel(p2) == obsColor) { //if one of the points are on obsColor
			return null;
		}
		if (p1.equals(p2)) {			//If this is the same point
			ans = new Pixel2D[]{p1};
			return ans;
		}
		Map2D allDistance = allDistance(p1, obsColor); 				//Creating a distance map from point 1
		if (allDistance.getPixel(p2) == -1) {        //if we couldnt reach p2
			return null;
		}
		ans = new Pixel2D[allDistance.getPixel(p2) + 1];
							//Creating a new array with the length of the distance between p1 and p2, plus
		ans[0] = new Index2D(p2);		//The first place in this array is the index of p2
		int pointer = 1;				//A variable that assigns the place in the array to add the next pixel

		Pixel2D start = new Index2D(p2);

		while (allDistance.getPixel(start) != 0) {			//Loop until we have reached p1 - distance 0
			int num = allDistance.getPixel(start);			//A variable that receives the distance of the chosen point from p1

			Pixel2D left = new Index2D(start.getX() - 1, start.getY());		 //Defining the neighbors of the chosen point
			Pixel2D right = new Index2D(start.getX() + 1, start.getY());
			Pixel2D down = new Index2D(start.getX(), start.getY() - 1);
			Pixel2D up = new Index2D(start.getX(), start.getY() + 1);

			if (isCyclic()) {						////Defining the neighboring points at the edges of the map if the map is cyclic


				if (start.getX() == 0) {
					left = new Index2D(getWidth() - 1, start.getY());
				}
				if (start.getX() == this.getWidth() - 1) {
					right = new Index2D(0, start.getY());
				}
				if (start.getY() == 0) {
					down = new Index2D(start.getX(), getHeight() - 1);
				}
				if (start.getY() == getHeight() - 1) {
					up = new Index2D(start.getX(), 0);
				}



				if (isInside(left)) {
					if (allDistance.getPixel(left) == num - 1) {		//If the neighbor is the previous point on the path
						ans[pointer] = new Index2D(left);				//Adding it to the array of the road points
						pointer++;
						start = left;							// check its neighbors
						continue;
					}
				}

				if (isInside(right)) {
					if (allDistance.getPixel(right) == num - 1) {
						ans[pointer] = new Index2D(right);
						pointer++;
						start = right;
						continue;
					}
				}
				if (isInside(up)) {
					if (allDistance.getPixel(up) == num - 1) {
						ans[pointer] = new Index2D(up);
						pointer++;
						start = up;
						continue;
					}
				}
				if (isInside(down)) {
					if (allDistance.getPixel(down) == num - 1) {
						ans[pointer] = new Index2D(down);
						pointer++;
						start = down;

					}
				}

			} else {									//In case the map is not circular - the same process

				if (isInside(left)) {
					if (allDistance.getPixel(left) == num - 1) {
						ans[pointer] = new Index2D(left);
						pointer++;
						start = left;
						continue;
					}
				}

				if (isInside(right)) {
					if (allDistance.getPixel(right) == num - 1) {
						ans[pointer] = new Index2D(right);
						pointer++;
						start = right;
						continue;
					}
				}
				if (isInside(up)) {
					if (allDistance.getPixel(up) == num - 1) {
						ans[pointer] = new Index2D(up);
						pointer++;
						start = up;
						continue;
					}
				}
				if (isInside(down)) {
					if (allDistance.getPixel(down) == num - 1) {
						ans[pointer] = new Index2D(down);
						pointer++;
						start = down;

					}
				}
			}

		}

        for (int left = 0, right = ans.length - 1; left < right; left++, right--) {
            Pixel2D tmp = ans[left]; ans[left] = ans[right]; ans[right] = tmp;
        }
        return ans;

	}

    @Override
    public Pixel2D[] shortestPath(Pixel2D[] points, int obsColor) {
        if (points == null || points.length < 2) return null;
        for (Pixel2D point : points) {
            if (point == null || !isInside(point) || getPixel(point) == obsColor) return null;
        }
        Map2D reachable = allDistance(points[0], obsColor);
        for (Pixel2D point : points) if (reachable.getPixel(point) < 0) return null;

        Pixel2D[] closest = findClosestPoints(points, obsColor);
        ArrayList<Pixel2D> remaining = new ArrayList<>(Arrays.asList(points));
        ArrayList<Pixel2D> route = new ArrayList<>();
        Collections.addAll(route, shortestPath(closest[0], closest[1], obsColor));
        remaining.remove(closest[0]);
        remaining.remove(closest[1]);
        Pixel2D current = closest[1];
        while (!remaining.isEmpty()) {
            Pixel2D next = findClosestPoint(current, remaining, obsColor);
            Pixel2D[] leg = shortestPath(current, next, obsColor);
            // Append the leg without duplicating its shared starting coordinate.
            for (int i = 1; i < leg.length; i++) route.add(leg[i]);
            remaining.remove(next);
            current = next;
        }
        return route.toArray(new Pixel2D[0]);
    }

	@Override			//Boaz
	public boolean isInside(Pixel2D p) {
		return isInside(p.getX(), p.getY());
	}

	@Override			//Boaz
	public boolean isCyclic() {
		return _cyclicFlag;
	}

	@Override			//Boaz
	public void setCyclic(boolean cy) {
		_cyclicFlag = cy;
	}

	private boolean isInside(int x, int y) {			//Boaz
		return x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight();
	}

	@Override			//ishay
	public Map2D allDistance(Pixel2D start, int obsColor) {

		Map2D ans = null;                                //Creating a new map

		if (!this.isInside(start)) {            //if the starting point is outside
			throw new RuntimeException("The start point is outside");
		}

		if (this.getPixel(start) == obsColor) {            //if the starting point is an obsColor
			ans = new Map(getWidth(), getHeight(), -1);     //Creating a new map and assigning a value to all the pixels 1-
			return ans;
		}

		ArrayDeque<Pixel2D> list = new ArrayDeque<>();
		////Creating  a list that will store all the points that we will check their neighbors

		list.add(start);

		ans = new Map(getWidth(), getHeight(), 0);               //Creating a new map and assigning a value to all the pixels 1-

		for (int i = 0; i < this.getWidth(); i++) {					//update the map to -1 for obs and -2 for optional pass
			for (int j = 0; j < this.getHeight(); j++) {
				if (this.getPixel(i, j) != obsColor) {
					ans.setPixel(i, j, -2);
				} else {
					ans.setPixel(i, j, -1);
				}
			}
		}

		ans.setPixel(start, 0);			//put the value 0 for "start"

		while (!list.isEmpty()) {			 // As long as the list of points we want to check is not empty
			start = list.removeFirst();			////Selecting a pixel from the list, putting it on start and removing it

			int num = ans.getPixel(start);		//Create an int character with which we will count the distance

			Pixel2D left = new Index2D(start.getX() - 1, start.getY());		//Defining the neighbors of the chosen point
			Pixel2D right = new Index2D(start.getX() + 1, start.getY());
			Pixel2D down = new Index2D(start.getX(), start.getY() - 1);
			Pixel2D up = new Index2D(start.getX(), start.getY() + 1);

			if (isCyclic()) {					//Defining the neighboring points at the edges of the map if the map is cyclic


				if (start.getX() == 0) {
					left = new Index2D(getWidth() - 1, start.getY());
				}
				if (start.getX() == this.getWidth() - 1) {
					right = new Index2D(0, start.getY());
				}
				if (start.getY() == 0) {
					down = new Index2D(start.getX(), getHeight() - 1);
				}
				if (start.getY() == getHeight() - 1) {
					up = new Index2D(start.getX(), 0);
				}

				if (isInside(left)) {
					if (ans.getPixel(left) == -2) {			//If the neighboring point is a violation,
						ans.setPixel(left, num + 1);		// put the distance value of the selected point plus 1
						list.add(left);			//add it to the list and
					}
				}

				if (isInside(right)) {
					if (ans.getPixel(right) == -2) {
						ans.setPixel(right, num + 1);
						list.add(right);
					}
				}

				if (isInside(up)) {
					if (ans.getPixel(up) == -2) {
						ans.setPixel(up, num + 1);
						list.add(up);
					}
				}

				if (isInside(down)) {
					if (ans.getPixel(down) == -2) {
						ans.setPixel(down, num + 1);
						list.add(down);
					}
				}

			} else {			////If the map is not Cyclic

				if (isInside(left)) {
					if (ans.getPixel(left) == -2) {
						ans.setPixel(left, num + 1);
						list.add(left);
					}
				}

				if (isInside(right)) {
					if (ans.getPixel(right) == -2) {
						ans.setPixel(right, num + 1);
						list.add(right);
					}
				}

				if (isInside(up)) {
					if (ans.getPixel(up) == -2) {
						ans.setPixel(up, num + 1);
						list.add(up);
					}
				}

				if (isInside(down)) {
					if (ans.getPixel(down) == -2) {
						ans.setPixel(down, num + 1);
						list.add(down);
					}
				}
			}
		}
		for (int i = 0; i < this.getWidth(); i++)            //Assign a value of -1 to all points that are passed, but cannot be reached from the starting point
			for (int j = 0; j < getHeight(); j++) {
				if (ans.getPixel(i, j) == -2) {
					ans.setPixel(i, j, -1);
				}
			}


		return ans;					//Returning the map with the distances
	}


	@Override					//ishay -  computes the number of Connected Components in the map.
	public int numberOfConnectedComponents(int obsColor) {
		int ans = 0;
		Map ans2 = new Map(getMap());
		for (int i = 0; i < this.getWidth(); i++) {					//update the map to -1 for obs and -2 for optional pass
			for (int j = 0; j < this.getHeight(); j++) {
				if (this.getPixel(i, j) != obsColor) {
					ans2.setPixel(i, j, -2);
				} else {
					ans2.setPixel(i, j, -1);
				}
			}
		}
		for (int i = 0; i < this.getWidth(); i++) {			//Loop over all the pixels in the map
			for (int j = 0; j < this.getHeight(); j++) {
				Pixel2D start = new Index2D(i, j);
				ArrayDeque<Pixel2D> list = new ArrayDeque<>();

				if (ans2.getPixel(start) == -2) {

					list.add(start);			// If the point is not an obstacle, add it to the list
					ans2.setPixel(start, ans+1);	//Giving a new value to the pixel

					while (!list.isEmpty()) {		//Loop as long as there are pixels in the list
						start = list.removeFirst();		////Selecting a point from the list, putting it on start and removing it

						Pixel2D left = new Index2D(start.getX() - 1, start.getY());		//Defining the neighbors of the chosen point
						Pixel2D right = new Index2D(start.getX() + 1, start.getY());
						Pixel2D down = new Index2D(start.getX(), start.getY() + 1);
						Pixel2D up = new Index2D(start.getX(), start.getY() - 1);

						if (isCyclic()) {						//Defining the neighboring points at the edges of the map if the map is cyclic

							if (start.getX() == 0) {
								left = new Index2D(getWidth() - 1, start.getY());
							}
							if (start.getX() == this.getWidth() - 1) {
								right = new Index2D(0, start.getY());
							}
							if (start.getY() == 0) {
								up = new Index2D(start.getX(), getHeight() - 1);
							}
							if (start.getY() == getHeight() - 1) {
								down = new Index2D(start.getX(), 0);
							}

							if (isInside(left)) {
								if (ans2.getPixel(left) == -2) {		//If the pixel is not an obstacle
									list.add(left);
									ans2.setPixel(left, ans+1);
								}
							}

							if (isInside(right)) {
								if (ans2.getPixel(right) == -2) {
									list.add(right);
									ans2.setPixel(right, ans+1);
								}
							}

							if (isInside(up)) {
								if (ans2.getPixel(up) == -2) {
									list.add(up);
									ans2.setPixel(up, ans+1);
								}
							}

							if (isInside(down)) {
								if (ans2.getPixel(down) == -2) {
									list.add(down);
									ans2.setPixel(down, ans+1);
								}
							}

						} else {								  //If the map is not Cyclic

							if (isInside(left)) {
								if (ans2.getPixel(left) == -2) {
									list.add(left);
									ans2.setPixel(left, ans +1);
								}
							}

							if (isInside(right)) {
								if (ans2.getPixel(right) == -2) {
									list.add(right);
									ans2.setPixel(right, ans +1);
								}
							}

							if (isInside(up)) {
								if (ans2.getPixel(up) == -2) {
									list.add(up);
									ans2.setPixel(up, ans +1);
								}
							}

							if (isInside(down)) {
								if (ans2.getPixel(down) == -2) {
									list.add(down);
									ans2.setPixel(down, ans +1);
								}
							}
						}
					}
					ans++;			//Added one because the adjacent points ran out
				}

			}

		}
		return ans;
	}



						//ishay -  Showing the map of numberOfConnectedComponents
	public Map2D numberOfConnectedComponentsMap(int obsColor) {
		int ans = 0;
		Map ans2 = new Map(getMap());
		for (int i = 0; i < this.getWidth(); i++) {					//update the map to -1 for obs and -2 for optional pass
			for (int j = 0; j < this.getHeight(); j++) {
				if (this.getPixel(i, j) != obsColor) {
					ans2.setPixel(i, j, -2);
				} else {
					ans2.setPixel(i, j, -1);
				}
			}
		}
		for (int i = 0; i < this.getWidth(); i++) {			//Loop over all the pixels in the map
			for (int j = 0; j < this.getHeight(); j++) {
				Pixel2D start = new Index2D(i, j);
				ArrayDeque<Pixel2D> list = new ArrayDeque<>();

				if (ans2.getPixel(start) == -2) {

					list.add(start);			// If the point is not an obstacle, add it to the list
					ans2.setPixel(start, ans+1);	//Giving a new value to the pixel

					while (!list.isEmpty()) {		//Loop as long as there are pixels in the list
						start = list.removeFirst();		////Selecting a point from the list, putting it on start and removing it

						Pixel2D left = new Index2D(start.getX() - 1, start.getY());		//Defining the neighbors of the chosen point
						Pixel2D right = new Index2D(start.getX() + 1, start.getY());
						Pixel2D down = new Index2D(start.getX(), start.getY() + 1);
						Pixel2D up = new Index2D(start.getX(), start.getY() - 1);

						if (isCyclic()) {						//Defining the neighboring points at the edges of the map if the map is cyclic

							if (start.getX() == 0) {
								left = new Index2D(getWidth() - 1, start.getY());
							}
							if (start.getX() == this.getWidth() - 1) {
								right = new Index2D(0, start.getY());
							}
							if (start.getY() == 0) {
								up = new Index2D(start.getX(), getHeight() - 1);
							}
							if (start.getY() == getHeight() - 1) {
								down = new Index2D(start.getX(), 0);
							}

							if (isInside(left)) {
								if (ans2.getPixel(left) == -2) {		//If the pixel is not an obstacle
									list.add(left);
									ans2.setPixel(left, ans+1);
								}
							}

							if (isInside(right)) {
								if (ans2.getPixel(right) == -2) {
									list.add(right);
									ans2.setPixel(right, ans+1);
								}
							}

							if (isInside(up)) {
								if (ans2.getPixel(up) == -2) {
									list.add(up);
									ans2.setPixel(up, ans+1);
								}
							}

							if (isInside(down)) {
								if (ans2.getPixel(down) == -2) {
									list.add(down);
									ans2.setPixel(down, ans+1);
								}
							}

						} else {								  //If the map is not Cyclic

							if (isInside(left)) {
								if (ans2.getPixel(left) == -2) {
									list.add(left);
									ans2.setPixel(left, ans +1);
								}
							}

							if (isInside(right)) {
								if (ans2.getPixel(right) == -2) {
									list.add(right);
									ans2.setPixel(right, ans +1);
								}
							}

							if (isInside(up)) {
								if (ans2.getPixel(up) == -2) {
									list.add(up);
									ans2.setPixel(up, ans +1);
								}
							}

							if (isInside(down)) {
								if (ans2.getPixel(down) == -2) {
									list.add(down);
									ans2.setPixel(down, ans +1);
								}
							}
						}
					}
					ans++;			//Added one because the adjacent points ran out
				}

			}

		}
		return ans2;
	}

	@Override			//ishay
	public boolean equals(Object ob) {
		boolean ans = false;
		if (ob == null) {
			return false;
		}
		if (getClass() != ob.getClass()) {
			return false;				//The object received as an argument is not of type Map2
		}

		Map2D temp = (Map) ob;
		if (this.isCyclic() != temp.isCyclic()) {
			return false;
		}
		if (temp.getHeight() != this.getHeight() || temp.getWidth() != this.getWidth()) {		//The length or width are not equal
			return false;
		}
		for (int i = 0; i < this.getWidth(); i++) {					//The value of the pixels are not equal
			for (int j = 0; j < this.getHeight(); j++) {
				if (this.getPixel(i, j) != temp.getPixel(i, j)) {
					return false;
				}
			}
		}
		ans = true;
		return ans;
	}

	//ishay - auxiliary function - Finding pixels between which the distance is the shortest from an array of pixels
	public Pixel2D[] findClosestPoints(Pixel2D[] points, int obsColor) {	//


		int minDistance = Integer.MAX_VALUE;
		Pixel2D[] closestPair = new Pixel2D[2];

		for (int i = 0; i < points.length; i++) {
			for (int j = i + 1; j < points.length; j++) {			//Loop over all the pixels in the array and run shortestPathDist between every two pixels
				int distance = shortestPathDist(points[i], points[j], obsColor);
				if (distance != -1 && distance < minDistance) {
					minDistance = distance;
					closestPair[0] = points[i];
					closestPair[1] = points[j];
				}
			}
		}

		return closestPair;
	}
	// ishay - auxiliary function - Finding the closest pixel in ArrayList to a given pixel
	public Pixel2D findClosestPoint(Pixel2D point, ArrayList<Pixel2D> pointsList, int obsColor) {


		if (pointsList == null || pointsList.isEmpty() || point == null) {
			return null;
		}
		Pixel2D closestPoint = null;
		int minDistance = Integer.MAX_VALUE;
		for (Pixel2D currentPoint : pointsList) {			//Loop over all elements in pointsList to find the closest pixel
			int distance = shortestPathDist(point, currentPoint, obsColor);
			if (distance != -1 && distance < minDistance) {
				minDistance = distance;
				closestPoint = currentPoint;
			}
		}

		return closestPoint;
	}



	// ishay - auxiliary function - A function to convert an array to an ArrayList

	public static <T> void appendArrayToArrayList(T[] array, ArrayList<T> arrayList) {
		Collections.addAll(arrayList, array);
	}


}


