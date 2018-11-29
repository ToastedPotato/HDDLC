package hdlc;

import java.net.*;
import java.io.*;
import java.sql.Timestamp;

public class Sender {
/*
	La classe se chargeant de lire les données dans le fichier, d'entamer
	la création des trames et de gérer les réponses du récepteur.
*/
	
    //Les paramètres fournis à l'exécution pour l'émetteur
    private String machineName;
    private String fileName;
    private int windowSize;
    private int portNum;    
        
    //S'occupe de la préparation des trames en calculant le CRC et procédant
    //au bit stuffing. Peut encoder et décoder, au besoin.
    private Encoder encoder;
    
    // Gère le transfert des données.
	private Socket socket; 
	private BufferedReader reader; 
	private DataOutputStream out;
	private DataInputStream in;

    
    //Constructeur
    public Sender(String mName, int pNum, String fName, int wSize){
		try {
			this.machineName = mName;
	        this.portNum = pNum;
	        this.fileName = fName;
	        this.windowSize = wSize;
	        this.encoder = new Encoder();
			this.socket = new Socket(this.machineName, this.portNum);  
			this.reader = new BufferedReader(new FileReader(this.fileName)); 
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
		}
		catch(UnknownHostException u) {
    		System.out.println(u); 
    	}
    	catch(IOException i) {
    		System.out.println(i); 
    	}
    }
    
    // Se charge de l'exécution du protocole HDLC à développer.
    private void run() {
    	boolean connectionSuccessful = this.connectionRequest();
    	if(connectionSuccessful){
    		goBackNLoop(this.windowSize);
    	}
    	disconnectRequest();
    	close();    	
    }

	// Gère l'algorithme de go-Back-N pour Sender.
    // inspiré de https://en.wikipedia.org/wiki/Go-Back-N_ARQ 
    public void goBackNLoop(int windowSize) {
    	int sequenceBase = 0;
    	int sequenceMax = windowSize + 1;
    	boolean dataLeftInFile = true;
    	String dataLine;
    	String response; 
    	String[] slidingWindow = new String[windowSize];
    	long[] temporizer = new long[windowSize];
    	
    	// Boucle initialisant la première sliding window. Elle envoie chaque trame
		// de la fenêtre, sans demander la réponse en retour, pour la première
		// instance de la fenêtre glissante.
    	try {	
    		for(int i=0; i < slidingWindow.length; i++){
    			dataLine = reader.readLine();
    			//S'il reste des lignes dans le fichier:
    			if(dataLine != null){
    				// Créer le frame, l'envoyer, et assigner le temps d'envoi
    				String frame = "I" + Integer.toString(i) + dataLine; 
    				frame = this.encoder.buildFrame(frame);
    				out.writeUTF(frame);
    				temporizer[i] = this.setTime();
    			} else {
    				dataLeftInFile = false;
    				//TODO: sequencemax = i or something
    				break;
    			}
    		}
    		while(dataLeftInFile) {    		    			
    			//Attente d'une réponse par le récepteur.
    			response = this.in.readUTF();
 
    			// S'il y a une réponse,
    			if(response != null) {
    				response = this.encoder.decodeFrame(response);
    				// 	Si la checksum concorde, regarder le type du message
    				System.out.println(this.encoder.checkCRC(response));
    				if(this.encoder.checkCRC(response) == 0) {
    					char frameType = response.charAt(1);
    					// Si la réponse est un Receive Ready, regarder le numéro de la trame. 
    					if(frameType == 'A') {
    						int requestNum = response.charAt(2);
					
    						// Si le numéro de requête est plus grand que le numéro 
    						// du début de la séquence, ou le dernier cas ou
    						// sequenceBase est à la fin du tableau
    						if(requestNum > sequenceBase
    								|| (requestNum == 0 && sequenceBase == 7)) {
						
    							// Ajuster les indices de la fenêtre glissante
    							sequenceMax = (sequenceMax - sequenceBase + requestNum) % windowSize;
    							sequenceBase = requestNum;
    					
    							//Envoie la prochaine trame de la fenêtre glissante
    							// et met à jour le temps d'envoi.
    					
    							out.writeUTF(slidingWindow[sequenceBase]);
    							temporizer[sequenceBase] = this.setTime();
    						    					
    							//Ajoute une nouvelle trame dans la fenêtre glissante, 
    							// à l'index de la requête précédente complétée.
    							dataLine = reader.readLine();
    							if(dataLine != null){
    								slidingWindow[sequenceMax] = dataLine;
    							} else {
    								dataLeftInFile = false;
    							}
    						}

    					// Si la réponse est un rejet, réenvoyer les trames à partir de cet index. 
    					} else if (frameType == 'R') {
    						sendWindow(sequenceBase, sequenceMax, slidingWindow, temporizer);
    					}
   				
    					// Si la vérification par CRC détecte une erreur:
    				} else {
    					System.out.println("Détection d'erreur dans la trame. Bit(s) possiblement corrompus");
    				}
    			}		
    		}
    	} catch (IOException e) {
			e.printStackTrace();
    	}	
   		// Vérification des envois des trames par temporisateur.
    	this.checkTimestamps(temporizer, slidingWindow);
    	
		//Envoi des dernières trames d'information
    	sendWindow(sequenceBase, sequenceMax, slidingWindow, temporizer);	
    }

