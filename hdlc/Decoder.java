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
    public string checkCRC(string frame){
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
    public string bitDeStuffing(string frame){
        /*
        Le but est de vérifier s'il y a des bits de stuffing dans la tramme 
        reçue et les retirer pour retourner la tramme sans bit Stuffing. Étant 
        donné que les champs Type, Num et CRC sont de 1-2 octets et que 
        Données est composé de caractères unicode ou ASCII, une tramme sans bit 
        stuffing a un nombre de bits qui est un multiple de 8. Si n est le 
        nombre de bits dans la tramme, n % 8 nous donne le nombre de bits à 
        retirer.
        */
        
        //TODO: Retirer le bit stuffing ici
        
        return destuffedFrame;
    
    }
    

}
