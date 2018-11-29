package hdlc;

import java.net.*;
import java.io.*;


public class Receiver {
/*
Le classe recevant les messages de l'émetteur. Décode les trames puis vérifie la
présence d'e/rreurs avec le CRC. Retourne un RR ou un REJ selon qu'il y a 
absence d'erreur ou non
*/	
	
	//Les paramètres fournis à l'exécution pour l'émetteur
    private int portNum;
            
    //S'occupe de la préparation des trames en calculant le CRC et procédant
    //au bit stuffing
    private Encoder encoder;
    
  //Gère le transfert de données 
  	private Socket		 	socket; 
  	private ServerSocket 	server; 
  	private DataInputStream in;
  	private DataOutputStream out;
    
    //Constructeur
    public Receiver(int pNum) {
        try {
        	this.portNum = pNum;        
            this.encoder = new Encoder();
			this.server = new ServerSocket(this.portNum);
			this.socket = this.server.accept();
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}        
    }
    
    // Gère les requêtes demandées par Sender.
    private void run() {
		String frameLine;
		String frame;
		String response;
		int requestNumber = 0;
		boolean openForReception = true;
		boolean connectionOpened = false;
		while(openForReception) {
			try {
				frameLine = this.in.readUTF();
				if(frameLine != null){
					frame = this.encoder.decodeFrame(frameLine);
					char frameType = frame.charAt(0); 
					
					if(frameType == 'C'){
						response = this.encoder.buildFrame("A0");
						out.writeUTF(response);
						connectionOpened = true;
					} else if(frameType == 'I') {
						if (connectionOpened){
							int frameNum = frame.charAt(1);
							if(frameNum == requestNumber) {
								String frameInfo = frame.substring(2, frame.length() - 1);
								System.out.println(" Receiver: Trame " + frameNum +
										"recue, contenu du message: " + frameInfo);
								requestNumber = (requestNumber + 1) % 8;
								response = this.encoder.buildFrame("A" + requestNumber);
								out.writeUTF(response);
							}						
						} else {
							response = this.encoder.buildFrame("R0");
							out.writeUTF(response);
							System.out.println("Erreur: Requête d'envoi" + 
							" d'information envoyée avant requête de connexion.");
						}
					} else if(frameType == 'F'){
						System.out.println("Demande de déconnexion reçue. Déconnexion.");
						openForReception = false;
					}
				}
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
