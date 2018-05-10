package com.robotson;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory;



public class Main {
	

	// singleton acces au GPIO RASPBERRY
	static final GpioController gpio = GpioFactory.getInstance();
	
	
	public static void main(String[] args)
			throws I2CFactory.UnsupportedBusNumberException,
			InterruptedException {

            
		//System.out.println("===================DEBUT=======================");
                //paramètres reçus
                String paramMoteurHorizontal=null;
                int paramPosMoteurHorizontal=0;
                String paramMoteurVertical=null;
                int paramPosMoteurVertical=0;
                
                //un seul moteur
                if (args.length == 2) {
                    try {
                        paramMoteurHorizontal = args[0];
                        paramPosMoteurHorizontal = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Argument" + args[0] + " must be an integer.");
                        System.exit(1);
                    }
                }
                //deux moteurs
                if (args.length == 4) {
                    try {
                        paramMoteurHorizontal = args[0];
                        paramPosMoteurHorizontal = Integer.parseInt(args[1]);
                        paramMoteurVertical = args[2];
                        paramPosMoteurVertical = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        System.err.println("Argument" + args[0] + " must be an integer.");
                        System.exit(1);
                    }
                }
                
                
		
                
                
                Moteur moteurHorizontal=null;
                Moteur moteurVertical=null;
                
                //commande du moteur horizontal
                if(paramMoteurHorizontal!=null){
                    // creation moteur horizontal
                    moteurHorizontal = new Moteur(gpio, RaspiPin.GPIO_00,RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_03);
                    moteurHorizontal.setActif(true);
                    //lui passer le parametre
                    moteurHorizontal.preference(paramPosMoteurHorizontal);
                }
                
                //commande du moteur vertical
		if(paramMoteurVertical!=null){
                    // creation moteur horizontal
                    moteurVertical = new Moteur(gpio, RaspiPin.GPIO_00,RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_03);
                    moteurVertical.setActif(true);
                    //lui passer le parametre
                    moteurVertical.preference(paramPosMoteurVertical);
                }

		
		// capteur de butee HORIZONTAL GAUCHE
                if(moteurHorizontal!=null || moteurVertical!=null){
                    CapteurButee capteurHorizontalGauche = new CapteurButee(gpio,RaspiPin.GPIO_13,"HORIZONTALGAUCHE");
                    capteurHorizontalGauche.getPinCapteur().addListener(new CapteurButee.CapteurButeeEcouteur(moteurHorizontal));
                    CapteurButee capteurHorizontalDroit = new CapteurButee(gpio,RaspiPin.GPIO_14,"HORIZONTALDROIT");
                    capteurHorizontalDroit.getPinCapteur().addListener(new CapteurButee.CapteurButeeEcouteur(moteurHorizontal));
                }
                
			
		// boucle d'attente
		while (moteurHorizontal.isActif()) {
			Thread.sleep(500);
		}
		

		// stop all GPIO activity/threads by shutting down the GPIO controller
		gpio.shutdown();
		// fermer l'application correctement
		System.exit(0);		 
	}



	
	//fin du programme
}
