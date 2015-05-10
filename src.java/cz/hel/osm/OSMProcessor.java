package cz.hel.osm;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import crosby.binary.file.BlockInputStream;

public class OSMProcessor {

	public static void main(String[] args) throws Exception {
		
		if (args == null || args.length != 6) {
			printUsage();
			System.exit(-1);
		}
		
		String sourceFileName = null;
		String wayKey = null;
		String waySpec = null;;
		
		try {
			for (int i = 0; i < args.length; i = i + 2) {
				String arg = args[i];
				switch (arg) {
				case "-sourceFile":
					sourceFileName = args[i + 1];
					break;
				case "-wayKey":
					wayKey = args[i + 1];
					break;
				case "-waySpec":
					waySpec = args[i + 1];
					break;
				default:
					throw new IllegalArgumentException();
				}
				
			}
		} catch (Exception e) {
			printUsage();
			System.exit(-1);
		}
		
		System.out.println(String.format("Configuration : source file=%s, way key=%s, way specification=%s.", sourceFileName, wayKey, waySpec));
		

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.print("processing");
					while(true) {
						Thread.sleep(1 * 1000);
						System.out.print(".");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(-1);

			}
		});
		thread.start();

		File map = new File(sourceFileName);
		InputStream is = new FileInputStream(map);
		
		WayParser wp = new WayParser(wayKey, waySpec);
		new BlockInputStream(is, wp).process();
		
		Set<Long> nodeIdsSet = new HashSet<>();
		for (List<Long> nodeIds : wp.nodeIds) {
			nodeIdsSet.addAll(nodeIds);
		}
		
		NodeParser np = new NodeParser(nodeIdsSet);
		is = new FileInputStream(map);
		new BlockInputStream(is, np).process();
		is.close();
		
		assert nodeIdsSet.size() == np.found.size();
		
		double distance = 0;
		for (List<Long> nodeIds : wp.nodeIds) {

			GPSCoordinates lastGps = np.found.get(nodeIds.get(0));
			for (int i = 1; i < nodeIds.size(); i++) {
				GPSCoordinates actual = np.found.get(nodeIds.get(i));
				
				distance += GPSDistance.getGPSDistance(lastGps, actual);
				
				lastGps = actual;
			}
		}
		
		thread.stop();
		System.out.println();
		System.out.println("total distance : " + distance + " km");
		
	}

	private static void printUsage() {
		System.out.println("Usage: params [-sourceFile] [-wayKey] [-waySpec] are all mandatory.");
	}
}
