package hdlc;

import java.net.*;
import java.io.*;

public class Sender {
/*
La classe se chargeant de lire les données dans le fichier, créer les trames et 
gérer les réponses du récepteur
*/

	private static final int POLYNOME = (1<<16)+(1<<12)+(1<<5)+1;
	
    //Les paramètres fournis à l'exécution pour l'émetteur
    private String machineName;
    private String fileName;
    private int windowSize;
    private int portNum;
    
    //Les données extraites du fichier
    private String fileData;    
        
    //S'occupe de la préparation des trames en calculant le CRC et procédant
    //au bit stuffing
    private Encoder encoder;
    
    //S'occupe d'éliminer le bit stuffing des trames reçues du récepteur
    private Decoder decoder;

    
    // Gère le transfert des données.
	private Socket socket; 
	private BufferedReader reader; 
	private DataOutputStream out; 

    
    //Constructeur
    public Sender(String mName, int pNum, String fName, int wSize){
		try {
			this.machineName = mName;
	        this.portNum = pNum;
	        this.fileName = fName;
	        this.windowSize = wSize;
	        this.encoder = new Encoder(POLYNOME);
	        this.decoder = new Decoder(POLYNOME);
			this.socket = new Socket(this.machineName, this.portNum);  
			this.reader = new BufferedReader(new FileReader(this.fileName)); 
			this.out = new DataOutputStream(socket.getOutputStream());
		}
		catch(UnknownHostException u) {
    		System.out.println(u); 
    	}
    	catch(IOException i) {
    		System.out.println(i); 
    	}
    }
    
    private void run(){
    	this.connectionRequest();	
    	// while there is lines in the file
    	String line = "";
    	while (!line.equals("Over")) { 
            try {
                line = reader.readLine(); 
                out.writeUTF(line); 
            } catch(IOException i) { 
                System.out.println(i); 
            }
        } 
    		// encode the lines    	
    }
    
    
    private void connectionRequest(){
    	
    	// request connection
		//create frame with info
		//send info, wait for return ACK
    }
        
    // closes the connection    
    private void close() {
		try { 
			reader.close(); 
			out.close(); 
			socket.close(); 
		} 
		catch(IOException i) {
			System.out.println(i); 
		}
    }

	public static void main(String[] args){
		String errorMsg = "Commande incorrecte, veuillez spécifier les" + 
	            "paramètres d'initialisation comme suit: \n" +
	            "java Sender <Nom_Machine> <Numero_Port> " +
	            "<Nom_fichier> <0>\n";
		System.out.println(args.length);
		if (args.length >= 4) {
			try{			
				String machineName = args[0];
				int portNumber = Integer.parseInt(args[1]);
				String fileName = args[2];
				int windowSize = Integer.parseInt(args[3]);
				Sender sender = new Sender(machineName, portNumber, fileName, windowSize);
				sender.run();
				sender.close();
			}catch(IllegalArgumentException e){
				System.out.println(e);
			}
		} else {
			System.out.println(errorMsg);
		}
	}
}
