package hdlc;

import java.net.*;
import java.io.*;


public class Receiver {
/*
Le classe recevant les messages de l'émetteur. Décode les trames puis vérifie la
présence d'erreurs avec le CRC. Retourne un RR ou un REJ selon qu'il y a 
absence d'erreur ou non
*/	
	private static final int POLYNOME = (1<<16)+(1<<12)+(1<<5)+1;
	
	//Les paramètres fournis à l'exécution pour l'émetteur
    private int portNum;
            
    //S'occupe de la préparation des trames en calculant le CRC et procédant
    //au bit stuffing
    private Encoder encoder;
    
    //S'occupe d'éliminer le bit stuffing des trames reçues du récepteur
    private Decoder decoder;
    
  //Gère le transfert de données 
  	private Socket		 	socket; 
  	private ServerSocket 	server; 
  	private DataInputStream in;
    
    //Constructeur
    public Receiver(int pNum) {
        try {
        	this.portNum = pNum;        
            this.encoder = new Encoder(POLYNOME);
            this.decoder = new Decoder(POLYNOME);
			this.server = new ServerSocket(this.portNum);
			this.socket = this.server.accept();
			this.in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}        
    }
    
    private void run() {
		String line = "";
		// TODO: placeholder 
		while (!line.equals("Over")) { 
			try {
				line = this.in.readUTF();
				System.out.println(line);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException i) { 
				System.out.println(i); 
			} 
		} 
    }
    
    private void close() { 
    	try {
			this.in.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    public static void main(String[] args){
		String errorMsg = "Commande incorrecte, veuillez spécifier les" + 
	            "paramètres d'initialisation comme suit: \n" +
	            "-Exécuter un receveur: Receiver <Numero_Port>\n";
		try {		 	
			int portNumber = Integer.parseInt(args[0]);
			Receiver receiver = new Receiver(portNumber);
			receiver.run();
			receiver.close();
		}catch(IllegalArgumentException e){
			System.out.println(errorMsg);
		}
	}
}
