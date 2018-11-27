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
    
    //conversion d'une chaîne de charactères en une chaîne de 0 et de 1
    private String convertToBits(String s){
        byte[] byteArray = s.getBytes();
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
        
        return bitChain.toString();
    
    }
    
    //conversion d'une chaîne de bits en chaîne de charactères
    private String convertToChars(String bits){
        
        int x = 0;
        StringBuilder chars = new StringBuilder();                
        for(int i = 1; i <= bits.length(); i++){
            
            if(i % 8 == 0){
                //on convertit le nombre x sur 8 bits en un charactère
                chars.append((char) x);
                x = 0;
            }
                        
            //on lit 1 par 1 les bits jusqu'à en avoir 8
            if(bits.charAt(i-1) == '0'){
                x = x<<1;
            }else{x = (x<<1) + 1;}            
        
        }
        
        return chars.toString();
    }
    
    //Cacule le CRC à l'aide du polynôme et retourne la tramme avec le CRC
    public String computeCRC(String data){
        /*
        diviser la chaîne de bits obtenue à partir des éléments Type, Num et
        Données du message. Retourne un string contenant Type, Num, Données et
        CRC
        */
        String crc = "";
        
        //TODO: faire le calcul du CRC ici
        
        //conversion du reste en char
        /*réutiliser la méthode checkCRC de decoder serait bien ici. En fait, 
        combiner Encoder et Decoder en une seule classe serait un bon choix.
        convertir le reste en string serait qqch comme ceci:
        String s = data + ((char) 0) + ((char) 0);         
        x = this.checkCRC(s);
        crc = ((char) (x>>8)) + "" + ((char) (x & 255));
        */
        
        return data+crc;
    
    }
    
    //Applique le bit stuffing sur la tramme à envoyer
    public String bitStuffing(String frame){
        /*
        Le but est de rendre impossible l'occurrence de caractères "~" 
        (01111110) dans la tramme. Essentiellment insérer un 0 après chaque 
        suite de 5 bits à 1. La méthode assume que la frame est fournie sans 
        les flags
        */
                
        //TODO: Appliquer le bit stuffing ici
                
        String bits = this.convertToBits(frame);
        
        //ajout d'un 0 après chaque 11111
        while(bits.indexOf("11111") != -1){
            int i = bits.indexOf("11111");
            bits = bits.substring(0, i+5) + "0" + bits.substring(i+5);
        
        }
        
        //étant donné que la tramme doit être envoyée sous forme de caractères
        //on doit s'assurer que le nombre de bits est divisible par 8. On retire
        //les 0 supplémentaires ajoutés par la méthode de bit stuffing
        if(bits.length() % 8 != 0){
            int padding = 8 - (bits.length() % 8);
            
            for(int i = 0; i < padding; i++){
                bits = bits + "0";
            
            }
        }
        
        //conversion de la chaîne de bits en chaîne de charactères
        return this.convertToChars(bits);
    
    } 

}
