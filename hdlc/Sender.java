package hdlc;

public class Sender
{
/*
La classe se chargeant de lire les données dans le fichier, créer les trames et 
gérer les réponses du récepteur
*/
    
    //Les paramètres fournis à l'exécution pour l'émetteur
    private String machineName;
    private String portNum;
    private String fileName;
    private int windowSize;
    
    //Les données extraites du fichier
    private String fileData;    
    
    private static final int polynome = (1<<16)+(1<<12)+(1<<5)+1;
    
    //S'occupe de la préparation des trames en calculant le CRC et procédant
    //au bit stuffing
    private Encoder e;
        
    //Constructeur
    public Sender(String mName, String pNum, String fName, int wSize){
        this.machineName = mName;
        this.portNum = pNum;
        this.fileName = fName;
        this.windowSize = wSize;
        
        this.e = new Encoder(this.polynome);
        
    }

}
