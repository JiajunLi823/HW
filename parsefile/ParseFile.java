package ljj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Dictionary {
    public static void main(String[] args)throws FileNotFoundException, IOException  {
   
        Scanner s= new Scanner(new BufferedReader(new  FileReader("shortwords.txt")));
        BufferedReader br= new BufferedReader(new FileReader("Frankenstein.txt"));
        HashMap <String,Integer> Dict1= new HashMap<>();
        HashMap <String,Integer> Dict2= new HashMap<>();
        while(s.hasNext()){
            Dict1.put(s.next(),0);
        }
       
        String frank; 
        String space=" ";
        String follow;
        String precede;
        String three;
   
        String temp;
        String temp2;
        String[] words;
       
        
        while((frank=br.readLine()) != null){
        
            words = frank.split("[ \\.\\?\\,\\:\\?\\!\\*\\#\\;\\-\\s]+");
            int len=words.length;
           
            
            for(int i=0;i<len;i++){
                if( Dict1.containsKey(words[i])  ){
                    if(i==0 & i+1<len){
                        follow=words[i]+space+words[i+1];
                        Integer count=Dict2.get(follow);
                        if (count==null){
                            Dict2.put(follow, 1);
                        }
                        else{
                            Dict2.put(follow, count+1);
                        }
                    }
                    if(i>0 & i+1<len){
                        three=words[i-1]+space+words[i]+space+words[i+1];
                        follow=words[i]+space+words[i+1];
                        precede=words[i-1]+space+words[i];
                        Integer count=Dict2.get(precede);
                        if (count==null){
                            Dict2.put(precede, 1);
                        }
                        else{
                            Dict2.put(precede, count+1);
                        }
                        Integer count2=Dict2.get(follow);
                        if (count2==null){
                            Dict2.put(follow, 1);
                        }
                        else{
                            Dict2.put(follow, count2+1);
                        }
                        Integer count3=Dict2.get(three);
                        if (count3==null){
                            Dict2.put(three, 1);
                        }
                        else{
                            Dict2.put(three, count3+1);
                        }
                    }
                    if(i>0 & i+1>=len){
                        precede=words[i-1]+space+words[i];
                        Integer count=Dict2.get(precede);
                        if (count==null){
                            Dict2.put(precede, 1);
                        }
                        else{
                            Dict2.put(precede, count+1);
                        }
                    }
                    else{
                        continue;
                    }
                    
                }
                else
                    continue;
            }
        }
        for(String word:Dict2.keySet()){
            int i=0;
            if((i=Dict2.get(word))>=50){
                System.out.println("'"+word+"'"+"("+i+" times) ");
            } 
        }
        
        
        
    }
}