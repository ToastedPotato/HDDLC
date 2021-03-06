package hdlc;

public class Test{
          
    public static void main(String[] args){  
        
        //tramme de test, Type = I, Num = @ (le numéro de la tramme est 2, encodé sur les bits 7, 6 et 5 de l'octet Num)    
        String testFrame="~I@ABCDEFG~je~connais~mon~alphabet~";
        
        //tramme de test pour le CRC; c'est le polynom CRC-CCIT multiplié par 8
        String testString=""+((char) 8)+((char) 129)+((char) 8);  
        
        Encoder e = new Encoder();
        
        //Test du bit stuffing; on vérifie que la tramme résultante n'a plus de caractères flags dedans
        String stuffedFrame = e.bitStuffing(testFrame.substring(1, testFrame.length()-1));
        
        String success1 = (stuffedFrame.indexOf("~") == -1)? "Test réussi!" : "Test échoué!";
        
        System.out.println("Test des fonctions de l'encodeur \n\n" + "Test du bit Stuffing: \n"+
            "Tramme à convertir: "+ testFrame + "\n" + 
            "Tramme après bit Stuffing: "+ stuffedFrame + "\n" + success1 + "\n");
        
        //Test du bit destuffing; on vérifie l'obtention de la tramme de test après application du bit destuffing
        String deStuffedFrame = e.bitDeStuffing(stuffedFrame);
        
        String success2 = (deStuffedFrame.equals(testFrame.substring(1, testFrame.length()-1)))? "Test réussi!" : "Test échoué!";
                
        System.out.println("Test du bit destuffing: \n"+
            "Tramme à convertir: "+ stuffedFrame + "\n" + 
            "Tramme (sans fanions) après bit destuffing: "+ deStuffedFrame + "\n" + success2 + "\n");
            
        //Test du calcul du CRC; le reste devrait valoir 0 car c'est un numtiple de CRC-CCIT
        int remainder = e.checkCRC(testString);
        
        String success3 = (remainder == 0)? "Test réussi!" : "Test échoué!";
        
        System.out.println("Test du calcul du reste: \n"+
            "Tramme sur laquelle calculer: "+ testString + "\n" + 
            "Reste: "+ remainder + "\n" + success3 + "\n");
        
        //Test de la vérification du CRC. Le reste devrait valoir 0 après l'ajout du CRC à tramme de test
        int remainder2 = e.checkCRC(testFrame.substring(1, testFrame.length()-1));
        
        String frameCRC = "~"+e.computeCRC(testFrame.substring(1, testFrame.length()-1))+"~";
        
        String success4 = (remainder2 != 0)? "Test réussi!" : "Test échoué!";
        
        int remainder3 = e.checkCRC(frameCRC.substring(1, frameCRC.length()-1));
        
        String success5 = (remainder3 == 0)? "Test réussi!" : "Test échoué!";
        
        System.out.println("Test de la vérification du checksum: \n"+
            "Tramme de départ: "+ testFrame + "\n" + 
            "Reste: "+ remainder2 + "\n" + success4 + "\n" +
            "Tramme avec CRC: "+ frameCRC + "\n" + 
            "Reste: "+ remainder3 + "\n" + success5 + "\n");
            
        
        String frameSent = e.buildFrame(testFrame.substring(1, testFrame.length()-1));
        
        int l = frameSent.length();        
        int i = 1024 % (l-1);
        
        if(i == 0){i++;}
        
        String badFrame = frameSent.substring(0, i) + frameSent.substring(i+1, l);
        
        int remainder4 = e.checkCRC(e.decodeFrame(badFrame));
                
        String success6 = (remainder4 != 0)? "Erreur détectée!" : "Erreur non détectée!";
        
        System.out.println("Test de la détection de perte de données: \n"+
            "Tramme envoyée: "+ testFrame + "\n" +
            "Tramme érronée: "+ badFrame +"\n" + success6 + "\n");
                        
        int flipped = frameSent.codePointAt(i)^255;
                   
        String badFrame2 = frameSent.substring(0, i)+Character.toChars(flipped)[0]+frameSent.substring(i+1, l);
        
        String error = e.bitDeStuffing(badFrame2);
                        
        String success7 = (error.equals("error"))? "Erreur détectée!" : "Erreur non détectée!";
        
        System.out.println("Test de la détection de l'inversion d'octets: \n"+
            "Tramme envoyée: "+ testFrame + "\n" +
            "Tramme érronée: "+ badFrame2 +"\n" + success7 + "\n");
                   
    }
    
}
