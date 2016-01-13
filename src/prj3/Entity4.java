//Project 3, DV Simulator, 2015
package prj3;

public class Entity4 extends Entity
{    
	//Constructor of Entity Class. Initiates Packet communication
	public Entity4()
	{
		InitializeDistanceVectorTable();
		int[] arrToSend = GetMinArr();
		Packet pTo1 = new Packet(4,1,arrToSend);
		Packet pTo3 = new Packet(4,3,arrToSend);
		NetworkSimulator.toLayer2(pTo1);
		NetworkSimulator.toLayer2(pTo3);
		//Perform any necessary 
		//initialization in the constructor 
	}

	//Returns Minimum values in each row
	private int[] GetMinArr() {
		int[] arr = new int[5];
		arr[0] = 999;
		arr[1] = 999;
		arr[2] = 999;
		arr[3] = 999;
		arr[4] = 999;

		for(int n = 0; n < 5; n++){
			for(int i = 0; i < 5; i++){
				if(arr[n] > distanceTable[n][i])
					arr[n] = distanceTable[n][i];
			}
		}

		return arr;
	}

	//Sets the initial values of the node4
	private void InitializeDistanceVectorTable() {

		for(int i = 0; i<5;i++){
			for(int n = 0; n<5;n++){
				distanceTable[i][n] = 999;
			}
		}
		distanceTable[4][4] = 0;
		distanceTable[1][1] = NetworkSimulator.cost[4][1];
		distanceTable[3][3] = NetworkSimulator.cost[4][3];    
	}

	//Updates the changes made by the corresponding packet and notifies other nodes if it is necessary
	public void update(Packet p)
	{   
		int sourceNode = p.getSource();
		boolean send = ArrangeVectorTable(sourceNode,p);
		if(send){
			AnswerNode(sourceNode,3);
			AnswerNode(sourceNode,1);
		}
	}

	//Notifies the destination node by using the source node
	private void AnswerNode(int sourceNode, int destination) {
		int[] arr = GetMinArr();
		Packet packetToSend = new Packet(4,destination,arr);
		NetworkSimulator.toLayer2(packetToSend);
	}

	//Sets the new values (if there are) to the distance vector table
	private boolean ArrangeVectorTable(int sourceNode, Packet p) {
		int counter = 0;
		for(int n = 0; n < 5; n++){
			if(distanceTable[n][sourceNode] != p.getMincost(n) + NetworkSimulator.cost[4][sourceNode]){
				distanceTable[n][sourceNode] = p.getMincost(n) + NetworkSimulator.cost[4][sourceNode];
				counter++;
			}
		}
		if(counter == 0)
			return false;
		else
			return true;
	}

	public void linkCostChangeHandler(int whichLink, int newCost)
	{
	}

	// Print destination table
	public void printDT()
	{
		System.out.println();
		System.out.println("         via");
		System.out.println(" D4 |   1   3");
		System.out.println("----+--------");
		for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
		{
			if (i == 4)
			{
				continue;
			}

			System.out.print("   " + i + "|");
			for (int j = 1; j < NetworkSimulator.NUMENTITIES; j++)
			{

				if(j==2 || j==4)
					continue;

				if (distanceTable[i][j] < 10)
				{    
					System.out.print("   ");
				}
				else if (distanceTable[i][j] < 100)
				{
					System.out.print("  ");
				}
				else 
				{
					System.out.print(" ");
				}

				System.out.print(distanceTable[i][j]);
			}
			System.out.println();
		}
	}
}
