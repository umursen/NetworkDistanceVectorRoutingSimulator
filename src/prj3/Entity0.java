//Project 3, DV Simulator, 2015
package prj3;

public class Entity0 extends Entity
{    

	//Constructor of Entity Class. Initiates Packet communication
    public Entity0()
    {	
    	InitializeDistanceVectorTable();
    	int[] arrToSend = GetMinArr();
    	Packet pTo1 = new Packet(0,1,arrToSend);
    	Packet pTo2 = new Packet(0,2,arrToSend);
    	NetworkSimulator.toLayer2(pTo1);
    	NetworkSimulator.toLayer2(pTo2);
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
    
    //Sets the initial values of the node0
    private void InitializeDistanceVectorTable() {

    	for(int i = 0; i<5;i++){
    		for(int n = 0; n<5;n++){
    			distanceTable[i][n] = 999;
    		}
    	}
    	distanceTable[0][0] = 0;
    	distanceTable[1][1] = NetworkSimulator.cost[0][1];    
    	distanceTable[2][2] = NetworkSimulator.cost[0][2];
    }

	//Updates the changes made by the corresponding packet and notifies other nodes if it is necessary
    public void update(Packet p)
	{   
		int sourceNode = p.getSource();
		boolean send = ArrangeVectorTable(sourceNode,p);
		if(send)
			AnswerNode(sourceNode,2);
			AnswerNode(sourceNode,1);
	}

    //Notifies the destination node by using the source node
	private void AnswerNode(int sourceNode, int destination) {
		int[] arr = GetMinArr();
		Packet packetToSend = new Packet(0,destination,arr);
		NetworkSimulator.toLayer2(packetToSend);
	}
    
	//Sets the new values (if there are) to the distance vector table
	private boolean ArrangeVectorTable(int sourceNode, Packet p) {
		int counter = 0;
		for(int n = 0; n < 5; n++){
			if(distanceTable[n][sourceNode] != p.getMincost(n) + NetworkSimulator.cost[0][sourceNode]){
				distanceTable[n][sourceNode] = p.getMincost(n) + NetworkSimulator.cost[0][sourceNode];
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
    	Packet pTo1 = new Packet(0,1,arrToSend);
    	Packet pTo2 = new Packet(0,2,arrToSend);
    	NetworkSimulator.toLayer2(pTo1);
    	NetworkSimulator.toLayer2(pTo2);	
    }
    
 // Print destination table
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D0 |   1   2  ");
        System.out.println("----+----------");
        for (int i = 1; i < NetworkSimulator.NUMENTITIES; i++)
        {
            System.out.print("   " + i + "|");
            for (int j = 1; j < NetworkSimulator.NUMENTITIES; j++)
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
                
                if(j==3 || j==4)
                	continue;
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
