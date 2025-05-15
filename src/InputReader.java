import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class InputReader {
    
    //Klassvariabel
    private static ArrayList<InputStream> savedStreams = new ArrayList<>(); 


    //Instansvariabel
    private Scanner input;
    private InputStream inputStream;

    // Konstruktorer
    public InputReader(){
        this(System.in);
    }

    public InputReader(InputStream in) {
        if(savedStreams.contains(in)){ 
            throw new IllegalStateException("Felmeddelande");
        }
        else{
            input = new Scanner(in);
            savedStreams.add(in);
            inputStream = in;
            //readerSaved = in;
            //readerActivated = true;
        }
    }



    // Metoder
    public void close(){
        input.close();
        savedStreams.remove(inputStream);
        //readerActivated = false;
    }
    

    public int readInt(String prompt){
        System.out.print(prompt + "?>");
        int answer = input.nextInt();
        input.nextLine(); // Töm inmatningsbufferten
        return answer;
    }

    public double readDouble(String prompt){
        System.out.print(prompt  + "?>");
        double answer = input.nextDouble();
        input.nextLine(); // Töm inmatningsbufferten
        return answer;
    }

    public String readString( String prompt){
        System.out.print(prompt  + "?>");
        String answer = input.nextLine().trim();
        
        return answer;


    }

    /* Metoderna för inläsning av tal måste också 
    tömma inmatningsbufferten (HR4.2)
    efter inläsning för att undvika problem med buffring.
    */
}
