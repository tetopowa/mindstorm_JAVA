package mindstorm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXT;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.util.Delay;
import lejos.util.Stopwatch;
import lejos.util.Timer;
import controls.CapteurContact;
import controls.CapteurLuminosite;
import controls.DetecteurMvt;


/**
 * Classe principale, elle permet de controleur les lumieres et les controleurs 
 */
public class CtrlLum {
	private static Boolean isConnected;
	private static Boolean isAmbiant = false;
	/**
	 * Initiliase tous les parametres en reglage d'usine
	 */
	public void reglageStandard() {
	}

	/**
	 * Initiliase tous les parametres par rapport a la config perso fournie depuis l'appli
	 */
	public void reglagePerso() {

		File data = new File("config.dat");

		try {
			InputStream is = new FileInputStream(data);
			DataInputStream din = new DataInputStream(is);

			while (is.available() > 3) { // at least 4 bytes left to read
				float x = din.readFloat();

				System.out.println("" + x);
			}
			din.close();
		} catch (IOException ioe) {
			System.err.println("Read Exception");
		}
	}

	public boolean ajoutPanneau() throws IOException {

		String connected = "Connected";
		String waiting = "Waiting...";
		String closing = "Closing...";

		LCD.drawString(waiting,0,0);
		LCD.refresh();

		BTConnection btc = Bluetooth.waitForConnection();
		btc.setIOMode(NXTConnection.RAW);
		LCD.clear();
		LCD.drawString(connected,0,0);
		LCD.refresh();	

		DataInputStream dis = btc.openDataInputStream();
		DataOutputStream dos = btc.openDataOutputStream();

		for(int i=0;i<100;i++) {
			int n = dis.readInt();
			LCD.drawInt(n,7,0,1);
			LCD.refresh();
			dos.writeInt(-n);
			dos.flush();
		}

		Byte n = dis.readByte();
		LCD.clear();
		LCD.drawInt(n, 4, 4);

		dis.close();
		dos.close();
		LCD.clear();
		LCD.drawString(closing,0,0);
		LCD.refresh();
		btc.close();
		LCD.clear();

		return true;
	}

	public boolean suppressionPanneau() {

		return true;
	}

	/**
	 * Active le mode Ambiant, calcule l'intensite du plafonnier en fonction de la luminosite exterieur
	 * et allume ou non la lampe de bureau
	 * @param cptlLuminosite 
	 * @param pln 
	 */
	public static SensorPortListener modeAmbiant(Plafonnier pln, CapteurLuminosite cptlLuminosite) {
		isAmbiant = true;
		SensorPortListener sensorListener = new SensorPortListener(){
			@Override
			public void stateChanged(SensorPort source, int oldValue, int newValue) {		
				pln.allumer();
				int intensite = pln.getIntensite();
				if (cptlLuminosite.getLuminosite() < 300 ){
					if(intensite < 360)
						pln.setIntensite(intensite  + 40);
				}
				else if (cptlLuminosite.getLuminosite() <= 700 && cptlLuminosite.getLuminosite() >= 300){
					if(intensite  < 240)
						pln.setIntensite(intensite  + 10);
					if(intensite > 240)
						pln.setIntensite(intensite  - 10);
				}
				else if (cptlLuminosite.getLuminosite() > 700){
					if(intensite  > 180)
						pln.setIntensite(intensite  - 40);
				}
			}
		};
		SensorPort.S3.addSensorPortListener(sensorListener);
		return sensorListener;
	}

	/**
	 * Active le mode manuel
	 * Mode par defaut
	 * @param pln 
	 */
	public static void modeManuel(Plafonnier pln) {
		isAmbiant = false;
		//SensorPort.S3.passivate();
		SensorPort.S3.reset();
		SensorPort.S3.enableColorSensor();
		pln.eteindre();
	}

	public static DataInputStream getBluetoothDIS() throws Exception{
		LCD.clear();
		LCD.drawString("Waiting for Bluetooth",0,0);
		BTConnection btc = Bluetooth.waitForConnection();
		DataInputStream dis = btc.openDataInputStream();
		btc.setIOMode(NXTConnection.RAW);
		LCD.clear();
		LCD.drawString("Connected",0,0);
		isConnected = true;
		Thread.sleep(500);
		return dis;
	}

