import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.lang.*;

public class Client {

	public void Client() {}
    
    static Hashtable<Integer, String> ownID = new Hashtable<Integer, String>();
    static int chunks = 0;
    static String ftype = "";
    static int clientno = 0;

    static int p0 = 0;
    static int p1 = 0;
    static int p2 = 0;
    static int p3 = 0;
    static int p4 = 0;
    static int p5 = 0;
    static int u1 = 0;
    static int u2 = 0;
    static int u3 = 0;
    static int u4 = 0;
    static int u5 = 0;
    static int d1 = 0;
    static int d2 = 0;
    static int d3 = 0;
    static int d4 = 0;
    static int d5 = 0;
    
    

    //Receive from the Server
	void run()
	{
        
        
		try{
			
            String hostDomain = "localhost";
            int port = p0;
            
            Socket socket = new Socket(InetAddress.getByName(hostDomain), port);
            
            InputStream kis = socket.getInputStream();
            ObjectInputStream kois = new ObjectInputStream(kis);
            clientno = (int) kois.readObject();     //Receive client number
                       
                      
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            chunks = (int) ois.readObject();       //Receive chunks
                                   
                       
            InputStream tis = socket.getInputStream();
            ObjectInputStream tois = new ObjectInputStream(tis);
            ftype = (String) tois.readObject();   //Receive file type
            
            String dirPath = "";
            
            if (clientno == 1)
            {
                dirPath = "C:/mywork/Client1";
            }
            
            else if (clientno == 2)
            {
                dirPath = "C:/mywork/Client2";
            }
            
            else if (clientno == 3)
            {
                dirPath = "C:/mywork/Client3";
            }
            
            else if (clientno == 4)
            {
                dirPath = "C:/mywork/Client4";
            }
            
            else if (clientno == 5)
            {
                dirPath = "C:/mywork/Client5";
            }
            
            
            //Recieve the files into Client Folder
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);
                
                
            int filesCount = dis.readInt();
            File[] files = new File[filesCount];
                
            for(int i = 0; i < filesCount; i++)
            {
                    
                    long fileLength = dis.readLong();
                    String fileName = dis.readUTF();
                    
                    files[i] = new File(dirPath + "/" + fileName);
		    System.out.println("Received chunk:" + fileName);
                    
                    FileOutputStream fos = new FileOutputStream(files[i]);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    
                    for(int j = 0; j < fileLength; j++) bos.write(bis.read());
                    
                    bos.flush();
                    bos.close();
                    
                                        
                    //Update Chunk ID                   
		    
                    File[] c1f = new File(dirPath).listFiles();
                    for (File file : c1f)
                    {
                        String name = file.getName();
			

                        
                            int length = name.length();
                	    String result = "";
                            for (int k = (length - 4); k < length; k++) {
                                Character character = name.charAt(k);
				if (Character.isDigit(character)) {
                                result = result+character;
				    
                                }
             }
                        try {                            
			    int xy = Integer.valueOf(result);			       
			    ownID.put(xy,name);
				} catch (NumberFormatException nfe)
  				{ System.out.println("Number format exception");
				}
			    
				
                        
                    }
                    
		  
                    
                }

            
        }catch(EOFException exp) {
            System.out.println("EOF EXCEPTION CAUGHT here");
        }
        catch(ClassNotFoundException CNF)
        {
            System.out.println("CNF Exception caught");
        }
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
        }

	}
    
    
    //Overoaded method to Receive chunks from the download neighbour
    
    void run (int cPort)
    {
        while (true)
        {
        
        try{
            
            String dirPath = "";
            
            if (clientno == 1)
            {
                dirPath = "C:/mywork/Client1";
            }
            
            else if (clientno == 2)
            {
                dirPath = "C:/mywork/Client2";
            }
            
            else if (clientno == 3)
            {
                dirPath = "C:/mywork/Client3";
            }
            
            else if (clientno == 4)
            {
                dirPath = "C:/mywork/Client4";
            }
            
            else if (clientno == 5)
            {
                dirPath = "C:/mywork/Client5";
            }
            
            String hostDomain = "localhost";
            int port = cPort;
            
            int counter = 0;
            
            Socket socket = null;
            
            while (true) {
                try {
                    
                    //Check if all chunks are received
                    
                    
                    File[] files3 = new File(dirPath).listFiles();
                    
                    counter = 0;
                    for (int i = 0; i < files3.length; i ++)
                        if (files3[i] != null)
                            counter ++;
                    
                                        
                    if (counter == chunks)
                        break;
                    
                    
                    socket = new Socket(InetAddress.getByName(hostDomain), port);
                    if (socket !=null) { break; }
                }catch (IOException e) {
                    try {
                        System.out.println ("Waiting for connection..");
                        Thread.sleep(5000);
                    }catch(InterruptedException ex)
                    {
                        System.out.println("Interrupted Exception");
                    }
                }
            }
            
            //Compare download neighbor's chunk ID with own
            
            System.out.println("Requesting download neighbor's chunk ID");
            
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            Hashtable downID = (Hashtable) ois.readObject();
            
            System.out.println("Received download neighbor's chunk ID ");
	    System.out.println(downID);
                        
                        
            Enumeration e1 = ownID.keys();
            Enumeration e5 = downID.keys();
            
            
            while(e1.hasMoreElements())
            {
                int key1 = (int) e1.nextElement();
                e5 = downID.keys();
                
                while(e5.hasMoreElements())
                {
                    
                    try {
                        int key5 = (int) e5.nextElement();
                        
                        
                        
                        if (key5 == key1)
                        {
                            
                            downID.remove(key5);
                        }
                        
                    }
                    catch(NoSuchElementException ns)
                    {
                        System.out.println("NSE Caught");
                    }
                    
                    
                }
                
            }
            
            
            //Request the chunks that are not present
            
            System.out.println("Chunks that need to be received from download neighbor are:");
            System.out.println(downID);
            
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(downID);
            
            //Recieve the chunks from download neighbor into Client folder

            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);                
              
            int filesCount = dis.readInt();
            File[] files = new File[filesCount];
                
            for(int i = 0; i < filesCount; i++)
            {
                    
                    long fileLength = dis.readLong();
                    String fileName = dis.readUTF();
                    
                    files[i] = new File(dirPath + "/" + fileName);
		    System.out.println("Received chunk:" + fileName);
                    
                    FileOutputStream fos = new FileOutputStream(files[i]);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    
                    for(int j = 0; j < fileLength; j++) bos.write(bis.read());
                    
                    bos.flush();
                    bos.close();
                    
                    
                    //add all the files inside the folder Client to the chunk ID table
                    
                    File[] c1f = new File(dirPath).listFiles();
                    for (File file : c1f)
                    {
                        String name = file.getName();
                        if (!name.startsWith("."))
                        {
                            int length = name.length();
                            String result = "";
                            for (int k = (length - 4); k < length; k++) {
                                Character character = name.charAt(k);
                                if (Character.isDigit(character)) {
                                    result += character;
                                }
                            }
                            
                          int xy = Integer.parseInt(result);
                            
                            ownID.put(xy,name);
                        }
                    }
                    
                                        
            }
            
        }catch(EOFException exp) {
            System.out.println("EOF EXCEPTION CAUGHT at this place");
        }
        catch (ConnectException e) {
            System.err.println("Connection refused. You need to initiate a server first.");
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(ClassNotFoundException cs)
        {
            System.out.println("CNF EXCEPTION CAUGHT at this place");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    }
	
    
    
    //Send the requested files to Upload neighbour
    
    private static class Handler extends Thread {
        
        public Handler() {
        }
        
        public void run() {
            try{
                
                
                int sPort = 0;
                
                if (clientno == 1)
                {
                    sPort = p1;
                }
                
                else if (clientno == 2)
                {
                    sPort = p2;
                }
                
                else if (clientno == 3)
                {
                    sPort = p3;
                }
                
                else if (clientno == 4)
                {
                    sPort = p4;
                }
                
                else if (clientno == 5)
                {
                    sPort = p5;
                }
                
                ServerSocket listener = new ServerSocket(sPort);
                int clientNum = 1;
                
                Socket connection = listener.accept();
                
            
                //Comparing own Chunk ID with upload neighbor's chunk ID
                
                System.out.println("Own Chunk ID sent to upload neighbor");
                
                ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
                oos.writeObject(ownID);
                
                InputStream is = connection.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                Hashtable upID = (Hashtable) ois.readObject();
                
                System.out.println("Missing chunks that need to be sent to the upload neighbor: ");
                System.out.println(upID);
                
                
                    
                //Send the requested files
                    
                String directory = "";
                
                if (clientno == 1)
                {
                    directory = "C:/mywork/Client1";
                }
                
                else if (clientno == 2)
                {
                    directory = "C:/mywork/Client2";
                }
                
                else if (clientno == 3)
                {
                    directory = "C:/mywork/Client3";
                }
                
                else if (clientno == 4)
                {
                    directory = "C:/mywork/Client4";
                }
                
                else if (clientno == 5)
                {
                    directory = "C:/mywork/Client5";
                }
                    
                File[] files = new File(directory).listFiles();
                
                
                java.util.List<java.io.File> newfiles = new ArrayList<>();
                
                Enumeration e2 = upID.elements();
                
                
                //create a new list of files that should be sent
                    
                for (File file : files)
                {
                        try {
                            
                        String name1 = file.getName();
                        //System.out.println("name1: " + name1);
                        e2 = upID.elements();
            
                        while(e2.hasMoreElements())
                        {
                            String name2 = (String) e2.nextElement();
                            //System.out.println("name2: " + name2);
                            if (name1.equals(name2))
                            {
                            
                            newfiles.add(file);
                            //System.out.println("Sending this file:" + name1);
                                
                            }
                        }
                        
                        }catch(NoSuchElementException n)
                        {
                            System.out.println("NSE Caught");
                        }
                        catch(ArrayIndexOutOfBoundsException a)
                        {
                            System.out.println("AIO Caught");
                        }
                        
                }
                
                
                BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
                DataOutputStream dos = new DataOutputStream(bos);
                
                dos.writeInt(files.length);
                
                
                //Send the files in the list newfiles
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
                    
                    
                    
                    listener.close();
                    connection.close();
                
                
                
            }
            catch(ClassNotFoundException cn)
            {
                System.out.println("CNF Caught");
            }
            catch(EOFException exp) {
                System.out.println("EOF EXCEPTION CAUGHT");
            }
            catch(IOException ioException){
                System.out.println("Disconnect with Client ");
            }

            }
        }
    
    
    //main method
    public static void main(String args[])
    {
        
        //Read from config file
        
        Properties prop = new Properties();
        InputStream input = null;
	String port0 = "";
        String port1 = "";
        String port2 = "";
        String port3 = "";
        String port4 = "";
        String port5 = "";
        
        try {
            
            input = new FileInputStream("C:/mywork/config.properties");
            
            // load a properties file
            prop.load(input);
            
            // get the property value and print it out
	    port0 = prop.getProperty("0");
            port1 = prop.getProperty("1");
            port2 = prop.getProperty("2");
            port3 = prop.getProperty("3");
            port4 = prop.getProperty("4");
            port5 = prop.getProperty("5");
            
            
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
        p1 = Integer.parseInt(port1);
        p2 = Integer.parseInt(port2);
        p3 = Integer.parseInt(port3);
        p4 = Integer.parseInt(port4);
        p5 = Integer.parseInt(port5);
        
        d1 = p1%10;
        p1 = p1/10;
        u1 = p1%10;
        p1 = p1/10;
        
        d2 = p2%10;
        p2 = p2/10;
        u2 = p2%10;
        p2 = p2/10;
        
        d3 = p3%10;
        p3 = p3/10;
        u3 = p3%10;
        p3 = p3/10;
        
        d4 = p4%10;
        p4 = p4/10;
        u4 = p4%10;
        p4 = p4/10;
        
        d5 = p5%10;
        p5 = p5/10;
        u5 = p5%10;
        p5 = p5/10;
        
        
        //Download chunks from the Server
        Client client = new Client();
        client.run();
                
            
            //Send chunks to Upload neighbour
            try {
                new Handler().start();
            }
            catch(Exception e){
                System.out.println("Exception found");
            }
            
            //Download chunks from Download neighbour
            
            int cPort = 0;
            
            if (clientno == 1)
            {
                if (d1 == 1) cPort = p1;
                else if (d1 == 2) cPort = p2;
                else if (d1 == 3) cPort = p3;
                else if (d1 == 4) cPort = p4;
                else if (d1 == 5) cPort = p5;
            }
            
            else if (clientno == 2)
            {
                if (d2 == 1) cPort = p1;
                else if (d2 == 2) cPort = p2;
                else if (d2 == 3) cPort = p3;
                else if (d2 == 4) cPort = p4;
                else if (d2 == 5) cPort = p5;
            }
            
            else if (clientno == 3)
            {
                if (d3 == 1) cPort = p1;
                else if (d3 == 2) cPort = p2;
                else if (d3 == 3) cPort = p3;
                else if (d3 == 4) cPort = p4;
                else if (d3 == 5) cPort = p5;
            }
            
            else if (clientno == 4)
            {
                if (d4 == 1) cPort = p1;
                else if (d4 == 2) cPort = p2;
                else if (d4 == 3) cPort = p3;
                else if (d4 == 4) cPort = p4;
                else if (d4 == 5) cPort = p5;
            }
        
            else if (clientno == 5)
            {
                if (d5 == 1) cPort = p1;
                else if (d5 == 2) cPort = p2;
                else if (d5 == 3) cPort = p3;
                else if (d5 == 4) cPort = p4;
                else if (d5 == 5) cPort = p5;
            }
            
            
            Client client1 = new Client();
        
            try{
            client1.run(cPort);
            }catch(NullPointerException np)
            {
            System.out.println("NP Exception Caught");
            }
        
            
            //Send the remaining chunks to the Upload neighbour
            
            String dirPath = "";
            String FILE_NAME = "";
            String FILE_NAME1 = "";
            
            if (clientno == 1)
            {
                dirPath = "C:/mywork/Client1";
                FILE_NAME = "C:/mywork/Client1/client.";
                FILE_NAME1 = "C:/mywork/ClientMerge/client1.";
            }
            
            else if (clientno == 2)
            {
                dirPath = "C:/mywork/Client2";
                FILE_NAME = "C:/mywork/Client2/client.";
                FILE_NAME1 = "C:/mywork/ClientMerge/client2.";
            }
            
            else if (clientno == 3)
            {
                dirPath = "C:/mywork/Client3";
                FILE_NAME = "C:/mywork/Client3/client.";
                FILE_NAME1 = "C:/mywork/ClientMerge/client3.";
            }
            
            else if (clientno == 4)
            {
                dirPath = "C:/mywork/Client4";
                FILE_NAME = "C:/mywork/Client4/client.";
                FILE_NAME1 = "C:/mywork/ClientMerge/client4.";
            }
            
            else if (clientno == 5)
            {
                dirPath = "C:/mywork/Client5";
                FILE_NAME = "C:/mywork/Client5/client.";
                FILE_NAME1 = "C:/mywork/ClientMerge/client5.";
            }                               
    
                
            File[] files5 = new File(dirPath).listFiles();
            
            int counter2 = 0;
            for (int j = 0; j < files5.length; j ++)
                if (files5[j] != null)
                    counter2 ++;
            
            if (counter2 == chunks)
            {
                try {
                    new Handler().start();
                }
                catch(Exception e){
                    System.out.println("Exception found");
                    
                }
                
            }
                
            
        
            //merge files
            
            int counter1 = 0;
            
            while (true)
            {
                
                File[] files4 = new File(dirPath).listFiles();
                
                counter1 = 0;
                for (int i = 0; i < files4.length; i ++)
                    if (files4[i] != null)
                        counter1 ++;
                if (counter1 == chunks)
                {
                    break;
                }
            }
            
            if (counter1 == chunks)
            {
                System.out.println("Merging files");
                FILE_NAME = FILE_NAME.concat(ftype);
                
                FILE_NAME1 = FILE_NAME1.concat(ftype);
                
                File ofile = new File(FILE_NAME1);
                FileOutputStream fos;
                FileInputStream fis;
                byte[] fileBytes;
                int bytesRead = 0;
                List<File> list = new ArrayList<File>();
                
                String s = ".part";
                
                for (int i = 0; i < chunks; i++)
                {
                    s = ".part";
                    String y = Integer.toString(i);
                    s = s.concat(y);
                    list.add(new File(FILE_NAME+s));
                }
                
                int k = 0;
                
                try {
                    fos = new FileOutputStream(ofile,true);
                    for (File file : list) {
                        fis = new FileInputStream(file);
                        fileBytes = new byte[(int) file.length()];
                        bytesRead = fis.read(fileBytes, 0,(int)  file.length());
                        assert(bytesRead == fileBytes.length);
                        assert(bytesRead == (int) file.length());
                        fos.write(fileBytes);
                        fos.flush();
                        fileBytes = null;
                        fis.close();
                        fis = null;
                        k++;
                    }
                    fos.close();
                    fos = null;
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
                
            }
            
            else
            {
                System.out.println("All packets not received yet..");
            }
        

    }
    

        
    }
    
    
    

