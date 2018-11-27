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
    public String checkCRC(String frame){
        /*
        diviser la chaîne de bits obtenue à partir des éléments Type, Num, 
        Données et CRC de la tramme. Retourne le reste de cette division. Un 
        reste = 0 indique aucune erreur, sinon il y a une erreur. La méthode 
        assume que la trame est founie sans les charactères flags
        */
        String data = frame;
        String remainder = "";
        //String p = "10001000000100001";
        
        
        //TODO: faire la vérification du CRC ici
        byte[] byteArray = data.getBytes();
        StringBuilder bitChain = new StringBuilder();
        for (byte b : byteArray)
        {
            int value = b;
            for (int i = 0; i < 8; i++)
            {
                bitChain.append((value & 128) == 0 ? 0 : 1);
                value <<= 1;
             }
        }
        
        //la division
        int x = 0;                
        for(int i = 0; i < data.length(); i++){
            
            if(x <= 65535){
            //on lit 1 à 1 les bits de la trame jusqu'à avoir un nombre assez 
            //grand pour le XOR
                if(bitChain.charAt(i) == 0){
                    x = x*2;
                }else{x = (x*2) + 1;}
            }else{ x = x^this.polynome;}
        
        }
        
        //conversion du reste en char
        remainder = ((char) (x>>8)) + "" + ((char) (x & 255));
        
        return remainder;
    
    }
    
    //Retire le bit stuffing de la tramme reçue
    public String bitDeStuffing(String frame){
        /*
        Le but est de vérifier s'il y a des bits de stuffing dans la tramme 
        reçue et les retirer pour retourner la tramme sans bit Stuffing. Si 5 
        bits à 1 consécutifs apparaissent, le bit 0 suivant est à retirer
        */
        
        String destuffedFrame = "";
        
        //TODO: Retirer le bit stuffing ici
               
        
        return destuffedFrame;
    
    }
    

}
