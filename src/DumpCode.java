//import java.awt.Color;
//import java.awt.image.BufferedImage;
//import java.awt.image.ColorModel;
//import java.io.File;
//
//import javax.imageio.ImageIO;
//
//
public class DumpCode {
//	public static void main(String[] args) {
//
//		ImageContrastEnhancement ice = new ImageContrastEnhancement();
//		BufferedImage sourceImage = null;
//		String imagePath = "input.jpg";
//		if (args.length == 2) {
//			imagePath = args[1];
//		}
//
//		try {
//
//			// Load Image
//			sourceImage = ImageIO.read(new File(imagePath));
//
//			// Processing
//		//	BufferedImage ruleBasedImage = makeCopy(sourceImage);
//		//	ice.RuleBasedContrastEnhancement(ruleBasedImage);
//			BufferedImage intBased = ice
//					.INTBasedContrastEnhancement(makeCopy(sourceImage));
//
//			// Save Images
//			File fRule = new File("RuleBased.jpg");
//			ImageIO.write(ruleBasedImage, "JPG", fRule);
//
//			// Show Histograms
//			ice.Histogram(sourceImage);
//			ice.Histogram(ruleBasedImage);
//			ice.Histogram(intBased);
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
//
//	private void RuleBasedContrastEnhancement(BufferedImage inputIm) {
//
//		// Convert the image data into fuzzy domain data
//		int widthIm = inputIm.getWidth();
//		int heightIm = inputIm.getHeight();
//
//		float minRed = 255, minGreen = 255, minBlue = 255;
//		float maxRed = 0, maxGreen = 0, maxBlue = 0;
//
//		// find min and max
//		for (int i = 0; i < widthIm; i++) {
//			for (int j = 0; j < heightIm; j++) {
//				Color c = new Color(inputIm.getRGB(i, j));
//
//				// find min
//				if (minRed > c.getRed()) {
//					minRed = c.getRed();
//				}
//				if (minGreen > c.getGreen()) {
//					minGreen = c.getGreen();
//				}
//				if (minBlue > c.getBlue()) {
//					minBlue = c.getBlue();
//				}
//
//				// find max
//				if (maxRed < c.getRed()) {
//					maxRed = c.getRed();
//				}
//				if (maxGreen < c.getGreen()) {
//					maxGreen = c.getGreen();
//				}
//				if (maxBlue < c.getBlue()) {
//					maxBlue = c.getBlue();
//				}
//
//			}
//		}
//
//		// Finding Middle value
//		float midRed = (minRed + maxRed) / 2, midGreen = (minGreen + maxGreen) / 2, midBlue = (minBlue + maxBlue) / 2;
//
//		float[][] fuzzyDataRed = new float[widthIm][heightIm];
//		float[][] fuzzyDataGreen = new float[widthIm][heightIm];
//		float[][] fuzzyDataBlue = new float[widthIm][heightIm];
//
//		for (int i = 0; i < widthIm; i++) {
//			for (int j = 0; j < heightIm; j++) {
//				Color c = new Color(inputIm.getRGB(i, j));
//
//				// Red Value fuzzification
//				try {
//					if (c.getRed() <= 0) {
//						fuzzyDataRed[i][j] = 0;
//					} else if (c.getRed() < midRed) {
//						fuzzyDataRed[i][j] = ((1 / (midRed - minRed) * minRed) + (1 / (midRed - minRed)))
//								* c.getRed();
//					} else if (c.getRed() < maxRed) {
//						fuzzyDataRed[i][j] = ((1 / (maxRed - minRed) * maxRed) + (1 / (maxRed - maxRed)))
//								* c.getRed();
//					}
//				} catch (Exception e) {
//
//				}
//
//				// Green Value fuzzification
//				try {
//					if (c.getGreen() <= 0) {
//						fuzzyDataGreen[i][j] = 0;
//					} else if (c.getGreen() < midGreen) {
//						fuzzyDataGreen[i][j] = ((1 / (midGreen - minGreen) * minGreen) + (1 / (midGreen - minGreen)))
//								* c.getGreen();
//					} else if (c.getGreen() < maxGreen) {
//						fuzzyDataGreen[i][j] = ((1 / (maxGreen - minGreen) * maxGreen) + (1 / (maxGreen - maxGreen)))
//								* c.getGreen();
//					}
//				} catch (Exception e) {
//
//				}
//
//				// Blue Value fuzzification
//				try {
//					if (c.getBlue() <= 0) {
//						fuzzyDataBlue[i][j] = 0;
//					} else if (c.getBlue() < midBlue) {
//						fuzzyDataBlue[i][j] = ((1 / (midBlue - minBlue) * minBlue) + (1 / (midBlue - minBlue)))
//								* c.getBlue();
//					} else if (c.getBlue() < maxBlue) {
//						fuzzyDataBlue[i][j] = ((1 / (maxBlue - minBlue) * maxBlue) + (1 / (maxBlue - maxBlue)))
//								* c.getBlue();
//					}
//
//				} catch (Exception e) {
//
//				}
//
//			}
//		}
//
//		// Membership modification
//		for (int i = 0; i < widthIm; i++) {
//			for (int j = 0; j < heightIm; j++) {
//				Color c = new Color(inputIm.getRGB(i, j));
//
//				// Red
//				if (c.getRed() <= 0) {
//					fuzzyDataRed[i][j] = 0;
//				} else if (c.getRed() >= 255) {
//					fuzzyDataRed[i][j] = 255;
//				} else {
//					if (fuzzyDataRed[i][j] >= 0 && fuzzyDataRed[i][j] < 0.5) {
//						fuzzyDataRed[i][j] = 2 * fuzzyDataRed[i][j]
//								* fuzzyDataRed[i][j];
//					} else if (fuzzyDataRed[i][j] >= 0.5
//							&& fuzzyDataRed[i][j] < 1) {
//						fuzzyDataRed[i][j] = 1 - (2 * (1 - fuzzyDataRed[i][j]) * (1 - fuzzyDataRed[i][j]));
//					}
//				}
//
//				// Green
//				if (c.getGreen() <= 0) {
//					fuzzyDataGreen[i][j] = 0;
//				} else if (c.getGreen() >= 255) {
//					fuzzyDataGreen[i][j] = 255;
//				} else {
//					if (fuzzyDataGreen[i][j] >= 0 && fuzzyDataGreen[i][j] < 0.5) {
//						fuzzyDataGreen[i][j] = 2 * fuzzyDataGreen[i][j]
//								* fuzzyDataGreen[i][j];
//					} else if (fuzzyDataGreen[i][j] >= 0.5
//							&& fuzzyDataGreen[i][j] < 1) {
//						fuzzyDataGreen[i][j] = 1 - (2 * (1 - fuzzyDataGreen[i][j]) * (1 - fuzzyDataGreen[i][j]));
//					}
//
//				}
//
//				// Blue
//				if (c.getBlue() <= 0) {
//					fuzzyDataBlue[i][j] = 0;
//				} else if (c.getBlue() >= 255) {
//					fuzzyDataBlue[i][j] = 255;
//				} else {
//					if (fuzzyDataBlue[i][j] >= 0 && fuzzyDataBlue[i][j] < 0.5) {
//						fuzzyDataBlue[i][j] = 2 * fuzzyDataBlue[i][j]
//								* fuzzyDataBlue[i][j];
//					} else if (fuzzyDataBlue[i][j] >= 0.5
//							&& fuzzyDataBlue[i][j] < 1) {
//						fuzzyDataBlue[i][j] = 1 - (2 * (1 - fuzzyDataBlue[i][j]) * (1 - fuzzyDataBlue[i][j]));
//					}
//				}
//
//			}
//		}
//
//		// Defuzzification
//		for (int i = 0; i < widthIm; i++) {
//			for (int j = 0; j < heightIm; j++) {
//
//				// Red
//				int red = 0;
//				if (fuzzyDataRed[i][j] <= 0) {
//					red = 0;
//				} else if (fuzzyDataRed[i][j] >= 255) {
//					red = 255;
//				} else {
//					if (fuzzyDataRed[i][j] >= 0 && fuzzyDataRed[i][j] < 0.5) {
//						red = (int) ((midRed - minRed) * fuzzyDataRed[i][j] + minRed);
//					} else if (fuzzyDataRed[i][j] >= 0.5
//							&& fuzzyDataRed[i][j] < 1) {
//						red = (int) ((maxRed - midRed) * fuzzyDataRed[i][j] + midRed);
//					}
//				}
//
//				// Green
//				int green = 0;
//				if (fuzzyDataGreen[i][j] <= 0) {
//					green = 0;
//				} else if (fuzzyDataGreen[i][j] >= 255) {
//					green = 255;
//				} else {
//					if (fuzzyDataGreen[i][j] >= 0 && fuzzyDataGreen[i][j] < 0.5) {
//						green = (int) ((midGreen - minGreen)
//								* fuzzyDataGreen[i][j] + minGreen);
//					} else if (fuzzyDataGreen[i][j] >= 0.5
//							&& fuzzyDataGreen[i][j] < 1) {
//						green = (int) ((maxGreen - midGreen)
//								* fuzzyDataGreen[i][j] + midGreen);
//					}
//				}
//
//				// Blue
//				int blue = 0;
//				if (fuzzyDataBlue[i][j] <= 0) {
//					blue = 0;
//				} else if (fuzzyDataBlue[i][j] >= 255) {
//					blue = 255;
//				} else {
//					if (fuzzyDataBlue[i][j] >= 0 && fuzzyDataBlue[i][j] < 0.5) {
//						blue = (int) ((midBlue - minBlue) * fuzzyDataBlue[i][j] + minBlue);
//					} else if (fuzzyDataBlue[i][j] >= 0.5
//							&& fuzzyDataBlue[i][j] < 1) {
//						blue = (int) ((maxBlue - midBlue) * fuzzyDataBlue[i][j] + midBlue);
//					}
//				}
//
//				// Changing pixel with enhanced pixel value
//				Color c = new Color(red, green, blue);
//				inputIm.setRGB(i, j, c.getRGB());
//			}
//		}
//
//	}
//
//	private BufferedImage INTBasedContrastEnhancement(BufferedImage input) {
//
//		return null;
//	}
//
//	private void Histogram(BufferedImage image) {
//
//	}
//
//	static BufferedImage makeCopy(BufferedImage bi) {
//		ColorModel cm = bi.getColorModel();
//		return new BufferedImage(cm, bi.copyData(null),
//				cm.isAlphaPremultiplied(), null);
//
//	}
}
