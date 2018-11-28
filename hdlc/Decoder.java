package hdlc;

public class Decoder
{
/*
Élimine le bit stuffing de la tramme reçue puis vérifie la présence d'erreur 
avec le CRC
*/

    private int polynome;
    
    public Decoder(int p){
        this.polynome = p;    
    }
    
    //Vérifie la présence d'erreurs dans la tramme à l'aide du polynôme
    public int checkCRC(String frame){
        /*
        diviser la chaîne de bits obtenue à partir des éléments Type, Num, 
        Données et CRC de la tramme. Retourne le reste de cette division. Un 
        reste = 0 indique aucune erreur, sinon il y a une erreur
        */
        int remainder = 0;
        
        //TODO: faire la vérification du CRC ici
        
        return remainder;
    
    }
    
    //Retire le bit stuffing de la tramme reçue
    public int bitDeStuffing(String frame){
        /*
        Le but est de vérifier s'il y a des bits de stuffing dans la tramme 
        reçue et les retirer pour retourner la tramme sans bit Stuffing. Si 5 
        bits à 1 consécutifs apparaissent, le bit 0 suivant est à retirer
        */
        int destuffedFrame = 10;
        //TODO: Retirer le bit stuffing ici
        
        return destuffedFrame;
    
    }
    

}
