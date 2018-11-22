package hdlc;

public class Sender
{
/*
La classe se chargeant de lire les données dans le fichier, créer les trames et 
gérer les réponses du récepteur
*/
    
    //Les paramètres fournis à l'exécution pour l'émetteur
    private string machineName;
    private string portNum;
    private string fileName;
    private int windowSize;
    
    //Les données extraites du fichier
    private string fileData;    
    
    private static final int polynome = (1<<16)+(1<<12)+(1<<5)+1;
    
    //S'occupe de la préparation des trames en calculant le CRC et procédant
    //au bit stuffing
    private Encoder e;
    
    //S'occupe d'éliminer le bit stuffing des trames reçues du récepteur
    private Decoder d;
    
    //Constructeur
    public Sender(string mName, string pNum, string fName, int wSize){
        this.machineName = mName;
        this.portNum = pNum;
        this.fileName = fName;
        this.windowSize = wSize;
        
        this.e = new Encoder(this.polynome);
        this.d = new Decoder(this.polynome);    
    
    }

}
