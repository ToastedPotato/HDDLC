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
    
    private String convertToBits(String s){
        //conversion de la chaîne de charactères en une chaîne de 0 et de 1 
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
            
            if(x <= 65535){
            //on lit 1 par 1 les bits de la trame jusqu'à avoir un nombre assez 
            //grand pour le XOR
                if(bits.charAt(i) == '0'){
                    x = x<<1;
                }else{x = (x<<1) + 1;}
            }else{ x = x^this.polynome;}
        
        }
                
        return x;
    
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