	/**
	 * Fonction main
	 */
	public static void main(String[] args) throws Exception {

		CapteurContact cptContactLampe = new CapteurContact(SensorPort.S1);
		CapteurContact cptContactPlafonnier = new CapteurContact(SensorPort.S2);
		CapteurLuminosite cptlLuminosite = new CapteurLuminosite(SensorPort.S3);
		DetecteurMvt detectMvt = new DetecteurMvt(SensorPort.S4);
		Lampe lmp = new Lampe(false);
		Plafonnier pln = new Plafonnier(100);
		Stopwatch watch = new Stopwatch();
		DataInputStream dis = getBluetoothDIS();
		//escapeButtonListener();
		// Check guy's presence
		presence(detectMvt, watch, pln, lmp);
		modeButton(pln, cptlLuminosite);
		contacLampetListener(lmp, cptContactLampe);

		contacPlafoniertListener(pln, cptContactPlafonnier, isAmbiant, cptlLuminosite);

		while(isConnected){
			Byte choix = dis.readByte();
			switch(choix){
			case 1:
				if(lmp.isEtat()){
					lmp.eteindre();
				} else {
					lmp.allumer();
				}
				break;
			case 2:
				if(pln.isEtat()){
					pln.eteindre();
				} else {
					pln.allumer();
				}
				break;
			case 3:
				SensorPortListener sensor;
				if(isAmbiant){
					sensor = null;
					LCD.clear(3);
					LCD.drawString("Ambiant OFF", 0, 3);
					modeManuel(pln);
				} else {
					LCD.clear(3);
					LCD.drawString("Ambiant ON", 0, 3);
					sensor = modeAmbiant(pln, cptlLuminosite);
				}
				break;
			default:
				dis.close();
				break;
			}

			if (choix >= 100) {
				int inten = Integer.parseInt(String.valueOf(choix).substring(2,String.valueOf(choix).length()));
				if (inten == 0) {
					pln.setIntensite(100);
				} else {
					pln.setIntensite(inten*100);
				}	
			}
		}
		return;
	}


	/**
	 * Ecoute le bouton d'activation du monde ambiant
	 * @param cptlLuminosite 
	 * @param pln 
	 */
	private static void modeButton(final Plafonnier pln, final CapteurLuminosite cptlLuminosite) {
		Button.LEFT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				LCD.clear(3);
				LCD.drawString("Ambiant ON", 0, 3);
				modeAmbiant(pln, cptlLuminosite);
			}

			public void buttonReleased(Button b) {
				LCD.clear(3);
				LCD.drawString("Ambiant OFF", 0, 3);
				modeManuel(pln);
			}
		});
	}

	/**
	 * Eteins les lumires
	 * @param lmp 
	 * @param pln 
	 * @param watch 
	 */
	private static void stopAll(Plafonnier pln, Lampe lmp, Stopwatch watch) {
		pln.eteindre();
		lmp.eteindre();
		watch.reset();
	}

	/**
	 * Ecoute sur le port du capteur sonore pour detecter une presence
	 * @param watch 
	 * @param lmp 
	 * @param pln 
	 */
	private static void presence(final DetecteurMvt detectMvt, final Stopwatch watch, final Plafonnier pln, final Lampe lmp) {
		SensorPort.S4.addSensorPortListener(new SensorPortListener(){
			@Override
			public void stateChanged(SensorPort source, int oldValue, int newValue) {
				// Stop light after 2 minutes
				if(watch.elapsed() > 120000)
					stopAll(pln, lmp, watch);	
				if (detectMvt.detecte())
					watch.reset();	 
			}
		});
	}

	/**
	 * Ecoute sur le port du capteur de contact du plafonnier
	 */
	private static void contacPlafoniertListener(final Plafonnier pln, final CapteurContact cptContact, final Boolean isAmbiant, 
			final CapteurLuminosite cptlLuminosite) {
		SensorPort.S2.addSensorPortListener(new SensorPortListener(){
			@Override
			public void stateChanged(SensorPort source, int oldValue, int newValue) {
				if (cptContact.contact()){
					pln.allumer();
				}
				else
					pln.eteindre();	 
			}
		});

	}

	/**
	 * Ecoute sur le port du capteur de contact de la lampe
	 */
	private static void contacLampetListener(final Lampe lmp, final CapteurContact cptContact) {
		SensorPort.S1.addSensorPortListener(new SensorPortListener(){
			@Override
			public void stateChanged(SensorPort source, int oldValue, int newValue) {
				if (cptContact.contact())
					lmp.allumer();
				else
					lmp.eteindre();
			}
		});
	}

	private static void escapeButtonListener(){
		Button.ESCAPE.waitForPressAndRelease();
		//isConnected = false;
		LCD.clear();
		LCD.drawString("Exit", 5, 4);
		return;
	}
}
