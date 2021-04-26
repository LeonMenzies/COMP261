
// DO NOT DISTRIBUTE THIS FILE TO STUDENTS
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import ecs100.UI;

/*
  getAudioInputStream
  -> getframelength,
  -> read into byteArray of 2x that many bytes
  -> convert to array of doubles in reversed pairs of bytes (signed)
  -> scale #FFFF to +/- 300

  array of doubles
   -> unscale  +/- 300  to #FFFF (
   -> convert to array of bytes (pairs little endian, signed)
   -> convert to inputStream
   -> convert to AudioInputStream
   -> write to file.
 */

public class SoundWaveform {

	public static final double MAX_VALUE = 300;
	public static final int SAMPLE_RATE = 44100;
	public static final int MAX_SAMPLES = SAMPLE_RATE / 100; // samples in 1/100 sec

	public static final int GRAPH_LEFT = 10;
	public static final int ZERO_LINE = 310;
	public static final int X_STEP = 2; // pixels between samples
	public static final int GRAPH_WIDTH = MAX_SAMPLES * X_STEP;

	private ArrayList<Double> waveform = new ArrayList<Double>(); // the displayed waveform
	private ArrayList<ComplexNumber> spectrum = new ArrayList<ComplexNumber>(); // the spectrum: length/mod of each X(k)

	/**
	 * Displays the waveform.
	 */
	public void displayWaveform() {
		if (this.waveform == null) { // there is no data to display
			UI.println("No waveform to display");
			return;
		}
		UI.clearText();
		UI.println("Printing, please wait...");

		UI.clearGraphics();

		// draw x axis (showing where the value 0 will be)
		UI.setColor(Color.black);
		UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_LEFT + GRAPH_WIDTH, ZERO_LINE);

		// plot points: blue line between each pair of values
		UI.setColor(Color.blue);

		double x = GRAPH_LEFT;
		for (int i = 1; i < this.waveform.size(); i++) {
			double y1 = ZERO_LINE - this.waveform.get(i - 1);
			double y2 = ZERO_LINE - this.waveform.get(i);
			if (i > MAX_SAMPLES) {
				UI.setColor(Color.red);
			}
			UI.drawLine(x, y1, x + X_STEP, y2);
			x = x + X_STEP;
		}

