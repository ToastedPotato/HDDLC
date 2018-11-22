package hdlc;

public class Hdlc
{
    public static void main(String[] args) {
        
        string errorMsg = "Commande incorrecte, veuillez spécifier les" + 
            "paramètres d'initialisation comme suit: \n" + 
            "-Exécuter un émetteur: Sender <Nom_Machine> <Numero_Port> " + 
            "<Nom_fichier> <0>\n" + 
            "-Exécuter un receveur: Receiver <Numero_Port>\n"
        
        if(args[0].equals("Sender")){
            //Si on reçoit la commande d'initialisation d'un émetteur
            if(args.length != 5){
                System.out.println(errorMsg);
            }else{
                /*
                Créer un Émetteur, lui faire lire les données du fichier et lui 
                faire exécuter les boucles d'envoi de trammes et de traitements 
                d'accusés de réception/messages d'erreurs
                */
                
                //TODO: création et exécution de l'émetteur ici
            
            }
        }else if(args[0].equals("Receiver")){
            //Si on reçoit la commande d'initialisation d'un émetteur
            if(args.length != 2){
                System.out.println(errorMsg);
            }else{
                /*
                Créer un Receveur, lui faire exécuter les boucles de 
                réception/traitement de trammes et d'envoi d'accusés de 
                réception/messages d'erreurs
                */
                
                //TODO: création et exécution du receveur ici
            
            }
        }else{
            System.out.println(errorMsg);        
        }
    }

}
