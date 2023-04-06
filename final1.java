import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class final1 {
	static int  count_method_in_folders=0;
    static int count_generated_folders=0;
    public static void main(String[] args) {
    	
    	
    
        // Set the directory path
        String directoryPath = "D:\\Jatin\\Downloads\\java-large.tar\\java-large\\java-large\\validation";
        // Set the output file path
        String outputFilePath = "C:\\Users\\91829\\Desktop\\mini_project\\output_Small\\validation\\";
        

        try {
        	 FileWriter fileWriter = new FileWriter(outputFilePath+"output.txt");
        	 Set<String> set = new HashSet<String>();
        	 Set<String> function_set = new HashSet<String>();
        	 
            // Create a new ParserConfiguration instance and set a symbol resolver
            ParserConfiguration parserConfiguration = new ParserConfiguration();
            parserConfiguration.setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));

            // Create a new JavaParser instance with the ParserConfiguration
            JavaParser javaParser = new JavaParser(parserConfiguration);

            // Get all files in the directory
            File directory = new File(directoryPath);
            try {
           listFilesRecursive(directory,javaParser,set,function_set,outputFilePath);
//         
            }
            catch(Exception e) {
                System.out.println("10");

            }
           
           
                fileWriter.write("count of distincts functions in folders: "+count_method_in_folders + "\n");  
                fileWriter.write("count all folders created: "+count_generated_folders + "\n");  

            
            fileWriter.close();
            
        } catch (Exception e) {
            // Handle the exception
            System.out.println("Error writing to output file.");
        }
        
//        System.out.println(count_folder);
        
    }
    
    private static void processJavaFile(File javaFile, JavaParser javaParser, Set<String> set, Set<String> function_set, String outputFilePath) throws IOException {
        // Parse the Java file
        CompilationUnit cu = javaParser.parse(javaFile).getResult().orElse(null);
        if (cu == null) {
            return;
        }

        // Get all method declarations in the file
        List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

        // Loop through all method declarations
        for (MethodDeclaration method : methods) {
            // Get all method calls in the method
            try {
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
                                count_generated_folders+=1;
                            }
//                            System.out.println(fullyQualifiedName);
                            String newDataPointFile = outputFilePath+fullyQualifiedName+"\\"+method.getName()+".java";
//                            System.out.println(newDataPointFile);
                            if(function_set.contains(method.toString())==false) {
                                count_method_in_folders+=1;
                                function_set.add(method.toString());
                            }
                            writeFile(newDataPointFile, method.toString());
                        }
                    } catch (UnsolvedSymbolException e) {
//                        System.out.println("1");
                        
                    }
                    catch ( UnsupportedOperationException e){
//                        System.out.println("2");
                    }
                    catch (java.lang.RuntimeException e) {
//                        System.out.println("3");
                    }
                    catch(Exception e) {
//                        System.out.println("4");

                    }
                }
            } catch (Exception e) {
//                System.out.println("5");
            }
        }
    }

    private static void listFilesRecursive(File directory, JavaParser javaParser, Set<String> set, Set<String> function_set, String outputFilePath) throws IOException {
        try {
    	for (File file : directory.listFiles()) {
    		try {
            if (file.isDirectory()) {
            	try {
                listFilesRecursive(file, javaParser, set, function_set, outputFilePath);
            	}
            	catch(Exception e) {
            		System.out.println("helo"+file);
            	}
            } else if (file.getName().endsWith(".java")) {
            	try {
                processJavaFile(file, javaParser, set, function_set, outputFilePath);
            	}
             catch(Exception e){
            	 System.out.println("hell"+file);
             }
               }
    		}
    		catch(Exception e) {
//                System.out.println("7");

    		}
        }
        }
        catch(Exception e) {
            System.out.println("8");

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
