import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageContrastEnhancement {

	public static void main(String[] args) {

		ImageContrastEnhancement ice = new ImageContrastEnhancement();
		BufferedImage sourceImage = null;
		String imagePath = "input.jpg";
		if (args.length == 2) {
			imagePath = args[1];
		}

		try {

			// Load Image
			sourceImage = ImageIO.read(new File(imagePath));

			// Processing
			BufferedImage ruleBasedImage = makeCopy(sourceImage);
			long time=System.currentTimeMillis();
			ice.RuleBasedContrastEnhancement(ruleBasedImage);
			System.out.println("Time Taken by Rule Based Contrast Enhancement is : "+(System.currentTimeMillis()-time)+" milliseconds");
			
			time=System.currentTimeMillis();
			BufferedImage INTOperatorImage = makeCopy(sourceImage);
			ice.INTBasedContrastEnhancement(INTOperatorImage);
			System.out.println("Time Taken by INT operator Based Contrast Enhancement is : "+(System.currentTimeMillis()-time)+" milliseconds");

			// Save Images
			File fRule = new File("RuleBasedOutput.jpg");
			ImageIO.write(ruleBasedImage, "JPG", fRule);
			File fINT = new File("INTOperatorOutput.jpg");
			ImageIO.write(INTOperatorImage, "JPG", fINT);

			// Measure fuzziness
			ice.measureFuzziness(sourceImage, "Original");
			ice.measureFuzziness(ruleBasedImage, "Rule Based");
			ice.measureFuzziness(INTOperatorImage, "INT Operator");

			// Show Histograms
			ImageIO.write(ice.Histogram(ruleBasedImage), "JPG", new File(
					"Histogram Rule Based.jpg"));
			ImageIO.write(ice.Histogram(INTOperatorImage), "JPG", new File(
					"Histogram INT Operator.jpg"));
			ImageIO.write(ice.Histogram(sourceImage), "JPG", new File(
					"Histogram Original.jpg"));

			System.out.println("Completed");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void RuleBasedContrastEnhancement(BufferedImage inputIm) {

		// Convert the image data into fuzzy domain data
		int widthIm = inputIm.getWidth();
		int heightIm = inputIm.getHeight();

		float min = 1, max = 255, mid = 127;

		float fuzzyDataDarkRed = 0;
		float fuzzyDataDarkGreen = 0;
		float fuzzyDataDarkBlue = 0;
		float fuzzyDataGreyRed = 0;
		float fuzzyDataGreyGreen = 0;
		float fuzzyDataGreyBlue = 0;
		float fuzzyDataBrightRed = 0;
		float fuzzyDataBrightGreen = 0;
		float fuzzyDataBrightBlue = 0;

		for (int i = 0; i < widthIm; i++) {
			for (int j = 0; j < heightIm; j++) {
				Color c = new Color(inputIm.getRGB(i, j));

				// Fuzzification pixel wise
				fuzzyDataDarkRed = getFuzzyValue(0, 0, min, mid, c.getRed());
				fuzzyDataDarkGreen = getFuzzyValue(0, 0, min, mid, c.getGreen());
				fuzzyDataDarkBlue = getFuzzyValue(0, 0, min, mid, c.getBlue());
				fuzzyDataGreyRed = getFuzzyValue(min, mid, mid, max, c.getRed());
				fuzzyDataGreyGreen = getFuzzyValue(min, mid, mid, max,
						c.getGreen());
				fuzzyDataGreyBlue = getFuzzyValue(min, mid, mid, max,
						c.getBlue());
				fuzzyDataBrightRed = getFuzzyValue(mid, max, 255, 255,
						c.getRed());
				fuzzyDataBrightGreen = getFuzzyValue(mid, max, 255, 255,
						c.getGreen());
				fuzzyDataBrightBlue = getFuzzyValue(mid, max, 255, 255,
						c.getBlue());

				// Membership modification
				// Rule Based Technique
				float difference = 0.5f, increment = 0.1f;

				if (fuzzyDataDarkRed > fuzzyDataBrightRed
						&& fuzzyDataDarkRed > fuzzyDataGreyRed) {
					if (fuzzyDataDarkRed - fuzzyDataBrightRed >= difference) {
						fuzzyDataDarkRed += increment;
						fuzzyDataBrightRed -= increment;
					}
				} else if (fuzzyDataBrightRed > fuzzyDataDarkRed
						&& fuzzyDataBrightRed > fuzzyDataGreyRed) {
					if (fuzzyDataBrightRed - fuzzyDataDarkRed >= difference) {
						fuzzyDataBrightRed += increment;
						fuzzyDataDarkRed -= increment;
					}
				}

				if (fuzzyDataDarkGreen > fuzzyDataBrightGreen
						&& fuzzyDataDarkGreen > fuzzyDataGreyGreen) {
					if (fuzzyDataDarkGreen - fuzzyDataBrightGreen >= difference) {
						fuzzyDataDarkGreen += increment;
						fuzzyDataBrightGreen -= increment;
					}
				} else if (fuzzyDataBrightGreen > fuzzyDataDarkGreen
						&& fuzzyDataBrightGreen > fuzzyDataGreyGreen) {
					if (fuzzyDataBrightGreen - fuzzyDataDarkGreen >= difference) {
						fuzzyDataBrightGreen += increment;
						fuzzyDataDarkGreen -= increment;
					}
				}

				if (fuzzyDataDarkBlue > fuzzyDataBrightBlue
						&& fuzzyDataDarkBlue > fuzzyDataGreyBlue) {
					if (fuzzyDataDarkBlue - fuzzyDataBrightBlue >= difference) {
						fuzzyDataDarkBlue += increment;
						fuzzyDataBrightBlue -= increment;
					}
				} else if (fuzzyDataBrightBlue > fuzzyDataDarkBlue
						&& fuzzyDataBrightBlue > fuzzyDataGreyBlue) {
					if (fuzzyDataBrightBlue - fuzzyDataDarkBlue >= difference) {
						fuzzyDataBrightBlue += increment;
						fuzzyDataDarkBlue -= increment;
					}
				}

				// Defuzzification
				int red = (int) (((fuzzyDataDarkRed * min)
						+ (fuzzyDataGreyRed * mid) + (fuzzyDataBrightRed * max)) / (fuzzyDataDarkRed
						+ fuzzyDataGreyRed + fuzzyDataBrightRed));
				int green = (int) (((fuzzyDataDarkGreen * min)
						+ (fuzzyDataGreyGreen * mid) + (fuzzyDataBrightGreen * max)) / (fuzzyDataDarkGreen
						+ fuzzyDataGreyGreen + fuzzyDataBrightGreen));
				int blue = (int) (((fuzzyDataDarkBlue * min)
						+ (fuzzyDataGreyBlue * mid) + (fuzzyDataBrightBlue * max)) / (fuzzyDataDarkBlue
						+ fuzzyDataGreyBlue + fuzzyDataBrightBlue));

				red = red < 0 ? 0 : red > 255 ? 255 : red;
				green = green < 0 ? 0 : green > 255 ? 255 : green;
				blue = blue < 0 ? 0 : blue > 255 ? 255 : blue;
				// Changing pixel with enhanced pixel value
				Color cOut = new Color(red, green, blue);
				// System.out.println(red +" "+green+" "+blue);
				inputIm.setRGB(i, j, cOut.getRGB());
			}
		}

	}

	private static float getFuzzyValue(float a, float b, float c, float d,
			float pixelV) {
		// Input member functions
		try {
			if (pixelV >= 0 && pixelV <= a) {
				return 0;
			} else if (pixelV >= a && pixelV <= b) {
				return (pixelV - a) / (b - a);
			} else if (pixelV >= b && pixelV <= c) {
				return 1;
			} else if (pixelV >= c && pixelV <= d) {
				return ((pixelV * -1) + d) / (d - c);
			} else {
				// if(pixelV>=d && pixelV<=255)
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	private static float getModifiedMembershipValue(float fuzzyValue) {
		// Input member functions
		try {
			if (fuzzyValue >= 0 && fuzzyValue <= 0.5) {
				return 4 * fuzzyValue * fuzzyValue * fuzzyValue;
			} else {
				return (1 - 4)
						* ((1 - fuzzyValue) * (1 - fuzzyValue) * (1 - fuzzyValue));
			}

		} catch (Exception e) {
			return 0;
		}

	}

	private void INTBasedContrastEnhancement(BufferedImage inputIm) {

		// Convert the image data into fuzzy domain data
		int widthIm = inputIm.getWidth();
		int heightIm = inputIm.getHeight();

		float Fe = 2, minRed = 255, minGreen = 255, minBlue = 255, maxRed = 0, maxGreen = 0, maxBlue = 0;
		for (int i = 0; i < widthIm; i++) {
			for (int j = 0; j < heightIm; j++) {
				Color c = new Color(inputIm.getRGB(i, j));

				// Finding min values
				if (c.getRed() < minRed) {
					minRed = c.getRed();
				}
				if (c.getGreen() < minGreen) {
					minGreen = c.getGreen();
				}
				if (c.getBlue() < minBlue) {
					minBlue = c.getBlue();
				}
				// Finding max values
				if (c.getRed() > maxRed) {
					maxRed = c.getRed();
				}
				if (c.getGreen() > maxGreen) {
					maxGreen = c.getGreen();
				}
				if (c.getBlue() > maxBlue) {
					maxBlue = c.getBlue();
				}
			}
		}
		float midRed = (float) ((maxRed + minRed) / 2.0), midGreen = (float) ((maxGreen + minGreen) / 2.0), midBlue = (float) ((maxBlue + minBlue) / 2.0);

		// finding Fd
		float FdRed = (float) ((maxRed - midRed) / (Math.pow(0.5, (-1 / Fe)) - 1));
		float FdGreen = (float) ((maxGreen - midGreen) / (Math.pow(0.5,
				(-1 / Fe)) - 1));
		float FdBlue = (float) ((maxBlue - midBlue) / (Math.pow(0.5, (-1 / Fe)) - 1));

		for (int i = 0; i < widthIm; i++) {
			for (int j = 0; j < heightIm; j++) {
				Color c = new Color(inputIm.getRGB(i, j));

				// Fuzzification pixel wise
				// Membership function
				float fuzzyDataRed = (float) Math.pow(
						1 + ((maxRed - c.getRed()) / FdRed), -Fe);
				float fuzzyDataGreen = (float) Math.pow(
						1 + ((maxGreen - c.getGreen()) / FdGreen), -Fe);
				float fuzzyDataBlue = (float) Math.pow(
						1 + ((maxBlue - c.getBlue()) / FdBlue), -Fe);

				// Membership modification
				// INT operator Technique
				fuzzyDataRed = fuzzyDataRed >= 0 && fuzzyDataRed <= 0.5 ? (2 * fuzzyDataRed * fuzzyDataRed)
						: (1 - 2 * (1 - fuzzyDataRed) * (1 - fuzzyDataRed));
				fuzzyDataGreen = fuzzyDataGreen >= 0 && fuzzyDataGreen <= 0.5 ? (2 * fuzzyDataGreen * fuzzyDataGreen)
						: (1 - 2 * (1 - fuzzyDataGreen) * (1 - fuzzyDataGreen));
				fuzzyDataBlue = fuzzyDataBlue >= 0 && fuzzyDataBlue <= 0.5 ? (2 * fuzzyDataBlue * fuzzyDataBlue)
						: (1 - 2 * (1 - fuzzyDataBlue) * (1 - fuzzyDataBlue));

				// Defuzzification
				int red = (int) (maxRed
						- (FdRed * (Math.pow(fuzzyDataRed, (-1 / Fe)))) + FdRed);
				int green = (int) (maxGreen
						- (FdGreen * (Math.pow(fuzzyDataGreen, (-1 / Fe)))) + FdGreen);
				int blue = (int) (maxBlue
						- (FdBlue * (Math.pow(fuzzyDataBlue, (-1 / Fe)))) + FdBlue);

				red = red < 0 ? 0 : red > 255 ? 255 : red;
				green = green < 0 ? 0 : green > 255 ? 255 : green;
				blue = blue < 0 ? 0 : blue > 255 ? 255 : blue;

				// Changing pixel with enhanced pixel value
				Color cOut = new Color(red, green, blue);
				inputIm.setRGB(i, j, cOut.getRGB());
			}
		}
	}

	private void measureFuzziness(BufferedImage inputIm, String type)
			throws IOException {

		// Convert the image data into fuzzy domain data
		int widthIm = inputIm.getWidth();
		int heightIm = inputIm.getHeight();

		File f = new File("Index of fuzziness " + type + ".txt");
		if (!f.exists()) {
			f.createNewFile();
		}

		FileWriter fw = new FileWriter(f.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 0; i < widthIm; i++) {
			for (int j = 0; j < heightIm; j++) {
				Color c = new Color(inputIm.getRGB(i, j));
				// Fuzzification pixel wise
				float fuzzyValue = (float) (((c.getRed() / 255.0)
						+ (c.getGreen() / 255.0) + (c.getBlue() / 255.0)) / 3);
				float fuzziness = 0;
				// finding fuzziness
				if (fuzzyValue <= 0.5) {
					fuzziness = fuzzyValue;
				} else if (fuzzyValue > 0.5) {
					fuzziness = 1 - fuzzyValue;
				}

				// Write to file
				bw.write(fuzziness + " ");

			}
			// Write in next line
			bw.newLine();
		}

		bw.close();

	}

	private BufferedImage Histogram(BufferedImage inputIm) {

		// Convert the image data into fuzzy domain data
		int widthIm = inputIm.getWidth();
		int heightIm = inputIm.getHeight();

		// Histogram Image
		BufferedImage hist = new BufferedImage(widthIm, widthIm,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = hist.createGraphics();
		g.setBackground(Color.white);
		g.fillRect(0, 0, widthIm, widthIm);
		g.setColor(Color.blue);
		BasicStroke bs = new BasicStroke(1);
		g.setStroke(bs);

		for (int i = 0; i < widthIm; i=i+2) {
			Color c = new Color(inputIm.getRGB(i, 0));
			// Fuzzification pixel wise
			float fuzzyValue = (float) (((c.getRed() / 255.0)
					+ (c.getGreen() / 255.0) + (c.getBlue() / 255.0)) / 3);

			int lineEnd = (int) (widthIm * fuzzyValue);

			// draw line
			g.drawLine(i, widthIm, i, lineEnd);
		}

		g.dispose();
		return hist;
	}

	static BufferedImage makeCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		return new BufferedImage(cm, bi.copyData(null),
				cm.isAlphaPremultiplied(), null);

	}
}