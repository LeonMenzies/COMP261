
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

	/*
	 * This DFT method converts a waveform of doubles into a spectrum of complex
	 * numbers to display the frequency
	 */
	public void dft() {
		UI.clearText();
		UI.println("DFT in process, please wait...");

		// Convert waveform to complex number array
		ArrayList<ComplexNumber> newSpec = new ArrayList<>();
		for (double d : waveform) {
			newSpec.add(new ComplexNumber(d, 0));
		}

		for (int k = 0; k < waveform.size(); k++) {

			ComplexNumber sum = new ComplexNumber();

			for (int n = 0; n < waveform.size(); n++) {

				// Calculate the term
				double t = -n * k * 2 * Math.PI / waveform.size();

				// Calculate the e value using Euler's formula
				ComplexNumber exp = new ComplexNumber(Math.cos(t), Math.sin(t));
				sum = sum.addition(newSpec.get(n).multiply(exp));

			}

			// Finally add the complex number to the spectrum
			spectrum.add(sum);
		}

		UI.println("DFT completed!");
		waveform.clear();
	}

	/*
	 * This method is the inverse of dft. It simply takes the spectrum of complex
	 * numbers and converts them back into a waveform of doubles
	 */
	public void idft() {
		UI.clearText();
		UI.println("IDFT in process, please wait...");

		ArrayList<ComplexNumber> newSpec = new ArrayList<>(spectrum);

		for (int k = 0; k < spectrum.size(); k++) {

			ComplexNumber sum = new ComplexNumber();

			for (int n = 0; n < spectrum.size(); n++) {

				double t = n * k * 2 * Math.PI / spectrum.size();

				ComplexNumber exp = new ComplexNumber(Math.cos(t), Math.sin(t));

				sum = sum.addition(newSpec.get(n).multiply(exp));

			}

			waveform.add(sum.getRe() * 1 / spectrum.size());
		}

		UI.println("IDFT completed!");
		spectrum.clear();
	}

	/*
	 * Helper method for checking a number is to the power of 2
	 */
	private boolean isPowerOfTwo(int number) {
		return number > 0 && ((number & (number - 1)) == 0);
	}

	/*
	 * FFT is used to compute the transformation in O(nlog(n)) instead of n^2 for
	 * much better efficiency using divide and conquer
	 */
	public void fft() {
		UI.clearText();
		UI.println("FFT in process, please wait...");

		// Check that there is numbers to compute
		if (waveform.isEmpty()) {
			return;
		}

		// Check that the wave form is over the power 2 so the divide and conquer is not
		// trying to split un-even bins
		while (!isPowerOfTwo(waveform.size())) {
			waveform.remove(waveform.size() - 1);
		}

		ArrayList<ComplexNumber> newSpec = new ArrayList<>();
		for (double d : waveform) {
			newSpec.add(new ComplexNumber(d, 0));
		}

		// Call the recursive helper method
		spectrum = fftHelper(newSpec);

		UI.println("FFT completed!");
		waveform.clear();
	}

	/*
	 * This method handles the recursion for the fft algorithm
	 */
	public ArrayList<ComplexNumber> fftHelper(ArrayList<ComplexNumber> x) {
		int N = x.size();

		// Base case
		if (N == 1) {
			return x;
		}

		ArrayList<ComplexNumber> xeven = new ArrayList<>();
		ArrayList<ComplexNumber> xodd = new ArrayList<>();

		// Split the array into odd and evens using the index of i
		for (int i = 0; i < x.size(); i += 2) {
			xeven.add(x.get(i));
			xodd.add(x.get(i + 1));
		}

		// Go a layer deeper into the recursion
		ArrayList<ComplexNumber> Xeven = fftHelper(xeven);
		ArrayList<ComplexNumber> Xodd = fftHelper(xodd);

		ComplexNumber[] X = new ComplexNumber[N];

		// Calculate the terms, then add and multiply using the e term calculated with
		// Euler's formula
		for (int k = 0; k < N / 2; k++) {
			double t1 = W(k, N);
			double t2 = W(k + N / 2, N);

			X[k] = (Xeven.get(k).addition(Xodd.get(k).multiply(new ComplexNumber(Math.cos(t1), Math.sin(t1)))));
			X[k + N / 2] = (Xeven.get(k).addition(Xodd.get(k).multiply(new ComplexNumber(Math.cos(t2), Math.sin(t2)))));

		}

		return new ArrayList<ComplexNumber>(Arrays.asList(X));
	}

	/*
	 * Helper method for calculating the term
	 */
	public double W(int k, int size) {
		return -k * 2 * Math.PI / size;

	}

	/*
	 * This is the inverse of fft for converting the spectrum of complex numbers
	 * back into a waveform of doubles
	 */
	public void ifft() {
		UI.clearText();
		UI.println("IFFT in process, please wait...");

		if (spectrum.isEmpty()) {
			return;
		}

		while (!isPowerOfTwo(spectrum.size())) {
			spectrum.remove(spectrum.size() - 1);
		}

		for (ComplexNumber c : fftHelper(spectrum)) {
			waveform.add(c.getRe() * 1 / spectrum.size());
		}

		for (Double d : waveform) {
			System.out.println(d);
		}

		UI.println("IFFT completed!");

		spectrum.clear();
	}

	public ArrayList<ComplexNumber> ifftHelper(ArrayList<ComplexNumber> x) {
		int N = x.size();

		if (N == 1) {
			return x;
		}

		ArrayList<ComplexNumber> xeven = new ArrayList<>();
		ArrayList<ComplexNumber> xodd = new ArrayList<>();

		for (int i = 0; i < x.size(); i += 2) {
			xeven.add(x.get(i));
			xodd.add(x.get(i + 1));
		}

		ArrayList<ComplexNumber> Xeven = fftHelper(xeven);
		ArrayList<ComplexNumber> Xodd = fftHelper(xodd);

		ComplexNumber[] X = new ComplexNumber[N];

		for (int k = 0; k < N / 2; k++) {

			double t1 = W(k, N);
			double t2 = W(k + N / 2, N);

			X[k] = (Xeven.get(k).addition(Xodd.get(k).multiply(new ComplexNumber(Math.cos(t1), Math.sin(t1)))));
			X[k + N / 2] = (Xeven.get(k).addition(Xodd.get(k).multiply(new ComplexNumber(Math.cos(t2), Math.sin(t2)))));

		}

		return new ArrayList<ComplexNumber>(Arrays.asList(X));
	}

	public double iW(int k, int size) {
		return k * 2 * Math.PI / size;

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

		ArrayList<Double> test1 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0));
		ArrayList<Double> test2 = new ArrayList<>(Arrays.asList(1.0, 2.0, 1.0, 2.0, 1.0, 2.0, 1.0, 2.0));
		ArrayList<Double> test3 = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 4.0, 3.0, 2.0, 1.0));

		waveform = test3;

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
