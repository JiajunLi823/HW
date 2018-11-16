package ljj;
import com.sun.xml.internal.bind.v2.TODO;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class ParseFile {
    public static void main(String[] args)throws FileNotFoundException, IOException  {
   
        Scanner s= new Scanner(new BufferedReader(new  FileReader("shortwords.txt")));
        HashMap <String,Integer> Shortsave= new HashMap<>();
        HashMap <String,Integer> Anssave= new HashMap<>();
        
        while(s.hasNext()){
        	Shortsave.put(s.next(),0);
        }
        
        BufferedReader br= new BufferedReader(new FileReader("Frankenstein.txt"));
        String line;
        String[] words;
        int len = 0;
        String save;
        int i = 0;
        
        while((line = br.readLine()) != null){
            words = line.split("[ \\.\\?\\,\\:\\?\\!\\*\\#\\;\\-\\s]+");
            len = words.length;
            String sp = " ";
            
            if (len == 0)
                continue;
            
            for(i = 0; i < len; ++i){
                if( Shortsave.containsKey(words[i])  ){
                    if(i == 0 & i + 1 < len)
                    	save = words[i] + sp + words[i + 1];
                    if(i == 0 & i + 1 >= len)
                    	save = words[i];
                    if(i > 0 & i + 1 < len)
                    	save = words[i - 1]+ sp + words[i] + sp + words[i + 1];
                    if(i > 0 & i + 1 >= len)
                    	save = words[i - 1] + sp + words[i];
                    else
                        continue;
                    Integer count=Anssave.get(save);
                    if (count == null)
                    	Anssave.put(save, 1);
                    else
                    	Anssave.put(save, count + 1);
                    
                }
                else
                    continue;
            }
        }
        
        for(String w:Anssave.keySet()){
            int a = 0;
            if((a = Anssave.get(w)) >= 50){
                System.out.println(w + "==>" + a);
            } 
        }
        
        
        
    }
}