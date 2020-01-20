import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by patry on 17.11.2017.
 */
public class App {

    public static void main(String[] args) throws IOException {

        String directoryPath = "D:/gutenberg_project/";
        File file = new File(directoryPath);
        File[] listOfFiles = file.listFiles();

        List<String> foldersWithBooks = new ArrayList<String>();
        for (int i = 0; i<10; i++){
            foldersWithBooks.add(String.valueOf(i));
        }
        System.out.println(foldersWithBooks);

        for (String folder : foldersWithBooks){

        }


//        for(File singleFile : listOfFiles){
//            System.out.println(singleFile.getName());
//        }

//        for (File singleFile : listOfFiles){
//            if(singleFile.isFile() == true){
//                System.out.println(singleFile.getName() + " is a file.");
//                int index = singleFile.getName().lastIndexOf(".");
//                System.out.println(singleFile.getName().substring(index));
//                System.out.println(singleFile.getAbsolutePath());
//                System.out.println(singleFile.getCanonicalPath());
//            } else if (singleFile.isDirectory()){
//                System.out.println(singleFile.getName() + " is a directory.");
//            }
//        }

    }

}
