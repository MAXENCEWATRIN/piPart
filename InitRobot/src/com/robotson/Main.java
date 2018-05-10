package com.robotson;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory;


public class Main {

	
	// singleton acces au GPIO RASPBERRY
	static final GpioController gpio = GpioFactory.getInstance();
	// singleton capteur de butee initialisation
	static final Initialisation initialisation = Initialisation.getInstance();

	
	public static void main(String[] args) throws I2CFactory.UnsupportedBusNumberException,InterruptedException {

//		System.out.println("===================DEBUT=======================");

		
		// creation moteur horizontal
		Moteur moteurHorizontal = new Moteur(gpio, RaspiPin.GPIO_00,RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_03);
		// creation moteur vertical
		Moteur moteurVertical = new Moteur(gpio, RaspiPin.GPIO_10,RaspiPin.GPIO_06, RaspiPin.GPIO_05, RaspiPin.GPIO_04);
		
		
		//initialisation
		initialisation.setParametres(moteurHorizontal,moteurVertical,gpio, RaspiPin.GPIO_13, "BUTEEHG", RaspiPin.GPIO_12, "BUTEEVB");
		initialisation.getPinCapteurHG().addListener(new Initialisation.CapteurButeeEcouteur(moteurHorizontal));
		initialisation.getPinCapteurVB().addListener(new Initialisation.CapteurButeeEcouteur(moteurVertical));
		//lancer l'initialisation
		initialisation.demarrer();
		moteurHorizontal.setActif(true);
		moteurVertical.setActif(true);
		


		while (moteurHorizontal.isActif() || moteurVertical.isActif()) {
			Thread.sleep(500);
		}
		
		
//                try {
//                    Runtime rt = Runtime.getRuntime();
//                    Process proc = rt.exec("java -jar /home/pi/joystickrobot/JoystickRobot.jar");
//                    int exitVal = proc.waitFor();
//                    System.out.println("Process exitValue: " + exitVal);
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
//                
                
		//System.out.println("====================FIN=========================");
		
		// stop all GPIO activity/threads by shutting down the GPIO controller
		gpio.shutdown();
		//fermer l'application correctement
		System.exit(0);		 
	}
	
	//fin du programme
}
