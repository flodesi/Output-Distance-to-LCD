package utils;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class Utils {

	public final static int TopRow = 0;
	public final static int BottomRow = 1;
	private static GpioPinDigitalOutput Trigger;
	private static GpioPinDigitalOutput Echo;
	final static GpioController gpio = GpioFactory.getInstance();

	public void use() throws InterruptedException {

		final GpioLcdDisplay lcd = new GpioLcdDisplay(2, 16, RaspiPin.GPIO_29,
				RaspiPin.GPIO_25, RaspiPin.GPIO_24, RaspiPin.GPIO_23,
				RaspiPin.GPIO_22, RaspiPin.GPIO_21);
		Trigger = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28);
		Echo = (GpioPinDigitalOutput) gpio.provisionDigitalInputPin(
				RaspiPin.GPIO_27, PinPullResistance.PULL_DOWN);

		while (true) {

			try {
				Thread.sleep(2000);
				Trigger.high();
				Thread.sleep((long) 0.01);
				Trigger.low();

				while (Echo.isLow()) {
				}

				long startTime = System.nanoTime();

				while (Echo.isHigh()) {
				}

				long endTime = System.nanoTime();
				int l = (int) Math
						.round((((endTime - startTime) / 1e3) / 2) / 29.1);
				String p = Integer.toString(l);
				lcd.clear();
				System.out.println(p);
				lcd.write(TopRow, "The Distance is:");
				lcd.write(BottomRow, (p + " cm"));
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
