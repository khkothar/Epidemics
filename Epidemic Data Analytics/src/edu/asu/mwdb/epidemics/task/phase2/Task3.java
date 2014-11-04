package edu.asu.mwdb.epidemics.task.phase2;

import edu.asu.mwdb.epidemics.epidemic.analysis.LDA;
import edu.asu.mwdb.epidemics.epidemic.analysis.SVD;
import edu.asu.mwdb.epidemics.epidemic.analysis.Task3C;

public class Task3 {

	public static void main(String[] args) {
		try {			
			boolean wrongArg = false;
			if(!(args.length==3||args.length ==4||args.length==7))
				wrongArg = true;
			if(wrongArg){
				System.err.println("Please enter correct command line arguments:");
				switch(args[0]) {
				case "3a":					
					System.err.println("Usage: Task3a <Choice of Program to Run (3a-3f)> <Epidemic word file (for simulation files)>"
							+ " <R semantics)>");
					break;
				case "3b":
					System.err.println("Usage: Task3b <Choice of Program to Run (3a-3f)> <Epidemic word file (for simulation files)>"
							+ " <R semantics)>");
					break;
				case "3c":
					System.err.println("Usage: Task3c <Choice of Program to Run (3a-3f)> <Epidemic simulation files>"
							+ " <R semantics)> + <Similarity Measure (1-8)>");
					break;
				case "3d":
				case "3e":
				case "3f":
					System.err.println("Usage: Task3d <Choice of Program to Run (3a-3f)> <Epidemic Simulation File (Query File)> "
							+ "<Epidemic Simulation Files Directory");
					System.err.println(" <R semantics)> <Top K> <Choice (3a-3c)> <Similarity Measure (1-8)>");
					break;
				}
				
				System.exit(0);
			}
			Task3 task3 = new Task3();
			task3.driverProgramforTask3(args);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*@params
	 * args[0] - Choice 3a-3b
	 * args[1] - Epidemic Word File location (simulation dictionary/epidemic_word_file.csv)
	 * args[2] - R semantics (integer)
	 * 
	 * args[0] - Choice 3c
	 * args[1] - Epidemic Simulation File (/InputCSVs)
	 * args[2] - R semantics (integer)
	 * args[3] - Similarity Measure(1-8)
	 * 
	 * args[0] - Choice 3d-3f
	 * args[1] - Query.csv
	 * args[2] - Epidemic Simulation file directory
	 * args[3] - R semantics (integer)
	 * args[4] - Top K files (integer)
	 * args[5] - Choice 3a-3c
	 * args[6] - Similarity Measure (1-8)
	*/
	private void driverProgramforTask3(String args[]) throws Exception{
		switch(args[0]) {
			case "3a" :
				SVD svd = new SVD();
				svd.createInputMatrixToFile(args[1]);
				System.out.println("Matrix file created !!");
				svd.svDecomposition(Integer.parseInt(args[2]),false);
				System.out.println("SVD matrices created!!");
				svd.createLatentSemanticScoreFile("Data/U.csv","Data/SVDSemanticScore.csv",SVD.fileNameAccessList, -1);
				System.out.println("Score file created !!!");
				break;
			case "3b" :
				LDA lda = new LDA();
				lda.computeLDA("simulation dictionary/epidemic_word_file.csv",Integer.parseInt(args[2]), false);
				break;
			case "3c" :
				Task3C task3c = new Task3C();
				task3c.executeLODUTask3C(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), -1, false, "Data/U.csv");
				break;
			case "3d" :
			case "3e" :
			case "3f" : 
				Task3d task3d = new Task3d();
				task3d.execute3D(args[1], Integer.parseInt(args[3]), Integer.parseInt(args[4]), args[5], args[2], 
						Integer.parseInt(args[6]));
				break;
		}
	}

}
