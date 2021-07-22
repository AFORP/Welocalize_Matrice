package fr.aforp.wilocalyse.algorithms;

import android.util.Log;

import java.util.*;

class GFG
{
	static int ROW = 2115 ;
	static int COL = 1020;

	static class Point
	{
		int x;
		int y;

		public Point(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		public String toString() {
			return "x :"+this.x+" y:"+this.y;
		}
	};

	// A Data Structure for queue used in BFS
	static class queueNode
	{
		Point pt;
		int dist;

		public queueNode(Point pt, int dist)
		{
			this.pt = pt;
			this.dist = dist;
		}
	};


	static boolean isValid(int row, int col)
	{

		return (row >= 0) && (row < ROW) &&
				(col >= 0) && (col < COL);
	}


	static int rowNum[] = {-1, 1, 0, 1,1,-1,-1,1};
	static int colNum[] = {0, 0, 1, -1,1,-1,1,-1};
	//static int rowNum[] = {-1, 0, 0, 1};
	//static int colNum[] = {0, -1, 1, 0};
	static Queue <Point> pathQ= new LinkedList<Point>();

	static int BFS(int mat[][], Point src, Point dest)
	{
		if (mat[src.x][src.y] != 0 || mat[dest.x][dest.y] != 0) {
			Log.i("path", "on tombe la");
			return -1;
		}

		boolean [][]visited = new boolean[ROW][COL];
		Point [][] previous= new  Point[ROW][COL];
		visited[src.x][src.y] = true;
		Queue<queueNode> q = new LinkedList<>();
		Queue<queueNode> qpath = new LinkedList<>();
		queueNode s = new queueNode(src, 0);
		q.add(s);

		while (!q.isEmpty())
		{
			queueNode curr = q.peek();
			Point pt = curr.pt;

			if (pt.x == dest.x && pt.y == dest.y){
				Point tmp=new Point( dest.x,dest.y);
				pathQ.clear();
				((LinkedList<Point>) pathQ).addFirst(new Point(tmp.x,tmp.y));
				while(previous[tmp.x][tmp.y] != null) {
					((LinkedList<Point>) pathQ).addFirst(new Point(previous[tmp.x][tmp.y].x,previous[tmp.x][tmp.y].y));
					tmp=previous[tmp.x][tmp.y];
				}

				return curr.dist;
			}

			q.remove();

			for (int i = 0; i < 8; i++)
			{
				int row = pt.x + rowNum[i];
				int col = pt.y + colNum[i];

				if (isValid(row, col) && (mat[row][col] == 1 ||mat[row][col] == 2) && !visited[row][col])
				{
					visited[row][col] = true;
					previous[row][col]= new Point(pt.x,pt.y);
					queueNode Adjcell = new queueNode(new Point(row, col), curr.dist + 1 );
					q.add(Adjcell);
				}
			}
		}

		return -1;
	}

}




