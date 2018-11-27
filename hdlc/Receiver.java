package hdlc;

public class Receiver
{
/*
Le classe recevant les messages de l'émetteur. Décode les trames puis vérifie la
présence d'erreurs avec le CRC. Retourne un RR ou un REJ selon qu'il y a 
absence d'erreur ou non
*/

    //Les paramètres fournis à l'exécution pour l'émetteur
    private String portNum;
        
    private static final int polynome = (1<<16)+(1<<12)+(1<<5)+1;
    
    //S'occupe de la préparation des trames en calculant le CRC et procédant
    //au bit stuffing
    private Encoder e;
    
    //S'occupe d'éliminer le bit stuffing des trames reçues du récepteur
    private Decoder d;
    
    //Constructeur
    public Receiver(String pNum){
        this.portNum = pNum;
        
        this.e = new Encoder(this.polynome);
        this.d = new Decoder(this.polynome);    
    
    }
}
