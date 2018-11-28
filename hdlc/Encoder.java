package hdlc;

public class Encoder
{
/*
Permet le calcul du CRC et du bit stuffing sur une tramme 
*/
    //Le polynôme générateur
	private static final int POLYNOME = (1<<16)+(1<<12)+(1<<5)+1;
    
    //conversion d'une chaîne de charactères en une chaîne de 0 et de 1
    private String convertToBits(String s){
        StringBuilder bitChain = new StringBuilder();
        for (int i = 0; i < s.length(); i++){
            int value = s.codePointAt(i);
            
            for (int j = 7; j >= 0; j--){
                bitChain.append((value & 1<<j) == 0? '0' : '1');
            }
        }
        
        return bitChain.toString();
    
    }
    
    //conversion d'une chaîne de bits en chaîne de charactères
    private String convertToChars(String bits){        
        int x = 0;
        StringBuilder chars = new StringBuilder();                
        for(int i = 1; i <= bits.length(); i++){
                                    
            //on lit 1 par 1 les bits jusqu'à en avoir 8
            if(bits.charAt(i-1) == '0'){
                x = x<<1;
            }else{x = (x<<1) + 1;} 
                        
            if(i % 8 == 0){
                //on convertit le nombre x sur 8 bits en un charactère
                chars.append(Character.toChars(x)[0]);
                x = 0;
            }           
        
        }
        
        return chars.toString();
    }
        
    //Vérifie la présence d'erreurs dans la tramme à l'aide du polynôme
    public int checkCRC(String frame){
        /*
        diviser la chaîne de bits obtenue à partir des éléments Type, Num, 
        Données et CRC de la tramme. Retourne le reste de cette division. Un 
        reste = 0 indique aucune erreur, sinon il y a une erreur. La méthode 
        assume que la trame est fournie sans les charactères flags
        */
        String data = frame;
        
        //TODO: faire la vérification du CRC ici
        
        //conversion de la trame en une chaîne de 0 et de 1        
        String bits = this.convertToBits(data);
        
        //la division
        int x = 0;                
        for(int i = 0; i < bits.length(); i++){
            //division si le 17e bit = 1
            if(x > 65535){x = x^POLYNOME;}
            
            //on lit 1 par 1 les bits de la trame jusqu'à avoir un nombre assez 
            //grand pour le XOR: 17 bits, avec le 17e à 1
            if(bits.charAt(i) == '0'){
                x = x<<1;
            }else{x = (x<<1) + 1;}
                
        }                
        return x;
    
    }
    
    //Cacule le CRC à l'aide du polynôme et retourne la tramme avec le CRC
    public String computeCRC(String data){
        /*
        diviser la chaîne de bits obtenue à partir des éléments Type, Num et
        Données du message. Retourne un string contenant Type, Num, Données et
        CRC
        */
        String crc = "";
        
        
        //Calcul du reste sur la tramme+16 bits à 0        
        String s = data + Character.toChars(0)[0] + Character.toChars(0)[0];         
        int x = this.checkCRC(s);
        
        //si le reste est non nul
        if(x != 0){            
            crc = Character.toChars(x>>8)[0] + "" + Character.toChars(x & 255)[0];
        }
        
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
                
        String bits = this.convertToBits(frame);
        
       //ajout d'un 0 après chaque 11111
        bits = bits.replace("11111", "111110");
        
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
    
    //Retire le bit stuffing de la tramme reçue
    public String bitDeStuffing(String frame){
        /*
        Le but est de vérifier s'il y a des bits de stuffing dans la tramme 
        reçue et les retirer pour retourner la tramme sans bit Stuffing. Si 5 
        bits à 1 consécutifs apparaissent, le bit 0 suivant est à retirer. On 
        assume que la frame est fournie sans les flags
        */
                
        //TODO: Retirer le bit stuffing ici
        String bits = this.convertToBits(frame);       
        
        //si on a eu des bits inversés/perdus, on attrape l'erreur ici avant d'avoir à 
        //passer au travers du calcul de CRC
        if(bits.indexOf("111111") != -1 || bits.length() % 8 != 0){
            return "error";
        }
        
        bits = bits.replace("111110", "11111");
        
        //Vu qu'on veut des chars, le nombre de bits doit être divisible par 8
        while(bits.length() % 8 != 0){
            bits = bits.substring(0, bits.length()-1);
        }
        
        return this.convertToChars(bits);
    
    }
}
