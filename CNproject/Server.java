import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server {

	static int p0 = 0;
    	static String fname = "";

	public static void main(String[] args) throws Exception {

		
        //Read from config file
        
        Properties prop = new Properties();
        InputStream input = null;
	String port0 = "";
        
        
        try {
            
            input = new FileInputStream("C:/mywork/config.properties");
            
            // load a properties file
            prop.load(input);
            
            // get the property value and print it out
	    port0 = prop.getProperty("0");
                        
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
	p0 = Integer.parseInt(port0);
	int sPort = p0;


		System.out.println("The server is running."); 
        	ServerSocket listener = new ServerSocket(sPort);
        	fname = args[0];
			int clientNum = 1;
        	try {
            		while(true) {
                		new Handler(listener.accept(),clientNum).start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
            			}
        	} finally {
            		listener.close();
        	} 
 
    	}


	// Handler thread class for multiple clients
    	private static class Handler extends Thread {
        	private Socket connection;
        	private int no;		//The index number of the client

        	public Handler(Socket connection, int no) {
            		this.connection = connection;
	    		this.no = no;
        	}

        public void run() {
 		try{
			
                    //Split a file
                    
                    String FILE_NAME = "C:/mywork/";
            
                    FILE_NAME = FILE_NAME.concat(fname);
                                
                    String FILE_NAME1 = "C:/mywork/Server/client.";
                    String[] out = fname.split("\\.");
                    FILE_NAME1 = FILE_NAME1.concat(out[1]);                                    
                                       
                    
                    File inputFile = new File(FILE_NAME);
                   
                    
                    double size = 100000;                       
                       
                   
                    
                    FileInputStream inputStream;
                    String newFileName;
                    FileOutputStream filePart;
                    int fileSize = (int) inputFile.length();
                    int chunks = 0, read = 0, readLength = (int) size;
                    byte[] byteChunkPart;
                    try {
                        inputStream = new FileInputStream(inputFile);
                        while (fileSize > 0) {
                            if (fileSize <= 100000) {
                                readLength = fileSize;
                            }
                            byteChunkPart = new byte[readLength];
                            read = inputStream.read(byteChunkPart, 0, readLength);
                            fileSize -= read;
                            assert (read == byteChunkPart.length);
                            chunks++;
                            newFileName = FILE_NAME1 + ".part"
                            + Integer.toString(chunks - 1);
                            filePart = new FileOutputStream(new File(newFileName));
                            filePart.write(byteChunkPart);
                            filePart.flush();
                            filePart.close();
                            byteChunkPart = null;
                            filePart = null;
                        }
                        inputStream.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    
            
                    System.out.println("The total number of chunks are:" + chunks);
            
                    //Send number of Chunks
            
                               
                    ObjectOutputStream moos = new ObjectOutputStream(connection.getOutputStream());
                    moos.writeObject(no);           
                               
                               
                    ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
                    oos.writeObject(chunks);
            
            
                    //Send file type
                    ObjectOutputStream fos = new ObjectOutputStream(connection.getOutputStream());
                    fos.writeObject(out[1]);
            
            
                    //Send the split files
                    
                    String directory = "C:/mywork/Server";
                    
                    File[] files = new File(directory).listFiles();
                                        
                    
                    java.util.List<java.io.File> newfiles = new ArrayList<>();
                    
                    if (no == 1)
                    {
                    
                    for (int i = 0; i <chunks; i++)
                    {
                        String name1 = files[i].getName();
                        int a = Character.getNumericValue(name1.charAt(name1.length() - 1));
                        
                        if ((a%5) == 0)
                        {
                            newfiles.add(files[i]);
                                                        
                        }
                        
                    }
                    
                    }
                    
                    if (no == 2)
                    {
                        
                        for (int i = 0; i <chunks; i++)
                        {
                            String name1 = files[i].getName();                            
                            int a = Character.getNumericValue(name1.charAt(name1.length() - 1));
                            
                            if ((a%5) == 1)
                            {
                                newfiles.add(files[i]);
                                                                
                            }
                            
                        }
                        
                    }
                    
                    if (no == 3)
                    {
                        
                        for (int i = 0; i <chunks; i++)
                        {
                            String name1 = files[i].getName();                            
                            int a = Character.getNumericValue(name1.charAt(name1.length() - 1));
                            
                            if ((a%5) == 2)
                            {
                                newfiles.add(files[i]);
                                
                                
                            }
                            
                        }
                        
                    }
            
                    if (no == 4)
                    {
                
                    for (int i = 0; i <chunks; i++)
                    {
                    String name1 = files[i].getName();                  
                    int a = Character.getNumericValue(name1.charAt(name1.length() - 1));
                    
                    if ((a%5) == 3)
                    {
                        newfiles.add(files[i]);
                        
                        
                    }
                    
                    }
                
                    }
            
                    if (no == 5)
                    {
                
                        for (int i = 0; i <chunks; i++)
                    {
                    String name1 = files[i].getName();                                        
                    int a = Character.getNumericValue(name1.charAt(name1.length() - 1));
                    
                    if ((a%5) == 4)
                    {
                        newfiles.add(files[i]);
                        
                        
                    }
                    
                    }
                
                    }
            
            
            
            
                    BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
                    DataOutputStream dos = new DataOutputStream(bos);
                    
                    dos.writeInt(files.length);
                    
                    for(File file : newfiles)
                    {
                        long length = file.length();
                        dos.writeLong(length);
                        
                        String name = file.getName();
                        
                        
                        dos.writeUTF(name);
                        
                        FileInputStream fis = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        
                        
                        int theByte = 0;
                        while((theByte = bis.read()) != -1) bos.write(theByte);
                                
                        System.out.println("Sent " + name);
                        
                        bis.close();
                    }
                    
                    dos.close();                   
                                      
                    
				
			
		}
		catch(IOException ioException){
			System.out.println("Disconnect with Client " + no);
		}
		finally{
			//Close connections
			try{
				
				connection.close();
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}
		}
	}


}
    
}
