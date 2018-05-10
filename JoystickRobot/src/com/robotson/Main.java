package com.robotson;

import java.util.Scanner;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory;



public class Main {

	//boucle
	static char quitter='n';
	
	// singleton acces au GPIO RASPBERRY
	static final GpioController gpio = GpioFactory.getInstance();
	

	public static void main(String[] args)
			throws I2CFactory.UnsupportedBusNumberException,
			InterruptedException {

		//System.out.println("===================DEBUT=======================");

		// creation moteur horizontal
		Moteur moteurHorizontal = new Moteur(gpio, RaspiPin.GPIO_00,RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_03);
		// creation moteur vertical
		Moteur moteurVertical = new Moteur(gpio, RaspiPin.GPIO_04,RaspiPin.GPIO_05, RaspiPin.GPIO_06, RaspiPin.GPIO_10);
		
		// capteur de butee HORIZONTAL GAUCHE
		CapteurButee capteurHorizontalGauche = new CapteurButee(gpio,RaspiPin.GPIO_13,"HORIZONTALGAUCHE");
		capteurHorizontalGauche.getPinCapteur().addListener(new CapteurButee.CapteurButeeEcouteur(moteurHorizontal));
		CapteurButee capteurHorizontalDroit = new CapteurButee(gpio,RaspiPin.GPIO_14,"HORIZONTALDROIT");
		capteurHorizontalDroit.getPinCapteur().addListener(new CapteurButee.CapteurButeeEcouteur(moteurHorizontal));
		
		// capteur de butee VERTICAL BAS
		CapteurButee capteurVertivalHaut = new CapteurButee(gpio,RaspiPin.GPIO_11,"VERTICALHAUT");
		capteurVertivalHaut.getPinCapteur().addListener(new CapteurButee.CapteurButeeEcouteur(moteurVertical));
		CapteurButee capteurVertivalBas = new CapteurButee(gpio,RaspiPin.GPIO_12,"VERTICALBAS");
		capteurVertivalBas.getPinCapteur().addListener(new CapteurButee.CapteurButeeEcouteur(moteurVertical));
		
			
		
		
		// creation joystick (listener GPIO, Gauche, Haut, Droite, Bas)
		Joystick joystick = new Joystick(gpio, RaspiPin.GPIO_23,RaspiPin.GPIO_24, RaspiPin.GPIO_25, RaspiPin.GPIO_22);
	
		// joystick listener Moteur Horizontal
		joystick.getPinEtatGauche().addListener(new Joystick.JoystickEcouteur(moteurHorizontal));
		joystick.getPinEtatDroite().addListener(new Joystick.JoystickEcouteur(moteurHorizontal));
		
		// joystick listener Moteur Vertical
		joystick.getPinEtatHaut().addListener(new Joystick.JoystickEcouteur(moteurVertical));
		joystick.getPinEtatBas().addListener(new Joystick.JoystickEcouteur(moteurVertical));
				
		
		
		
		
		// gestion d'arret du programme en console
		while (quitter!='o') {
			//Thread.sleep(1000);
			
			 System.out.print("Voulez vous quitter l'application ? (o/n) : ");
			 
			 // scanne le clavier
		     Scanner ch = new Scanner(System.in);
		     quitter = ch.next().charAt(0);
		     ch.close();
		}
		
		
		//System.out.println("====================FIN=========================");
		
		
		// stop all GPIO activity/threads by shutting down the GPIO controller
		gpio.shutdown();
		// fermer l'application correctement
		System.exit(0);		 
	}
	
	//fin du programme
}
