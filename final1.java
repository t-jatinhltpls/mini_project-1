import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class final1 {
    public static void main(String[] args) {
        // Set the directory path
        String directoryPath = "C:\\Users\\91829\\Desktop\\mini_project\\input\\";
        // Set the output file path
        String outputFilePath = "C:\\Users\\91829\\Desktop\\mini_project\\output\\";

        try {
        	 FileWriter fileWriter = new FileWriter(outputFilePath+"output.txt");
        	 Set<String> set = new HashSet<String>();
        	 
            // Create a new ParserConfiguration instance and set a symbol resolver
            ParserConfiguration parserConfiguration = new ParserConfiguration();
            parserConfiguration.setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));

            // Create a new JavaParser instance with the ParserConfiguration
            JavaParser javaParser = new JavaParser(parserConfiguration);

            // Get all files in the directory
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            
            

            // Loop through all files in the directory
            for (File javaFile : files) {
                if (javaFile.getName().endsWith(".java")) {
                    try {
                        // Parse the Java file
                        CompilationUnit cu = javaParser.parse(javaFile).getResult().get();

                        // Get all method declarations in the file
                        List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

                        // Loop through all method declarations
                        for (MethodDeclaration method : methods) {
                            // Get all method calls in the method
                            List<MethodCallExpr> methodCalls = method.findAll(MethodCallExpr.class);

                            // Loop through all method calls
                            for (MethodCallExpr methodCall : methodCalls) {
                                // Get the fully qualified name of the method call
                                try {
                                    String fullyQualifiedName = methodCall.resolve().getQualifiedName();
                                    // Write the fully qualified name to the output file
                                    if (fullyQualifiedName.startsWith("java")) {
                                        set.add(fullyQualifiedName);
                                        File methodFolder = new File(outputFilePath+fullyQualifiedName);
                                        if(!methodFolder.exists()) {
                                        	methodFolder.mkdir();
                                        }
                                  	    System.out.println(fullyQualifiedName);
                                  	    String newDataPointFile = outputFilePath+fullyQualifiedName+"\\"+method.getName()+".java";
                                  	    System.out.println(newDataPointFile);
                                  	    writeFile(newDataPointFile, method.toString());  
                                    }
                                } catch (UnsolvedSymbolException e) {
                                    // Handle the exception
//                                    System.out.println("Could not resolve method call: " + methodCall);
                                }
                                catch ( UnsupportedOperationException e){
//                                	 System.out.println("UnsupportedOperationException for:  " + methodCall);
                                }
                                
                                catch (java.lang.RuntimeException e) {
//                                	System.out.println("java.lang.RuntimeException for: "+method.getSignature());
                                }
                                
                                
                            }
                        }
                        
                        
                        
                    } catch (IOException e) {
                        // Handle the exception
//                        System.out.println("Error parsing file: " + javaFile.getName());
                    }
                }
            }
            for (String str : set) {
                fileWriter.write(str + "\n");            
            }
            fileWriter.close();
            
        } catch (IOException e) {
            // Handle the exception
            System.out.println("Error writing to output file.");
        }
        
        
    }
    
    private static void writeFile(String filename, String contents) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
          writer.write(contents);
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        
      }
}