    private void sendWindow(int start, int finish, String[] information, long[] temporizer){
    	try {
    		for(int i = start % information.length; i != finish; i++){
				out.writeUTF(information[i]);
				temporizer[i] = this.setTime();
    		}
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void checkTimestamps(long[] temporizer, String[] slidingWindow){
    	try {
    		long now = this.setTime();
    		for(int i=0; i < temporizer.length; i++) {
    			//Si le temps écoulé est plus long que 3 secondes,
    			if(now - temporizer[i] >= 3000) { 
    				// Réenvoyer la trame, et mettre à jour le temporisateur
			    	out.writeUTF(slidingWindow[i]);
			    	temporizer[i] = this.setTime();	  
    			}
    		}
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }

    // Entâme une requête de connexion avec Receiver.
    private boolean connectionRequest(){
    	String connectionFrame = this.encoder.buildFrame("C0");
    	try {
			this.out.writeUTF(connectionFrame);
			// Vérifier si une trame de réponse a été envoyée par le récepteur
			while(true) {		
				String response = this.in.readUTF();
				if(response != null) {
					// La trame reçue est décodée
					response = this.encoder.decodeFrame(response);
					
					// Si la trame est valide
					if(this.encoder.checkCRC(response) == 0) {
						
						// Si la trame est une trame d'acceptation de résultats
						if(response.charAt(0) == 'A') {
							System.out.println("Requête de connexion acceptée, connexion établie.");
							return true;
						}
					} else {
						System.out.println("Échec de connexion.");
						return false;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
    }
    
    private void disconnectRequest(){
		try {
			String finalFrame = this.encoder.buildFrame("F0");
			out.writeUTF(finalFrame);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    // Ferme la connexion après l'envoi d'une tramme de fermeture.     
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

    public long setTime(){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.getTime();
	}
    
	public static void main(String[] args) {
		String errorMsg = "Commande incorrecte, veuillez spécifier les" + 
	            "paramètres d'initialisation comme suit: \n" +
	            "java Sender <Nom_Machine> <Numero_Port> " +
	            "<Nom_fichier> <0>\n";
		if (args.length >= 4) {
			try {			
				String machineName = args[0];
				int portNumber = Integer.parseInt(args[1]);
				String fileName = args[2];
				int windowSize = Integer.parseInt(args[3]);
				if (windowSize > 8){
					System.out.println("La taille de la fenêtre doit être moins que 8.");
				} else {
					Sender sender = new Sender(machineName, portNumber, fileName, windowSize);
					sender.run();
					//sender.close();
				}
			} catch(IllegalArgumentException e) {
				System.out.println(e);
			}
		} else {
			System.out.println(errorMsg);
		}
	}
}
