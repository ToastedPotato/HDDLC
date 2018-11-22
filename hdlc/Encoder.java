package hdlc;

public class Encoder
{
/*
Permet le calcul du CRC et du bit stuffing sur une tramme 
*/
    //Le polynôme générateur
    private int polynome;
    
    //Constructeur
    public Encoder(int p){
        this.polynome = p;    
    }
    
    //Cacule le CRC à l'aide du polynôme et retourne la tramme avec le CRC
    public string computeCRC(string data){
        /*
        diviser la chaîne de bits obtenue à partir des éléments Type, Num et
        Données du message. Retourne un string contenant Type, Num, Données et
        CRC
        */
        string crc = "";
        
        //TODO: faire le calcul du CRC ici
        
        return data+crc;
    
    }
    
    //Applique le bit stuffing sur la tramme à envoyer
    public string bitStuffing(string frame){
        /*
        Le but est de rendre impossible l'occurrence de caractères "~" 
        (01111110) dans la tramme. Essentiellment insérer un 0 après chaque 
        suite de 5 bits à 1
        */
        
        string stuffedFrame = "";
        
        //TODO: Appliquer le bit stuffing ici
        
        return stuffedFrame;
    
    } 

}
