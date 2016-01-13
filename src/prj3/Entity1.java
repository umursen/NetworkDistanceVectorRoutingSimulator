//Project 3, DV Simulator, 2015
package prj3;

public class Entity1 extends Entity
{    
	//Constructor of Entity Class. Initiates Packet communication
	public Entity1()
	{
		InitializeDistanceVectorTable();
		int[] arrToSend = GetMinArr();
		Packet pTo0 = new Packet(1,0,arrToSend);
		Packet pTo3 = new Packet(1,3,arrToSend);
		Packet pTo4 = new Packet(1,4,arrToSend);
		NetworkSimulator.toLayer2(pTo0);
		NetworkSimulator.toLayer2(pTo3);
		NetworkSimulator.toLayer2(pTo4);
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

	//Sets the initial values of the node1
	private void InitializeDistanceVectorTable() {

		for(int i = 0; i<5;i++){
			for(int n = 0; n<5;n++){
				distanceTable[i][n] = 999;
			}
		}
		distanceTable[1][1] = 0;
		distanceTable[0][0] = NetworkSimulator.cost[1][0];
		distanceTable[3][3] = NetworkSimulator.cost[1][3];    
		distanceTable[4][4] = NetworkSimulator.cost[1][4];
	}

	//Updates the changes made by the corresponding packet and notifies other nodes if it is necessary
	public void update(Packet p)
	{   
		int sourceNode = p.getSource();
		boolean send = ArrangeVectorTable(sourceNode,p);
		if(send){
			AnswerNode(sourceNode,0);
			AnswerNode(sourceNode,3);
			AnswerNode(sourceNode,4);
		}
	}

	//Notifies the destination node by using the source node
	private void AnswerNode(int sourceNode, int destination) {
		int[] arr = GetMinArr();
		Packet packetToSend = new Packet(1,destination,arr);
		NetworkSimulator.toLayer2(packetToSend);
	}

	//Sets the new values (if there are) to the distance vector table
	private boolean ArrangeVectorTable(int sourceNode, Packet p) {
		int counter = 0;
		for(int n = 0; n < 5; n++){
			if(distanceTable[n][sourceNode] != p.getMincost(n)+ NetworkSimulator.cost[1][sourceNode]){
				distanceTable[n][sourceNode] = p.getMincost(n) + NetworkSimulator.cost[1][sourceNode];
				counter++;
			}
		}
		if(counter == 0)
			return false;
		else
			return true;
	}

	//Updates the new link between the input node and node0 and notifies the neighbor nodes
	public void linkCostChangeHandler(int whichLink, int newCost)
	{
		distanceTable[whichLink][whichLink] = newCost;

		int[] arrToSend = GetMinArr();
		Packet pTo0 = new Packet(1,0,arrToSend);
		Packet pTo3 = new Packet(1,3,arrToSend);
		Packet pTo4 = new Packet(1,4,arrToSend);
		NetworkSimulator.toLayer2(pTo0);
		NetworkSimulator.toLayer2(pTo3);
		NetworkSimulator.toLayer2(pTo4);
	}

	// Print destination table
	public void printDT()
	{
		System.out.println();
		System.out.println("         via");
		System.out.println(" D1 |   0    3    4");
		System.out.println("----+----------------");
		for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
		{
			if (i == 1)
			{
				continue;
			}

			System.out.print("   " + i + "|");
			for (int j = 0; j < NetworkSimulator.NUMENTITIES; j += 1)
			{

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
				if(j==1 || j==2)
					continue;

				System.out.print(distanceTable[i][j]);
			}
			System.out.println();
		}
	}
}
