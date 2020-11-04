package org.labs.workshops.checker.Checker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Checker {
	private static final int THREADS = 40;
	private static final int TASKS = 1000;
	
	private static final String INPUT_FILE = "D:\\combo.txt";
	private static final String OUTPUT_FILE = "D:\\out.txt";
	public static final String LOGIN_URL = "http://localhost:5002/validateLogin";


	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		List<Future<String>> tareas = new ArrayList<>();
		Long ini = System.currentTimeMillis();
		
		BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE));
		FileWriter out = new FileWriter(OUTPUT_FILE);
		String linea;
		int scheduledTasks = 0;
		int totalProcessed = 0;
		while ((linea = reader.readLine()) != null) {
			totalProcessed++;
			if (totalProcessed % 10000 == 0) {
				Long totalTime =  System.currentTimeMillis() - ini; 
				System.out.println("processed "+(totalProcessed / 1000)+"K in "+(totalTime/1000)+"s ("+totalTime+"ms)");
			}
	 		String[] tokens = linea.split(":");
			if (tokens.length > 1) {
				Future<String> futureAnswer = executor.submit(new CheckerTask(tokens[0], tokens[1]));
				tareas.add(futureAnswer);
				scheduledTasks++;
			}
			if(scheduledTasks >= TASKS) {
				//espero todas
				for (Future<String> f:tareas) {
					String r = f.get();
					if (r != null) {
						System.out.println(r);
					}
				}
				//Borro lista
				tareas.clear();
				//reinicio contador
				scheduledTasks = 0;
			}
		}
		//proceso las pendientes
		for (Future<String> f:tareas) {
			String r = f.get();
			if (r != null) {
				System.out.println(r);
			}
		}
		executor.shutdown();
		reader.close();
		out.close();

	}

}
