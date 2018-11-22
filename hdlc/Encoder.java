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
        string CRC = "";
        
        //TODO: faire le calcul du CRC ici
        
        return data+CRC;
    
    }
    
    //Applique le bit stuffing sur la tramme à envoyer
    public string bitStuffing(string frame){
        /*
        Le but est de rendre impossible l'occurrence de caractères "~" 
        (01111110) dans la tramme. Ex: insérer un 0 entre le 5e et 6e 1 d'une 
        chaîne de 6 bits 1 consécutifs
        */
        string stuffedFrame = "";
        
        //TODO: Appliquer le bit stuffing ici
        
        return stuffedFrame;
    
    } 

}
