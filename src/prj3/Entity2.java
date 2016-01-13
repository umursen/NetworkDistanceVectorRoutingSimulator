//Project 3, DV Simulator, 2015
package prj3;

public class Entity2 extends Entity
{    
	//Constructor of Entity Class. Initiates Packet communication
	public Entity2()
	{
		InitializeDistanceVectorTable();
		int[] arrToSend = GetMinArr();
		Packet pTo0 = new Packet(2,0,arrToSend);
		Packet pTo3 = new Packet(2,3,arrToSend);
		NetworkSimulator.toLayer2(pTo0);
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

	//Sets the initial values of the node2
	private void InitializeDistanceVectorTable() {

		for(int i = 0; i<5;i++){
			for(int n = 0; n<5;n++){
				distanceTable[i][n] = 999;
			}
		}
		distanceTable[2][2] = 0;
		distanceTable[0][0] = NetworkSimulator.cost[2][0];
		distanceTable[3][3] = NetworkSimulator.cost[2][3];    
	}

	//Updates the changes made by the corresponding packet and notifies other nodes if it is necessary
	public void update(Packet p)
	{   
		int sourceNode = p.getSource();
		boolean send = ArrangeVectorTable(sourceNode,p);
		if(send){
			AnswerNode(sourceNode,3);
			AnswerNode(sourceNode,0);
		}
	}

	//Notifies the destination node by using the source node
	private void AnswerNode(int sourceNode, int destination) {
		int[] arr = GetMinArr();
		Packet packetToSend = new Packet(2,destination,arr);
		NetworkSimulator.toLayer2(packetToSend);
	}

	//Sets the new values (if there are) to the distance vector table
	private boolean ArrangeVectorTable(int sourceNode, Packet p) {
		int counter = 0;
		for(int n = 0; n < 5; n++){
			if(distanceTable[n][sourceNode] != p.getMincost(n) + NetworkSimulator.cost[2][sourceNode]){
				distanceTable[n][sourceNode] = p.getMincost(n) + NetworkSimulator.cost[2][sourceNode];
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
		System.out.println("           via");
		System.out.println(" D2 |   0   3");
		System.out.println("----+---------");
		for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
		{
			if (i == 2)
			{
				continue;
			}

			System.out.print("   " + i + "|");
			for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
			{
				if (j == 2 || j==1 || j==4)
				{
					continue;
				}

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