		UI.println("Printing completed!");
	}

	/**
	 * Displays the spectrum. Scale to the range of +/- 300.
	 */
	public void displaySpectrum() {
		if (this.spectrum == null) { // there is no data to display
			UI.println("No spectrum to display");
			return;
		}
		UI.clearText();
		UI.println("Printing, please wait...");

		UI.clearGraphics();

		// calculate the mode of each element
		ArrayList<Double> spectrumMod = new ArrayList<Double>();
		double max = 0;
		for (int i = 0; i < spectrum.size(); i++) {
			if (i == MAX_SAMPLES)
				break;
			System.out.println(spectrum.get(i));
			double value = spectrum.get(i).mod();
			max = Math.max(max, value);
			spectrumMod.add(spectrum.get(i).mod());
		}

		double scaling = 300 / max;
		for (int i = 0; i < spectrumMod.size(); i++) {
			spectrumMod.set(i, spectrumMod.get(i) * scaling);
		}

		// draw x axis (showing where the value 0 will be)
		UI.setColor(Color.black);
		UI.drawLine(GRAPH_LEFT, ZERO_LINE, GRAPH_LEFT + GRAPH_WIDTH, ZERO_LINE);

		// plot points: blue line between each pair of values
		UI.setColor(Color.blue);

		double x = GRAPH_LEFT;
		for (int i = 1; i < spectrumMod.size(); i++) {
			double y1 = ZERO_LINE;
			double y2 = ZERO_LINE - spectrumMod.get(i);
			if (i > MAX_SAMPLES) {
				UI.setColor(Color.red);
			}
			UI.drawLine(x, y1, x + X_STEP, y2);
			x = x + X_STEP;
		}

		UI.println("Printing completed!");
	}

	public void dft() {
		UI.clearText();
		UI.println("DFT in process, please wait...");

		// TODO
		// Add your code here: you should transform from the waveform to the spectrum

		for (double k = 0; k < waveform.size(); k++) {

			ComplexNumber sum = new ComplexNumber();

			// for (double d : waveform) {
			for (int n = 0; n < waveform.size(); n++) {

				double d = waveform.get(n);

				// double kth = -2 * k * Math.PI / waveform.size();
				// double t = -i * k * ((2 * Math.PI) / (waveform.size()));
				double t = -n * k * 2 * Math.PI / waveform.size();

				ComplexNumber term = new ComplexNumber(Math.cos(t), Math.sin(t));

				// c = c.pow(new ComplexNumber(d, 0));

				sum = sum.addition(new ComplexNumber(d, 0).multiply(term));

//				double term = (-2 * Math.PI * k) / waveform.size();
//
//				// ComplexNumber exp = new ComplexNumber(Math.cos(term), Math.sin(term));
//				
//				double ihatecomp = count * k * ((2 * Math.PI) / waveform.size());
//				ComplexNumber e = new ComplexNumber(0.0, );
//				
//				ComplexNumber exp = new ComplexNumber(Math.pow(Math.E, sum.getRe()) * Math.cos(term),
//						Math.pow(Math.E, sum.getRe()) * Math.sin(term));
//
//				// sum = sum.addition(new ComplexNumber(d, 0).multiply(exp));
//				exp = exp.multiply(new ComplexNumber(d, exp.getIm()));
//				sum = sum.addition(exp);

			}
			System.out.println(sum);

			spectrum.add(sum);

		}

		UI.println("DFT completed!");
		waveform.clear();
	}

	public void idft() {
		UI.clearText();
		UI.println("IDFT in process, please wait...");

		// TODO
		// Add your code here: you should transform from the spectrum to the waveform

		UI.println("IDFT completed!");

		spectrum.clear();
	}

	public void fft() {
		UI.clearText();
		UI.println("FFT in process, please wait...");

		// TODO
		// Add your code here: you should transform from the waveform to the spectrum

		UI.println("FFT completed!");
		waveform.clear();
	}

	public void ifft() {
		UI.clearText();
		UI.println("IFFT in process, please wait...");

		// TODO
		// Add your code here: you should transform from the spectrum to the waveform

		UI.println("IFFT completed!");

		spectrum.clear();
	}

	/**
	 * Save the wave form to a WAV file
	 */
	public void doSave() {
		WaveformLoader.doSave(waveform, WaveformLoader.scalingForSavingFile);
	}

	/**
	 * Load the WAV file.
	 */
	public void doLoad() {
		UI.clearText();
		UI.println("Loading...");

		// waveform = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0,
		// 8.0));
		waveform = new ArrayList<>(Arrays.asList(1.0, 2.0, 1.0, 2.0, 1.0, 2.0, 1.0, 2.0));

		// waveform = WaveformLoader.doLoad();

		this.displayWaveform();

		UI.println("Loading completed!");
	}

	public static void main(String[] args) {
		SoundWaveform wfm = new SoundWaveform();
		// core
		UI.addButton("Display Waveform", wfm::displayWaveform);
		UI.addButton("Display Spectrum", wfm::displaySpectrum);
		UI.addButton("DFT", wfm::dft);
		UI.addButton("IDFT", wfm::idft);
		UI.addButton("FFT", wfm::fft);
		UI.addButton("IFFT", wfm::ifft);
		UI.addButton("Save", wfm::doSave);
		UI.addButton("Load", wfm::doLoad);
		UI.addButton("Quit", UI::quit);
		// UI.setMouseMotionListener(wfm::doMouse);
		UI.setWindowSize(950, 630);
	}
}
